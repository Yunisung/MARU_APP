package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.pgmate.app.dao.LoanSettleDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

@Controller
// KBR : 대출 정산 
public class LoanSettleController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.LoanSettleController.class );
	

	
	@RequestMapping(value = "/loanSettle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loanList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new LoanSettleDAO().calcSum(cpRequest.data).getRow(0));
		LoanSettleDAO dao = new LoanSettleDAO();
		cpRequest.replaceKeyValue("identity",dao.getAESEnc(cpRequest.getKeyValue("identity")));
		RecordSet rset = dao.loanList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/loanSettle/list","");
		
	}
	
	@RequestMapping(value = "/loanSettle/status/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loanDtlList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new LoanSettleDAO().calcSettleList(cpRequest.data).getRow(0));
		request.setAttribute("AMTSUMMAP", new LoanSettleDAO().calcAmountList(cpRequest.data).getRow(0));
		LoanSettleDAO dao = new LoanSettleDAO();
		RecordSet rset = dao.loanDtlList(cpRequest.data,cpRequest.page);
		int i = 0;
		for (SharedMap<String, Object> map : rset.getRows()) {
			if(map.getString("loanType").equals("대출실행")) {
				rset.getRow(i).replace("loanStlId", "-");
				rset.getRow(i).replace("payCk", "-");
				rset.getRow(i).replace("delayCk", "-");
			} else {
				rset.getRow(i).replace("amount", 0);
			}
			i++;
		}
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/loanSettle/status/list","");
		
	}
	
	@RequestMapping(value = "/loanSettle/settleList/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loanSettleList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new LoanSettleDAO().calcSettleList(cpRequest.data).getRow(0));
		LoanSettleDAO dao = new LoanSettleDAO();
		RecordSet rset = dao.loanStlList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/loanSettle/settleList/list","");
		
	}
	
	
	@RequestMapping(value = "/loanSettle/settle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loanSettle(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		LoanSettleDAO dao = new LoanSettleDAO();
		RecordSet rset = dao.loanStl(cpRequest.data,cpRequest.page);
		int totAmt = 0;
		for(SharedMap<String,Object> data : rset.getRows()) {
			totAmt += data.getInt("payAmt");
		}
		request.setAttribute("totAmt", totAmt);
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/loanSettle/settle/list","");
		
	}
	
	@RequestMapping(value = "/settle/loanSettle/payout/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMchtPayout(HttpServletRequest request, @PathVariable String status, @RequestBody String loanStlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("loanStlId : {} => {}", loanStlId, status);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_LOAN_SETTLE");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("loanStlId", loanStlId, DAO.in);

		if (status.equals("지급완료")) {
			dao.addWhere("payStatus", "지급완료", DAO.eq);
		} else if (status.equals("지급보류")) {
			dao.addWhere("payStatus", "'지급완료','지급보류'", DAO.in);
		} else {
			logger.error("지급 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "지급 상태 변경에 실패하였습니다.");
			return resultMap;
		}
		
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			dao.initRecord();
			dao.update("UPDATE PG_LOAN_SETTLE SET payStatus = '"+status+"' WHERE loanStlId IN ("+loanStlId+")");
			resultMap.put("result", "OK");
		}else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'" + status + "' 상태로 변경할 수 없는 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = {"/loanSettle/prepay/form"})
    public ModelAndView prepayForm(HttpServletRequest request) {
		RecordSet rset = new UserDAO().getById(SessionUtil.getUserId(request));
		if(rset.getRowFirst().getString("grade").equals("본사")) {
			request.setAttribute("DATAMAP", new LoanSettleDAO().getAllMchtList());
		} else {
			request.setAttribute("DATAMAP", new LoanSettleDAO().getMchtList(SessionUtil.getParentId(request)));
		}
		
        return new ModelAndView("/loanSettle/prepay/form");
    }
	
	@RequestMapping(value = "/mchtId/loanCheck/{mchtId}", method = RequestMethod.GET)
	public @ResponseBody Object prepayCheck(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String, Object> map = new LoanSettleDAO().getLoanList(mchtId).getRowFirst();
		map.put("maxCnt", map.getInt("conCnt")-map.getInt("payCnt"));
		SharedMap<String, Object> beforeMap = new LoanSettleDAO().getBeforeLoanDtl(mchtId);
		if(beforeMap != null) {
			map.put("delayAmt", beforeMap.getString("delayAmt"));
		} else {
			map.put("delayAmt", 0);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/loanSettle/prepay/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse prepayUpdate(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		String loanStlId = new LoanSettleDAO().getLoanSettleStlId();
		SharedMap<String, Object> requestMap = new SharedMap<String, Object>();
		SharedMap<String, Object> beforeMap = new LoanSettleDAO().getBeforeLoanDtl(cpRequest.getValue("mchtId"));
		requestMap.put("loanStlId", loanStlId);
		requestMap.put("loanId", cpRequest.getValue("loanId"));
		requestMap.put("prepayType", cpRequest.getValue("prepayType"));
		requestMap.put("totPayAmt", cpRequest.getValue("totPayAmt"));
		requestMap.put("balance", cpRequest.getValue("balance"));
		requestMap.put("prepayAmt", cpRequest.getValue("prepayAmt"));
		requestMap.put("delayCnt", cpRequest.getValue("delayCnt"));
		requestMap.put("payCnt", cpRequest.getValue("payCnt"));
		requestMap.put("paySession", cpRequest.getValue("paySession"));
		requestMap.put("maxCnt", cpRequest.getValue("maxCnt"));
		requestMap.put("prepayCnt", cpRequest.getValue("prepayCnt"));

		if(new LoanSettleDAO().prepayUpdate(requestMap, beforeMap) && new LoanSettleDAO().prepayInsert(requestMap, beforeMap, SessionUtil.getUserId(request))) {
			return new CPRUtil(cpRequest)
					.resultOK("중도상환이 완료되었습니다.")
		    		.cpResponse();
		} else {
			return new CPRUtil(cpRequest) 
					.resultOK("중도상환에 실패하였습니다.")
		    		.cpResponse();
		}
	}
	

}
