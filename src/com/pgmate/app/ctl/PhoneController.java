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

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.dao.PhoneDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.RefundUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

@Controller
public class PhoneController {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.PhoneController.class);
	
	//휴대폰 결제 조회 페이지
	@RequestMapping(value="/phone/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView phoneList(HttpServletRequest request, @RequestBody CPRequest cpRequest, String mchtName) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("AMOUNT_SUM", new PhoneDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));
		
		PhoneDAO phoneDAO = new PhoneDAO();
		cpRequest.replaceKeyValue("payerName",phoneDAO.getAESEnc(cpRequest.getKeyValue("payerName")));
		RecordSet rset = phoneDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,phoneDAO).setView(request,"/phone/list","");
	}
	
	//휴대폰 결제 조회 상세 페이지
	@RequestMapping(value = "/phone/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView phoneView(HttpServletRequest request, @PathVariable String trxId) {
		request.setAttribute("DATAMAP", new PhoneDAO().getByTrxId(trxId).getRow(0));
		request.setAttribute("DATAREFMAP", new PhoneDAO().getByRootTrxId(trxId).getRow(0));
        return new ModelAndView("/phone/modal");
    }
	
	//modal 취소 요청
	@RequestMapping(value = "/phone/cancel/{trxId}/{amount}", method = RequestMethod.GET)
    public @ResponseBody String cancel(HttpServletRequest request, @PathVariable String trxId, @PathVariable String amount) {
    	CPSession session = SessionUtil.get(request);
     	return new RefundUtil().phoneExecute(new PhoneDAO().getByTrxId(trxId).getRow(0), amount,SessionUtil.getUserId(request), session.getGrade());
    }
	
	//가맹점 휴대폰 결제 정보 등록
	@RequestMapping(value = "/phone/mng/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView diffAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDAO().getById(mchtId).getRowFirst());
		return new ModelAndView("/phone/mng/add");
	}
	
	//가맹점 휴대폰 결제 정보 등록
	@RequestMapping(value = {"/phone/mng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse phoneInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.insert("PG_MCHT_PHONE_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("휴대폰 결제 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("휴대폰 결제 정보 등록에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
    }
	
	//가맹점 휴대폰 결제 정보 수정
	@RequestMapping(value = "/phone/mng/modify/{mchtId}", method = RequestMethod.GET)
    public ModelAndView phoneModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT", new MchtDAO().getById(mchtId).getRowFirst());
        return new ModelAndView("/phone/mng/modify","DATAMAP",new PhoneDAO().getByMchtId(mchtId));
    }
	
	//가맹점 휴대폰 결제 정보 등록
	@RequestMapping(value = {"/phone/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse phoneUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		PhoneDAO phoneDAO = new PhoneDAO();
		if(cpDAO.update("PG_MCHT_PHONE_MNG", cpRequest.data)){
			phoneDAO.modInfo(SessionUtil.getUserId(request), CommonUtil.timestampToString(CommonUtil.getCurrentTimestamp(), "yyyyMMddHHmmss"));
			return new CPRUtil(cpRequest)
	        		.resultOK("휴대폰 결제 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("휴대폰 결제 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
}
