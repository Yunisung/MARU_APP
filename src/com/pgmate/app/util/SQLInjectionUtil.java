package com.pgmate.app.util;

import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLInjectionUtil {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.SQLInjectionUtil.class);
	
	public static String changeValue(String orgVal) {
		// 특수문자 필터링을 위해 특수문자를 정의
		// pys : 암호화때문에 = 뺌
		Pattern evilChars = Pattern.compile("[’‘`'\"\\#;]");
		//Pattern evilChars = Pattern.compile("[’‘`'\"\\#;=]");
		
		// 특수문자는 모두 공백으로 치환
		orgVal = evilChars.matcher(orgVal).replaceAll("");
		
		/**
			KBR 
			compile : 특수문자 정규식으로 패턴화 
			matcher : compile된 정규식과 String 값 구문하여 object형식으로 반환 (replceAll메소드를 통해 치환 가능) 
		 **/

		// 특수 구문 필터링
		String test_str_low = orgVal.toLowerCase();
		
		// KBR : 전달되는 매개변수 값 내용중 아래내용과 일치 할 경우
		if (test_str_low.contains("union") || test_str_low.contains("select") || test_str_low.contains("insert")
				|| test_str_low.contains("drop") || test_str_low.contains("update") || test_str_low.contains("delete")
				|| test_str_low.contains("join") || test_str_low.contains("from") || test_str_low.contains("where")
				|| test_str_low.contains("substr") || test_str_low.contains("information_schema")
				|| test_str_low.contains("table_schema")) {
			orgVal = test_str_low;
			orgVal = orgVal.replaceAll("-", "&#45;");
			//orgVal = orgVal.replaceAll("@", "&#64;");
			//orgVal = orgVal.replaceAll("(", "&#40;");
			//orgVal = orgVal.replaceAll(")", "&#41;");

			orgVal = orgVal.replaceAll("union", "q-union");
			orgVal = orgVal.replaceAll("select", "q-select");
			orgVal = orgVal.replaceAll("insert", "q-insert");
			orgVal = orgVal.replaceAll("drop", "q-drop");
			orgVal = orgVal.replaceAll("update", "q-update");
			orgVal = orgVal.replaceAll("delete", "q-delete");
			orgVal = orgVal.replaceAll("and", "q-and");
			orgVal = orgVal.replaceAll("or", "q-or");
			orgVal = orgVal.replaceAll("join", "q-join");
			orgVal = orgVal.replaceAll("substr", "q-substr");
			orgVal = orgVal.replaceAll("from", "q-from");
			orgVal = orgVal.replaceAll("where", "q-where");
			orgVal = orgVal.replaceAll("declare", "q-declare");
			orgVal = orgVal.replaceAll("openrowset", "q-openrowset");
			orgVal = orgVal.replaceAll("information_schema", "q-information_schema");
			orgVal = orgVal.replaceAll("table_schema ", "q-table_schema");
			orgVal = orgVal.replaceAll("table_name", "q-table_name");
			orgVal = orgVal.replaceAll("column_name", "q-column_name");
			orgVal = orgVal.replaceAll("row_num", "q-row_num");
		}
		return orgVal;
	}
	
	public static boolean checkInjectionValue(String orgVal) {
		// 특수 구문 필터링
		String test_str_low = orgVal.toLowerCase();
		if (test_str_low.contains("union") || test_str_low.contains("select") || test_str_low.contains("insert")
				|| test_str_low.contains("drop") || test_str_low.contains("update") || test_str_low.contains("delete")
				|| test_str_low.contains("join") || test_str_low.contains("from") || test_str_low.contains("where")
				|| test_str_low.contains("substr") || test_str_low.contains("information_schema") 
				|| test_str_low.contains("table_schema")) {
			return false;
		}
		return true;
	}
	
	public static String xssChange(String orgVal) {
		// 특수문자 필터링을 위해 특수문자를 정의
		//pys : 암호화때문에 = 뺌
		Pattern evilChars = Pattern.compile("[’‘`\\=]");
		// 특수문자는 모드 공백으로 치환
		//pys : 일단 주석처리
		//orgVal = evilChars.matcher(orgVal).replaceAll("");
		
		// 특수 문자 필터링
		//KJM : 온라인결제시 db에 치환된 값으로 들어감 -> 주석처리
		//orgVal = orgVal.replaceAll("'", "&#39;");
		//orgVal = orgVal.replaceAll("\"", "&#34;");
		// orgVal = orgVal.replaceAll("-", "&#45;");
		// orgVal = orgVal.replaceAll("(", "&#40;");
		// orgVal = orgVal.replaceAll(")", "&#41;");
		orgVal = orgVal.replaceAll("<", "&#60;");
		//pys : = 는 입력되게 변경(암호화이슈)
		//orgVal = orgVal.replaceAll("=", "&#61;");
		orgVal = orgVal.replaceAll(">", "&#62;");
		//orgVal = orgVal.replaceAll("@", "&#64;");
		
		//KJM : res 카맬 풀리는 문제로 주석처리
		// 특수 구문 필터링
//		String test_str_low = orgVal.toLowerCase();
//		if (test_str_low.indexOf("script") > -1) {
//			orgVal = test_str_low;
//			orgVal = orgVal.replaceAll("script", "q-script");
//		}
		
		return orgVal;
	}
}
