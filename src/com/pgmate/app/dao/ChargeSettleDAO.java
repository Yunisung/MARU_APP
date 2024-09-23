package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.pgmate.lib.util.lang.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class ChargeSettleDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.ChargeSettleDAO.class );
	private static final String TABLE = "VW_CHARGE_SETTLE";
	private static final String COLUMNS = "*";
	
	
	public ChargeSettleDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(ChargeSettleDAO.COLUMNS);
	}
	
	public RecordSet getById(String trxId){
		super.setColumns("trxId, name, mchtId, trxType, trxUnit, trxDay, trxTime, amount, fee, feeVat, bankFee, netAmount, balance, trackId, refId, bankCd, bankName, "
				+ "FN_AES_DEC(account) as account, FN_AES_DEC(holder) as holder, recordInfo, summary, regId, regDay, regDate");
		addWhere("trxId", trxId, eq);
		return search();
	}
	
	public RecordSet getByMchtId(String memberId){ 
		addWhere("mchtId",memberId,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);

		super.setTable("(SELECT D.*, (SELECT codeName FROM PG_CODE WHERE alias='BANK' AND code=D.vactBankCd) AS vactBankName " +
				"FROM (SELECT A.*, CASE WHEN A.trxType = '입금' THEN B.bankCd WHEN A.trxType = '출금' THEN C.bankCd END AS vactBankCd " +
				"FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN PG_VACT_TRX B ON A.trxId = B.vactId AND A.mchtId = B.mchtId AND A.trxType = '입금' AND A.trxUnit = '가상계좌정산' " +
				"LEFT OUTER JOIN PG_FIRM_TRX C ON A.refId = C.idx AND A.trxType = '출금' AND A.trxUnit = '펌뱅킹') D) E");
		super.setColumns("E.*");
		super.setOrderBy("trxId desc");

//		super.setTable("(SELECT A.*, B.vactBankCd FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN PG_MCHT_MNG_VACT B ON A.mchtId = B.mchtId) C");
//		super.setColumns("C.*");

//		super.setColumns("trxId, name, mchtId, trxType, trxUnit, trxDay, trxTime, amount, fee, feeVat, bankFee, netAmount, balance, trackId, refId, bankCd, bankName, "
//				+ "FN_AES_DEC(account) as account, FN_AES_DEC(holder) as holder, recordInfo, summary, regId, regDay, regDate, vactBankCd");

		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
		
	}

	public RecordSet listWithDecAccount(List<Data> datas, Page page){
		super.setDebug(true);
		page = CPUtil.correctPage(page);

		super.setTable("(SELECT D.*, FN_AES_DEC(account) as decAccount, FN_AES_DEC(holder) as decHolder," +
				"(SELECT codeName FROM PG_CODE WHERE alias='BANK' AND code=D.vactBankCd) AS vactBankName " +
				"FROM (SELECT A.*, CASE WHEN A.trxType = '입금' THEN B.bankCd WHEN A.trxType = '출금' THEN C.bankCd END AS vactBankCd, B.account as vactAccount " +
				"FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN PG_VACT_TRX B ON A.trxId = B.vactId AND A.mchtId = B.mchtId AND A.trxType = '입금' AND A.trxUnit = '가상계좌정산' " +
				"LEFT OUTER JOIN PG_FIRM_TRX C ON A.refId = C.idx AND A.trxType = '출금' AND A.trxUnit = '펌뱅킹' " +
				"LEFT OUTER JOIN VW_TRX_CAP_LIST F ON A.trxId=F.trxId " +
				"WHERE IFNULL(F.serviceType, '') != '월세앱' AND A.trxUnit != '월세앱정산') D) E");
		super.setColumns("E.*");
		super.setOrderBy("trxId desc");

		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색

	}
	
	public RecordSet sumAmount(List<Data> datas){
		super.setColumns("SUM(stlAmt) as stlAmt");
		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public boolean insertChargeSettle(SharedMap<String, Object> settleMap) {
		super.setTable("PG_CHARGE_SETTLE");
		super.setRecord("trxId", settleMap.getString("trxId"));
		super.setRecord("mchtId", settleMap.getString("mchtId"));
		super.setRecord("trxType", settleMap.getString("trxType"));
		super.setRecord("trxUnit", settleMap.getString("trxUnit"));
		super.setRecord("trxDay", settleMap.getString("trxDay"));
		super.setRecord("trxTime", settleMap.getString("trxTime"));
		super.setRecord("amount", Math.abs(settleMap.getLong("netAmount")));
		super.setRecord("fee", 0);
		super.setRecord("feeVat", 0);
		super.setRecord("bankFee", 0);
		super.setRecord("netAmount", Math.abs(settleMap.getLong("netAmount")));
		super.setRecord("balance", settleMap.getLong("balance"));
		super.setRecord("trackId", settleMap.getString("trackId"));
		super.setRecord("refId", settleMap.getString("refId"));
		super.setRecord("bankCd", "");
		super.setRecord("bankName", "");
		super.setRecord("account", "");
		super.setRecord("holder", "");
		super.setRecord("recordInfo", "");
		super.setRecord("summary", settleMap.getString("summary"));
		super.setRecord("regId", settleMap.getString("regId"));
		super.setRecord("regDay", settleMap.getString("regDay"));
		
		boolean result = super.insert();
		super.initRecord();
		return result;
	}

	public boolean insertChargeSettleForVact(SharedMap<String, Object> settleMap) {
		super.setTable("PG_CHARGE_SETTLE");
		super.setRecord("trxId", settleMap.getString("trxId"));
		super.setRecord("mchtId", settleMap.getString("mchtId"));
		super.setRecord("trxType", settleMap.getString("trxType"));
		super.setRecord("trxUnit", settleMap.getString("trxUnit"));
		super.setRecord("trxDay", settleMap.getString("trxDay"));
		super.setRecord("trxTime", settleMap.getString("trxTime"));
		super.setRecord("amount", Math.abs(settleMap.getLong("amount")));
		super.setRecord("fee", settleMap.getString("fee"));
		super.setRecord("feeVat", settleMap.getString("feeVat"));
		super.setRecord("bankFee", 0);
		super.setRecord("netAmount", Math.abs(settleMap.getLong("netAmount")));
		super.setRecord("balance", settleMap.getLong("balance"));
		super.setRecord("trackId", settleMap.getString("trackId"));
		super.setRecord("refId", settleMap.getString("refId"));
		super.setRecord("bankCd", "");
		super.setRecord("bankName", "");
		super.setRecord("account", "");
		super.setRecord("holder", "");
		super.setRecord("recordInfo", "");
		super.setRecord("summary", settleMap.getString("summary"));
		super.setRecord("regId", settleMap.getString("regId"));
		super.setRecord("regDay", settleMap.getString("regDay"));

		boolean result = super.insert();
		super.initRecord();
		return result;
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

	public synchronized String getChargeSettleTrxId() {
		return "CS" + getFunction("FN_NEXTVAL2", "TRN");
	}
	
	public SharedMap<String, Object> getMchtBalance(String mchtId){
		super.setTable("PG_MCHT_BALANCE");
		super.setColumns("*");
		super.addWhere("mchtId",mchtId,eq);
		super.setOrderBy("");
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	public RecordSet depositSum(List<Data> datas) {
		super.setColumns("SUM(if(trxType='출금',amount,0)) AS depositAmt, SUM(if(trxType='입금',amount,0)) AS withdrawAmt");
		super.addWhere("trxUnit", "월세앱정산", ne);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public int insertChargeSettleFirm(SharedMap<String,Object> insertChargeSettleFirm){
		int inserted = 0;
		String query = "insert into PG_CHARGE_SETTLE_FIRM_RESERVE (trxId, transferType, mchtId, trackId, pubDay, pubTime, status, retry, trxDay, trxTime, amount, fee, feeVat, bankFee, netAmount, balance, resultCd, resultMsg, refId, rootTrxId, account, bankCd, bankName, holder, recordInfo, regId, regDay)  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		DBManager db = null ;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result      =0;

		try{
			db 		= DBFactory.getInstance();
			conn	= db.getConnection();
			pstmt	= conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;

			int i=1;
			pstmt.setString(i++, insertChargeSettleFirm.getString("trxId"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("transferType"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("mchtId"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("trackId"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("pubDay"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("pubTime"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("status"));
			pstmt.setInt(i++   , insertChargeSettleFirm.getInt("retry"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("trxDay"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("trxTime"));
			pstmt.setLong(i++  , insertChargeSettleFirm.getLong("amount"));
			pstmt.setLong(i++  , insertChargeSettleFirm.getLong("fee"));
			pstmt.setLong(i++  , insertChargeSettleFirm.getLong("feeVat"));
			pstmt.setLong(i++  , insertChargeSettleFirm.getLong("bankFee"));
			pstmt.setLong(i++  , insertChargeSettleFirm.getLong("netAmount"));
			pstmt.setLong(i++  , insertChargeSettleFirm.getLong("balance"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("resultCd"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("resultMsg"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("refId"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("rootTrxId"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("account"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("bankCd"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("bankName"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("holder"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("recordInfo"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("regId"));
			pstmt.setString(i++, insertChargeSettleFirm.getString("regDay"));

			result = pstmt.executeUpdate();
			conn.commit();
		}catch(Exception e){
			logger.debug("insert batch chargeSettleFirm error : {}", CommonUtil.getExceptionMessage(e));
		}finally{
			db.close(pstmt);
			db.close(conn);
		}

		return result;
	}

	public int deleteStlFirmReserve(SharedMap<String,Object> capMap) {
		int deleted = 0;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		logger.info("delete PG_CHARGE_SETTLE_FIRM_RESERVE batch : {} )", capMap.size());
		String query = "DELETE FROM PG_CHARGE_SETTLE_FIRM_RESERVE WHERE refTrxId=?";

		try {
			int batchSize = 100;
			int count = 0;

			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, capMap.getString("trxId"));
			pstmt.addBatch();
			if (++count % batchSize == 0) {
				deleted += pstmt.executeBatch().length;
			}
			deleted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("delete batch PG_CHARGE_SETTLE_FIRM_RESERVE error : {})", com.pgmate.lib.util.lang.CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return deleted;
	}

}