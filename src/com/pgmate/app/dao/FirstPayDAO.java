package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.FirstHead;

/**
 * 210813_PYS : 테이블정의서에도 없는데.. 이거왠지 대행사쪽에 거래정보 다시 보내는 코드같음;
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
	
	/**
	 * 210813_PYS : 'tmnId'로 터미널 기본정보 조회
	 * <pre>
	 * SELECT * FROM PG_MCHT_TMN WHERE tmnId = 'tmnId'
	 * </pre>
	 * @param tmnId : 터미널ID (TMN000000)
	 * @return
	 */
	//WH
	public SharedMap<String, Object> getMchtTmnByTmnId(String tmnId) {
		super.setTable("PG_MCHT_TMN");
		super.setColumns("*");
		super.addWhere("tmnId", tmnId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	/**
	 * 210813_PYS : 결제응답내역 조회
	 * <pre>
	 * SELECT * FROM PG_TRX_RES WHERE van = 'van', vanTrxId = 'vanTrxId', regDay = 'trnDate', regTime = 'trnDate' 
	 * </pre>
	 * @param van : 결제처리사(KSNET)
	 * @param vanTrxId : 처리사 거래번호
	 * @param trnDate : 수신날짜
	 * @return
	 */
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
	
	/**
	 * pys : 승인거래 원장 조회
	 * <pre>
	 * SELECT * FROM PG_TRX_PAY WHERE van = 'van', vanTrxId = 'vanTrxId'
	 * </pre>
	 * @param van : 거래처리사(KSNET)
	 * @param vanTrxId : 처리사 거래번호
	 * @return
	 */
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
	
	/**
	 * pys : 승인된 거래가 있는지 조회
	 * <pre>
	 * SELECT * FROM PG_TRX_RFD WHERE van = 'van', vanTrxId = 'vanTrxId'
	 * </pre>
	 * @param van : 거래처리사 (KSNET)
	 * @param vanTrxId : 처리사 거래번호
	 * @return
	 */
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
	
	/**
	 * pys : 가맹점 정보 조회
	 * <pre>
	 * SELECT * FROM PG_MCHT WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점 ID
	 * @return
	 */
	public SharedMap<String, Object> getMchtByMchtId(String mchtId) {
		super.setTable("PG_MCHT");
		super.setColumns("*");
		super.addWhere("mchtId", mchtId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}
	
	/**
	 * pys : 결제취소 조회
	 * <pre>
	 * SELECT * FROM PG_TRX_RFD WHERE rootTrxId = 'trxId'
	 * </pre>
	 * @param trxId : 거래번호
	 * @return
	 */
	public SharedMap<String, Object> getTrxRfdByTrxId(String trxId) {

		super.setTable("PG_TRX_RFD");
		super.setColumns("*");
		super.addWhere("rootTrxId", trxId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRow(0);
	}

	/**
	 * pys : 매입내역에서 취소원거래번호로 매입금액 조회 (+ : 매입, - : 취소)
	 * <pre>
	 * SELECT SUM(amount) AS AMT FROM PG_TRX_CAP WHERE rootTrxId = 'trxId'
	 * </pre>
	 * @param trxId : 취소원거래번호
	 * @return
	 */
	public long getTrxRefundSumByTrxId(String trxId) {

		super.setTable("PG_TRX_CAP");
		super.setColumns(" SUM(amount) as AMT ");
		super.addWhere("rootTrxId", trxId, eq);
		RecordSet rset = super.search();
		super.setColumns("*");
		super.initRecord();
		return rset.getRow(0).getLong("AMT");

	}
	
	/**
	 * pys : 카드 BIN 정보 조회 (조회가 안될시 신용 기타 로 나옴)
	 * <pre>
	 * SELECT * FROM PG_CODE_BIN WHERE bin = 'bin'
	 * </pre>
	 * @param bin : 카드번호 bin (카드번호 앞 6자리)
	 * @return
	 */
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
	
	/**
	 * pys : 승인거래원장 조회
	 * <pre>
	 * SELECT * FROM PG_TRX_PAY 
	 * WHERE van = 'van', tmnId = 'tmnId', authCd = 'authCd', regDay = 'regDay', bin = 'bin'
	 * </pre>
	 * @param van : 거래처리사
	 * @param tmnId : 단말기ID
	 * @param authCd : 승인번호
	 * @param regDay : 수신일자
	 * @param bin : 카드 BIN
	 * @return
	 */
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
	
	/**
	 * pys : 발행사로 카드브랜드 조회
	 * @param issuer : 발행사
	 * @return
	 */
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
	
	/**
	 * pys : 카드 암호화 정보 추가
	 * <pre>
	 * INSERT INTO 
	 * PG_TRX_BOX (cardId, value) 
	 * VALUES 
	 * ('cardId', 'value')
	 * </pre>
	 * @param cardId : 카드ID
	 * @param value : 카드암호화값
	 */
	public void insertCard(String cardId, String value) {

		super.setTable("PG_TRX_BOX");
		logger.info("set card id : {}", cardId);
		super.setColumns("*");
		super.setRecord("cardId", cardId);//1개
		super.setRecord("value", value);
		logger.info("set card : {}", super.insert());
		super.initRecord();

	}
	
	/**
	 * pys : 결제요청내역 추가
	 * <pre>
	 * INSERT INTO 
	 * PG_TRX_REQ (trxId, trxType, mchtId, tmnId, trackId, payerName, payerEmail, payerTel, amount, cardId, issuer, last4, cardtype, bin, installment,  acquirer, prodId, regDay, regTime, regDate
	 * VALUES
	 * ('trxId', 'trxType', 'mchtId', 'tmnId', 'trackId', 'payerName', 'payerEmail', 'payerTel', 'amount', 'cardId', 'issuer', 'last4', 'cardtype', 'bin', 'installment',  'acquirer', 'prodId', 'regDay', 'regTime', 'regDate')
	 * </pre>
	 * @param trxId : 거래번호
	 * @param trxType : 거래유형
	 * @param mchtId : 가맹점ID
	 * @param tmnId : 단말기ID
	 * @param trackId : 주문번호
	 * @param payerName : 주문자명
	 * @param payerEmail : 주문자이메일
	 * @param payerTel : 주문자전화번호
	 * @param amount : 금액
	 * @param cardId : 카드ID
	 * @param issuer : 발급사
	 * @param last4 : 카드 뒤 4자리
	 * @param cardType : 카드유형(신용, 체크, 기타)
	 * @param bin : 카드 bin
	 * @param installment : 할부기간
	 * @param acquirer : 매입사
	 * @param prodId : 주문내역ID
	 * @param regDay : 수신일자
	 * @param regTime : 수신시간
	 * @param regDate : 기록일시
	 */
	public void insertTrxREQ(SharedMap<String, Object> sharedMap) {
		
		CPRequest cp = new CPRequest();
		
		// KBR : PG_TRX_ERR 에서 insert 시 PG_TRX_REQ 테이블 참조하여 insert
		super.setTable("PG_TRX_REQ");
		super.setRecord("trxId", sharedMap.getString("trxId"));
		super.setRecord("trxType", sharedMap.getString("trxType"));
		super.setRecord("mchtId", sharedMap.getString("mchtId"));
		super.setRecord("tmnId", sharedMap.getString("tmnId"));
		super.setRecord("trackId", sharedMap.getString("trackId"));
		//super.setRecord("payerName", sharedMap.getString("payerName")); // 주문자명
		// KBR : 암호화 한 값 
		super.setRecord("payerName", cp.getData("payerName").val); // 주문자명
		super.setRecord("payerEmail", sharedMap.getString("payerEmail")); // 주문자 이메일 
		super.setRecord("payerTel", sharedMap.getString("payerTel")); // 주문자 전화번호 
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

	/**
	 * pys : 결제응답내역 추가
	 * <pre>
	 * INSERT INTO
	 * PG_TRX_RES (trxId, authCd, resultCd, resultMsg, van, vanId, vanTrxId, vanResultCd, vanResultMsg, regDay, regTime, regDate)
	 * VALUES
	 * ('trxId', 'authCd', 'resultCd', 'resultMsg', 'van', 'vanId', 'vanTrxId', 'vanResultCd', 'vanResultMsg', 'regDay', 'regTime', 'regDate')
	 * </pre>
	 * @param trxId : 거래번호
	 * @param authCd : 승인번호
	 * @param resultCd : 응답코드
	 * @param resultMsg : 응답메세지
	 * @param van : 거래처리사
	 * @param vanId : 거래처리사 ID
	 * @param vanTrxId : 처리사 거래번호
	 * @param vanResultCd : VAN 응답코드
	 * @param vanResultMsg : VAN 메세지
	 * @param regDay : 수신일자
	 * @param regTime : 수신시간
	 * @param regDate : 기록일시
	 */
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
			// KBR : 승인 실패 내역 추가 
			insertTrxERR(sharedMap.getString("trxId"));
		}

		super.initRecord();
	}
	
	/**
	 * pys : 승인거래원장 추가
	 * @param trxId : 거래번호
	 */
	public void insertTrxPAY(String trxId) {
		String q = "INSERT INTO PG_TRX_PAY " + "SELECT A.trxId,mchtId,tmnId,trackId,payerName,payerEmail,payerTel,amount,installment,cardId,cardType,bin,last4,'승인',prodId,issuer,acquirer,"
					+ " A.regDay,A.regTime,authCd,resultCd,resultMsg,van,vanId,vanTrxId,B.regDay,B.regTime,B.regDate " + " FROM PG_TRX_REQ A, PG_TRX_RES B WHERE A.trxId = B.trxId AND A.trxId = '" + trxId + "'";
		logger.info("set TRX_PAY : {}", super.update(q));
		super.initRecord();
	}
	
	/**
	 * pys : 승인실패내역 추가
	 * @param trxId : 거래번호
	 */
	public void insertTrxERR(String trxId) {
		
		String q = "INSERT INTO PG_TRX_ERR  " + " SELECT A.trxId,trxType,mchtId,tmnId,trackId,payerName,payerEmail,payerTel,amount,installment,cardId,cardType,bin,last4,issuer,acquirer,prodId,"
				+ " A.regDay,A.regTime,resultCd,resultMsg,van,vanId,vanTrxId,vanResultCd,vanResultMsg,B.regDay,B.regTime,B.regDate " + " FROM PG_TRX_REQ A, PG_TRX_RES B WHERE A.trxId = B.trxId AND A.trxId = '" + trxId + "'";
		logger.info("set TRX_ERR : {}", super.update(q));
		super.initRecord();

	}
	
	//WH
	/**
	 * pys : 결제취소원장 추가
	 * <pre>
	 * INSERT INTO PG_TRX_RFD (....) 
	 * VALUES (....)
	 * </pre>
	 * @param trxId : 거래번호
	 * @param mchtId : 가맹점ID
	 * @param tmnId : 단말기ID
	 * @param trackId : 주문번호
	 * @param status : 처리결과, 접수, 완료, 실패
	 * @param rfdType : 취소유형, 승인취소, 매입취소, 정산취소
	 * @param rfdAll : 전액, 부분
	 * @param rfdAmount : 금액
	 * @param rfdVat : 수수료금액
	 * @param cardId : 카드ID
	 * @param bin : 카드bin
	 * @param last4 : 카드번호뒤4자리
	 * @param issuer : 발급사
	 * @param acquirer : 매입사
	 * @param rootTrnDay : 원거래일자
	 * @param rootTrxId : 원거래번호
	 * @param rootTrackId : 원거래주문번호
	 * @param rootAmount : 원거래금액
	 * @param rootVat : 원거래VAT
	 * @param authCd : 승인번호
	 * @param reqDay : 수신일자
	 * @param reqTime : 수신시간
	 * @param reqDate : 기록일시 
	 */
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
	/**
	 * pys : 결제취소내역 수정
	 * <pre>
	 * UPDATE PG_TRX_RFD
	 * SET (....)
	 * WHERE trxId = 'trxId'
	 * </pre>
	 * @param vanResultCd : 처리사응답코드
	 * @param status : 처리결과, 접수, 완료, 실패
	 * @param vanResultMsg : 처리사응답메세지
	 * @param van : 거래처리사
	 * @param vanId : 거래처리사 ID
	 * @param vanTrxId : 처리사 거래번호
	 * @param regDay : 수신일자
	 * @param regTime : 수신시간
	 * @param trxId : 거래번호
	 */
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
		
		/**
		 * pys : 승인거래내역 취소로 변경
		 * <pre>
		 * UPDATE PG_TRX_PAY
		 * SET status = '승인취소'
		 * WHERE trxId = 'trxId'
		 * </pre>
		 * @param trxId : 거래번호
		 */

		public void updateTrxPay(String trxId) {

			super.setTable("PG_TRX_PAY");

			super.setRecord("status", "승인취소");
			super.addWhere("trxId", trxId);
			logger.info("set TRX_PAY : {}", super.update());
			super.initRecord();
		}

	/**
	 * pys : 사이트를 통한 거래취소요청 정보 조회
	 * <pre>
	 * SELECT idx 
	 * FROM PG_TRX_ADMIN_RFD
	 * WHERE vanTrxId = 'vanTrxId'
	 * </pre>
	 * @param vanTrxId : van사 거래번호
	 * @return
	 */
	public SharedMap<String, Object> getAdminRfdByVanTrxId(String vanTrxId) {
		super.setTable("PG_TRX_ADMIN_RFD");
		super.setColumns("idx");
		super.addWhere("vanTrxId", vanTrxId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRow(0);
	}
	
	/**
	 * pys : 사이트를 통한 거래 취소 요청 내역 수정
	 * UPDATE PG_TRX_ADMIN_RFD 
	 * SET trxId = 'trxId', resultCd = 'resultCd'
	 * WHERE idx = 'idx'
	 * @param idx : 인덱스
	 * @param trxId : 거래번호
	 * @param resultCd : 결과코드
	 */
	public void updateAdminRfd(String idx, String trxId, String resultCd) {
		super.setTable("PG_TRX_ADMIN_RFD");
		super.setRecord("trxId", trxId);
		super.setRecord("resultCd", resultCd);
		super.addWhere("idx", idx, eq);
		logger.info("set PG_TRX_ADMIN_RFD : {}", super.update());
		super.initRecord();
	}

	/**
	 * pys : 카드 코드명 조회 (신한, 삼성, 현대 등등...)
	 * <pre>
	 * SELECT codeName
	 * FROM PG_CODE
	 * WHERE alias = 'FIRST', code = 'code'
	 * ORDER BY idx desc
	 * </pre>
	 * @param : code 부여코드 (카드번호 앞6자리)
	 * @return
	 */
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

	/**
	 * pys : DB에 정보없음, 안쓰는듯
	 * <pre>
	 * UPDATE PG_TRX_LOAD_FIRSTPAY
	 * SET (...)
	 * WHERE trxId = head.getTrxId()
	 * 
	 * </pre>
	 * @param head
	 * @param response
	 */
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
	
	/**
	 * pys : DB에 정보 없음, 무선단말 거래내역 추가
	 * <pre>
	 * INSERT INTO PG_TRX_WH (...)
	 * VALUES (...)
	 * </pre>
	 * @param sharedMap
	 */
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
	
	/**
	 * pys : DB에 정보없음, 무선단말 거래내역 수정
	 * <pre>
	 * UPDATE PG_TRX_WH
	 * SET trxId = 'trxId', tmnId = 'tmnId', resData = 'resData', retry = 'Y'
	 * WHERE vanTrxId = vanTrxId, trxType = 'trxType'
	 * </pre>
	 * @param sharedMap
	 */
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
