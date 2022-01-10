package com.pgmate.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.lib.util.comm.UrlClient;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

public class DanalUtil {
	
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.DanalUtil.class);
	static final String DN_CREDIT_URL 		= "https://tx_creditcard.danalpay.com/credit/";
	static final int DN_CONNECT_TIMEOUT 	= 5000;
	static final int DN_TIMEOUT 			= 30000;
	
	static final String ERC_NETWORK_ERROR 	= "-1";
	static final String ERM_NETWORK 		= "Network Error";
	static final String CHARSET 			= "EUC-KR";
	
	static final String IVKEY 				= "d7d02c92cb930b661f107cb92690fc83"; 
	
	public DanalUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public String refund(SharedMap<String, Object> sharedMap) {
		
		HashMap<String,String> req = new HashMap<String,String>();
		// CP 정보
		req.put("TID", 				sharedMap.getString("vanTrxId"));
		
		// 결제 정보
		req.put("AMOUNT", 			sharedMap.getString("amount"));
		req.put("ORDERID", 			sharedMap.getString("trackId") + "|" + sharedMap.getString("regId"));
		req.put("CANCELREQUESTER", 	"CP_CS_PERSON"); //취소 요청자. 로그성 자료.
		req.put("CANCELDESC", 		"Customer request refund"); //취소 사유.
		req.put("CANCELTYPE", 		"C"); //취소 사유.
		
		// 기본 정보
		req.put("TXTYPE", 			"CANCEL");
		req.put("SERVICETYPE", 		"DANALCARD");
		 
		//CPDAO dao = new CPDAO();
		//dao.setTable("PG_TRX_WH_CPID");
		//dao.setColumns("*");
		//dao.addWhere("cpid", sharedMap.getString("vanId"), CPDAO.eq);
		//SharedMap<String,Object> cpidMap = dao.search().getRow(0);
		String CRYPTOKEY				= "48b17753d19b230f19d7611d14a4a29e1e2eee3f5179b4b95a383c48709b70f3";
		
		HashMap<String,String> resD = connect(req, sharedMap.getString("vanId"), CRYPTOKEY/*cpidMap.getString("cryptokey")*/);
		//sharedMap.put("authCd", CommonUtil.toString(resD.get("CARDAUTHNO")));
		if(CommonUtil.toString(resD.get("RETURNCODE")).equals("0000")){
			sharedMap.put("result","정상취소");
		}else{
			sharedMap.put("result","취소실패");
		}
		
		SharedMap<String,Object> resMap = new SharedMap<String,Object>();
		resMap.put("orgTrxId", 			sharedMap.getString("trxId"));
		resMap.put("vanId", 			sharedMap.getString("vanId"));
		resMap.put("vanTrxId", 			CommonUtil.toString(resD.get("TID")));
		resMap.put("vanResultCd",		CommonUtil.toString(resD.get("RETURNCODE")));
		resMap.put("vanResultMsg",		CommonUtil.toString(resD.get("RETURNMSG")));
		resMap.put("amount", 			sharedMap.getString("amount"));
		resMap.put("regId", 			sharedMap.getString("regId"));
		resMap.put("regDay", 			CommonUtil.getCurrentDate("yyyyMMdd"));
		
		
		CPDAO dao = new CPDAO();
		List<Data> resList = new ArrayList<Data>();
		for(Entry<String, Object> each : resMap.entrySet()) {
			Data data = new Data();
			data.name = each.getKey();
			data.val = each.getValue();
			resList.add(data);
		}
		logger.debug("INSERT PG_TRX_ADMIN_RFD : {}", dao.insert("PG_TRX_ADMIN_RFD", resList));
		
		return sharedMap.toJson();
	}
	
	public HashMap<String,String> connect(HashMap<String,String> data, String CPID, String CRYPTOKEY){

		String req = "CPID="+CPID+"&DATA="+urlEncode(toEncrypt(toQueryString(data), CRYPTOKEY));
		logger.info("send : [{}]",req);
		HashMap<String,String> resData= new HashMap<String,String>();
		long time = System.currentTimeMillis();
		String message = "";
		UrlClient client = null;
		try {
			client = new UrlClient(DN_CREDIT_URL, "POST", "application/x-www-form-urlencoded; charset=euc-kr");
			client.setTimeout(DN_CONNECT_TIMEOUT,DN_TIMEOUT);
			client.setDoInputOutput(true, true);
			message = client.connect(req);
			
			logger.debug("recv : [{}]",message);
			if(message.indexOf("RETURNCODE") > -1){
				resData = parseQueryString(message);
			}else if(message.indexOf("DATA=") > -1){
				String decrypted = toDecrypt(urlDecode(message.split("=")[1]), CRYPTOKEY);
				resData = parseQueryString(decrypted);
			}else{
				logger.debug("danal,data error : {}",message);
			}
			
			
		} catch(Exception e) {
			message = "NOTCONNECTED";
			resData.put("RETURNCODE", "XXXX");
			resData.put("RETURNMSG", message);
			logger.debug("danal,error : {}",e.getMessage());
		}finally{
			logger.debug("danal Elasped Time =[{} sec]",CommonUtil.parseDouble((System.currentTimeMillis()-time)/1000) );
			logger.debug("danal, res = [{}]",resData);
			
		}
		return resData;
	}
	
	public static String trxRetry(String paymentUrl, String request, String van, String vanId) {
		if(paymentUrl.equals("cp.cyrexpay.com")) {
			paymentUrl = "http://cp.cyrexpay.com:8080";
		}else {
			paymentUrl = "http://pgwas2:10002";
		}
		
		if(van.startsWith("DANAL")) {
			paymentUrl += "/api/webhooks/danal/" + vanId;
		} else if(van.startsWith("NICE")){
			paymentUrl += "/api/webhooks/nice";
		} else if(van.startsWith("DAOU")){
			paymentUrl += "/api/webhooks/daou";
		} else if(van.startsWith("ALLAT")){
			paymentUrl += "/api/webhooks/allat";
		}else if(van.startsWith("WELCOME")){
			paymentUrl += "/api/webhooks/welcome";
		} else if(van.startsWith("KICC")){
			paymentUrl += "/api/webhooks/kicc";
		} else if(van.startsWith("SPC")){
			paymentUrl += "/api/webhooks/spc";
		}
		
		logger.info("REQ RETRY URL : {}" + paymentUrl);
		
		StringBuilder result = new StringBuilder();
		URL url = null;
		HttpURLConnection conn = null;
		
		//System.setProperty("https.protocols", "TLSv1.2");

		long time = System.currentTimeMillis();
		try {
			logger.info("LOCAL >> PAYMENT [" + request + "]");
			url = new URL(paymentUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(60000);

			conn.setRequestProperty("Content-Type", "text/html");
			//conn.setRequestProperty("Authorization", PayTest.PAY_KEY);
			//conn.setRequestProperty("Connection", "close");

			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes());
			os.flush();
			os.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line;
			while ((line = br.readLine()) != null)
				result.append(line + "\n");

			br.close();

		} catch (Exception e) {
			result.append("CONNECT ERROR [" + e.getMessage() + "] " + paymentUrl);
			logger.info("PAYMENT URL REQUEST ERROR =[" + e.getMessage() + "]");

		} finally {
			logger.info("ElapsedTime : " + (long) (System.currentTimeMillis() - time) + "msec");
			logger.info("LOCAL << PAYMENT [" + result.toString() + "]");
			conn.disconnect();
		}
		return result.toString();
	}
	
	public String toQueryString(HashMap<String,String> data){
		StringBuilder sb = new StringBuilder();
		for( String key : data.keySet() ){
			sb.append(key+"="+urlEncode(changeCharset(CommonUtil.nToB(data.get(key)),"euc-kr")));
			sb.append("&");
	    }
		if (sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		} else {
			return "";
		}
	}
	
	
	public HashMap<String,String> parseQueryString(String str){
		HashMap<String,String> data = new HashMap<String,String>();
		String[] st = str.split("&");

		for (int i = 0; i < st.length; i++) {
			int index = st[i].indexOf('=');
			if (index > 0)
				data.put(st[i].substring(0, index), changeCharset(urlDecode(st[i].substring(index + 1)),"utf-8"));
		}
		return data;

	}
	
	
	
	
	public String urlEncode(Object obj) {
		if (obj == null)
			return null;

		try {
			return URLEncoder.encode(obj.toString(), CHARSET);
		} catch (Exception e) {
			return obj.toString();
		}
	}

	/*
	 *  urlDecode
	 */
	public String urlDecode(Object obj) {
		if (obj == null)
			return null;

		try {
			return URLDecoder.decode(obj.toString(),CHARSET);
		} catch (Exception e) {
			return obj.toString();
		}
	}

	
	public String toEncrypt(String originalMsg, String CRYPTOKEY)  {
		String AESMode = "AES/CBC/PKCS5Padding";
		String SecetKeyAlgorithmString = "AES";
		//logger.debug("request : [{}]",originalMsg);
		IvParameterSpec ivspec = new IvParameterSpec(hexToByteArray(IVKEY));
		SecretKey keySpec = new SecretKeySpec(hexToByteArray(CRYPTOKEY), SecetKeyAlgorithmString);
		try{
			Cipher cipher = Cipher.getInstance(AESMode);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivspec);
			byte[] encrypted = cipher.doFinal(originalMsg.getBytes());
			return new String(Base64.encodeBase64(encrypted));
		}catch(Exception e){
			logger.info("Encrypt error : {}",e.getMessage());
			return "";
		}
	}
	
	public String toDecrypt(String originalMsg, String CRYPTOKEY) {
		//logger.debug("response : [{}]",originalMsg);
		String AESMode = "AES/CBC/PKCS5Padding";
		String SecetKeyAlgorithmString = "AES";

		IvParameterSpec ivspec = new IvParameterSpec(hexToByteArray(IVKEY));
		SecretKey keySpec = new SecretKeySpec(hexToByteArray(CRYPTOKEY), SecetKeyAlgorithmString);
		try{
			Cipher cipher = Cipher.getInstance(AESMode);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivspec);
			byte[] decrypted = cipher.doFinal( Base64.decodeBase64(originalMsg) );
			String retValue =  new String(decrypted);
    		
    		return retValue;
		}catch(Exception e){
			logger.info("Decrypt error : {}",e.getMessage());
			return "";
		}
	}
	
	private byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}
	
	 public String changeCharset(String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return new String(bytes, charset);
        } catch(UnsupportedEncodingException e) { }//Exception
        return "";
    }
}
