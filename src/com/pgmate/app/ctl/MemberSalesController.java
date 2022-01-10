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
import com.pgmate.app.dao.HTDAO;
import com.pgmate.app.dao.MemberSalesDAO;
import com.pgmate.app.dao.MemberSalesMngDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class MemberSalesController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.MemberSalesController.class );
	
	@RequestMapping(value = {"/member/sales/form"})
    public ModelAndView form(HttpServletRequest request) {
		request.setAttribute("SELECT_AGENCY", new AgencyDAO().getSelectOption());
        return new ModelAndView("/member/sales/form");
    }
	
	@RequestMapping(value = {"/member/sales/add"})
    public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("/member/sales/add");
    }
	
	@RequestMapping(value = "/member/sales/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MemberSalesDAO salesDAO = new MemberSalesDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = salesDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,salesDAO).setView(request,"/member/sales/list","");
	}
	@RequestMapping(value = "/member/sales/idList", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView idList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MemberSalesDAO salesDAO = new MemberSalesDAO();
		
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = salesDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,salesDAO).setView(request,"/member/sales/idList","");
	}
	
	@RequestMapping(value = "/member/sales/view/{salesId}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String salesId) throws Exception {
		request.setAttribute("DATAMAP", new MemberSalesDAO().getById(salesId).getRowFirst());
		request.setAttribute("DATAMNGMAP", new MemberSalesMngDAO().getById(salesId).getRowFirst());
		
        return new ModelAndView("/member/sales/view");
    }
	
    @RequestMapping(value = "/member/sales/modify/{salesId}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String salesId) {
        return new ModelAndView("/member/sales/modify","DATAMAP",new MemberSalesDAO().getById(salesId).getRowFirst());
    }
	
	@RequestMapping(value = {"/member/sales/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		cpRequest.setData("salesId", cpDAO.getFunction("FN_GET_SALES_ID"));
		if(cpDAO.insert("PG_MAM_SALES", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/member/sales/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		if(cpDAO.updateAndBack("PG_MAM_SALES", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("사용자 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("사용자 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
    @RequestMapping(value = "/member/sales/ht/list/{salesId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String salesId) {
    	SessionUtil.setSearchGrade(request, cpRequest);
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(salesId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/member/sales/ht/list","");
	}
    
    // ======================================================= 관리정보
    @RequestMapping(value = {"/member/sales/mng/add/{salesId}"})
    public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String salesId) {
    	SharedMap<String,Object> result = new MemberSalesDAO().getById(salesId).getRowFirst();
    	request.setAttribute("DATADISTMNGMAP", new AgencyMngDAO().getById(result.getString("agencyId")).getRowFirst());
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/member/sales/mng/add", "DATAMAP", result);
    }
    
    @RequestMapping(value = "/member/sales/mng/modify/{salesId}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String salesId) {
    	SharedMap<String,Object> result = new MemberSalesDAO().getById(salesId).getRowFirst();
    	request.setAttribute("DATADISTMNGMAP", new AgencyMngDAO().getById(result.getString("agencyId")).getRowFirst());
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
        return new ModelAndView("/member/sales/mng/modify", "DATAMAP", new MemberSalesMngDAO().getById(salesId).getRowFirst());
    }
	
	@RequestMapping(value = {"/member/sales/mng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.insert("PG_MAM_SALES_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/member/sales/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.updateAndBack("PG_MAM_SALES_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
}
