package com.pgmate.app.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EformUtil {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.EformUtil.class);
	
	private static String apiID = "";
	private static String apiKEY = "";
	
	/*
	 * 발급받은 id, key를 이용해서 액세스 토큰 발급 (10분간 유효)
	 */
	public static String eform_token() throws Exception {
		configSetting();
		
		String apiUrl = "https://api.eform.io/v2/token";
		String setMethod = "GET";
		String id = apiID;

		JSONObject resJson = connectionRes(apiUrl, "", setMethod, id);

		String token = (String) resJson.get("access_token");

		return token;
	}
	
	/*
	 * 전자계약서 목록 가져오기
	 */
	public static JSONObject eform_list() throws Exception {
		String token = EformUtil.eform_token();
		String apiUrl = "https://api.eform.io/v2/form";
		String setMethod = "GET";

		JSONObject resJson = EformUtil.connectionRes(apiUrl, token, setMethod, "");

		return resJson;
	}
	
	/*
	 * 이폼 서비스 API 연동
	 */
	public static JSONObject connectionRes(String urlAddr, String token, String setMethod, String apiId)
			throws Exception {
		JSONObject apiRes = new JSONObject();

		configSetting();
		
		URL url = new URL(urlAddr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("x-api-key", apiKEY);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		if (setMethod.equals("GET")) {
			conn.setRequestMethod("GET");
		} else if (setMethod.equals("POST")) {
			conn.setRequestMethod("POST");
		}

		if (apiId != null && !"".equals(apiId)) {
			conn.setRequestProperty("x-api-id", apiId);
		}

		if (token != null) {
			conn.setRequestProperty("x-access-token", token);
		}

		try {
			if (conn.getResponseCode() != 200) {
				logger.error("connectionRes Connection error : " + conn.getResponseCode());
			} else {
				String line = "";
				String result = "";
				InputStream in = new BufferedInputStream(conn.getInputStream());
				try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
					line = br.lines().collect(Collectors.joining(System.lineSeparator()));
				}
				result = line;

				in.close();

				JSONParser jParser = new JSONParser();
				apiRes = (JSONObject) jParser.parse(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return apiRes;
	}
	
	/**
     * config 파일 읽어서 변수에 세팅
     */
    public static void configSetting() {
    	try{
            //프로퍼티 파일 위치
    		//운영
            //String propFile = "/home/MARU/MARU_APP/conf/eform.properties"; 
    		//테스트
    		String propFile = "/home/MARU/MARU_APP/conf/eform.properties";
    		//로컬
    		//String propFile = "C:/01/MARU_APP/conf/eform.properties";
    		 
            // 프로퍼티 객체 생성
            Properties props = new Properties();
            
            // 프로퍼티 파일 스트림에 담기
            FileInputStream fis = new FileInputStream(propFile);

            // 프로퍼티 파일 로딩
            props.load(new java.io.BufferedInputStream(fis));
            
            // 항목 읽기
            apiID = props.getProperty("apiID");
            apiKEY = props.getProperty("apiKEY");
        }catch(Exception e){
        	logger.info(e.getMessage(), e);
        }
    }
}
