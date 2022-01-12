package com.pgmate.app.util;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import com.pgmate.app.export.CPDocument;
import com.pgmate.app.export.Template;
import com.pgmate.app.export.XlsExport;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Files;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.model.ajax.Result;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.lib.util.xml.XmlUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Administrator
 *
 */
//view단으로 보내주는 데이터 가공
public class CPRUtil {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.util.CPRUtil.class );

	// request에 저장된 정보 값을 view에 전송할 때 필요한 변수
	private CPResponse cpResponse 	= null;
	// 문서화 정보
	private CPDocument doc			= null;
	// excel , pdf 파일에 필요한 컬럼 정보 값
	private LinkedHashMap<String,String> thead = null;
	
	// response 값 셋팅 (데이터 값 제외)
	public CPRUtil(CPRequest request) {
		
		if(CPUtil.CP_DEBUG){
			logger.debug("json request : {}",GsonUtil.toJson(request, true, ""));
		}
		
		cpResponse 			= new CPResponse();
		cpResponse.type 		= request.type;
		
		// 수정이나 등록 후 이전 view페이지로 redirect하기 위한 셋팅 값 
		// ex ) 가맹점 수정 후 이전 view 맵핑값 (/mcht/view/test1/tab_basic) 으로 redirect
		cpResponse.redirect 	= request.redirect;
		
		if(request.page != null){
			cpResponse.page		= request.page;
		}
		// thead는 가맹점이 아닐경우 모두 다 값이 셋팅 됨
		if(request.thead != null){
			cpResponse.thead		= request.thead;
		}
		// 카테고리 명이 있고 엑셀이거나 pdf 일 때 
		if (request.reason != null && cpResponse.type.equals("excel") || cpResponse.type.equals("pdf")) {
			doc = new CPDocument(request.reason, "Export DATA", "SYSTEM");
		}
	}
	
	public CPRUtil(){
		this.cpResponse 			= new CPResponse();
	}
	/**
	 * 200,OK 응답 지정
	 * @return
	 */
	public CPRUtil resultOK(){
		return result(CPUtil.RESULT_OK,CPUtil.RESULT_OK_MSG,"");
	}
	
	public CPRUtil resultOK(String message){
		return result(CPUtil.RESULT_OK,message,"");
	}
	
	/**
	 * 600 등록 , 수정 실패 등.
	 * @param message
	 * @return
	 */
	public CPRUtil resultNOK(){
		return result(CPUtil.RESULT_NOK,CPUtil.RESULT_NOK_MSG,"");
	}

	/**
	 * 600 등록 , 수정 실패 등.
	 * @param message
	 * @return
	 */
	public CPRUtil resultNOK(String message){
		return result(CPUtil.RESULT_NOK,message,"");
	}
	/**
	 * 600 등록 , 수정 실패 등.
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultNOK(String message,String error){
		return result(CPUtil.RESULT_NOK,message,error);
	}
	
	public CPRUtil resultNOT_FOUND(){
		return result(CPUtil.RESULT_NOK,CPUtil.RESULT_NOT_FOUND_MSG,"");
	}
	
	/**
	 * 601 검색 결과가 없을 경우 
	 * @param message
	 * @return
	 */
	public CPRUtil resultNOT_FOUND(String message){
		return result(CPUtil.RESULT_NOT_FOUND,message,"");
	}
	/**
	 * 601 검색 결과가 없을 경우 
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultNOT_FOUND(String message,String error){
		return result(CPUtil.RESULT_NOT_FOUND,message,error);
	}
	
	public CPRUtil resultACCESS_DENIED(){
		return result(CPUtil.RESULT_ACCESS_DENIED,CPUtil.RESULT_ACCESS_DENIED_MSG,"");
	}

	/**
	 * 602 접근 거부 메세지
	 * @param message
	 * @return
	 */
	public CPRUtil resultACCESS_DENIED(String message){
		return result(CPUtil.RESULT_ACCESS_DENIED,message,"");
	}
	/**
	 * 602 접근 거부 메세지
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultACCESS_DENIED(String message,String error){
		return result(CPUtil.RESULT_ACCESS_DENIED,message,error);
	}
	
	public CPRUtil resultSESSION_EXPIRED(){
		return result(CPUtil.RESULT_SESSION_EXPIRED,CPUtil.RESULT_SESSION_EXPIRED_MSG,"");
	}
	
	/**
	 * 603 세션 종료
	 * @param message
	 * @return
	 */
	public CPRUtil resultSESSION_EXPIRED(String message){
		return result(CPUtil.RESULT_SESSION_EXPIRED,message,"");
	}
	/**
	 * 603 세션 종료
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultSESSION_EXPIRED(String message,String error){
		return result(CPUtil.RESULT_SESSION_EXPIRED,message,error);
	}
	
	public CPRUtil resultBAD_REQUEST(){
		return result(CPUtil.RESULT_BAD_REQUEST,CPUtil.RESULT_BAD_REQUEST_MSG,"");
	}
	
	/**
	 * 604 잘 못된 요청 이 수신된 경우
	 * @param message
	 * @return
	 */
	public CPRUtil resultBAD_REQUEST(String message){
		return result(CPUtil.RESULT_BAD_REQUEST,message,"");
	}
	/**
	 *604 잘 못된 요청 이 수신된 경우
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultBAD_REQUEST(String message,String error){
		return result(CPUtil.RESULT_BAD_REQUEST,message,error);
	}
	
	/**
	 * 700 시스템 오류
	 * @param message
	 * @return
	 */
	public CPRUtil resultERROR(String message){
		return result(CPUtil.RESULT_ERROR,message,"");
	}
	/**
	 * 700 시스템 오류
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultERROR(String message,String error){
		return result(CPUtil.RESULT_ERROR,message,error);
	}
	
	/**
	 * 코드,메세지,에러 메세지 직접 회신 
	 * @param code
	 * @param message
	 * @param error
	 * @return
	 */
	public CPRUtil result(int code,String message,String error){
		Result result	= new Result();
		result.code 	= code;
		result.message 	= message;
		result.error 	= error;
		cpResponse.result = result;
		return this;
	}
	
	/**
	 * rediect 변수 값 직접 지정 
	 * @param redirect
	 * @return
	 */
	public CPRUtil redirect(String redirect){
		cpResponse.redirect = redirect;
		return this;
	}
	
	/**
	 * search 형 데이터 set
	 * @param rset
	 * @return
	 */
	public CPRUtil data(RecordSet rset){

		if(cpResponse.result == null){
			if(rset.size() == 0){
				resultNOT_FOUND(CPUtil.RESULT_NOT_FOUND_MSG);
			}else{
				resultOK();
			}
		}
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST) || cpResponse.type.equals(CPUtil.CP_TYPE_SEARCH)){
			cpResponse.data = rset.getRows();
			setSummary();
		}
		
		return this;
	}
	
	/**
	 * list 형 테이터 지정 
	 * @param dao
	 * @param rset
	 * @return
	 */
	public CPRUtil dataList(RecordSet rset,DAO dao){
		// 1.EXCEL or PDF 파일의 헤더칼럼값 셋팅 
		if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL) || cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			// 2.카테고리명(reason)이 없으면 doc값 설정 안돼서 여기서 title값  AutoExport로 셋팅
			if(doc == null){
										//Title, Description, Author
				doc = new CPDocument("AutoExport", "Export DATA", "SYSTEM");
			}
			// 3.thead를 ":" 기준으로 값을 나누어 저장하기 위해 Map 생성
			LinkedHashMap <String,String> thead = new LinkedHashMap <String,String>();
			
			if(cpResponse.thead != null) {
				// [mchtId:가맹점ID],[name:가맹점] ...
				String[] dhead = CommonUtil.split(cpResponse.thead, ",", true);
				for(String head : dhead){
					if(head.indexOf(":") > -1){
						// {name:가맹점} ":" 기준으로 나눠 배열형식 data에 저장 
						String[] data = CommonUtil.split(head, ":", true);
						// <name(key):name(value)>
						thead.put(data[0], data[1]);
					}else{
						// <mchtId:가맹점ID>(key) : <mchtId:가맹점ID>(value)
						thead.put(head, head);
					}
				}
			} 
			
			// 4.Excel, pdf 문서 정보 값 셋팅 
			export(doc, thead);
		}
		
		//5.list의 경우 페이징 값 셋팅, pdf와 exel의 경우 파일 만들기 
		//cprespons 값 세팅
		setDefaultData(rset,dao);
		
		//6.응답코드 , 메세지, error셋팅
		if(cpResponse.result == null){
			if(rset.size() == 0){
				resultNOT_FOUND(CPUtil.RESULT_NOT_FOUND_MSG); // 601 ,데이터가 없습니다.
			}else{
				resultOK(); // 200 , OK 
			}
		}
		// 7.data셋팅 
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST) || cpResponse.type.equals(CPUtil.CP_TYPE_SEARCH)){
			cpResponse.data = rset.getRows(); 
		}
		return this;
	}
	
	/**
	 * 파일 링크시 파일 정보 설정 
	 * @param link
	 * @param auth
	 * @return
	 */
	private void setFile(String link,String auth){
		Files file 		= new Files();
		file.link 		= link;
		file.auth 		= auth;
		cpResponse.file = file;
	}
	
	//Excel, pdf 값 세팅
	public CPRUtil export(CPDocument doc,LinkedHashMap<String,String> thead){
		this.doc = doc;
		this.thead = thead;
		return this;
	}
	
	/**
	 * 응답 CPResponse 반환
	 * @return
	 */
	public CPResponse cpResponse(){
		if(CPUtil.CP_DEBUG){
			logger.debug("json response : {}",cpResponseJson());
		}
		return cpResponse;
	}
	
	public ModelAndView setView(HttpServletRequest webRequest,String view,String cpResponseName ){
		
		if(CPUtil.CP_DEBUG){
			// view 단으로 보여줄 데이터 josn화 하여 콘솔창 출력
			logger.debug("json response : {}",cpResponseJson());
		}
		
		if(webRequest == null){
			logger.debug("HttpServletRequest is null please set HttpServletRequest");
			
		}else{
			/*	
			 *  cpResponseName 매게변수로 값이 들어와 있지 않을 때, "CPR"로 이름 정해줌
			 *  값이 들어와 있을 때는 매게변수 값으로 이름 정함
			 *  이 값은 jsp 에서 값을 사용할 때 이용 됨
			 */
			if(CommonUtil.isNullOrSpace(cpResponseName)){
				// view단 넘겨줄 value들 셋팅
				webRequest.setAttribute("CPR", cpResponse);
			}else{
				webRequest.setAttribute(cpResponseName, cpResponse);
			}
		}
		
		/* 	KJM
		 *  exel 파일이나 pdf 파일 요청이라면 해당 파일로 값 보내준다
		 *  그 외의 경우 매게변수의 view 값의 경로로 리스트 뿌려준다
		 */
		if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL) || cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			
			cpResponse.page = null;
			cpResponse.thead = null;
									// 	viewName , modelName , modalObject	
			return new ModelAndView("/common/jsonResponse","message",GsonUtil.toJson(cpResponse));
			
		}else{
			
			return new ModelAndView(view);
		}
	}
	
	public String cpResponseJson(){
		
		boolean pretty = true;
		
		if(cpResponse.data != null &&  cpResponse.data.size() > 5){
			pretty = false;
		}
		
		return GsonUtil.toJson(cpResponse,pretty,"");
	}
	
	public String cpResponseXml(){
		return XmlUtil.toXml(cpResponse,true,"utf-8");
	}
	

	//KJM : list의 경우 페이징, pdf와 exel의 경우 링크 생성 후 파일 세팅 해줌
	private void setDefaultData(RecordSet rset,DAO dao){
		//리스트 일경우만 PAGING 을 제공한다.
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST)){
			
			if(cpResponse.page == null){	
				//page, current 세팅
				cpResponse.page = CPUtil.correctPage(new Page());
			}
			cpResponse.page.total 	= dao.getTotal();
			cpResponse.page.hash 	= dao.getHash();
			// 총 페이지 = 리스트 갯수 / 한 페이지당 리스트 갯수 
			cpResponse.page.totalPage = cpResponse.page.total/cpResponse.page.size;
			// 리스트 갯수 % 한 페이지당 리스트 갯수 != 0 
			if(cpResponse.page.total % cpResponse.page.size != 0){
				cpResponse.page.totalPage++;
			}
			
		//PDF파일 요청 일 때
		}else if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			if(cpResponse.file == null && rset.size() !=0){
				
				// sheet css 설정
				Template t = new Template();
				// 파일 이름 설정
				if(doc != null){
					t.setDocument(doc);
				}
				String link = "";
				
				try{
					if(CommonUtil.isNullOrSpace(cpResponse.thead)){
						link = "";
					}else{
						// PDF생성 후 파일명과 경로 리턴 
						link = t.export(thead, rset,CPUtil.CP_TYPE_FILE_PDF );
					}
				}catch(Exception e){
					link = e.getMessage();
				}
				setFile(link,"authkey");
			}
		}else if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL)){
			//파일에 넣을 데이터가 없을 때
			//매입현황조회의 경우 rset에 조회된 리스트 존재
			if(cpResponse.file == null && rset.size() !=0){
				//파일 폴더 확인 및 생성
				XlsExport export = new XlsExport(doc);
				// 파일 경로 및 생성 이름 셋팅
				String link = "";
				
				try{
					//컬럼명 없을 경우
					if(CommonUtil.isNullOrSpace(cpResponse.thead)){
						link = "";
					}else{
						// 엑셀 생성 후 파일명과 경로 리턴
						link = export.makeExcel(thead, rset, true, true);
					}
				}catch(Exception e){
					link = e.getMessage();
				}
				// 파일이름과 작성자명 셋팅 후 response에 셋팅
				setFile(link, "authkey");
			}
		}else{
			
		} 
	}
	
	//cpResponse의 sum세팅
	private void setSummary(){
		
		if(cpResponse.data == null){return;}
		
		//넘어온 컬럼들이 존재할 때
		if(cpResponse.data.size() > 0){
			
			cpResponse.sum = new SharedMap<String,Object>();
			for(int i= 0 ; i< cpResponse.data.size() ; i++){
				SharedMap<String,Object> dataMap = cpResponse.data.get(i);
				for(String key : dataMap.keySet()){
					if(i == 0){	//initialize
						cpResponse.sum.put(key,0);
					}
						cpResponse.sum.put(key, cpResponse.sum.getDouble(key)+dataMap.getDouble(key));
				}
			}
			
		}
	}	
	
	

	/**
	 * list 형 테이터 지정 
	 * @param dao
	 * @param rset
	 * @return
	 */
	public CPRUtil dataList2(RecordSet rset){
		//EXCEL , PDF
		if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL) || cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			if(doc == null){
				doc = new CPDocument("AutoExport", "Export DATA", "SYSTEM");
			}
			
			LinkedHashMap <String,String> thead = new LinkedHashMap <String,String>();
			if(cpResponse.thead != null) {
				String[] dhead = CommonUtil.split(cpResponse.thead, ",", true);
				for(String head : dhead){
					if(head.indexOf(":") > -1){
						String[] data = CommonUtil.split(head, ":", true);
						thead.put(data[0], data[1]);
					}else{
						thead.put(head, head);
					}
				}
			}
			export(doc, thead);
		}
		
		setDefaultData2(rset);
		
		//결과 메세지가 없을 경우 값 설정
		if(rset.size() == 0){
			resultNOT_FOUND(CPUtil.RESULT_NOT_FOUND_MSG);
		}else {
			resultERROR(CPUtil.RESULT_ERROR_MSG);
		}
		
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST) || cpResponse.type.equals(CPUtil.CP_TYPE_SEARCH)){
			cpResponse.data = rset.getRows();
			setSummary();
		}
		
		return this;
	}
	
	private void setDefaultData2(RecordSet rset){
		//리스트 일경우만 PAGING 을 제공한다.
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST)){
			if(cpResponse.page == null){	
				cpResponse.page = CPUtil.correctPage(new Page());
			}			
			
			cpResponse.page.total 	= rset.size();
			cpResponse.page.totalPage = cpResponse.page.total/cpResponse.page.size;
			if(cpResponse.page.total%cpResponse.page.size != 0){
				cpResponse.page.totalPage++;
			}
		}else if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			if(cpResponse.file == null && rset.size() !=0){
				Template t = new Template();
				if(doc != null){
					t.setDocument(doc);
				}
				String link = "";
				try{
					if(CommonUtil.isNullOrSpace(cpResponse.thead)){
						link = "";
					}else{
						link = t.export(thead, rset,CPUtil.CP_TYPE_FILE_PDF );
					}
				}catch(Exception e){
					link = e.getMessage();
				}
				setFile(link,"authkey");
			}
		}else if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL)){
			if(cpResponse.file == null && rset.size() !=0){
				XlsExport export = new XlsExport(doc);
				String link = "";
				try{
					if(CommonUtil.isNullOrSpace(cpResponse.thead)){
						link = "";
					}else{
						link = export.makeExcel(thead, rset, true, true);
					}
				}catch(Exception e){
					link = e.getMessage();
				}
				
				setFile(link, "authkey");
			}
		}else{
			
		} 
	}
	
}
