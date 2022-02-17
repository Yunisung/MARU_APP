package com.pgmate.app.ctl;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
import com.pgmate.app.dao.AgencyDAO;
import com.pgmate.app.dao.AgencyMngDAO;
import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.ChargeSettleDAO;
import com.pgmate.app.dao.CodeDAO;
import com.pgmate.app.dao.DistDAO;
import com.pgmate.app.dao.FileDAO;
import com.pgmate.app.dao.HTDAO;
import com.pgmate.app.dao.LoanDAO;
import com.pgmate.app.dao.LoanSettleDAO;
import com.pgmate.app.dao.MchtChargeSettleDAO;
import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.dao.MchtDdctDAO;
import com.pgmate.app.dao.MchtDiffDAO;
import com.pgmate.app.dao.MchtFeeTemplateDAO;
import com.pgmate.app.dao.MchtInterTemplateDAO;
import com.pgmate.app.dao.MchtInterestDAO;
import com.pgmate.app.dao.MchtMngDAO;
import com.pgmate.app.dao.MchtPispDAO;
import com.pgmate.app.dao.MchtSvcDAO;
import com.pgmate.app.dao.MchtTaxDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.MchtVactDAO;
import com.pgmate.app.dao.MemberSalesDAO;
import com.pgmate.app.dao.OrgFeeDAO;
import com.pgmate.app.dao.OrgInterFeeDAO;
import com.pgmate.app.dao.PhoneDAO;
import com.pgmate.app.dao.TotCapDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.dao.VactDtlDAO;
import com.pgmate.app.dao.VanDAO;
import com.pgmate.app.dao.WalletDAO;
import com.pgmate.app.interceptor.SessionExclude;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Interest;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SQLInjectionUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.app.util.WalletAccntUtil;
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
		
		//long startTime1 = System.currentTimeMillis();
		//logger.info("FIRST TIME : {}", (startTime1 - startTime));
		
		request.setAttribute("DATATRXMAP", new TrxCapDAO().getByMchtId(mchtId, 20).getRows());
		request.setAttribute("DATATOTMAP", new TotCapDAO().getByMchtId(mchtId, 20).getRows());
		SharedMap<String, Object> svcMap = new MchtSvcDAO().getByMchtId(mchtId);
		request.setAttribute("DATASVCMAP", svcMap);
		if (svcMap.getString("virAccount").equals("사용")) {
			request.setAttribute("VACT_MAP", new MchtVactDAO().getByMchtId(mchtId));
		}
		if (svcMap.getString("pisp").equals("사용")) {
			request.setAttribute("PISP_MAP", new MchtPispDAO().getByMchtId(mchtId));
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
		
		
	//	long startTime2 = System.currentTimeMillis();
	//	logger.info("SECOND TIME : {}", (startTime2 - startTime1));
		
		request.setAttribute("HT_MAP", mchtDAO.getHtById(mchtId).getRows());
		request.setAttribute("HT_MNG_MAP", mchtMngDAO.getHtById(mchtId).getRows());
		request.setAttribute("HT_TAX_MAP", mchtTaxDAO.getHtByMchtId(mchtId).getRows());
		request.setAttribute("HT_TMN_MAP", mchtTmnDAO.getHtByMchtId(mchtId).getRows());
		
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
		// 민감 정보 암호화 
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity",cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		// 업데이트 및 히스토리 추가 
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
		loanStlMap.put("name", cpRequest.getValue("name"));
		loanStlMap.put("submitGrade", submitUserData.getString("grade"));
		loanStlMap.put("submitId", submitUserData.getString("parentId"));
		loanStlMap.put("submitName", submitUserData.getString("name"));
		loanStlMap.put("loanType", "대출실행");
		loanStlMap.put("trxDay", cpRequest.getValue("loanDay"));
		loanStlMap.put("conCnt", cpRequest.getValue("conCnt"));
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
		cpRequest.setData("ceoName", mchtData.getString("ceoName"));
		cpRequest.setData("identity", mchtData.getString("identity"));
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
        return new ModelAndView("/mcht/loanSettle/modify","DATAMAP",new LoanSettleDAO().getByLoanId(loanId));
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
	
	// KBR : 사용 안하는것 같은데..
    @RequestMapping(value = "/mcht/ht/list/{mchtId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request,@RequestBody CPRequest cpRequest, @PathVariable String mchtId) {
    	
    	SessionUtil.setSearchGrade(request, cpRequest);
    	HTDAO htDAO = new HTDAO();
    	RecordSet rset = htDAO.getUserById(mchtId, 10);
		return new CPRUtil(cpRequest).dataList(rset,htDAO).setView(request,"/mcht/ht/list","");
		
	}
    
    // KBR : Home > 가맹점 관리 > 가맹점 정보 수정(아이디 ajax 요청하여 유효성 체크) 
    @RequestMapping(value = {"/mcht/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String idCheck(HttpServletRequest request, @RequestParam("mchtId") String mchtId) {
    	
		CPDAO dao = new CPDAO();
		
		dao.setTable("PG_MCHT");
		dao.setColumns("mchtId");
		dao.addWhere("mchtId", mchtId, DAO.eq);
		
		if(!Pattern.matches("^[0-9a-zA-Z]*$", mchtId)) {
			return GsonUtil.toJson("가맹점 아이디는 영문 및 숫자만 가능합니다.");
		}
		// KBR : 띄어쓰기 체크  
		if(mchtId.indexOf(" ") > -1){
			return GsonUtil.toJson("가맹점 아이디에는 공백이 포함 될 수 없습니다.");
		}
		
		// KBR : 들어오는 값이 명령문의 일종이면 ( ex: select, delete, insert 등등 ) 
		if(!SQLInjectionUtil.checkInjectionValue(mchtId)) {
			return GsonUtil.toJson("사용할 수 없는 가맹점 아이디입니다.");
		}
		
		// KBR : 아이디가 없으면 
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
    // KBR : 가맹점 소속 수정
    @RequestMapping(value = {"/mcht/change/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse changeUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
    	
		CPDAO cpDAO = new CPDAO();
		String salesId = cpRequest.getValue("salesId");
		
		// 지사 정보 들고오기
		SharedMap<String,Object> salesMap = new MemberSalesDAO().getById(salesId).getRow(0);
		// 에이전시 정보 들고오기 
		SharedMap<String,Object> agencyMap = new AgencyDAO().getById(salesMap.getString("agencyId")).getRow(0);
		
		// data.list 공간 생성 후 저장 
		cpRequest.setData("salesId", salesId);
		cpRequest.setData("agencyId", salesMap.getString("agencyId"));
		cpRequest.setData("distId", agencyMap.getString("distId"));
		
		/* 소속 변경시 수수료 재계산 로직 삭제 
		// 매입 내역 정보 리스트 들고오기
		cpDAO.setTable("VW_TRX_CAP");
		cpDAO.setColumns("COUNT(*) as cnt");
		cpDAO.setWhere(" capId IN (SELECT capId FROM PG_TRX_CAP WHERE mchtId = '"+ cpRequest.getKeyValue("mchtId") +"') ");
		
		// 대행사
		cpDAO.addWhere("stlDistId", "",CPDAO.eq);
		// 에이전시
		cpDAO.addWhere("stlAgencyId", "",CPDAO.eq);
		// 지사
		cpDAO.addWhere("stlSalesId", "",CPDAO.eq);
		
		// 리스트 갯수 가져오기 
		long trxCapCnt =  cpDAO.search().getRowFirst().getLong("cnt");
		
		logger.debug("CHANGE TRX CAP CNT : {}", trxCapCnt);
		
		if(trxCapCnt > 0) {
			// 리스트가 없을 경우 
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
			// 매입건이 있을경우 추가 작업 
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
    
    // B : 지불 및 정산 체킹
    @RequestMapping(value = {"/mcht/mng/check/{mchtId}"})
    public @ResponseBody SharedMap<String, Object> distMngCheck(HttpServletRequest request, @PathVariable String mchtId) {
    	
    	SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
    	// 가맹점 정보 셋팅
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	
    	// 에이전시
    	AgencyMngDAO agencyDAO = new AgencyMngDAO();
    	// 대행사
    	DistDAO DistDAO = new DistDAO();
    	
    	// KBR : 복호호 불필요
    	agencyDAO.setTable("PG_MAM_AGENCY_MNG");
		agencyDAO.setColumns("count(1) as cnt");
		agencyDAO.addWhere("agencyId", result.get("agencyId").toString(), DAO.eq);

		// B : result 로 불로온 가맹점 정보에 agencyId가 존재하는지 여부 확인  
		//에이전시 관리정보 체크		
		if(agencyDAO.search().getRowFirst().getInt("cnt") == 1) {
			resultMap.put("agencyRes", "OK");
		} else {
			resultMap.put("agencyRes", "NOK");
			resultMap.put("agencyMsg", "에이전시");
		}
		
		//대행사 관리정보 체크
		DistDAO.setTable("PG_MAM_DIST_MNG");
		DistDAO.setColumns("count(1) as cnt");
		DistDAO.addWhere("distId", result.get("distId").toString(), DAO.eq);
		
		if(DistDAO.search().getRowFirst().getInt("cnt") == 1) {
			resultMap.put("distRes", "OK");
		} else {
			resultMap.put("distRes", "NOK");
			resultMap.put("distMsg", "대행사");
		}
		
		return resultMap;
    }
    
    // ======================================================= 관리정보 
    // B : 정산정보 등록 or 추가 ( 가맹점 등록 후 바로 맵핑)
    @RequestMapping(value = {"/mcht/mng/add/{mchtId}"})
    public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String mchtId) {
    	
    	// 가맹점 정보 
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	// 해당 id의 에이전시 정보
    	SharedMap<String,Object> agencyMngMap = new AgencyMngDAO().getById(result.getString("agencyId")).getRowFirst();
    	
    	request.setAttribute("DATADISTMNGMAP", agencyMngMap);
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
    	request.setAttribute("DISTRATE_OPTION", new DistDAO().getRateOption(result.getString("distId")));
    	request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
    	request.setAttribute("FEE_TEMPLATE", new MchtFeeTemplateDAO().getFeeTemplete().getRows());
    	request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
    	
    	// 영중소 체크 
    	String diffType = new MchtDiffDAO().getDiffType(mchtId).getRowFirst().getString("mchtType");
    	
    	// 영중소 체크 후 빈값이면 일반으로 등록 
    	if(CommonUtil.isNullOrSpace(diffType)) diffType = "일반";
    	
    	request.setAttribute("DIFFTYPE", diffType);
    	
    	// 상위 대행사가 없으면 
    	if(result.isEquals("distId", "00")) {
    		// (선정산 가맹점용) add 
    		return new ModelAndView("/mcht/mng/fact/add", "DATAMAP", result);
    	} else {
    		return new ModelAndView("/mcht/mng/add", "DATAMAP", result);
    	}
    }
    // B : 정산정보 수정
    @RequestMapping(value = "/mcht/mng/modify/{mchtId}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String mchtId) {
    	
    	// 클릭 한 가맹점 정보 가져오기
    	SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
    	
    	// 연계 에이전시 정보
    	request.setAttribute("DATADISTMNGMAP", new AgencyMngDAO().getById(result.getString("agencyId")).getRowFirst());
    	// 등록 가능 은행 정보 가져오기
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
    	// 연계 대행사 정보
    	request.setAttribute("DISTRATE_OPTION", new DistDAO().getRateOption(result.getString("distId")));
    	// 등록된 가맹점 수수료 템플릿 정보 가져오기
    	request.setAttribute("FEE_TEMPLATE", new MchtFeeTemplateDAO().getFeeTemplete().getRows());
    	request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
    	
    	// 대행사 idx가 00 인것 ( 즉, 대행사가 없을 때 )
    	if(result.isEquals("distId", "00")) {
    		return new ModelAndView("/mcht/mng/fact/modify", "DATAMAP", new MchtMngDAO().getById(mchtId).getRowFirst());
    	} else {
    		return new ModelAndView("/mcht/mng/modify", "DATAMAP", new MchtMngDAO().getById(mchtId).getRowFirst());
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
		
		if(cpDAO.insert("PG_MCHT_MNG", SessionUtil.getUserId(request), cpRequest.data)){
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
	// 지불 및 정산정보 submit 클릭 시
	@RequestMapping(value = {"/mcht/mng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		// 디버그 셋팅 
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.updateAndBack("PG_MCHT_MNG", SessionUtil.getUserId(request), cpRequest.data)){
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
	/*@RequestMapping(value = "/mcht/noti/list", method = RequestMethod.POST)
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
		
		if(cpDAO.updateAndBack("PG_MCHT_WEBHOOK", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("가맹점 노티 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("가맹점 노티 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/noti/idCheck", method = RequestMethod.POST)
	public @ResponseBody String notiCheck(HttpServletRequest request, @RequestParam("id") String id, @RequestParam("idType") String idType) {
		
		CPDAO dao = new CPDAO();
		
		if(id.indexOf(" ") > -1){
			return GsonUtil.toJson("아이디에는 공백이 포함 될 수 없습니다.");
		}
		
		logger.info("idType : " + idType + ", id : " + id);
		
		if(idType == "distId") {
			dao.setTable("PG_MAM_DIST");
			dao.setColumns("distId");
			dao.addWhere("distId", id, DAO.eq);
			
		}else if(idType == "agencyId") {
			dao.setTable("PG_MAM_AGENCY");
			dao.setColumns("agencyId");
			dao.addWhere("agencyId", id, DAO.eq);
			
		}else if(idType == "salesId") {
			dao.setTable("PG_MAM_SALES");
			dao.setColumns("salesId");
			dao.addWhere("salesId", id, DAO.eq);
			
		}else if(idType == "mchtId") {
			dao.setTable("PG_MCHT");
			dao.setColumns("mchtId");
			dao.addWhere("mchtId", id, DAO.eq);
			
		}else if(idType == "tmnId") {
			dao.setTable("PG_MCHT_TMN");
			dao.setColumns("tmnId");
			dao.addWhere("tmnId", id, DAO.eq);
		}
		
		if(dao.search().size() == 0){
			return GsonUtil.toJson("해당 아이디가 존재하지 않습니다.");
		}else if(dao.search().size() == 1) {
				return GsonUtil.toJson("true");
		}
		
		return GsonUtil.toJson("해당 아이디가 존재하지 않습니다.");
	}
	
	@RequestMapping(value = "/mcht/noti/tpCheck", method = RequestMethod.POST)
	public @ResponseBody Object notiType(HttpServletRequest request, @RequestParam("ckId") String id, @RequestParam("ckType") String idType) {
	
		String getId = "";
		
			 if ("distId".equals(idType)) {
				 id = id + "D";
				 getId = new MchtDAO().getIdByType(id).getRowFirst().getString("distId");
				 
			 } else if ("agencyId".equals(idType)) {
				 id = id + "A";
				 getId = new MchtDAO().getIdByType(id).getRowFirst().getString("agencyId");
				 
			 } else if ("salesId".equals(idType)) {
				 id = id + "S";
				 getId = new MchtDAO().getIdByType(id).getRowFirst().getString("salesId");
				 
			 } else if ("mchtId".equals(idType)) {
				 id = id + "M";
				 getId = new MchtDAO().getIdByType(id).getRowFirst().getString("mchtId");
				 
			 } else if ("tmnId".equals(idType)) {
				 id = id + "T";
				 getId = new MchtDAO().getIdByType(id).getRowFirst().getString("tmnId");
			 }
			 
		if(CommonUtil.isNullOrSpace(getId)) {
			getId = "존재하지 않는 가맹점 정보 입니다.";
		}
		SharedMap<String, Object> map = new SharedMap<String, Object>();
		map.put("getId", getId);
		return map;
	}*/
	//======================================================== 노티 정보
	
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
	 // B : 터미널 등록 클릭 시
	@RequestMapping(value = {"/mcht/tmn/add/{mchtId}"})
	public ModelAndView tmnAdd(HttpServletRequest request, @PathVariable String mchtId) {
		
		// 해당 가맹점 정보 
		SharedMap<String,Object> result = new MchtDAO().getById(mchtId).getRowFirst();
		
		// 새로 생성 할 터미널 아이디 
		String tmnId = new MchtTmnDAO().getNewId();
		
		// 터미널 아이디 생성
		result.put("tmnId", tmnId);
		// 일련번호 생성
		result.put("serial", tmnId.replace("TMN", ""));
		// 온라인 결제 key값 생성  
		result.put("payKey", GenKey.genKeys(CPKEY.PUBLIC_KEY, mchtId));
		// tax에 등록된 결제 정보 (taxID 와  taxName 정보 들고옴) 
		request.setAttribute("DATATAXMAP", new MchtTaxDAO().getSelectOption(mchtId));
		// 벤 리스트 정보(KSPAY5,KGMOBIL4 등등의 리스트 현재는 2종류 밖에 없음)
		request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
		
		return new ModelAndView("/mcht/tmn/add", "DATAMAP", result);
	}
    
	// 터미널 조회 클릭 시 
    @RequestMapping(value = "/mcht/tmn/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView tmnList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
    	
		MchtTmnDAO vanDAO = new MchtTmnDAO();
		
		SessionUtil.setSearchGrade(request, cpRequest);
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/tmn/list","");
	}
    // 터미널 기본정보 클릭 시 
    @RequestMapping(value = "/mcht/tmn/modify/{tmnId}", method = RequestMethod.GET)
    public ModelAndView tmnModify(HttpServletRequest request, @PathVariable String tmnId) {
    	
    	// 가맹점 터미널 정보
    	SharedMap<String,Object> result = new MchtTmnDAO().getById(tmnId).getRowFirst(); 
    	
    	// tax 정보 
    	request.setAttribute("DATATAXMAP", new MchtTaxDAO().getSelectOption(result.getString("mchtId")));
    	// van 리스트 
    	request.setAttribute("VANMAP", new VanDAO().vanList().getRows());
    	
    	//KJM : 터미널 정보 수정 > van 리스트 나오게 기존 코드 주석 처리 후 코드 추가 (L.686~696)
    	String van = result.getString("van");
    	request.setAttribute("VANIDMAP", new VanDAO().getVan(van).getRows());
    	
		/*
		 * // DANAL or NICE (현재는 해당 van 명칭이 없음)
		 * if(result.getString("van").equals("DANAL")) {
		 * System.out.println("danal van :: " + new VanDAO().danalVanId().getRows());
		 * request.setAttribute("VANIDMAP", new VanDAO().danalVanId().getRows()); } else
		 * { System.out.println("nice van :: " + new VanDAO().niceVanId().getRows());
		 * request.setAttribute("VANIDMAP", new VanDAO().niceVanId().getRows()); }
		 */
		 
    	// 해당 tmnId 매입내역 리스트 
    	RecordSet rset = new TrxCapDAO().getByTmnId(tmnId);
    	
    	if(rset.size() > 0) {
    		// 매입내역이 있으면 터미널 아이디 변경 불가능하게 만듦
    		request.setAttribute("HasTransaction", "true");
    	} else {
    		request.setAttribute("HasTransaction", "false");
    	}
        return new ModelAndView("/mcht/tmn/modify","DATAMAP",result);
    }
    // KBR : 터미널 등록 submit 시VANMAP
    @RequestMapping(value = {"/mcht/tmn/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse tmnInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
    	
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.insert("PG_MCHT_TMN", SessionUtil.getUserId(request), cpRequest.data)){
			// 이걸 왜 해주는지 모르겠네.....
			new VanDAO().updateUsed(cpRequest.getValue("vanIdx"));
			// 변경값으로 세션값 초기화
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
	
    // 터미널 수정 submit 시 
	@RequestMapping(value = {"/mcht/tmn/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse tmnUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		
		if(cpRequest.getValue("status").equals("폐기")) {
			CPDAO dao = new CPDAO();
			dao.setTable("VW_TRX_CAP");
			dao.setColumns("COUNT(*) AS CNT");
			dao.addWhere("tmnId", cpRequest.getKeyValue("tmnId"),CPDAO.eq);
			
			SharedMap<String,Object> resMap = dao.search().getRow(0);
			
			// 해당 터미널이 매입내역이 없는경우 
			if(resMap.getLong("CNT") <= 0) {
				// 삭제 성공 시 
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
			
			// KBR : 수정됐다면 key값이 false로 처리됨 > 그래서 null이 아닐 경우 수정됐다는 의미 
			if(cpRequest.getData("tmnId") != null) {
				// 수정된 값 셋팅
				tmnId = (String)cpRequest.getData("tmnId").val;
			}
			
			// 수정된값으로 셋팅 됐으면
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
		
		
		if(cpDAO.updateAndBack("PG_MCHT_TMN", SessionUtil.getUserId(request), cpRequest.data)){
			// 터미널 아이디 수정됐을 경우 
			if(!cpRequest.getValue("tmnId").equals(cpRequest.getKeyValue("tmnId"))) {
				// HT_TMNID_CLOG : 터미널 아이디 변경 이력 테이블
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
	
	// KBR : 터미널 아이디 유효성 체크
	@RequestMapping(value = {"/mcht/tmn/idCheck"}, method = RequestMethod.POST)
    public @ResponseBody String tmnIdCheck(HttpServletRequest request, @RequestParam("tmnId") String tmnId) {
		
		CPDAO dao = new CPDAO();
		dao.setTable("PG_MCHT_TMN");
		dao.setColumns("tmnId");
		dao.addWhere("tmnId", tmnId, DAO.eq);
		
		
		if(tmnId.indexOf(" ") > -1){
			return GsonUtil.toJson("터미널 아이디에는 공백이 포함 될 수 없습니다.");
		}
		// 아이디가 사용가능하다면 
		if(dao.search().size() == 0){
			// 현재 가입된 userID중 중복된값 있는지 체크
			// SELECT id FROM PG_USER WHERE  id = '?' ORDER BY regDate DESC
			dao.initRecord();
			dao.setTable("PG_USER");
			dao.setColumns("id");
			dao.addWhere("id", tmnId, DAO.eq);
			// 모두 없다면 사용 true
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
	
	// KBR : 터미널 추가정보 클릭 시 
	@RequestMapping(value = {"/mcht/tmnDtl/add/{tmnId}"})
	public ModelAndView tmnDtlAdd(HttpServletRequest request, @PathVariable String tmnId) {
		// 클릭한 터미널 아이디 모든 정보 가져오기
		SharedMap<String,Object> result = new MchtTmnDAO().getById(tmnId).getRowFirst();
		request.setAttribute("MCHTMAP", new MchtDAO().getById(result.getString("mchtId")).getRowFirst());
		
		// 은행 정보 (해당 가맹점 tax 정보)
		request.setAttribute("TAXMAP", new MchtTaxDAO().getById(result.getString("taxId")).getRowFirst());
		// 은행 리스트 (부산은행,광주은행 ... )
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		
		return new ModelAndView("/mcht/tmnDtl/add", "DATAMAP", result);
	}
	// 터미널 추가 정보 클릭 시 
	@RequestMapping(value = {"/mcht/tmnDtl/modify/{tmnId}"})
	public ModelAndView tmnDtlModify(HttpServletRequest request, @PathVariable String tmnId) {
		
		
		SharedMap<String,Object> result = new MchtTmnDAO().getById(tmnId).getRowFirst();
		request.setAttribute("MCHTMAP", new MchtDAO().getById(result.getString("mchtId")).getRowFirst());
		request.setAttribute("TAXMAP", new MchtTaxDAO().getById(result.getString("taxId")).getRowFirst());
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/mcht/tmnDtl/modify", "DATAMAP", result);
	}
	
	// KBR : 터미널 추가 정보 등록 submit 시
	@RequestMapping(value = {"/mcht/tmnDtl/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse tmnDtlInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.insert("PG_MCHT_TMN_DTL", SessionUtil.getUserId(request), cpRequest.data)){
			// 변경값으로 세션 초기화 
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
		if(cpDAO.updateAndBack("PG_MCHT_TMN_DTL", SessionUtil.getUserId(request), cpRequest.data)){
			
			logger.debug("tmnId [{}]",cpRequest.getKeyValue("tmnId"));
			logger.debug("bankCd [{}]",cpRequest.getValue("bankCd"));
			logger.debug("account [{}]",cpRequest.getValue("account"));
			
			WalletDAO walletDAO = new WalletDAO();
			
			// 분리정산 터미널 여부 확인
			SharedMap<String, Object> walletMap = walletDAO.getWalletByTmnId(cpRequest.getKeyValue("tmnId"));
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
			}
			
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
	
	// KBR : 등록된 텍스 클릭 시 ajax 통신 
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
    	request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
        return new ModelAndView("/mcht/tax/modify","DATAMAP",new MchtTaxDAO().getById(taxId).getRowFirst());
    }
    // 택스 정보 추가 시 
    @RequestMapping(value = {"/mcht/tax/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse taxInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		
		if(cpDAO.insert("PG_MCHT_TAX", SessionUtil.getUserId(request), cpRequest.data)){
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
		//identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));
		if(cpDAO.updateAndBack("PG_MCHT_TAX", SessionUtil.getUserId(request), cpRequest.data)){
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
	
	 // tax 정보 수정 시 사용 상태 체크 
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
	
	// 미실적 가맹점 조회 클릭 시 
	@RequestMapping(value = {"/mcht/nontran/form"})
    public ModelAndView nonform(HttpServletRequest request) {
        return new ModelAndView("/mcht/nontran/form");
    }
	
	// 미실적 가맹점 > 리스트요청 및 엑셀 다운로드 시 
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
	// B : 선정산 등록 클릭 시 ( DB에 테이블이 없어서 데이터가 안나옴 )
	@RequestMapping(value= {"/mcht/loan/add/{mchtId}"})
	public ModelAndView mchtLoanAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("mchtId", mchtId);
		request.setAttribute("DATALIST", new LoanDAO().search().getRows());
		return new ModelAndView("/mcht/selectLoan");
	}
	
	// ======================================================= VAN 정보
	// KBR : VAN ID 조회 클릭 시 
	@RequestMapping(value = "/mcht/van/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		VanDAO vanDAO = new VanDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		// KBR : 검색 값 체크 후 select문 완성 및 list 값 
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/van/list","");
	}
	// van ID 조회 > van 리스트 클릭 시 
    @RequestMapping(value = "/mcht/van/modify/{vanid}", method = RequestMethod.GET)
    public ModelAndView vanModify(HttpServletRequest request, @PathVariable String vanid) {
    	SharedMap<String,Object> result = new VanDAO().getByVanId(vanid).getRowFirst();
        return new ModelAndView("/mcht/van/modify","DATAMAP",result);
    }
    
    // van ID 생성 submit 시 
    @RequestMapping(value = {"/mcht/van/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse vanInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
    	
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insert("PG_VAN", SessionUtil.getUserId(request), cpRequest.data)){
			// 변경된 값 세션 초기화
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
	// vanid 조회 > 리스트 클릭 > submit 클릭 시 
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
	//  van 수수료 조회 클릭 시
	@RequestMapping(value = "/mcht/van/fee/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanFeeList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		OrgFeeDAO vanDAO = new OrgFeeDAO();
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/van/fee/list","");
	}
	
	//KJM : 조회 조건 > VAN 입력 시
	/**
	 * KBR : VAN selected 하여 ajax 요청 
	 * SELECT idx, name,vanId,status FROM PG_VAN WHERE  van = '?' ORDER BY idx asc
	 * **/ 
	@RequestMapping(value= {"/mcht/van/select/{van}"}, method = RequestMethod.GET)
	public @ResponseBody String vanIdSelect(HttpServletRequest request, @PathVariable String van) {
		
		VanDAO vanDAO = new VanDAO();
		
		List<SharedMap<String, Object>> resMap = new ArrayList<SharedMap<String, Object>>();
		
		//KJM : 선택한 van에 대한 van id 리스트 가져옴
		RecordSet rset = vanDAO.getVan(van);
		//KJM : 조회된 리스트가 있을 경우
		if(rset.size() > 0){
			//KJM : recordset형식으로 되어있는 데이터 리스트 형식으로 넣어준다
			resMap = rset.getRows();
		}
		
		
		//KJM : 리스트를 json형태로 변환시킨 뒤 반환해준다
		return GsonUtil.toJson(resMap);
	}
	
	// nav > 서류관리 > 파일 등록 클릭 시 
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
	// nav > 서류관리 > 본사뷰 권한으로 설정 클릭 시 
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
	 // KBR : 서비스 수정 탭 > submit 클릭 시 
	 @RequestMapping(value = {"/mcht/svc/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody CPResponse svcUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		 
			CPDAO cpDAO = new CPDAO();
			
			//identity 입력시 암호화하여 넣어야함.
			if(cpDAO.update("PG_MCHT_SVC", SessionUtil.getUserId(request), cpRequest.data)){
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
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
		return new ModelAndView("/mcht/vact/add", "DATAMAP", new MchtVactDAO().getByMchtId(mchtId));
	}

	@RequestMapping(value = {"/mcht/vact/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse vactInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.insert("PG_MCHT_MNG_VACT", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 가상계좌 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest)
		        		.resultNOK("가맹점 가상계좌 정보 변경에 실패하였습니다.",cpDAO.getError())
		        		.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/vact/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView vactModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATASVCMAP", new MchtSvcDAO().getByMchtId(mchtId));
	  return new ModelAndView("/mcht/vact/modify","DATAMAP",new MchtVactDAO().getByMchtId(mchtId));
	}
	 
	@RequestMapping(value = {"/mcht/vact/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse vactUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.update("PG_MCHT_MNG_VACT", SessionUtil.getUserId(request), cpRequest.data)) {
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
		request.setAttribute("UNUSED_ACCNT_MAP", dao.search().getRows());

		return new ModelAndView("/mcht/vact/issue", "DATAMAP", vartDtlList);
	}
	// 가상계좌 발급 클릭 시 
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
		request.setAttribute("UNUSED_ACCNT_MAP", dao.search().getRows());
		//return new ModelAndView("/mcht/vact/issue/list", "DATAMAP", vartDtlList);
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
		resMap.put("result", "NOK");

		DAO dao = new DAO();
		dao.setTable("VW_VACT_UNUSED");
		dao.setColumns("COUNT(*) as cnt");
		dao.addWhere("bankCd", bankCd, DAO.eq);
		long orgCnt = dao.search().getRow(0).getLong("cnt");
		logger.debug("{} UNUSED VACT CNT: {}, {}", bankCd, orgCnt, cnt);
		if (cnt > orgCnt) {
			resMap.put("msg", "보유 계좌가 요청 계좌보다 적습니다.");
		} else if(new VactDtlDAO().insert(mchtId, bankCd, cnt, holderName, SessionUtil.getUserId(request)) < 1) {
			resMap.put("msg", "DB 작업에 실패했습니다.관리자에게 문의해주세요.");
		} else {
			dao.initRecord();
			logger.debug("UPDATE PG_MCHT_MNG_VACT {}:", dao.update("UPDATE PG_MCHT_MNG_VACT A LEFT JOIN (SELECT COUNT(*) as cnt, mchtId FROM PG_VACT_DTL GROUP BY mchtId) B ON A.mchtId = B.mchtId SET A.quantity = B.cnt WHERE A.mchtId ='"+mchtId+"' "));
			resMap.put("result", "OK");
		}

		return resMap;
	}
	
	
	// 지급대행 > 지급이체 설정 클릭 시 
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
		return new ModelAndView("/mcht/pisp/add", "DATAMAP", new MchtPispDAO().getByMchtId(mchtId));
	} 

	@RequestMapping(value = {"/mcht/pisp/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse pispInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		SharedMap<String,Object> map = new MchtPispDAO().setPispAccount(cpRequest.getKeyValue("mchtId"));
		//가상계좌 낑겨 넣기
		cpRequest.setData("bankCd", "020");
		cpRequest.setData("account",map.getString("account"));
		cpRequest.setData("issueId",map.getString("issueId"));
		
		
		
		if (cpDAO.insert("PG_MCHT_MNG_PISP", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("지급이체대행 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest)
		        		.resultNOK("지급이체대행 정보 변경에 실패하였습니다.",cpDAO.getError())
		        		.cpResponse();
		} 
	}
	// 지급대행 생성 클릭 시 
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
	// B : 영중소 가맹점 정보 등록 클릭 시 정보 가져오기 
	@RequestMapping(value = "/mcht/diff/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView diffAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDiffDAO().getMcht(mchtId).getRowFirst());
		return new ModelAndView("/mcht/diff/add");
	}
	
	// KBR : 영중소 가맹점 정보 등록 submit 시 
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
	
	// KBR : 영중소 정보 수정 클릭 시 
	@RequestMapping(value = "/mcht/diff/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView diffModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDiffDAO().getDiffUpload(mchtId).getRowFirst());
		return new ModelAndView("/mcht/diff/modify");
	}
	
	// B : 영중소 submit 시  (상위 insert 메소드랑 똑같
	@RequestMapping(value = {"/mcht/diff/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse diffUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		// B : 사업자번호(identity)입력시 암호화하여 넣어야함.
		if (cpDAO.update("PG_MCHT_DIFF_UPLOAD", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("차액정산 가맹점 정보가 수정되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("차액정산 가맹점 정보 수정에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	// 영중소 가맹점 대상 확인 클릭 시 
	@RequestMapping(value = "/mcht/diffType/{identity}", method = RequestMethod.GET)
	public @ResponseBody Object diffType(HttpServletRequest request, @PathVariable String identity) {
		String diffType = new MchtDiffDAO().getDiffTypeByIdentity(identity).getRowFirst().getString("mchtType");
    	if(CommonUtil.isNullOrSpace(diffType)) diffType = "일반";
    	SharedMap<String, Object> map = new SharedMap<String, Object>();
		map.put("diffType", diffType);
		return map;
	}
	// 가맹점 수수료 템플릿 클릭 시 
	@RequestMapping(value = "/mcht/feeTemplate/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtFeeTemplateList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		MchtFeeTemplateDAO feeDAO = new MchtFeeTemplateDAO();
		RecordSet rset = feeDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,feeDAO).setView(request,"/mcht/feeTemplate/list","");
	}
	// 가맹점 수수료 템플릿 > 신규 템플릿 생성 submit 시
	@RequestMapping(value = {"/mcht/feeTemplate/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse mchtFeeTemplateInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		CPDAO cpDAO = new CPDAO();
				
		if (cpDAO.insert("PG_MCHT_FEE_TEMPLATE", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 수수료 템플릿이 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 수수료 템플릿 등록에 실패하였습니다.",cpDAO.getError() ).cpResponse();
		}
    }
	// 가맹점 수수료 템플릿 리스트 클릭 시 
	@RequestMapping(value = "/mcht/feeTemplate/modify/{idx}", method = RequestMethod.GET)
	public ModelAndView mchtFeeTemplateModify(HttpServletRequest request, @PathVariable String idx) {
		request.setAttribute("DATAMAP", new MchtFeeTemplateDAO().getFeeTemplate(idx).getRowFirst());
		return new ModelAndView("/mcht/feeTemplate/modify");
	}
	// 가맹점 수수료 템플릿 리스트 수정 클릭 시 
	@RequestMapping(value = {"/mcht/feeTemplate/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse mchtFeeTemplateUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.update("PG_MCHT_FEE_TEMPLATE", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 수수료 템플릿이 수정되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 수수료 템플릿 수정에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	// B : 지불 및 정산정보 수정 > 수수료 템플릿 리스트 ajax요청 시
	@RequestMapping(value = "/mcht/feeTemplate/get/{idx}", method = RequestMethod.GET)
	public @ResponseBody Object mchtFeeTemplateGet(HttpServletRequest request, @PathVariable String idx) {
		// 가맹점 수수료 리스트 정보 리턴 
		return new MchtFeeTemplateDAO().getFeeTemplate(idx).getRowFirst();
	}
	
	// B : nav > 차감정산 생성 클릭 시  
	@RequestMapping(value = "/mcht/ddct/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView ddctAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHT_MAP", new MchtDAO().getById(mchtId).getRowFirst());
		// PG_CODE 테이블에서  alias가'DDCT'로 돼 있는 항목만  리스트로 들고옴
		request.setAttribute("DDCTCODE", new MchtDdctDAO().getCode().getRows());
		
		return new ModelAndView("/mcht/ddct/add");
	}
	// B : 차감정산 등록 submit 시 
	@RequestMapping(value = "/mcht/ddct/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse ddctInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		SharedMap<String, Object> ddctMap = new SharedMap<String, Object>();
		ddctMap.put("ddctId",MchtDdctDAO.getDdctId());
		ddctMap.put("mchtId",cpRequest.getValue("mchtId"));
		ddctMap.put("type",cpRequest.getValue("type"));
		// 정기 차감 금액
		ddctMap.put("monthlyAmt",cpRequest.getValue("monthlyAmt"));
		ddctMap.put("startMonth",cpRequest.getValue("startMonth"));
		ddctMap.put("endMonth",cpRequest.getValue("endMonth"));
		ddctMap.put("status","진행");
		// 정산 일자 
		ddctMap.put("settleType",cpRequest.getValue("settleType"));
		ddctMap.put("regId",regId);
		ddctMap.put("regDay",regDay);
		
		// 차감정산 테이블 생성
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		
		// 시작일  ~ 종료일 담기 
		// ex ) 9 월 ~ 11월이면 202109,202110,202111 
		List<String> monthList = CommonUtil.getMonthList(ddctMap.getString("startMonth"), ddctMap.getString("endMonth"));
		// 총차감금액 셋팅
		ddctMap.put("totalAmt", ddctMap.getLong("monthlyAmt") * monthList.size());
		
		
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		// PG_SETTLE_DDCT = 차감정산 스케쥴 테이블
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
				// 정산일자 M+{day}로 String값 공백으로 바꾸고 나머지 정산일자 int 값 셋팅
				int term = CommonUtil.parseInt(ddctMap.getString("settleType").replaceAll("M[+]", ""));
				// 휴일 여부 체크 후 가능 날짜 셋팅
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
				
				// addBatch() : 쿼리 실행을 하지 않고 쿼리 구문을 메모리에 올려두었다가, 실행 명령이 있으면 한번에 DB쪽으로 쿼리를 날린다. ( insert시 동작 빠름)
				pstmt.addBatch();
				
				// 실행 안되는듯 함
				if(++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			
			// 몇번의 insert가 들어갔는지 확인
			inserted +=pstmt.executeBatch().length;
			// 
			if(inserted > 0) {
				// 가맹점 차감정산 스케쥴 테이블 값 셋팅
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
	
	//차감정산 리스트 클릭 시 
	@RequestMapping(value = "/mcht/ddct/modify/{ddctId}", method = RequestMethod.GET)
	public ModelAndView ddctModify(HttpServletRequest request, @PathVariable String ddctId) {
		
		// 해당 ddctId를 가진 업체의 차감정산 관련 모든 정보
		SharedMap<String, Object> ddctMap = new MchtDdctDAO().getDdctId(ddctId).getRowFirst();
		
		// 단말기통신비,유심비 등등 리스트
		request.setAttribute("DDCTCODE", new MchtDdctDAO().getCode().getRows());
		
		request.setAttribute("DATAMAP", ddctMap);
		// 해당 ddctId의 모든 차감정산 정보리스트
		request.setAttribute("SCHELIST", new MchtDdctDAO().getSchedule(ddctId).getRows());
		// 차감정산 시작월 
		request.setAttribute("SETDATE", new MchtDdctDAO().getScheduleDate(ddctId).getRowFirst());
		
		CPSession session = SessionUtil.get(request);
		
		if(!session.getGrade().equals("본사") || session.getRole().equals("일반")) {
			return new ModelAndView("/mcht/ddct/view");
		}
		// 차감정산 status 종류 2가지 (진행, 종료) 
		if(ddctMap.getString("status").equals("종료")) {
			return new ModelAndView("/mcht/ddct/view");
		}else {
			return new ModelAndView("/mcht/ddct/modify");
		}
	}
	
	// 차감정산 수정 > 수정 클릭 시
	@RequestMapping(value = "/mcht/ddct/update", method = RequestMethod.POST)
	public @ResponseBody Object ddctUpdate(HttpServletRequest request) {
		
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		SharedMap<String, Object> ddctMap = new SharedMap<String, Object>();
		ddctMap.put("ddctId",request.getParameter("ddctId"));
		ddctMap.put("mchtId",request.getParameter("mchtId"));
		ddctMap.put("type",request.getParameter("type"));
		ddctMap.put("monthlyAmt",request.getParameter("monthlyAmt"));
		ddctMap.put("startMonth",request.getParameter("startMonth"));
		ddctMap.put("endMonth",request.getParameter("endMonth"));
		ddctMap.put("settleType",request.getParameter("settleType"));
		ddctMap.put("regId",regId);
		ddctMap.put("regDay",regDay);
		
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		// 차감정산 시작월 가져오기
		String startMonth = ddctDAO.getStartMonth(ddctMap.getString("ddctId"));
		// 차감정산 시작월 ~ 종료월 가져오기
		List<String> monthList = CommonUtil.getMonthList(startMonth, ddctMap.getString("endMonth"));
		// 총 차감금액 
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
		endMap.put("ddctAmt", request.getParameter("ddctAmt"));
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
	// 차감정산 수정탭 > 삭제 클릭 시
	@RequestMapping(value = "/mcht/ddct/delete", method = RequestMethod.POST)
	public @ResponseBody Object ddctDelete(HttpServletRequest request) {
		
		MchtDdctDAO ddctDAO = new MchtDdctDAO();
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		
		// 삭제 성공 시
		if(ddctDAO.ddctDelete(request.getParameter("ddctId"))){
			resultMap.put("resultCd", "0000");
		// 삭제 실패 시
		}else {
			resultMap.put("resultCd", "9999");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/mcht/ddct/change/{scheId}", method = RequestMethod.GET)
    public ModelAndView ddctChange(HttpServletRequest request, @PathVariable String scheId) {
		request.setAttribute("DATAMAP", new MchtDdctDAO().getScheduleByScheId(scheId));
		return new ModelAndView("/mcht/ddct/change");
	}
	
	// 차감일정 개별 수정 
	@RequestMapping(value = "/mcht/ddct/scheUpdate", method = RequestMethod.POST)
	public @ResponseBody Object scheUpdate(HttpServletRequest request) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		SharedMap<String, Object> map = new SharedMap<String, Object>();
		map.put("scheId", request.getParameter("scheId"));
		map.put("ddctId", request.getParameter("ddctId"));
		map.put("stlDay", request.getParameter("stlDay"));
		map.put("ddctAmt", request.getParameter("ddctAmt"));
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
	// 상점 부담 무이자 등록 클릭 시 
	@RequestMapping(value = "/mcht/inter/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView interAdd(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("DATAMAP", new MchtDAO().getById(mchtId).getRowFirst());
		// 현재 mchtId의 상점 부담 무이자 등록된 값
		request.setAttribute("DATAACQLIST", new MchtInterestDAO().acquirerList().getRows());
		// 현재 등록 돼 있는 템플릿 리스트 
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
				pstmt.setDouble(i++, map.getDouble("m02"));
				pstmt.setDouble(i++, map.getDouble("m03"));
				pstmt.setDouble(i++, map.getDouble("m04"));
				pstmt.setDouble(i++, map.getDouble("m05"));
				pstmt.setDouble(i++, map.getDouble("m06"));
				pstmt.setDouble(i++, map.getDouble("m07"));
				pstmt.setDouble(i++, map.getDouble("m08"));
				pstmt.setDouble(i++, map.getDouble("m09"));
				pstmt.setDouble(i++, map.getDouble("m10"));
				pstmt.setDouble(i++, map.getDouble("m11"));
				pstmt.setDouble(i++, map.getDouble("m12"));
				pstmt.setDouble(i++, map.getDouble("m13"));
				pstmt.setDouble(i++, map.getDouble("m14"));
				pstmt.setDouble(i++, map.getDouble("m15"));
				pstmt.setDouble(i++, map.getDouble("m16"));
				pstmt.setDouble(i++, map.getDouble("m17"));
				pstmt.setDouble(i++, map.getDouble("m18"));
				pstmt.setDouble(i++, map.getDouble("m19"));
				pstmt.setDouble(i++, map.getDouble("m20"));
				pstmt.setDouble(i++, map.getDouble("m21"));
				pstmt.setDouble(i++, map.getDouble("m22"));
				pstmt.setDouble(i++, map.getDouble("m23"));
				pstmt.setDouble(i++, map.getDouble("m24"));
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
				pstmt.setDouble(i++, map.getDouble("m02"));
				pstmt.setDouble(i++, map.getDouble("m03"));
				pstmt.setDouble(i++, map.getDouble("m04"));
				pstmt.setDouble(i++, map.getDouble("m05"));
				pstmt.setDouble(i++, map.getDouble("m06"));
				pstmt.setDouble(i++, map.getDouble("m07"));
				pstmt.setDouble(i++, map.getDouble("m08"));
				pstmt.setDouble(i++, map.getDouble("m09"));
				pstmt.setDouble(i++, map.getDouble("m10"));
				pstmt.setDouble(i++, map.getDouble("m11"));
				pstmt.setDouble(i++, map.getDouble("m12"));
				pstmt.setDouble(i++, map.getDouble("m13"));
				pstmt.setDouble(i++, map.getDouble("m14"));
				pstmt.setDouble(i++, map.getDouble("m15"));
				pstmt.setDouble(i++, map.getDouble("m16"));
				pstmt.setDouble(i++, map.getDouble("m17"));
				pstmt.setDouble(i++, map.getDouble("m18"));
				pstmt.setDouble(i++, map.getDouble("m19"));
				pstmt.setDouble(i++, map.getDouble("m20"));
				pstmt.setDouble(i++, map.getDouble("m21"));
				pstmt.setDouble(i++, map.getDouble("m22"));
				pstmt.setDouble(i++, map.getDouble("m23"));
				pstmt.setDouble(i++, map.getDouble("m24"));
				pstmt.setString(i++, regId);
				pstmt.setString(i++, regDay);
				
				//update
				pstmt.setDouble(i++, map.getDouble("m02"));
				pstmt.setDouble(i++, map.getDouble("m03"));
				pstmt.setDouble(i++, map.getDouble("m04"));
				pstmt.setDouble(i++, map.getDouble("m05"));
				pstmt.setDouble(i++, map.getDouble("m06"));
				pstmt.setDouble(i++, map.getDouble("m07"));
				pstmt.setDouble(i++, map.getDouble("m08"));
				pstmt.setDouble(i++, map.getDouble("m09"));
				pstmt.setDouble(i++, map.getDouble("m10"));
				pstmt.setDouble(i++, map.getDouble("m11"));
				pstmt.setDouble(i++, map.getDouble("m12"));
				pstmt.setDouble(i++, map.getDouble("m13"));
				pstmt.setDouble(i++, map.getDouble("m14"));
				pstmt.setDouble(i++, map.getDouble("m15"));
				pstmt.setDouble(i++, map.getDouble("m16"));
				pstmt.setDouble(i++, map.getDouble("m17"));
				pstmt.setDouble(i++, map.getDouble("m18"));
				pstmt.setDouble(i++, map.getDouble("m19"));
				pstmt.setDouble(i++, map.getDouble("m20"));
				pstmt.setDouble(i++, map.getDouble("m21"));
				pstmt.setDouble(i++, map.getDouble("m22"));
				pstmt.setDouble(i++, map.getDouble("m23"));
				pstmt.setDouble(i++, map.getDouble("m24"));
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
	// van 상점부담 무이자 수수료 조회 클릭 시 
	@RequestMapping(value = "/mcht/van/interFee/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vanFeeInterList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		OrgInterFeeDAO vanDAO = new OrgInterFeeDAO();
		RecordSet rset = vanDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,vanDAO).setView(request,"/mcht/van/interFee/list","");
	}
	// 무이자 수수료 템플릿 카테고리 클릭 시 
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
				pstmt.setDouble(i++, map.getDouble("m02"));
				pstmt.setDouble(i++, map.getDouble("m03"));
				pstmt.setDouble(i++, map.getDouble("m04"));
				pstmt.setDouble(i++, map.getDouble("m05"));
				pstmt.setDouble(i++, map.getDouble("m06"));
				pstmt.setDouble(i++, map.getDouble("m07"));
				pstmt.setDouble(i++, map.getDouble("m08"));
				pstmt.setDouble(i++, map.getDouble("m09"));
				pstmt.setDouble(i++, map.getDouble("m10"));
				pstmt.setDouble(i++, map.getDouble("m11"));
				pstmt.setDouble(i++, map.getDouble("m12"));
				pstmt.setDouble(i++, map.getDouble("m13"));
				pstmt.setDouble(i++, map.getDouble("m14"));
				pstmt.setDouble(i++, map.getDouble("m15"));
				pstmt.setDouble(i++, map.getDouble("m16"));
				pstmt.setDouble(i++, map.getDouble("m17"));
				pstmt.setDouble(i++, map.getDouble("m18"));
				pstmt.setDouble(i++, map.getDouble("m19"));
				pstmt.setDouble(i++, map.getDouble("m20"));
				pstmt.setDouble(i++, map.getDouble("m21"));
				pstmt.setDouble(i++, map.getDouble("m22"));
				pstmt.setDouble(i++, map.getDouble("m23"));
				pstmt.setDouble(i++, map.getDouble("m24"));
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
	// 무이자 수수료 템플릿 리스트 수정 클릭 시 
	@RequestMapping(value = "/mcht/interTemplate/modify/{templateId}", method = RequestMethod.GET)
	public ModelAndView mchtInterTemplateModify(HttpServletRequest request, @PathVariable String templateId) {
		request.setAttribute("DATAMAP", new MchtInterTemplateDAO().getInterTemplate(templateId).getRowFirst());
		request.setAttribute("DATALIST", new MchtInterTemplateDAO().getModifyList(templateId).getRows());
		return new ModelAndView("/mcht/interTemplate/modify");
	}
	
	// KBR : 무이자 수수료 템플릿 수정 클릭 시 
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
			
			// json 형태로 java 객체를 만들기 위해 생성
			Gson gson = new Gson();
			
			// 맵핑 할 객체 , 맵핑 할 객체가 들어있는 클래스
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
				pstmt.setDouble(i++, map.getDouble("m02"));
				pstmt.setDouble(i++, map.getDouble("m03"));
				pstmt.setDouble(i++, map.getDouble("m04"));
				pstmt.setDouble(i++, map.getDouble("m05"));
				pstmt.setDouble(i++, map.getDouble("m06"));
				pstmt.setDouble(i++, map.getDouble("m07"));
				pstmt.setDouble(i++, map.getDouble("m08"));
				pstmt.setDouble(i++, map.getDouble("m09"));
				pstmt.setDouble(i++, map.getDouble("m10"));
				pstmt.setDouble(i++, map.getDouble("m11"));
				pstmt.setDouble(i++, map.getDouble("m12"));
				pstmt.setDouble(i++, map.getDouble("m13"));
				pstmt.setDouble(i++, map.getDouble("m14"));
				pstmt.setDouble(i++, map.getDouble("m15"));
				pstmt.setDouble(i++, map.getDouble("m16"));
				pstmt.setDouble(i++, map.getDouble("m17"));
				pstmt.setDouble(i++, map.getDouble("m18"));
				pstmt.setDouble(i++, map.getDouble("m19"));
				pstmt.setDouble(i++, map.getDouble("m20"));
				pstmt.setDouble(i++, map.getDouble("m21"));
				pstmt.setDouble(i++, map.getDouble("m22"));
				pstmt.setDouble(i++, map.getDouble("m23"));
				pstmt.setDouble(i++, map.getDouble("m24"));
				
				//update
				pstmt.setDouble(i++, map.getDouble("m02"));
				pstmt.setDouble(i++, map.getDouble("m03"));
				pstmt.setDouble(i++, map.getDouble("m04"));
				pstmt.setDouble(i++, map.getDouble("m05"));
				pstmt.setDouble(i++, map.getDouble("m06"));
				pstmt.setDouble(i++, map.getDouble("m07"));
				pstmt.setDouble(i++, map.getDouble("m08"));
				pstmt.setDouble(i++, map.getDouble("m09"));
				pstmt.setDouble(i++, map.getDouble("m10"));
				pstmt.setDouble(i++, map.getDouble("m11"));
				pstmt.setDouble(i++, map.getDouble("m12"));
				pstmt.setDouble(i++, map.getDouble("m13"));
				pstmt.setDouble(i++, map.getDouble("m14"));
				pstmt.setDouble(i++, map.getDouble("m15"));
				pstmt.setDouble(i++, map.getDouble("m16"));
				pstmt.setDouble(i++, map.getDouble("m17"));
				pstmt.setDouble(i++, map.getDouble("m18"));
				pstmt.setDouble(i++, map.getDouble("m19"));
				pstmt.setDouble(i++, map.getDouble("m20"));
				pstmt.setDouble(i++, map.getDouble("m21"));
				pstmt.setDouble(i++, map.getDouble("m22"));
				pstmt.setDouble(i++, map.getDouble("m23"));
				pstmt.setDouble(i++, map.getDouble("m24"));
				
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
	
	// 가맹점 수수료 템플릿 리스트 삭제 
	@RequestMapping(value = "/mcht/feeTemplate/delete/{idx}", method = RequestMethod.GET)
	public @ResponseBody Object mchtTemplateDelete(HttpServletRequest request, @PathVariable String idx) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String,Object>();
		CPDAO cpDAO = new CPDAO();
		
		// 테이블과 삭제 idx값 셋팅
		cpDAO.setTable("PG_MCHT_FEE_TEMPLATE");
		cpDAO.addWhere("idx", idx, DAO.eq);
		// 결과값 메세지 셋팅
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
	
	// 비대면 계좌 개설 클릭 시 
	@RequestMapping(value = { "/mcht/accntSearch/form" })
	public ModelAndView accntSearchform(HttpServletRequest request) {
		return new ModelAndView("/mcht/accntSearch/form");
	}
	// 비대면 계좌 리스트
	@RequestMapping(value = "/mcht/accntSearch/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView accntSearchlist(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws UnsupportedEncodingException {
		
		MchtDAO mchtDAO = new MchtDAO();
		
		SessionUtil.setSearchGrade(request, cpRequest);
		//String key = "yGXMeC5TP3xHkmX5+Yk03PZ1wYZ3JbdEYnRNcmDTd64vL9V1aW4hdzSeJ86ztLSo"; //테스트키
		String key = "6wCPEeQ0egkz3mPaE3R3MMGGW3KNoxunQBTcnow5g80VU431JHHPtYKLM0VDAgkU"; //운영키
		// 비대면 계좌 등록 리스트 
		RecordSet rset = mchtDAO.accntSeachList(cpRequest.data, cpRequest.page);
		
		logger.info("accntSearchlist : " + rset.size());
		
		for(int i = 0; i < rset.size(); i++) {
			String name = rset.getRow(i).getString("name");
			String mobileno = rset.getRow(i).getString("mobileno");
			String accntno = rset.getRow(i).getString("accntno");
			
// 			KBR : 해당 코드 주석하니 비대면 계좌 리스트 조회 됨 (HanaTICryptoUtil 문제가 뭐지..)
//			name = HanaTICryptoUtil.Decrypt(name,key);
//			mobileno = HanaTICryptoUtil.Decrypt(mobileno,key);
//			accntno = HanaTICryptoUtil.Decrypt(accntno,key);

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
		if (cpDAO.insert("PG_MCHT_CHARGE_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 충전정산 정보가 등록되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 충전정산 정보 등록에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
	}
	
	@RequestMapping(value = "/mcht/chargeMng/modify/{mchtId}", method = RequestMethod.GET)
	public ModelAndView chargeMngModify(HttpServletRequest request, @PathVariable String mchtId) {
		request.setAttribute("MCHTMAP", new MchtDAO().getById(mchtId).getRowFirst());
		request.setAttribute("DATAMAP", new MchtChargeSettleDAO().getById(mchtId).getRowFirst());
		return new ModelAndView("/mcht/chargeMng/modify");
	}
	
	@RequestMapping(value = {"/mcht/chargeMng/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse chargeMngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//identity 입력시 암호화하여 넣어야함.
		if (cpDAO.update("PG_MCHT_CHARGE_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("가맹점 충전정산 정보가 수정되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("가맹점 충전정산 정보 수정에 실패하였습니다.",cpDAO.getError() )
					.cpResponse();
		}
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
		settleMap.put("trxUnit", "수기등록");
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


}
