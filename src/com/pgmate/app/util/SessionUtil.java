package com.pgmate.app.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pgmate.app.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.session.CPSession;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class SessionUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.util.SessionUtil.class );
	
	public static boolean isLive(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return false;
		}else{
			return true;
		}
	}
	
	public static void create(HttpServletRequest request,CPSession cpSession){
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT);
		setAttribute(request,CPUtil.CP_SESSION,cpSession);
	}
	
	public static void create(HttpServletRequest request, CPSession cpSession, SharedMap<String, Object> memberMap) {
		HttpSession session = request.getSession(true);
		if(memberMap.getString("grade").equals("본사") && !memberMap.getString("role").equals("일반")) {
			session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT_MARU);
		}else {
			session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT);
		}
		setAttribute(request,CPUtil.CP_SESSION,cpSession);
	}
	
	public static void setAttribute(HttpServletRequest request,String name,Object object){
		request.getSession().setAttribute(name, object);   
	}
	
	
	public static void destroy(HttpServletRequest request){
		request.getSession().removeAttribute(CPUtil.CP_SESSION);
		request.getSession().invalidate();
	}
	
	
	public static CPSession get(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return null;
		}else{
			return (CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION);
		}
	}
	
	public static String getUserId(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return "";
		}else{
			return ((CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION)).getUserId();
		}
	}
	
	public static String getParentId(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return "";
		}else{
			return ((CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION)).getParentId();
		}
	}
	
	public static void setPwYes(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
		}else{
			CPSession cpSession =  (CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION);
			cpSession.setPwYn("예");
			setAttribute(request,CPUtil.CP_SESSION,cpSession);
		}
	}
	
	
	
	public static List<SharedMap<String,Object>> getChildList(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return new ArrayList<SharedMap<String,Object>>();
		}else{
			return ((CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION)).getChildList();
		}
	}
	
	public static void setSearchGrade(HttpServletRequest request,CPRequest cpRequest){
		CPSession cpSession = get(request);
		//본사,대행사,에이전시,지사
		Data data = new Data();
		data.key = true;
		data.oper = "eq";
		data.val = cpSession.getParentId();
		if(cpSession.getGrade().equals("대행사")){
			data.name = "distId";
		} else if(cpSession.getGrade().equals("에이전시")){
			data.name = "agencyId";
		} else if(cpSession.getGrade().equals("지사")){
			data.name = "salesId";
		} else if(cpSession.getGrade().equals("가맹점")){
			data.name = "mchtId";
		} else if(cpSession.getGrade().equals("하위가맹점")){
			data.name = "tmnId";
		} else {
			
		}
		
		if(!data.name.equals("")){
			cpRequest.data.add(data);
		}
	}
	

	
	
	public static String toString(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		sb.append("--- SESSION ---\n");
		sb.append("ID          = "+request.getSession().getId());
		sb.append("CreateTime  = "+request.getSession().getCreationTime());
		sb.append("LastAccess  = "+request.getSession().getLastAccessedTime());
		sb.append("MaxInterval = "+request.getSession().getMaxInactiveInterval());
		Enumeration<String> attr = request.getSession().getAttributeNames();
		int i=0;
		for(String name : Collections.list(attr)){
			sb.append("attr["+i+"] ="+name);
		}
		return sb.toString();
	}
	
	public static void initSessionData(HttpServletRequest request) {
		SharedMap<String,Object> memberMap = new UserDAO().getById(getUserId(request)).getRow(0);
		initSessionData(get(request), memberMap);
	}
	
	public static void initSessionData(CPSession cpSession, SharedMap<String,Object> memberMap) {
		cpSession.setRole(memberMap.getString("role"));								//VIEW권한 일반,마스터,관리자
		cpSession.setName(memberMap.getString("name"));								//이름
		cpSession.setUserId(memberMap.getString("id"));							//ID
		cpSession.setGrade(memberMap.getString("grade"));							//현재소속(본사,대행사,에이전시,지사)
		cpSession.setPwYn(memberMap.getString("pwYn"));								//패스워드 업데이트 여부 
		cpSession.setParentId(memberMap.getString("parentId"));						//실 소속 아이디
		cpSession.setShowOthTrns(memberMap.getString("showOthTrns"));			// 기타 거래 메뉴 보기 여부
		cpSession.setEformStatus(memberMap.getString("eformStatus"));			// 전자계약서 권한 확인 여부
		cpSession.setLoanSettleStatus(memberMap.getString("loanSettleStatus"));			// 대출정산 보기 여부
				
		UserDAO userDAO = new UserDAO();
		if(cpSession.getGrade().equals("본사")){
			cpSession.setChildList(userDAO.getChildForDist(""));
			cpSession.setParentName("본사");
		}else if(cpSession.getGrade().equals("대행사")){
			cpSession.setChildList(userDAO.getChildForAgency(cpSession.getParentId(),""));
			cpSession.setParentName(userDAO.getDistName(cpSession.getParentId()));
		}else if(cpSession.getGrade().equals("에이전시")){
			cpSession.setChildList(userDAO.getChildForSales(cpSession.getParentId(),""));
			cpSession.setParentName(userDAO.getAgencyName(cpSession.getParentId()));
		}else if(cpSession.getGrade().equals("가맹점")){
			cpSession.setChildList(new ArrayList<SharedMap<String,Object>>());
			cpSession.setParentName(memberMap.getString("name"));
			cpSession.setAggregator(new DAO().query("SELECT aggregator FROM PG_MCHT WHERE mchtId ='"+cpSession.getParentId()+"'").getRow(0).getString("aggregator"));
			cpSession.setWebPay(new MchtTmnDAO().getWebPay(cpSession.getParentId()));
			cpSession.setMchtWebPay(new MchtSvcDAO().getMchtWebPay(cpSession.getParentId()));
			logger.debug("aggregator: {}", cpSession.getAggregator());
		}
		
		//거래기준 정보
		cpSession.setSalesMonthList(new TrxCapDAO().salesMonthList());
		
		//최근 공지사항 정보
		List<SharedMap<String, Object>> noticeList = new NoticeDAO().getByNew().getRows();
		if(noticeList.size() > 0){
			cpSession.setNewNoticeList(noticeList);
		}
		
		//van list
		cpSession.setVanList(new OrgFeeDAO().vanList());
				
	}
	
	public static void initTmnSessionData(CPSession cpSession, SharedMap<String,Object> tmnMap) {
		cpSession.setRole("일반");								//VIEW권한 일반,마스터,관리자
		cpSession.setName(tmnMap.getString("dtlName"));			//이름
		cpSession.setUserId(tmnMap.getString("tmnId"));			//ID
		cpSession.setGrade("하위가맹점");						//현재소속(본사,대행사,에이전시,지사)
		cpSession.setPwYn("Y");									//패스워드 업데이트 여부 
		cpSession.setParentId(tmnMap.getString("tmnId"));							//실 소속 아이디
		cpSession.setParentName(tmnMap.getString("dtlName"));
		cpSession.setWebPay(new MchtTmnDAO().getTmnWebPay(tmnMap.getString("tmnId")));
	}

	
}
