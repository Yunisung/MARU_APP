package com.pgmate.app.ctl;

import java.io.IOException;
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

/**
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.LoginController.class );
	
	@RequestMapping(value = {"/login/form"})
	@SessionExclude
	public ModelAndView loginPage() {
			return new ModelAndView("/sso/websso");
	}
	
	@RequestMapping(value = "/login/in", method = RequestMethod.POST)
	@SessionExclude
	public @ResponseBody String in(HttpServletRequest request,@RequestParam(value="memberId") String memberId,
					@RequestParam(value="memberPw") String memberPw, @RequestParam(value="smsKey", required=false) String smsKey){
		UserDAO userDAO = new UserDAO();
		
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.isEmpty()) ip = CommonUtil.nToB(request.getHeader("X-Real-IP"));
		if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
		
		
		//입력받은 패스워드 암호화 값
		String inputPw = userDAO.getPassword(memberPw);
		//대소문자 구분안함.
		
		RecordSet rset = userDAO.getById(memberId);
		//존재하지 않는 아이디
		if (rset.size() < 1) {
			// 로그인 아이디가 아닌 터미널로 접속했는지 확인
			RecordSet rset2 = new MchtTmnDAO().getById(memberId);
			if(rset2.size() > 0) {
				SharedMap<String,Object> tmnMap = rset2.getRow(0);
				SharedMap<String,Object> tmnMchtMap = new MchtDAO().getById(tmnMap.getString("mchtId")).getRow(0);
				
				
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
					//Session  생성
					SessionUtil.create(request, cpSession);
					SessionUtil.setAttribute(request,"endDate",CommonUtil.getCurrentDate("yyyyMMdd"));
					return "OK||"+cpSession.getTargetURL();
				} else {
					return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
				}
			} else {
				return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
			}
		}
		
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
			return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
		}
		
		if(!memberMap.isEquals("status", "사용")){
			return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
		}
		
		if(memberMap.isNullOrSpace("parentId")){
			return "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.";
		}
		
		if(!smsKey.isEmpty()) {
			/* 현재 IP를 인증된 IP로 등록 */
			WebCache wc = new WebCache();
			String cSMSKey = wc.getSMSKey(memberId);
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
		
		
		//접속 패스워드에 대한 재시도횟수 초기화
		userDAO.updateUserPwRetry(memberMap.getString("id"), 0);
		if(memberMap.getString("grade").equals("본사") && memberMap.getString("role").equals("관리자")) {
			SessionUtil.setAttribute(request, "CP_DEBUG",CPUtil.CP_DEBUG);
		}
		logger.debug("grade [{}]",memberMap.getString("grade"));
		logger.debug("role [{}]",memberMap.getString("role"));
		
		//USERSESSION
		CPSession cpSession = new CPSession();
		cpSession.setAccessDate(CommonUtil.getCurrentTimestamp());
		
		cpSession.setLastAccessDate(userDAO.getAccessById(memberMap.getString("id")));
		
		SessionUtil.initSessionData(cpSession, memberMap);
		
		cpSession.setTargetURL("/");
		
		// 모든 유저 대행사 아이디 세팅
		SharedMap<String, Object> userInfo = userDAO.getParentsById(memberId).getRowFirst();
		if(!userInfo.isEmpty()) {
			cpSession.setDistId(userInfo.getString("distId"));
		}
		
		//ACCESS 기록 추가 
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
	
	@RequestMapping(value = "/login/send/{userId}/{memberType}", method = RequestMethod.PUT)
	@SessionExclude
	public @ResponseBody String sendSMS(HttpServletRequest request, @PathVariable String userId, @PathVariable String memberType) throws IOException {
		WebCache wc = new WebCache();
		String number = String.format("%1$" + 6 + "s", ((int) (Math.random() * 999999) + 1)).replace(' ', '0');
		wc.setSMSKey(userId, number);
		
		if(memberType.equals("MEMBER")) {
			SharedMap<String, Object> userMap = new UserDAO().getById(userId).getRow(0);
			logger.debug("SEND SMS!");
	
			InfoBankSMS infoBankSMS = new InfoBankSMS();
			String msgBody = "[CREDITOP] 본인인증번호는 [" + number + "] 입니다. 정확히 입력해주세요.";
			infoBankSMS.sendSms(infoBankSMS.SMS_URL, userMap.getString("phone").replaceAll("\\[^0-9]+", ""), msgBody);
		}else {
			SharedMap<String, Object> userMap = new MchtTmnDAO().getById(userId).getRow(0);
			logger.debug("SEND SMS!");
	
			InfoBankSMS infoBankSMS = new InfoBankSMS();
			String msgBody = "[CREDITOP] 본인인증번호는 [" + number + "] 입니다. 정확히 입력해주세요.";
			infoBankSMS.sendSms(infoBankSMS.SMS_URL, userMap.getString("ceoPhone").replaceAll("\\[^0-9]+", ""), msgBody);
		}
		
		return "OK";
	}
	
	@RequestMapping(value = {"/login/expired"},produces=MediaType.APPLICATION_JSON_VALUE)
	@SessionExclude
    public String expired(HttpServletRequest request) {
		SessionUtil.destroy(request);
		request.setAttribute("message", "접속 세션이 종료되었습니다. 다시 로그인하여 주시기 바랍니다.");
		
		// request.setAttribute("redirectURL", "root");
		return "/common/redirectParent";
    }
	
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

	