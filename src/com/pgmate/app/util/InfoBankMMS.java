package com.pgmate.app.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.CodeDAO;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class InfoBankMMS{

	private static Logger logger	= LoggerFactory.getLogger( com.pgmate.app.util.InfoBankMMS.class );
	private String MMS_URL	= "https://file.supersms.co:7010/sms/v3/file"; 
	private String SMS_URL	= "https://sms.supersms.co:7020/sms/v3/multiple-destinations";
	
	public InfoBankMMS() {
	}
	
	public String getMmsKey(File file) throws Exception {
		String getFileKey = "";
		String resData = "";
		String boundary = "----------"+UUID.randomUUID().toString();
		String getName = new String(file.getName().getBytes("UTF-8"), "8859_1");

		try {
			URL url = new URL(MMS_URL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			String authKey = new CodeDAO().getInfoBankSmsKey();
			
			con.setRequestMethod("POST");
			con.addRequestProperty("Authorization", authKey);
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
			con.addRequestProperty("Accept", "application/json");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			con.setRequestProperty("charset", "UTF-8");
			
			OutputStream outputStream = con.getOutputStream();
			DataOutputStream dataStream = new DataOutputStream(con.getOutputStream());
			dataStream.writeBytes("--"+boundary+"\r\n");
			dataStream.writeBytes("Content-Disposition: form-data; name=\"attachment\"; filename=\""+getName+"\"" + "\r\n");
			dataStream.writeBytes("Content-Type: image/jpeg" + "\r\n");
			dataStream.writeBytes("\r\n");
			
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
			    outputStream.write(buffer, 0, bytesRead);
			}
			dataStream.writeBytes("\r\n"); 
			dataStream.writeBytes("--" + boundary + "--" + "\r\n");
			outputStream.flush();
			inputStream.close();

			JSONObject obj = new JSONObject();
			StringBuilder sb = new StringBuilder();
			String line;
			if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				resData = sb.toString();
				logger.debug("resData [{}]", resData);
				
				JSONParser parse = new JSONParser();
				obj = (JSONObject) parse.parse(resData);
				getFileKey = (String) obj.get("fileKey");
			}  else {
				logger.error("Connection error : {}, {}", con.getResponseCode(), con.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("getMmsKey Exception : {} ", e);
		}
		return getFileKey;
	}
	
	public void sendMms(String phone, String msg, String fileKey){
		SharedMap<String, Object> jsonMap = new SharedMap<String, Object>();
		List<SharedMap<String, Object>> list = new ArrayList<SharedMap<String,Object>>();
		SharedMap<String, Object> map2 = new SharedMap<String, Object>();

		map2.put("to", "82"+phone.substring(1).replaceAll("-", ""));
		list.add(map2);
		jsonMap.put("destinations", list);
		jsonMap.put("from","18551838");
		jsonMap.put("ttl","0");
		jsonMap.put("text",msg);
		jsonMap.put("fileKey", fileKey); //MMS 발송 시 파일 키
		
		String json = jsonMap.toJson();

		try {
			URL url = new URL(SMS_URL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			
			String authKey = new CodeDAO().getInfoBankSmsKey();
			
			con.addRequestProperty("Accept", "application/json");
			con.addRequestProperty("Authorization", authKey);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");

			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);
			
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(json);
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
		}
	}
}
