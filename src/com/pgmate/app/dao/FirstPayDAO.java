package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.FirstHead;

/**
 * @author Administrator
 *
 */
public class FirstPayDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.FirstPayDAO.class );
	
	public FirstPayDAO() {
		// TODO Auto-generated constructor stub
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

	
	public boolean insertFirstPayIO(FirstHead head,String reqData) {
		super.setTable("PG_TRX_LOAD_FIRSTPAY");
		super.setRecord("trxId", head.getTrxId());
		super.setRecord("headerName", head.getHeaderName());
		super.setRecord("transNo", head.getTransNo());
		super.setRecord("terminalId", head.getTerminalId());
		super.setRecord("vanUniqueId", head.getFiller1());
		super.setRecord("recvDay", head.getTrnDate().substring(0,8));
		super.setRecord("recvTime", head.getTrnDate().substring(8));
		super.setRecord("trxType", head.getTrxType());
		super.setRecord("reqData", reqData);
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		boolean b =super.insert();
		super.initRecord();
		return b;
	}
	
	//WH
	public SharedMap<String, Object> getMchtTmnByTmnId(String tmnId) {
		super.setTable("PG_MCHT_TMN");
		super.setColumns("*");
		super.addWhere("tmnId", tmnId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	public boolean isDuplicatedVanTrxId(String van,String vanTrxId, String trnDate) {
		super.setTable("PG_TRX_RES");
		super.setColumns("*");
		super.addWhere("van", van);
		super.addWhere("vanTrxId", vanTrxId);
		super.addWhere("regDay", trnDate.substring(0, 8));
		super.addWhere("regTime", trnDate.substring(8));
		RecordSet rset = super.search();
		super.initRecord();
		if (rset.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public SharedMap<String, Object> getTrxByVanTrxId(String van,String vanTrxId) {
		super.setDebug(false);
		super.setTable("PG_TRX_PAY");
		super.setColumns("*");
		super.addWhere("van", van, eq);
		super.addWhere("vanTrxId", vanTrxId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRow(0);

	}
	
	public boolean isDuplicatedRFDVanTrxId(String van,String vanTrxId) {
		super.setTable("PG_TRX_RFD");
		super.setColumns("*");
		super.addWhere("van", van);
		super.addWhere("vanTrxId", vanTrxId);
		RecordSet rset = super.search();
		super.initRecord();
		if (rset.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public SharedMap<String, Object> getMchtByMchtId(String mchtId) {
		super.setTable("PG_MCHT");
		super.setColumns("*");
		super.addWhere("mchtId", mchtId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}
	
	public SharedMap<String, Object> getTrxRfdByTrxId(String trxId) {

		super.setTable("PG_TRX_RFD");
		super.setColumns("*");
		super.addWhere("rootTrxId", trxId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRow(0);
	}

	public long getTrxRefundSumByTrxId(String trxId) {

		super.setTable("PG_TRX_CAP");
		super.setColumns(" SUM(amount) as AMT ");
		super.addWhere("rootTrxId", trxId, eq);
		RecordSet rset = super.search();
		super.setColumns("*");
		super.initRecord();
		return rset.getRow(0).getLong("AMT");

	}
	
	public SharedMap<String,Object> getDBIssuer(String bin){
		SharedMap<String,Object> issuerMap = new SharedMap<String,Object>();
		
		super.setTable("PG_CODE_BIN");
		super.setColumns("*");
		super.addWhere("bin", bin, eq);
		RecordSet rset = super.search();
		super.initRecord();
		
		if(rset.size() == 0){
			issuerMap.put("bin", bin);
			issuerMap.put("issuer", "기타");
			issuerMap.put("type", "신용");
			return issuerMap;
		}else {
			return rset.getRowFirst();
		}
		
	}
	
	public SharedMap<String, Object> getTrxByOriTrxId(String van,String tmnId, String authCd, String regDay,String bin) {
		super.setDebug(true);
		super.setTable("PG_TRX_PAY");
		super.setColumns("*");
		super.addWhere("van", van, eq);
		super.addWhere("tmnId", tmnId, eq);
		super.addWhere("authCd", authCd, eq);
		super.addWhere("regDay", regDay, eq);
		super.addWhere("bin", bin, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRow(0);

	}
	
	public static String getIssuer(String issuer) {
		if (issuer.startsWith("KB") || issuer.indexOf("국민") > -1 || issuer.indexOf("신세계한미") > -1) {
			return "국민";
		} else if (issuer.startsWith("NH") || issuer.indexOf("농협") > -1) {
			return "농협";
		} else if (issuer.indexOf("롯데") > -1) {
			return "롯데";
		} else if (issuer.indexOf("삼성") > -1) {
			return "삼성";
		} else if (issuer.indexOf("신한") > -1) {
			return "신한";
		} else if (issuer.indexOf("비씨") > -1 || issuer.indexOf("BC") > -1 || issuer.indexOf("신세계한미") > -1) {
			return "비씨";
		} else if (issuer.indexOf("현대") > -1) {
			return "현대";
		} else if (issuer.indexOf("하나") > -1 || issuer.indexOf("외환") > -1) {
			return "하나";
		} else {
			if (issuer.indexOf("광주") > -1 || issuer.indexOf("제주") > -1 || issuer.indexOf("강원") > -1 || issuer.indexOf("조흥") > -1 || issuer.indexOf("신한") > -1) {
				return "신한";
			} else if (issuer.indexOf("우리") > -1 || issuer.indexOf("전북") > -1 || issuer.indexOf("수협") > -1 || issuer.indexOf("씨티") > -1 || issuer.indexOf("산업") > -1 || issuer.indexOf("기업") > -1 || issuer.indexOf("시티") > -1
					|| issuer.indexOf("우체국") > -1 || issuer.indexOf("신협") > -1 || issuer.indexOf("새마을") > -1) {
				return "비씨";
			} else {
				logger.info("UNKNOWN ISSUER : {}", issuer);
				return "";
			}
		}
	}
	
	public void insertCard(String cardId, String value) {

		super.setTable("PG_TRX_BOX");
		logger.info("set card id : {}", cardId);
		super.setColumns("*");
		super.setRecord("cardId", cardId);//1개
		super.setRecord("value", value);
		logger.info("set card : {}", super.insert());
		super.initRecord();

	}
	public void insertTrxREQ(SharedMap<String, Object> sharedMap) {

		super.setTable("PG_TRX_REQ");

		super.setRecord("trxId", sharedMap.getString("trxId"));
		super.setRecord("trxType", sharedMap.getString("trxType"));
		super.setRecord("mchtId", sharedMap.getString("mchtId"));
		super.setRecord("tmnId", sharedMap.getString("tmnId"));
		super.setRecord("trackId", sharedMap.getString("trackId"));
		super.setRecord("payerName", sharedMap.getString("payerName"));
		super.setRecord("payerEmail", sharedMap.getString("payerEmail"));
		super.setRecord("payerTel", sharedMap.getString("payerTel"));
		super.setRecord("amount", sharedMap.getString("amount"));
		super.setRecord("cardId", sharedMap.getString("cardId"));
		if (!CommonUtil.isNullOrSpace(sharedMap.getString("cardId"))) {
			super.setRecord("issuer", sharedMap.getString("issuer"));
			super.setRecord("last4", sharedMap.getString("last4"));
			super.setRecord("cardType", sharedMap.getString("cardType"));
			super.setRecord("bin", sharedMap.getString("bin"));
			super.setRecord("installment", sharedMap.getString("installment"));
			super.setRecord("acquirer", sharedMap.getString("acquirer"));
		}
		super.setRecord("prodId", sharedMap.getString("prodId"));
		super.setRecord("regDay", sharedMap.getString("regDate").substring(0, 8));
		super.setRecord("regTime", sharedMap.getString("regDate").substring(8));
		super.setRecord("regDate", sharedMap.getString("regDate"));
		logger.info("set TRX_REQ : {}", super.insert());
		super.initRecord();

	}

	public void insertTrxRES(SharedMap<String, Object> sharedMap) {

		super.setTable("PG_TRX_RES");

		super.setRecord("trxId", sharedMap.getString("trxId"));
		super.setRecord("authCd", sharedMap.getString("authCd"));
		super.setRecord("resultCd", sharedMap.getString("resultCd"));
		super.setRecord("resultMsg", sharedMap.getString("resultMsg"));
		super.setRecord("van", sharedMap.getString("van"));
		super.setRecord("vanId", sharedMap.getString("vanId"));
		super.setRecord("vanTrxId", sharedMap.getString("vanTrxId"));
		super.setRecord("vanResultCd", sharedMap.getString("vanResultCd"));
		super.setRecord("vanResultMsg", sharedMap.getString("vanResultMsg"));
		super.setRecord("regDay", sharedMap.getString("regDate").substring(0, 8));
		super.setRecord("regTime", sharedMap.getString("regDate").substring(8));
		super.setRecord("regDate", sharedMap.getString("regDate"));

		logger.info("set TRX_RES : {}", super.insert());

		if (sharedMap.getString("resultCd").equals("0000")) {
			insertTrxPAY(sharedMap.getString("trxId"));
		} else {
			insertTrxERR(sharedMap.getString("trxId"));
		}

		super.initRecord();
	}
	
	public void insertTrxPAY(String trxId) {

		String q = "INSERT INTO PG_TRX_PAY  " + " SELECT A.trxId,mchtId,tmnId,trackId,payerName,payerEmail,payerTel,amount,installment,cardId,cardType,bin,last4,'승인',prodId,issuer,acquirer,"
				+ " A.regDay,A.regTime,authCd,resultCd,resultMsg,van,vanId,vanTrxId,B.regDay,B.regTime,B.regDate " + " FROM PG_TRX_REQ A, PG_TRX_RES B WHERE A.trxId = B.trxId AND A.trxId = '" + trxId + "'";
		logger.info("set TRX_PAY : {}", super.update(q));
		super.initRecord();

	}
	
	public void insertTrxERR(String trxId) {

		String q = "INSERT INTO PG_TRX_ERR  " + " SELECT A.trxId,trxType,mchtId,tmnId,trackId,payerName,payerEmail,payerTel,amount,installment,cardId,cardType,bin,last4,issuer,acquirer,prodId,"
				+ " A.regDay,A.regTime,resultCd,resultMsg,van,vanId,vanTrxId,vanResultCd,vanResultMsg,B.regDay,B.regTime,B.regDate " + " FROM PG_TRX_REQ A, PG_TRX_RES B WHERE A.trxId = B.trxId AND A.trxId = '" + trxId + "'";
		logger.info("set TRX_ERR : {}", super.update(q));
		super.initRecord();

	}
	
	//WH
		public void insertTrxRFD(SharedMap<String, Object> sharedMap, SharedMap<String, Object> trxMap) {
			super.setTable("PG_TRX_RFD");
			long vat = new Double(sharedMap.getLong("amount") *10 /110).longValue();
			super.setRecord("trxId", sharedMap.getString("trxId"));
			super.setRecord("mchtId", trxMap.getString("mchtId"));
			super.setRecord("tmnId", sharedMap.getString("tmnId"));
			if(sharedMap.isNullOrSpace("trackId")){
				super.setRecord("trackId", trxMap.getString("trackId"));
			}else{
				super.setRecord("trackId", sharedMap.getString("trackId"));
			}
			super.setRecord("status", "접수");
			super.setRecord("rfdType", sharedMap.getString("rfdType"));
			super.setRecord("rfdAll", sharedMap.getString("rfdAll"));
			super.setRecord("rfdAmount", -sharedMap.getLong("amount"));
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
			super.setRecord("reqDay", sharedMap.getString("regDate").substring(0, 8));
			super.setRecord("reqTime", sharedMap.getString("regDate").substring(8));
	 
			super.setRecord("regDay", sharedMap.getString("regDate").substring(0, 8));
			super.setRecord("regTime", sharedMap.getString("regDate").substring(8));
			super.setRecord("regDate", sharedMap.getString("regDate"));
			logger.info("set TRX_RFD : {}", super.insert());
			super.initRecord();
		}

		//WH
		public void updateTrxRFD(SharedMap<String, Object> sharedMap) {
			super.setTable("PG_TRX_RFD");
			if (sharedMap.getString("vanResultCd").equals("0000")) {
				super.setRecord("status", "완료");
			} else {
				super.setRecord("status", "실패");
			}
			super.setRecord("resultCd", sharedMap.getString("vanResultCd"));
			super.setRecord("resultMsg", sharedMap.getString("vanResultMsg"));
			super.setRecord("van", sharedMap.getString("van"));
			super.setRecord("vanId", sharedMap.getString("vanId"));
			super.setRecord("vanTrxId", sharedMap.getString("vanTrxId"));
			super.setRecord("vanResultCd", sharedMap.getString("vanResultCd"));
			super.setRecord("vanResultMsg", sharedMap.getString("vanResultMsg"));
			super.setRecord("regDay", sharedMap.getString("regDate").substring(0, 8));
			super.setRecord("regTime", sharedMap.getString("regDate").substring(8));
			super.setRecord("regDate", sharedMap.getString("regDate"));
			super.addWhere("trxId", sharedMap.getString("trxId"));
			logger.info("set TRX_RFD : {}", super.update());
			super.initRecord();
		}

		public void updateTrxPay(String trxId) {

			super.setTable("PG_TRX_PAY");

			super.setRecord("status", "승인취소");
			super.addWhere("trxId", trxId);
			logger.info("set TRX_PAY : {}", super.update());
			super.initRecord();
		}
		
		public SharedMap<String, Object> getAdminRfdByVanTrxId(String vanTrxId) {
			super.setTable("PG_TRX_ADMIN_RFD");
			super.setColumns("idx");
			super.addWhere("vanTrxId", vanTrxId, eq);
			RecordSet rset = super.search();
			super.initRecord();
			return rset.getRow(0);
		}
		
		public void updateAdminRfd(String idx, String trxId, String resultCd) {
			super.setTable("PG_TRX_ADMIN_RFD");
			super.setRecord("trxId", trxId);
			super.setRecord("resultCd", resultCd);
			super.addWhere("idx", idx, eq);
			logger.info("set PG_TRX_ADMIN_RFD : {}", super.update());
			super.initRecord();
		}

		public String getCardName(String code) {
			super.setTable("PG_CODE");
			super.setColumns("codeName");
			super.addWhere("alias","FIRST",eq);
			super.addWhere("code",code,eq);
			super.setOrderBy("idx desc");
			RecordSet rset = super.search();
			super.initRecord();
			return rset.getRowFirst().getString("codeName");
		}

		public void updateFirstPayIO(FirstHead head, String response) {
			String curDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
			super.setTable("PG_TRX_LOAD_FIRSTPAY");
			super.setRecord("resData", response);
			super.setRecord("resultCd", "00");
			super.setRecord("sendDay", curDate.substring(0, 8));
			super.setRecord("sendTime", curDate.substring(8));
			super.addWhere("trxId",head.getTrxId(),eq);
			logger.info("update PG_TRX_LOAD_FIRSTPAY : {}", super.update());
			super.initRecord();
		}
		
		public void insertTrxWH(SharedMap<String, Object> sharedMap) {

			super.setTable("PG_TRX_WH");
			super.setRecord("trxId", sharedMap.getString("trxId"));
			super.setRecord("tmnId", sharedMap.getString("tmnId"));
			super.setRecord("trxType", sharedMap.getString("trxType"));
			super.setRecord("reqData", sharedMap.getString("reqData"));
			super.setRecord("resData", sharedMap.getString("resData"));
			super.setRecord("orgData", sharedMap.getString("orgData"));
			super.setRecord("van", sharedMap.getString("van"));
			super.setRecord("vanId", sharedMap.getString("vanId"));
			super.setRecord("vanTrxId", sharedMap.getString("vanTrxId"));

			logger.info("set TRX_WH : {}", super.insert());
			super.initRecord();
		}
		
		public void updateTrxWH(SharedMap<String, Object> sharedMap) {
			super.setTable("PG_TRX_WH");
			super.setRecord("trxId", sharedMap.getString("trxId"));
			super.setRecord("tmnId", sharedMap.getString("tmnId"));
			super.setRecord("resData", sharedMap.getString("resData"));
			super.setRecord("retry", "Y");
			super.addWhere("vanTrxId", sharedMap.getString("vanTrxId"), eq);
			super.addWhere("trxType", sharedMap.getString("trxType"), eq);
			logger.info("UPDATE PG_TRX_WH : {}", super.update());
			super.initRecord();
		}
}
