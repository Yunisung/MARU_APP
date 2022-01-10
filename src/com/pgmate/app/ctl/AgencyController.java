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
	
	@RequestMapping(value = {"/member/agency/form"})
    public ModelAndView form(HttpServletRequest request) {
		request.setAttribute("SELECT_DIST", new DistDAO().getSelectOption());
        return new ModelAndView("/member/agency/form");
    }
	
	@RequestMapping(value = {"/member/agency/add"})
    public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("/member/agency/add");
    }
	
	
	@RequestMapping(value = "/member/agency/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		AgencyDAO agencyDAO = new AgencyDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		//identity는 암호화되어 있으므로 복호화해서 비교해야 함.
		cpRequest.replaceKeyValue("identity",agencyDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",agencyDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
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
	
	@RequestMapping(value = "/member/agency/view/{agencyId}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String agencyId) throws Exception {
		request.setAttribute("DATAMAP", new AgencyDAO().getById(agencyId).getRowFirst());
		request.setAttribute("DATAMNGMAP", new AgencyMngDAO().getById(agencyId).getRowFirst());
		
        return new ModelAndView("/member/agency/view");
    }
	
    @RequestMapping(value = "/member/agency/modify/{agencyId}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String agencyId) {
        return new ModelAndView("/member/agency/modify","DATAMAP",new AgencyDAO().getById(agencyId).getRowFirst());
    }
	
	@RequestMapping(value = {"/member/agency/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		cpRequest.setData("agencyId", cpDAO.getFunction("FN_GET_AGENCY_ID", cpRequest.getValue("distId")));
		cpRequest.setData("distId", cpRequest.getValue("distId"));
		if(cpDAO.insert("PG_MAM_AGENCY", SessionUtil.getUserId(request), cpRequest.data)){
			if(new MemberSalesDAO().insertDefault(cpRequest.getValue("agencyId"))){
				SessionUtil.initSessionData(request);
				return new CPRUtil(cpRequest)
						.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
		        		.cpResponse();
			}
		}
		
		return new CPRUtil(cpRequest)
        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
        		.cpResponse();
    }
	
	@RequestMapping(value = {"/member/agency/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
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
	
    @RequestMapping(value = "/member/agency/ht/list/{agencyId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String agencyId) {
    	SessionUtil.setSearchGrade(request, cpRequest);
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(agencyId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/member/agency/ht/list","");
	}
    
    // ======================================================= 관리정보
    @RequestMapping(value = {"/member/agency/mng/add/{agencyId}"})
    public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String agencyId) {
    	SharedMap<String,Object> result = new AgencyDAO().getById(agencyId).getRowFirst();
    	request.setAttribute("DATADISTMNGMAP", new DistMngDAO().getById(result.getString("distId")).getRowFirst());
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/member/agency/mng/add", "DATAMAP", result);
    }
    
    @RequestMapping(value = "/member/agency/mng/modify/{agencyId}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String agencyId) {
    	SharedMap<String,Object> result = new AgencyDAO().getById(agencyId).getRowFirst();
    	request.setAttribute("DATADISTMNGMAP", new DistMngDAO().getById(result.getString("distId")).getRowFirst());
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
        return new ModelAndView("/member/agency/mng/modify", "DATAMAP", new AgencyMngDAO().getById(agencyId).getRowFirst());
    }
	
	@RequestMapping(value = {"/member/agency/mng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
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
	
	@RequestMapping(value = {"/member/agency/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
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
