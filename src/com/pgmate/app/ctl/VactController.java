package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;

import com.pgmate.app.dao.*;
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

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SQLInjectionUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

import java.io.UnsupportedEncodingException;

@Controller
public class VactController {
  private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.VactController.class);

  @RequestMapping(value = "/vact/dtl/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
  public ModelAndView dtlList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
	SessionUtil.setSearchGrade(request, cpRequest);
	
	//터미널아이디로 접속한 경우 가맹점아이디로 검색
    CPSession cpSession = SessionUtil.get(request);
    if(cpSession.getGrade().equals("하위가맹점")) {
		MchtTmnDAO mDAO = new MchtTmnDAO();
		SharedMap<String, Object> map = mDAO.getById(cpRequest.getKeyValue("tmnId")).getRowFirst();
		cpRequest.replaceKeyName("tmnId", "mchtId");
		cpRequest.replaceKeyValue("mchtId",	map.getString("mchtId"));
	}
	
    VactDtlDAO vactDtlDAO = new VactDtlDAO();
    RecordSet rset = vactDtlDAO.list(cpRequest.data, cpRequest.page);
    return new CPRUtil(cpRequest).dataList(rset, vactDtlDAO).setView(request, "/vact/dtl/list", "");
  }
  
  @RequestMapping(value = "/vact/trx/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
  public ModelAndView trxList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
    SessionUtil.setSearchGrade(request, cpRequest);
    
    //터미널아이디로 접속한 경우 가맹점아이디로 검색
    CPSession cpSession = SessionUtil.get(request);
    if(cpSession.getGrade().equals("하위가맹점")) {
	    MchtTmnDAO mDAO = new MchtTmnDAO();
		SharedMap<String, Object> map = mDAO.getById(cpRequest.getKeyValue("tmnId")).getRowFirst();
		cpRequest.replaceKeyName("tmnId", "mchtId");
		cpRequest.replaceKeyValue("mchtId",	map.getString("mchtId"));
    }
	
    VactTrxDAO vactTrxDAO = new VactTrxDAO();
    request.setAttribute("AMOUNT_SUM", new VactTrxDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));
    RecordSet rset = vactTrxDAO.list(cpRequest.data, cpRequest.page);
//    RecordSet rset = vactTrxDAO.listWithDecAccount(cpRequest.data, cpRequest.page);
    return new CPRUtil(cpRequest).dataList(rset, vactTrxDAO).setView(request, "/vact/trx/list", "");
  }

    /**
     * CREATE by PYS : 거래관리 - 가상계좌관리 - 인증수수료조회 페이지
     * @param request
     * @param cpRequest
     * @return
     */
    @RequestMapping(value = "/vact/auth/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView authList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);

        //터미널아이디로 접속한 경우 가맹점아이디로 검색
        CPSession cpSession = SessionUtil.get(request);
        if(cpSession.getGrade().equals("하위가맹점")) {
            MchtTmnDAO mDAO = new MchtTmnDAO();
            SharedMap<String, Object> map = mDAO.getById(cpRequest.getKeyValue("tmnId")).getRowFirst();
            cpRequest.replaceKeyName("tmnId", "mchtId");
            cpRequest.replaceKeyValue("mchtId",	map.getString("mchtId"));
        }

        VactTrxDAO vactTrxDAO = new VactTrxDAO();
        RecordSet rset = vactTrxDAO.authList(cpRequest.data, cpRequest.page);
/*

        //가상계좌 은행이름, 계좌번호 세팅
        //나중에 PAY쪽에서 가상계좌 발행할때 update하는 방법도 괜춘할듯.
        //230306_PYS : 출금계좌은행, 예금주 표시

        for(SharedMap<String,Object>  data : rset.getRows()) {
            String totalAuthId = data.getString("totalAuthId");
            RecordSet recordSet = vactTrxDAO.getVactAuth(totalAuthId);
            if(recordSet.size() != 0) {

                */
/*String bankCd = recordSet.getRowFirst().getString("vactBankCd");
                String bankName = vactTrxDAO.getBankName(bankCd).getString("codeName");
                data.put("vactBank", bankName);*//*


                String account = recordSet.getRowFirst().getString("vactAccount");
                data.put("vactAccount", account);

                //230509_PYS : 아래로직은 출금계좌등록된것만 가져오기때문에, 은행과 예금주를 TOTAL_AUTH에서 가져오도록 한다.
//                SharedMap<String, Object> regMap = vactTrxDAO.withdrawAccount(account);
//                data.put("withdrawBankName", regMap.getString("withdrawBankNm"));
//                data.put("holderName", regMap.getString("holderName"));
            }
        }

*/

        //수수료 건수, 수수료 총금액 표시
        if(cpSession.getGrade().equals("본사")) {
            String ownerCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "실명인증", "").getRowFirst().getString("count");
            String ownerFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "실명인증", "").getRowFirst().getString("authFeeSum");
            String accountCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "1원인증", "").getRowFirst().getString("count");
            String accountFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "1원인증", "").getRowFirst().getString("authFeeSum");
            String arsCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "ARS인증", "").getRowFirst().getString("count");
            String arsFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "ARS인증", "").getRowFirst().getString("authFeeSum");
            String totalCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "", "").getRowFirst().getString("count");
            String totalFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "", "").getRowFirst().getString("authFeeSum");

            request.setAttribute("OWNER_COUNT", ownerCount);
            request.setAttribute("OWNER_SUM", ownerFeeSum);
            request.setAttribute("ACCOUNT_COUNT", accountCount);
            request.setAttribute("ACCOUNT_SUM", accountFeeSum);
            request.setAttribute("ARS_COUNT", arsCount);
            request.setAttribute("ARS_SUM", arsFeeSum);
            request.setAttribute("TOTAL_COUNT", totalCount);
            request.setAttribute("TOTAL_SUM", totalFeeSum);
        } else if(cpSession.getGrade().equals("가맹점")) {
            String mchtId = cpRequest.getKeyValue("mchtId");
            String ownerCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "실명인증", mchtId).getRowFirst().getString("count");
            String ownerFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "실명인증", mchtId).getRowFirst().getString("authFeeSum");
            String accountCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "1원인증", mchtId).getRowFirst().getString("count");
            String accountFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "1원인증", mchtId).getRowFirst().getString("authFeeSum");
            String arsCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "ARS인증", mchtId).getRowFirst().getString("count");
            String arsFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "ARS인증", mchtId).getRowFirst().getString("authFeeSum");
            String totalCount = vactTrxDAO.getAuthFeeSum(cpRequest.data, "", mchtId).getRowFirst().getString("count");
            String totalFeeSum = vactTrxDAO.getAuthFeeSum(cpRequest.data, "", mchtId).getRowFirst().getString("authFeeSum");

            request.setAttribute("OWNER_COUNT", ownerCount);
            request.setAttribute("OWNER_SUM", ownerFeeSum);
            request.setAttribute("ACCOUNT_COUNT", accountCount);
            request.setAttribute("ACCOUNT_SUM", accountFeeSum);
            request.setAttribute("ARS_COUNT", arsCount);
            request.setAttribute("ARS_SUM", arsFeeSum);
            request.setAttribute("TOTAL_COUNT", totalCount);
            request.setAttribute("TOTAL_SUM", totalFeeSum);
        }

        return new CPRUtil(cpRequest).dataList(rset, vactTrxDAO).setView(request, "/vact/auth/list", "");
    }

    @RequestMapping(value = "/vact/auth/changeStlDayUpdate", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object changeAuthStlDay(HttpServletRequest request, @RequestBody SharedMap<String, Object> reqMap) {
        SharedMap<String, Object> resMap = new SharedMap<String, Object>();

        String authId = reqMap.getString("trxId");
        String summary = reqMap.getString("stlDay");

        VactTrxDAO vactTrxDAO = new VactTrxDAO();

        if(vactTrxDAO.updateAuthStlDay(authId, summary)) {
            resMap.put("resultCd", "0000");
            resMap.put("resultMsg", "변경되었습니다.");
        } else {
            resMap.put("resultCd", "9999");
            resMap.put("resultMsg", "변경에 실패했습니다.");
        }

        return resMap;
    }

    @RequestMapping(value = "/vact/auth/changeStlStatusUpdate", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object changeAuthStlStatus(HttpServletRequest request, @RequestBody SharedMap<String, Object> reqMap) {
        SharedMap<String, Object> resMap = new SharedMap<String, Object>();

        String authId = reqMap.getString("trxId");

        VactTrxDAO vactTrxDAO = new VactTrxDAO();

        if(vactTrxDAO.updateAuthStlStatus(authId)) {
            resMap.put("resultCd", "0000");
            resMap.put("resultMsg", "변경되었습니다.");
        } else {
            resMap.put("resultCd", "9999");
            resMap.put("resultMsg", "변경에 실패했습니다.");
        }

        return resMap;
    }

    @RequestMapping(value = "/vact/auth/changeSummary", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object changeAuthSummary(HttpServletRequest request, @RequestBody SharedMap<String, Object> reqMap) {
        SharedMap<String, Object> resMap = new SharedMap<String, Object>();

        String authId = reqMap.getString("trxId");
        String summary = reqMap.getString("summary");

        VactTrxDAO vactTrxDAO = new VactTrxDAO();

        if(vactTrxDAO.updateAuthSummary(authId, summary)) {
            resMap.put("resultCd", "0000");
            resMap.put("resultMsg", "변경되었습니다.");
        } else {
            resMap.put("resultCd", "9999");
            resMap.put("resultMsg", "변경에 실패했습니다.");
        }

        return resMap;
    }
  
  @RequestMapping(value = "/vact/trx/view/{vactId}", method = RequestMethod.GET)
  public ModelAndView trxView(HttpServletRequest request, @PathVariable String vactId) {
    VactTrxDAO vactTrxDAO = new VactTrxDAO();
    return new ModelAndView("/vact/trx/modal", "DATAMAP", vactTrxDAO.getById(vactId).getRowFirst());
  }
  
  @RequestMapping(value = "/vact/retry/{vactId}", method = RequestMethod.GET)
  public @ResponseBody SharedMap<String, Object> trxRetry(HttpServletRequest request, @PathVariable String vactId) {
    SharedMap<String, Object> resMap = new SharedMap<String, Object>();
    resMap.put("result", "NOK");
    DAO dao = new DAO();
    if (dao.update("UPDATE PG_VACT_TRX SET hookStatus='전송장애', hookRetry='1' WHERE vactId ='" + vactId + "'")) {
      resMap.put("result", "OK");
    } else {
      resMap.put("msg", "재전송에 실패했습니다. 관리자에게 문의해주세요.");
    }
    return resMap;
  }

    @RequestMapping(value = "/vact/withdrawAccount/{account}", method = RequestMethod.GET)
    public @ResponseBody SharedMap<String, Object> withdrawAccount(HttpServletRequest request, @PathVariable String account) {
        SharedMap<String, Object> resMap = new SharedMap<String, Object>();

        VactTrxDAO vactTrxDAO = new VactTrxDAO();
        SharedMap<String, Object> sharedMap = vactTrxDAO.withdrawAccount(account);
        String withdrawAccountDec = sharedMap.getString("withdrawAccountDec");
        String withdrawBankCd = sharedMap.getString("withdrawBankCd");
        String withdrawBankNm = sharedMap.getString("withdrawBankNm");
        String holderName = sharedMap.getString("holderName");
        String identity = sharedMap.getString("identity");

        if (!CommonUtil.isNullOrSpace(withdrawAccountDec)) {
            resMap.put("msg", "[" + withdrawBankNm + "/" + withdrawAccountDec + "/" + holderName + "/" + identity + "]");
            resMap.put("withdrawAccount", withdrawAccountDec);
            resMap.put("withdrawBankCd", withdrawBankCd);
            resMap.put("withdrawBankNm", withdrawBankNm);
            resMap.put("holderName", holderName);
            resMap.put("identity", identity);
            resMap.put("result", "OK");
        } else {
            resMap.put("msg", "출금계좌번호가 존재하지 않습니다.");
            resMap.put("result", "NOK");
        }
        return resMap;
    }

    @RequestMapping(value = "/vact/withdrawAccountByAuthId/{authId}", method = RequestMethod.GET)
    public @ResponseBody SharedMap<String, Object> withdrawAccountByAuthId(HttpServletRequest request, @PathVariable String authId) {
        SharedMap<String, Object> resMap = new SharedMap<String, Object>();

        VactTrxDAO vactTrxDAO = new VactTrxDAO();
        SharedMap<String, Object> sharedMap = vactTrxDAO.getTotalAuth(authId);
        String withdrawAccountDec = sharedMap.getString("withdrawAccountDec");
        String withdrawBankCd = sharedMap.getString("withdrawBankCd");
        String withdrawBankNm = sharedMap.getString("withdrawBankNm");
        String holderName = sharedMap.getString("holderName");
        String identity = sharedMap.getString("identity");

        if (!CommonUtil.isNullOrSpace(withdrawAccountDec)) {
            resMap.put("msg", "[" + withdrawBankNm + "/" + withdrawAccountDec + "/" + holderName + "/" + identity + "]");
            resMap.put("withdrawAccount", withdrawAccountDec);
            resMap.put("withdrawBankCd", withdrawBankCd);
            resMap.put("withdrawBankNm", withdrawBankNm);
            resMap.put("holderName", holderName);
            resMap.put("identity", identity);
            resMap.put("result", "OK");
        } else {
            resMap.put("msg", "계좌번호가 존재하지 않습니다.");
            resMap.put("result", "NOK");
        }
        return resMap;
    }
  
  @RequestMapping(value = {"/vact/reg/blackList/add"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody CPResponse blackListInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
        CPDAO cpDAO = new CPDAO();
		VactTrxDAO vactTrxDAO = new VactTrxDAO();
		
		String account = cpRequest.getValue("account");
		String bankCd = cpRequest.getValue("bankCd");
        String bankName = cpRequest.getValue("bankName");
        String holderName = cpRequest.getValue("holderName");
        String identity = cpRequest.getValue("identity");
		String reason = cpRequest.getValue("reason");

        if(CommonUtil.isNullOrSpace(bankName)) {
            bankName = vactTrxDAO.getBankName(bankCd);
            cpRequest.setData("bankName", bankName);
        }

		logger.info("blackListInsert : [{}][{}][{}][{}][{}][{}]", account, bankCd, bankName, holderName, identity, reason);
		cpRequest.replaceValue("account",cpDAO.getAESEnc(cpRequest.getValue("account")));
        cpRequest.replaceValue("identity",cpDAO.getAESEnc(cpRequest.getValue("identity")));

        if(!CommonUtil.isNullOrSpace(holderName) && CommonUtil.isNullOrSpace(identity)) {
            return new CPRUtil(cpRequest)
                    .resultNOK(CPUtil.RESULT_DATA_INFAIL, "생년월일을 입력해야 합니다.")
                    .cpResponse();
        }

        if(CommonUtil.isNullOrSpace(holderName) && !CommonUtil.isNullOrSpace(identity)) {
            return new CPRUtil(cpRequest)
                    .resultNOK(CPUtil.RESULT_DATA_INFAIL, "이름을 입력해야 합니다.")
                    .cpResponse();
        }

        if(!CommonUtil.isNullOrSpace(bankCd) && CommonUtil.isNullOrSpace(account)) {
            return new CPRUtil(cpRequest)
                    .resultNOK(CPUtil.RESULT_DATA_INFAIL, "출금계좌번호를 입력해야 합니다.")
                    .cpResponse();
        }

        if(CommonUtil.isNullOrSpace(bankCd) && !CommonUtil.isNullOrSpace(account)) {
            return new CPRUtil(cpRequest)
                    .resultNOK(CPUtil.RESULT_DATA_INFAIL, "출금은행을 입력해야 합니다.")
                    .cpResponse();
        }

        boolean isAdd = false;

        if(!CommonUtil.isNullOrSpace(holderName) && !CommonUtil.isNullOrSpace(identity)) {
            if(vactTrxDAO.isBlackList2(holderName, cpRequest.getValue("identity"))) {
                return new CPRUtil(cpRequest)
                        .resultNOK(CPUtil.RESULT_DATA_INFAIL, "이미 등록된 이름과 생년월일 입니다.")
                        .cpResponse();
            } else {
                isAdd = true;
            }
        }

        if(!CommonUtil.isNullOrSpace(bankCd) && !CommonUtil.isNullOrSpace(account)) {
            if(vactTrxDAO.isBlackList(bankCd, cpRequest.getValue("account"))) {
                return new CPRUtil(cpRequest)
                        .resultNOK(CPUtil.RESULT_DATA_INFAIL, "이미 등록된 출금계좌 정보 입니다.")
                        .cpResponse();
            } else {
                isAdd = true;
            }
        }

        if(isAdd) {
            if(cpDAO.insert2("PG_VACT_REG_BLACKLIST", SessionUtil.getUserId(request), cpRequest.data)){
                return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect).cpResponse();
            } else {
                return new CPRUtil(cpRequest)
                        .resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
                        .cpResponse();
            }
        } else {
            return new CPRUtil(cpRequest)
                    .resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
                    .cpResponse();
        }
  }

    @RequestMapping(value = "/vact/reg/blackList/searchAccountModal", method = RequestMethod.GET)
    public ModelAndView searchAccountModal(HttpServletRequest request) {
        return new ModelAndView("/vact/blackList/search_account_modal");
    }

    @RequestMapping(value = "/vact/reg/blackList/searchAuthIdModal", method = RequestMethod.GET)
    public ModelAndView searchAuthIdModal(HttpServletRequest request) {
        return new ModelAndView("/vact/blackList/search_authid_modal");
    }
  
  
  @RequestMapping(value = "/vact/reg/blackList/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
  public ModelAndView blackList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
    SessionUtil.setSearchGrade(request, cpRequest);
    
    CPDAO cpDAO = new CPDAO();
    VactTrxDAO vactTrxDAO = new VactTrxDAO();
    
	for(Data data:cpRequest.data){
		if(data.key){
			if(!CommonUtil.toString(data.val).equals("")) {
				String str = CommonUtil.toString(data.val);
				String convaerted = SQLInjectionUtil.changeValue(str);
				
				if(data.name.equals("account")) {
					data.val = cpDAO.getAESEnc(convaerted);
				}

                if(data.name.equals("identity")) {
                    data.val = cpDAO.getAESEnc(convaerted);
                }
			}
		}
	}
    
    RecordSet rset = vactTrxDAO.blackList(cpRequest.data, cpRequest.page);
    return new CPRUtil(cpRequest).dataList(rset, vactTrxDAO).setView(request, "/vact/blackList/list", "");
  }
  
  @RequestMapping(value = "/vact/reg/blackList/delete/{idx}", method = RequestMethod.GET)
  public @ResponseBody String selectDelete(HttpServletRequest request, @PathVariable String idx) { 
	  logger.info("selectDelete Call : " + idx);
		
	  VactTrxDAO vactTrxDAO = new VactTrxDAO();

		String[] arr =  idx.split(",");

		for(int i = 0; arr.length > i; i++) {
			logger.info("selectDelete idx : [{}]", arr[i]);
			
			vactTrxDAO.updateUseYn(arr[i], SessionUtil.getUserId(request));
		}
		
      return "true";
  }
    @RequestMapping(value = "/vact/blackList/changeReason", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object changeBlackListReasen(HttpServletRequest request, @RequestBody SharedMap<String, Object> reqMap) {
        SharedMap<String, Object> resMap = new SharedMap<String, Object>();

        String idx = reqMap.getString("idx");
        String reason = reqMap.getString("reason");

        VactTrxDAO vactTrxDAO = new VactTrxDAO();

        if(vactTrxDAO.updateBlackReason(idx, reason)) {
            resMap.put("resultCd", "0000");
            resMap.put("resultMsg", "변경되었습니다.");
        } else {
            resMap.put("resultCd", "9999");
            resMap.put("resultMsg", "변경에 실패했습니다.");
        }

        return resMap;
    }

    @RequestMapping(value = "/vact/error/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView errorList(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws UnsupportedEncodingException {
        SessionUtil.setSearchGrade(request, cpRequest);

        CPDAO cpDAO = new CPDAO();
        VactTrxDAO vactTrxDAO = new VactTrxDAO();

        RecordSet rset = vactTrxDAO.errorList(cpRequest.data, cpRequest.page);
        for(SharedMap<String, Object> data : rset.getRows() ) {
            String reqData = data.getString("reqData");
            byte[] reqDataBytes = reqData.getBytes("EUC-KR");

            String name = reqData.substring(58, 68).trim();
            String amount = CommonUtil.toString(reqDataBytes,78, 13).trim();

            data.put("name", name);
            data.put("amount", CommonUtil.parseInt(amount));
        }
        return new CPRUtil(cpRequest).dataList(rset, vactTrxDAO).setView(request, "/vact/error/list", "");
    }


}