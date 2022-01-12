package com.pgmate.lib.util.gson;

import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class GsonUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.gson.GsonUtil.class );
	private static String DEFAULT_DATE_PATTERN = "yyyy/MM/dd HH:mm:ss.S";
	
	//KJM : json형식으로 변환
	public static String toJson(Object obj){
		return toJson(obj,false,"");
	}
	
	public static String toJsonExcludeStrategies(Object obj){
		
		return toJsonExcludeStrategies(obj,false);
	}
	
	public static String toJsonExcludeStrategies(Object obj,boolean pretty){
		if(obj == null){ return "";}
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setExclusionStrategies(new UserExclusionStrategy());
		if(pretty){
			gsonBuilder.setPrettyPrinting();
		}
		return gsonBuilder.create().toJson(obj);
	}
	
	// KBR : JAVA객체 JSON객체로 변환 메소드
	public static String toJson(Object obj,boolean pretty,String datePattern){
		
		// KBR : 셋팅 된 데이터 값 없을 시 리턴
		if(obj == null){ return "";}
		
		/**
		 * Gson은 Java에서 Json을 파싱하고, 생성하기 위해 사용되는 구글에서 개발한 오픈소스입니다.
		 * Java Object를 Json 문자열로 변환할 수 있고, Json 문자열을 Java Object로 변환할 수 있습니다.
		 **/	
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		if(pretty){
			gsonBuilder.setPrettyPrinting();
		}
		
		// KBR : 공백이 아니면 
		if(!datePattern.equals("")){
			// 인자값 날짜형식로 포맷
			gsonBuilder.setDateFormat(datePattern);
		}else{
			// 공백이면 기본 날짜 형식으로 포맷 
			gsonBuilder.setDateFormat(DEFAULT_DATE_PATTERN);
		}
		return gsonBuilder.serializeSpecialFloatingPointValues().create().toJson(obj);
		
		// pys : 암호화에 특수문자가 무조건 들어가므로 escaping기능 비활성화
		// KBR : java object 를 json형태로 만들어 return
		//return gsonBuilder.disableHtmlEscaping().create().toJson(obj);
	}
	
	
	public static boolean toJsonFileWrite(Object obj,boolean pretty,String datePattern,String file){
		boolean isCreated = false;
		String json = toJson(obj, pretty, datePattern);
		try{
			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();
			isCreated = true;
		}catch(Exception e){
			System.out.println("에러 1");
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return isCreated;
		
	}
	
	public static String toPrettyFormat(String jsonString){
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }
	
	
	public static Object fromJson(String json,Object obj){
		return fromJson(json, obj.getClass(), DEFAULT_DATE_PATTERN);
	}
	public static Object fromJson(String json,Object obj,String datePattern){
		return fromJson(json, obj.getClass(), datePattern);
	}
	
	//KJM : json 형식의 데이터 형식 변환
	public static Object fromJson(String json,Class<?> clazz){
		return fromJson(json, clazz, DEFAULT_DATE_PATTERN);
	}
	
	//KJM : json -> object 형식으로 변환
	public static Object fromJson(String json,Class<?> clazz,String datePattern){
		Object obj = null;
		try{
			
			/*	KJM 
			 *	Gson : json -> object 변환
			 *	fromJson : 첫번째 인자(json형식의 데이터), 두번째 인자(변환을 원하는 java의 class 반환)
			 */
			obj = new GsonBuilder().setDateFormat(datePattern).create().fromJson(json, clazz);
			
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return obj;
	}
}
