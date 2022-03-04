package com.pgmate.lib.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.map.SharedMap;

public class SmsUtil {

	private static Logger logger 				= LoggerFactory.getLogger( com.pgmate.lib.sms.SmsUtil.class);

	public static final String SMS_URL = "http://link.smsceo.co.kr/sendsms_utf8.php"; //일반문자 (한글45자)
	public static final String LMS_URL = "http://link.smsceo.co.kr/sendlms_utf8.php"; //장문문자 (한글1000자)
	public static final String MMS_URL = "http://link.smsceo.co.kr/sendmms_utf8.php"; //그림문자 (한글1000자)
	
	public SmsUtil() {
		
	}
	
	
	public static void sendSms(String sendUrl, String phone, String msg){
		
		String userKey = "VG8HMwo6Bz1VY1E2Ai0AMAQ6AXMDPARlA2xdbwh+UHFWIA==";
		String userId = "bkwinners";
		String callBack = "0517516422";
		String SendPhoneNum = phone.replaceAll("-", "");
		String sendMsg = msg;
		
		SharedMap<String, String> jsonMap = new SharedMap<String, String>();

		jsonMap.put("userkey", userKey);
		jsonMap.put("userid", userId);
		jsonMap.put("phone", SendPhoneNum);
		jsonMap.put("callback", callBack);
		jsonMap.put("msg", sendMsg);
		StringJoiner sj = new StringJoiner("&");
		
		for(Map.Entry<String, String> entry : jsonMap.entrySet()) {
			sj.add(entry.getKey() + "=" + entry.getValue());
		}
		
		String parameter = sj.toString();
		
		logger.debug("parameter [{}]", parameter);
		
		
		try {
			URL url = new URL(sendUrl);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);

			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);
			
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(parameter);
			wr.flush();
			
			String resData = "";
			StringBuilder sb2 = new StringBuilder();
			
			
			if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb2.append(line).append("\n");
				}
				br.close();
				
				resData = sb2.toString();
				logger.debug("resData [{}]",resData);
				
				
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.debug("lib.sms.SmsUtil Error");
		}
	}

}
