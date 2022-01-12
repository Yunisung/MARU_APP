package com.pgmate.app.ctl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.EformDAO;
import com.pgmate.app.dao.HTDAO;
import com.pgmate.app.dao.LoanDAO;
import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.dao.MchtMngDAO;
import com.pgmate.app.dao.UserAccessDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.EformUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.UserController.class );
	
	//KJM : 멤버관리 > 로그인 ID관리 > ID 조회
	@RequestMapping(value = {"/member/user/form"})
    public ModelAndView userForm() {
        return new ModelAndView("/member/user/form");
    }
	
	//KJM : 멤버관리 > 임직원 등록
	@RequestMapping(value = {"/member/user/add/admin"})
    public ModelAndView adminAdd(HttpServletRequest request) {
		//KJM : 대행사, 에이전시, 임직원 등록 시 같은 jsp 파일 사용하기 때문에 구분하기 위한 변수 넣어준다.
		request.setAttribute("isAdmin", "true");
		return new ModelAndView("/member/user/add");
    }
	
	//KJM : 멤버관리 > 로그인 ID관리 > 대행사/에이전시 등록
	@RequestMapping(value = {"/member/user/add"})
    public ModelAndView userAdd(HttpServletRequest request) {
		return new ModelAndView("/member/user/add");
    }
	
	//KJM : 로그인 ID 조회 리스트
	@RequestMapping(value = "/member/user/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		UserDAO userDAO = new UserDAO();
		//KJM : input 값의 내용이 상태값이 아닐 때
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			/*	KJM
			 *  cpRequest.data 정보 ↓밑의 데이터로 세팅 (상태값 != 폐기)
			 *  상태가 중지 아닌 것들 조회 하기 위해
			 */
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		//KJM : 로그인 중인 계정의 소속 구분 ( 각 업체마다 보여지는 것이 달라야 함)
		SessionUtil.setSearchGrade(request, cpRequest);
		
		//KJM : cpRequest.data = input 입력값
		//KJM : 페이징 처리와 DB 기능 수행 후 반환된 레코드들 받아서 rset에 넣음
		RecordSet rset = userDAO.list(cpRequest.data,cpRequest.page);
		//KJM : 결과 값 해당 경로에 뿌려줌
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
	
	//KJM : 사용자 등록 후 이동 시 OR 직접 선택 이동 시
	@RequestMapping(value = "/member/user/view/{userid}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String userid) throws Exception {
		request.setAttribute("DATAMAP", new UserDAO().getById(userid).getRow(0));
		request.setAttribute("DATAACCESSMAP", new UserAccessDAO().getById(userid,10).getRows());
		//KJM : 해당 사용자 정보 데이터
		
		//전자계약서 목록 가져오기
		request.setAttribute("DATAMAPFORM", new EformDAO().getEformById(userid).getRows());
		
		String token = EformUtil.eform_token();
		JSONObject resJson = EformUtil.eform_list();

		request.setAttribute("resJson", resJson);
		request.setAttribute("token", token);
		//전자계약서 목록 가져오기
		//KJM : 멤버관리 > 사용자 정보 > 접속 정보에서 데이터 안나오는 이유 => getById(userid,10)에서 조회하는 아이디(userid)와 로그인 중인 아이디가 같아야 데이터 쌓여서 나옴
		//KJM : 해당 아이디의 접속 정보 데이터
		
		//KJM : 이동하는 경로
        return new ModelAndView("/member/user/view");
    }
	
	//KJM : 사용자 정보 변경 페이지 이동
    @RequestMapping(value = "/member/user/modify/{userid}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String userid) {
        return new ModelAndView("/member/user/modify","DATAMAP",new UserDAO().getById(userid).getRow(0));
    	
    }
	
    //KJM : 멤버관리 > 로그인 ID관리 > 대행사/에이전시 등록 submit
    // KBR  : 멤버관리 > 가맹점 등록 > 가맹점 클릭 > submit
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
//		// 패스워드 기본 12345
//		if(CommonUtil.isNullOrSpace(cpRequest.getValue("pw"))){
//			cpRequest.deleteData("pw");
//			cpRequest.setData("pw", "*00A51F3F48415C7D4E8908980D443C29C69B60C9");
//		}else{
//			String pw = new CPDAO().getPassword(cpRequest.getValue("pw"));
//			cpRequest.deleteData("pw");
//			cpRequest.setData("pw",pw);
//		}
		
		CPDAO cpDAO = new CPDAO();
		/*	KJM
		 *	table, regId(= 로그인 중인 아이디), data
		 *	insert 쿼리문이 제대로 실행 되었으면 true 반환
		 */
		
		// KBR : PG_USER (번호 암호화)
		KSignUtil.getInstance().Encrypt(cpRequest, "phone");
		
		if(cpDAO.insert("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
			//KJM : 로그인 세션 초기화
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest)
					//KJM : "등록 성공하였습니다" 메시지 출력
					.resultOK(CPUtil.RESULT_DATA_INSERTED)
	        		.cpResponse();
		//KJM : 등록 실패 시
		}else{
			return new CPRUtil(cpRequest)
					//KJM : "등록 실패하였습니다" 메시지 출력
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	//KJM : 멤버관리 > 사용자 정보 수정
	@RequestMapping(value = {"/member/user/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
				
		
		CPDAO cpDAO = new CPDAO();
		
		// KBR : PG_USER 사용자 전화번호 암호화
		KSignUtil.getInstance().Encrypt(cpRequest, "phone");
		
		if(cpDAO.updateAndBack("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
			//KJM : 업데이트 성공 시
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest)
	        		.resultOK("사용자 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			//KJM : 업데이트 실패 시
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
    
    //KJM : 사용자 등록 시 아이디 체크 ajax
    @RequestMapping(value = {"/member/user/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String idCheck(HttpServletRequest request, @RequestParam("id") String id) {
		CPDAO dao = new CPDAO();
		//SELECT * FROM PG_USER WHERE id='id'
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
    
    //KJM : 조회 조건 > 소속 선택 시 하위 소속 업체 리스트 반환
    @RequestMapping(value = {"/member/user/childrenList"}, method = RequestMethod.POST)
    public @ResponseBody List<SharedMap<String,Object>> childrenList(HttpServletRequest request, @RequestParam("grade") String grade, @RequestParam("parentId") String parentId) {
    	List<SharedMap<String,Object>> result = new ArrayList<SharedMap<String,Object>>();
    	
    	UserDAO userDAO = new UserDAO();
    	//KJM : 선택한 대행사의 하위 에이전시 리스트 가져옴
    	if(grade.equals("대행사")) {
    		result = userDAO.getChildForAgency(parentId,"");
    	//KJM : 선택한 에이전시의 하위 지사 리스트 가져옴
    	} else if(grade.equals("에이전시")) {
    		result = userDAO.getChildForSales(parentId,"");
    	}
    	//KJM : 조회 한 리스트 결과 반환
    	return result;
    }
    
    // =============================================================== 내 정보 보기
    //KJM : 로그인중인 계정의 내 정보 보기
    @RequestMapping(value = "/member/user/myprofile/{userid}", method = RequestMethod.GET)
    public ModelAndView mypage(HttpServletRequest request, @PathVariable String userid) {
    		//KJM : 로그인 세션에서 아이디 가져옴
			userid = SessionUtil.getUserId(request);
			//KJM : 해당하는 아이디에 대한 계정 정보 조회
			SharedMap<String, Object> userMap = new UserDAO().getById(userid).getRow(0);
			request.setAttribute("DATAMAP", userMap);
			//KJM : 유저의 접속정보 10개까지만 조회
			request.setAttribute("DATAACCESSMAP", new UserAccessDAO().getById(userid,10).getRows());
			//KJM : 계정의 현재 소속이 가맹점 일 때
			if(userMap.getString("grade").equals("가맹점")) {
				request.setAttribute("DATAMCHTMAP", new MchtDAO().getById(userMap.getString("parentId")).getRow(0));
			}
		//KJM : 해당 경로로 이동
      return new ModelAndView("/member/user/myprofile");
    }
    
    // ================================================================ 가맹점 사용자 추가 
    //KJM : 멤버 관리 > 가맹점 등록 (가맹점 리스트 폼)
    @RequestMapping(value = {"/member/user/add/mcht"})
    public ModelAndView selectMcht(HttpServletRequest request) {
		return new ModelAndView("/member/user/selectMcht/form");
    }
    //KJM : 멤버 관리 > 가맹점 등록 (가맹점 리스트)	
	@RequestMapping(value = "/member/user/add/mcht/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		//KJM : identity = 주민번호, 사업자번호 / ceoIdentity = 대표자식별번호
		//KJM 0915 : 사업자(주민)번호로 리스트 조회 시 원래 코드인 getValue()로 수행하면 조건문이 안맞아 리스트 조회 안됨 -> 조건문 맞는 코드인 getKeyValue()로 바꿈 (대행사, 에이전시 컨트롤도 바꿨음)
		//KJM : getKeyValue는 조회, getValue는 등록/수정할 때 쓰는듯...
		cpRequest.replaceKeyValue("identity",mchtDAO.getAESEnc(cpRequest.getKeyValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",mchtDAO.getAESEnc(cpRequest.getKeyValue("ceoIdentity")));
		
		RecordSet rset = mchtDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/member/user/selectMcht/list","");
	}
	
	//KJM : 멤버 관리 > 가맹점 등록 > 하위사용자 등록
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
	//KJM : 내 정보 > 비밀번호 변경 창 열림
    @RequestMapping(value = "/member/user/password/{userid}", method = RequestMethod.GET)
    public ModelAndView password(HttpServletRequest request, @PathVariable String userid) {
    	//KJM : 해당 유저의 정보 조회 후 url에 같이 보내줌
		SharedMap<String,Object> sharedMap = new UserDAO().getById(userid).getRow(0);
		
        return new ModelAndView("/member/user/password","DATAMAP",sharedMap);
    }
    
    //KJM : 내 정보 > 비밀번호 변경 > 현재 비밀번호 확인
	@RequestMapping(value = {"/member/user/pwCheck/{userid}"}, method = RequestMethod.POST)
    public @ResponseBody String pwCheck(HttpServletRequest request, @PathVariable String userid, @RequestParam("pw") String pw) {
		UserDAO userDAO = new UserDAO();
		//KJM : 해당 아이디의 유저 정보 조회
		SharedMap<String, Object> result = userDAO.getById(userid).getRowFirst();
		
		//KJM : 입력한 비밀번호에 공백이 있을 경우
		if(pw.indexOf(" ") > -1){
			return GsonUtil.toJson("비밀번호에는 공백이 포함 될 수 없습니다.");
		}
		
		//KJM : 입력한 비밀번호의 암호화값 받음 / 암호화 실패 시 빈값 받음
		String hashed =  new CPDAO().getPassword(pw);
		//KJM : 기존 비밀번호 입력값이 있는값인지 대소문자 구분 없이 비교
		if(result.getString("pw").equalsIgnoreCase(hashed)){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("비밀번호가 맞지 않습니다.");
		}
    }
	
	//KJM : 내 정보 > 비밀번호 변경 submit
    @RequestMapping(value = "/member/user/updatePassword/{userid}", method = RequestMethod.POST)
    public @ResponseBody String updatePassword(HttpServletRequest request, @PathVariable String userid) {
    	//KJM : 입력한 비밀번호가 null이면 공백처리 / 아니면 그대로
    	String passKey = CommonUtil.nToB(request.getParameter("pw"));
    	//KJM : 입력받은 값들 cprequest 객체에 넣어줌
    	CPRequest cpRequest = new CPRequest();
    	cpRequest.setData("id", userid, "eq", "", true);
    	
    	//KJM : hashed = 암호화된 비밀번호 입력값
    	String hashed =  new CPDAO().getPassword(passKey);
		cpRequest.setData("pw", hashed);
		
    	CPDAO cpDAO = new CPDAO();
    	//KJM : 유저 정보 update한 후(id에 대한 pw)
    	if(cpDAO.update("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
    		cpDAO = new CPDAO();
    		//KJM : 유저 비밀번호 정보 수정할 데이터 세팅
    		CPRequest cpRequestPW = new CPRequest();
    		cpRequestPW.setData("id", userid, "eq", "", true);
    		cpRequestPW.setData("pwStatus", "사용");
    		cpRequestPW.setData("pwYn", "예");	//비밀번호 변경 여부
    		cpRequestPW.setData("pwRetry", 0);	//재시도 횟수
    		cpRequestPW.setData("pwDate", CommonUtil.getCurrentTimestamp());	//최근 갱신일
    		
    		//KJM : 유저 비밀번호 정보 정상 수정 시 
    		if(cpDAO.update("PG_USER_PW", cpRequestPW.data)){
    			//KJM : 현재 로그인 중인 계정의 세션정보의 비밀번호 변경 여부도 "예" 바꿔주기
    			SessionUtil.setPwYes(request);
    			//KJM : "OK" 반환
    			return "OK";
    		}
    	}
    	
    	//KJM : 수정 실패 시 "NOK" 반환
    	return "NOK:비밀번호 변경이 실패하였습니다.";
    }
    
    //KJM : 멤버관리 > 사용자 정보 > 임시 비밀번호 발급
    @RequestMapping(value = "/member/user/resetPassword/{userid}", method = RequestMethod.GET)
    public @ResponseBody Map<String , Object> resetPassword(HttpServletRequest request, @PathVariable String userid) {
    	Map<String, Object> resMap = new HashMap<String, Object>();
    	//KJM: 5자리인 랜덤 숫자 생성 (123->00123, 12345->12345)
    	String passKey = String.format("%05d", new Random().nextInt(99999));
    	CPRequest cpRequest = new CPRequest();
    	cpRequest.setData("id", userid, "eq", "", true);
    	
    	//KJM : 생성한 랜덤수를 암호화해서 넣어줌
    	String hashed =  new CPDAO().getPassword(passKey);
		cpRequest.setData("pw", hashed);
		
    	CPDAO cpDAO = new CPDAO();
    	//KJM : 유저 정보 수정
    	if(cpDAO.update("PG_USER", SessionUtil.getUserId(request), cpRequest.data)){
    		cpDAO = new CPDAO();
    		//KJM : 유저 비밀번호 정보 수정할 데이터 세팅
    		CPRequest cpRequestPW = new CPRequest();
    		cpRequestPW.setData("id", userid, "eq", "", true);
    		cpRequestPW.setData("pwStatus", "사용");
    		cpRequestPW.setData("pwYn", "아니오");
    		cpRequestPW.setData("pwRetry", 0);
    		cpRequestPW.setData("pwDate", CommonUtil.getCurrentTimestamp());
    		
    		//KJM : 유저 비밀번호 정보 수정
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
