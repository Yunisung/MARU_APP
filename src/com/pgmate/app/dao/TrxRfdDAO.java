package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.FitcollaboResult;
import com.pgmate.pay.bean.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class TrxRfdDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxRfdDAO.class );
	private static final String TABLE = "VW_TRX_RFD_LIST";
	private static final String COLUMNS = "*";

	public TrxRfdDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxRfdDAO.COLUMNS);
	}

	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByOrdTrxId(String orgTrxId){
		addWhere("orgTrxId",orgTrxId,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(RfdAmount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		RecordSet rset =  super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색
	}

	public RecordSet getByRootTrxId(String trxId) {
		super.setTable("PG_TRX_RFD");
		addWhere("rootTrxId", trxId);
		return super.search();
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
			logger.debug("sql error : {}, query : {}", t.getMessage(), query);
		} finally {
			db.close(conn, pstmt, rset);
		}
		return returnVal;
	}

	public synchronized static String getTrxId() {
		return "T" + getFunction("FN_NEXTVAL2", "TRN");
	}

	public void insertTrxRfd(SharedMap<String, Object> trxMap, String amount, String trxId) {
		String curDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
		super.setTable("PG_TRX_RFD");
		super.setXssChange(false);
		long vat = new Double(Long.parseLong(amount)*10 /110).longValue();
		super.setRecord("trxId", trxId);
		super.setRecord("mchtId", trxMap.getString("mchtId"));
		super.setRecord("tmnId", trxMap.getString("tmnId"));
		super.setRecord("trackId", trxMap.getString("trackId"));
		super.setRecord("status", "접수");
		super.setRecord("rfdType", "");
		super.setRecord("rfdAll", trxMap.getString("rfdAll"));
		super.setRecord("rfdAmount", -Long.parseLong(amount));
		super.setRecord("rfdVat", -vat);
		super.setRecord("cardId", trxMap.getString("cardId"));
		super.setRecord("bin", trxMap.getString("bin"));
		super.setRecord("issuer", trxMap.getString("issuer"));
		super.setRecord("acquirer", trxMap.getString("acquirer"));
		super.setRecord("last4", trxMap.getString("last4"));
		super.setRecord("rootTrnDay", trxMap.getString("regDay"));
		super.setRecord("rootTrxId", trxMap.getString("trxId"));
		super.setRecord("rootTrackId", trxMap.getString("trackId"));
		super.setRecord("rootAmount", trxMap.getLong("amount"));
		super.setRecord("rootVat", trxMap.getLong("vat"));
		super.setRecord("authCd", trxMap.getString("authCd"));
		super.setRecord("reqDay", curDate.substring(0, 8));
		super.setRecord("reqTime", curDate.substring(8));

		super.setRecord("regDay", curDate.substring(0, 8));
		super.setRecord("regTime", curDate.substring(8));
		super.setRecord("regDate", curDate);
		logger.info("set TRX_RFD : {}", super.insert());
		super.initRecord();
	}

	public void updateTrxRfd(SharedMap<String, Object> trxMap, FitcollaboResult resp, String trxId) {
		String curDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
		super.setTable("PG_TRX_RFD");
		super.setXssChange(false);
		if (resp.resultCode.equals("0000")) {
			super.setRecord("status", "완료");
		} else {
			super.setRecord("status", "실패");
		}
		super.setRecord("resultCd", resp.resultCode);
		super.setRecord("resultMsg", "[" + resp.resultMessage + "]");
		// 취소주문번호 = 핏콜라보 취소거래번호
		super.setRecord("trackId", resp.resultData.cancelTid);
		super.setRecord("van", trxMap.getString("van"));
		super.setRecord("vanId", trxMap.getString("vanId"));
		super.setRecord("vanTrxId", resp.resultData.cancelPgTno);
		super.setRecord("vanResultCd", resp.resultCode);
		super.setRecord("vanResultMsg", resp.resultMessage);
		super.setRecord("regDay", curDate.substring(0, 8));
		super.setRecord("regTime", curDate.substring(8));
		super.setRecord("regDate", curDate);
		super.addWhere("trxId", trxId);
		logger.info("set TRX_RFD : {}", super.update());
		super.initRecord();
	}
}