<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import = "java.sql.*" %>
<%@page import="com.pgmate.lib.util.map.SharedMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.pgmate.lib.dao.DAO"%>
<%@page import="com.pgmate.lib.dao.RecordSet"%>
<%@page import="com.pgmate.lib.util.lang.CommonUtil"%>

<html><body><% 

	//수신 내용 인코딩
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html; charset=UTF-8");
	
	//데이터값 key, value 형식으로 받기위해 map 변수 선언
	HashMap<String, Object> paramList = new HashMap<String, Object>();
	
	//데이터의 name 값 가져옴
	Enumeration params = request.getParameterNames();
	
	//hasMoreElements : 다음 내용이 있는지 확인
	while(params.hasMoreElements()) {
		//nextElemnet : 값을 가져옴
		String key = (String) params.nextElement();
		String value = request.getParameter(key);
		paramList.put(key, value);
	}
	
	//db 추가 성공 여부 확인 용 변수
	boolean flag = false;
	
	//노티 데이터 값 정상적으로 들어왔을 때 db 로직 수행
	//거래번호값이 있을 경우 로직 수행
	if(paramList.get("transaction_id") != null && !"".equals(paramList.get("transaction_id"))) {
		try {
			//rs : 조회 쿼리 수행 후 값을 받을 변수
			RecordSet rs;
			
			//insert 수행할 dao
			DAO inDao = new DAO();
			inDao.setTable("pg_trx_load_galaxia");
			
			//vanId 조회 쿼리 세팅 및 수행
			DAO dao1 = new DAO();
			dao1.setTable("vw_mcht_tmn");
			dao1.setColumns("vanId");
			dao1.addWhere("tmnId", paramList.get("store_id").toString());
			
			rs = dao1.search();
			
			//조회된 vanid값이 있을 경우 insert 쿼리문에 추가
			if(rs.size() > 0) {
				SharedMap<String, Object> map = rs.getRow(0);
				inDao.setRecord("vanId", map.getString("vanId"));
			}
				
			inDao.setRecord("vanTrxId", paramList.get("transaction_id").toString());
			inDao.setRecord("bin", paramList.get("masking_pin_number").toString().substring(0,6));
			inDao.setRecord("last4", paramList.get("masking_pin_number").toString().substring(6));
			inDao.setRecord("amount", paramList.get("auth_amount").toString());
			inDao.setRecord("installment", paramList.get("quota").toString());
			inDao.setRecord("authCd", paramList.get("auth_number").toString());
			inDao.setRecord("trxDay", paramList.get("order_date").toString().substring(0, 8));
			inDao.setRecord("trxTime", paramList.get("order_date").toString().substring(8));
			inDao.setRecord("tmnId", paramList.get("store_id").toString());
			inDao.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
			inDao.setRecord("response_code", paramList.get("response_code").toString());
			inDao.setRecord("response_message", paramList.get("response_message").toString());
			inDao.setRecord("service_id", paramList.get("service_id").toString());
			inDao.setRecord("service_code", paramList.get("service_code").toString());
			inDao.setRecord("order_date", paramList.get("order_date").toString());
			inDao.setRecord("issue_company_name", paramList.get("issue_company_name").toString());
			inDao.setRecord("issue_company_code", paramList.get("issue_company_code").toString());
			
			//trnType(승인구분) 승인거래 시
			if(paramList.get("cancel_date") == null || "".equals(paramList.get("cancel_date"))) {
				inDao.setRecord("trnType", "승인");
				
			//승인거래가 아닐 때
			} else {
				//취소거래(망취소 노티에만 있는 cancel_type으로 취소, 망취소 구분)
				if(paramList.get("cancel_type") == null || "".equals(paramList.get("cancel_type"))) {
					inDao.setRecord("trnType", "승인취소");
				//망취소 거래
				} else {
					inDao.setRecord("trnType", "망취소");
					inDao.setRecord("cancel_type", paramList.get("cancel_type").toString());
				}
				
				//원거래 일자의 경우 취소, 망취소 경우에 모두 들어가야 함
				//원거래 승인 일자 조회 쿼리 세팅 및 수행
				DAO dao2 = new DAO();
				dao2.setTable("pg_trx_load_galaxia");
				dao2.setColumns("trxDay,trxTime");
				dao2.addWhere("vanTrxId", paramList.get("transaction_id").toString());
				
				rs = dao2.search();
				
				//조회된 값이 있다면 insert 쿼리문에 추가
				if(rs.size() > 0) {
					List<SharedMap<String, Object>> rootList = rs.getRows();
					for(SharedMap<String, Object> map : rootList){
						inDao.setRecord("rootTrxDay", map.get("trxDay"));
						inDao.setRecord("rootTrxTime", map.get("trxTime"));
					}
				}
			}
			
			//쿼리문 정상 수행됐을때 flag값 true 설정
			if(inDao.insert()) {
				flag = true;
			}
			
		//db 추가 실패 시 falg 값 설정, 로그 출력
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
	}
	
	//화면상에 보여줄 메시지 변수 선언
	String message = "";
	
	//db에 정상적으로 insert 되었을 경우 메시지 보여줌
	if(flag) {
		message = "RC:111";
	}
%>
<%=message%>
</body></html>