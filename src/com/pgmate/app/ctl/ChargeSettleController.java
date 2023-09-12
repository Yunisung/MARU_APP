package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgmate.app.dao.*;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;


/* KBR 
 * 충전정산
 * */
@Controller
public class ChargeSettleController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.ChargeSettleController.class );
	

	
	@RequestMapping(value = "/chargeSettle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView trxList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		ChargeSettleDAO dao = new ChargeSettleDAO();

		request.setAttribute("SUMMAP", dao.depositSum(cpRequest.data,null).getRow(0));

//		RecordSet rset = dao.list(cpRequest.data,cpRequest.page);
		RecordSet rset = dao.listWithDecAccount(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/chargeSettle/list","");
		
	}
	
	@RequestMapping(value = "/chargeSettle/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView trxView(HttpServletRequest request, @PathVariable("trxId") String trxId) {
		request.setAttribute("DATAMAP", new ChargeSettleDAO().getById(trxId).getRow(0));
		request.setAttribute("DATANOTIMAP", new ChargeSettleNotiDAO().getById(trxId).getRow(0));
        return new ModelAndView("/chargeSettle/modal");

	}

	@RequestMapping(value = "/chargeSettle/retry/{trxId}", method = RequestMethod.GET)
	public @ResponseBody
	SharedMap<String, Object> trxRetry(HttpServletRequest request, @PathVariable String trxId) {
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		resMap.put("result", "NOK");
		DAO dao = new DAO();
		if (dao.update("UPDATE PG_CHARGE_SETTLE_NOTI SET status='전송장애', retry='1' WHERE trxId ='" + trxId + "'")) {
			resMap.put("result", "OK");
		} else {
			resMap.put("msg", "재전송에 실패했습니다. 관리자에게 문의해주세요.");
		}
		return resMap;
	}
	
	@RequestMapping(value = "/chargeSettle/err/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView errList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		ChargeSettleErrDAO dao = new ChargeSettleErrDAO();
		RecordSet rset = dao.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/chargeSettle/err/list","");
		
	}
	
	@RequestMapping(value = "/chargeSettle/err/view/{trxId}", method = RequestMethod.GET)
	public ModelAndView errView(HttpServletRequest request, @PathVariable("trxId") String trxId) {
		request.setAttribute("DATAMAP", new ChargeSettleErrDAO().getById(trxId).getRow(0));
		return new ModelAndView("/chargeSettle/err/modal");
		
		
	}
	
	@RequestMapping(value = "/chargeSettle/auto/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView autoList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		ChargeAutoSettleDAO dao = new ChargeAutoSettleDAO();
		request.setAttribute("SUMMAP", new ChargeAutoSettleDAO().trxSum(cpRequest.data,null).getRowFirst());
		RecordSet rset = dao.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,dao).setView(request,"/chargeSettle/auto/list","");
		
	}
}
