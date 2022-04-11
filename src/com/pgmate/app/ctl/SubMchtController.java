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
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.SettleSubDAO;
import com.pgmate.app.dao.TotCapSubDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxReqDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.AllatUtil;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class SubMchtController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.SubMchtController.class );
	
	@RequestMapping(value = "/subMcht/trx/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView capList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		cpRequest.setData("capId", "", "", "desc", false);
		RecordSet rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/subMcht/trx/list","");
	}
	
	@RequestMapping(value = "/subMcht/trx/view/{capId}", method = RequestMethod.GET)
    public ModelAndView capView(HttpServletRequest request, @PathVariable String capId) {
		SharedMap<String, Object> res =  new TrxCapDAO().getByCapId(capId).getRow(0);
		// 다날 영수증 조회용 param
		if("DANAL".equals(res.getString("van"))){
			res.put("danalParam", Base64.encodeString(res.getString("vanID")+"|"+res.getString("vanTrxId")+"|"+res.getString("amount")));
		}
			
		// 올앳 영수증 조회용 거래번호
//		if("ALLAT".startsWith(res.getString("van"))){
		if(res.startsWith("van", "ALLAT")){
			AllatUtil allatUtil = new AllatUtil();
			if(res.getString("capType").equals("매입")){
				if(new TrxReqDAO().isTrxType(res.getString("trxId"), "WHTR")){
					res.put("allatParam", allatUtil.getParam(res.getString("vanId"), res.getString("trackId"),res.getString("amount")));
				} else {
					res.put("allatParam", allatUtil.getParam(res.getString("vanId"),res.getString("trxId"),res.getString("amount")));
				}
			}else{
				SharedMap<String, Object> reqMap = new TrxReqDAO().getTrxOrgReq(capId);
				if(reqMap.getString("trxType").equals("WHTR")){
					res.put("allatParam",allatUtil.getParam(res.getString("vanId"),reqMap.getString("trackId"),res.getString("amount")));
				} else {
					res.put("allatParam",allatUtil.getParam(res.getString("vanId"),reqMap.getString("trxId"),res.getString("amount")));
				}
			}
		}
		
		//PYS : 갤럭시아 영수증 조회용
		if(res.startsWith("van", "GALAXIA")) {
			res.put("GalaxiaMID", res.getString("vanId"));
		}
		
		request.setAttribute("DATAMAP", res);
		request.setAttribute("DATAREFMAP", new TrxCapDAO().getByRootTrxId(res.getString("trxId")).getRow(0));
        return new ModelAndView("/subMcht/trx/modal");
    }
	
	@RequestMapping(value = "/subMcht/sales/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapSubDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapSubDAO totCapDAO = new TotCapSubDAO();
		RecordSet rset = totCapDAO.salesDailyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDAO).setView(request,"/subMcht/sales/list","");
	}
	
	@RequestMapping(value = "/subMcht/settle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAggregatorList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		CPDAO dao = new CPDAO();
		dao.setTable("VW_SETTLE_SUB");
		dao.setColumns("stlDay, SUM(payAmt) as payAmt, SUM(payFee) as payFee, SUM(payVat) as payVat, SUM(payCnt) as payCnt, SUM(rfdAmt) as rfdAmt, SUM(rfdFee) as rfdFee, SUM(rfdVat) as rfdVat, SUM(rfdCnt) as rfdCnt, SUM(stlAmt) as stlAmt");
		dao.setWhere("stlDay = (SELECT MAX(stlDay) FROM VW_SETTLE_SUB)");
		CPSession cpSession = SessionUtil.get(request);
		dao.addWhere("tmnId", cpSession.getParentId(), CPDAO.eq);
		
		request.setAttribute("SUMMAP", dao.search().getRow(0));
		
		SettleSubDAO settleDAO = new SettleSubDAO();
		//cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("dtlName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,settleDAO).setView(request,"/subMcht/settle/list","");
	}
	
	// =============================================================== 내 정보 보기
    @RequestMapping(value = "/subMcht/myprofile/{tmnId}", method = RequestMethod.GET)
    public ModelAndView mypage(HttpServletRequest request, @PathVariable String tmnId) {
    	SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(tmnId).getRow(0);
		request.setAttribute("DATAMAP", tmnMap);
        return new ModelAndView("/subMcht/myprofile");
    }
    
}
