package com.pgmate.app.ctl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.dao.UserIpDAO;
import com.pgmate.app.interceptor.SessionExclude;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.InfoBankSMS;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.app.util.WebCache;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.lib.sms.SmsUtil;

/**
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	
	//210809_PYS : 로그 출력 선언
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.LoginController.class );
	
	//210809_PYS : /login/form 요청이 들어왔을때 sso/form model을 생성후 리턴
	@RequestMapping(value = {"/login/form"})
	@SessionExclude
	public ModelAndView loginPage() {
			return new ModelAndView("/sso/websso");
	}
	
	@RequestMapping(value = "/login/in", method = RequestMethod.POST)
	@SessionExclude
	public @ResponseBody String in(HttpServletRequest request,@RequestParam(value="memberId") String memberId,
					@RequestParam(value="memberPw") String memberPw, @RequestParam(value="smsKey", required=false) String smsKey){
	
		//210809_PYS : DAO생성
		// KBR :DAO 생성되면서 user
		logger.info("----- login/in START -----");
		UserDAO userDAO = new UserDAO();
		
		//210809_PYS : IP를 가져온다.
		String ip = request.getHeader("X-FORWARDED-FOR");
		//KJM : port 번호 가져옴
		int port = request.getServerPort();
		
		//210809_PYS : X-FORWARED-FOR에 없으면 X-Real-IP에서 가져온다.
		if (ip == null || ip.isEmpty()) {
			ip = CommonUtil.nToB(request.getHeader("X-Real-IP"));
		}
		
		//210809_PYS : X-Real-IP에도 없으면 request객체에서 클라이언트IP주소를 가져온다.
		if (ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}
		
		
		//210809_PYS : VW_USER_PW에서 해당 패스워드가 있는지 조회한다.
		//210809_PYS : 패스워드가 있으면 String으로 리턴, 없으면 ""리턴
		//입력받은 패스워드 암호화 값 
		// KBR : 하단에서 조건을 걸기위해 미리 변수 생성하여 값 넣어놓기
		String inputPw = userDAO.getPassword(memberPw);
		//대소문자 구분안함.
		
		//210809_PYS : VW_USER_PW에서 해당 ID가 있는지 조회한다.
		//210809_PYS : SELECT * FROM VW_USER_PW WHERE id = 'memberId'
		RecordSet rset = userDAO.getById(memberId);
		
		//210809_PYS : VW_USER_PW에서 해당 ID가 있는지 조회한다.
		SharedMap<String, Object> memberMap = rset.getRow(0);
		
		//KJM : 로그인 시도 아이디 조회 후 포트별, 소속별 로그인 차단
		//KJM : 부포트 접속 시 접속 ip 확인
//		if(port == 10001 && !SessionUtil.ipCheck(ip)) {
//			return "NOK||관리자 페이지 페이지 접속 불가.";
//		}
		
		//존재하지 않는 아이디
		if (rset.size() < 1) {
			//210809_PYS : 터미널아이디는 TMN000000로 되어있는 ID
			//210809_PYS : VW_MCNT_TMN에서 해당 ID가 있는지 조회한다.
			//210809_PYS : SELECT * FROM VW_MCHT_TMN WHERE tmnid = 'memberId'
			logger.info("----- login/in notFind ID -----");
			// 로그인 아이디가 아닌 터미널로 접속했는지 확인
			RecordSet rset2 = new MchtTmnDAO().getById(memberId);
			
			if(rset2.size() > 0) {
				
				//210809_PYS : 아이디로 검색해서 값이 하나밖에 안나옴
				SharedMap<String,Object> tmnMap = rset2.getRow(0);
				
				//210809_PYS : VW_MCHT_TMN에서 mchtId를 가져온다.
				//210809_PYS : SELECT * VW_MCHT WHERE mchtId = 'mchtId'
				SharedMap<String,Object> tmnMchtMap = new MchtDAO().getById(tmnMap.getString("mchtId")).getRow(0);
				
				
				
				//210809_PYS : 대표가맹점이 'Y' && 비밀번호가 시리얼번호(TMN다음에 오는 숫자)와 같을때 && 터미널 상태가 '사용'일때
				if(tmnMchtMap.getString("aggregator").equalsIgnoreCase("Y") && tmnMap.isEquals("serial", memberPw) && tmnMap.isEquals("status", "사용")){
//					if(!smsKey.isEmpty()) {
//						/* 현재 IP를 인증된 IP로 등록 */
//						WebCache wc = new WebCache();
//						String cSMSKey = wc.getSMSKey(memberId);
//						if(smsKey.equals(cSMSKey)) {
//							new UserIpDAO().insertIp(memberId, ip, request.getHeader("User-Agent"));
//						} else {
//							return "INVALIDKEY||인증번호가 올바르지 않습니다.||TERMINAL";
//						}
//					} else {
//						UserIpDAO userIpDAO = new UserIpDAO();
//						/* 인증된 아이피인지 확인 */
//						rset = userIpDAO.getByActiveIp(memberId);
//						if (rset.size() < 1) {
//							return "UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||TERMINAL";
//						}
//						boolean authorized = false;
//						List<SharedMap<String, Object>> ipList = rset.getRows();
//						for (SharedMap<String, Object> eachMap : ipList) {
//							if (eachMap.getString("ipAddr").equals(ip)) {
//								authorized = true;
//								logger.debug("EXPIREDAY UPDATE : {}", userIpDAO.updateExpireDay(memberId, ip));
//								break;
//							}
//						}
//						
//						if (!authorized) {
//							return "UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||TERMINAL";
//						}
//					}

					CPSession cpSession = new CPSession();
					SessionUtil.initTmnSessionData(cpSession, tmnMap);
					cpSession.setTargetURL("/");
					
					//Session 생성
					SessionUtil.create(request, cpSession);
					// KBR : Session에 종료일 담는 코드 
					SessionUtil.setAttribute(request,"endDate",CommonUtil.getCurrentDate("yyyyMMdd"));
					
					
					return "OK||"+cpSession.getTargetURL();
					
				} else {
					return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
				}
			} else {
				return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
			}
		}
		// KBR : 해당 로그인 아이디의 정보 모두 가져오기  (key : value 형식)
		
		//210809_PYS : 로그인 ID 'status'가 "사용"이고 비밀번호가 틀렸을때
		// KBR : 비밀번호 틀린 횟수 카운트하기위한 조건문
		logger.info("----- login/in find ID -----"); 
		SharedMap<String, Object> memberMap = rset.getRow(0);
		
		if(!memberMap.isEquals("pw", inputPw) && memberMap.isEquals("status", "사용")){
			int retry = memberMap.getInt("pwRetry")+1;
			if(retry > 4){
				//5회 이상 만료 처리 
				userDAO.updateUserPwRetry(memberMap.getString("id"), retry, "만료");
			}else{
				//5회 미만에 대해서는 횟수만 + 
				userDAO.updateUserPwRetry(memberMap.getString("id"), retry);
			}
			
			return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
		}
		
		
		
		if(memberMap.isEquals("pwStatus", "만료")){
			//210809_PYS : 이렇게 바꾸는게 더 좋아보이는데..
			return "NOK||비밀번호가 만료되었습니다. 관리자에게 문의해주세요.";
			//return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
		}
		
		if(!memberMap.isEquals("status", "사용")){
		}
		
		//210809_PYS : parentId 해당계정소속ID (default : 0)
		//210809_PYS : 본사(0), 대행사(distId), 에이전시(agencyId), 가맹점(mchtId)
		if(memberMap.isNullOrSpace("parentId")){
			return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
		}
		
		// KBR : 권한 포트 확인하여 나누기
//		if(!SessionUtil.CheckPort(memberMap.getString("grade"), request.getServerPort())) {
//			return "NOK||접속 권한이 없습니다.";
//		}
		logger.info("----- login/in pw Check End -----"); 
		
		if(!smsKey.isEmpty()) {
			/* 현재 IP를 인증된 IP로 등록 */
			WebCache wc = new WebCache();
			String cSMSKey = wc.getSMSKey(memberId);
			logger.info("----- loing/in IP Check -----"); 
			if(smsKey.equals(cSMSKey)) {
				new UserIpDAO().insertIp(memberId, ip, request.getHeader("User-Agent"));
			} else {
				return "INVALIDKEY||인증번호가 올바르지 않습니다.||MEMBER";
			}
		} else {
			UserIpDAO userIpDAO = new UserIpDAO();
			/* 인증된 아이피인지 확인 */
			rset = userIpDAO.getByActiveIp(memberId);
			if (rset.size() < 1) {
				logger.info("----- loing/in IP ADD -----"); 
				return "UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||MEMBER";
			}
			boolean authorized = false;
			List<SharedMap<String, Object>> ipList = rset.getRows();
			for (SharedMap<String, Object> eachMap : ipList) {
				if (eachMap.getString("ipAddr").equals(ip)) {
					authorized = true;
					logger.debug("EXPIREDAY UPDATE : {}", userIpDAO.updateExpireDay(memberId, ip));
					break;
				}
			}
			
			if (!authorized) {
				return "UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||MEMBER";
			}
		}
		
		// KBR : 본사의 경우 접속 ip 제한하기
//		if(ip.length() >= 10) {
//			String ipTemp = ""; 
//			ipTemp = ip.substring(0,10);
//			if(memberMap.get("grade").equals("본사") && !(ipTemp.equals("192.168.51") || ipTemp.equals("192.168.52"))) {
//					return "NOK||접속 권한이 없습니다.";
//			}
//		}

		/*
		 * 로그인시 SMS 인증 절차
		 * 아래 sendSMS를 수정 후 주석 제거하여 인증 절차 수행
		 */
		// if(!smsKey.isEmpty()) {
		// 	/* 현재 IP를 인증된 IP로 등록 */
		// 	WebCache wc = new WebCache();
		// 	String cSMSKey = wc.getSMSKey(memberId);
		// 	if(smsKey.equals(cSMSKey)) {
		// 		new UserIpDAO().insertIp(memberId, ip, request.getHeader("User-Agent"));
		// 	} else {
		// 		return "INVALIDKEY||인증번호가 올바르지 않습니다.||MEMBER";
		// 	}
		// } else {
		// 	return "UNAUTHORIZED||SMS 인증이 필요합니다.||MEMBER";
			
		// 	UserIpDAO userIpDAO = new UserIpDAO();
		// 	/* 인증된 아이피인지 확인 */
		// 	rset = userIpDAO.getByActiveIp(memberId);
		// 	if (rset.size() < 1) {
		// 		return "UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||MEMBER";
		// 	}
		// 	boolean authorized = false;
		// 	List<SharedMap<String, Object>> ipList = rset.getRows();
		// 	for (SharedMap<String, Object> eachMap : ipList) {
		// 		if (eachMap.getString("ipAddr").equals(ip)) {
		// 			authorized = true;
		// 			logger.debug("EXPIREDAY UPDATE : {}", userIpDAO.updateExpireDay(memberId, ip));
		// 			break;
		// 		}
		// 	}
			
		// 	if (!authorized) {
		// 		return "UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||MEMBER";
		// 	}
		// }
		
		//접속 패스워드에 대한 재시도횟수 초기화
		//210809_PYS : 접속성공 했으니 retry를 0으로 초기화
		userDAO.updateUserPwRetry(memberMap.getString("id"), 0);
		
		//210809_PYS : 'grade'가 "본사" 일때 && 'role'이 "관리자"일때 디버그 가능
		if(memberMap.getString("grade").equals("본사") && memberMap.getString("role").equals("관리자")) {
			SessionUtil.setAttribute(request, "CP_DEBUG",CPUtil.CP_DEBUG);
		}
		logger.debug("grade [{}]",memberMap.getString("grade"));
		logger.debug("role [{}]",memberMap.getString("role"));
		
		
		//USERSESSION
		CPSession cpSession = new CPSession();
		
		// KBR : 접속시간 셋팅
		cpSession.setAccessDate(CommonUtil.getCurrentTimestamp());
		// KBR : 이건 뭐지 ?
		cpSession.setLastAccessDate(userDAO.getAccessById(memberMap.getString("id")));
		
		// KBR : 세션값 셋팅 !! 
		SessionUtil.initSessionData(cpSession, memberMap);
		
		cpSession.setTargetURL("/");
		
		// 모든 유저 대행사 아이디 세팅
		SharedMap<String, Object> userInfo = userDAO.getParentsById(memberId).getRowFirst();
		if(!userInfo.isEmpty()) {
			//210809_PYS : VW_USER에 있는 모든 대행사ID세팅
			cpSession.setDistId(userInfo.getString("distId"));
		}
		
		//ACCESS 기록 추가 
		logger.info("----- loing/in ID Access -----"); 
		userDAO.insertUserAcess(memberMap.getString("id"),ip,request.getHeader("User-Agent"));
		
		//Session  생성
		SessionUtil.create(request, cpSession, memberMap);
		SessionUtil.setAttribute(request,"endDate",CommonUtil.getCurrentDate("yyyyMMdd"));
		return "OK||"+cpSession.getTargetURL();
		
	}
	
	@RequestMapping(value = "/login/out", method = RequestMethod.GET)
	@SessionExclude
	public String out(HttpServletRequest request) {
		SessionUtil.destroy(request);
		request.setAttribute("message", "접속 세션이 종료되었습니다. 다시 로그인하여 주시기 바랍니다.");
		// request.setAttribute("redirectURL", request.getScheme()+"://"+request.getServerName());
		return "/common/redirectParent";
	}
	
	/*
	 * sms 인증문자 발송
	 * sms 발송 업체와 계약하여 해당업체 API 와 연동필요함
	 */
	@RequestMapping(value = "/login/send/{userId}/{memberType}", method = RequestMethod.PUT)
	@SessionExclude
	public @ResponseBody String sendSMS(HttpServletRequest request, @PathVariable String userId, @PathVariable String memberType) throws IOException {
		
		WebCache wc = new WebCache();
		String number = String.format("%1$" + 6 + "s", ((int) (Math.random() * 999999) + 1)).replace(' ', '0');
		wc.setSMSKey(userId, number);
		
		if(memberType.equals("MEMBER")) {
			SharedMap<String, Object> userMap = new UserDAO().getById(userId).getRow(0);
			//InfoBankSMS infoBankSMS = new InfoBankSMS();
			String msgBody = "[CREDITOP] 본인인증번호는 [" + number + "] 입니다. 정확히 입력해주세요.";
			//infoBankSMS.sendSms(InfoBankSMS.SMS_URL, userMap.getString("phone").replaceAll("\\[^0-9]+", ""), msgBody);
			SmsUtil smsUtil = new SmsUtil();
			smsUtil.sendSms(smsUtil.SMS_URL, userMap.getString("phone").replaceAll("\\[^0-9]+", ""), msgBody);
		}else {
			SharedMap<String, Object> userMap = new MchtTmnDAO().getById(userId).getRow(0);

			//InfoBankSMS infoBankSMS = new InfoBankSMS();
			String msgBody = "[CREDITOP] 본인인증번호는 [" + number + "] 입니다. 정확히 입력해주세요.";
			//infoBankSMS.sendSms(InfoBankSMS.SMS_URL, userMap.getString("ceoPhone").replaceAll("\\[^0-9]+", ""), msgBody);
			SmsUtil smsUtil = new SmsUtil();
			smsUtil.sendSms(smsUtil.SMS_URL, userMap.getString("phone").replaceAll("\\[^0-9]+", ""), msgBody);
		}
		
		return "OK";
	}
	
	// KBR : 접속 시간 만료 시 작동 
	@RequestMapping(value = {"/login/expired"},produces=MediaType.APPLICATION_JSON_VALUE)
	@SessionExclude
    public String expired(HttpServletRequest request) {
		SessionUtil.destroy(request);
		request.setAttribute("message", "접속 세션이 종료되었습니다. 다시 로그인하여 주시기 바랍니다.");
		
		// request.setAttribute("redirectURL", "root");
		return "/common/redirectParent";
    }
	
	// KBR : 가맹점 수수료 템플릿 > 신규 템플릿 생성 클릭 시 
	@RequestMapping(value = {"/sessionAlive"}, method = RequestMethod.GET)
	@SessionExclude
    public @ResponseBody String sessionAlive(HttpServletRequest request) {
		if(SessionUtil.isLive(request)){
			return "MSG||ALIVE";
		}else{
			return GsonUtil.toJson("MSG||사용자 세션이 종료되었습니다.");
		}
	}
	
	@RequestMapping(value = {"/ajaxSession"}, method = RequestMethod.GET)
	@SessionExclude
    public ResponseEntity<String> ajaxSession(HttpServletRequest request) {
		if(SessionUtil.isLive(request)){
			return new ResponseEntity<String>("OK",HttpStatus.OK);
		}else{
			return new ResponseEntity<String>("접속자 세션이 종료되었습니다.",HttpStatus.UNAUTHORIZED);
		}
		
	}
}

	