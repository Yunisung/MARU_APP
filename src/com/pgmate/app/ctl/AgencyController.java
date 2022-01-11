package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.AgencyDAO;
import com.pgmate.app.dao.AgencyMngDAO;
import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.CodeDAO;
import com.pgmate.app.dao.DistDAO;
import com.pgmate.app.dao.DistMngDAO;
import com.pgmate.app.dao.HTDAO;
import com.pgmate.app.dao.MemberSalesDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class AgencyController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.AgencyController.class );
	//KJM : 멤버관리 > 에이전시 조회 리스트 이동
	@RequestMapping(value = {"/member/agency/form"})
    public ModelAndView form(HttpServletRequest request) {
		//KJM : jsp 파일에서 확인 안됨
		request.setAttribute("SELECT_DIST", new DistDAO().getSelectOption());
        return new ModelAndView("/member/agency/form");
    }
	
	//KJM : 멤버관리 > 에이전시 등록 페이지 이동
	@RequestMapping(value = {"/member/agency/add"})
    public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("/member/agency/add");
    }
	
	//KJM : 멤버 관리 > 에이전시 조회 리스트
	@RequestMapping(value = "/member/agency/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		AgencyDAO agencyDAO = new AgencyDAO();
		//KJM : 로그인 중인 계정 소속 구분
		SessionUtil.setSearchGrade(request, cpRequest);
		//KJM : 입력 조건에 상태값 안줬을 경우 "상태!=폐기" 세팅
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		//identity는 암호화되어 있으므로 복호화해서 비교해야 함.
		cpRequest.replaceKeyValue("identity",agencyDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",agencyDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		/*
		 * KJM : identity : 업체 식별번호 / ceoIdentity : 대표자 식별번호
		 */
		
		
		//KJM : 에이전시 리스트
		RecordSet rset = agencyDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,agencyDAO).setView(request,"/member/agency/list","");
	}
	
	@RequestMapping(value = "/member/agency/idList", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView idList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		AgencyDAO agencyDAO = new AgencyDAO();
		
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = agencyDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,agencyDAO).setView(request,"/member/agency/idList","");
	}
	
	//KJM : 멤버관리 > 에이전시 페이지
	@RequestMapping(value = "/member/agency/view/{agencyId}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String agencyId) throws Exception {
		request.setAttribute("DATAMAP", new AgencyDAO().getById(agencyId).getRowFirst());
		request.setAttribute("DATAMNGMAP", new AgencyMngDAO().getById(agencyId).getRowFirst());
		
        return new ModelAndView("/member/agency/view");
    }
	
	//KJM : 멤버관리 > 에이전시 정보 수정 페이지 이동
    @RequestMapping(value = "/member/agency/modify/{agencyId}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String agencyId) {
        return new ModelAndView("/member/agency/modify","DATAMAP",new AgencyDAO().getById(agencyId).getRowFirst());
    }
	
    //KJM : 멤버관리 > 에이전시 등록	
	@RequestMapping(value = {"/member/agency/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		//KJM : 조건 입력값 중 업체, 대표자 식별번호 복호화 한 값으로 바꿔줌
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		//KJM : 새로운 에이전시의 아이디 부여
		cpRequest.setData("agencyId", cpDAO.getFunction("FN_GET_AGENCY_ID", cpRequest.getValue("distId")));
		//KJM : 상위 소속인 대행사 아이디 정보 넣어줌
		cpRequest.setData("distId", cpRequest.getValue("distId"));
		//KJM : insert 쿼리문 수행
		if(cpDAO.insert("PG_MAM_AGENCY", SessionUtil.getUserId(request), cpRequest.data)){
			//KJM : 등록하는 에이전시에 대한 기본 지사 정보 생성
			//KJM : 에이전시가 등록 되어도 기본 지사가 생성 안되면 (등록 실패)
			if(new MemberSalesDAO().insertDefault(cpRequest.getValue("agencyId"))){
				SessionUtil.initSessionData(request);
				//KJM : "등록 성공하였습니다" 메시지 전달
				return new CPRUtil(cpRequest)
						.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
		        		.cpResponse();
			}
		}
		//KJM : "등록 실패하였습니다 " 메시지 전달
		return new CPRUtil(cpRequest)
        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
        		.cpResponse();
    }
	
	//KJM : 멤버관리 > 에이전시 정보 수정
	@RequestMapping(value = {"/member/agency/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		//KJM : update 쿼리문 수행
		if(cpDAO.updateAndBack("PG_MAM_AGENCY", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("사용자 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("사용자 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	// KBR : 사용 안함
    @RequestMapping(value = "/member/agency/ht/list/{agencyId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String agencyId) {
    	SessionUtil.setSearchGrade(request, cpRequest);
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(agencyId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/member/agency/ht/list","");
	}
    
    
    // ======================================================= 관리정보
    //KJM : 멤버관리 > 에이전시 지불정산정보 등록 페이지 이동
    @RequestMapping(value = {"/member/agency/mng/add/{agencyId}"})
    public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String agencyId) {
    	//KJM : 해당 에이전시 정보 가져옴
    	SharedMap<String,Object> result = new AgencyDAO().getById(agencyId).getRowFirst();
    	//KJM : 에이전시 지불정산정보, select 박스안에 들어갈 은행정보들 들고옴
    	request.setAttribute("DATADISTMNGMAP", new DistMngDAO().getById(result.getString("distId")).getRowFirst());
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/member/agency/mng/add", "DATAMAP", result);
    }
    
    
    //KJM : 멤버관리 > 에이전시 지불정산정보 수정 페이지 이동
    @RequestMapping(value = "/member/agency/mng/modify/{agencyId}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String agencyId) {
    	SharedMap<String,Object> result = new AgencyDAO().getById(agencyId).getRowFirst();
    	request.setAttribute("DATADISTMNGMAP", new DistMngDAO().getById(result.getString("distId")).getRowFirst());
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
        return new ModelAndView("/member/agency/mng/modify", "DATAMAP", new AgencyMngDAO().getById(agencyId).getRowFirst());
    }
    
    //KJM : 멤버관리 > 에이전시 지불정산정보 등록
	@RequestMapping(value = {"/member/agency/mng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//KJM : insert 쿼리문 수행
		if(cpDAO.insert("PG_MAM_AGENCY_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	//KJM : 멤버관리 > 에이전시 지불정산정보 수정
	@RequestMapping(value = {"/member/agency/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
				
		//KJM : update 쿼리문 수행
		if(cpDAO.updateAndBack("PG_MAM_AGENCY_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("사용자 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("사용자 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
}
