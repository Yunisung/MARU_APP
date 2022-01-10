package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.OthTrnsDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;

@Controller
public class OthTrmsController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.OthTrmsController.class );
	

	
	@RequestMapping(value = "/othTrns/van/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		
		cpRequest.replaceKeyName("amount", "abs(amount)");
		
		request.setAttribute("AMOUNT_SUM", new OthTrnsDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));
		
		OthTrnsDAO othTrnsDAO = new OthTrnsDAO();
//		cpRequest.setData("capId", "", "", "desc", false);
		RecordSet rset = othTrnsDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,othTrnsDAO).setView(request,"/othTrns/van/list","");
	}
	
	

}







