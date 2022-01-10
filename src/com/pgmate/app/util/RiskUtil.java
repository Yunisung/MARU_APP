package com.pgmate.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

public class RiskUtil extends Thread {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.RiskUtil.class);
	private String trxId = "";

	public RiskUtil(String trxId) {
		this.trxId = trxId;
	}

	@Override
	public void run(){
		logger.info("리스크 거래 확인 {}", trxId);
		if(CommonUtil.isNullOrSpace(trxId)) {
			return;
		}
		
		// 중복거래 체크 (합이 20만원 이상) 매입상태 거래.
		duplicationCheck(trxId);
		// 10만원 단위 거래 체크 (20만원 이상)
		// 40만원 이상 거래
		divisibleByHundredCheck(trxId);
		logger.info("리스크 거래 확인 종료");
	}
	
	private void duplicationCheck(String trxId) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_TRX_PAY A JOIN PG_TRX_PAY B ON "
				+ "date_add(A.regDate, interval -24 hour) < B.regDate "
				+ "AND A.regDate > B.regDate "
				+ "AND A.last4 = B.last4 "
				+ "AND A.tmnId = B.tmnId ");
		dao.setColumns("A.mchtId, A.tmnId, B.trxId as trxId, A.amount as amount, B.amount as dupAmount, IF(A.amount + B.amount >= 200000, 1 ,0) as dup");
		dao.setWhere("A.last4 <> ''");
		dao.addWhere("A.trxId", trxId, CPDAO.eq);
		dao.setOrderBy("");
		SharedMap<String, Object> resMap = dao.search().getRow(0);
		if(resMap != null && resMap.size() > 0) {
			String resString = "중복거래: "+resMap.getString("trxId")+" 거래와 중복된 카드 거래, 금액: " + resMap.getString("amount") +", "+ resMap.getString("dupAmount");
			logger.debug(resString);
			
			new CPDAO().query("INSERT INTO PG_TRX_RISK SET trxId='" + resMap.getString("trxId") + "' tmnId='" +resMap.getString("tmnId")+ "' riskCode='0001' "
					+" mchtId='"+resMap.getString("mchtId")+"' summary='"+resString+"' regDay='" + CommonUtil.getCurrentDate("yyyyMMdd")+ "' ");
		}
	}
	
	private void divisibleByHundredCheck(String trxId) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_TRX_PAY");
		dao.setColumns("mchtId, tmnId, trxId, amount,"
				+ "IF(amount >= 200000 AND MOD(amount, 100000) = 0, 1, 0) as divisible,"
				+ "IF(amount >= 400000, 1, 0) as maxAmount");
		dao.setWhere("last4 <> ''");
		dao.addWhere("trxId", trxId, CPDAO.eq);
		dao.setOrderBy("");
		SharedMap<String, Object> resMap = dao.search().getRow(0);
		if(resMap.getLong("divisible") > 0) {
			String summary = "10만원 단위거래: 금액: " + resMap.getString("amount");
			logger.debug(summary);
			
			new CPDAO().query("INSERT INTO PG_TRX_RISK SET trxId='" + resMap.getString("trxId") + "', tmnId='" +resMap.getString("tmnId")+ "', riskCode='0002' "
					+", mchtId='"+resMap.getString("mchtId")+"', summary='"+summary+"', regDay='" + CommonUtil.getCurrentDate("yyyyMMdd")+ "' ");
		}
		 
		if(resMap.getLong("maxAmount") > 0) {
			String summary = "40만원 이상거래: 금액: " + resMap.getString("amount");
			logger.debug(summary);
			
			new CPDAO().query("INSERT INTO PG_TRX_RISK SET trxId='" + resMap.getString("trxId") + "', tmnId='" +resMap.getString("tmnId")+ "', riskCode='0003' "
					+", mchtId='"+resMap.getString("mchtId")+"', summary='"+summary+"', regDay='" + CommonUtil.getCurrentDate("yyyyMMdd")+ "' ");
		}
	}
}