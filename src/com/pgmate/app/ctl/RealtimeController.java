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

import com.pgmate.app.dao.RealtimePayOutDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.FirmBean;
import com.pgmate.app.util.FirmUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.conf.Firm;
import com.pgmate.lib.conf.FirmLoader;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class RealtimeController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.RealtimeController.class );
	
	@RequestMapping(value = "/realtime/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView realtimeList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		logger.info("realtimeList Call");
		
		request.setAttribute("SUMMAP", new RealtimePayOutDAO().calcRealTimeList(cpRequest.data).getRow(0));
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		RecordSet rset = realtimeDAO.list(cpRequest.data,cpRequest.page);
		
		return new CPRUtil(cpRequest).dataList(rset,realtimeDAO).setView(request,"/realtimeSettle/realtime/list","");
	}
	
	@RequestMapping(value = "/realtime/cntUpdate/{trxId}", method = RequestMethod.GET)
    public @ResponseBody String cntUpdate(HttpServletRequest request, @PathVariable String trxId) {
		logger.info("cntUpdate Call : " + trxId);
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		StringBuilder result = new StringBuilder();
		
		if(realtimeDAO.updateCnt(trxId)) {
			result.append(true);
		}else {
			result.append(false);
		}
		
        return result.toString();
    }
	
	@RequestMapping(value = "/realtime/selectRetry/{trxId}", method = RequestMethod.GET)
    public @ResponseBody String selectRetry(HttpServletRequest request, @PathVariable String trxId) {
		logger.info("selectRetry Call : " + trxId);
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();

		String[] arr = trxId.split(",");

		for(int i = 0; arr.length > i; i++) {
			logger.info("selectRetry trxId : [{}]", arr[i]);
			
			realtimeDAO.updateCnt(arr[i]);
		}
		
        return "true";
    }
	
	@RequestMapping(value = "/realtime/cancelUpdate/{trxId}", method = RequestMethod.POST)
	public @ResponseBody String cancelUpdate(HttpServletRequest request, @PathVariable String trxId) {
		String amt = request.getParameter("cancelAmount");
		String memo = CommonUtil.nToB(request.getParameter("cancelMemo"));
		
		logger.info("cancelUpdate Call [{}][{}][{}]", trxId, amt, memo);
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		StringBuilder result = new StringBuilder();
		
		if(realtimeDAO.cancelUpdate(trxId, amt, memo)) {
			result.append(true);
		}else {
			result.append(false);
		}
		
        return result.toString();
    }
	
	@RequestMapping(value = "/realtime/selectMemo/{trxId}", method = RequestMethod.POST)
    public @ResponseBody String selectMemo(HttpServletRequest request, @PathVariable String trxId) {
		logger.info("selectMemo Call : " + trxId);
		
		String memo = "";
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();

		memo = realtimeDAO.selectMemo(trxId);
		
		logger.info("selectMemo memo : " + memo);
		
        return memo;
    }
	
	@RequestMapping(value = "/auto/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView autoList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new RealtimePayOutDAO().calcAutoList(cpRequest.data).getRow(0));
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		// column 'A.bankFee' 에러발생으로 주석처리
		//231207_PYS : 주석처리 해제
		RecordSet rset = realtimeDAO.autoList(cpRequest.data,cpRequest.page);
		//RecordSet rset = new RecordSet();

		return new CPRUtil(cpRequest).dataList(rset,realtimeDAO).setView(request,"/realtimeSettle/auto/list","");
	}
	
	@RequestMapping(value = "/auto/minusAmtUpdate/{stlId}", method = RequestMethod.POST)
	public @ResponseBody String minusAmtUpdate(HttpServletRequest request, @PathVariable String stlId) {
		String amt = request.getParameter("minusAmt");
		String memo = CommonUtil.nToB(request.getParameter("minusAmtMemo"));
		String payOutAmount = request.getParameter("payOutAmount");

		logger.info("minusAmtUpdate Call [{}][{}][{}][{}]", stlId, amt, memo, payOutAmount);
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		StringBuilder result = new StringBuilder();
		
		if(realtimeDAO.minusAmtUpdate(stlId, amt, memo, payOutAmount)) {
			result.append(true);
		}else {
			result.append(false);
		}
		
        return result.toString();
    }
	
	@RequestMapping(value = "/auto/selectMinusAmtMemo/{stlId}", method = RequestMethod.POST)
    public @ResponseBody String selectMinusAmtMemo(HttpServletRequest request, @PathVariable String stlId) {
		logger.info("selectMinusAmtMemo Call : " + stlId);
		
		String memo = "";
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();

		memo = realtimeDAO.selectMinusAmtMemo(stlId);
		
		logger.info("selectMinusAmtMemo memo : " + memo);
		
        return memo;
    }
	
	@RequestMapping(value = "/auto/deductAmtUpdate/{stlId}", method = RequestMethod.POST)
	public @ResponseBody String deductAmtUpdate(HttpServletRequest request, @PathVariable String stlId) {
		String amt = request.getParameter("deductAmt");
		String memo = CommonUtil.nToB(request.getParameter("deductAmtMemo"));
		String payOutAmount = request.getParameter("payOutAmount");
		
		logger.info("deductAmtUpdate Call [{}][{}][{}][{}]", stlId, amt, memo, payOutAmount);
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		StringBuilder result = new StringBuilder();
		
		if(realtimeDAO.deductAmtUpdate(stlId, amt, memo, payOutAmount)) {
			result.append(true);
		}else {
			result.append(false);
		}
		
        return result.toString();
    }
	
	@RequestMapping(value = "/auto/selectDeductAmtMemo/{stlId}", method = RequestMethod.POST)
    public @ResponseBody String selectDeductAmtMemo(HttpServletRequest request, @PathVariable String stlId) {
		logger.info("selectDeductAmtMemo Call : " + stlId);
		
		String memo = "";
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();

		memo = realtimeDAO.selectDeductAmtMemo(stlId);
		
		logger.info("selectDeductAmtMemo memo : " + memo);
		
        return memo;
    }
	
	@RequestMapping(value = "/auto/autoCntUpdate/{stlId}", method = RequestMethod.GET)
    public @ResponseBody String autoCntUpdate(HttpServletRequest request, @PathVariable String stlId) {
		logger.info("autoCntUpdate Call : " + stlId);
		
		RealtimePayOutDAO realtimeDAO = new RealtimePayOutDAO();
		StringBuilder result = new StringBuilder();
		
		if(realtimeDAO.updateAutoCnt(stlId)) {
			result.append(true);
		}else {
			result.append(false);
		}
		
        return result.toString();
    }
	
	@RequestMapping(value = "/auto/retrySend", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object retrySend(HttpServletRequest request, @RequestBody SharedMap<String, Object> reqMap) {
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		String stlStatus = "지급완료";
		
		int currentTime = CommonUtil.parseInt(CommonUtil.getCurrentDate("HHmmss"));
		Firm firm = FirmLoader.getConfig();
		if(currentTime > firm.firmEndTime || currentTime < firm.firmStartTime) {
			resMap.put("resultCd", "9999");
			resMap.put("resultMsg", "이체 서비스 가능한 시간이 아닙니다.[23:30 ~ 00:30 은행공동망 점검시간]");
			return resMap;
		}
		
		RealtimePayOutDAO dao = new RealtimePayOutDAO();		
				
		SharedMap<String,Object> mchtTaxMap	= dao.getMchtTaxByMchtId(reqMap.getString("mchtId"));
		
		//FirmBean firmBean = new FirmUtil().transfer("089",mchtTaxMap.getString("bankCd"), mchtTaxMap.getString("account").replace("-", "").trim(), reqMap.getLong("amount"), reqMap.getString("stlId"), "");
		FirmBean firmBean = new FirmBean();
		firmBean.resultCd = "0000";
		firmBean.resultMsg = "성공";
		
		if(!firmBean.resultCd.equals("0000") ) {
			stlStatus = "지급실패";
			resMap.put("resultCd", "9999");
			resMap.put("resultMsg", "이체에 실패하였습니다.");
			resMap.put("advanceMsg", "["+firmBean.resultCd+"]"+firmBean.resultMsg);
			logger.info("fail: ",resMap.getString("advanceMsg"));
			
			//자동정산출금 결과 저장
			dao.updateAutoPayOutRes(reqMap.getString("stlId"), stlStatus, CommonUtil.getCurrentDate("yyyyMMdd"), CommonUtil.getCurrentDate("HHmmss"), mchtTaxMap.getString("bankCd"), 
								    mchtTaxMap.getString("bankName"), dao.getAESEnc(mchtTaxMap.getString("account")), dao.getAESEnc(mchtTaxMap.getString("accntHolder")), firmBean.resultCd, firmBean.resultMsg);
			
			return resMap;
		}else {
			//자동정산출금 결과 매입테이블 업데이트
			//231207_PYS : 필요없는 로직 주석
			//dao.updateAutoPayOutCapUpdate(reqMap.getString("stlId"), reqMap.getString("stlDay"), reqMap.getString("stlType"), reqMap.getString("mchtId"), CommonUtil.getCurrentDate("yyyyMMdd"), "정산완료");
			
			//자동정산출금 결과 저장
			dao.updateAutoPayOutRes(reqMap.getString("stlId"), stlStatus, CommonUtil.getCurrentDate("yyyyMMdd"), CommonUtil.getCurrentDate("HHmmss"), mchtTaxMap.getString("bankCd"), 
								    mchtTaxMap.getString("bankName"), dao.getAESEnc(mchtTaxMap.getString("account")), dao.getAESEnc(mchtTaxMap.getString("accntHolder")), firmBean.resultCd, firmBean.resultMsg);
		}
		
			
		resMap.put("resultCd", "0000");
		resMap.put("resultMsg", "이체가 완료되었습니다.");
		resMap.put("advanceMsg", "["+firmBean.resultCd+"]"+firmBean.resultMsg);
		
    	return resMap;
 	}
	
	@RequestMapping(value = "/auto/statusUpdate", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object statusUpdate(HttpServletRequest request, @RequestBody SharedMap<String, Object> reqMap) {
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		String stlStatus = "지급완료";
		
		RealtimePayOutDAO dao = new RealtimePayOutDAO();		
				
		SharedMap<String,Object> mchtTaxMap	= dao.getMchtTaxByMchtId(reqMap.getString("mchtId"));
		
		//자동정산출금 결과 매입테이블 업데이트
		dao.updateAutoPayOutCapUpdate(reqMap.getString("stlId"), reqMap.getString("stlDay"), reqMap.getString("stlType"), reqMap.getString("mchtId"), CommonUtil.getCurrentDate("yyyyMMdd"), "정산완료");
		
		//자동정산출금 결과 저장
		dao.updateAutoPayOutRes(reqMap.getString("stlId"), stlStatus, CommonUtil.getCurrentDate("yyyyMMdd"), CommonUtil.getCurrentDate("HHmmss"), mchtTaxMap.getString("bankCd"), 
							    mchtTaxMap.getString("bankName"), dao.getAESEnc(mchtTaxMap.getString("account")), dao.getAESEnc(mchtTaxMap.getString("accntHolder")), "0000", "상태 업데이트 완료");
	
		resMap.put("resultCd", "0000");
		resMap.put("resultMsg", "상태업데이트가 완료되었습니다.");
		resMap.put("advanceMsg", "[0000]"+"상태 업데이트 완료");
		
    	return resMap;
 	}
}





