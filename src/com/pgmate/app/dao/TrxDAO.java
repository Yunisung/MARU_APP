package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.Card;

/**
 * @author Administrator
 *
 */
//KJM : 매입건에 대한 기능 (기본 테이블, 컬럼 안정해져있음)
public class TrxDAO extends DAO {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxDAO.class );
	
	public TrxDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public SharedMap<String, Object> getSalesMngById(String salesId) {
		super.setTable("PG_MAM_SALES_MNG");
		super.addWhere("salesId", salesId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0){
			return rset.getRow(0);
		}else{
			return new SharedMap<String,Object>();
		}
	}
	
	
	public SharedMap<String, Object> getOrgFee(String van) {
		
		super.setTable("PG_ORG_FEE");
		super.addWhere("van", van, eq);
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0){
			return rset.getRow(0);
		}else{
			return new SharedMap<String,Object>();
		}
	}
	
	
	public String getSettleDay(String today,int term) {	
		String start = CommonUtil.toString(term-1);
		String q = "SELECT days FROM PG_CODE_HOLIDAY WHERE days > '"+today+"' AND status ='no' limit "+start+",1";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("days");
	}
	
	
	public String getSettleDay(String today) {	
		String q = "SELECT days FROM PG_CODE_HOLIDAY WHERE days >= '"+today+"' AND status ='no' limit 1";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("days");
	}
	
	//KJM : 매입히스토리 수정
	//KJM : vanId가 FACTORING이 아닐 때
	public boolean updateTrxCapDtl(SharedMap<String,Object> updateMap){
		super.setTable("PG_TRX_CAP_DTL");
		super.setRecord("stlRate"	, updateMap.getDouble("stlRate"));	//KJM : 선정산 수수료
		super.setRecord("stlFee"	, updateMap.getLong("stlFee"));		//가맹점 수수료
		super.setRecord("stlFeeVat"	, updateMap.getLong("stlFeeVat"));	//가맹점 수수료 VAT
		super.setRecord("stlAmount"	, updateMap.getLong("stlAmount"));	//가맹점 정산금액
		/*
		if(!updateMap.isNullOrSpace("stlDay")){
			super.setRecord("stlDay"	, updateMap.getString("stlDay"));
		}*/
		super.setRecord("risk"	, updateMap.getString("risk"));		//리스크
		super.addWhere("capId", updateMap.getString("capId"));		//매입거래번호
		//KJM : update 쿼리문 수행
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	//KJM : 매입히스토리 수정
	//KJM : 가맹점 정산 유형이 D+1이 아닐 때
	public boolean updateTrxCapDtlToRisk(SharedMap<String,Object> updateMap){
		super.setTable("PG_TRX_CAP_DTL");
		//super.setRecord("stlRate"	, updateMap.getDouble("stlRate"));
		//super.setRecord("stlFee"	, updateMap.getLong("stlFee"));
		//super.setRecord("stlFeeVat"	, updateMap.getLong("stlFeeVat"));
		//super.setRecord("stlAmount"	, updateMap.getLong("stlAmount"));
		super.setRecord("risk"	, updateMap.getString("risk"));
		super.addWhere("capId", updateMap.getString("capId"));
		
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	//KJM : 매입히스토리 수정
	//KJM : vanId가 FACTORING일 때
	public boolean updateTrxCapDtlWithAgency(SharedMap<String,Object> updateMap){
		super.setTable("PG_TRX_CAP_DTL");
		super.setRecord("stlRate"	, updateMap.getDouble("stlRate"));	//KJM : 선정산 수수료
		super.setRecord("stlFee"	, updateMap.getLong("stlFee"));		//가맹점 수수료
		super.setRecord("stlFeeVat"	, updateMap.getLong("stlFeeVat"));	//가맹점 수수료 VAT
		super.setRecord("stlAmount"	, updateMap.getLong("stlAmount"));	//가맹점 정산금액
		super.setRecord("stlAgencyFee"	, updateMap.getLong("stlAgencyFee"));	//대리점 수수료
		/*
		if(!updateMap.isNullOrSpace("stlDay")){
			super.setRecord("stlDay"	, updateMap.getString("stlDay"));
		}*/
		super.setRecord("risk"	, updateMap.getString("risk"));		//리스크
		super.addWhere("capId", updateMap.getString("capId"));		//매입거래번호
		//KJM : update 쿼리문 수행
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	public boolean updateTrxCapDtlToRiskWithAgency(SharedMap<String,Object> updateMap){
		super.setTable("PG_TRX_CAP_DTL");
		//super.setRecord("stlRate"	, updateMap.getDouble("stlRate"));
		//super.setRecord("stlFee"	, updateMap.getLong("stlFee"));
		//super.setRecord("stlFeeVat"	, updateMap.getLong("stlFeeVat"));
		//super.setRecord("stlAmount"	, updateMap.getLong("stlAmount"));
		super.setRecord("stlAgencyFee"	, updateMap.getLong("stlAgencyFee"));
		super.setRecord("risk"	, updateMap.getString("risk"));
		super.addWhere("capId", updateMap.getString("capId"));
		
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	//KJM : 매입 삭제
	public boolean deleteTrxCap(String capId){
		//KJM : DELETE FROM PG_TRX_CAP WHERE capId IN (값1,값2,...)
		super.setTable("PG_TRX_CAP");
		super.addWhere("capId", capId,in);
		
		//KJM : 쿼리문 수행결과에 따라 true, false 반환값 받음
		boolean updated = super.delete();
		super.initRecord();
		return updated;
	}
	
	
	//KJM : 정산일자 변경
	public boolean updateDay(String capId,String stlDay,String stlVanDay){
		super.setTable("PG_TRX_CAP_DTL");
		super.setRecord("stlDay"	, stlDay);
		super.setRecord("stlVanDay"	, stlVanDay);
		super.addWhere("capId", capId);
		
		//KJM : update 쿼리문 수행
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	//KJM : ONLINE 거래 생성 INSERT
	public long insertTrxLoad(SharedMap<String,Object> map){
		
		
		super.setTable("PG_TRX_LOAD");
		
		super.setRecord("mchtId"	, map.getString("mchtId"));
		super.setRecord("tmnId"		, map.getString("tmnId"));
		super.setRecord("amount"	, map.getLong("amount"));
		super.setRecord("cnt"		, map.getLong("cnt"));
		super.setRecord("van"		, map.getString("van"));
		super.setRecord("vanId"		, map.getString("vanId"));
		super.setRecord("vanType"	, map.getString("vanType"));
		super.setRecord("rootTrxId"	, map.getString("rootTrxId"));
		super.setRecord("status"	, map.getString("status"));
		super.setRecord("summary"	, map.getString("summary"));
		super.setRecord("regId"		, map.getString("regId"));
		super.setRecord("regDay"	, map.getString("regDay"));
		
		//KJM : 추가된 인덱스 값
		long idx = super.insertAndLastIdx();
		super.initRecord();
		return idx;
	}
	
	public void deleteTrxLoad(long idx){
		super.setTable("PG_TRX_LOAD");
		super.addWhere("idx"	, idx);
		super.delete();
		super.initRecord();
		
		return;
	}
	
	public boolean deleteTrxLoad2(long idx){
		super.setTable("PG_TRX_LOAD");
		super.addWhere("idx"	, idx);
		boolean deleted = super.delete();
		super.initRecord();
		
		return deleted;
	}
	
	//KJM : ONLINE 거래 생성 상세정보 INSERT
	public boolean insertTrxLoadDtl(SharedMap<String,Object> map){
		super.setTable("PG_TRX_LOAD_DTL");
		super.setRecord("batchIdx"	, map.getLong("batchIdx"));	
		super.setRecord("trnType"	, map.getString("trnType"));
		super.setRecord("trackId"	, map.getString("trackId"));
		super.setRecord("amount"	, map.getLong("amount"));
		super.setRecord("installment", map.getString("installment"));
		super.setRecord("cardType"	, map.getString("cardType"));
		super.setRecord("bin"		, map.getString("bin"));
		super.setRecord("last4"		, map.getString("last4"));
		super.setRecord("authCd"	, map.getString("authCd"));
		super.setRecord("trxDay"	, map.getString("trxDay"));
		super.setRecord("trxTime"	, map.getString("trxTime"));
		super.setRecord("rootTrxDay", map.getString("rootTrxDay"));
		super.setRecord("vanTrxId"	, map.getString("vanTrxId"));
		super.setRecord("vanDay"	, map.getString("vanDay"));
		super.setRecord("vanStlFee"	, map.getLong("vanStlFee"));
		super.setRecord("exeStatus"	, map.getString("exeStatus"));
		super.setRecord("regId"		, map.getString("regId"));
		super.setRecord("regDay"	, map.getString("regDay"));
		
		//KJM : insert 수행 후 정상수행(true), 실패(flase) 반환
		boolean updated = super.insert();
		super.initRecord();
		return updated;
	}
	
	
	public boolean updateTrxLoad(long idx ,String status){
		super.setTable("PG_TRX_LOAD");
		super.setRecord("status"	, status);
		super.addWhere("idx", idx);
		
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	public boolean updateTrxLoad2(SharedMap<String, Object> map){
		super.setTable("PG_TRX_LOAD");
		super.setRecord("mchtId"	, map.getString("mchtId"));
		super.setRecord("tmnId"		, map.getString("tmnId"));
		super.setRecord("amount"	, map.getLong("amount"));
		super.setRecord("cnt"		, map.getLong("cnt"));
		super.setRecord("van"		, map.getString("van"));
		super.setRecord("vanId"		, map.getString("vanId"));
		super.setRecord("vanType"	, map.getString("vanType"));
		super.setRecord("rootTrxId"	, map.getString("rootTrxId"));
		super.setRecord("status"	, map.getString("status"));
		super.setRecord("summary"	, map.getString("summary"));
		super.setRecord("regId"		, map.getString("regId"));
		super.setRecord("regDay"	, map.getString("regDay"));
		super.addWhere("idx", map.getString("idx"));
		
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	public boolean updateTrxLoadDtl(SharedMap<String,Object> map){
		super.setTable("PG_TRX_LOAD_DTL");
		super.setRecord("trnType"	, map.getString("trnType"));
		super.setRecord("trackId"	, map.getString("trackId"));
		super.setRecord("amount"	, map.getLong("amount"));
		super.setRecord("installment", map.getString("installment"));
		super.setRecord("cardType"	, map.getString("cardType"));
		super.setRecord("bin"		, map.getString("bin"));
		super.setRecord("last4"		, map.getString("last4"));
		super.setRecord("authCd"	, map.getString("authCd"));
		super.setRecord("trxDay"	, map.getString("trxDay"));
		super.setRecord("trxTime"	, map.getString("trxTime"));
		super.setRecord("rootTrxDay", map.getString("rootTrxDay"));
		super.setRecord("vanTrxId"	, map.getString("vanTrxId"));
		super.setRecord("vanDay"	, map.getString("vanDay"));
		super.setRecord("vanStlFee"	, map.getLong("vanStlFee"));
		super.setRecord("exeStatus"	, map.getString("exeStatus"));
		super.setRecord("regId"		, map.getString("regId"));
		super.setRecord("regDay"	, map.getString("regDay"));
		super.addWhere("batchIdx", map.getString("batchIdx"));
		
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	
	public SharedMap<String,Object> getDBIssuer(String bin){
		SharedMap<String,Object> issuerMap = new SharedMap<String,Object>();
		if(CommonUtil.isNullOrSpace(bin)){
			return issuerMap;
		}
		
		super.setTable("PG_CODE_BIN");
		super.addWhere("bin", bin, eq);
		super.setColumns("*");
		RecordSet rset = super.search();
		super.initRecord();
	
		if(rset.size() == 0){
			issuerMap.put("bin", bin);
			issuerMap.put("issuer", "기타");
			issuerMap.put("type", "신용");
			return issuerMap;
		}else{
			issuerMap = rset.getRowFirst();
		}
		return issuerMap;
	}
	
	
	public void insertCard(String cardId, String value) {

		super.setTable("PG_TRX_BOX");

		super.setRecord("cardId", cardId);//1개
		super.setRecord("value", value);
		super.insert();
		super.initRecord();
	}
	
	
	public boolean updateBin(String table,String id, Card card){
		super.setTable(table);
		super.setRecord("bin"	, card.bin);
		super.setRecord("last4"	, card.last4);
		super.setRecord("issuer"	, card.issuer);
		super.setRecord("cardType"	, card.cardType);
		super.setRecord("cardId"	, card.cardId);
		super.setRecord("acquirer"	, card.acquirer);
		if(table.equals("PG_TRX_PAY")){
			super.addWhere("trxId", id);
		}else if(table.equals("PG_TRX_CAP")){
			super.addWhere("capId", id);
		}
		
		boolean updated = super.update();
		super.initRecord();
		return updated;
	}
	
	
	public static String getFunction(String function, String value) {
		String returnVal = "";
		String query = "SELECT " + function + "(?) as val";

		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rset = null;

		try {

			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, value);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				returnVal = rset.getString(1);
			}
			conn.commit();
		} catch (Exception t) {
			
		} finally {
			db.close(conn, pstmt, rset);
		}
		return returnVal;
	}
	
	//KJM : 정산번호 생성 후 리턴
	public synchronized static String getSettleId() {
		return "S" + getFunction("FN_NEXTVAL2", "SETTLE");
		//KJM : FN_NEXTVAL2 함수 => 오늘날짜(000000), 6자리의 자동 증가값(000000)을 합쳐서 리턴해줌
	}
	
	//KJM : 지급보류아이디 생성 후 리턴
	public synchronized static String getHoldId() {
		return "S" + getFunction("FN_NEXTVAL2", "HOLD");
	}

	public synchronized static String getIssueId() {
		return "VI" + getFunction("FN_NEXTVAL2", "VACT_ISSUE");
	}

	public RecordSet getCapIdByCollectId(String collectId) {
		super.setTable("PG_COLLECT_SETTLE_IDX");
		super.setColumns("capId");
		super.addWhere("collectId", collectId);
		super.setOrderBy("");
		return super.search();
	}
	public RecordSet getRiskCapIdByCollectId(String collectId) {
		super.setTable("PG_COLLECT_SETTLE_RISK_IDX");
		super.setColumns("capId");
		super.addWhere("collectId", collectId);
		super.setOrderBy("");
		return super.search();
	}
	
	public RecordSet getCapIdByDiffCollectId(String collectId) {
		super.setTable("PG_COLLECT_DIFF_IDX");
		super.setColumns("capId");
		super.addWhere("collectId", collectId);
		super.setOrderBy("");
		return super.search();
	}
	public RecordSet getRiskCapIdByDiffCollectId(String collectId) {
		super.setTable("PG_COLLECT_DIFF_RISK_IDX");
		super.setColumns("capId");
		super.addWhere("collectId", collectId);
		super.setOrderBy("");
		return super.search();
	}
	
	public int updateCollectedCapDtl(String collectId) {
		return updateCollectedCapDtl(collectId, false);
	}
	public int updateCollectedCapDtl(String collectId, boolean isRisk) {
		RecordSet rest = null;
		if(!isRisk) {
			rest = getCapIdByCollectId(collectId);
		} else {
			rest = getRiskCapIdByCollectId(collectId);
		}
		
		if(rest.size() < 1) {
			return 0;
		}
		
		int inserted = 0;
		logger.debug("update TRX_CAP_DTL batch : {} (IS RISK: {})", rest.size(), isRisk);
		String query = "UPDATE PG_TRX_CAP_DTL SET vanStatus='입금완료' WHERE capId=?";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 30;
			int count = 0;

			for (SharedMap<String, Object> map : rest.getRows()) {
				pstmt.setString(1, map.getString("capId"));
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("update batch PG_TRX_CAP_DTL error : {} (IS RISK: {})", CommonUtil.getExceptionMessage(e), isRisk);
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
	public int updateCollectedDiffCapDtl(String collectId) {
		return updateCollectedDiffCapDtl(collectId, false);
	}
	public int updateCollectedDiffCapDtl(String collectId, boolean isRisk) {
		RecordSet rest = null;
		if(!isRisk) {
			rest = getCapIdByDiffCollectId(collectId);
		} else {
			rest = getRiskCapIdByDiffCollectId(collectId);
		}
		
		if(rest.size() < 1) {
			return 0;
		}
		
		int inserted = 0;
		logger.debug("update TRX_CAP_DTL batch : {} (IS RISK: {})", rest.size(), isRisk);
		String query = "UPDATE PG_TRX_CAP_DTL SET stlDiffStatus='입금완료' WHERE capId=?";
		
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			
			int batchSize = 30;
			int count = 0;
			
			for (SharedMap<String, Object> map : rest.getRows()) {
				pstmt.setString(1, map.getString("capId"));
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			
			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("update batch PG_TRX_CAP_DTL error : {} (IS RISK: {})", CommonUtil.getExceptionMessage(e), isRisk);
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
	
	public RecordSet getByTmnId(String tmnId) {
		super.setTable("PG_MCHT_TMN A join PG_MCHT B on A.mchtId = B.mchtId join PG_MAM_AGENCY C on B.agencyId = C.agencyId");
		super.setColumns("A.tmnId as tmnId, B.mchtId as mchtId, B.name as mchtName, C.agencyId as agencyId, C.name as agencyName");
		super.addWhere("tmnId", tmnId);
		super.setOrderBy("");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
}
