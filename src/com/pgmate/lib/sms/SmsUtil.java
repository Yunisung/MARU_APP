package com.pgmate.lib.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.StringJoiner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
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

	public static void aligoProcess(String phone, String msg) {
		//알리고에 잔액 있는지 확인후 전송
		SharedMap<String, Object> check = checkSms();
		if(check.getInt("SMS_CNT") > 1) {
			SharedMap<String, Object> send = sendAligoSms(phone, msg);
			if(!send.getString("result_code").equals("1")) {
				logger.error("SMS 전송실패 : {}", send.getString("message"));
				sendSms(SMS_URL, phone, msg);
			}
		} else {
			logger.error("알리고 잔액부족 : {}", check.getString("message"));
			sendSms(SMS_URL, phone, msg);
		}
	}

	public static SharedMap<String, Object> checkSms() {
		//알리고 잔액확인
		SharedMap<String, Object> resultMap = new SharedMap<>();
		String remain_url = "https://apis.aligo.in/remain/";

		SharedMap<String, String> remain = new SharedMap<>();
		remain.put("user_id", "bukook");
		remain.put("key", "a1jmo0aeize877b8mhd8hpfoite2oeeu");

		StringJoiner joiner = new StringJoiner("&");
		for(Map.Entry<String, String> entry : remain.entrySet()) {
			joiner.add(entry.getKey()+"="+entry.getValue());
		}
		String param = joiner.toString();

		try {
			URL url = new URL(remain_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			os.flush();
			os.close();

			String result = "";
			String buffer = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while((buffer = in.readLine())!=null){
				result += buffer;
			}

			in.close();

			//json처리
			Gson gson = new Gson();
			Type type = new TypeToken<SharedMap<String, Object>>(){}.getType();
			resultMap = gson.fromJson(result, type);

			logger.info(resultMap.toString());
		} catch (Exception e){
			logger.error(e.getMessage());
		}

		return resultMap;
	}

	public static SharedMap<String, Object> sendAligoSms(String phone, String msg) {
		//알리고 메세지 전송
		SharedMap<String, Object> resultMap = new SharedMap<>();
		String send_url = "https://apis.aligo.in/send/";

		SharedMap<String, String> sms = new SharedMap<>();
		sms.put("user_id", "bukook");
		sms.put("key", "a1jmo0aeize877b8mhd8hpfoite2oeeu");
		sms.put("msg", msg);
		sms.put("receiver", phone);
		sms.put("sender", "1644-1109");

		StringJoiner joiner = new StringJoiner("&");
		for(Map.Entry<String, String> entry : sms.entrySet()) {
			joiner.add(entry.getKey()+"="+entry.getValue());
		}
		String param = joiner.toString();

		try {
			URL url = new URL(send_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			os.flush();
			os.close();

			String result = "";
			String buffer = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while((buffer = in.readLine())!=null){
				result += buffer;
			}

			in.close();

			//json처리
			Gson gson = new Gson();
			Type type = new TypeToken<SharedMap<String, Object>>(){}.getType();
			resultMap = gson.fromJson(result, type);
			logger.info(resultMap.toString());
		} catch (Exception e){
			logger.error(e.getMessage());
		}

		return resultMap;
	}
	
	
	public static void sendSms(String sendUrl, String phone, String msg){
		
		String userKey = "VG8HMwo6Bz1VY1E2Ai0AMAQ6AXMDPARlA2xdbwh+UHFWIA==";
		String userId = "bkwinners";
		String callBack = "16441109";
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
						new InputStreamReader(con.getInputStream(), "EUC-KR"));
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
