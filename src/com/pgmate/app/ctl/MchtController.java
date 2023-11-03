package com.pgmate.app.ctl;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgmate.app.dao.*;
import com.pgmate.app.security.filter.CustomWebAuthenticationDetails;
import com.pgmate.app.util.*;
import com.pgmate.lib.sms.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
//import com.pgmate.app.dao.SimpleDAO;
import com.pgmate.app.interceptor.SessionExclude;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Interest;
import com.pgmate.app.model.ajax.TmnList;
import com.pgmate.app.session.CPSession;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.cipher.SeedKisa;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

import hanati.openapi.cipher.blockcipher.HanaTICryptoUtil;

@Controller
public class MchtController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.MchtController.class );

	@RequestMapping(value = {"/mcht/form"})
    public ModelAndView form(HttpServletRequest request) {
		return new ModelAndView("/mcht/form");
    }
	
	@RequestMapping(value = {"/mcht/add"})
    public ModelAndView add(HttpServletRequest request) {
		return new ModelAndView("/mcht/add");
    }
	
	@RequestMapping(value = "/mcht/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		cpRequest.replaceKeyValue("identity",mchtDAO.getAESEnc(cpRequest.getKeyValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",mchtDAO.getAESEnc(cpRequest.getKeyValue("ceoIdentity")));
		RecordSet rset = mchtDAO.exList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/list","");
	}
	
	@RequestMapping(value = "/mcht/dist/mnglist/{payType}/{distId}/{num}", method = RequestMethod.GET)
    public ModelAndView mchtDistMngList(HttpServletRequest request, @PathVariable String payType, @PathVariable String distId, @PathVariable String num) {
		request.setAttribute("SEARCH_DISTID", distId);
		request.setAttribute("SEARCH_NUM", num);
		request.setAttribute("SEARCH_PAYTYPE", payType);
		request.setAttribute("SEARCH_SETTLENAME", new DistMngDAO().getByNum(num).getRowFirst().getString("settleName"));
		
        return new ModelAndView("/mcht/dist/form");
    }
	
	@RequestMapping(value = "/mcht/dist/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtDistList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		RecordSet rset;
		
		for(Data data :  cpRequest.data) {
			logger.debug("CPRequest {}, {}", data.name, data.val);
		}
		if(cpRequest.getKeyValue("payType").equals("신용카드")) {
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngList(cpRequest.data,cpRequest.page);
		} else if(cpRequest.getKeyValue("payType").equals("가상계좌")){
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngVactList(cpRequest.data,cpRequest.page);
		} else {
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngSimpleList(cpRequest.data,cpRequest.page);
		}
		
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/list","");
	}
	
	@RequestMapping(value = "/mcht/agency/mnglist/{payType}/{agencyId}/{num}", method = RequestMethod.GET)
    public ModelAndView mchtAgencyMngList(HttpServletRequest request, @PathVariable String payType, @PathVariable String agencyId, @PathVariable String num) {
		request.setAttribute("SEARCH_AGENCYID", agencyId);
		request.setAttribute("SEARCH_NUM", num);
		request.setAttribute("SEARCH_PAYTYPE", payType);
		request.setAttribute("SEARCH_SETTLENAME", new AgencyMngDAO().getByNum(num).getRowFirst().getString("settleName"));
        return new ModelAndView("/mcht/agency/form");
    }
	
	@RequestMapping(value = "/mcht/agency/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtAgnecyList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		RecordSet rset;
		
		for(Data data :  cpRequest.data) {
			logger.debug("CPRequest {}, {}", data.name, data.val);
		}
		
		if(cpRequest.getKeyValue("payType").equals("신용카드")) {
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngList(cpRequest.data,cpRequest.page);
		} else if(cpRequest.getKeyValue("payType").equals("가상계좌")){
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngVactList(cpRequest.data,cpRequest.page);
		} else {
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngSimpleList(cpRequest.data,cpRequest.page);
		}
		
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/list","");
	}
	
	@RequestMapping(value = "/mcht/sales/mnglist/{payType}/{salesId}/{num}", method = RequestMethod.GET)
    public ModelAndView mchtSalesMngList(HttpServletRequest request, @PathVariable String payType, @PathVariable String salesId, @PathVariable String num) {
		request.setAttribute("SEARCH_SALESID", salesId);
		request.setAttribute("SEARCH_NUM", num);
		request.setAttribute("SEARCH_PAYTYPE", payType);
		request.setAttribute("SEARCH_SETTLENAME", new MemberSalesMngDAO().getByNum(num).getRowFirst().getString("settleName"));
        return new ModelAndView("/mcht/sales/form");
    }
	
	@RequestMapping(value = "/mcht/sales/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtSalesList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		RecordSet rset;
		
		for(Data data :  cpRequest.data) {
			logger.debug("CPRequest {}, {}", data.name, data.val);
		}
		
		if(cpRequest.getKeyValue("payType").equals("신용카드")) {
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngList(cpRequest.data,cpRequest.page);
		} else if(cpRequest.getKeyValue("payType").equals("가상계좌")){
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngVactList(cpRequest.data,cpRequest.page);
		} else {
			cpRequest.deleteKeyData("payType");
			rset = mchtDAO.mngSimpleList(cpRequest.data,cpRequest.page);
		}
		
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/list","");
	}
	
	@RequestMapping(value = "/mcht/idList", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView idList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = mchtDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/idList","");
	}
	
	@RequestMapping(value = "/mcht/view/{mchtId}/{tab}", method = RequestMethod.GET)
    public ModelAndView viewTab(HttpServletRequest request, @PathVariable String mchtId, @PathVariable String tab) {
		MchtDAO mchtDAO = new MchtDAO();
		MchtMngDAO mchtMngDAO = new MchtMngDAO();
		MchtTmnDAO mchtTmnDAO = new MchtTmnDAO();
		MchtDdctDAO mchtDdctDAO = new MchtDdctDAO();
		MchtTaxDAO mchtTaxDAO = new MchtTaxDAO();
		MchtInterestDAO mchtInterestDAO = new MchtInterestDAO();
		MemberSalesDAO memberSalesDAO = new MemberSalesDAO();
		LoanSettleDAO loanDAO = new LoanSettleDAO(); 
		
		//long startTime = System.currentTimeMillis();
		request.setAttribute("DATAMAP", mchtDAO.getById(mchtId).getRowFirst());
		mchtDAO.initRecord();
		request.setAttribute("DATAMNGMAP", mchtMngDAO.getById(mchtId).getRowFirst());
		mchtMngDAO.initRecord();
		request.setAttribute("DATASALESMNGMAP", memberSalesDAO.getSalesMngByMchtId(mchtId).getRowFirst());
		memberSalesDAO.initRecord();
		request.setAttribute("DATATMNMAP", mchtTmnDAO.getByMchtId(mchtId).getRows());
		mchtTmnDAO.initRecord();
		
		request.setAttribute("DATATAXMAP", mchtTaxDAO.getWithUsedLimit2(mchtId).getRows());
		mchtTaxDAO.initRecord();
		
		request.setAttribute("DATADDCTMAP", mchtDdctDAO.getByMchtId(mchtId).getRows());
		mchtDdctDAO.initRecord();

		request.setAttribute("DATAINTERMAP", mchtInterestDAO.getByMchtId(mchtId).getRows());
		mchtInterestDAO.initRecord();
		
		request.setAttribute("DATALOANMAP", loanDAO.getById(mchtId).getRowFirst());
		loanDAO.initRecord();
		
		request.setAttribute("DATADISTMAP", new DistMngDAO().getById(mchtDAO.getById(mchtId).getRowFirst().getString("distId")).getRowFirst());
		request.setAttribute("DATAAGENCYMAP", new AgencyMngDAO().getById(mchtDAO.getById(mchtId).getRowFirst().getString("agencyId")).getRowFirst());
		request.setAttribute("DATASALESMAP", new MemberSalesMngDAO().getById(mchtDAO.getById(mchtId).getRowFirst().getString("salesId")).getRowFirst());
		
		//long startTime1 = System.currentTimeMillis();
		//logger.info("FIRST TIME : {}", (startTime1 - startTime));
		
		request.setAttribute("DATATRXMAP", new TrxCapDAO().getByMchtId(mchtId, 20).getRows());
		request.setAttribute("DATATOTMAP", new TotCapDAO().getByMchtId(mchtId, 20).getRows());
		SharedMap<String, Object> svcMap = new MchtSvcDAO().getByMchtId(mchtId);
		request.setAttribute("DATASVCMAP", svcMap);
		if (svcMap.getString("virAccount").equals("사용")) {
			request.setAttribute("VACT_MAP", new MchtVactDAO().getByMchtId(mchtId));
			if(request.getAttribute("VACT_MAP") != null){
				request.setAttribute("VACTDISTMAP", new DistMngDAO().getById(mchtDAO.getById(mchtId).getRowFirst().getString("distId")).getRowFirst());
				request.setAttribute("VACTAGENCYMAP", new AgencyMngDAO().getById(mchtDAO.getById(mchtId).getRowFirst().getString("agencyId")).getRowFirst());
				request.setAttribute("VACTSALESMAP", new MemberSalesMngDAO().getById(mchtDAO.getById(mchtId).getRowFirst().getString("salesId")).getRowFirst());
				request.setAttribute("ORGFEEMAP", new CodeDAO().getOrgFee("ORGFEE").getRows());
			}
		}
		//230619 PG_MCHT_MNG_PISP(지급대행) 테이블 미존재로 주석처리
//		if (svcMap.getString("pisp").equals("사용")) {
//			request.setAttribute("PISP_MAP", new MchtPispDAO().getByMchtId(mchtId));
//		}

//		if(svcMap.getString("rebill").equals("사용")) {
//			request.setAttribute("REBILL_MAP", new MchtRebillDAO().getByMchtId(mchtId));
//		}

		if(svcMap.getString("rent").equals("사용")) {
			request.setAttribute("RENT_MAP", new MchtRentDAO().getByMchtId(mchtId));
		}
		
		//가맹점 휴대폰 결제 정보
		request.setAttribute("DATAPHONEMAP", new PhoneDAO().getByMchtId(mchtId));
		
		request.setAttribute("DATADOCMAP", new FileDAO().getByMchtId(mchtId,SessionUtil.get(request).getGrade(), 500).getRows());
		request.setAttribute("TAB", tab);
		
		request.setAttribute("DATADIFFMAP", new MchtDiffDAO().getDiffUpload(mchtId).getRowFirst());
		request.setAttribute("DATADIFFLIST", new MchtDiffDAO().getDiffDownload(mchtId).getRows());
		
		// 가맹점 충전정산 설정 정보
		request.setAttribute("DATACHARGEMAP", new MchtChargeSettleDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATABALMAP", new MchtChargeSettleDAO().getBalance(mchtId));
		
		//가맹점 간편 결제 설정 
//		request.setAttribute("DATASIMPLEMAP", new SimpleDAO().getByMchtId(mchtId));
//		if(request.getAttribute("DATASIMPLEMAP") != null){
//			request.setAttribute("SIMPLE_DISTMAP", new DistMngDAO().getByNum(new SimpleDAO().getByMchtId(mchtId).getString("distNum")).getRowFirst());
//			request.setAttribute("SIMPLE_AGENCYMAP", new AgencyMngDAO().getByNum(new SimpleDAO().getByMchtId(mchtId).getString("agencyNum")).getRowFirst());
//			request.setAttribute("SIMPLE_SALESMAP", new MemberSalesMngDAO().getByNum(new SimpleDAO().getByMchtId(mchtId).getString("salesNum")).getRowFirst());
//		}
		
	//	long startTime2 = System.currentTimeMillis();
	//	logger.info("SECOND TIME : {}", (startTime2 - startTime1));
		
		request.setAttribute("PG_MAP", mchtDAO.getPgById(mchtId).getRowFirst());
		request.setAttribute("PG_MNG_MAP", mchtMngDAO.getById(mchtId).getRowFirst());
		request.setAttribute("PG_TAX_MAP", mchtTaxDAO.getByMchtId(mchtId).getRowFirst());
		request.setAttribute("PG_TMN_MAP", mchtTmnDAO.getPgByMchtId(mchtId).getRowFirst());
		request.setAttribute("PG_VACT_MNG_MAP", mchtTmnDAO.getVactByMchtId(mchtId).getRowFirst());

		request.setAttribute("HT_MAP", mchtDAO.getHtById(mchtId).getRows());
		request.setAttribute("HT_MNG_MAP", mchtMngDAO.getHtById(mchtId).getRows());
		request.setAttribute("HT_TAX_MAP", mchtTaxDAO.getHtByMchtId(mchtId).getRows());
		request.setAttribute("HT_TMN_MAP", mchtTmnDAO.getHtByMchtId(mchtId).getRows());
		request.setAttribute("HT_VACT_MNG_MAP", mchtTmnDAO.getHtVactByMchtId(mchtId).getRows());


		//230112_PYS : 통합인증 탭 추가
		request.setAttribute("TOTALAUTH_MAP", new MchtTotalAuthDAO().getByMchtId(mchtId));
		
	//	long startTime3 = System.currentTimeMillis();
	//	logger.info("THIRD TIME : {}", (startTime3 - startTime2));
        return new ModelAndView("/mcht/view");
    }
	
    @RequestMapping(value = "/mcht/modify/{mchtId}", method = RequestMethod.GET)
    public ModelAndView modify(HttpServletRequest request, @PathVariable String mchtId) {
        return new ModelAndView("/mcht/modify","DATAMAP",new MchtDAO().getById(mchtId).getRowFirst());
    }
	
	@RequestMapping(value = {"/mcht/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		SharedMap<String, Object> parents = new MemberSalesDAO().getParentsId(cpRequest.getValue("salesId"));
		cpRequest.setData("agencyId", parents.getString("agencyId"));
		cpRequest.setData("distId", parents.getString("distId"));
		cpRequest.setData("activeDate", CommonUtil.getCurrentDate("yyyyMMdd"));

		if(cpDAO.insert("PG_MCHT", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
		        	.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/mcht/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		if(cpDAO.updateAndBack("PG_MCHT", SessionUtil.getUserId(request), cpRequest.data)){
			/*
			// 분리정산 터미널 여부 확인
			WalletDAO walletDAO = new WalletDAO();
			List<SharedMap<String, Object>> walletList = walletDAO.getWalletBymchtId(cpRequest.getKeyValue("mchtId"));
			if(walletList != null) {
				for(SharedMap<String, Object> map:walletList) {
					SharedMap<String, Object> walletMap = new SharedMap<String, Object>();
					walletMap.put("walletId", map.getString("walletId"));
					walletMap.put("tel", cpRequest.getValue("tel1"));
					walletMap.put("phone", cpRequest.getValue("ceoPhone"));
					walletMap.put("zip", cpRequest.getValue("zip"));
					walletMap.put("addr1", cpRequest.getValue("addr1"));
					walletMap.put("addr2", cpRequest.getValue("addr2"));
					walletDAO.updateWallet(walletMap);
				}
			}*/
			String identity = cpDAO.getAESDec(cpRequest.getValue("identity"));
			if(!CommonUtil.isNullOrSpace(identity)) {
				String diffType = new MchtDiffDAO().getDiffTypeByIdentity(identity).getRowFirst().getString("mchtType");
				if(!CommonUtil.isNullOrSpace(diffType)) {
					new MchtMngDAO().updateDiffType(diffType, cpRequest.getKeyValue("mchtId"));
				} else {
					new MchtMngDAO().updateDiffType("일반", cpRequest.getKeyValue("mchtId"));
				}
			}
			
			return new CPRUtil(cpRequest)
	        		.resultOK("가맹점 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("가맹점 정보 변경에 실패했습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	 @RequestMapping(value = "/mcht/loanSettle/add/{mchtId}", method = RequestMethod.GET)
	    public ModelAndView loanAdd(HttpServletRequest request, @PathVariable String mchtId) {
	        return new ModelAndView("/mcht/loanSettle/add","DATAMAP",new MchtDAO().getById(mchtId).getRowFirst());
	    }
	 
	@RequestMapping(value = {"/mcht/loanSettle/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse loanInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		
		cpRequest.setData("loanId", cpDAO.getFunction("FN_GET_LOAN_ID"));
		
		LoanSettleDAO loanDAO = new LoanSettleDAO();
		String loanStlId = loanDAO.getLoanSettleStlId();
		
		SharedMap<String, Object> submitUserData = loanDAO.getSubmitUser(SessionUtil.getUserId(request));
		SharedMap<String, Object> mchtData = loanDAO.getMcht(cpRequest.getValue("mchtId"));
		SharedMap<String, Object> loanStlMap = new SharedMap<String,Object>();
		
		loanStlMap.put("loanStlId", loanStlId);
		loanStlMap.put("loanId", cpRequest.getValue("loanId"));
		if(submitUserData.getString("grade").equals("대행사")) {
			loanStlMap.put("distId", submitUserData.getString("parentId"));
		} else if(submitUserData.getString("grade").equals("에이전시")) {
			loanStlMap.put("agencyId", submitUserData.getString("parentId"));
		} else if(submitUserData.getString("grade").equals("지사")) {
			loanStlMap.put("salesId", submitUserData.getString("parentId"));
		}
		loanStlMap.put("mchtId", cpRequest.getValue("mchtId"));
//		loanStlMap.put("name", cpRequest.getValue("name"));
		loanStlMap.put("submitGrade", submitUserData.getString("grade"));
		loanStlMap.put("submitId", submitUserData.getString("parentId"));
		loanStlMap.put("submitName", submitUserData.getString("name"));
		loanStlMap.put("loanType", "대출실행");
		loanStlMap.put("trxDay", cpRequest.getValue("loanDay"));
		loanStlMap.put("conCnt", cpRequest.getValue("conCnt"));
		cpRequest.replaceValue("amount", String.valueOf(cpRequest.getValue("amount")).replace(",", ""));
		loanStlMap.put("amount", cpRequest.getLongValue("amount"));
		loanStlMap.put("conAmt", cpRequest.getLongValue("amount")/cpRequest.getLongValue("conCnt"));
		loanStlMap.put("balance", cpRequest.getLongValue("amount"));
		String regDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
		loanStlMap.put("regId", SessionUtil.getUserId(request));
		loanStlMap.put("regDay", regDate.substring(0, 8));

		if(submitUserData.getString("grade").equals("대행사")) {
			cpRequest.setData("distId", submitUserData.getString("parentId"));
		} else if(submitUserData.getString("grade").equals("에이전시")) {
			cpRequest.setData("agencyId", submitUserData.getString("parentId"));
		} else if(submitUserData.getString("grade").equals("지사")) {
			cpRequest.setData("salesId", submitUserData.getString("parentId"));
		}
		cpRequest.setData("submitGrade", submitUserData.getString("grade"));
		cpRequest.setData("submitId", submitUserData.getString("parentId"));
		cpRequest.setData("submitName", submitUserData.getString("name"));
//		cpRequest.setData("ceoName", mchtData.getString("ceoName"));
//		cpRequest.setData("identity", mchtData.getString("identity"));
		cpRequest.setData("conAmt", cpRequest.getLongValue("amount")/cpRequest.getLongValue("conCnt"));
		cpRequest.setData("balance", cpRequest.getLongValue("amount"));
		
		if(cpDAO.insert("PG_LOAN", SessionUtil.getUserId(request), cpRequest.data)){
			loanDAO.insertLoanStl(loanStlMap);
			new MchtMngDAO().updateLoanStatus(cpRequest.getValue("mchtId"));
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
		        	.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	 @RequestMapping(value = "/mcht/loanSettle/modify/{loanId}", method = RequestMethod.GET)
    public ModelAndView loanModify(HttpServletRequest request, @PathVariable String loanId) {
		SharedMap<String, Object> sharedMap = new LoanSettleDAO().getByLoanId(loanId);
    	sharedMap.put("amount", CommonUtil.moneyFormat(sharedMap.getString("amount")));
    	request.setAttribute("DATAMAP", sharedMap);
        return new ModelAndView("/mcht/loanSettle/modify");
    }
	 
	@RequestMapping(value = {"/mcht/loanSettle/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse loanUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		LoanSettleDAO loanDAO = new LoanSettleDAO();
		SharedMap<String, Object> loanStlMap = new SharedMap<String,Object>();
		SharedMap<String, Object> loanMap = loanDAO.getLoanByMcthId(cpRequest.getValue("mchtId"));
		loanStlMap.put("loanId", loanMap.getString("loanId"));
		loanStlMap.put("trxDay", cpRequest.getValue("loanDay"));
		loanStlMap.put("conCnt", cpRequest.getValue("conCnt"));
		cpRequest.replaceValue("amount", String.valueOf(cpRequest.getValue("amount")).replace(",", ""));
		loanStlMap.put("amount", cpRequest.getLongValue("amount"));
		loanStlMap.put("conAmt", cpRequest.getLongValue("amount")/cpRequest.getLongValue("conCnt"));
		loanStlMap.put("balance", cpRequest.getLongValue("amount"));
		String regDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
		loanStlMap.put("regId", SessionUtil.getUserId(request));
		loanStlMap.put("regDay", regDate.substring(0, 8));
		cpRequest.setData("conAmt", cpRequest.getLongValue("amount")/cpRequest.getLongValue("conCnt"));
		cpRequest.setData("balance", cpRequest.getLongValue("amount"));
		
		if(cpDAO.update("PG_LOAN", SessionUtil.getUserId(request), cpRequest.data)){
			loanDAO.updateLoanStl(loanStlMap);
			return new CPRUtil(cpRequest)
	        		.resultOK("가맹점 대출 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("가맹점 대출 정보 변경에 실패했습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
    @RequestMapping(value = "/mcht/ht/list/{mchtId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String mchtId) {
    	SessionUtil.setSearchGrade(request, cpRequest);
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(mchtId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/mcht/ht/list","");
	}
    
    @RequestMapping(value = {"/mcht/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String idCheck(HttpServletRequest request, @RequestParam("mchtId") String mchtId) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_MCHT");
		dao.setColumns("mchtId");
		dao.addWhere("mchtId", mchtId, DAO.eq);

		if(!Pattern.matches("^[0-9a-zA-Z]*$", mchtId)) {
			return GsonUtil.toJson("가맹점 아이디는 영문 및 숫자만 가능합니다.");
		}
		
		if(mchtId.indexOf(" ") > -1){
			return GsonUtil.toJson("가맹점 아이디에는 공백이 포함 될 수 없습니다.");
		}
		
		if(!SQLInjectionUtil.checkInjectionValue(mchtId)) {
			return GsonUtil.toJson("사용할 수 없는 가맹점 아이디입니다.");
		}
		
		if(dao.search().size() == 0){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("가맹점 아이디가 이미 있습니다.");
		}
    }
    
    // 가맹점 소속 변경
    @RequestMapping(value = "/mcht/change/{mchtId}", method = RequestMethod.GET)
    public ModelAndView viewTab(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDAO().getById(mchtId).getRowFirst());
        return new ModelAndView("/mcht/change");
    }
    
    @RequestMapping(value = {"/mcht/change/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse changeUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		String salesId = cpRequest.getValue("salesId");
		SharedMap<String,Object> salesMap = new MemberSalesDAO().getById(salesId).getRow(0);
		SharedMap<String,Object> agencyMap = new AgencyDAO().getById(salesMap.getString("agencyId")).getRow(0);
		cpRequest.setData("salesId", salesId);
		cpRequest.setData("agencyId", salesMap.getString("agencyId"));
		cpRequest.setData("distId", agencyMap.getString("distId"));
		
		/* 소속 변경시 수수료 재계산 로직 삭제 
		cpDAO.setTable("VW_TRX_CAP");
		cpDAO.setColumns("COUNT(*) as cnt");
		cpDAO.setWhere(" capId IN (SELECT capId FROM PG_TRX_CAP WHERE mchtId = '"+ cpRequest.getKeyValue("mchtId") +"') ");
		cpDAO.addWhere("stlDistId", "",CPDAO.eq);
		cpDAO.addWhere("stlAgencyId", "",CPDAO.eq);
		cpDAO.addWhere("stlSalesId", "",CPDAO.eq);
		long trxCapCnt =  cpDAO.search().getRowFirst().getLong("cnt");
		logger.debug("CHANGE TRX CAP CNT : {}", trxCapCnt);
		if(trxCapCnt > 0) {
			if(!cpDAO.insert("INSERT INTO HT_TRX_CAP_MOVED (capId, trxId, mchtId, tmnId, trackId, capType, rfdType, rootTrxId, amount, vat, cardId, issuer, last4, authCd, trxDay, regDay, regTime, regDate, stlAmount, stlRate, stlFee, stlFeeVat, stlType, stlDay, stlId, stlDistFee, stlDistRate, stlDistDay, stlDistId, stlAgencyFee, stlAgencyRate, stlAgencyDay, stlAgencyId, stlSalesFee, stlSalesRate, stlSalesDay, stlSalesId, benefit, taxId, name, distId, distName, agencyId, agencyName, salesId, salesName) "
					+ "SELECT capId, trxId, mchtId, tmnId, trackId, capType, rfdType, rootTrxId, amount, vat, cardId, issuer, last4, authCd, trxDay, regDay, regTime, regDate, stlAmount, stlRate, stlFee, stlFeeVat, stlType, stlDay, stlId, stlDistFee, stlDistRate, stlDistDay, stlDistId, stlAgencyFee, stlAgencyRate, stlAgencyDay, stlAgencyId, stlSalesFee, stlSalesRate, stlSalesDay, stlSalesId, benefit, taxId, name, distId, distName, agencyId, agencyName, salesId, salesName FROM VW_TRX_CAP "
					+ "WHERE capId IN (SELECT capId FROM PG_TRX_CAP WHERE mchtId = '"+ cpRequest.getKeyValue("mchtId") +"') "
					+ "AND stlDistId = '' "
					+ "AND stlAgencyId = '' "
					+ "And stlSalesId = '' ")){
				logger.debug("백업 데이터 생성 실패");
			}
		}
		*/
		if(cpDAO.updateAndBack("PG_MCHT", SessionUtil.getUserId(request), cpRequest.data)){
			/*
			if(trxCapCnt < 1) {
				return new CPRUtil(cpRequest)
		        		.resultOK("가맹점 소속이 변경되었습니다. <BR>거래가 아직 없는 가맹점입니다.")
		        		.cpResponse();
			}
			
			StringBuffer buf = new StringBuffer();
			buf.append(" UPDATE PG_TRX_CAP_DTL AS A LEFT JOIN ( ");
			buf.append(" SELECT capId, ");
			buf.append(" FLOOR(((B.amount * B.stlAgencyRate) - ((B.amount * B.stlAgencyRate) * B.stlSalesRate))*1.1) as agencyFee,  ");
			buf.append(" FLOOR(((B.amount * B.stlAgencyRate) * B.stlSalesRate)*1.1)  as salesFee, ");
			buf.append(" stlFee + stlFeeVat - (FLOOR(((B.amount * B.stlAgencyRate) - ((B.amount * B.stlAgencyRate) * B.stlSalesRate))*1.1)+FLOOR(((B.amount * B.stlAgencyRate) * B.stlSalesRate)*1.1)+stlDistFee) - stlVanFee -stlDiffDistFee - stlDiffAgencyFee - stlDiffSalesFee + if(stlDiffStatus = '결과대기',stlDiffAmt,stlDiffVanAmt) as newBenefit ");
			buf.append(" 	FROM VW_TRX_CAP B) B ON A.capId = B.capId ");
			buf.append(" SET A.stlAgencyFee = B.agencyFee, A.stlSalesFee = B.salesFee, A.benefit = B.newBenefit ");
			buf.append(" WHERE A.capId IN (SELECT capId FROM PG_TRX_CAP WHERE mchtId = '"+ cpRequest.getKeyValue("mchtId") +"') ");
			buf.append(" 	AND stlDistId = '' AND stlAgencyId = '' AND stlSalesId = ''; ");
			
			CPDAO dao = new CPDAO();
			if(dao.update(buf.toString())) {
				return new CPRUtil(cpRequest)
		        		.resultOK("가맹점 소속이 변경되었습니다. <BR>정산되지 않은 데이터는 새로운 소속으로 정산됩니다.")
		        		.cpResponse();
			} else {
				return new CPRUtil(cpRequest)
		        		.resultNOK("가맹점 소속이 변경되었습니다. <BR> 정산되지 않은 데이터는 기존 소속으로 정산됩니다.",cpDAO.getError())
		        		.cpResponse();
			}*/
			return new CPRUtil(cpRequest)
	        		.resultOK("가맹점 소속이 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("가맹점 소속 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
    
 // 관리정보 지불 및 정산 체킹
    @RequestMapping(value = {"/mcht/mng/check/{mchtId}"})
    public @ResponseBody SharedMap<String, Object> distMngCheck(HttpServletRequest request, @PathVariable String mchtId) {
    	SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	
    	AgencyMngDAO agencyDAO = new AgencyMngDAO();
    	DistDAO DistDAO = new DistDAO();

    	agencyDAO.setTable("PG_MAM_AGENCY_MNG");
		agencyDAO.setColumns("count(1) as cnt");
		agencyDAO.addWhere("agencyId", result.get("agencyId").toString(), DAO.eq);
		agencyDAO.addWhere("payType", "신용카드", DAO.eq);

		//에이전시 관리정보 체크		
		if(agencyDAO.search().getRowFirst().getInt("cnt") >= 1) {
			resultMap.put("agencyRes", "OK");
		} else {
			resultMap.put("agencyRes", "NOK");
			resultMap.put("agencyMsg", "에이전시");
		}
		
		//대행사 관리정보 체크
		DistDAO.setTable("PG_MAM_DIST_MNG");
		DistDAO.setColumns("count(1) as cnt");
		DistDAO.addWhere("distId", result.get("distId").toString(), DAO.eq);
		DistDAO.addWhere("payType", "신용카드", DAO.eq);
		
		if(DistDAO.search().getRowFirst().getInt("cnt") >= 1) {
			resultMap.put("distRes", "OK");
		} else {
			resultMap.put("distRes", "NOK");
			resultMap.put("distMsg", "대행사");
		}
		
		return resultMap;
    }
    
    // ======================================================= 관리정보 
    @RequestMapping(value = {"/mcht/mng/add/{mchtId}"})
    public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String mchtId) {
    	
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	SharedMap<String,Object> agencyMngMap = new AgencyMngDAO().getById(result.getString("agencyId")).getRowFirst();
    	request.setAttribute("DATADISTMNGMAP", agencyMngMap);
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
    	request.setAttribute("DISTRATE_OPTION", new DistDAO().getRateOption(result.getString("distId")));
    	request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
    	request.setAttribute("FEE_TEMPLATE", new MchtFeeTemplateDAO().getFeeTemplete().getRows());
    	request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
//    	request.setAttribute("DISTMNGTYPE", new DistMngDAO().getDistMngByPayType(result.getString("distId"), "신용카드").getRows());
//    	request.setAttribute("AGENCYMNGTYPE", new AgencyMngDAO().getAgencyMngByPayType(result.getString("agencyId"), "신용카드").getRows());
//    	request.setAttribute("SALESMNGTYPE", new MemberSalesMngDAO().getSalesMngByPayType(result.getString("salesId"), "신용카드").getRows());
    	String diffType = new MchtDiffDAO().getDiffType(mchtId).getRowFirst().getString("mchtType");
    	if(CommonUtil.isNullOrSpace(diffType)) diffType = "일반";
    	request.setAttribute("DIFFTYPE", diffType);
    	if(result.isEquals("distId", "00")) {
    		return new ModelAndView("/mcht/mng/fact/add", "DATAMAP", result);
    	} else {
    		return new ModelAndView("/mcht/mng/add", "DATAMAP", result);
    	}
    }
    
    @RequestMapping(value = "/mcht/mng/modify/{mchtId}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String mchtId) {
    	SharedMap<String, Object> sharedMap = new MchtMngDAO().getById(mchtId).getRowFirst();
    	sharedMap.put("limitOnce", CommonUtil.moneyFormat(sharedMap.getString("limitOnce")));
    	sharedMap.put("limitDay", CommonUtil.moneyFormat(sharedMap.getString("limitDay")));
    	sharedMap.put("limitMonth", CommonUtil.moneyFormat(sharedMap.getString("limitMonth")));
    	sharedMap.put("limitYear", CommonUtil.moneyFormat(sharedMap.getString("limitYear")));
    	sharedMap.put("largeAmount", CommonUtil.moneyFormat(sharedMap.getString("largeAmount")));
    	sharedMap.put("payOutFee", CommonUtil.moneyFormat(sharedMap.getString("payOutFee")));
    	sharedMap.put("distPayInFee", CommonUtil.moneyFormat(sharedMap.getString("distPayInFee")));
    	sharedMap.put("agencyPayInFee", CommonUtil.moneyFormat(sharedMap.getString("agencyPayInFee")));
    	sharedMap.put("salesPayInFee", CommonUtil.moneyFormat(sharedMap.getString("salesPayInFee")));
    	
//    	sharedMap.put("salesRate", String.format("%.3f",sharedMap.getDouble("rate")));
    	sharedMap.put("rate", sharedMap.getDouble("rate"));
    	sharedMap.put("salesRate", sharedMap.getDouble("salesRate"));
    	sharedMap.put("agencyRate", sharedMap.getDouble("agencyRate"));
    	sharedMap.put("distRate", sharedMap.getDouble("distRate"));
    	sharedMap.put("diff0DistRate", sharedMap.getDouble("diff0DistRate"));
    	sharedMap.put("diff0CheckDistRate", sharedMap.getDouble("diff0CheckDistRate"));
    	sharedMap.put("diff1DistRate", sharedMap.getDouble("diff1DistRate"));
    	sharedMap.put("diff1CheckDistRate", sharedMap.getDouble("diff1CheckDistRate"));
    	sharedMap.put("diff2DistRate", sharedMap.getDouble("diff2DistRate"));
    	sharedMap.put("diff2CheckDistRate", sharedMap.getDouble("diff2CheckDistRate"));
    	sharedMap.put("diff3DistRate", sharedMap.getDouble("diff3DistRate"));
    	sharedMap.put("diff3CheckDistRate", sharedMap.getDouble("diff3CheckDistRate"));
    	sharedMap.put("diff0AgencyRate", sharedMap.getDouble("diff0AgencyRate"));
    	sharedMap.put("diff0CheckAgencyRate", sharedMap.getDouble("diff0CheckAgencyRate"));
    	sharedMap.put("diff1AgencyRate", sharedMap.getDouble("diff1AgencyRate"));
    	sharedMap.put("diff1CheckAgencyRate", sharedMap.getDouble("diff1CheckAgencyRate"));
    	sharedMap.put("diff2AgencyRate", sharedMap.getDouble("diff2AgencyRate"));
    	sharedMap.put("diff2CheckAgencyRate", sharedMap.getDouble("diff2CheckAgencyRate"));
    	sharedMap.put("diff3AgencyRate", sharedMap.getDouble("diff3AgencyRate"));
    	sharedMap.put("diff3CheckAgencyRate", sharedMap.getDouble("diff3CheckAgencyRate"));
    	sharedMap.put("diff0SalesRate", sharedMap.getDouble("diff0SalesRate"));
    	sharedMap.put("diff0CheckSalesRate", sharedMap.getDouble("diff0CheckSalesRate"));
    	sharedMap.put("diff1SalesRate", sharedMap.getDouble("diff1SalesRate"));
    	sharedMap.put("diff1CheckSalesRate", sharedMap.getDouble("diff1CheckSalesRate"));
    	sharedMap.put("diff2SalesRate", sharedMap.getDouble("diff2SalesRate"));
    	sharedMap.put("diff2CheckSalesRate", sharedMap.getDouble("diff2CheckSalesRate"));
    	sharedMap.put("diff3SalesRate", sharedMap.getDouble("diff3SalesRate"));
    	sharedMap.put("diff3CheckSalesRate", sharedMap.getDouble("diff3CheckSalesRate"));
    	
    	request.setAttribute("DATAMAP", sharedMap);
//    	request.setAttribute("DATADISTMNGMAP", new AgencyMngDAO().getById(sharedMap.getString("agencyId")).getRowFirst());
//    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
//    	request.setAttribute("DISTRATE_OPTION", new DistDAO().getRateOption(sharedMap.getString("distId")));
    	request.setAttribute("FEE_TEMPLATE", new MchtFeeTemplateDAO().getFeeTemplete().getRows());
    	request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
//    	request.setAttribute("DISTMNGTYPE", new DistMngDAO().getDistMngByPayType(new MchtDAO().getById(mchtId).getRowFirst().getString("distId"), "신용카드").getRows());
//    	request.setAttribute("AGENCYMNGTYPE", new AgencyMngDAO().getAgencyMngByPayType(new MchtDAO().getById(mchtId).getRowFirst().getString("agencyId"), "신용카드").getRows());
//    	request.setAttribute("SALESMNGTYPE", new MemberSalesMngDAO().getSalesMngByPayType(new MchtDAO().getById(mchtId).getRowFirst().getString("salesId"), "신용카드").getRows());
    	if(sharedMap.isEquals("distId", "00")) {
    		return new ModelAndView("/mcht/mng/fact/modify");
    	} else {
    		return new ModelAndView("/mcht/mng/modify");
    	}
    	
    }
	
	@RequestMapping(value = {"/mcht/mng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		String mchtId = cpRequest.getValue("mchtId");

		String bankName = (String)cpRequest.getData("bankName").val;
		String account = (String)cpRequest.getData("account").val;
		String accntHolder = (String)cpRequest.getData("accntHolder").val;
		String bankCd = (String)cpRequest.getData("bankCd").val;
		String email = (String)cpRequest.getData("email").val;
		
		cpRequest.deleteData("bankName");
		cpRequest.deleteData("account");
		cpRequest.deleteData("accntHolder");
		cpRequest.deleteData("bankCd");
		cpRequest.deleteData("email");
		
		if(cpDAO.insertByOper("PG_MCHT_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			String taxId = GenKey.genKeys(CPKEY.MCHT_TAX, mchtId);
			String tmnId = new MchtTmnDAO().getNewId();
			String pk = GenKey.genKeys(CPKEY.PUBLIC_KEY, mchtId);
			String serial = tmnId.replace("TMN", "");
			if(new MchtTmnDAO().insertDefault(tmnId, pk, mchtId, taxId, serial)) {
				if(new MchtTaxDAO().insertDefault(mchtId, taxId, email, bankName, bankCd, account, accntHolder)){
					return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect).cpResponse();
				}
			}
		}
		
		return new CPRUtil(cpRequest)
	        	.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        	.cpResponse();
    }
	
	@RequestMapping(value = {"/mcht/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.updateAndBackByOper("PG_MCHT_MNG", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("가맹점 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("가맹점 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	//======================================================== 노티 정보
	@RequestMapping(value = "/mcht/noti/list", method = RequestMethod.POST)
	public ModelAndView notiList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		RecordSet rset = mchtDAO.getWebHook(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/noti/list","");
	}
	
	@RequestMapping(value = {"/mcht/noti/add"})
	public ModelAndView notiAdd(HttpServletRequest request) {
		return new ModelAndView("/mcht/noti/add");
	}
	
	@RequestMapping(value = {"/mcht/noti/modify/{idx}"})
	public ModelAndView notiMod(HttpServletRequest request, @PathVariable String idx) {
		return new ModelAndView("/mcht/noti/modify", "DATAMAP", new MchtDAO().getWebHookByIdx(idx).getRowFirst());
	}
	
	@RequestMapping(value = "/mcht/noti/insert", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse notiInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.insert("PG_MCHT_WEBHOOK", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
		        	.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/noti/update", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse notiUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if (cpDAO.update("PG_MCHT_WEBHOOK", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 노티 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 노티 정보 변경에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/noti/idCheck", method = RequestMethod.POST)
	public @ResponseBody String notiCheck(HttpServletRequest request, @RequestParam("id") String id, @RequestParam("idType") String idType) {
		
		CPDAO dao = new CPDAO();
		
		logger.info("NOTI INSERT ID CHECK >>> idType : " + idType + ", id : " + id);
		
		if(idType == "distId" || "distId".equals(idType)) {
			dao.setTable("PG_MAM_DIST");
			dao.setColumns("distId");
			dao.addWhere("distId", id, DAO.eq);
			dao.setOrderBy("");
			
		}else if(idType == "agencyId" || "agencyId".equals(idType)) {
			dao.setTable("PG_MAM_AGENCY");
			dao.setColumns("agencyId");
			dao.addWhere("agencyId", id, DAO.eq);
			dao.setOrderBy("");
			
		}else if(idType == "salesId" || "salesId".equals(idType)) {
			dao.setTable("PG_MAM_SALES");
			dao.setColumns("salesId");
			dao.addWhere("salesId", id, DAO.eq);
			dao.setOrderBy("");
			
		}else if(idType == "mchtId" || "mchtId".equals(idType)) {
			dao.setTable("PG_MCHT");
			dao.setColumns("mchtId");
			dao.addWhere("mchtId", id, DAO.eq);
			dao.setOrderBy("");
			
		}else if(idType == "tmnId" || "tmnId".equals(idType)) {
			dao.setTable("PG_MCHT_TMN");
			dao.setColumns("tmnId");
			dao.addWhere("tmnId", id, DAO.eq);
			dao.setOrderBy("");
		}
		
		if(id.indexOf(" ") > -1){
			return GsonUtil.toJson("아이디에는 공백이 포함 될 수 없습니다.");
		}
		
		if(dao.search().size() == 0){
			return GsonUtil.toJson("해당 아이디가 존재하지 않습니다.");
		} else {
			return GsonUtil.toJson("true");
		}
	}
	//======================================================== 노티 정보

	//230112_PYS : 통합인증
	@RequestMapping(value = "/mcht/totalAuth/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView totalAuthAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		return new ModelAndView("/mcht/totalAuth/add");
	}

	@RequestMapping(value = {"/mcht/totalAuth/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse totalAuthInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.insertByOper("PG_MCHT_TOTAL_AUTH", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 통합인증 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest)
					.resultNOK("가맹점 통합인증 정보 등록에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}
	}

	@RequestMapping(value = "/mcht/totalAuth/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView totalAuthModify(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String, Object> sharedMap = new MchtTotalAuthDAO().getByMchtId(mchtId);
		request.setAttribute("DATAMAP", sharedMap);
		return new ModelAndView("/mcht/totalAuth/modify");
	}

	@RequestMapping(value = "/mcht/totalAuth/update", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse totalAuthUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if (cpDAO.updateAndBack("PG_MCHT_TOTAL_AUTH", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("통합인증 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("통합인증 정보 변경에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}
	}


	//======================================================== 가맹점 월 정산내역 이메일 전송
	/*
	 * 이메일 전송 내용 SELECT(가맹점 정산 내역)
	 */
	@SessionExclude
	@RequestMapping(value = {"/mcht/doc/settleSendEmail"})
	@ModelAttribute("DATAMAP")
	public List<SharedMap<String, Object>> mappingSettle(HttpServletRequest request, @RequestParam(value="mchtId") String mchtId, 
			@RequestParam(value="startDay") String startDay, @RequestParam(value="endDay") String endDay){
		request.setAttribute("DATAMCHT", new MchtDAO().getIdForSend(mchtId).getRowFirst());
		List<SharedMap<String, Object>> result = new MchtDAO().getListForSend(mchtId, startDay, endDay);
		
		logger.info("MAPPING SETTLE LIST id : [{}], startDay : [{}], endDay : [{}]", mchtId, startDay, endDay);
		
		return result;
	}
	
	/*
	 * 이메일 발송 수신거부 처리 페이지
	 */
	@SessionExclude
	@RequestMapping(value = "/emailStatus", method = RequestMethod.GET)
	public String emailConfirm(HttpServletRequest request, @RequestParam(value="mchtId") String mchtId) {
		request.setAttribute("mchtId", mchtId);
	 
		return "/mcht/doc/emailConfirm";
	}
	
	/*
	 * 이메일 발송 수신거부 처리
	 */
	@ResponseBody
	@RequestMapping(value = "/mcht/email/update", method = RequestMethod.POST)
	public void emailUpdate(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		logger.info("emailConfirm UPDATE mchtId : " + id);
		
		MchtDAO mchtDAO = new MchtDAO();
		mchtDAO.updateEmailConfirm(id);
	}
	//======================================================== 가맹점 월 정산내역 이메일 전송
	
	// ======================================================= 터미널 정보
	@RequestMapping(value = {"/mcht/tmn/add/{mchtId}"})
	public ModelAndView tmnAdd(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
		String tmnId = new MchtTmnDAO().getNewId();
		result.put("tmnId", tmnId);
		result.put("serial", tmnId.replace("TMN", ""));
		result.put("payKey", GenKey.genKeys(CPKEY.PUBLIC_KEY, mchtId) );
		
		request.setAttribute("DATATAXMAP", new MchtTaxDAO().getSelectOption(mchtId));
		request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
		return new ModelAndView("/mcht/tmn/add", "DATAMAP", result);
	}
    
    @RequestMapping(value = "/mcht/tmn/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView tmnList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtTmnDAO vanDAO = new MchtTmnDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/tmn/list","");
	}
    
    @RequestMapping(value = "/mcht/tmn/modify/{tmnId}", method = RequestMethod.GET)
    public ModelAndView tmnModify(HttpServletRequest request, @PathVariable String tmnId) {
    	SharedMap<String,Object> result = new MchtTmnDAO().getById(tmnId).getRowFirst();
    	
    	result.put("minAmount", CommonUtil.moneyFormat(result.getString("minAmount")));
    	result.put("limitAmount", CommonUtil.moneyFormat(result.getString("limitAmount")));
    	
    	request.setAttribute("DATATAXMAP", new MchtTaxDAO().getSelectOption(result.getString("mchtId")));
    	request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
    	if(result.getString("van").equals("DANAL")) {
    		request.setAttribute("VANIDMAP", new VanDAO().danalVanId().getRows());
    	} else {
    		request.setAttribute("VANIDMAP", new VanDAO().niceVanId().getRows());
    	}
    	
    	RecordSet rset = new TrxCapDAO().getByTmnId(tmnId);
    	if(rset.size() > 0) {
    		request.setAttribute("HasTransaction", "true");
    	} else {
    		request.setAttribute("HasTransaction", "false");
    	}
    	
        return new ModelAndView("/mcht/tmn/modify","DATAMAP",result);
    }
    
    @RequestMapping(value = {"/mcht/tmn/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse tmnInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.insertByOper("PG_MCHT_TMN", SessionUtil.getUserId(request), cpRequest.data)){
			new VanDAO().updateUsed(cpRequest.getValue("vanIdx"));
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
	
	@RequestMapping(value = {"/mcht/tmn/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse tmnUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpRequest.getValue("status").equals("폐기")) {
			CPDAO dao = new CPDAO();
			dao.setTable("VW_TRX_CAP");
			dao.setColumns("COUNT(*) AS CNT");
			dao.addWhere("tmnId", cpRequest.getKeyValue("tmnId"),CPDAO.eq);
			SharedMap<String,Object> resMap = dao.search().getRow(0);
			if(resMap.getLong("CNT") <= 0) {
				if(cpDAO.update("DELETE FROM PG_MCHT_TMN WHERE tmnId = '" +cpRequest.getKeyValue("tmnId")+"'")) {
					new DAO().query("INSERT INTO HT_TMNID_CLOG SET tmnId='deleted' ,orgTmnId='" +cpRequest.getKeyValue("tmnId")+ "' "
							+ ",regId='"+ SessionUtil.getUserId(request) +"' ,regDay='"+CommonUtil.getCurrentDate("yyyyMMdd")+"'");
					return new CPRUtil(cpRequest)
			        		.resultOK("터미널이 폐기되었습니다.")
			        		.cpResponse();
				} else {
					return new CPRUtil(cpRequest)
			        		.resultNOK("터미널 폐기에 실패하였습니다.",cpDAO.getError())
			        		.cpResponse();
				}
			}
		} else {
			//터미널 아이디가 변경되고 && 변경된 아이디가 이미 있는지 검사
			String tmnIdKey = (String)cpRequest.getKeyData("tmnId").val;
			
			String tmnId = "";
			
			if(cpRequest.getData("tmnId") != null) {
				tmnId = (String)cpRequest.getData("tmnId").val;
			}
			
			if(!tmnId.isEmpty() && !tmnIdKey.equals(tmnId)) {
				RecordSet rest = new MchtTmnDAO().getById(tmnId);
				if(rest.size() > 0) {
					return new CPRUtil(cpRequest)
					        		.resultNOK("터미널 아이디가 이미 있습니다.",cpDAO.getError())
					        		.cpResponse();
				}
				
				RecordSet rest2 = new UserDAO().getById(tmnId);
				if(rest2.size() > 0) {
					return new CPRUtil(cpRequest)
					        		.resultNOK("사용불가능한 터미널 아이디입니다.",cpDAO.getError())
					        		.cpResponse();
				}
			}
		}
		
		
		
		if(cpDAO.updateAndBackByOper("PG_MCHT_TMN", SessionUtil.getUserId(request), cpRequest.data)){
			if(!cpRequest.getValue("tmnId").equals(cpRequest.getKeyValue("tmnId"))) {
				new DAO().query("INSERT INTO HT_TMNID_CLOG SET tmnId='"+cpRequest.getValue("tmnId")+"' ,orgTmnId='" +cpRequest.getKeyValue("tmnId")+ "' "
						+ ",regId='"+ SessionUtil.getUserId(request) +"' ,regDay='"+CommonUtil.getCurrentDate("yyyyMMdd")+"'");
			}
			
			new VanDAO().updateUsed(cpRequest.getValue("vanIdx"));
			return new CPRUtil(cpRequest)
	        		.resultOK("터미널 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("터미널 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/mcht/tmn/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String tmnIdCheck(HttpServletRequest request, @RequestParam("tmnId") String tmnId) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_MCHT_TMN");
		dao.setColumns("tmnId");
		dao.addWhere("tmnId", tmnId, DAO.eq);
		
		if(tmnId.indexOf(" ") > -1){
			return GsonUtil.toJson("터미널 아이디에는 공백이 포함 될 수 없습니다.");
		}
		if(dao.search().size() == 0){
			dao.initRecord();
			dao.setTable("PG_USER");
			dao.setColumns("id");
			dao.addWhere("id", tmnId, DAO.eq);
			if(dao.search().size() == 0) {
				return GsonUtil.toJson("true");
			}else {
				return GsonUtil.toJson("사용 불가능한 터미널 아이디입니다.");
			}
			
		}else{
			return GsonUtil.toJson("터미널 아이디가 이미 사용중입니다.");
		}
    }
	
	// ====================================================== 단말기 추가정보
	
	@RequestMapping(value = {"/mcht/tmnDtl/add/{tmnId}"})
	public ModelAndView tmnDtlAdd(HttpServletRequest request, @PathVariable String tmnId) {
		SharedMap<String,Object> result = new MchtTmnDAO().getById(tmnId).getRowFirst();
		request.setAttribute("MCHTMAP", new MchtDAO().getById(result.getString("mchtId")).getRowFirst());
		request.setAttribute("TAXMAP", new MchtTaxDAO().getById(result.getString("taxId")).getRowFirst());
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/mcht/tmnDtl/add", "DATAMAP", result);
	}
	
	@RequestMapping(value = {"/mcht/tmnDtl/modify/{tmnId}"})
	public ModelAndView tmnDtlModify(HttpServletRequest request, @PathVariable String tmnId) {
		SharedMap<String,Object> result = new MchtTmnDAO().getById(tmnId).getRowFirst();
		
		result.put("dtlRate", String.format("%.3f",result.getDouble("dtlRate")*100));
		
		request.setAttribute("MCHTMAP", new MchtDAO().getById(result.getString("mchtId")).getRowFirst());
		request.setAttribute("TAXMAP", new MchtTaxDAO().getById(result.getString("taxId")).getRowFirst());
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		request.setAttribute("DATAMAP", result);
		return new ModelAndView("/mcht/tmnDtl/modify");
	}
	
	@RequestMapping(value = {"/mcht/tmnDtl/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse tmnDtlInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insertByOper("PG_MCHT_TMN_DTL", SessionUtil.getUserId(request), cpRequest.data)){
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
	
	@RequestMapping(value = {"/mcht/tmnDtl/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse tmnDtlUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.updateAndBackByOper("PG_MCHT_TMN_DTL", SessionUtil.getUserId(request), cpRequest.data)){
			
			logger.debug("tmnId [{}]",cpRequest.getKeyValue("tmnId"));
			logger.debug("bankCd [{}]",cpRequest.getValue("bankCd"));
			logger.debug("account [{}]",cpRequest.getValue("account"));
			
			WalletDAO walletDAO = new WalletDAO();
			
			// WL_USER 테이블이 존재하지 않으므로 에러발생 - 주석처리
			// 분리정산 터미널 여부 확인
			/*SharedMap<String, Object> walletMap = walletDAO.getWalletByTmnId(cpRequest.getKeyValue("tmnId"));
			if(walletMap != null) {
				// 월렛 기본정보 변경처리
				String identity = cpRequest.getValue("identity");
				if(CommonUtil.replace(identity, "-", "").trim().length() == 13) {
					identity = identity.substring(0,6)+":"+Base64.encodeToString(SeedKisa.encrypt(identity, ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16)));
				}else {
					identity = identity.replaceAll("/[^0-9]","").trim();
				}
				walletMap.put("identity", identity);
				walletMap.put("compName", cpRequest.getValue("name"));
				walletMap.put("ceo", cpRequest.getValue("ceoName"));
				if(!CommonUtil.isNullOrSpace(cpRequest.getValue("tel"))) {
					walletMap.put("tel", cpRequest.getValue("tel").replaceAll("/[^0-9]","").trim());
				}else {
					walletMap.put("tel", "");
				}
				if(!CommonUtil.isNullOrSpace(cpRequest.getValue("ceoPhone"))) {
					walletMap.put("phone", cpRequest.getValue("ceoPhone").replaceAll("/[^0-9]","").trim());
				}else {
					walletMap.put("phone", "");
				}
				walletMap.put("zip", cpRequest.getValue("zip"));
				walletMap.put("addr1", cpRequest.getValue("addr1"));
				walletMap.put("addr2", cpRequest.getValue("addr2"));
				walletMap.put("email", cpRequest.getValue("email"));
				logger.debug(walletMap.toString());
				walletDAO.updateShopWallet(walletMap);
				
				// 계좌번호 변경시 처리
				if(!cpRequest.getValue("bankCd").equals(walletMap.getString("bankCd")) || !cpRequest.getValue("account").replaceAll("/[^0-9]","").trim().equals(walletMap.getString("account"))) {
					SharedMap<String, Object> accntMap = walletDAO.getWalletAccnt(walletMap.getString("accntId"), walletMap.getString("walletId"));
					String method = "";
					if(accntMap.getString("status").equals("사용")) {
						method = "PUT";
					}else {
						method = "POST";
					}
					new WalletAccntUtil().excute(cpRequest.getValue("bankCd"), cpRequest.getValue("account"), walletMap.getString("walletId"), walletMap.getString("apiKey"), method);
				}
			}*/
			
			return new CPRUtil(cpRequest)
							.resultOK("가맹점 정보가 변경되었습니다.")
							.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
							.resultNOK("가맹점 정보 변경에 실패하였습니다.",cpDAO.getError())
							.cpResponse();
		}
	}
    
    // ======================================================= Tax 정보
	
	@RequestMapping(value = {"/mcht/tax/modal/{taxId}"})
	public ModelAndView taxModal(HttpServletRequest request, @PathVariable String taxId) {
		return new ModelAndView("/mcht/tax/modal", "DATAMAP", new MchtTaxDAO().getById(taxId).getRowFirst());
    }
	
    @RequestMapping(value = {"/mcht/tax/add/{mchtId}"})
    public ModelAndView taxAdd(HttpServletRequest request, @PathVariable String mchtId) {
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	result.put("taxId", GenKey.genKeys(CPKEY.MCHT_TAX, mchtId) );
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
    	return new ModelAndView("/mcht/tax/add", "DATAMAP", result);
    }
    
    @RequestMapping(value = "/mcht/tax/modify/{taxId}", method = RequestMethod.GET)
    public ModelAndView taxModify(HttpServletRequest request, @PathVariable String taxId) {
    	SharedMap<String,Object> sharedMap = new MchtTaxDAO().getById(taxId).getRowFirst();
    	sharedMap.put("taxLimit", CommonUtil.moneyFormat(sharedMap.getString("taxLimit")));
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
    	request.setAttribute("DATAMAP", sharedMap);
        return new ModelAndView("/mcht/tax/modify");
    }
    
    @RequestMapping(value = {"/mcht/tax/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse taxInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		if(cpDAO.insertByOper("PG_MCHT_TAX", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
	}
	
	@RequestMapping(value = {"/mcht/tax/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse taxUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
//		MchtDAO mchtDao = new MchtDAO();
//		
//		String taxId = cpRequest.getKeyValue("taxId");
//		String account = cpRequest.getValue("account");
//		String bankCd = cpRequest.getValue("bankCd");
//		String accntHolder = cpRequest.getValue("accntHolder");
//		
//		logger.info("taxUpdate : [{}][{}][{}][{}]", taxId, account, bankCd, accntHolder);
//
//		SharedMap<String, Object> taxData = mchtDao.getTaxData(taxId).getRowFirst();
//		
//		if(!taxData.getString("account").equals(account) || 
//		   !taxData.getString("bankCd").equals(bankCd) ||
//		   !taxData.getString("accntHolder").equals(accntHolder)) {
//			logger.info("taxUpdate : [{}][{}][{}][{}]", taxId, taxData.getString("account"), taxData.getString("bankCd"), taxData.getString("accntHolder"));
//			
//			return new CPRUtil(cpRequest)
//	        		.resultNOK("은행정보는 변경할 수 없습니다. 관리자에게 문의해 주세요.",cpDAO.getError())
//	        		.cpResponse();
//		}
		
		
		
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		if(cpDAO.updateAndBackByOper("PG_MCHT_TAX", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("가맹점 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("가맹점 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
	}
	
	@RequestMapping(value = {"/mcht/tax/nameCheck"}, method = RequestMethod.POST)
	public @ResponseBody String taxNameCheck(HttpServletRequest request, @RequestParam("name") String name) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_MCHT_TAX");
		dao.setColumns("name");
		dao.addWhere("name", name, DAO.eq);
		
		if(name.indexOf(" ") > -1){
			return GsonUtil.toJson("사용자 이름에는 공백이 포함 될 수 없습니다.");
		}
		if(dao.search().size() == 0){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("사용자 이름이 이미 사용중입니다.");
		}
	}
	
	@RequestMapping(value = {"/mcht/tax/alreadActiveCheck"}, method = RequestMethod.POST)
    public @ResponseBody String alreadActiveCheck(HttpServletRequest request, @RequestParam("mchtId") String mchtId, @RequestParam("taxId") String taxId, @RequestParam("taxStatus") String taxStatus) {
		if(!taxStatus.equals("사용")) {
			return GsonUtil.toJson("true");
		}
		
		CPDAO dao = new CPDAO();
		dao.setTable("PG_MCHT_TAX");
		dao.setColumns("taxStatus");
		dao.addWhere("mchtId", mchtId, DAO.eq);
		dao.addWhere("taxId", taxId, DAO.ne);
		dao.addWhere("taxStatus", "사용", DAO.eq);
		
		if(dao.search().size() == 0){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("사용 상태의 Tax는 하나만 지정할 수 있습니다.");
		}
    }
	
	// ==================================================== 미실적 가맹점 조회
	@RequestMapping(value = {"/mcht/nontran/form"})
    public ModelAndView nonform(HttpServletRequest request) {
        return new ModelAndView("/mcht/nontran/form");
    }

	@RequestMapping(value = "/mcht/nontran/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView nonlist(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		MchtDAO mchtDAO = new MchtDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		//identity는 암호화되어 있으므로 복호화해서 비교해야 함.
		cpRequest.replaceKeyValue("identity",mchtDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity",mchtDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		cpRequest.replaceKeyName("distId", "A.distId");
		cpRequest.replaceKeyName("agencyId", "A.agencyId");
		cpRequest.replaceKeyName("salesId", "A.salesId");
		
		RecordSet rset = mchtDAO.nonTranList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,mchtDAO).setView(request,"/mcht/nontran/list","");
	}
	
	// ======================================================
	@RequestMapping(value= {"/mcht/loan/add/{mchtId}"})
	public ModelAndView mchtLoanAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("mchtId", mchtId);
		request.setAttribute("DATALIST", new LoanDAO().search().getRows());
		return new ModelAndView("/mcht/selectLoan");
	}
	
	// ======================================================= VAN 정보
	@RequestMapping(value = "/mcht/van/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		VanDAO vanDAO = new VanDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/van/list","");
	}
	
    @RequestMapping(value = "/mcht/van/modify/{vanid}", method = RequestMethod.GET)
    public ModelAndView vanModify(HttpServletRequest request, @PathVariable String vanid) {
    	SharedMap<String,Object> result = new VanDAO().getByVanId(vanid).getRowFirst();
        return new ModelAndView("/mcht/van/modify","DATAMAP",result);
    }
    
    @RequestMapping(value = {"/mcht/van/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse vanInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insert("PG_VAN", SessionUtil.getUserId(request), cpRequest.data)){
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
	
	@RequestMapping(value = {"/mcht/van/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse vanUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
        
		if(cpRequest.getValue("settleType").equals("사용안함")) {
			cpRequest.replaceValue("settleType","");
		}
        if(cpDAO.updateAndBack("PG_VAN", SessionUtil.getUserId(request), cpRequest.data)) {
            return new CPRUtil(cpRequest)
                    .resultOK("VAN 정보가 변경되었습니다.")
                    .cpResponse();
        }else{
            return new CPRUtil(cpRequest)
                    .resultNOK("VAN 정보 변경에 실패하였습니다.",cpDAO.getError())
                    .cpResponse();
        }
    }
	
	@RequestMapping(value = {"/mcht/van/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String vanIdCheck(HttpServletRequest request, @RequestParam("vanid") String vanid) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_VAN");
		dao.setColumns("vanid");
		dao.addWhere("vanid", vanid, DAO.eq);
		
		if(vanid.indexOf(" ") > -1){
			return GsonUtil.toJson("아이디에는 공백이 포함 될 수 없습니다.");
		}
		if(dao.search().size() == 0){
			return GsonUtil.toJson("true");
		}else{
			return GsonUtil.toJson("아이디가 이미 사용중입니다.");
		}
	}
		
	@RequestMapping(value = "/mcht/van/fee/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanFeeList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		OrgFeeDAO vanDAO = new OrgFeeDAO();
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/van/fee/list","");
	}
	
	@RequestMapping(value= {"/mcht/van/select/{van}"}, method = RequestMethod.GET)
	public @ResponseBody String vanIdSelect(HttpServletRequest request, @PathVariable String van) {
		VanDAO vanDAO = new VanDAO();
		List<SharedMap<String, Object>> resMap = new ArrayList<SharedMap<String, Object>>();
		
		RecordSet rset = vanDAO.getVan(van);
		if(rset.size() > 0){
			resMap = rset.getRows();
		}
		
		
		return GsonUtil.toJson(resMap);
	}
	
	@RequestMapping(value = {"/mcht/doc/upload/{mchtId}"})
    public ModelAndView upload(HttpServletRequest request, @PathVariable String mchtId) {
        return new ModelAndView("/mcht/doc/upload","DATAMAP",new MchtDAO().getById(mchtId).getRowFirst());
    }
	
	@RequestMapping(value= {"/mcht/doc/status"}, method = RequestMethod.POST)
	public @ResponseBody String changeFileStatus(HttpServletRequest request, @RequestParam("idx") String idx, @RequestParam("status") String status ) {
		if(new DAO().update("UPDATE PG_FILE SET status='"+status+"' WHERE idx='"+idx+"'")) {
			return "OK";
		} else {
			return "NOK||파일 상태변경에 실패했습니다.";
		}
	}
	
	@RequestMapping(value= {"/mcht/doc/status/all"}, method = RequestMethod.POST)
	public @ResponseBody String changeAllFileStatus(HttpServletRequest request, @RequestParam("mchtId") String mchtId, @RequestParam("status") String status ) {
		if(CommonUtil.isNullOrSpace(mchtId)) {
			return "NOK||잘못된 요청입니다.";
		}
		
		if(new DAO().update("UPDATE PG_FILE SET status='"+status+"' WHERE grade='가맹점' AND memberId='"+ mchtId +"'")) {
			return "OK";
		} else {
			return "NOK||파일 상태변경에 실패했습니다.";
		}
	}
	
	
	 @RequestMapping(value = "/mcht/svc/modify/{mchtId}", method = RequestMethod.GET)
	    public ModelAndView svcModify(HttpServletRequest request, @PathVariable String mchtId) {
		    request.setAttribute("MCHT", new MchtDAO().getById(mchtId).getRowFirst());
	        return new ModelAndView("/mcht/svc/modify","DATAMAP",new MchtSvcDAO().getByMchtId(mchtId));
	}

	 @RequestMapping(value = {"/mcht/svc/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody CPResponse svcUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
			CPDAO cpDAO = new CPDAO();
			MchtDAO mchtDAO = new MchtDAO();

			//identity 입력시 암호화하여 넣어야함.
			if(cpDAO.update("PG_MCHT_SVC", SessionUtil.getUserId(request), cpRequest.data)){
				String checkSimple = cpRequest.getValue("simpleBill");
				String mchtId = cpRequest.getValue("mchtId");

				//간편결제 사용시 타 결제 수단 미사용
				if(checkSimple.equals("미사용")) {
					mchtDAO.updateSimpleStatus(mchtId);
				}

				return new CPRUtil(cpRequest)
		        		.resultOK("가맹점 서비스 정보가 변경되었습니다.")
		        		.cpResponse();
			}else{
				return new CPRUtil(cpRequest)
		        		.resultNOK("가맹점 서비스 정보 변경에 실패하였습니다.",cpDAO.getError())
		        		.cpResponse();
			}
	}
	
	@RequestMapping(value = "/mcht/vact/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView vactAdd(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
		request.setAttribute("DISTMNGTYPE", new DistMngDAO().getDistMngByPayType(result.getString("distId"), "가상계좌").getRows());
    	request.setAttribute("AGENCYMNGTYPE", new AgencyMngDAO().getAgencyMngByPayType(result.getString("agencyId"), "가상계좌").getRows());
		//230620 지사 테이블에 trxType이 없어 에러생김 -> 가상계좌 값을 가지는 값이 없어 object로 변경
//    	request.setAttribute("SALESMNGTYPE", new MemberSalesMngDAO().getSalesMngByPayType(result.getString("salesId"), "가상계좌").getRows());
    	request.setAttribute("SALESMNGTYPE", new ArrayList());
		return new ModelAndView("/mcht/vact/add", "DATAMAP", new MchtVactDAO().getByMchtId(mchtId));
	}

	@RequestMapping(value = {"/mcht/vact/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse vactInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.insertByOper("PG_MCHT_MNG_VACT", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 가상계좌 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest)
		        		.resultNOK("가맹점 가상계좌 정보 변경에 실패하였습니다.",cpDAO.getError())
		        		.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/vact/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView vactModify(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> sharedMap = new MchtVactDAO().getByMchtId(mchtId);
		sharedMap.put("limitOnce", sharedMap.getString("limitOnce"));
		sharedMap.put("limitDay", sharedMap.getString("limitDay"));
		sharedMap.put("limitAmount", sharedMap.getString("limitAmount"));
		sharedMap.put("fee", CommonUtil.moneyFormat(sharedMap.getString("fee")));
		sharedMap.put("distFee", CommonUtil.moneyFormat(sharedMap.getString("distFee")));
		sharedMap.put("agencyFee", CommonUtil.moneyFormat(sharedMap.getString("agencyFee")));
		sharedMap.put("salesFee", CommonUtil.moneyFormat(sharedMap.getString("salesFee")));
		sharedMap.put("payOutFee", CommonUtil.moneyFormat(sharedMap.getString("payOutFee")));
		sharedMap.put("distPayInFee", CommonUtil.moneyFormat(sharedMap.getString("distPayInFee")));
		sharedMap.put("agencyPayInFee", CommonUtil.moneyFormat(sharedMap.getString("agencyPayInFee")));
		sharedMap.put("salesPayInFee", CommonUtil.moneyFormat(sharedMap.getString("salesPayInFee")));
		sharedMap.put("ownerAuthFee", CommonUtil.moneyFormat(sharedMap.getString("ownerAuthFee")));
		sharedMap.put("accountAuthFee", CommonUtil.moneyFormat(sharedMap.getString("accountAuthFee")));
		sharedMap.put("arsAuthFee", CommonUtil.moneyFormat(sharedMap.getString("arsAuthFee")));
		sharedMap.put("totalAuthFee", CommonUtil.moneyFormat(sharedMap.getString("totalAuthFee")));
    	
    	sharedMap.put("rate", String.format("%.5f",sharedMap.getDouble("rate")));
    	sharedMap.put("distRate", String.format("%.5f",sharedMap.getDouble("distRate")));
    	sharedMap.put("agencyRate", String.format("%.5f",sharedMap.getDouble("agencyRate")));
    	sharedMap.put("salesRate", String.format("%.5f",sharedMap.getDouble("salesRate")));
    	
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
		request.setAttribute("DISTMNGTYPE", new DistMngDAO().getDistMngByPayType(result.getString("distId"), "가상계좌").getRows());
    	request.setAttribute("AGENCYMNGTYPE", new AgencyMngDAO().getAgencyMngByPayType(result.getString("agencyId"), "가상계좌").getRows());
		//230620 지사 테이블에 trxType이 없어 에러생김 -> 가상계좌 값을 가지는 값이 없어 object로 변경
//    	request.setAttribute("SALESMNGTYPE", new MemberSalesMngDAO().getSalesMngByPayType(result.getString("salesId"), "가상계좌").getRows());
    	request.setAttribute("SALESMNGTYPE", new ArrayList());
    	request.setAttribute("DATAMAP", sharedMap);
	  return new ModelAndView("/mcht/vact/modify");
	}
	 
	@RequestMapping(value = {"/mcht/vact/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse vactUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.updateAndBack("PG_MCHT_MNG_VACT", SessionUtil.getUserId(request), cpRequest.data)) {
			
			DAO dao = new DAO();
			dao.setTable("PG_VACT_AUTH_INFO");
			dao.setRecord("respiteCnt", cpRequest.getData("respiteCnt").val);
			dao.addWhere("mchtId", cpRequest.getKeyValue("mchtId"), DAO.eq);

			dao.update();

			//PYS : 입금제한횟수 업데이트 하기
			dao = new DAO();
			dao.setTable("PG_VACT_DTL");
			dao.setRecord("depositLimitCnt", cpRequest.getData("depositLimitCnt").val);
			dao.addWhere("mchtId", cpRequest.getKeyValue("mchtId"), DAO.eq);
			dao.update();

//			dao = new DAO();
//			dao.setTable("PG_VACT_AUTH_STATEINIT_INFO");
//			dao.setRecord("stateInitCnt", cpRequest.getData("stateInitCnt").val);
//			dao.addWhere("mchtId", cpRequest.getKeyValue("mchtId"), DAO.eq);
//
//			dao.update();
			
			return new CPRUtil(cpRequest).resultOK("가맹점 가상계좌 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 가상계좌 정보 변경에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/vact/issue/{mchtId}", method = RequestMethod.GET)
	public ModelAndView vactIssue(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		List<SharedMap<String, Object>> vartDtlList = new VactDtlDAO().getByMchtId(mchtId);
		request.setAttribute("ISSUED_ACCNT", vartDtlList.size());

		DAO dao = new DAO();
		dao.setTable("VW_VACT_UNUSED");
		dao.setColumns("issuerBank, bankCd, COUNT(*) as cnt");
		dao.setGroupBy("bankCd");
		dao.setOrderBy("bankCd");
		String vactBankCd = new MchtVactDAO().getByMchtId(mchtId).getString("vactBankCd");
		dao.setWhere(" bankCd = '"+vactBankCd+"'");

		request.setAttribute("UNUSED_ACCNT_MAP", dao.search().getRows());

		return new ModelAndView("/mcht/vact/issue", "DATAMAP", vartDtlList);
	}
	
	@RequestMapping(value = "/mcht/vact/issue/form/{mchtId}", method = RequestMethod.GET)
	public ModelAndView vactIssueForm(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("mchtId", mchtId);
		return new ModelAndView("/mcht/vact/issue/form");
	}
	
	@RequestMapping(value = "/mcht/vact/issue/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vactIssueList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(cpRequest.getKeyValue("mchtId")).getRowFirst());
		VactDtlDAO vactDtlDAO = new VactDtlDAO();
		RecordSet rset = vactDtlDAO.list(cpRequest.data,cpRequest.page);
		CPDAO dao = new CPDAO();
		dao.setTable("VW_VACT_UNUSED");
		dao.setColumns("issuerBank, bankCd, COUNT(*) as cnt");
		dao.setGroupBy("bankCd");
		dao.setOrderBy("bankCd");
		String vactBankCd = new MchtVactDAO().getByMchtId(cpRequest.getKeyValue("mchtId")).getString("vactBankCd");
		dao.setWhere(" bankCd = '"+vactBankCd+"'");
		request.setAttribute("UNUSED_ACCNT_MAP", dao.search().getRows());
		//return new ModelAndView("/mcht/vact/issue/list", "DATAMAP", vartDtlList);

		// 계좌상태 노티실패건
		VactTrxDAO vactTrxDAO = new VactTrxDAO();
		int notiFailCount = vactTrxDAO.countVactStatusNotiFail(cpRequest.getKeyValue("mchtId"));
		request.setAttribute("VACT_STATUS_NOTI_FAIL_CNT", notiFailCount);

		return new CPRUtil(cpRequest).dataList(rset,vactDtlDAO).setView(request,"/mcht/vact/issue/list","");
	}
	
	
	
	
	@RequestMapping(value = {"/mcht/vact/row/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> vactRowUpdate(HttpServletRequest request, @RequestBody SharedMap<String, String> requestMap) {
		SharedMap<String, Object> resMap = new SharedMap<>();
		resMap.put("result", "NOK");
		String issueId = requestMap.getString("issueId");
		DAO dao = new DAO();
		dao.setTable("PG_VACT_DTL");
		dao.setRecord("trackId", requestMap.getString("trackId"));
		dao.setRecord("status", requestMap.getString("status"));
		dao.setRecord("udf1", requestMap.getString("udf1"));
		dao.setRecord("udf2", requestMap.getString("udf2"));
		dao.addWhere("issueId", issueId, DAO.eq);

		if (dao.update()) {
			resMap.put("result", "OK");
		} else {
			logger.debug("UPDATE PG_VACT_DTL FAIL: {}", issueId);
			resMap.put("msg", "정보 업데이트에 실패했습니다. 관리자에게 문의해주세요.");
		}
		return resMap;
	}

	@RequestMapping(value = "/mcht/vact/exissue/{mchtId}/{bankCd}/{holderName}/{cnt}", method = RequestMethod.GET)
	public @ResponseBody SharedMap<String, Object> vactExIssue(HttpServletRequest request, @PathVariable String mchtId,
		@PathVariable String bankCd, @PathVariable String holderName, @PathVariable long cnt) {
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		boolean flag = true;
		resMap.put("result", "NOK");

		DAO dao = new DAO();
		VactDtlDAO vactDtlDAO = new VactDtlDAO();
		dao.setTable("VW_VACT_UNUSED");
		dao.setColumns("COUNT(*) as cnt");
		dao.addWhere("bankCd", bankCd, DAO.eq);
		long orgCnt = dao.search().getRow(0).getLong("cnt");
		logger.debug("{} UNUSED VACT CNT: {}, {}", bankCd, orgCnt, cnt);
		if (cnt > orgCnt) {
			resMap.put("msg", "보유 계좌가 요청 계좌보다 적습니다.");
			flag = false;
		}
		if(flag) {
			List<String> issueIdList;
			//insert 된 issueId 리스트 반환
			issueIdList = vactDtlDAO.insert(mchtId, bankCd, cnt, holderName, SessionUtil.getUserId(request));

			if(issueIdList.size() < 1) {
				resMap.put("msg", "DB 작업에 실패했습니다.관리자에게 문의해주세요.");
			} else {
				dao.initRecord();

				logger.debug("INSERT HT_VACT_DTL {} : ", vactDtlDAO.insertHtVactDtl(issueIdList));
				logger.debug("UPDATE PG_MCHT_MNG_VACT {}:", dao.update("UPDATE PG_MCHT_MNG_VACT A LEFT JOIN (SELECT COUNT(*) as cnt, mchtId FROM PG_VACT_DTL GROUP BY mchtId) B ON A.mchtId = B.mchtId SET A.quantity = B.cnt WHERE A.mchtId ='"+mchtId+"' "));
				resMap.put("result", "OK");
			}

		}

		return resMap;
	}
	
	 // 관리정보 지불 및 정산 체킹
    @RequestMapping(value = {"/mcht/vact/check/{mchtId}"})
    public @ResponseBody SharedMap<String, Object> distVactCheck(HttpServletRequest request, @PathVariable String mchtId) {
    	SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	
    	AgencyMngDAO agencyDAO = new AgencyMngDAO();
    	DistDAO DistDAO = new DistDAO();

    	agencyDAO.setTable("PG_MAM_AGENCY_MNG");
		agencyDAO.setColumns("count(1) as cnt");
		agencyDAO.addWhere("agencyId", result.get("agencyId").toString(), DAO.eq);
		agencyDAO.addWhere("payType", "가상계좌", DAO.eq);

		//에이전시 관리정보 체크		
		if(agencyDAO.search().getRowFirst().getInt("cnt") >= 1) {
			resultMap.put("agencyRes", "OK");
		} else {
			resultMap.put("agencyRes", "NOK");
			resultMap.put("agencyMsg", "에이전시");
		}
		
		//대행사 관리정보 체크
		DistDAO.setTable("PG_MAM_DIST_MNG");
		DistDAO.setColumns("count(1) as cnt");
		DistDAO.addWhere("distId", result.get("distId").toString(), DAO.eq);
		DistDAO.addWhere("payType", "가상계좌", DAO.eq);
		
		if(DistDAO.search().getRowFirst().getInt("cnt") >= 1) {
			resultMap.put("distRes", "OK");
		} else {
			resultMap.put("distRes", "NOK");
			resultMap.put("distMsg", "대행사");
		}
		
		return resultMap;
    }
    
	
	
	
	@RequestMapping(value = "/mcht/pisp/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView pispAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("PISP_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("STARTDAY", CommonUtil.getCurrentDate("yyyyMMdd"));
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		StringBuffer key = new StringBuffer();
		while(key.length()<16){
			int iKey = Math.abs(random.nextInt()%74)+48;
			if((iKey >= 48 && iKey <=57)||(iKey >= 65 && iKey <=90) || (iKey >= 97 && iKey <=122)) {
				key.append((char)(iKey));
			}
		}
		
		
		request.setAttribute("AUTHKEY", key.toString());
		//230621 테이블 미존재로 빈값 리턴하게 수정
//		return new ModelAndView("/mcht/pisp/add", "DATAMAP", new MchtPispDAO().getByMchtId(mchtId));
		return new ModelAndView("/mcht/pisp/add", "DATAMAP", new SharedMap<String,Object>());
	}

	@RequestMapping(value = {"/mcht/pisp/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse pispInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		SharedMap<String,Object> map = new MchtPispDAO().setPispAccount(cpRequest.getKeyValue("mchtId"));
		//가상계좌 낑겨 넣기
		cpRequest.setData("bankCd", "020");
		cpRequest.setData("account",map.getString("account"));
		cpRequest.setData("issueId",map.getString("issueId"));
		
		//230621 테이블 미존재로 주석처리 , 에러 리턴
		/*if (cpDAO.insert("PG_MCHT_MNG_PISP", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("지급이체대행 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest)
		        		.resultNOK("지급이체대행 정보 변경에 실패하였습니다.",cpDAO.getError())
		        		.cpResponse();
		}*/

		return new CPRUtil(cpRequest)
				.resultNOK("지급이체대행 정보 변경에 실패하였습니다.",cpDAO.getError())
				.cpResponse();
	}
	
	@RequestMapping(value = "/mcht/pisp/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView pispModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());  
	  return new ModelAndView("/mcht/pisp/modify","DATAMAP",new MchtPispDAO().getByMchtId(mchtId));
	}
	 
	@RequestMapping(value = {"/mcht/pisp/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse pispUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.update("PG_MCHT_MNG_PISP", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("지급이체대행 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("지급이체대행 정보 변경에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/diff/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView diffAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDiffDAO().getMcht(mchtId).getRowFirst());
		return new ModelAndView("/mcht/diff/add");
	}
	
	@RequestMapping(value = {"/mcht/diff/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse diffInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.insert("PG_MCHT_DIFF_UPLOAD", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("차액정산 가맹점 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("차액정산 가맹점 정보 등록에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/diff/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView diffModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDiffDAO().getDiffUpload(mchtId).getRowFirst());
		return new ModelAndView("/mcht/diff/modify");
	}
	
	@RequestMapping(value = {"/mcht/diff/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse diffUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.update("PG_MCHT_DIFF_UPLOAD", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("차액정산 가맹점 정보가 수정되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("차액정산 가맹점 정보 수정에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/diffType/{identity}", method = RequestMethod.GET)
	public @ResponseBody Object diffType(HttpServletRequest request, @PathVariable String identity) {
		String diffType = new MchtDiffDAO().getDiffTypeByIdentity(identity).getRowFirst().getString("mchtType");
    	if(CommonUtil.isNullOrSpace(diffType)) diffType = "일반";
    	SharedMap<String, Object> map = new SharedMap<String, Object>();
		map.put("diffType", diffType);
		return map;
	}
	
	@RequestMapping(value = "/mcht/feeTemplate/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtFeeTemplateList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtFeeTemplateDAO feeDAO = new MchtFeeTemplateDAO();
		RecordSet rset = feeDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,feeDAO).setView(request,"/mcht/feeTemplate/list","");
	}
	
	@RequestMapping(value = {"/mcht/feeTemplate/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mchtFeeTemplateInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
				
		if (cpDAO.insertByOper("PG_MCHT_FEE_TEMPLATE", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 수수료 템플릿이 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 수수료 템플릿 등록에 실패하였습니다.",cpDAO.getError() ).cpResponse();
		}
    }
	
	@RequestMapping(value = "/mcht/feeTemplate/modify/{idx}", method = RequestMethod.GET)
	public ModelAndView mchtFeeTemplateModify(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> sharedMap = new MchtFeeTemplateDAO().getFeeTemplate(idx).getRowFirst();
    	
    	sharedMap.put("rate", String.format("%.3f",sharedMap.getDouble("rate")*100));
    	sharedMap.put("agencyRate", String.format("%.3f",sharedMap.getDouble("agencyRate")*100));
    	sharedMap.put("distRate", String.format("%.3f",sharedMap.getDouble("distRate")*100));
    	sharedMap.put("salesRate", String.format("%.3f",sharedMap.getDouble("salesRate")*100));
    	sharedMap.put("diff0DistRate", String.format("%.3f",sharedMap.getDouble("diff0DistRate")*100));
    	sharedMap.put("diff1DistRate", String.format("%.3f",sharedMap.getDouble("diff1DistRate")*100));
    	sharedMap.put("diff2DistRate", String.format("%.3f",sharedMap.getDouble("diff2DistRate")*100));
    	sharedMap.put("diff3DistRate", String.format("%.3f",sharedMap.getDouble("diff3DistRate")*100));
    	sharedMap.put("diff0CheckDistRate", String.format("%.3f",sharedMap.getDouble("diff0CheckDistRate")*100));
    	sharedMap.put("diff1CheckDistRate", String.format("%.3f",sharedMap.getDouble("diff1CheckDistRate")*100));
    	sharedMap.put("diff2CheckDistRate", String.format("%.3f",sharedMap.getDouble("diff2CheckDistRate")*100));
    	sharedMap.put("diff3CheckDistRate", String.format("%.3f",sharedMap.getDouble("diff3CheckDistRate")*100));
    	sharedMap.put("diff0AgencyRate", String.format("%.3f",sharedMap.getDouble("diff0AgencyRate")*100));
    	sharedMap.put("diff1AgencyRate", String.format("%.3f",sharedMap.getDouble("diff1AgencyRate")*100));
    	sharedMap.put("diff2AgencyRate", String.format("%.3f",sharedMap.getDouble("diff2AgencyRate")*100));
    	sharedMap.put("diff3AgencyRate", String.format("%.3f",sharedMap.getDouble("diff3AgencyRate")*100));
    	sharedMap.put("diff0CheckAgencyRate", String.format("%.3f",sharedMap.getDouble("diff0CheckAgencyRate")*100));
    	sharedMap.put("diff1CheckAgencyRate", String.format("%.3f",sharedMap.getDouble("diff1CheckAgencyRate")*100));
    	sharedMap.put("diff2CheckAgencyRate", String.format("%.3f",sharedMap.getDouble("diff2CheckAgencyRate")*100));
    	sharedMap.put("diff3CheckAgencyRate", String.format("%.3f",sharedMap.getDouble("diff3CheckAgencyRate")*100));
    	sharedMap.put("diff0SalesRate", String.format("%.3f",sharedMap.getDouble("diff0SalesRate")*100));
    	sharedMap.put("diff1SalesRate", String.format("%.3f",sharedMap.getDouble("diff1SalesRate")*100));
    	sharedMap.put("diff2SalesRate", String.format("%.3f",sharedMap.getDouble("diff2SalesRate")*100));
    	sharedMap.put("diff3SalesRate", String.format("%.3f",sharedMap.getDouble("diff3SalesRate")*100));
    	sharedMap.put("diff0CheckSalesRate", String.format("%.3f",sharedMap.getDouble("diff0CheckSalesRate")*100));
    	sharedMap.put("diff1CheckSalesRate", String.format("%.3f",sharedMap.getDouble("diff1CheckSalesRate")*100));
    	sharedMap.put("diff2CheckSalesRate", String.format("%.3f",sharedMap.getDouble("diff2CheckSalesRate")*100));
    	sharedMap.put("diff3CheckSalesRate", String.format("%.3f",sharedMap.getDouble("diff3CheckSalesRate")*100));

    	request.setAttribute("DATAMAP", sharedMap);
		return new ModelAndView("/mcht/feeTemplate/modify");
	}
	
	@RequestMapping(value = {"/mcht/feeTemplate/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse mchtFeeTemplateUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.updateByOper("PG_MCHT_FEE_TEMPLATE", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 수수료 템플릿이 수정되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 수수료 템플릿 수정에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/feeTemplate/get/{idx}", method = RequestMethod.GET)
	public @ResponseBody Object mchtFeeTemplateGet(HttpServletRequest request, @PathVariable String idx) {
		return new MchtFeeTemplateDAO().getFeeTemplate(idx).getRowFirst();
	}
	
	@RequestMapping(value = "/mcht/ddct/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView ddctAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DDCTCODE", new MchtDdctDAO().getCode().getRows());
		return new ModelAndView("/mcht/ddct/add");
	}
	
	@RequestMapping(value = "/mcht/ddct/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse ddctInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		SharedMap<String, Object> ddctMap = new SharedMap<String, Object>();
		ddctMap.put("ddctId",MchtDdctDAO.getDdctId());
		ddctMap.put("mchtId",cpRequest.getValue("mchtId"));
		ddctMap.put("type",cpRequest.getValue("type"));
		ddctMap.put("monthlyAmt",(Object)String.valueOf(cpRequest.getValue("monthlyAmt")).replace(",", ""));
		ddctMap.put("startMonth",cpRequest.getValue("startMonth"));
		ddctMap.put("endMonth",cpRequest.getValue("endMonth"));
		ddctMap.put("status","진행");
		ddctMap.put("settleType",cpRequest.getValue("settleType"));
		ddctMap.put("regId",regId);
		ddctMap.put("regDay",regDay);
		
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		List<String> monthList = CommonUtil.getMonthList(ddctMap.getString("startMonth"), ddctMap.getString("endMonth"));
		ddctMap.put("totalAmt", ddctMap.getLong("monthlyAmt") * monthList.size());
		
		
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String query = "INSERT INTO PG_SETTLE_DDCT (scheId,mchtId,ddctId,stlDay,type,ddctAmt,stlStatus,stlId,summary,regId,regDay) VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
		try {
			int inserted = 0;
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			int batchSize = 10;
			int count = 0;
			for(String month:monthList) {
				int term = CommonUtil.parseInt(ddctMap.getString("settleType").replaceAll("M[+]", ""));
				String stlDay = ddctDAO.getSettleDay(month+CommonUtil.zerofill(term,2));
				
				int i =1;
				pstmt.setString(i++, MchtDdctDAO.getSettleSchId(count));
				pstmt.setString(i++, ddctMap.getString("mchtId"));
				pstmt.setString(i++, ddctMap.getString("ddctId"));
				pstmt.setString(i++, stlDay);
				pstmt.setString(i++, ddctMap.getString("type"));
				pstmt.setLong(i++, ddctMap.getLong("monthlyAmt"));
				pstmt.setString(i++, "정산대기");
				pstmt.setString(i++, ddctMap.getString("stlId"));
				pstmt.setString(i++, ddctMap.getString("summary"));
				pstmt.setString(i++, ddctMap.getString("regId"));
				pstmt.setString(i++, ddctMap.getString("regDay"));
				pstmt.addBatch();
				if(++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			inserted +=pstmt.executeBatch().length;
			if(inserted > 0) {
				String query2 = "INSERT INTO PG_MCHT_DDCT (ddctId,mchtId,startMonth,endMonth,status,type,settleType,totalAmt,monthlyAmt,regId,regDay) VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
				pstmt2 = conn.prepareStatement(query2);
				int j = 1;
				pstmt2.setString(j++, ddctMap.getString("ddctId"));
				pstmt2.setString(j++, ddctMap.getString("mchtId"));
				pstmt2.setString(j++, ddctMap.getString("startMonth"));
				pstmt2.setString(j++, ddctMap.getString("endMonth"));
				pstmt2.setString(j++, ddctMap.getString("status"));
				pstmt2.setString(j++, ddctMap.getString("type"));
				pstmt2.setString(j++, ddctMap.getString("settleType"));
				pstmt2.setString(j++, ddctMap.getString("totalAmt"));
				pstmt2.setString(j++, ddctMap.getString("monthlyAmt"));
				pstmt2.setString(j++, ddctMap.getString("regId"));
				pstmt2.setString(j++, ddctMap.getString("regDay"));
				
				if(pstmt2.executeUpdate() > 0) {
					conn.commit();
				}else {
					conn.rollback();
				}
			}else {
				conn.rollback();
			}
			
		}catch (Exception e) {
			return new CPRUtil(cpRequest).resultNOK("차감 정산 등록에 실패하였습니다.",CommonUtil.getExceptionMessage(e))	.cpResponse();
		}finally {
			db.close(pstmt);
			db.close(pstmt2);
			db.close(conn);
		}
		return new CPRUtil(cpRequest).resultOK("차감 정산이 등록되었습니다.").cpResponse();
	}
	
	@RequestMapping(value = "/mcht/ddct/modify/{ddctId}", method = RequestMethod.GET)
	public ModelAndView ddctModify(HttpServletRequest request, @PathVariable String ddctId) {
		SharedMap<String, Object> ddctMap = new MchtDdctDAO().getDdctId(ddctId).getRowFirst();
		request.setAttribute("DDCTCODE", new MchtDdctDAO().getCode().getRows());
		ddctMap.put("monthlyAmt", CommonUtil.moneyFormat(ddctMap.getString("monthlyAmt")));
		request.setAttribute("DATAMAP", ddctMap);
		request.setAttribute("SCHELIST", new MchtDdctDAO().getSchedule(ddctId).getRows());
		request.setAttribute("SETDATE", new MchtDdctDAO().getScheduleDate(ddctId).getRowFirst());
		CPSession session = SessionUtil.get(request);
		if(!session.getGrade().equals("본사") || session.getRole().equals("일반")) {
			return new ModelAndView("/mcht/ddct/view");
		}
		if(ddctMap.getString("status").equals("종료")) {
			return new ModelAndView("/mcht/ddct/view");
		}else {
			return new ModelAndView("/mcht/ddct/modify");
		}
	}
	
	@RequestMapping(value = "/mcht/ddct/update", method = RequestMethod.POST)
	public @ResponseBody Object ddctUpdate(HttpServletRequest request) {
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		SharedMap<String, Object> ddctMap = new SharedMap<String, Object>();
		ddctMap.put("ddctId",request.getParameter("ddctId"));
		ddctMap.put("mchtId",request.getParameter("mchtId"));
		ddctMap.put("type",request.getParameter("type"));
		ddctMap.put("monthlyAmt",(Object)String.valueOf(request.getParameter("monthlyAmt")).replace(",", ""));
		ddctMap.put("startMonth",request.getParameter("startMonth"));
		ddctMap.put("endMonth",request.getParameter("endMonth"));
		ddctMap.put("settleType",request.getParameter("settleType"));
		ddctMap.put("regId",regId);
		ddctMap.put("regDay",regDay);
		
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		String startMonth = ddctDAO.getStartMonth(ddctMap.getString("ddctId"));
		List<String> monthList = CommonUtil.getMonthList(startMonth, ddctMap.getString("endMonth"));
		ddctMap.put("totalAmt", ddctDAO.getCompleteAmt(ddctMap.getString("ddctId"))+ ddctMap.getLong("monthlyAmt") * monthList.size());
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String query = "INSERT INTO PG_SETTLE_DDCT (scheId,mchtId,ddctId,stlDay,type,ddctAmt,stlStatus,regId,regDay) VALUES (?,?,?,?,?,?,?,?,?) ";
		try {
			// 먼저 기존 정산대기 스케쥴 삭제처리
			int inserted = 0;
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt3 = conn.prepareStatement("DELETE FROM PG_SETTLE_DDCT WHERE ddctId = '"+ddctMap.getString("ddctId")+"' AND stlStatus = '정산대기'");
			pstmt3.executeUpdate();
			
			pstmt = conn.prepareStatement(query);
			int batchSize = 10;
			int count = 0;
			for(String month:monthList) {
				int term = CommonUtil.parseInt(ddctMap.getString("settleType").replaceAll("M[+]", ""));
				String stlDay = ddctDAO.getSettleDay(month+CommonUtil.zerofill(term,2));
					
				int i =1;
				pstmt.setString(i++, MchtDdctDAO.getSettleSchId(count));
				pstmt.setString(i++, ddctMap.getString("mchtId"));
				pstmt.setString(i++, ddctMap.getString("ddctId"));
				pstmt.setString(i++, stlDay);
				pstmt.setString(i++, ddctMap.getString("type"));
				pstmt.setLong(i++, ddctMap.getLong("monthlyAmt"));
				pstmt.setString(i++, "정산대기");
				pstmt.setString(i++, ddctMap.getString("regId"));
				pstmt.setString(i++, ddctMap.getString("regDay"));
				pstmt.addBatch();
				if(++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			inserted +=pstmt.executeBatch().length;
			if(inserted > 0) {
				String query2 = "UPDATE PG_MCHT_DDCT SET startMonth= ?, endMonth = ?, type = ?, settleType = ?, totalAmt = ?, monthlyAmt = ? WHERE ddctId = ? ";
				pstmt2 = conn.prepareStatement(query2);
				int j = 1;
				pstmt2.setString(j++, ddctMap.getString("startMonth"));
				pstmt2.setString(j++, ddctMap.getString("endMonth"));
				pstmt2.setString(j++, ddctMap.getString("type"));
				pstmt2.setString(j++, ddctMap.getString("settleType"));
				pstmt2.setString(j++, ddctMap.getString("totalAmt"));
				pstmt2.setString(j++, ddctMap.getString("monthlyAmt"));
				pstmt2.setString(j++, ddctMap.getString("ddctId"));
				if(pstmt2.executeUpdate() > 0) {
					conn.commit();
					resultMap.put("resultCd", "0000");
				}else {
					conn.rollback();
					resultMap.put("resultCd", "9999");
				}
			
			}else {
				conn.rollback();
				resultMap.put("resultCd", "9999");
			}
			
			
		}catch (Exception e) {
			logger.debug(e.getMessage());
			resultMap.put("resultCd", "9999");
		}finally {
			db.close(pstmt);
			db.close(pstmt2);
			db.close(pstmt3);
			db.close(conn);
		}
		
		return resultMap;
	}
	

	@RequestMapping(value = "/mcht/ddct/close", method = RequestMethod.POST)
	public @ResponseBody Object ddctClose(HttpServletRequest request) {
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		SharedMap<String, Object> endMap = new SharedMap<String, Object>();
		endMap.put("ddctId",request.getParameter("ddctId"));
		endMap.put("mchtId",request.getParameter("mchtId"));
		endMap.put("type",request.getParameter("type"));
		endMap.put("summary", request.getParameter("summary"));
		endMap.put("endDate", request.getParameter("endDate").replace("-",""));
		endMap.put("ddctAmt", (Object)String.valueOf(request.getParameter("ddctAmt")).replace(",", ""));
		endMap.put("regId",regId);
		endMap.put("regDay",regDay);
		logger.debug(endMap.toString());
		
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		resultMap.put("resultCd", "0000");
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String query = "INSERT INTO PG_SETTLE_DDCT (scheId,mchtId,ddctId,stlDay,type,ddctAmt,stlStatus,summary,regId,regDay) VALUES (?,?,?,?,?,?,?,?,?,?) ";
		
		try {
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt3 = conn.prepareStatement("DELETE FROM PG_SETTLE_DDCT WHERE ddctId = '"+endMap.getString("ddctId")+"' AND stlStatus = '정산대기'");
			pstmt3.executeUpdate();
			
			pstmt = conn.prepareStatement(query);
			
			int i =1;
			pstmt.setString(i++, MchtDdctDAO.getSettleSchId(1));
			pstmt.setString(i++, endMap.getString("mchtId"));
			pstmt.setString(i++, endMap.getString("ddctId"));
			pstmt.setString(i++, endMap.getString("endDate"));
			pstmt.setString(i++, endMap.getString("type"));
			pstmt.setLong(i++, endMap.getLong("ddctAmt"));
			pstmt.setString(i++, "정산완료");
			pstmt.setString(i++, endMap.getString("summary"));
			pstmt.setString(i++, endMap.getString("regId"));
			pstmt.setString(i++, endMap.getString("regDay"));
			
			
			
			String query2 = "UPDATE PG_MCHT_DDCT SET status = ? WHERE ddctId = ? ";
			pstmt2 = conn.prepareStatement(query2);
			int j = 1;
			pstmt2.setString(j++, "종료");
			pstmt2.setString(j++, endMap.getString("ddctId"));
			if(pstmt.executeUpdate() > 0 && pstmt2.executeUpdate() > 0) {
				conn.commit();
				resultMap.put("resultCd", "0000");
			}else {
				conn.rollback();
				resultMap.put("resultCd", "9999");
			}
			
			
			
		}catch (Exception e) {
			logger.debug(e.getMessage());
			resultMap.put("resultCd", "9999");
		}finally {
			db.close(pstmt);
			db.close(pstmt2);
			db.close(pstmt3);
			db.close(conn);
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/ddct/delete", method = RequestMethod.POST)
	public @ResponseBody Object ddctDelete(HttpServletRequest request) {
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		if(ddctDAO.ddctDelete(request.getParameter("ddctId"))){
			resultMap.put("resultCd", "0000");
		}else {
			resultMap.put("resultCd", "9999");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/ddct/change/{scheId}", method = RequestMethod.GET)
    public ModelAndView ddctChange(HttpServletRequest request, @PathVariable String scheId) {
		SharedMap<String, Object> map = new MchtDdctDAO().getScheduleByScheId(scheId);
		map.put("ddctAmt", CommonUtil.moneyFormat(map.getString("ddctAmt")));
		request.setAttribute("DATAMAP", map);
		return new ModelAndView("/mcht/ddct/change");
	}
	
	@RequestMapping(value = "/mcht/ddct/scheUpdate", method = RequestMethod.POST)
	public @ResponseBody Object scheUpdate(HttpServletRequest request) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		SharedMap<String, Object> map = new SharedMap<String, Object>();
		map.put("scheId", request.getParameter("scheId"));
		map.put("ddctId", request.getParameter("ddctId"));
		map.put("stlDay", request.getParameter("stlDay"));
		map.put("ddctAmt", (Object)String.valueOf(request.getParameter("ddctAmt")).replace(",", ""));
		map.put("regId", SessionUtil.getUserId(request));
		map.put("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		if(ddctDAO.updateSchedule(map)) {
			resultMap.put("resultCd", "0000");
		}else {
			resultMap.put("resultCd", "9999");
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/inter/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView interAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATAACQLIST", new MchtInterestDAO().acquirerList().getRows());
		request.setAttribute("DATATEMPLATE", new MchtInterTemplateDAO().getList().getRows());
		return new ModelAndView("/mcht/inter/add");
	}
	
	@RequestMapping(value = "/mcht/inter/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object interInsert(HttpServletRequest request, HttpServletResponse response) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String line = null;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		String query = "INSERT INTO PG_MCHT_INTEREST (mchtId, acquirer, authType, `m02`, `m03`, `m04`, `m05`, `m06`, `m07`, `m08`, `m09`, `m10`, `m11`, `m12`,  `m13`, `m14`, `m15`, `m16`, `m17`, `m18`, `m19`, `m20`, `m21`, `m22`, `m23`, `m24`, regId, regDay) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			logger.debug(sb.toString());
			Gson gson = new Gson();
			Interest interest = gson.fromJson(sb.toString(), Interest.class);
			logger.debug(interest.mchtId);
			String mchtId = interest.mchtId;
			List<SharedMap<String, Object>> list = interest.list;
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			for(SharedMap<String, Object> map:list) {
				int i =1;
				pstmt.setString(i++, mchtId);
				pstmt.setString(i++, map.getString("acquirer"));
				pstmt.setString(i++, "인증");
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m02") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m03") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m04") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m05") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m06") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m07") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m08") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m09") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m10") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m11") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m12") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m13") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m14") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m15") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m16") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m17") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m18") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m19") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m20") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m21") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m22") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m23") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m24") / 100)));
				pstmt.setString(i++, regId);
				pstmt.setString(i++, regDay);
				pstmt.addBatch();
			}
			if(pstmt.executeBatch().length > 0) {
				conn.commit();
				resultMap.put("resultCd", "0000");
			}else {
				conn.rollback();
				resultMap.put("resultCd", "9999");
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCd", "9999");
		}finally {
			db.close(pstmt);
			db.close(conn);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/inter/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView interModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATAACQLIST", new MchtInterestDAO().getModifyList(mchtId).getRows());
		request.setAttribute("DATATEMPLATE", new MchtInterTemplateDAO().getList().getRows());
		return new ModelAndView("/mcht/inter/modify");
	}
	
	@RequestMapping(value = "/mcht/inter/update", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object interUpdate(HttpServletRequest request, HttpServletResponse response) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String line = null;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		String query = "INSERT INTO PG_MCHT_INTEREST (mchtId, acquirer, authType, `m02`, `m03`, `m04`, `m05`, `m06`, `m07`, `m08`, `m09`, `m10`, `m11`, `m12`, `m13`, `m14`, `m15`, `m16`, `m17`, `m18`, `m19`, `m20`, `m21`, `m22`, `m23`, `m24`, regId, regDay) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE `m02` = ?, `m03` = ?, `m04` = ?, `m05` = ?, `m06` = ?, `m07` = ?, `m08` = ?, `m09` = ?, `m10` = ?, `m11` = ?, `m12` = ?,  `m13` = ?, `m14` = ?, `m15` = ?, `m16` = ?, `m17` = ?, `m18` = ?, `m19` = ?, `m20` = ?, `m21` = ?, `m22` = ? , `m23` = ?, `m24` = ? , regId = ? ";
				
	
		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			logger.debug(sb.toString());
			Gson gson = new Gson();
			Interest interest = gson.fromJson(sb.toString(), Interest.class);
			logger.debug(interest.mchtId);
			String mchtId = interest.mchtId;
			List<SharedMap<String, Object>> list = interest.list;
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			for(SharedMap<String, Object> map:list) {
				int i =1;
				// insert
				pstmt.setString(i++, mchtId);
				pstmt.setString(i++, map.getString("acquirer"));
				pstmt.setString(i++, "인증");
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m02") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m03") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m04") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m05") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m06") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m07") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m08") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m09") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m10") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m11") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m12") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m13") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m14") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m15") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m16") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m17") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m18") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m19") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m20") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m21") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m22") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m23") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m24") / 100)));
				pstmt.setString(i++, regId);
				pstmt.setString(i++, regDay);
				
				//update
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m02") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m03") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m04") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m05") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m06") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m07") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m08") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m09") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m10") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m11") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m12") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m13") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m14") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m15") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m16") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m17") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m18") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m19") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m20") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m21") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m22") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m23") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m24") / 100)));
				pstmt.setString(i++, regId);
				
				
				pstmt.addBatch();
			}
			if(pstmt.executeBatch().length > 0) {
				conn.commit();
				resultMap.put("resultCd", "0000");
			}else {
				conn.rollback();
				resultMap.put("resultCd", "9999");
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCd", "9999");
		}finally {
			db.close(pstmt);
			db.close(conn);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/van/interFee/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanFeeInterList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		OrgInterFeeDAO vanDAO = new OrgInterFeeDAO();
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/van/interFee/list","");
	}
	
	@RequestMapping(value = "/mcht/interTemplate/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtInterTemplateList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtInterTemplateDAO feeDAO = new MchtInterTemplateDAO();
		RecordSet rset = feeDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,feeDAO).setView(request,"/mcht/interTemplate/list","");
	}
	
	@RequestMapping(value = "/mcht/interTemplate/add", method = RequestMethod.GET)
	public ModelAndView interTemplateAdd(HttpServletRequest request) {
		request.setAttribute("DATAACQLIST", new MchtInterestDAO().acquirerList().getRows());
		return new ModelAndView("/mcht/interTemplate/add");
	}
	
	@RequestMapping(value = {"/mcht/interTemplate/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object mchtInterTemplateInsert(HttpServletRequest request) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String line = null;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String templateId =  new MchtInterTemplateDAO().getTemplateId();
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		String query = "INSERT INTO PG_MCHT_INTER_TEMPLATE_DTL (`templateId`,`acquirer`, `authType`, `m02`, `m03`, `m04`, `m05`, `m06`, `m07`, `m08`, `m09`, `m10`, `m11`, `m12`, `m13`, `m14`, `m15`, `m16`, `m17`, `m18`, `m19`, `m20`, `m21`, `m22`, `m23`, `m24` ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		String query2 = "INSERT INTO PG_MCHT_INTER_TEMPLATE (`templateId`,`name`, `regId`, `regDay`) VALUES(?, ?, ?, ?) ";
		
		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			logger.debug(sb.toString());
			Gson gson = new Gson();
			Interest interest = gson.fromJson(sb.toString(), Interest.class);
			String name = interest.name;
			List<SharedMap<String, Object>> list = interest.list;
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			for(SharedMap<String, Object> map:list) {
				int i =1;
				pstmt.setString(i++, templateId);
				pstmt.setString(i++, map.getString("acquirer"));
				pstmt.setString(i++, "인증");
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m02") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m03") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m04") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m05") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m06") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m07") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m08") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m09") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m10") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m11") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m12") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m13") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m14") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m15") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m16") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m17") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m18") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m19") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m20") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m21") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m22") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m23") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m24") / 100)));
				pstmt.addBatch();
			}
			if(pstmt.executeBatch().length > 0) {
				pstmt2 = conn.prepareStatement(query2);
				int j = 1;
				pstmt2.setString(j++, templateId);
				pstmt2.setString(j++, name);
				pstmt2.setString(j++, regId);
				pstmt2.setString(j++, regDay);
				if(pstmt2.executeUpdate() > 0) {
					conn.commit();
					resultMap.put("resultCd", "0000");
				}else {
					conn.rollback();
					resultMap.put("resultCd", "9999");
				}
			}else {
				conn.rollback();
				resultMap.put("resultCd", "9999");
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCd", "9999");
		}finally {
			db.close(pstmt);
			db.close(pstmt2);
			db.close(conn);
		}
		return resultMap;
    }
	
	@RequestMapping(value = "/mcht/interTemplate/modify/{templateId}", method = RequestMethod.GET)
	public ModelAndView mchtInterTemplateModify(HttpServletRequest request, @PathVariable String templateId) {
		request.setAttribute("DATAMAP", new MchtInterTemplateDAO().getInterTemplate(templateId).getRowFirst());
		request.setAttribute("DATALIST", new MchtInterTemplateDAO().getModifyList(templateId).getRows());
		return new ModelAndView("/mcht/interTemplate/modify");
	}
	
	@RequestMapping(value = {"/mcht/interTemplate/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object mchtInterTemplateUpdate(HttpServletRequest request) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String line = null;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		String query = "INSERT INTO PG_MCHT_INTER_TEMPLATE_DTL (templateId, acquirer, authType, `m02`, `m03`, `m04`, `m05`, `m06`, `m07`, `m08`, `m09`, `m10`, `m11`, `m12`, `m13`, `m14`, `m15`, `m16`, `m17`, `m18`, `m19`, `m20`, `m21`, `m22`, `m23`, `m24`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE `m02` = ?, `m03` = ?, `m04` = ?, `m05` = ?, `m06` = ?, `m07` = ?, `m08` = ?, `m09` = ?, `m10` = ?, `m11` = ?, `m12` = ?,`m13` = ?, `m14` = ?, `m15` = ?, `m16` = ?, `m17` = ?, `m18` = ?, `m19` = ?, `m20` = ?, `m21` = ?, `m22` = ? , `m23` = ?, `m24` = ? ";
			
		String query2 = "UPDATE PG_MCHT_INTER_TEMPLATE SET `name` = ?  WHERE templateId = ? ";
	
		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			logger.debug(sb.toString());
			Gson gson = new Gson();
			Interest interest = gson.fromJson(sb.toString(), Interest.class);
			String templateId = interest.templateId;
			String name = interest.name;
			List<SharedMap<String, Object>> list = interest.list;
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			for(SharedMap<String, Object> map:list) {
				int i =1;
				// insert
				pstmt.setString(i++, templateId);
				pstmt.setString(i++, map.getString("acquirer"));
				pstmt.setString(i++, "인증");
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m02") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m03") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m04") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m05") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m06") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m07") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m08") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m09") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m10") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m11") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m12") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m13") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m14") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m15") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m16") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m17") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m18") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m19") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m20") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m21") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m22") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m23") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m24") / 100)));
				
				//update
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m02") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m03") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m04") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m05") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m06") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m07") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m08") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m09") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m10") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m11") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m12") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m13") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m14") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m15") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m16") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m17") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m18") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m19") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m20") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m21") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m22") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m23") / 100)));
				pstmt.setDouble(i++, Double.parseDouble(String.format("%.5f", map.getDouble("m24") / 100)));
				
				pstmt.addBatch();
			}
			if(pstmt.executeBatch().length > 0) {
				pstmt2 = conn.prepareStatement(query2);
				int j = 1;
				pstmt2.setString(j++, name);
				pstmt2.setString(j++, templateId);
				if(pstmt2.executeUpdate() > 0) {
					conn.commit();
					resultMap.put("resultCd", "0000");
				}else {
					conn.rollback();
					resultMap.put("resultCd", "9999");
				}
			}else {
				conn.rollback();
				resultMap.put("resultCd", "9999");
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCd", "9999");
		}finally {
			db.close(pstmt);
			db.close(pstmt2);
			db.close(conn);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/interTemplate/get/{templateId}", method = RequestMethod.GET)
	public @ResponseBody Object mchtInterTemplateGet(HttpServletRequest request, @PathVariable String templateId) {
		return new MchtInterTemplateDAO().getModifyList(templateId).getRows();
	}
	
	@RequestMapping(value = "/mcht/feeTemplate/delete/{idx}", method = RequestMethod.GET)
	public @ResponseBody Object mchtTemplateDelete(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> resultMap = new SharedMap<String,Object>();
		CPDAO cpDAO = new CPDAO();
		cpDAO.setTable("PG_MCHT_FEE_TEMPLATE");
		cpDAO.addWhere("idx", idx, DAO.eq);
		if(cpDAO.delete()) {
			resultMap.put("result", "OK");
		}else {
			resultMap.put("result", "NOK");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/InterTemplate/delete/{templateId}", method = RequestMethod.GET)
	public @ResponseBody Object mchtInterTemplateDelete(HttpServletRequest request, @PathVariable String templateId) {
		SharedMap<String, Object> resultMap = new SharedMap<String,Object>();
		CPDAO cpDAO = new CPDAO();
		cpDAO.setTable("PG_MCHT_INTER_TEMPLATE");
		cpDAO.addWhere("templateId", templateId, DAO.eq);
		if(cpDAO.delete()) {
			resultMap.put("result", "OK");
		}else {
			resultMap.put("result", "NOK");
		}
		return resultMap;
	}
	
	// ==================================================== 비대면계좌개설 정보 조회
	@RequestMapping(value = { "/mcht/accntSearch/form" })
	public ModelAndView accntSearchform(HttpServletRequest request) {
		return new ModelAndView("/mcht/accntSearch/form");
	}

	@RequestMapping(value = "/mcht/accntSearch/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView accntSearchlist(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws UnsupportedEncodingException {
		MchtDAO mchtDAO = new MchtDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		//String key = "yGXMeC5TP3xHkmX5+Yk03PZ1wYZ3JbdEYnRNcmDTd64vL9V1aW4hdzSeJ86ztLSo"; //테스트키
		String key = "6wCPEeQ0egkz3mPaE3R3MMGGW3KNoxunQBTcnow5g80VU431JHHPtYKLM0VDAgkU"; //운영키

		// 테이블이 존재하지 않아 빈값 처리
		//RecordSet rset = mchtDAO.accntSeachList(cpRequest.data, cpRequest.page);
		RecordSet rset = new RecordSet();

		logger.info("accntSearchlist : " + rset.size());
		for(int i = 0; i < rset.size(); i++) {
			String name = rset.getRow(i).getString("name");
			String mobileno = rset.getRow(i).getString("mobileno");
			String accntno = rset.getRow(i).getString("accntno");

			name = HanaTICryptoUtil.Decrypt(name,key);
			mobileno = HanaTICryptoUtil.Decrypt(mobileno,key);
			accntno = HanaTICryptoUtil.Decrypt(accntno,key);

			rset.getRow(i).put("name", name);
			rset.getRow(i).put("mobileno", mobileno);
			rset.getRow(i).put("accntno", accntno);
		}
		
		return new CPRUtil(cpRequest).dataList(rset, mchtDAO).setView(request, "/mcht/accntSearch/list", "");
	}
	
	@RequestMapping(value = "/mcht/chargeMng/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView chargeMngfAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHTMAP", new MchtDAO().getById(mchtId).getRowFirst());
		return new ModelAndView("/mcht/chargeMng/add");
	}
	
	@RequestMapping(value = {"/mcht/chargeMng/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse chargeMngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		String mchtId = cpRequest.getValue("mchtId");
		String tk = GenKey.genKeys(CPKEY.CASH_TRANSFER, mchtId);

		//PYS : 출금키 생성시 암호화해서 DB에 저장
		String encKey = KSignUtil.getInstance().Encrypt(tk);

		if (cpDAO.insertByOperAddKey("PG_MCHT_CHARGE_MNG", encKey, SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 충전정산 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 충전정산 정보 등록에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}

	@RequestMapping(value = "/mcht/chargeMng/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView chargeMngModify(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> sharedMap = new MchtChargeSettleDAO().getById(mchtId).getRowFirst();
		sharedMap.put("withdrawFee", CommonUtil.moneyFormat(sharedMap.getString("withdrawFee")));
		sharedMap.put("distPayInFee", CommonUtil.moneyFormat(sharedMap.getString("distPayInFee")));
		sharedMap.put("agencyPayInFee", CommonUtil.moneyFormat(sharedMap.getString("agencyPayInFee")));
		sharedMap.put("salesPayInFee", CommonUtil.moneyFormat(sharedMap.getString("salesPayInFee")));
		
		request.setAttribute("MCHTMAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATAMAP", sharedMap);
		return new ModelAndView("/mcht/chargeMng/modify");
	}
	
	@RequestMapping(value = {"/mcht/chargeMng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse chargeMngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.updateByOper("PG_MCHT_CHARGE_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 충전정산 정보가 수정되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 충전정산 정보 수정에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}

	@RequestMapping(value = {"/mcht/chargeMng/transferKey"}, method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> createTransferKey(HttpServletRequest request, @RequestBody Map<String, Object> param) {
		MchtChargeSettleDAO mchtChargeSettleDAO = new MchtChargeSettleDAO();
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();

		String mchtId = (String) param.get("mchtId");
		SharedMap<String,Object> sharedMap = mchtChargeSettleDAO.getById(mchtId).getRowFirst();

		// 키 생성
		String originalKey = GenKey.genKeys(CPKEY.CASH_TRANSFER, mchtId);
		String encKey = KSignUtil.getInstance().Encrypt(originalKey);

		boolean updated = false;
		resMap.put("msg", "출금키 생성에 실패하였습니다.");
		resMap.put("result", "NOK");

		if(sharedMap.size() > 0) {
			/*String transferKeyTel = sharedMap.getString("transferKeyTel");
			if(CommonUtil.isEmpty(transferKeyTel)) {
				logger.info("출금키 연락처가 존재하지 않습니다.");
				return resMap;
			} else {
				String msg = "출금키가 생성되었습니다. 출금키: " + originalKey + "";
				if(!sendSMS(msg, transferKeyTel)) {
					return resMap;
				}
			}*/
			updated = mchtChargeSettleDAO.updateTransferKey(encKey, SessionUtil.getUserId(request), mchtId);
		}

		if(updated) {
			resMap.put("msg", "출금키가 생성되었습니다. " + "<br> 출금키 : " + originalKey + "");
			String newEncKey = encKey.substring(0, 10) + "************";
			resMap.put("transferKey", newEncKey);
			resMap.put("originalKey", originalKey);
			resMap.put("result", "OK");
		}

		return resMap;
	}

	private boolean sendSMS(String msg, String setTel) {
		WebCache wc = new WebCache();
		String number = String.format("%1$" + 6 + "s", ((int) (Math.random() * 999999) + 1)).replace(' ', '0');
		wc.setSMSKey(setTel, number);

		String msgBody = "[(주)부국위너스] " + msg + "";
		try {
			SmsUtil.sendSms(SmsUtil.LMS_URL, setTel.replaceAll("\\[^0-9]+", ""), msgBody);
			logger.debug("NoticeSend SMS SEND");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("공지사항 SMS 전송 중 오류발생. 확인요망 [" + e.getMessage() + "]");

			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/mcht/balance/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView modifyBalance(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHTMAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATAMAP", new MchtChargeSettleDAO().getBalance(mchtId));
		return new ModelAndView("/mcht/balance/modify");
	}
	
	@RequestMapping(value = {"/mcht/balance/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse modifyBalanceInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPSession session = SessionUtil.get(request);
		if(!session.getGrade().equals("본사") || !session.getRole().equals("마스터")) {
			logger.debug("권한 없음 실패");
			return new CPRUtil(cpRequest).resultNOK("가맹점 충전정산 수기등록 권한이 없습니다.")
					.cpResponse();
		}
		ChargeSettleDAO chargeSettleDAO = new ChargeSettleDAO();
		SharedMap<String, Object> settleMap = new SharedMap<String,Object>();
		settleMap.put("trxId", chargeSettleDAO.getChargeSettleTrxId());
		settleMap.put("mchtId", cpRequest.getValue("mchtId"));
		settleMap.put("trxType", cpRequest.getValue("trxType"));
		settleMap.put("trxUnit", cpRequest.getValue("trxUnit"));
		String regDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
		settleMap.put("trxDay", regDate.substring(0, 8));
		settleMap.put("trxTime", regDate.substring(8));
		settleMap.put("trackId", settleMap.getString("trxId"));
		settleMap.put("refId", "");
		settleMap.put("amount", cpRequest.getLongValue("amount"));
		settleMap.put("netAmount", cpRequest.getLongValue("amount"));
		if(settleMap.isEquals("trxType", "입금")) {
			settleMap.put("balance", chargeSettleDAO.getMchtBalance(settleMap.getString("mchtId")).getLong("balance")+settleMap.getLong("netAmount"));
		}else {
			settleMap.put("balance", chargeSettleDAO.getMchtBalance(settleMap.getString("mchtId")).getLong("balance")-settleMap.getLong("netAmount"));
		}
		settleMap.put("summary", cpRequest.getValue("comment"));
		settleMap.put("regId", SessionUtil.getUserId(request));
		settleMap.put("regDay", regDate.substring(0, 8));
		
		if(chargeSettleDAO.insertChargeSettle(settleMap)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 충전정산 수기등록이 완료되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 충전정산 수기등록에 실패하였습니다.")
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/distRate/get/{distNum}", method = RequestMethod.GET)
	public @ResponseBody Object distRateGet(HttpServletRequest request, @PathVariable String distNum) {
		return new DistMngDAO().getById(distNum).getRowFirst();
	}
	
	@RequestMapping(value = "/mcht/agencyRate/get/{agencyNum}", method = RequestMethod.GET)
	public @ResponseBody Object agencyRateGet(HttpServletRequest request, @PathVariable String agencyNum) {
		return new AgencyMngDAO().getById(agencyNum).getRowFirst();
	}
	
	@RequestMapping(value = "/mcht/salesRate/get/{salesNum}", method = RequestMethod.GET)
	public @ResponseBody Object salesRateGet(HttpServletRequest request, @PathVariable String salesNum) {
		return new MemberSalesMngDAO().getByNum(salesNum).getRowFirst();
	}
	
	@RequestMapping(value = {"/mcht/tmnInsert/form"})
	public ModelAndView tmnInsertForm(HttpServletRequest request) {
		request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
		return new ModelAndView("/mcht/tmnInsert/form");
	}
	
	@RequestMapping(value = "/mcht/tmnInsert/excel/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> tmnExceladd(HttpServletRequest request) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		String regId = "SYSTEM";
//		String regId = SessionUtil.getUserId(request);
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			Gson gson = new Gson();
			TmnList tmnList = gson.fromJson(sb.toString(), TmnList.class);
			
			List<SharedMap<String, Object>> list = tmnList.list;
			
			logger.info("mchtId [{}]",tmnList.mchtId);
			logger.info("taxId [{}]",tmnList.taxId);
			logger.info("webPay [{}]",tmnList.webPay);
			logger.info("appDirect [{}]",tmnList.appDirect);
			logger.info("apiMaxInstall [{}]",tmnList.apiMaxInstall);
			logger.info("semiAuth [{}]",tmnList.semiAuth);
			logger.info("activeDate [{}]",tmnList.activeDate);
			logger.info("ccType [{}]",tmnList.ccType);
			logger.info("refundType [{}]",tmnList.refundType);
			logger.info("payLimit [{}]",tmnList.payLimit);
			logger.info("limitAmount [{}]",tmnList.limitAmount);
			logger.info("limitStartTime [{}]",tmnList.limitStartTime);
			logger.info("limitEndTime [{}]",tmnList.limitEndTime);
			logger.info("van [{}]",tmnList.van);
			logger.info("vanIdx [{}]",tmnList.vanIdx);
			logger.info("TmnInsert excel COUNT : {}", list.size());
			
			if(list.size() > 0) {
				MchtTmnDAO mchtTmnDAO = new MchtTmnDAO();
				
				String tmnArr = "";
				for(SharedMap<String, Object> eachMap : list) {
					if(!eachMap.isNullOrSpace("tmnId")) {
						tmnArr += "'"+eachMap.getString("tmnId")+"',";
					}
				}
				
				if(!"".equals(tmnArr)) {
					tmnArr = tmnArr.substring(0,tmnArr.length()-1);
					List<SharedMap<String, Object>> tmnLists = mchtTmnDAO.getTmnList(tmnArr);
					
					if(tmnLists.size() > 0) {
						resultMap.put("result", "FAIL");
						resultMap.put("msg", "기존에 등록되어 있는 터미널아이디와 중복된 건이 있어 일괄 업로드가 불가합니다.<br>해당건 제외한 엑셀양식으로 등록 바랍니다.");
						
						String failTable = "<div class=\"form-group col-sm-12 form-subtitle\"><label><i class=\"fa fa-reorder\"></i>기존등록 터미널 내용</label></div>" + 
								"<div class=\"portlet-body form light\">" + 
								"<div class=\"table-scrollable\"><table class=\"pg-table table table-striped table-hover flip-content\">" +
								"<thead><th>가맹점아이디</th><th>가맹점명</th><th>터미널아이디</th><th>VAN</th><th>VAN정보</th><th>취급품목</th></thead>";
						for(SharedMap<String, Object> failMap:tmnLists) {
							failTable += "<tr>";
							failTable += "<td>"+failMap.getString("mchtId")+"</td>";
							failTable += "<td>"+failMap.getString("mchtName")+"</td>";
							failTable += "<td>"+failMap.getString("tmnId")+"</td>";
							failTable += "<td>"+failMap.getString("van")+"</td>";
							if(!failMap.isNullOrSpace("van")) {
								failTable += "<td>"+failMap.getString("vanName")+"("+failMap.getString("vanId")+")</td>";
							}else {
								failTable += "<td colspan=\"\" class=\"font-red\">지정되지 않음</td>";
							}
							
							failTable += "<td>"+failMap.getString("description")+"</td>";
							failTable += "</tr>";
						}
						failTable +="</table></div></div>";
						resultMap.put("failTable", failTable);
						return resultMap;
					}
				}
				
				List<SharedMap<String, Object>> dtlList = new ArrayList<SharedMap<String,Object>>(); 
				List<SharedMap<String, Object>> tmnLists = new ArrayList<SharedMap<String,Object>>(); 
				for(SharedMap<String, Object> eachMap : list) {
					SharedMap<String, Object> tmnMap = new SharedMap<String,Object>();
					if(!eachMap.isNullOrSpace("tmnId")) {
						tmnMap.put("tmnId",eachMap.getString("tmnId"));
					}else {
						tmnMap.put("tmnId",new MchtTmnDAO().getNewId());
					}
					tmnMap.put("mchtId",tmnList.mchtId);
					tmnMap.put("taxId",tmnList.taxId);
					tmnMap.put("status",eachMap.getString("status"));
					tmnMap.put("serial",eachMap.getString("serial"));
					tmnMap.put("payKey",GenKey.genKeys(CPKEY.PUBLIC_KEY, tmnList.mchtId));
					tmnMap.put("activeDate",tmnList.activeDate);
					tmnMap.put("apiMaxInstall",tmnList.apiMaxInstall);
					tmnMap.put("webPay",tmnList.webPay);
					tmnMap.put("appDirect",tmnList.appDirect);
					tmnMap.put("semiAuth",tmnList.semiAuth);
					tmnMap.put("refundType",tmnList.refundType);
					tmnMap.put("van",tmnList.van);
					tmnMap.put("vanIdx",tmnList.vanIdx);
					tmnMap.put("ccType",tmnList.ccType);
					tmnMap.put("description",eachMap.getString("description"));
					tmnMap.put("payLimit",tmnList.payLimit);
					tmnMap.put("limitAmount",tmnList.limitAmount);
					tmnMap.put("limitStartTime",tmnList.limitStartTime);
					tmnMap.put("limitEndTime",tmnList.limitEndTime);
					tmnMap.put("regId",regId);
					tmnMap.put("regDay",regDay);
					
					if(!CommonUtil.isNullOrSpace(tmnMap.getString("isDtl"))) {
						tmnLists.add(tmnMap);
					}
					if(eachMap.isEquals("isDtl", "Y")) {
						SharedMap<String, Object> tmnDtlMap = new SharedMap<String,Object>();
						tmnDtlMap.put("tmnId", tmnMap.getString("tmnId"));
						tmnDtlMap.put("name", eachMap.getString("dtlName"));
						tmnDtlMap.put("rate", eachMap.getDouble("dtlRate"));
						tmnDtlMap.put("ceoName", eachMap.getString("dtlCeoName"));
						tmnDtlMap.put("identity", eachMap.getString("dtlIdentity"));
						tmnDtlMap.put("ceoPhone", eachMap.getString("dtlCeoPhone"));
						tmnDtlMap.put("tel", eachMap.getString("dtlTel"));
						tmnDtlMap.put("email", eachMap.getString("dtlEmail"));
						tmnDtlMap.put("bankCd", eachMap.getString("dtlBankCd"));
						tmnDtlMap.put("bankName", mchtTmnDAO.getBank(tmnDtlMap.getString("bankCd")).getString("codeName"));
						tmnDtlMap.put("account", eachMap.getString("dtlAccount"));
						tmnDtlMap.put("accntHolder", eachMap.getString("dtlAccntHolder"));
						tmnDtlMap.put("zip", eachMap.getString("dtlZip"));
						tmnDtlMap.put("addr1", eachMap.getString("dtlAddr1"));
						tmnDtlMap.put("addr2", eachMap.getString("dtlAddr2"));
						tmnDtlMap.put("rctIdentity", eachMap.getString("rctIdentity"));
						tmnDtlMap.put("rctCeoName", eachMap.getString("rctCeoName"));
						tmnDtlMap.put("rctName", eachMap.getString("rctName"));
						tmnDtlMap.put("rctTelNo", eachMap.getString("rctTelNo"));
						tmnDtlMap.put("rctAddr", eachMap.getString("rctAddr"));
						tmnMap.put("regId",regId);
						tmnMap.put("regDay",regDay);
						dtlList.add(tmnDtlMap);
					}
					
				}
				if(mchtTmnDAO.insetMchtTmnBatch(tmnLists) > 0) {
					if(dtlList.size() > 0) {
						if(mchtTmnDAO.insetMchtTmnDtlBatch(dtlList) > 0) {
							resultMap.put("result", "OK");
							resultMap.put("msg", "터미널 일괄등록이 완료되었습니다.");
							return resultMap;
						}else {
							resultMap.put("result", "OK");
							resultMap.put("msg", "터미널 기본정보는 등록이 완료되었으나 추가정보 등록에 실패하였습니다.");
							return resultMap;
						}
					}else {
						resultMap.put("result", "OK");
						resultMap.put("msg", "터미널 일괄등록이 완료되었습니다.");
					}
					
				}else {
					resultMap.put("result", "NOK");
					resultMap.put("msg", "터미널 일괄등록에 실패하였습니다.");
					return resultMap;
				}
			} else {
				logger.info("터미널 일괄등록 Excel Data 이상, 확인요망!!!");
				resultMap.put("result", "NOK");
				resultMap.put("msg", "터미널 일괄등록에 실패하였습니다.");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "NOK");
			resultMap.put("msg", "터미널 일괄등록에 실패하였습니다.");
		}
		
			
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/getMcht/{mchtId}", method = RequestMethod.GET)
    public @ResponseBody SharedMap<String, Object> getMcht(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> resultMap = new SharedMap<String,Object>();
		SharedMap<String,Object> mchtMap = new MchtDAO().getById(mchtId).getRowFirst();
		if(mchtMap.isNullOrSpace("name")) {
			resultMap.put("resultCd", "XXXX");
			return resultMap;
		}
		resultMap.put("name", mchtMap.getString("name"));
		List<SharedMap<String, Object>> taxList = new MchtTaxDAO().getSelectOption(mchtId);
		if(taxList.size() == 0) {
			resultMap.put("resultCd", "9999");
			return resultMap;
		}
		StringBuffer sb= new StringBuffer();
		for(SharedMap<String, Object> tax:taxList) {
			sb.append("<option value=\""+tax.getString("id")+"\">"+tax.getString("name")+"</option>");
		}
		resultMap.put("tax", sb.toString());
        return resultMap;
    }

	@RequestMapping(value = {"/mcht/rebill/add/{mchtId}"})
	public ModelAndView rebillAdd(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();

		request.setAttribute("DATAMAP", result);

		return new ModelAndView("/mcht/rebill/add", "MCHT_MAP", result);
	}

	@RequestMapping(value = {"/mcht/rebill/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse rebillInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if(cpDAO.insertByOper("PG_MCHT_REBILL", SessionUtil.getUserId(request), cpRequest.data)){
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

	@RequestMapping(value = "/mcht/rebill/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView rebillModify(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
		SharedMap<String,Object> rebillMap = new MchtRebillDAO().getByMchtId(mchtId);
		request.setAttribute("DATAMAP", result);
		request.setAttribute("REBILLMAP", rebillMap);

		return new ModelAndView("/mcht/rebill/modify","MCHT_MAP",result);
	}

	@RequestMapping(value = {"/mcht/rebill/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse rebillUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.updateAndBackByOper("PG_MCHT_REBILL", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("정기결제 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("정기결제 정보 변경에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}

	}

	@RequestMapping(value = {"/mcht/rent/add/{mchtId}"})
	public ModelAndView rentAdd(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();

		request.setAttribute("DATAMAP", result);

		return new ModelAndView("/mcht/rent/add", "MCHT_MAP", result);
	}

	@RequestMapping(value = {"/mcht/rent/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse rentInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if(cpDAO.insertByOper("PG_MCHT_RENT", SessionUtil.getUserId(request), cpRequest.data)){
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
	@RequestMapping(value = "/mcht/rent/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView rentModify(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
		SharedMap<String,Object> rentMap = new MchtRentDAO().getByMchtId(mchtId);
		request.setAttribute("DATAMAP", result);
		request.setAttribute("RENTMAP", rentMap);

		return new ModelAndView("/mcht/rent/modify","MCHT_MAP",result);
	}

	@RequestMapping(value = {"/mcht/rent/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse rentUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.updateAndBackByOper("PG_MCHT_RENT", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("월세앱 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("월세앱 정보 변경에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}
	}
}
