package com.pgmate.app.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.NoticeDAO;
import com.pgmate.app.dao.OrgFeeDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.UserDAO;
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
	// KBR : 포트 담을 Map 셋팅
	private static SharedMap<String, Integer> portMap = new SharedMap<String, Integer>();
//	private static SharedMap<String, HttpServletRequest> loginPortMap = new SharedMap<String, HttpServletRequest>();
	
	public static SharedMap<String, Integer> getPortMap() {
		return portMap;
	}
	
//	public static boolean addLoginRequest(String ip, HttpServletRequest request) {
//		
//		if(loginPortMap.containsKey(ip) == true) {
//			return false;
//		} else {
//			loginPortMap.put(ip, request);
//			return true;
//		}
//	}
//	
//	public static boolean checkLoginPort(String ip, HttpServletRequest request) {
//		if(loginPortMap.containsKey(ip) == true && loginPortMap.get(ip).getServerPort() == request.getServerPort()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	
//	public static HttpServletRequest getLoginRequest(String id) {
//		if(loginPortMap.containsKey(id))
//			return loginPortMap.get(id);
//		else
//			return null;
//	}
	
	// KBR : 포트 리스트 셋팅
	public static void setPortList(){
		portMap.clear();
		portMap.put("본사", 10001);
		portMap.put("대행사", 10022);
		portMap.put("에이전시", 10022);
		portMap.put("지사", 10022);
		portMap.put("가맹점", 10022);
	}
	

	public static boolean isLive(HttpServletRequest request){
		// KBR : 포트 리스트 map 초기화
		setPortList();
		
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return false;
		}else{
			return true;
		}
	}
	
	public static void create(HttpServletRequest request,CPSession cpSession){
		
		HttpSession session = request.getSession(true);
		
		session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT); // 본사 제외 30분 
		
		setAttribute(request,CPUtil.CP_SESSION,cpSession);
		
	}
	
	public static void create(HttpServletRequest request, CPSession cpSession, SharedMap<String, Object> memberMap) {
		HttpSession session = request.getSession(true);
		
		if(memberMap.getString("grade").equals("본사") && !memberMap.getString("role").equals("일반")) {
			session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT_KWON);
		}else {
			session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT);
		}
		setAttribute(request,CPUtil.CP_SESSION,cpSession);
	}
	
	// 
	public static void setAttribute(HttpServletRequest request,String name,Object object){
		// KBR : 세션정보 셋팅
		request.getSession().setAttribute(name, object);
	}
	
	
	public static void destroy(HttpServletRequest request){

		request.getSession().removeAttribute(CPUtil.CP_SESSION);
		request.getSession().invalidate();
	}
	
//	public static void destroyById(String id, HttpServletRequest request) {
//		request.getSession().removeAttribute(id);
//		request.getSession().invalidate();
//	}
	
	// KBR : 세션값 있을 경우 세션값 리턴 
	public static CPSession get(HttpServletRequest request){
		
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return null;
		}else{
			return (CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION);
		}
	}
	
	//KJM : 로그인 세션 확인 후 빈값  OR 아이디값 반환
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
	
	//KJM : 로그인중의 계정 세션 정보의 비밀번호 변경여부 "예"로 변경
	public static void setPwYes(HttpServletRequest request){
		//KJM : 로그인 세션 확인
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
		}else{
			//KJM : 현제 세션 정보 가져오기
			CPSession cpSession =  (CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION);
			//KJM : 비밀번호 변경여부 변경
			cpSession.setPwYn("예");
			//KJM : 세션정보 세팅
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
	
	//KJM : 로그인 계정의 현재 소속 구분
	public static void setSearchGrade(HttpServletRequest request,CPRequest cpRequest){
		
		// KBR : 로그인 시 세션에 저장했던 현재 로그인한 계정의 정보를 가져온다 
		CPSession cpSession = get(request);
		// 본사,대행사,에이전시,지사
		// KBR : 현재 맵핑된 값 말고 새로운 공간 생성 후 할당 
		Data data = new Data();
		data.key = true;
		data.oper = "eq";
		// KBR : 세션에 저장 된 부모 아이디 들고오기 
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
	// KBR : 변경된 값으로 세션 다시 초기화 
	public static void initSessionData(HttpServletRequest request) {
		SharedMap<String,Object> memberMap = new UserDAO().getById(getUserId(request)).getRow(0);
		initSessionData(get(request), memberMap);
	}

	// KBR : 로그인 시 세션값 셋팅  
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
		// KBR : 등급에 따른 하위 업체 셋팅 ( 본사 기준 대리점 리스트 셋팅)
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
			logger.debug("aggregator: {}", cpSession.getAggregator());
		}
		
		// KBR :거래기준 정보
		cpSession.setSalesMonthList(new TrxCapDAO().salesMonthList());
		
		// KBR :최근 공지사항 정보
		List<SharedMap<String, Object>> noticeList = new NoticeDAO().getByNew().getRows();
		if(noticeList.size() > 0){
			cpSession.setNewNoticeList(noticeList);
		}
		
		// KBR :van list
		cpSession.setVanList(new OrgFeeDAO().vanList());
				
	}
	
	/**
	 * 210809_PYS : 터미널 ID로 로그인 했을때만 사용
	 * @param cpSession
	 * @param tmnMap : VW_MCHT_TMN
	 */
	public static void initTmnSessionData(CPSession cpSession, SharedMap<String,Object> tmnMap) {
		cpSession.setRole("일반");								//VIEW권한 일반,마스터,관리자
		cpSession.setName(tmnMap.getString("dtlName"));			//이름
		cpSession.setUserId(tmnMap.getString("tmnId"));			//ID
		cpSession.setGrade("하위가맹점");						//현재소속(본사,대행사,에이전시,지사)
		cpSession.setPwYn("Y");									//패스워드 업데이트 여부 
		cpSession.setParentId(tmnMap.getString("tmnId"));							//실 소속 아이디
		cpSession.setParentName(tmnMap.getString("dtlName"));
		//210809_PYS : VW_MCHT_TMN에서 paykey값을 가져와서 세팅
		cpSession.setWebPay(new MchtTmnDAO().getTmnWebPay(tmnMap.getString("tmnId")));
	}
	
	// KBR : port 체크 
	public static boolean CheckPort(String key ,int port ) {
		
		if (getPortMap().get(key) == port) {
			return true;
		}else {
			return false;
		}
		
	}

	
}
