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

import com.pgmate.app.dao.FirmErrDAO;
import com.pgmate.app.dao.FirmMasterDAO;
import com.pgmate.app.dao.FirmTrxDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.FirmBean;
import com.pgmate.app.util.FirmUtil;
import com.pgmate.app.util.IPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class FirmController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.FirmController.class );
	
	@RequestMapping(value = "/firm/trx/form", method = RequestMethod.GET)
	public ModelAndView trxForm(HttpServletRequest request) {
		DAO dao = new DAO("PG_CODE",CPUtil.CP_DEBUG);
		dao.setColumns("code,codeName");
		dao.setWhere(" ALIAS ='BANK' AND CODE IN (SELECT DISTINCT(recvBank) FROM PG_FIRM_TRX)");
		dao.setOrderBy(" codeName asc");
        return new ModelAndView("/firm/trx/form","recvBank",dao.search().getRows());
	}
	
	@RequestMapping(value = "/firm/trx/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView trxList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		request.setAttribute("AMOUNT_SUM", new FirmTrxDAO().amtSum(cpRequest.data,null).getRowFirst().getString("amount"));
		
		FirmTrxDAO firmTrxDAO = new FirmTrxDAO();
		RecordSet rset = firmTrxDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,firmTrxDAO).setView(request,"/firm/trx/list","");
	}
	
	@RequestMapping(value = "/firm/master/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView masterList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		FirmMasterDAO firmMasterDAO = new FirmMasterDAO();
		RecordSet rset = firmMasterDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,firmMasterDAO).setView(request,"/firm/master/list","");
	}
	
	@RequestMapping(value = "/firm/master/view/{idx}", method = RequestMethod.GET)
    public ModelAndView masterView(HttpServletRequest request, @PathVariable String idx) {
		request.setAttribute("DATAMAP", new FirmMasterDAO().getByIdx(idx).getRow(0));
        return new ModelAndView("/firm/master/view");
    }
	
	
	@RequestMapping(value = "/firm/err/form", method = RequestMethod.GET)
	public ModelAndView errForm(HttpServletRequest request) {
		DAO dao = new DAO("PG_CODE",CPUtil.CP_DEBUG);
		dao.setColumns("code,codeName");
		dao.setWhere(" ALIAS ='BANK' AND CODE IN (SELECT DISTINCT(recvBankCd) FROM PG_FIRM_ERR)");
		dao.setOrderBy(" codeName asc");
        return new ModelAndView("/firm/err/form","recvBank",dao.search().getRows());
	}
	
	@RequestMapping(value = "/firm/err/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView errList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		FirmErrDAO firmErrDAO = new FirmErrDAO();
		RecordSet rset = firmErrDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,firmErrDAO).setView(request,"/firm/err/list","");
	}
	
	@RequestMapping(value = "/firmaction/action", method = RequestMethod.GET)
	public ModelAndView action(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		FirmMasterDAO firmMasterDAO = new FirmMasterDAO();
		RecordSet rset = firmMasterDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,firmMasterDAO).setView(request,"/firm/master/list","");
	}
	
	
	@RequestMapping(value = "/firm/action", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody FirmBean action(HttpServletRequest request,@RequestBody SharedMap<String, Object> reqMap) {
		
		String action = reqMap.getString("action");
		String mAccnt = "";
		String bankCd = reqMap.getString("bankCd");

		//모계좌 세팅
		switch (bankCd) {
			case "089": mAccnt = "70022000000008";break;
			case "039": mAccnt = "8003344291839";break;
			case "034": mAccnt = "1107021617114";break;
			case "007": mAccnt = "101024656079";break;
			case "048": mAccnt = "131022424175";break;
			case "049": mAccnt = "131022424199";bankCd="048";break;
		}
		
		logger.debug("Action: {}, bankCd: {} , mAccnt: {}", action, bankCd, mAccnt);

		FirmUtil firm = new FirmUtil();
		FirmBean firmBean = firm.execute(mAccnt, bankCd, action, IPUtil.getIp(request), SessionUtil.getUserId(request));
		
		
		return firmBean;
	}
	
	
	@RequestMapping(value = "/firm/balanceTransfer", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody FirmBean balanceTransfer(HttpServletRequest request,@RequestBody SharedMap<String, Object> reqMap) {
		
		String action = reqMap.getString("action");
		
		String sendBankCd = reqMap.getString("sendBankCd");
		String sendAccnt = "";

		//모계좌 세팅
		switch (sendBankCd) {
			case "089": sendAccnt = "70022000000008";break;
			case "039": sendAccnt = "8003344291839";break;
			case "034": sendAccnt = "1107021617114";break;
			case "007": sendAccnt = "101024656079";break;
			case "048": sendAccnt = "131022424175";break;
			case "049": sendAccnt = "131022424199"; sendBankCd = "048";break;
		}
		
		String recvBankCd = reqMap.getString("recvBankCd");
		String recvAccnt = reqMap.getString("recvAccnt");
		
		Long amount = reqMap.getLong("amount");
	
		String sender = "㈜건흥페이먼츠";
		
		logger.info("balanceTransfer: {}, sendBankCd: {} , sendAccnt: {}, recvBankCd: {} , recvAccnt: {}, amount: {}, sender: {}", 
				action, sendBankCd, sendAccnt, recvBankCd, recvAccnt, amount, sender);

		FirmUtil firm = new FirmUtil();
		FirmBean firmBean = new FirmBean(); 
		
		if("0600300".equals(action)) {
			firmBean = firm.balanceCheck(sendAccnt, sendBankCd, action, IPUtil.getIp(request), SessionUtil.getUserId(request));
		}else if("0100100".equals(action)) {
			firmBean = firm.balanceTransfer(sendBankCd, recvBankCd, recvAccnt, amount, sender);
		}

		return firmBean;
	}
}
