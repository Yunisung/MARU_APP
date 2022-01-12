package com.pgmate.app.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.FirstPayDAO;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.cipher.SeedKisa;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.lib.util.regex.Validator;
import com.pgmate.pay.bean.Card;
import com.pgmate.pay.bean.FirstBody;
import com.pgmate.pay.bean.FirstHead;

public class FirstPayUtil {
	
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.FirstPayUtil.class);
	private FirstHead head = null;
	private FirstBody body = null;
	
	private SharedMap<String, Object> mchtTmnMap = null;
	private SharedMap<String, Object> mchtMap = null;
	private SharedMap<String, Object> trxMap  = null;
	
	private SharedMap<String, Object> requestMap = new SharedMap<String, Object>();
	private SharedMap<String, Object> sharedMap = new SharedMap<String, Object>();
	private String resMsg = "";
	public String response = "Fail";
	
	private static int HEAD				= 50;

	private FirstPayDAO trxDAO = new FirstPayDAO();
	
	public FirstPayUtil() {
		// TODO Auto-generated constructor stub
	}
	

	public String trxRetry(String request, String trxId) {
		sharedMap.put("trxId", trxId);
		head = new FirstHead(request);
		body = new FirstBody(request.substring(HEAD));
		
		logger.info("vanTrxId : {}, vanId : {}",head.transNo,head.terminalId);
		logger.info("trxId : {}, trxType : {}",head.transNo,head.trxType);
		parseFirstPay();
		
		
		String trnType = "";
		if(requestMap.getString("trxType").equals("D1") || requestMap.getString("trxType").equals("S1") || requestMap.getString("trxType").equals("K1")){
			trnType ="PAY";
		}else if(requestMap.getString("trxType").equals("D2") || requestMap.getString("trxType").equals("S2") || requestMap.getString("trxType").equals("K2")){
			trnType ="REFUND";
		}else{
			trnType ="UNKNOWN : "+head.trxType;
		}
		requestMap.put("REQ_TYPE", trnType);
		
		
		if( !head.serviceType.equals("KK")){
			if(!valid()) {
				logger.info("transaction erroe");
				response = "Fail|" +resMsg;
				setLog();
				return response;
			}
			response = "OK";
		}else{
			logger.info("not card transaction");
			response = "Fail|" +"신용카드 거래아님";
			setLog();
			return response;
		}
		
		
		
		// 공통 사항

		sharedMap.put("trxType"			, "WHTR");
		sharedMap.put("tmnId"			, mchtTmnMap.getString("tmnId"));
		sharedMap.put("mchtId"			, mchtTmnMap.getString("mchtId"));
		sharedMap.put("trackId" 		, requestMap.getString("transNo"));
				
		sharedMap.put("van", requestMap.getString("van"));        
		sharedMap.put("vanId", requestMap.getString("mchtNo"));        
		sharedMap.put("vanTrxId", requestMap.getString("transNo"));     
		sharedMap.put("vanResultCd", "0000");  
		sharedMap.put("vanResultMsg", "정상");
		sharedMap.put("amount", requestMap.getString("amount"));
				
		if(requestMap.getString("REQ_TYPE").equals("REFUND")) { // valid 에서 만든 값
			SharedMap<String, Object> adminRfd = trxDAO.getAdminRfdByVanTrxId(requestMap.getString("transNo"));
			boolean isAdminRfd = false;
			if(adminRfd != null && adminRfd.size() > 0) {
				logger.debug("ADMIN REFUND REQUEST : {} / IDX : {}" + adminRfd.getString("idx"));
				trxDAO.updateAdminRfd(adminRfd.getString("idx"), sharedMap.getString("trxId"), "0000");
				isAdminRfd = false;
			}
			
			/*
			if(CommonUtil.isNullOrSpace(requestMap.getString("authDate"))) {
				logger.debug("authDate OR TIME IS NULL");
				sharedMap.put("regDate", requestMap.getString("trnDate"));
			} else {
				sharedMap.put("regDate", requestMap.getString("authDate"));
			}*/
			sharedMap.put("regDate", requestMap.getString("trnDate"));		
			refund(isAdminRfd);
		} else {
			sharedMap.put("regDate", requestMap.getString("trnDate"));
			pay();
		}
		return response;
	}
	
	
	
	private void parseFirstPay() {
		// head
		requestMap.put("headerName", head.headerName);
		logger.debug("headerName [{}]",requestMap.getString("headerName"));
		
		requestMap.put("serviceType", head.serviceType);
		logger.debug("serviceType [{}]",requestMap.getString("serviceType"));
		
		requestMap.put("trxType", head.trxType);
		logger.debug("trxType [{}]",requestMap.getString("trxType"));
		
		requestMap.put("terminalId", head.terminalId);
		logger.debug("terminalId [{}]",requestMap.getString("terminalId"));
		
		requestMap.put("trnDate", "20"+head.trnDate);
		logger.debug("trnDate [{}]",requestMap.getString("trnDate"));
		
		requestMap.put("transNo", head.transNo);
		logger.debug("transNo [{}]",requestMap.getString("transNo"));
		
		requestMap.put("resultCd", head.resultCd);
		logger.debug("resultCd [{}]",requestMap.getString("resultCd"));
		
		requestMap.put("filler1", head.filler1);
		logger.debug("filler1 [{}]",requestMap.getString("filler1"));
		
		//body
		requestMap.put("cardNo", body.cardNo);
		logger.debug("cardNo [{}]",requestMap.getString("cardNo"));
		
		requestMap.put("expireDate", body.expireDate);
		logger.debug("expireDate [{}]",requestMap.getString("expireDate"));
		
		requestMap.put("inputType", body.inputType);
		logger.debug("inputType [{}]",requestMap.getString("inputType"));
		
		requestMap.put("installment", body.installment);
		logger.debug("installment [{}]",requestMap.getString("installment"));
		
		requestMap.put("amount", body.amount);
		logger.debug("amount [{}]",requestMap.getString("amount"));
		
		requestMap.put("serviceCharge", body.serviceCharge);
		logger.debug("serviceCharge [{}]",requestMap.getString("serviceCharge"));
		
		requestMap.put("tax", body.tax);
		logger.debug("tax [{}]",requestMap.getString("tax"));
		
		requestMap.put("rfdType", body.rfdType);
		logger.debug("rfdType [{}]",requestMap.getString("rfdType"));
		
		requestMap.put("authCd", body.authCd);
		logger.debug("authCd [{}]",requestMap.getString("authCd"));
		
		requestMap.put("authDate", "20"+body.authDate);
		logger.debug("authDate [{}]",requestMap.getString("authDate"));
		
		requestMap.put("mchtNo", body.mchtNo);
		logger.debug("mchtNo [{}]",requestMap.getString("mchtNo"));
		
		requestMap.put("cardName", body.cardName);
		logger.debug("cardName [{}]",requestMap.getString("cardName"));
		
		requestMap.put("issuerCode", body.issuerCode);
		logger.debug("issuerCode [{}]",requestMap.getString("issuerCode"));
		
		requestMap.put("issuer", body.issuer);
		logger.debug("issuer [{}]",requestMap.getString("issuer"));
		
		requestMap.put("acquirerCode", body.acquirerCode);
		logger.debug("acquirerCode [{}]",requestMap.getString("acquirerCode"));
		
		requestMap.put("acquirer", body.acquirer);
		logger.debug("acquirer [{}]",requestMap.getString("acquirer"));
		
		requestMap.put("fdikCode", body.fdikCode);
		logger.debug("fdikCode [{}]",requestMap.getString("fdikCode"));
		
		requestMap.put("cardResultCode", body.cardResultCode);
		logger.debug("cardResultCode [{}]",requestMap.getString("cardResultCode"));
		
		requestMap.put("balance", body.balance);
		logger.debug("balance [{}]",requestMap.getString("balance"));
		
		requestMap.put("cardType", body.cardType);
		logger.debug("cardType [{}]",requestMap.getString("cardType"));
		
		requestMap.put("gasCode", body.gasCode);
		logger.debug("gasCode [{}]",requestMap.getString("gasCode"));
		
		requestMap.put("filler2", body.filler2);
		logger.debug("filler2 [{}]",requestMap.getString("filler2"));
		
		requestMap.put("filler3", body.filler3);
		logger.debug("filler3 [{}]",requestMap.getString("filler3"));
	}

	private boolean valid() {
		
		// 필수값이 모두 있는지는 확인하자.
		
		// seq_no 가 중복 되었는지 확인한다. PG_TRX_RES.VanTrxId 중복확인
		if(CommonUtil.isNullOrSpace(requestMap.getString("transNo"))) {
			logger.debug("거래번호 없음");
			resMsg = "거래번호 없음";
			return false;
		}
		
		
		if(CommonUtil.isNullOrSpace(requestMap.getString("terminalId"))) {
			logger.debug("터미널 ID 정보 없음");
			resMsg = "터미널 ID 정보 없음";
			return false;
		}
		mchtTmnMap = trxDAO.getMchtTmnByTmnId(requestMap.getString("terminalId"));
		
		// 승인일 경우 오프 PG단말기 tmnId 조회 
		if(requestMap.getString("REQ_TYPE").equals("PAY")){
			logger.debug("terminalId [{}]",requestMap.getString("terminalId"));
			
			if (mchtTmnMap == null || mchtTmnMap.isEmpty()) {
				logger.debug("등록되지 않은 터미널ID | TERMINALID : {}", CommonUtil.nToB(requestMap.getString("terminalId")));
				resMsg = "등록되지 않은 터미널ID | TERMINALID : "+ CommonUtil.nToB(requestMap.getString("terminalId"));
				return false;
			}

			if(CommonUtil.isNullOrSpace(mchtTmnMap.getString("van"))){
				logger.info("VAN 설정되지 않은 터미널ID | TERMINALID : {}", requestMap.getString("terminalId"));
				resMsg = "VAN 설정되지 않은 터미널ID | TERMINALID :" +requestMap.getString("terminalId");
				return false;
			}
			requestMap.put("van", mchtTmnMap.getString("van"));
			if (trxDAO.isDuplicatedVanTrxId(mchtTmnMap.getString("van"),requestMap.getString("transNo"),requestMap.getString("trnDate"))) {
				logger.info("중복된 VAN 거래번호 TRX_RES => {}", requestMap.getString("transNo"));
				resMsg = "중복된 VAN 거래번호 =>" +requestMap.getString("transNo");
				return false;
			}
			
		} else {
			String bin = requestMap.getString("cardNo").substring(0, 6);
			trxMap = trxDAO.getTrxByOriTrxId(mchtTmnMap.getString("van"),requestMap.getString("terminalId"),requestMap.getString("authCd"),requestMap.getString("authDate"),bin);
			
			
			if(trxMap == null || trxMap.isEmpty()) {
				trxMap = trxDAO.getTrxByOriTrxId(mchtTmnMap.getString("van"),requestMap.getString("terminalId"),requestMap.getString("authCd"),requestMap.getString("authDate"),bin);
				if(trxMap == null || trxMap.isEmpty()) {
					logger.info("원거래 없음 VanTrxId : {}", requestMap.getString("transNo"));
					resMsg = "원거래 없음 VanTrxId : "+ requestMap.getString("transNo");
					return false;
				}
			}

			mchtTmnMap = trxDAO.getMchtTmnByTmnId(trxMap.getString("tmnId"));

			if (mchtTmnMap == null || mchtTmnMap.isEmpty()) {
				logger.debug("등록되지 않은 터미널ID | TERMINALID : {}", CommonUtil.nToB(requestMap.getString("terminalId")));
				resMsg = "등록되지 않은 터미널ID | TERMINALID : "+ CommonUtil.nToB(requestMap.getString("terminalId"));
				return false;
			}

			requestMap.put("van", mchtTmnMap.getString("van"));
			if (trxDAO.isDuplicatedRFDVanTrxId(mchtTmnMap.getString("van"),requestMap.getString("transNo"))) {
				logger.info("중복된 VAN 거래번호 TRX_RFD=> {}", requestMap.getString("transNo"));
				resMsg = "중복된 VAN 거래번호 =>" +requestMap.getString("transNo");
				return false;
			}
		}
		
		if(mchtTmnMap.isNullOrSpace("taxId")) {
			logger.info("Tax 등록되지 않음. {}, taxId = {}", requestMap.getString("terminalId"), mchtTmnMap.getString("taxId"));
			resMsg = "Tax 등록되지 않은 터미널 | seq_no : "+ CommonUtil.nToB(requestMap.getString("terminalId"));
			return false;
		}
		
		mchtMap = trxDAO.getMchtByMchtId(mchtTmnMap.getString("mchtId"));
		if (mchtMap == null || mchtMap.isEmpty()) {
			logger.debug("MERCHANT IS INVALID = {}", requestMap.getString("terminalId"));
			resMsg = "MERCHANT IS INVALID = " + CommonUtil.nToB(requestMap.getString("terminalId"));
			return false;
		}
		
		// 취소거래의 경우 다음을 확인한다.
		if(requestMap.getString("REQ_TYPE").equals("REFUND")) {
			logger.info("ROOT_VAN_TRX_ID  : {}",requestMap.getString("transNo"));
//			trxMap = trxDAO.getTrxByVanTrxId(sharedMap.getString("van"),requestMap.getString("tx_seq_no"));
			sharedMap.put("trackId" 	,trxMap.getString("trackId"));
			logger.info("ROOT_TRX_ID: {}",trxMap.getString("trxId"));
			logger.info("ROOT_AMOUNT: {}",trxMap.getLong("amount"));
			logger.info("ROOT_TRX_DAY: {}",trxMap.getString("trxDay"));
			logger.info("ROOT_AUTH_CD: {}",trxMap.getString("authCd"));
			logger.info("ROOT_TRACKID: {}",sharedMap.getString("trackId"));
			
			sharedMap.put("rootTrxId", trxMap.getString("trxId"));
			
			//원거래 취소 확인
			SharedMap<String,Object> rfdMap = trxDAO.getTrxRfdByTrxId(trxMap.getString("trxId"));
			if(rfdMap != null && !trxMap.isEmpty()){
				if(rfdMap.isEquals("rfdAll", "전액") && rfdMap.isEquals("status", "완료")){
					logger.info("이미 취소된 거래입니다.");
					resMsg = "이미 취소된 거래입니다.";
					return false;
				}
			}
			
			long refundedAmount = trxDAO.getTrxRefundSumByTrxId(trxMap.getString("trxId"));
			logger.info("REFUNDED_AMT: {}",refundedAmount);
			
			if(trxMap.getLong("amount") == -refundedAmount){
				logger.info("이미 취소된 거래입니다.");
				resMsg = "이미 취소된 거래입니다.";
				return false;
			}
			
			if(-refundedAmount+ requestMap.getLong("amt") > trxMap.getLong("amount") ){
				logger.info("취소요청금액이 원거래금액보다 큽니다.");
				resMsg = "취소요청금액이 원거래금액보다 큽니다.";
				return false;
			}
			
			if(requestMap.getLong("amount") == trxMap.getLong("amount")){
				sharedMap.put("rfdAll", "전액");
			}else{
				sharedMap.put("rfdAll", "부분");
			}
			
			
			
			logger.info("RFD_ALL   : {}",sharedMap.getString("rfdAll"));
			logger.info("RFD_TYPE  : {}",sharedMap.getString("rfdType"));
		// 승인거래의 경우 다음을 확인한다.
		} else {
			sharedMap.put("cardId", GenKey.genKeys(CPKEY.CARD, sharedMap.getString("trxId")));
			sharedMap.put("prodId", GenKey.genKeys(CPKEY.PRODUCT, sharedMap.getString("trxId")));
			
			requestMap.replace("cardNo", requestMap.getString("cardNo").substring(0,6)+"xxxxxx"+requestMap.getString("cardNo").substring(12));
			
			if (!CommonUtil.isNullOrSpace(requestMap.getString("cardNo"))) {
				int cardLength = requestMap.getString("cardNo").length();
				String[] issuer = getIssuer();
				sharedMap.put("last4", requestMap.getString("cardNo").substring(cardLength - 4, cardLength));
				sharedMap.put("issuer", issuer[0]);
				sharedMap.put("acquirer", issuer[2]);
				sharedMap.put("cardId", sharedMap.getString("cardId"));
				sharedMap.put("cardType", issuer[1]);

				Card card = new Card();
				card.number = requestMap.getString("card_no");
				card.last4 = sharedMap.getString("last4");
				card.issuer = sharedMap.getString("issuer");
				card.cardId = sharedMap.getString("cardId");
				card.bin    = sharedMap.getString("bin");
				card.installment = CommonUtil.parseInt(requestMap.getString("installment"));
				card.acquirer= sharedMap.getString("acquirer");
				card.cardType = sharedMap.getString("cardType");
				String encrypted = Base64.encodeToString(SeedKisa.encrypt(GsonUtil.toJson(card), ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16)));
				trxDAO.insertCard(sharedMap.getString("cardId"), encrypted);
				sharedMap.put("CARD_INSERTED", true); //카드정보가 이미 등록되었는지 여부
			}
		}
		
		return true;
	}

	public String[] getIssuer(){
		String[] issuer={"","",""};
		if(requestMap.getString("cardNo").length() > 6 ){
			sharedMap.put("bin", requestMap.getString("cardNo").substring(0, 6));
			if(Validator.isNumber(sharedMap.getString("bin"))){
				SharedMap<String,Object> issuerMap = trxDAO.getDBIssuer(requestMap.getString("cardNo").substring(0, 6));
				issuer[0] = issuerMap.getString("issuer");
				issuer[1] = issuerMap.getString("type") ;
				issuer[2] = issuerMap.getString("acquirer") ;
				
				logger.info("card bin:[{}],issuer:{},type:{},brand:{}",issuerMap.getString("bin"),issuerMap.getString("issuer"),issuerMap.getString("type"),issuerMap.getString("brand"));
			}else{
				issuer[0] = trxDAO.getCardName(requestMap.getString("issuerCode"));
				String cardType = "";
				if(requestMap.getString("cardType").equals("00")) {
					cardType = "신용";
				}else {
					cardType = "체크";
				}
				issuer[1] = cardType;
				issuer[2] = trxDAO.getCardName(requestMap.getString("acquirerCode"));
			}
		}
		
		return issuer;
	}
	
	private void pay() {
		sharedMap.put("installment", requestMap.getString("installment"));
		trxDAO.insertTrxREQ(sharedMap);
		
		sharedMap.put("authCd", CommonUtil.nToB(requestMap.getString("authCd")));	
		sharedMap.put("resultCd", "0000");
		sharedMap.put("resultMsg", "정상");
		sharedMap.put("advanceMsg", "정상승인");
		
		trxDAO.insertTrxRES(sharedMap);
		
	}
	
	private void refund(boolean isAdminRfd) {
		
		sharedMap.put("trackId", trxMap.getString("trackId"));
		trxDAO.insertTrxRFD(sharedMap, trxMap);
		trxDAO.updateTrxRFD(sharedMap);
		

		sharedMap.put("resultCd", "0000");
		sharedMap.put("resultMsg", "정상");
		sharedMap.put("advanceMsg", "정상취소");
		
		//당일 취소는 반드시 전액 취소만 가능하며 . 승인 취소로 업데이트 한다.
		if(trxMap.isEquals("regDay",sharedMap.getString("regDate").substring(0,8))){
			trxDAO.updateTrxPay(trxMap.getString("trxId"));
		}
	
	}
	
	public void setLog(){
		
		logger.info("head : [{}]",GsonUtil.toJson(head, true, ""));
    	logger.info("body : [{}]",GsonUtil.toJson(body, true, ""));
	}

	
}
