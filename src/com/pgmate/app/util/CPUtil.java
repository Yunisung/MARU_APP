package com.pgmate.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.io.FileUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class CPUtil {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.CPUtil.class);

	public static final String CP_TYPE_INSERT 		= "insert";		//RECORD INSERT
	public static final String CP_TYPE_UPDATE		= "update";		//RECORD UPDATE
	public static final String CP_TYPE_SEARCH		= "search";		//단일 RECORD  요청
	public static final String CP_TYPE_LIST			= "list";		//LIST RECORD 요청
	public static final String CP_TYPE_UPLOAD		= "upload";		//UPLOAD 결과
	public static final String CP_TYPE_DONWLOAD		= "down";		//UPLOAD 결과
	public static final String CP_TYPE_FILE_PDF 	= "pdf";		//PDF File 요청
	public static final String CP_TYPE_FILE_EXL		= "excel";		//Excel File 요청
	public static final String CP_TYPE_FILE_TXT		= "txt";		//Text File 요청
	public static final String CP_TYPE_FILE_CSV		= "csv";		//CSV File 요청
	public static final String CP_TYPE_FILE_IMG		= "image";		//IMAGE File 요청
	public static final String CP_TYPE_FILE_HTML	= "html";		//HTML File 요청
	public static final String CP_TYPE_ACCESS		= "access";		//PAGE 접근 권한 요청 
	
	
	
	public static final int RESULT_OK				= 200;			//성공 결과 
	public static final int RESULT_NOK				= 600;			//실패 등.	
	public static final int RESULT_NOT_FOUND		= 601;			//결과 검색 실패 
	public static final int RESULT_ACCESS_DENIED	= 602;			//엑세스 거부
	public static final int RESULT_SESSION_EXPIRED	= 603;			//세션 종료
	public static final int RESULT_BAD_REQUEST		= 604;			//요청 값에 오류가 있을 경우 
	public static final int RESULT_ERROR			= 700;			//시스템 오류
	
	
	public static final String RESULT_OK_MSG			= "OK";			
	public static final String RESULT_NOK_MSG			= "NOK";
	public static final String RESULT_NOT_FOUND_MSG		= "데이터가 없습니다.";
	public static final String RESULT_ACCESS_DENIED_MSG	= "access denied";
	public static final String RESULT_SESSION_EXPIRED_MSG= "session expired";
	public static final String RESULT_BAD_REQUEST_MSG	=  "bad request";		 
	public static final String RESULT_ERROR_MSG			= "system error";
	
	
	public static final String RESULT_DATA_INSERTED	 	= "등록 성공하였습니다.";
	public static final String RESULT_DATA_UPDATED	 	= "업데이트 성공하였습니다.";
	public static final String RESULT_DATA_DELETED	 	= "data deleted";
	public static final String RESULT_DATA_INFAIL		= "등록 실패하였습니다.";
	public static final String RESULT_DATA_UPFAIL	 	= "업데이트 실패하였습니다.";
	public static final String RESULT_DATA_DELFAIL	 	= "delete failure";
	 
	public static final String CP_SESSION			= "CP_SESSION";	//Session Attribute NAME
	public static final int CP_SESSION_TIMEOUT		= 30*160;		//30 minutes
	public static final int CP_SESSION_TIMEOUT_KWON	= 30*160;		//30 minutes -- 본사용
	
	public static boolean CP_DEBUG					= true;
	public static boolean CP_DEV_SESSION			= false;
	
	public static final String CP_UPLOAD_DIR		= "/upload";
	public static final String CP_TEMPLATE_DIR		= "template";
	
	//KJM : 사용하는 운영체제에 맞는 파일 경로 지정
	public static String getUploadDir() {
		String path = "";
		switch (System.getProperty("os.name")) {
			case "Linux":
				path = "/home/data/app";
				break;
		    case "Windows":
		    	path = "D://tmp//upload";
		    	break;
		}
		return path;
	}
	
	/* KJM : page.current 값 설정에서 page.size 값 설정으로 코드 수정 함
	 * if(page.size == 0) { page.current = 10; }
	 */
	
	public static Page correctPage(Page page){
		
		if(page == null){
			page = new Page();
		}
		if(page.current == 0){
			page.current = 1;
		}
		/* KJM : page.size가 0일 때 코드 수정
		 * if(page.size == 0) { page.current = 10; }
		 */
		if(page.size == 0){
			page.size = 20;
		}
		return page;
	}
	
	/**
	 * 210812_PYS : Data클래스를 이용해서 SQL검색
	 * @param dao
	 * @param datas
	 */
	// 쿼리문에 들어갈 where ~ orderBy 절 세팅
	public static void setDAO(DAO dao,List<Data> datas){
		//데이터가 없을 때 setDAO 함수 종료
		if(datas == null){
			return;
		}
		
		//orderBy = 정렬
		StringBuilder orderBy = new StringBuilder();

		//데이터 수만큼 반복
		for(Data data:datas){ 
			if(data.key){
				//value가 존재 하면 
				if(!CommonUtil.toString(data.val).equals("")) {
					// value값 분석하여 String값으로 치환
					String str = CommonUtil.toString(data.val);
					// str 문장의 특수문자 제거
					// changeValue = 매개변수값이 SQL 명령문 중 하나인지 체크하며, 특수문자 치환 메소드
					String convaerted = SQLInjectionUtil.changeValue(str);
					// 치환이 이뤄졌다면
					if(!str.equalsIgnoreCase(convaerted)) {
						// 치환 된 값으로 벨류값 셋팅
						data.val = convaerted;
						logger.warn("==== SQL INJECTION CHECK : {} => {}", str, convaerted);
					}
				}
				// 조건문 생성
				dao.addWhere(data.name,data.val,data.oper);
//				dao.sysTest("할당");
			}else{
				//값 직접 지정 시
				dao.setRecord(data.name, data.val);
			}
			
			// 정렬값 있을  때
			if(!data.order.equals("")){ 
				if(orderBy.length() !=0){
					orderBy.append(",");
				}
				/*	
				 *  StringBuilder를 이용해 orderBy에 문자열 붙임 
				 *  orderBy = "data.name data.order,data.name data.order, ..."
				 */
				orderBy.append(data.name);
				orderBy.append(" ");
				orderBy.append(data.order);
			} 
			
		} // for end
		
		//orderBy의 길이가 2이상일 때 = 정렬 값 있을 때
		if(orderBy.toString().trim().length() > 1){
			dao.setOrderBy(orderBy.toString());
		}
	}
	
	public static void setOperDAO(DAO dao,List<Data> datas){
		if(datas == null){
			return;
		}
		StringBuilder orderBy = new StringBuilder(); 
		for(Data data:datas){
			if(data.key){
				if(!CommonUtil.toString(data.val).equals("")) {
					String str = CommonUtil.toString(data.val);
					String convaerted = SQLInjectionUtil.changeValue(str);
					if(!str.equalsIgnoreCase(convaerted)) {
						data.val = convaerted;
						logger.warn("==== SQL INJECTION C HECK : {} => {}", str, convaerted);
					}
				}
				dao.addWhere(data.name,data.val,data.oper);
			}else{
				dao.setRecord(data.name, data.val,data.oper);
			}
			if(!data.order.equals("")){
				if(orderBy.length() !=0){
					orderBy.append(",");
				}
				orderBy.append(data.name);
				orderBy.append(" ");
				orderBy.append(data.order);
			}
		}
		if(orderBy.toString().trim().length() > 1){
			dao.setOrderBy(orderBy.toString());
		}
	}
	
	public static void setRedisDAO(DAO dao,List<Data> datas){
		if(datas == null){
			return;
		}
		
		for(Data data:datas){
			if(data.key){
			}else{
				dao.setRecord(data.name, CommonUtil.toString(data.val));
			}	
		}
		
	}
	
	
	public static String postToString(HttpServletRequest request){
		StringBuilder buf = new StringBuilder();
		String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					buf.append(line);
			} catch (Exception e) { /*report an error*/ }

		return buf.toString();
	}
	
	
	public static void setUploadDirectory(){
		FileUtil fileUtil = new FileUtil();
		String path = CPUtil.getUploadDir()+CommonUtil.getCurrentDate("yyyyMMdd");
		if(!fileUtil.existDirectory(CPUtil.getUploadDir())){
			fileUtil.createDirectory(CPUtil.getUploadDir());
		}
		if(!fileUtil.existDirectory(path)){
			fileUtil.createDirectory(path);
		}
	}
	
	public static void setTemplateDirectory(String directory){
		FileUtil fileUtil = new FileUtil();
		//파일 path에 현재 날짜 더함
		String path = directory+CommonUtil.getCurrentDate("yyyyMMdd");
		
		//해당 경로(날짜없는)에 파일이 없을 경우
		if(!fileUtil.existDirectory(directory)){
			//폴더 생성
			fileUtil.createDirectory(directory);
		}
		//해당 경로(날짜있는)에 파일이 없을 경우
		if(!fileUtil.existDirectory(path)){
			//폴더 생성
			fileUtil.createDirectory(path);
		}
	}
	
	
	public static void merge(RecordSet rset, RecordSet add){
		for(SharedMap<String,Object> data:add.getRows()){
			rset.addRow(data);
		}
	}
	
	public static RecordSet mergeValue(RecordSet rset, RecordSet add){
		RecordSet newRecord = new RecordSet();
		SharedMap<String,Object> data = (SharedMap<String,Object>)rset.getRows().get(0);
		if(add.size() > 0){
			SharedMap<String,Object> data2 = (SharedMap<String,Object>)add.getRows().get(0);
			data.putAll(data2);
		}
		newRecord.addRow(data);
		return newRecord;
	}
	
		public static String getCanonicalPath(){
		String path = "";
		try{
			path = new File("../").getCanonicalPath();
		}catch(Exception e){}
		return path;
	}
	
	//엑셀 파일 연결 경로 설정
	public static String getCanonicalWebPath(){
		String path = "";
		try{
			//0916기준으로 C:\git\creditop\web
			path = new File("../web").getCanonicalPath();
		}catch(Exception e){}
		return path;
	}
	
	public static String getCanonicalTemplatePath(){
		return getCanonicalPath()+File.separator+"web"+File.separator+"assets"+File.separator+"tpl";
	}
	
	
}
