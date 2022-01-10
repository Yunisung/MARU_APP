package com.pgmate.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.CodeDAO;
import com.pgmate.lib.util.map.SharedMap;



/**
 * @author Administrator
 *
 */
public class InfoBankSMS{

	private static Logger logger 				= LoggerFactory.getLogger( com.pgmate.app.util.InfoBankSMS.class );
	private final String SMS_URL = "https://sms.supersms.co:7020/sms/v3/multiple-destinations"; 
	
	public InfoBankSMS() {
		
	}
	
	
	public void sendSms(String phone, String msg){
		
		SharedMap<String, Object> jsonMap = new SharedMap<String, Object>();
		List<SharedMap<String, Object>> list = new ArrayList<SharedMap<String,Object>>();
		SharedMap<String, Object> map2 = new SharedMap<String, Object>();
		
		map2.put("to", "82"+phone.substring(1).replaceAll("-", ""));
		list.add(map2);
		jsonMap.put("destinations", list);
		jsonMap.put("from","18551838");
		jsonMap.put("ttl","0");
				
		jsonMap.put("text",msg);
		String json = jsonMap.toJson();
		
		System.out.println(json);
		
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
