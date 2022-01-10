package com.pgmate.app.dao;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

public class CashReceiptDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.dao.CashReceiptDAO.class);
	private static final String TABLE = "PG_CASH_RECEIPT";
	private static final String COLUMNS = "*";
	
	public CashReceiptDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CashReceiptDAO.COLUMNS);
	}

	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("(SELECT A.*, B.name FROM PG_CASH_RECEIPT A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A ");
		super.setColumns("A.cashId, A.trxId, A.mchtId, A.name, A.stateCd, IF(A.cashType = '0', '가상계좌',  IF(A.cashType = '1', '기타/단 건', '')) AS cashType, A.authNo, "
				+ "IF(A.assort = '0', '승인',  IF(A.assort = '1', '취소', '')) AS assort, "
				+ "A.transNo, A.amt, A.vat, A.svcAmt, "
				+ "A.ordNm, A.purpose, A.identityGb, A.identity, A.bizRegNo, A.deductionType, A.resultCd,"
				+ "A.resultMsg, A.trDt, A.trTime, IF(A.taxYn = 'N', '과세', IF(A.taxYn = 'Y', '면세', IF(A.taxYn = 'G', '복합과세', ''))) AS taxYn, "
				+ "IF(A.identityGb = '1', '현금영수증 카드', IF(A.identityGb = '3', '사업자번호', IF(A.identityGb = '4', '휴대전화번호', ''))) AS identityGb, "
				+ "IF(A.purpose = '0', '소득공제', IF(A.purpose = '1', '지출증빙', '')) AS purpose, "
				+ "A.orgAuthNo, A.orgTrDt, A.errCd, A.regDay, A.regTime, A.regDate");
		super.setOrderBy("A.regDate desc");
		
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
	}
	
	public RecordSet getByCashId(String cashId){
		super.setTable("(SELECT A.*, B.name FROM PG_CASH_RECEIPT A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A ");
		super.setColumns("A.cashId, A.trxId, A.mchtId, A.name, A.stateCd, IF(A.cashType = '0', '가상계좌', '단 건/기타')AS cashType, A.authNo, "
				+ "IF(A.assort = '0', '승인', '취소')AS assort, A.transNo, IF(A.taxYn = 'N', '과세', IF(A.taxYn = 'Y', '면세', IF(A.taxYn = 'G', '복합과세', ''))) AS taxYn, "
				+ "A.amt, A.vat, A.svcAmt, A.ordNm, "
				+ "IF(A.identityGb = '1', '현금영수증 카드', IF(A.identityGb = '3', '사업자번호', IF(A.identityGb = '4', '휴대전화번호', ''))) AS identityGb, "
				+ "FN_AES_DEC(A.identity) AS identity, A.bizRegNo, IF(A.resultCd = '0000', '성공', '실패')AS resultCd, "
				+ "IF(A.deductionType = 'Y', '대중교통', IF(A.deductionType = 'C', '도서/공연비', '')) AS deductionType, "
				+ "IF(A.errCd = '', '대기', IF(A.errCd = '0000', '성공', A.errCd)) AS errCd, "
				+ "IF(A.purpose = '0', '소득공제', IF(A.purpose = '1', '지출증빙', '')) AS purpose, "
				+ "A.resultMsg, A.trDt, A.trTime, A.orgAuthNo, A.orgTrDt, A.regDay, A.regTime, A.regDate");
		
		if(cashId.startsWith("F")) {
			super.addWhere("authNo",cashId,eq);
		} else {
			super.addWhere("cashId",cashId,eq);
		}
		return search();
	}
	
	public RecordSet getByorgAuthNo(String orgAuthNo){
		super.addWhere("authNo",orgAuthNo,eq);
		return search();
	}
	
	public String getCashId() {
		return "CASH" + getFunction("FN_NEXTVAL2", "CASH");
	}
	
	String cashId = getCashId();
	
	public boolean insertReceipt(SharedMap<String, Object> sharedMap) {
		boolean insertFlag = false;
		
		try {
			super.setTable("PG_CASH_RECEIPT");
			super.setRecord("cashId", sharedMap.getString("cashId"));
			super.setRecord("trxId", sharedMap.getString("trxId"));
			super.setRecord("mchtId", sharedMap.getString("mchtId"));
			super.setRecord("mid", sharedMap.getString("mid"));
			super.setRecord("stateCd", sharedMap.getString("stateCd"));
			super.setRecord("cashType", sharedMap.getString("cashType"));
			super.setRecord("authNo", sharedMap.getString("authNo"));
			super.setRecord("assort", sharedMap.getString("assort"));
			super.setRecord("transNo", sharedMap.getString("transNo"));
			super.setRecord("taxYn", sharedMap.getString("taxYn"));
			super.setRecord("amt", sharedMap.getString("amt"));
			super.setRecord("vat", sharedMap.getString("vat"));
			super.setRecord("svcAmt", sharedMap.getString("svcAmt"));
			super.setRecord("ordNm", sharedMap.getString("ordNm"));
			super.setRecord("purpose", sharedMap.getString("purpose"));
			super.setRecord("identityGb", sharedMap.getString("identityGb"));
			super.setRecord("identity", sharedMap.getString("identity"));
			super.setRecord("bizRegNo", sharedMap.getString("bizRegNo"));
			super.setRecord("deductionType", sharedMap.getString("deductionType"));
			super.setRecord("resultCd", sharedMap.getString("resultCd"));
			super.setRecord("resultMsg", sharedMap.getString("resultMsg"));
			super.setRecord("trDt", sharedMap.getString("trDt"));
			super.setRecord("trTime", sharedMap.getString("trTime"));
			super.setRecord("orgAuthNo", sharedMap.getString("orgAuthNo"));
			super.setRecord("orgTrDt", sharedMap.getString("orgTrDt"));
			super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
			super.setRecord("regTime", CommonUtil.getCurrentDate("HHmmss"));
			super.setRecord("regInfo", "ADMIN");
			super.setRecord("regDate", CommonUtil.getCurrentTimestamp());
			
			logger.info("set INSERT PG_CASH_RECEIPT : {}", super.insert());
			super.initRecord();
			insertFlag = true;
		} catch (Exception e) {
			insertFlag = false;e.getStackTrace();
			logger.error("insertReceipt TEST Exception : " + e.getMessage());
		}
		
		return insertFlag;
	}
	
	/**
	 * 현금영수증 결과 업데이트
	 * @param apiRes
	 * @return
	 */
	public void updateReceipt(JSONObject apiRes) {
		logger.info("Service ------ updateReceipt ------");
		
		String q = "UPDATE PG_CASH_RECEIPT SET stateCd='"+apiRes.get("stateCd")+"', authNo='"+apiRes.get("authNo")+"', "
				+ "resultCd='"+apiRes.get("resultCd")+"', resultMsg='"+apiRes.get("resultMsg")+"', orgAuthNo='"+apiRes.get("orgAuthNo")+"', "
				+ "orgTrDt='"+apiRes.get("orgTrDt")+"' WHERE cashId='"+apiRes.get("cashId")+"'";
		super.update(q);
	}
	
	public SharedMap<String,Object> getPayKeyByKey(String payKey) {
		String key = "PG_MCHT_TMN_" + payKey;
		logger.debug("load key : {}", key);

		super.setTable("PG_MCHT_TMN");
		super.setColumns("payKey");
		super.addWhere("payKey",payKey,eq);
		
		return super.search().getRow(0);
	}
	
	/*
	 * Authorization
	 */
	public SharedMap<String,Object> getPayKeyByMchtId(String mchtId) {
		String key = "PG_MCHT_TMN_" + mchtId;
		logger.debug("load key : {}", key);
		
		super.setTable("PG_MCHT_TMN");
		super.setColumns("payKey");
		super.addWhere("mchtId",mchtId,eq);
		super.addWhere("limit","1",eq);
		
		return super.search().getRow(0);
	}
	
	/**
	 * 현금영수증 사용여부 조회
	 * @param mchtId
	 * @return
	 */
	public SharedMap<String,Object> cashReceiptStatus(String mchtId) {
		super.setTable("PG_MCHT_SVC");
		super.addWhere("mchtId",mchtId,eq);
		super.setColumns("cashReceipt");
		
		return super.search().getRow(0);
	}
	
	/**
	 * 현금영수증 주문번호 조회
	 * @param mchtId
	 * @return
	 */
	public int duplicateCheck(String transNo) {
		String query = "SELECT COUNT(*) AS cnt FROM PG_CASH_RECEIPT WHERE transNo = '"+transNo+"' AND resultCd = '0000'";
		
		RecordSet rset = super.query(query);
		super.initRecord();
		return rset.getRowFirst().getInt("cnt");
	}
	
	/**
	 * trxId 조회
	 * @param mchtId
	 * @return
	 */
	public SharedMap<String,Object> duplicateTrxId(String trxId) {
		super.setTable("PG_CASH_RECEIPT");
		super.setColumns("COUNT(*) as cnt");
		super.addWhere("trxId",trxId,eq);
		super.addWhere("resultCd","0000",eq);
		
		return super.search().getRow(0);
	}
	
	/**
	 * 취소요청 데이터의 원거래 조회
	 * 
	 * @param cashId
	 * @return
	 */
	public SharedMap<String,Object> getOrgDataAuthNo(String authNo){
		super.setTable("PG_CASH_RECEIPT");
		super.setColumns("*");
		super.addWhere("authNo",authNo,eq);
		
		return super.search().getRow(0);
	}
}
