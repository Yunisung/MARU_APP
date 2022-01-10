package com.pgmate.app.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.Account;
import com.pgmate.pay.bean.Request;



public class WalletAccntUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.util.WalletAccntUtil.class );
	
	
	public SharedMap<String, Object> excute(String bankCd, String account,String walletId,String apiKey,String method){
		
		Request request = new Request();
		Account accnt = new Account();
		accnt.bankCd = bankCd;
		accnt.account = account;
		accnt.accountVerify = true;
		accnt.walletId = walletId;
		accnt.trackId = GenKey.genKeys(CPKEY.ACCNT, "ADMIN");
		
		request.accnt = accnt;
		
		String reqJson = GsonUtil.toJson(request);
		
//		String apiKey = new PtnDAO().getApiKeyByWalletId(cpRequest.getKeyValue("walletId"));
		return comm(reqJson, apiKey, method);
		
	}
	
	private SharedMap<String, Object> comm(String request, String appKey, String method){
		String paymentUrl = "http://pgwas2:10003/wapi/accnt";
		URL url = null;
		HttpURLConnection conn = null;
		
		System.setProperty("https.protocols", "TLSv1.2");
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		long time = System.currentTimeMillis();
		try {
			logger.debug("LOCAL >> PAYMENT ["+request+"]");
			url = new URL(paymentUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod(method);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(60000);
			
			
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", appKey);
			conn.setRequestProperty("Connection", "close");
			
			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes("utf-8"));
			os.flush();
			os.close();
			
			int responseCode = conn.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				
				JSONObject result = (JSONObject)JSONValue.parseWithException(new InputStreamReader(is,"UTF-8"));
				JSONObject resultJson = (JSONObject)result.get("result");
				resMap.put("resultCd",(String)resultJson.get("resultCd"));	
				resMap.put("resultMsg",(String)resultJson.get("resultMsg"));	
				resMap.put("advanceMsg",(String)resultJson.get("advanceMsg"));	
			}
			
		} catch(Exception e) {
			resMap.put("resultCd", "9999");
			resMap.put("resultMsg", "통신오류");
			resMap.put("advanceMsg", "CONNECT ERROR ["+e.getMessage()+"] "+paymentUrl);
			logger.debug("PAYMENT URL REQUEST ERROR =["+e.getMessage()+"]");
			
		}finally{
			logger.debug("ElapsedTime : "+(long)(System.currentTimeMillis()-time)+"msec");
			conn.disconnect();
		}
		return resMap;
	}
}
