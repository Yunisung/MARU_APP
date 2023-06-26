package com.pgmate.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.Refund;
import com.pgmate.pay.bean.Request;
import com.pgmate.pay.bean.Response;
import com.pgmate.pay.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class RefundUtil {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.RefundUtil.class);
	/**
	 * 
	 */
	public RefundUtil() {

	}
	
	
	public String execute(SharedMap<String,Object> trxMap,String regId, String grade){
		Request request = new Request();
		String res = "";
		Refund refund = new Refund();
		refund.trxType 	= "ONTR";
		refund.tmnId  	= trxMap.getString("tmnId");
		refund.trackId	= GenKey.genKeys(CPKEY.REFUND,"ADMIN");
		refund.amount	= trxMap.getLong("amount");
		refund.rootTrxId= trxMap.getString("trxId");
		if("본사".equals(grade)){
			refund.udf1     = "ADMINWEB";
		}  else	if("대행사".equals(grade)){
			refund.udf1     = "DISTWEB";
		} else {
			refund.udf1     = "";
		}
		refund.udf2     = regId;
		request.refund = refund;
		
		SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
		if(!tmnMap.getString("status").equals("사용")) {
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
			response.result = result;
			res = GsonUtil.toJson(response);
			return res;
		}
		
		String reqJson = GsonUtil.toJson(request);
		
		res = comm(reqJson,tmnMap.getString("payKey"));
		if(res.startsWith("CONNECT")){
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "통신장애로 인한 취소 실패 ";
			response.result = result;
			res = GsonUtil.toJson(response);
		}else{
			Response resp = (Response)GsonUtil.fromJson(res, Response.class);
			try{
				if(resp.refund != null){
					if(!CommonUtil.isNullOrSpace(resp.refund.trxId)){
						String q = "INSERT INTO PG_TRX_ADMIN_RFD SELECT NULL,rootTrxId,vanTrxId,vanId,vanResultCd,vanResultMsg,rfdAmount,resultCd,trxId,'"+regId+"', '" + CommonUtil.getCurrentDate("yyyyMMdd") + "', regDate FROM PG_TRX_RFD  WHERE trxId = '" + resp.refund.trxId + "'";
						new DAO().update(q);
					}
				}
			}catch(Exception e){}
		}
		return res;
	
	}
	
	public String execute(SharedMap<String, Object> trxMap, String amount, String regId, String grade) {
		Request request = new Request();
		String res = "";
		Refund refund = new Refund();
		refund.trxType 	= "ONTR";
		refund.tmnId  	= trxMap.getString("tmnId");
		refund.trackId	= GenKey.genKeys(CPKEY.REFUND,"ADMIN");
		refund.amount	= Long.parseLong(amount);
		refund.rootTrxId= trxMap.getString("trxId");
		if("본사".equals(grade)){
			refund.udf1     = "ADMINWEB";
		}  else	if("대행사".equals(grade)){
			refund.udf1     = "DISTWEB";
		} else {
			refund.udf1     = "";
		}
		refund.udf2     = regId;
		request.refund = refund;
		
		SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
		if(!tmnMap.getString("status").equals("사용")) {
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
			response.result = result;
			res = GsonUtil.toJson(response);
			return res;
		}
		
		String reqJson = GsonUtil.toJson(request);
		
		res = comm(reqJson,tmnMap.getString("payKey"));
		if(res.startsWith("CONNECT")){
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "통신장애로 인한 취소 실패 ";
			response.result = result;
			res = GsonUtil.toJson(response);
		}else{
			Response resp = (Response)GsonUtil.fromJson(res, Response.class);
			try{
				if(resp.refund != null){
					if(!CommonUtil.isNullOrSpace(resp.refund.trxId)){
						String q = "INSERT INTO PG_TRX_ADMIN_RFD SELECT NULL,rootTrxId,vanTrxId,vanId,vanResultCd,vanResultMsg,rfdAmount,resultCd,trxId,'"+regId+"', '" + CommonUtil.getCurrentDate("yyyyMMdd") + "', regDate FROM PG_TRX_RFD  WHERE trxId = '" + resp.refund.trxId + "'";
						new DAO().update(q);
					}
				}
			}catch(Exception e){}
		}
		return res;
	}
	
	public String comm(String request,String payKey){
		//String paymentUrl = "https://api.bkwinners.kr/api/refund";
		String paymentUrl = "http://pgwas1:10002/api/refund";
		//String paymentUrl = "http://127.0.0.1:10002/api/refund";
		StringBuilder result = new StringBuilder();
		URL url = null;
		HttpURLConnection conn = null;
		
		System.setProperty("https.protocols", "TLSv1.2");
		
		long time = System.currentTimeMillis();
		try {

			logger.info("LOCAL >> PAYMENT ["+request+"]");
			url = new URL(paymentUrl);
			
			
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(60000);
			
			
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", payKey);
			conn.setRequestProperty("Connection", "close");
			
			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes("utf-8"));
			os.flush();
			os.close();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream()));
			
			String line;
			while ((line = br.readLine()) != null)
				result.append(line+"\n");
			
			br.close();

		} catch(Exception e) {
			result.append("CONNECT ERROR ["+e.getMessage()+"] "+paymentUrl);
			logger.error("PAYMENT URL REQUEST ERROR =["+e.getMessage()+"]");
			
		}finally{
			logger.info("ElapsedTime : "+(long)(System.currentTimeMillis()-time)+"msec");
			logger.info("LOCAL << PAYMENT ["+result.toString()+"]");
			conn.disconnect();
		}
		return result.toString();
	}
		
	//휴대폰 결제 승인취소 API 적용====================================================================================
	public String phoneExecute(SharedMap<String, Object> trxMap, String amount, String regId, String grade) {
		Request request = new Request();
		String res = "";
		Refund refund = new Refund();
		refund.tmnId  	= trxMap.getString("tmnId");
		refund.trackId	= GenKey.genKeys(CPKEY.REFUND,"ADMIN");
		refund.amount	= Long.parseLong(amount);
		refund.rootTrxId= trxMap.getString("trxId");
		if("본사".equals(grade)){
			refund.udf1     = "ADMINWEB";
		}  else	if("대행사".equals(grade)){
			refund.udf1     = "DISTWEB";
		} else {
			refund.udf1     = "";
		}
		refund.udf2     = regId;
		request.refund = refund;
		
		SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
		if(!tmnMap.getString("status").equals("사용")) {
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
			response.result = result;
			res = GsonUtil.toJson(response);
			return res;
		}
		
		String reqJson = GsonUtil.toJson(request);
		
		res = phoneRef(reqJson,tmnMap.getString("payKey"));
		if(res.startsWith("CONNECT")){
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "통신장애로 인한 취소 실패 ";
			response.result = result;
			res = GsonUtil.toJson(response);
		}else{
			Response resp = (Response)GsonUtil.fromJson(res, Response.class);
			try{
				if(resp.refund != null){
					if(!CommonUtil.isNullOrSpace(resp.refund.trxId)){
						String q = "INSERT INTO PG_TRX_ADMIN_RFD SELECT NULL,rootTrxId,vanTrxId,vanId,vanResultCd,vanResultMsg,rfdAmount,resultCd,trxId,'"+regId+"', '" + CommonUtil.getCurrentDate("yyyyMMdd") + "', regDate FROM PG_PHONE_RFD WHERE trxId = '" + resp.refund.trxId + "'";
						new DAO().update(q);
					}
				}
			}catch(Exception e){}
		}
		return res;
	}
	
	public String phoneRef(String request,String payKey){
		//개발
		//String paymentUrl = "https://svcapidev.mtouch.com/api/phone/refund";
		//운영
		String paymentUrl = "http://api.bkwinners.kr/api/phone/refund";
		//String paymentUrl = "http://127.0.0.1:10002/api/phone/refund";
		
		StringBuilder result = new StringBuilder();
		URL url = null;
		HttpURLConnection conn = null;
		
		System.setProperty("https.protocols", "TLSv1.2");
		
		long time = System.currentTimeMillis();
		try {

			logger.info("LOCAL >> PAYMENT ["+request+"]");
			url = new URL(paymentUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(60000);
			
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", payKey);
			conn.setRequestProperty("Connection", "close");
			
			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes("utf-8"));
			os.flush();
			os.close();
			
			BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream()));
			
			String line;
			while ((line = br.readLine()) != null)
				result.append(line+"\n");
			
			br.close();

		} catch(Exception e) {
			result.append("CONNECT ERROR ["+e.getMessage()+"] "+paymentUrl);
			logger.error("PAYMENT URL REQUEST ERROR =["+e.getMessage()+"]");
			
		}finally{
			logger.info("ElapsedTime : "+(long)(System.currentTimeMillis()-time)+"msec");
			logger.info("LOCAL << PAYMENT ["+result.toString()+"]");
			conn.disconnect();
		}
		return result.toString();
	}
	//휴대폰 결제 승인취소 API 적용====================================================================================
}
