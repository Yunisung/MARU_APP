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
import com.pgmate.app.dao.TmsIODAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;

@Controller
public class TMSController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.TMSController.class );
	

	@RequestMapping(value = "/tms/io/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView ioList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		
		
		TmsIODAO tmsIoDAO = new TmsIODAO();
//		cpRequest.setData("capId", "", "", "desc", false);
		RecordSet rset = tmsIoDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,tmsIoDAO).setView(request,"/tms/io/list","");
	}
	
	

}







