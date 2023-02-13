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

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.VactDtlDAO;
import com.pgmate.app.dao.VactTrxDAO;
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
    return new CPRUtil(cpRequest).dataList(rset, vactTrxDAO).setView(request, "/vact/trx/list", "");
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

        if (!CommonUtil.isNullOrSpace(withdrawAccountDec)) {
            resMap.put("msg", "출금계좌번호 : " + withdrawAccountDec + "");
            resMap.put("withdrawAccount", withdrawAccountDec);
            resMap.put("result", "OK");
        } else {
            resMap.put("msg", "출금계좌번호가 존재하지 않습니다.");
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
		String reason = cpRequest.getValue("reason");
		
		logger.info("blackListInsert : [{}][{}][{}]", account, bankCd, reason);
		cpRequest.replaceValue("account",cpDAO.getAESEnc(cpRequest.getValue("account")));
		
		if(!vactTrxDAO.isBlackList(bankCd, cpRequest.getValue("account"))) {
			if(cpDAO.insert2("PG_VACT_REG_BLACKLIST", SessionUtil.getUserId(request), cpRequest.data)){
				return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect).cpResponse();
			}
		}else {
			return new CPRUtil(cpRequest)
		        	.resultNOK(CPUtil.RESULT_DATA_INFAIL, "이미 등록된 출금계좌 정보입니다.")
		        	.cpResponse();
		}
		
		return new CPRUtil(cpRequest)
	        	.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        	.cpResponse();
  }

    @RequestMapping(value = "/vact/reg/blackList/withdrawAccount", method = RequestMethod.GET)
    public ModelAndView withdrawAccountView(HttpServletRequest request) {
        return new ModelAndView("/vact/blackList/modal");
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
}