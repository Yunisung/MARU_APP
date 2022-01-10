package com.pgmate.app.ctl;

import java.util.ArrayList;
import java.util.List;

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
import com.pgmate.app.dao.FaqDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class FaqController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.FaqController.class );
	
	@RequestMapping(value = {"/system/faq/form"})
    public ModelAndView form(HttpServletRequest request) {
        return new ModelAndView("/system/faq/form");
    }
	
	@RequestMapping(value = {"/system/faq/add"})
    public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("/system/faq/add");
    }
	
	@RequestMapping(value = "/system/faq/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		FaqDAO faqDAO = new FaqDAO();
		CPSession session = SessionUtil.get(request);
		if(!(session.getGrade().equals("본사") && !session.getRole().equals("일반"))){
			cpRequest.setData("status", "개시", "eq", "", true);
			cpRequest.setData("pubDay", CommonUtil.getCurrentDate("yyyyMMdd"), "le", "", true);
			cpRequest.setData("closeDay", CommonUtil.getCurrentDate("yyyyMMdd"), "ge", "", true);
		}
		
		RecordSet rset = faqDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,faqDAO).setView(request,"/system/faq/list","");
	}
	
	//읽기 전용 FAQ 페이지
	@RequestMapping(value = "/system/faq/view", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView view(HttpServletRequest request) {
		List<SharedMap<String, Object>> list = new FaqDAO().searchView().getRows();
		for(SharedMap<String, Object> map:list){
			map.put("summary", CommonUtil.unescapeHtml(map.getString("summary")));
		}
		request.setAttribute("DATAMAP", list);
		return new ModelAndView("/system/faq/view");
	}
	
	@RequestMapping(value = "/system/faq/modal/{idx}", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> map = new FaqDAO().getById(idx).getRowFirst();
		map.put("summary", CommonUtil.unescapeHtml(map.getString("summary")));
		request.setAttribute("DATAMAP", map);
		return new ModelAndView("/system/faq/modal");
    }
	
    @RequestMapping(value = "/system/faq/modify/{idx}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String idx) {
    	SharedMap<String, Object> map = new FaqDAO().getById(idx).getRowFirst();
		map.put("summary", CommonUtil.unescapeHtml(map.getString("summary")));
		request.setAttribute("DATAMAP", map);
        return new ModelAndView("/system/faq/modify");
    }
	
	@RequestMapping(value = {"/system/faq/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.insert("PG_FAQ", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
				.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
		        .cpResponse();
		} else {
			return new CPRUtil(cpRequest)
        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/system/faq/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.update("PG_FAQ", SessionUtil.getUserId(request), cpRequest.data)){
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
