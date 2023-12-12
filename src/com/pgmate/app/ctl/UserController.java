package com.pgmate.app.ctl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgmate.app.dao.*;
import com.pgmate.app.util.*;
import com.pgmate.lib.sms.SmsUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.UserController.class );
	
	@RequestMapping(value = {"/member/user/form"})
    public ModelAndView userForm() {
        return new ModelAndView("/member/user/form");
    }
	
	@RequestMapping(value = {"/member/user/add/admin"})
    public ModelAndView adminAdd(HttpServletRequest request) {
		request.setAttribute("isAdmin", "true");
		return new ModelAndView("/member/user/add");
    }
	
	@RequestMapping(value = {"/member/user/add"})
    public ModelAndView userAdd(HttpServletRequest request) {
		return new ModelAndView("/member/user/add");
    }
	
	@RequestMapping(value = "/member/user/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		UserDAO userDAO = new UserDAO();
		
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		SessionUtil.setSearchGrade(request, cpRequest);
		RecordSet rset = userDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,userDAO).setView(request,"/member/user/list","");
	}
	
	@RequestMapping(value = "/member/user/idList", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView idList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		UserDAO userDAO = new UserDAO();
		
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = userDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,userDAO).setView(request,"/member/user/idList","");
	}
	
	@RequestMapping(value = "/member/user/view/{userid}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String userid) throws Exception {

		SharedMap<String, Object> userDao = new UserDAO().getById(userid).getRow(0);
		request.setAttribute("DATAMAP", userDao);
		request.setAttribute("DATAACCESSMAP", new UserAccessDAO().getById(userid,10).getRows());
		
		//전자계약서 목록 가져오기
		request.setAttribute("DATAMAPFORM", new EformDAO().getEformById(userid).getRows());
		
		String token = EformUtil.eform_token();
		JSONObject resJson = EformUtil.eform_list();

		request.setAttribute("resJson", resJson);
		request.setAttribute("token", token);
		//전자계약서 목록 가져오기

		//소속표시
		String parentName = "";
		String parentId = userDao.getString("parentId");
		if(userDao.getString("grade").equals("대행사")) {
			SharedMap<String, Object> distDao = new DistDAO().getById(parentId).getRowFirst();
			parentName = " [ " + distDao.getString("name") + " ] ";
		} else if(userDao.getString("grade").equals("에이전시")) {
			SharedMap<String, Object> agencyDao = new AgencyDAO().getById(parentId).getRowFirst();
			String distId = agencyDao.getString("distId");
			SharedMap<String, Object> distDao = new DistDAO().getById(distId).getRowFirst();
			parentName = " [ " + distDao.getString("name") + " > " + agencyDao.getString("name") + " ] ";
		} else if(userDao.getString("grade").equals("지사")) {
			SharedMap<String, Object> salesDao = new MemberSalesDAO().getById(parentId).getRowFirst();
			String agencyId = salesDao.getString("agencyId");
			SharedMap<String, Object> agencyDao = new AgencyDAO().getById(agencyId).getRowFirst();
			String distId = agencyDao.getString("distId");
			SharedMap<String, Object> distDao = new DistDAO().getById(distId).getRowFirst();
			parentName = " [ "+distDao.getString("name") + " > " + agencyDao.getString("name") + " > " + salesDao.getString("name") + " ] ";
		}
		request.setAttribute("parentName", parentName);
		
        return new ModelAndView("/member/user/view");
    }
	
    @RequestMapping(value = "/member/user/modify/{userid}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String userid) {
        return new ModelAndView("/member/user/modify","DATAMAP",new UserDAO().getById(userid).getRow(0));
    }
	
	@RequestMapping(value = {"/member/user/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		//패스워드는 TRIGGER에 의하여 기본 12345로 생성된다.
		/**
		//패스워드 기본 12345
		if(CommonUtil.isNullOrSpace(cpRequest.getValue("pw"))){
			cpRequest.deleteData("pw");
			cpRequest.setData("pw", "*00A51F3F48415C7D4E8908980D443C29C69B60C9");
		}else{
			String pw = new CPDAO().getPassword(cpRequest.getValue("pw"));
			cpRequest.deleteData("pw");
			cpRequest.setData("pw",pw);
		}**/
		
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insert("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }

	//대행사, 에이전시 소속변경
	@RequestMapping(value = {"/member/user/change/{userid}"}, method = RequestMethod.GET)
	public ModelAndView change(HttpServletRequest request, @PathVariable String userid) {
		SharedMap<String, Object> userDao = new UserDAO().getById(userid).getRowFirst();
		request.setAttribute("DATAMAP", userDao);

		//소속표시
		String parentName = "";
		String parentId = userDao.getString("parentId");
		if(userDao.getString("grade").equals("대행사")) {
			SharedMap<String, Object> distDao = new DistDAO().getById(parentId).getRowFirst();
			parentName = distDao.getString("name");
		} else if(userDao.getString("grade").equals("에이전시")) {
			SharedMap<String, Object> agencyDao = new AgencyDAO().getById(parentId).getRowFirst();
			String distId = agencyDao.getString("distId");
			SharedMap<String, Object> distDao = new DistDAO().getById(distId).getRowFirst();
			parentName = distDao.getString("name") + " > " + agencyDao.getString("name");
		} else if(userDao.getString("grade").equals("지사")) {
			SharedMap<String, Object> salesDao = new MemberSalesDAO().getById(parentId).getRowFirst();
			String agencyId = salesDao.getString("agencyId");
			SharedMap<String, Object> agencyDao = new AgencyDAO().getById(agencyId).getRowFirst();
			String distId = agencyDao.getString("distId");
			SharedMap<String, Object> distDao = new DistDAO().getById(distId).getRowFirst();
			parentName = distDao.getString("name") + " > " + agencyDao.getString("name") + " > " + salesDao.getString("name");
		}
		request.setAttribute("parentName", parentName);

		return new ModelAndView("/member/user/change");
	}

	@RequestMapping(value = {"/member/user/change/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse changeUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if(cpDAO.updateAndBack("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest)
					.resultOK("사용자 소속이 변경되었습니다.")
					.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
					.resultNOK("사용자 소속 변경에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}
	}

	
	@RequestMapping(value = {"/member/user/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
				
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.updateAndBack("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest)
	        		.resultOK("사용자 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("사용자 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
    @RequestMapping(value = "/member/user/ht/list/{userId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String userId) {
    	
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(userId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/member/user/ht/list","");
	}
    
    @RequestMapping(value = {"/member/user/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String idCheck(HttpServletRequest request, @RequestParam("id") String id) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_USER");
		dao.setColumns("id");
		dao.addWhere("id", id, DAO.eq);
		
		if(id.indexOf(" ") > -1){
			return GsonUtil.toJson("사용자 아이디에는 공백이 포함 될 수 없습니다.");
		}
		if(dao.search().size() == 0){
			
			// 터미널 아이디와 중복 여부 체크
			dao.initRecord();
			dao.setTable("PG_MCHT_TMN");
			dao.setColumns("tmnId");
			dao.addWhere("tmnId", id, DAO.eq);
			if(dao.search().size() == 0) {
				return GsonUtil.toJson("true");
			}else {
				return GsonUtil.toJson("사용이 불가능한  아이디입니다.");
			}
		}else{
			return GsonUtil.toJson("사용자 아이디가 이미 있습니다.");
		}
    }
    
    @RequestMapping(value = {"/member/user/childrenList"}, method = RequestMethod.POST)
    public @ResponseBody List<SharedMap<String,Object>> childrenList(HttpServletRequest request, @RequestParam("grade") String grade, @RequestParam("parentId") String parentId) {
    	List<SharedMap<String,Object>> result = new ArrayList<SharedMap<String,Object>>();
    	UserDAO userDAO = new UserDAO();
    	if(grade.equals("대행사")) {
    		result = userDAO.getChildForAgency(parentId,"");
    	} else if(grade.equals("에이전시")) {
    		result = userDAO.getChildForSales(parentId,"");
    	}
    	return result;
    }
    
    // =============================================================== 내 정보 보기
    @RequestMapping(value = "/member/user/myprofile/{userid}", method = RequestMethod.GET)
    public ModelAndView mypage(HttpServletRequest request, @PathVariable String userid) {
			userid = SessionUtil.getUserId(request);
			SharedMap<String, Object> userMap = new UserDAO().getById(userid).getRow(0);
			request.setAttribute("DATAMAP", userMap);
			request.setAttribute("DATAACCESSMAP", new UserAccessDAO().getById(userid,10).getRows());
			if(userMap.getString("grade").equals("가맹점")) {
				request.setAttribute("DATAMCHTMAP", new MchtDAO().getById(userMap.getString("parentId")).getRow(0));
			}
			
      return new ModelAndView("/member/user/myprofile");
    }
    
    // ================================================================ 가맹점 사용자 추가
    @RequestMapping(value = {"/member/user/add/mcht"})
    public ModelAndView selectMcht(HttpServletRequest request) {
		return new ModelAndView("/member/user/selectMcht/form");
    }
	
	@RequestMapping(value = "/member/user/add/mcht/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		cpRequest.replaceKeyValue("identity",mchtDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",mchtDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		RecordSet rset = mchtDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/member/user/selectMcht/list","");
	}
	
	@RequestMapping(value = {"/member/user/add/mcht/{mchtId}"})
    public ModelAndView mchtAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("isMcht", "true");
		request.setAttribute("DATAMAP", new MchtDAO().getById(mchtId).getRowFirst());
		return new ModelAndView("/member/user/add");
    }
	
	// ================================================================ 선정산 업체 사용자 추가
    @RequestMapping(value = {"/member/user/add/loan"})
    public ModelAndView selectLoan(HttpServletRequest request) {
		return new ModelAndView("/member/user/selectLoan/form");
    }
	
	@RequestMapping(value = "/member/user/add/loan/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loanList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		LoanDAO mchtDAO = new LoanDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		cpRequest.replaceKeyValue("identity",mchtDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",mchtDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		RecordSet rset = mchtDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/member/user/selectLoan/list","");
	}
	
	@RequestMapping(value = {"/member/user/add/loan/{loanId}"})
    public ModelAndView loanAdd(HttpServletRequest request, @PathVariable String loanId) {
		request.setAttribute("isLoan", "true");
		request.setAttribute("DATAMAP", new LoanDAO().getById(loanId).getRowFirst());
		return new ModelAndView("/member/user/add");
    }
    
    // ================= 비밀번호
    @RequestMapping(value = "/member/user/password/{userid}", method = RequestMethod.GET)
    public ModelAndView password(HttpServletRequest request, @PathVariable String userid) {
		SharedMap<String,Object> sharedMap = new UserDAO().getById(userid).getRow(0);
		
        return new ModelAndView("/member/user/password","DATAMAP",sharedMap);
    }
    
	@RequestMapping(value = {"/member/user/pwCheck/{userid}"}, method = RequestMethod.POST)
    public @ResponseBody String pwCheck(HttpServletRequest request, @PathVariable String userid, @RequestParam("pw") String pw) {
		String sessionId = SessionUtil.getUserId(request);
		if(!sessionId.equals(userid)) {
			return "잘못된 요청입니다.";
		}

		UserDAO userDAO = new UserDAO();
		SharedMap<String, Object> result = userDAO.getById(userid).getRowFirst();
		
		if(pw.indexOf(" ") > -1){
			return GsonUtil.toJson("비밀번호에는 공백이 포함 될 수 없습니다.");
		}
		
		String hashed =  new CPDAO().getPassword(pw);
		if(result.getString("pw").equalsIgnoreCase(hashed)){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("비밀번호가 맞지 않습니다.");
		}
    }

	@RequestMapping(value = {"/member/user/sendSms/{userid}"}, method = RequestMethod.POST)
	public @ResponseBody String sendSms(HttpServletRequest request, @PathVariable String userid) {
		String sessionId = SessionUtil.getUserId(request);
		if(!sessionId.equals(userid)) {
			return "잘못된 요청입니다.";
		}

		UserDAO userDAO = new UserDAO();
		SharedMap<String, Object> result = userDAO.getById(userid).getRowFirst();

		String oldPassWord = CommonUtil.nToB(request.getParameter("check"));

		String pwCheck =  new CPDAO().getPassword(oldPassWord);
		if(result.getString("pw").equalsIgnoreCase(pwCheck)){
			return "기존 비밀번호가 틀립니다";
		}

		String passKey = CommonUtil.nToB(request.getParameter("pw"));

		CPRequest cpRequest = new CPRequest();
		cpRequest.setData("id", userid, "eq", "", true);

		String hashed =  new CPDAO().getPassword(passKey);
		cpRequest.setData("pw", hashed);

		CPDAO cpDAO = new CPDAO();

		//과거에 사용했던 비밀번호 확인
		cpDAO.setTable("HT_USER_PW");
		cpDAO.setColumns("pw");
		cpDAO.addWhere("pw", hashed, DAO.eq);
		cpDAO.addWhere("regId", userid, DAO.eq);
		cpDAO.setOrderBy("");

		if(cpDAO.search().size() > 0)
			return "예전에 사용한 비밀번호 입니다.";

		WebCache wc = new WebCache();
		String number = String.format("%1$" + 6 + "s", ((int) (Math.random() * 999999) + 1)).replace(' ', '0');
		wc.setSMSKey(userid, number);

		String msgBody = "[CREDITOP] 본인인증번호는 [" + number + "] 입니다. 정확히 입력해주세요.";
		SmsUtil smsUtil = new SmsUtil();
		smsUtil.sendSms(smsUtil.SMS_URL, result.getString("phone").replaceAll("\\[^0-9]+", ""), msgBody);

		return "OK";
	}

	@RequestMapping(value = {"/member/user/smsCheck/{userid}"}, method = RequestMethod.POST)
	public @ResponseBody String smsCheck(HttpServletRequest request, @PathVariable String userid) {
		String sessionId = SessionUtil.getUserId(request);
		if(!sessionId.equals(userid)) {
			return "잘못된 요청입니다.";
		}

		UserDAO userDAO = new UserDAO();
		SharedMap<String, Object> result = userDAO.getById(userid).getRowFirst();

		String oldPassWord = CommonUtil.nToB(request.getParameter("check"));

		String pwCheck =  new CPDAO().getPassword(oldPassWord);
		if(result.getString("pw").equalsIgnoreCase(pwCheck)){
			return "기존 비밀번호가 틀립니다";
		}

		String smsNumber = CommonUtil.nToB(request.getParameter("smsNumber"));

		WebCache wc = new WebCache();
		String saveNumber = wc.getSMSKey(userid);

		if(saveNumber.equals(smsNumber)) {
			String passKey = CommonUtil.nToB(request.getParameter("pw"));

			CPDAO cpDAO = new CPDAO();

			CPRequest cpRequest = new CPRequest();
			cpRequest.setData("id", userid, "eq", "", true);

			String hashed =  new CPDAO().getPassword(passKey);
			cpRequest.setData("pw", hashed);

			if(cpDAO.update("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){

				//히스토리 테이블에 변경 전 비밀번호 저장
				cpDAO.insert("HT_USER_PW", cpRequest.data);

				cpDAO = new CPDAO();
				//유저 비밀번호 정보 수정할 데이터 세팅
				CPRequest cpRequestPW = new CPRequest();
				cpRequestPW.setData("id", userid, "eq", "", true);
				cpRequestPW.setData("pwStatus", "사용");
				cpRequestPW.setData("pwYn", "예");	//비밀번호 변경 여부
				cpRequestPW.setData("pwRetry", 0);	//재시도 횟수
				cpRequestPW.setData("pwDate", CommonUtil.getCurrentTimestamp());	//최근 갱신일

				//유저 비밀번호 정보 정상 수정 시
				if(cpDAO.update("PG_USER_PW", cpRequestPW.data)){
					//KJM : 현재 로그인 중인 계정의 세션정보의 비밀번호 변경 여부도 "예" 바꿔주기
					SessionUtil.setPwYes(request);

					//"OK" 반환
					return "OK";
				}
			}

			return "OK";
		} else {
			return "인증번호가 틀립니다";
		}
	}
  
    @RequestMapping(value = "/member/user/updatePassword/{userid}", method = RequestMethod.POST)
    public @ResponseBody String updatePassword(HttpServletRequest request, @PathVariable String userid) {

		String sessionId = SessionUtil.getUserId(request);
		if(!sessionId.equals(userid)) {
			return "잘못된 요청입니다.";
		}

		UserDAO userDAO = new UserDAO();
		SharedMap<String, Object> result = userDAO.getById(userid).getRowFirst();

		String oldPassWord = CommonUtil.nToB(request.getParameter("check"));

		String pwCheck =  new CPDAO().getPassword(oldPassWord);
		if(result.getString("pw").equalsIgnoreCase(pwCheck)){
			return "기존 비밀번호가 틀립니다";
		}

    	String passKey = CommonUtil.nToB(request.getParameter("pw"));
    	
    	CPRequest cpRequest = new CPRequest();
    	cpRequest.setData("id", userid, "eq", "", true);
    	
    	String hashed =  new CPDAO().getPassword(passKey);
		cpRequest.setData("pw", hashed);
		
    	CPDAO cpDAO = new CPDAO();

    	//과거에 사용했던 비밀번호 확인
    	cpDAO.setTable("HT_USER_PW");
    	cpDAO.setColumns("pw");
    	cpDAO.addWhere("pw", hashed, DAO.eq);
    	cpDAO.addWhere("regId", userid, DAO.eq);
    	cpDAO.setOrderBy("");
    	
    	//과거에 사용했던 비밀번호 아닐 시 변경 진행
    	if(cpDAO.search().size() <= 0) {
	    	//유저 정보 update한 후(id에 대한 pw)
	    	if(cpDAO.update("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
	    		
	    		//히스토리 테이블에 변경 전 비밀번호 저장
    			cpDAO.insert("HT_USER_PW", cpRequest.data);
    			
	    		cpDAO = new CPDAO();
	    		//유저 비밀번호 정보 수정할 데이터 세팅
	    		CPRequest cpRequestPW = new CPRequest();
	    		cpRequestPW.setData("id", userid, "eq", "", true);
	    		cpRequestPW.setData("pwStatus", "사용");
	    		cpRequestPW.setData("pwYn", "예");	//비밀번호 변경 여부
	    		cpRequestPW.setData("pwRetry", 0);	//재시도 횟수
	    		cpRequestPW.setData("pwDate", CommonUtil.getCurrentTimestamp());	//최근 갱신일
	    		
	    		//유저 비밀번호 정보 정상 수정 시 
	    		if(cpDAO.update("PG_USER_PW", cpRequestPW.data)){
	    			//KJM : 현재 로그인 중인 계정의 세션정보의 비밀번호 변경 여부도 "예" 바꿔주기
	    			SessionUtil.setPwYes(request);
	    			
	    			//"OK" 반환
	    			return "OK";
	    		}
	    	}
    	}
    	
    	//수정 실패 시 "NOK" 반환
    	//22.06.28 NOK 반환 시 충돌 문제로 인해 NOK 삭제
//    	return "NOK:비밀번호 변경이 실패하였습니다.";
    	return "비밀번호 변경이 실패하였습니다.";
    }
    
    @RequestMapping(value = "/member/user/resetPassword/{userid}", method = RequestMethod.GET)
    public @ResponseBody Map<String , Object> resetPassword(HttpServletRequest request, @PathVariable String userid) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		String sessionId = SessionUtil.getUserId(request);
		if(!sessionId.equals(userid)) {
			resMap.put("result", "NOK");
			resMap.put("msg", "잘못된 접근입니다.");
			return resMap;
		}

    	String passKey = String.format("%05d", new Random().nextInt(99999));
    	CPRequest cpRequest = new CPRequest();
    	cpRequest.setData("id", userid, "eq", "", true);
    	
    	String hashed =  new CPDAO().getPassword(passKey);
		cpRequest.setData("pw", hashed);
		
    	CPDAO cpDAO = new CPDAO();
    	if(cpDAO.update("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
    		cpDAO = new CPDAO();
    		CPRequest cpRequestPW = new CPRequest();
    		cpRequestPW.setData("id", userid, "eq", "", true);
    		cpRequestPW.setData("pwStatus", "사용");
    		cpRequestPW.setData("pwYn", "아니오");
    		cpRequestPW.setData("pwRetry", 0);
    		cpRequestPW.setData("pwDate", CommonUtil.getCurrentTimestamp());
    		
    		if(cpDAO.update("PG_USER_PW", cpRequestPW.data)){
    			SessionUtil.setPwYes(request);
    			resMap.put("result", "OK");
    			resMap.put("msg", "비밀번호가 변경되었습니다. <br> 임시 발급 비밀번호 : [ " + passKey + " ] ");
    			return resMap;
    		}
    	}
    	resMap.put("result", "NOK");
    	resMap.put("msg", "비밀번호 변경이 실패하였습니다.");
    	return resMap;
    }
}
