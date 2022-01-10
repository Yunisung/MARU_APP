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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.CodeDAO;
import com.pgmate.app.dao.HTDAO;
import com.pgmate.app.dao.LoanDAO;
import com.pgmate.app.dao.LoanMngDAO;
import com.pgmate.app.dao.MngViewDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;

@Controller
public class LoanController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.LoanController.class );
	
	@RequestMapping(value = {"/member/loan/form"})
    public ModelAndView loanForm() {
        return new ModelAndView("/member/loan/form");
    }
	
	@RequestMapping(value = {"/member/loan/add"})
    public ModelAndView loanAdd(HttpServletRequest request) {
		return new ModelAndView("/member/loan/add");
    }
	
	@RequestMapping(value = "/member/loan/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		LoanDAO loanDAO = new LoanDAO();
		
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		//identity는 암호화되어 있으므로 복호화해서 비교해야 함.
		cpRequest.replaceKeyValue("identity",loanDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",loanDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		RecordSet rset = loanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,loanDAO).setView(request,"/member/loan/list","");
	}
	@RequestMapping(value = "/member/loan/idList", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView idList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		LoanDAO loanDAO = new LoanDAO();
		
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = loanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,loanDAO).setView(request,"/member/loan/idList","");
	}
	
	@RequestMapping(value = "/member/loan/view/{loanid}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String loanid) {
		request.setAttribute("DATAMAP", new LoanDAO().getById(loanid).getRow(0));
		request.setAttribute("DATAMNGMAP", new LoanMngDAO().getById(loanid).getRow(0));
		
        return new ModelAndView("/member/loan/view");
    }
	
    @RequestMapping(value = "/member/loan/modify/{loanid}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String loanid) {
        return new ModelAndView("/member/loan/modify","DATAMAP",new LoanDAO().getById(loanid).getRow(0));
    }
	
	@RequestMapping(value = {"/member/loan/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		if(cpDAO.insert("PG_LOAN", SessionUtil.getUserId(request), cpRequest.data)){
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/member/loan/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		if(cpDAO.updateAndBack("PG_LOAN", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("사용자 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("사용자 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
    @RequestMapping(value = "/member/loan/ht/list/{loanId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String loanId) {
    	
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(loanId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/member/loan/ht/list","");
	}
    
    
    
    @RequestMapping(value = {"/member/loan/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String idCheck(HttpServletRequest request, @RequestParam("loanId") String loanId) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_LOAN");
		dao.setColumns("loanId");
		dao.addWhere("loanId", loanId, DAO.eq);
		
		if(loanId.indexOf(" ") > -1){
			return GsonUtil.toJson("선정산 아이디에는 공백이 포함 될 수 없습니다.");
		}
		if(dao.search().size() == 0){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("선정산 아이디가 이미 있습니다.");
		}
    }
    // ======================================================= 관리정보
    @RequestMapping(value = {"/member/loan/mng/add/{loanid}"})
    public ModelAndView loanMngAdd(HttpServletRequest request, @PathVariable String loanid) {
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/member/loan/mng/add","DATAMAP",new LoanDAO().getById(loanid).getRow(0));
    }
    
    @RequestMapping(value = "/member/loan/mng/modify/{loanid}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String loanid) {
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
    	request.setAttribute("LIMITMAP", new MngViewDAO().getByChildrenLimit(loanid).getRowFirst());
        return new ModelAndView("/member/loan/mng/modify","DATAMAP",new LoanMngDAO().getById(loanid).getRow(0));
    }
	
	@RequestMapping(value = {"/member/loan/mng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insert("PG_LOAN_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/member/loan/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.updateAndBack("PG_LOAN_MNG", SessionUtil.getUserId(request), cpRequest.data)){
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
