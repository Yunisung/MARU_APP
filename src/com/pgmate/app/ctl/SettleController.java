package com.pgmate.app.ctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.ChargeSettleDAO;
import com.pgmate.app.dao.DepositDAO;
import com.pgmate.app.dao.LoanSettleDAO;
import com.pgmate.app.dao.MchtDdctDAO;
import com.pgmate.app.dao.PispSettleDAO;
import com.pgmate.app.dao.SettleDAO;
import com.pgmate.app.dao.SettleDdctDAO;
import com.pgmate.app.dao.SettleHoldDAO;
import com.pgmate.app.dao.SettleMchtDAO;
import com.pgmate.app.dao.SettlePhoneDAO;
import com.pgmate.app.dao.SettleSubDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.app.export.CPDocument;
import com.pgmate.app.export.TaxExport;
import com.pgmate.app.export.XlsExport;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Files;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class SettleController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.SettleController.class);

	@RequestMapping(value = "/settle/dist/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDistList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		cpRequest.setData("grade", "대행사", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/dist/list", "");
	}

	@RequestMapping(value = "/settle/agency/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAgencyList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		cpRequest.setData("grade", "에이전시", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/agency/list", "");
	}

	@RequestMapping(value = "/settle/sales/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleSalesList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		cpRequest.setData("grade", "지사", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/sales/list", "");
	}

	/*
	 * @RequestMapping(value = "/settle/mcht/list", method =
	 * RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE) public
	 * ModelAndView saelsSettleList(HttpServletRequest request,@RequestBody
	 * CPRequest cpRequest) { SessionUtil.setSearchGrade(request, cpRequest); CPDAO
	 * dao = new CPDAO(); dao.setTable("VW_SETTLE"); dao.
	 * setColumns("stlDay, SUM(payAmt) as payAmt, SUM(payFee) as payFee, SUM(payVat) as payVat, SUM(payCnt) as payCnt, SUM(rfdAmt) as rfdAmt, SUM(rfdFee) as rfdFee, SUM(rfdVat) as rfdVat, SUM(rfdCnt) as rfdCnt, SUM(stlAmt) as stlAmt"
	 * ); dao.setWhere("stlDay = (SELECT MAX(stlDay) FROM VW_SETTLE)"); CPSession
	 * cpSession = SessionUtil.get(request); if(cpSession.getGrade().equals("대행사")){
	 * dao.addWhere("distId", cpSession.getParentId(), CPDAO.eq); }else
	 * if(cpSession.getGrade().equals("에이전시")){ dao.addWhere("agencyId",
	 * cpSession.getParentId(), CPDAO.eq); }else
	 * if(cpSession.getGrade().equals("지사")){ dao.addWhere("salesId",
	 * cpSession.getParentId(), CPDAO.eq); }else
	 * if(cpSession.getGrade().equals("가맹점")){ dao.addWhere("mchtId",
	 * cpSession.getParentId(), CPDAO.eq); }
	 * 
	 * request.setAttribute("SUMMAP", dao.search().getRow(0));
	 * 
	 * SettleDAO settleDAO = new SettleDAO(); cpRequest.setData("grade", "가맹점",
	 * "eq", "", true); cpRequest.setData("stlAmt", "0", "ne", "", true);
	 * cpRequest.setData("stlDay", "", "", "desc", false);
	 * cpRequest.setData("memberName", "", "", "asc", false); RecordSet rset =
	 * settleDAO.list(cpRequest.data,cpRequest.page); return new
	 * CPRUtil(cpRequest).dataList(rset,settleDAO).setView(request,
	 * "/settle/mcht/list",""); }
	 */
	@RequestMapping(value = "/settle/mcht/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleMchtDAO settleDAO = new SettleMchtDAO();
		if(cpRequest.page.size ==20){
			cpRequest.page.size = 200;
		}
		cpRequest.setData("stlType", "C+0", "ne", "", true);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/mcht/list", "");
	}

	@RequestMapping(value = "/settle/payout/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView payoutList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		cpRequest.setData("status", "확정", "eq", "", true);
		cpRequest.setData("payStatus", "대기", "eq", "", true);
		CPSession session = SessionUtil.get(request);
		if (session.getGrade().equals("대행사")) {
			cpRequest.setData("grade", "에이전시", "eq", "", true);
		}

		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		request.setAttribute("AMOUNT_SUM", new SettleDAO().sumAmount(cpRequest.data).getRowFirst().getString("stlAmt"));
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/payout/list", "");
	}

	@RequestMapping(value = "/settle/detail", method = RequestMethod.POST)
	public ModelAndView detail(HttpServletRequest request, @RequestParam String grade, @RequestParam String stlId) throws Exception {
		String columns = "capId,trxId,mchtId,name,tmnId,trackId,capType,rfdType,rootTrxId,amount,vat,issuer,authCd,trxDay,regTime,stlType";
		LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
		thead.put("capId", "매입번호");
		thead.put("trxId", "거래번호");
		thead.put("mchtId", "가맹점ID");

		thead.put("name", "가맹점");
		if (grade.equals("stlDistId")) {
			columns += ", distName";
			thead.put("distName", "대행사");
		} else if (grade.equals("stlAgencyId")) {
			columns += ", agencyName";
			thead.put("agencyName", "에이전시");
		} else if (grade.equals("stlSalesId")) {
			columns += ", salesName";
			thead.put("salesName", "지사");
		} 

		thead.put("tmnId", "터미널ID");
		thead.put("trackId", "거래추적번호");
		thead.put("capType", "매입구분");
		thead.put("rfdType", "취소구분");
		thead.put("rootTrxId", "원거래번호");
		thead.put("amount", "금액");
		thead.put("vat", "VAT");
		thead.put("issuer", "매입사");
		thead.put("authCd", "승인번호");
		thead.put("stlType", "정산일 기준");

		if (grade.equals("stlDistId")) {
			columns += ", stlDistFee, stlDistRate, stlDiffDistFee, stlDiffDistRate,stlDistDay, stlDistId";
			thead.put("stlDistFee", "정산 수수료");
			thead.put("stlDistRate", "정산 기준 수수료율");
			thead.put("stlDiffDistFee", "차액정산 수수료");
			thead.put("stlDiffDistRate", "차액정산 기준 수수료율");
			thead.put("stlDistDay", "정산예정일");
			thead.put("stlDistId", "정산 ID");
		} else if (grade.equals("stlAgencyId")) {
			columns += ", stlAgencyFee, stlAgencyRate, stlDiffAgencyFee, stlDiffAgencyRate, stlAgencyDay, stlAgencyId";
			thead.put("stlAgencyFee", "정산 수수료");
			thead.put("stlAgencyRate", "정산 기준 수수료율");
			thead.put("stlDiffAgencyFee", "차액정산 수수료");
			thead.put("stlDiffAgencyRate", "차액정산 기준 수수료율");
			thead.put("stlAgencyDay", "정산예정일");
			thead.put("stlAgencyId", "정산 ID");
		} else if (grade.equals("stlSalesId")) {
			columns += ", stlSalesFee, stlSalesRate, stlDiffSalesFee, stlDiffSalesRate, stlSalesDay, stlSalesId";
			thead.put("stlSalesFee", "정산 수수료");
			thead.put("stlSalesRate", "정산 기준 수수료율");
			thead.put("stlDiffSalesFee", "차액정산 수수료");
			thead.put("stlDiffSalesRate", "차액정산 기준 수수료율");
			thead.put("stlSalesDay", "정산예정일");
			thead.put("stlSalesId", "정산 ID");
		} else if (grade.equals("stlId")) {
			columns += ",stlAmount, stlRate, stlFee, stlFeeVat, stlDay, stlId";
			thead.put("stlAmount", "정산 금액");
			thead.put("stlFee", "정산 수수료");
			thead.put("stlFeeVat", "정산 수수료 VAT");
			thead.put("stlRate", "정산 기준 수수료율");
			thead.put("stlDay", "정산예정일");
			thead.put("stlId", "정산 ID");
		}

		thead.put("trxDay", "거래일");
		thead.put("regTime", "거래시간");

		CPDAO dao = new CPDAO();
		dao.setTable("VW_TRX_CAP_LIST");
		dao.setColumns(columns);
		dao.addWhere(grade, stlId, CPDAO.eq);

		RecordSet recordSet = dao.search();
		if (recordSet.size() > 0) {
//			String filePath = "webexport";
//			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
//			CPUtil.setTemplateDirectory(filePath);
//			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

			CPDocument doc = new CPDocument();
			doc.title = "정산 대상 거래";
			XlsExport export = new XlsExport(doc);
			String link = "";

			try {
				link = export.makeExcel(thead, recordSet, true, true);
			} catch (Exception e) {
				link = e.getMessage();
			}
			
			CPResponse cpResponse = new CPResponse();
			Files file = new Files();
			file.link = link;
			cpResponse.file = file;
			
			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		return new ModelAndView();
	}
	
	
	@RequestMapping(value = "/download/xlsx")
	public void get(HttpServletRequest request,HttpServletResponse response,@RequestParam String fileName) throws Exception {
		String filePath = "upload"+ File.separator +"webexport";
		filePath = CPUtil.getCanonicalWebPath() + File.separator + filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;
		
	    File file = new File(filePath + fileName);
	    
	    if(file.isFile()) {
	    	
	    	response.setContentType( "application/download; UTF-8" );
	        response.setContentLength((int) file.length());

	    	
	        String header = request.getHeader( "User-Agent" );
	        String encodedFilename = "";

	    	if ( header.indexOf( "MSIE" ) > -1 ) {

	    		encodedFilename = URLEncoder.encode( fileName, "UTF-8" ).replaceAll( "\\+", "%20" );

	    	}

	    	else if ( header.indexOf( "Trident" ) > -1 ) { 

	    		encodedFilename = URLEncoder.encode( fileName, "UTF-8" ).replaceAll( "\\+", "%20" );

	    	}

	    	else if ( header.indexOf( "Chrome" ) > -1 ) {

	    		StringBuffer sb = new StringBuffer();

	    		for ( int i = 0; i < fileName.length(); i++ ) {

	    			char c = fileName.charAt( i );

	    			if ( c > '~' ) {

	    				sb.append( URLEncoder.encode( "" + c, "UTF-8" ) );

	    			}

	    			else {

	    				sb.append( c );

	    			}

	    		}

	    		encodedFilename = sb.toString();

	    	}

	    	else if ( header.indexOf( "Opera" ) > -1 ) {

	    		encodedFilename = "\"" + new String( fileName.getBytes( "UTF-8" ), "8859_1" ) + "\"";

	    	}

	    	else if ( header.indexOf( "Safari" ) > -1 ) {

	    		encodedFilename = "\"" + new String( fileName.getBytes( "UTF-8" ), "8859_1" ) + "\"";

//	    		encodedFilename = URLDecoder.decode( encodedFilename );

	    	}else{

	    		encodedFilename = "\"" + new String( fileName.getBytes( "UTF-8" ), "8859_1" ) + "\"";

//	    		encodedFilename = URLDecoder.decode( encodedFilename );

	    	}
	    	

	    	response.setHeader( "Content-Disposition", "attachment; filename=\"" + encodedFilename + "\";" );
	    	response.setHeader( "Content-Transfer-Encoding", "binary" );
	        response.setHeader("Pragma", "no-cache;");
		    response.setHeader("Expires", "-1;");
	        
	        OutputStream out = response.getOutputStream();
	
	        FileInputStream fis = null;
	        
	        try {	
	        	
	            fis = new FileInputStream(file);
	            FileCopyUtils.copy(fis, out);
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if(fis != null) {
	                try { 
	                    fis.close(); 
	                }catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        out.flush();
	        out.close();
	    }
		
	}

	@RequestMapping(value = "/settle/modal/{stlId}", method = RequestMethod.GET)
	public ModelAndView settleView(HttpServletRequest request, @PathVariable String stlId) {
		request.setAttribute("DATAMAP", new SettleDAO().getById(stlId).getRow(0));
		return new ModelAndView("/settle/modal");
	}

	@RequestMapping(value = "/settle/status/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("stlId", stlId, DAO.in);

		if (status.equals("확정") || status.equals("보류")) {
			dao.addWhere("status", "대기", DAO.ne);
		} else if (status.equals("대기")) {
			dao.addWhere("payStatus", "대기", DAO.ne);
		} else {
			logger.error("정산 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			return resultMap;
		}

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_SETTLE SET status = '" + status + "' WHERE stlId IN (" + stlId + ")")) {
				resultMap.put("result", "OK");
			} else {
				resultMap.put("result", "NOK");
				resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");

			}
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'대기' 상태가 아닌 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		return resultMap;
	}

	@RequestMapping(value = "/settle/paystatus/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settlePayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("status", "확정", DAO.ne);
		dao.addWhere("stlId", stlId, DAO.in);

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_SETTLE SET payStatus = '" + status + "', payOutDay = '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
				resultMap.put("result", "OK");
			} else {
				resultMap.put("result", "NOK");
			}
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "확정 또는 지불 상태를 확인해주세요.");
		}
		return resultMap;
	}

	@RequestMapping(value = "/settle/export", method = RequestMethod.POST)
	public @ResponseBody Object settleExport(HttpServletRequest request, @RequestParam String bankCd, @RequestParam String stlId) {
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("VW_SETTLE");
		//dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,CONCAT('(정산)',memberName) as memberName, stlId");
		dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,'사업자' as memberName, stlId");
		dao.addWhere("status", "확정", DAO.eq);
		dao.addWhere("stlId", stlId, DAO.in);
		RecordSet recordSet = dao.search();
		
		if(recordSet.size() < 1) {
			dao.initRecord();
			dao.setTable("VW_SETTLE_MCHT");
			dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,(stlAmount + (relsAmt - relsFee - relsVat) + deductAmt) as stlAmt,accntHolder,'' as a,'' as b,'사업자' as memberName, stlId");
			dao.addWhere("stlId", stlId, DAO.in);
			dao.addWhere("settleSvc", "일반", DAO.eq);
			recordSet = dao.search();
		}
		
		
		if (recordSet.size() > 0) {
			String filePath = "webexport";
			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
			CPUtil.setTemplateDirectory(filePath);
			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

			CPDocument doc = new CPDocument();

			XlsExport export = new XlsExport(doc);
			String link = "";
			LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
			boolean headerView = false;
			if (bankCd.equals("020")) {
				doc.title = "우리은행";
				thead.put("bankCd", "bankCd");
				thead.put("account", "account");
				thead.put("stlAmt", "stlAmt");
				thead.put("accntHolder", "accntHolder");
				thead.put("a", "a");
				thead.put("b", "b");
				thead.put("memberName", "memberName");
				thead.put("stlId", "stlId");
			} else if (bankCd.equals("004")) {
				doc.title = "국민은행";
				thead.put("bankCd", "bankCd");
				thead.put("account", "account");
				thead.put("stlAmt", "stlAmt");
				thead.put("accntHolder", "accntHolder");
				thead.put("memberName", "memberName");
			} else {
				doc.title = "하나은행";
				thead.put("bankName", "입금은행코드");
				thead.put("account", "입금계좌번호");
				thead.put("stlAmt", "이체금액");
				thead.put("accntHolder", "예상예금주");
				thead.put("memberName", "memberName");
				thead.put("stlId", "보내는분 통장표시내용");
				headerView = true;
			}
			doc.title += " 정산 지급 데이터_" + CommonUtil.getCurrentDate("yyMMddhhmmss");

			try {
				link = export.makeExcel(thead, recordSet, headerView, false);
			} catch (Exception e) {
				link = e.getMessage();
			}

			CPResponse cpResponse = new CPResponse();
			Files file = new Files();
			file.link = link;
			// file.auth = auth;
			cpResponse.file = file;
			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		SharedMap<String,Object> map = new SharedMap<String,Object>();
		map.put("message", "다운로드할 내역이 없습니다.");
		return map;
	}

	// ========================================================== 대표가맹점 정산
	@RequestMapping(value = "/settle/aggregator/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAggregatorList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		CPDAO dao = new CPDAO();
		dao.setTable("VW_SETTLE_SUB");
		dao.setColumns("stlDay, SUM(payAmt) as payAmt, SUM(payFee) as payFee, SUM(payVat) as payVat, SUM(payCnt) as payCnt, SUM(rfdAmt) as rfdAmt, SUM(rfdFee) as rfdFee, SUM(rfdVat) as rfdVat, SUM(rfdCnt) as rfdCnt, SUM(stlAmt) as stlAmt");
		dao.setWhere("stlDay = (SELECT MAX(stlDay) FROM VW_SETTLE_SUB)");
		CPSession cpSession = SessionUtil.get(request);
		if (cpSession.getGrade().equals("가맹점")) {
			dao.addWhere("mchtId", cpSession.getParentId(), CPDAO.eq);
		}

		request.setAttribute("SUMMAP", dao.search().getRow(0));

		SettleSubDAO settleDAO = new SettleSubDAO();
		// cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("dtlName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/aggregator/list", "");
	}

	@RequestMapping(value = "/settle/payoutsub/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView payoutSubList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleSubDAO settleDAO = new SettleSubDAO();
		cpRequest.setData("status", "확정", "eq", "", true);
		cpRequest.setData("payStatus", "대기", "eq", "", true);

		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/payoutsub/list", "");
	}

	@RequestMapping(value = "/settle/detail/sub", method = RequestMethod.POST)
	public @ResponseBody Object detailSub(HttpServletRequest request, @RequestParam String grade, @RequestParam String stlId) {
		String columns = "capId,trxId,mchtId,name,(SELECT name FROM PG_MCHT_TMN_DTL B WHERE VW_TRX_CAP_SUB.tmnId = B.tmnId) AS tmnName," + "tmnId,trackId,capType,rfdType,rootTrxId,amount,vat,issuer,authCd,trxDay,regTime,stlType,"
						+ "stlAmount, stlRate, stlFee, stlFeeVat, stlDay, stlId ";
		LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
		thead.put("capId", "매입번호");
		thead.put("trxId", "거래번호");
		thead.put("mchtId", "가맹점ID");
		thead.put("name", "가맹점");
		thead.put("tmnId", "가맹점ID");
		thead.put("tmnName", "상호");
		thead.put("tmnId", "터미널ID");
		thead.put("trackId", "거래추적번호");
		thead.put("capType", "매입구분");
		thead.put("rfdType", "취소구분");
		thead.put("rootTrxId", "원거래번호");
		thead.put("amount", "금액");
		thead.put("vat", "VAT");
		thead.put("issuer", "매입사");
		thead.put("authCd", "승인번호");
		thead.put("stlType", "정산일 기준");
		thead.put("stlAmount", "정산 금액");
		thead.put("stlFee", "정산 수수료");
		thead.put("stlFeeVat", "정산 수수료 VAT");
		thead.put("stlRate", "정산 기준 수수료율");
		thead.put("stlDay", "정산예정일");
		thead.put("stlId", "정산 ID");

		thead.put("trxDay", "거래일");
		thead.put("regTime", "거래시간");

		CPDAO dao = new CPDAO();
		dao.setTable("VW_TRX_CAP_SUB");
		dao.setColumns(columns);
		dao.addWhere(grade, stlId, CPDAO.eq);

		RecordSet recordSet = dao.search();
		if (recordSet.size() > 0) {
			String filePath = "webexport";
			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
			CPUtil.setTemplateDirectory(filePath);
			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

			CPDocument doc = new CPDocument();
			doc.title = "정산 대상 거래";
			XlsExport export = new XlsExport(doc);
			String link = "";

			try {
				link = export.makeExcel(thead, recordSet, true, true);
			} catch (Exception e) {
				link = e.getMessage();
			}

			CPResponse cpResponse = new CPResponse();
			Files file = new Files();
			file.link = link;
			cpResponse.file = file;

			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		SharedMap<String,Object> map = new SharedMap<String,Object>();
		map.put("message", "다운로드할 내역이 없습니다.");
		return map;
	}

	@RequestMapping(value = "/settle/modal/sub/{stlId}", method = RequestMethod.GET)
	public ModelAndView settleSubView(HttpServletRequest request, @PathVariable String stlId) {
		request.setAttribute("DATAMAP", new SettleSubDAO().getById(stlId).getRow(0));
		return new ModelAndView("/settle/modal");
	}

	@RequestMapping(value = "/settle/status/sub/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleSubStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE_SUB");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("stlId", stlId, DAO.in);

		if (status.equals("확정") || status.equals("보류")) {
			dao.addWhere("status", "대기", DAO.ne);
		} else if (status.equals("대기")) {
			dao.addWhere("payStatus", "대기", DAO.ne);
		} else {
			logger.error("정산 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			return resultMap;
		}

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_SETTLE_SUB SET status = '" + status + "' WHERE stlId IN (" + stlId + ")")) {
				resultMap.put("result", "OK");
			} else {
				resultMap.put("result", "NOK");
				resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");

			}
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'대기' 상태가 아닌 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		return resultMap;
	}

	@RequestMapping(value = "/settle/paystatus/sub/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleSubPayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE_SUB");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("status", "확정", DAO.ne);
		dao.addWhere("stlId", stlId, DAO.in);

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_SETTLE_SUB SET payStatus = '" + status + "', payOutDay = '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
				resultMap.put("result", "OK");
			} else {
				resultMap.put("result", "NOK");
			}
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "확정 또는 지불 상태를 확인해주세요.");
		}
		return resultMap;
	}

	@RequestMapping(value = "/settle/sub/export", method = RequestMethod.POST)
	public @ResponseBody Object settleSubExport(HttpServletRequest request, @RequestParam String bankCd, @RequestParam String stlId) {
		CPDAO dao = new CPDAO();
		dao.setTable("VW_SETTLE_SUB");
		dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,'사업자' as dtlName, stlId");
		dao.addWhere("status", "확정", DAO.eq);
		dao.addWhere("stlId", stlId, DAO.in);
		RecordSet recordSet = dao.search();
		if (recordSet.size() > 0) {
			String filePath = "webexport";
			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
			CPUtil.setTemplateDirectory(filePath);
			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

			CPDocument doc = new CPDocument();

			XlsExport export = new XlsExport(doc);
			String link = "";
			LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
			if (bankCd.equals("020")) {
				doc.title = "우리은행";
				thead.put("bankCd", "bankCd");
				thead.put("account", "account");
				thead.put("stlAmt", "stlAmt");
				thead.put("accntHolder", "accntHolder");
				thead.put("a", "a");
				thead.put("b", "b");
				thead.put("dtlName", "dtlName");
				thead.put("stlId", "stlId");
			} else if (bankCd.equals("004")) {
				doc.title = "국민은행";
				thead.put("bankCd", "bankCd");
				thead.put("account", "account");
				thead.put("stlAmt", "stlAmt");
				thead.put("accntHolder", "accntHolder");
				thead.put("dtlName", "dtlName");
			} else {
				doc.title = "하나은행";
				thead.put("bankName", "bankName");
				thead.put("account", "account");
				thead.put("stlAmt", "stlAmt");
				thead.put("accntHolder", "accntHolder");
				thead.put("dtlName", "dtlName");
				thead.put("stlId", "stlId");
			}
			doc.title += " 정산 지급 데이터";

			try {
				link = export.makeExcel(thead, recordSet, true, false);
			} catch (Exception e) {
				link = e.getMessage();
			}

			CPResponse cpResponse = new CPResponse();
			Files file = new Files();
			file.link = link;
			// file.auth = auth;
			cpResponse.file = file;

			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		SharedMap<String,Object> map = new SharedMap<String,Object>();
		map.put("message", "다운로드할 내역이 없습니다.");
		return map;
	}

	@RequestMapping(value = "/settle/calc/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView calcList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		request.setAttribute("SUMMAP", new TrxCapDAO().calcPaySum(cpRequest.data).getRow(0));

		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset = trxCapDAO.calcPayList(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, trxCapDAO).setView(request, "/settle/calc/list", "");
	}

	// ==============================

	@RequestMapping(value = "/settle/taxexport/{date}", method = RequestMethod.POST)
	public ModelAndView taxExport(HttpServletRequest request, @PathVariable String date) {
		date = date.replace("-", "");
		CPDAO dao = new CPDAO();
		dao.setTable("VW_TRX_CAP A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId LEFT JOIN PG_MCHT C ON A.mchtId = C.mchtId");
		dao.setColumns("MAX(trxDay) as endDay, A.taxId, A.mchtId, A.name, SUM(amount) AS amount, SUM(vat) AS vat, " + "SUM(stlAmount) AS stlAmount, AVG(stlRate) as stlRate, " + "SUM(stlFee) AS stlFee, SUM(stlFeeVat) AS stlFeeVat, "
						+ "SUM(stlLoanFee) AS stlLoanFee, SUM(benefit) AS benefit, " + "FN_AES_DEC(B.identity) as identity, B.ceoName, B.compName, B.addr1, B.addr2, B.email ,C.bizCategory, C.bizType");
		dao.setWhere("SUBSTR(A.trxDay,1,6) = '" + date + "'");
		dao.setGroupBy("taxId");
		dao.setOrderBy("A.name");
		RecordSet rset = dao.search();

		if (rset.size() > 0) {
			List<SharedMap<String, Object>> targetList = rset.getRows();
			SharedMap<String, Object> senderMap = new SharedMap<String, Object>();
			senderMap.put("identity", "6758600152");
			senderMap.put("compName", "㈜부국위너스");
			senderMap.put("ceoName", "권규미");
			senderMap.put("addr1", "부산광역시 해운대구 센텀중앙로97");
			senderMap.put("addr2", "A동 2510호");
			senderMap.put("bizCategory", "서비스");
			senderMap.put("bizType", "전자금융업외");
			senderMap.put("email", "bukook@bkwinners.com");

			TaxExport taxExport = new TaxExport();

			String link = "";
			try {
				link = taxExport.makeTaxExcel(senderMap, targetList, SessionUtil.getUserId(request));
			} catch (Exception e) {
				link = e.getMessage();
			}

			CPResponse cpResponse = new CPResponse();
			Files file = new Files();
			file.link = link;
			// file.auth = auth;
			cpResponse.file = file;

			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		return new ModelAndView();
	}

	/*
	 * 가맹점 지급정산 - 리스크 여부와 상관없이 정상 가맹점 지급
	 */
	/*
	@RequestMapping(value = "/settle/mcht/make/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView makeSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO dao = new SettleDAO();
		
		// 가맹점 지급정산 TEMP 데이터 삭제 - 2021.05.25 추가
		dao.deleteSettleMchtTemp();
		
		StringBuffer query = new StringBuffer();
		
		query.append("( SELECT concat(DATE_FORMAT(now(),'%y%m%d'), substr(uuid(),1,8)) as idx ,C1.*,(payAmt - payFee - payVat) + (rfdAmt - rfdVat - rfdFee) as stlAmount"
				+ " ,C2.bankCd,C2.bankName,C2.account,C2.accntHolder FROM ");
		query.append("(SELECT T1.*,IFNULL(T2.relsAmt,0) AS relsAmt,IFNULL(T2.relsFee,0) AS relsFee,IFNULL(T2.relsVat,0) AS relsVat,IFNULL(T2.relsCnt,0) AS relsCnt, ");
		query.append(" IFNULL(T3.deductAmount,0) as deductAmt, IFNULL(T4.manualRelsAmt,0) as manualRelsAmt, IFNULL(T5.ddctAmt,0) as ddctAmt, IF(T5.ddctAmt IS NULL, 'X', 'O') AS ddctTypeTemp ");
		query.append(" FROM (SELECT T6.mchtId, T6.name as mchtName, T6.ceoName,'지급대기' as `status` ,max(T6.stlDay) stlDay, min(T6.stlDay) stlStartDay , min(T6.trxDay) startDay ,max(T6.trxDay) endDay, T6.stlType,");
		query.append(" SUM(IF(T6.capType ='매입',T6.amount,0)) as payAmt, SUM(IF(T6.capType ='매입',T6.stlFee,0)) as payFee, SUM(IF(T6.capType ='매입',T6.stlFeeVat,0)) as payVat, SUM(IF(T6.capType ='매입',1,0)) as payCnt, ");
		query.append(" SUM(IF(T6.capType ='매입취소',T6.amount,0)) as rfdAmt, SUM(IF(T6.capType ='매입취소',T6.stlFee,0)) as rfdFee, SUM(IF(T6.capType ='매입취소',T6.stlFeeVat,0)) as rfdVat, SUM(IF(T6.capType ='매입취소',1,0)) rfdCnt, ");
		query.append(" 0 as holdAmt, 0 AS holdFee, 0 as holdVat, 0 as holdCnt, 0 as loanDeductAmt,");
		
		query.append(" SUM(T6.stlDistFee) as distFee,");
		query.append(" SUM(T6.stlAgencyFee) as agencyFee,");
		query.append(" SUM(T6.stlVanFee) as vanFee,");
		query.append(" SUM(T6.stlDiffAmt) as diffAmt,");
		query.append(" SUM(T6.benefit) as benefit,");
		query.append(" MAX(T6.stlRate) as stlRate,");
		query.append(" MAX(T6.taxId) as taxId");
//		query.append(" FROM VW_TRX_CAP T6 left join PG_TRX_REALTIME_PAY T7 ON T6.trxId = T7.trxId WHERE T6.stlStatus='정산대기' AND T6.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND T6.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND T7.trxId IS null ");
		query.append(" FROM VW_TRX_CAP T6 WHERE T6.stlStatus='정산대기' AND T6.stlType not like 'A%' AND stlType != 'D+0' AND stlType not like 'B%' AND T6.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND T6.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			query.append(" AND T6.stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("settleSvc"))) {
			if(cpRequest.getKeyValue("settleSvc").equals("충전정산")) {
				query.append(" AND T6.stlType like 'C%' ");
			}else {
				query.append(" AND T6.stlType like 'D%' ");
			}
		}
		query.append(" GROUP BY T6.mchtId, T6.stlType ");
		query.append(" ) AS T1 LEFT OUTER JOIN ");
		query.append(" ( SELECT B.mchtId ,SUM(B.amount) as relsAmt , SUM(stlFee) as relsFee,  SUM(stlFeeVat) as relsVat , SUM(1) as relsCnt");
		query.append("   FROM PG_SETTLE_HOLD A, VW_TRX_CAP B  WHERE A.capId = B.capId and A.`status` ='반환요청' AND B.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND B.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
		query.append(" group by B.mchtId");
		query.append(" ) AS T2 ON T1.mchtId = T2.mchtId LEFT OUTER JOIN ");
		query.append(" ( SELECT mchtId,SUM(deductAmount) deductAmount FROM VW_COLLECT_SETTLE_DTL_STATUS WHERE status = '확정' and deductAmount < 0 and deductStlId ='' group by mchtId");
		query.append(" ) AS T3 ON T1.mchtId = T3.mchtId LEFT OUTER JOIN ");
		query.append(" ( SELECT mchtId,SUM(ABS(amount)) manualRelsAmt FROM PG_MCHT_DEPOSIT WHERE `depType` ='반환요청' AND stlId = '' AND status = '생성' group by mchtId");
		query.append(" ) AS T4 ON T1.mchtId = T4.mchtId ");
		query.append(" LEFT JOIN ( SELECT mchtId, SUM(ddctAmt) AS ddctAmt FROM PG_SETTLE_DDCT WHERE stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND stlStatus = '정산대기' GROUP BY mchtId) T5 ON T1.mchtId = T5.mchtId");
	
		query.append(" ) AS C1 LEFT OUTER JOIN PG_MCHT_TAX C2 ON C1.taxId = C2.taxId) A");

		dao.setTable(query.toString());
		dao.setColumns("A.*, case when ddctTypeTemp = 'X' then 'X' when (stlAmount - ddctAmt) < 0 then  'N' ELSE 'Y' END AS ddctType ");
		dao.setOrderBy("A.mchtName asc");
		
		cpRequest.deleteKeyData("stlEndDay");
		cpRequest.deleteKeyData("stlStartDay");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			cpRequest.deleteKeyData("stlType");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("settleSvc"))) {
			cpRequest.deleteKeyData("settleSvc");
		}
		RecordSet rset = dao.search(cpRequest.data);
		
		LoanSettleDAO loanDAO = new LoanSettleDAO();
		
		// 대출정산: 차감금액, 정산금액 설정
		int i=0;
		for(SharedMap<String,Object> data : rset.getRows()) {
			SharedMap<String, Object> loanData = loanDAO.getLoanByMcthId(data.getString("mchtId"));
			SharedMap<String,Object> loanDtlData = loanDAO.getBeforeLoanDtl(data.getString("mchtId"));
			if(loanData != null) {
				long stlAmout = data.getLong("stlAmount");
				int stlDay = data.getInt("stlDay");
				int loanDay = loanData.getInt("loanDay");
			
				long totLoanAmt = loanData.getLong("conAmt") + loanDtlData.getLong("delayAmt") - loanDtlData.getLong("holdAmt");
				
				if(loanData != null && loanDay < stlDay) {
					if(stlAmout > totLoanAmt) {
						rset.getRow(i).replace("stlAmount", stlAmout - totLoanAmt);
						rset.getRow(i).replace("loanDeductAmt", -totLoanAmt);
					} else {
						rset.getRow(i).replace("stlAmount", 0);
						rset.getRow(i).replace("loanDeductAmt", -stlAmout);
					}
				} else {
					rset.getRow(i).replace("loanDeductAmt", 0);
				}
			}
			
			i++;
		}

		if (cpRequest.type.equalsIgnoreCase("list")) {
			insertMchtSettleTemp(rset.getRows(), request);
		}

		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/make/list", "");
	}
	*/
	
	
//	  가맹점 지급정산 - 리스크 보류금액 설정
	
	@RequestMapping(value = "/settle/mcht/make/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView makeSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO dao = new SettleDAO();
		StringBuffer query = new StringBuffer();
		
		query.append("( SELECT concat(DATE_FORMAT(now(),'%y%m%d'), substr(uuid(),1,8)) as idx ,C1.*,(payAmt - payFee - payVat) + (rfdAmt - rfdVat - rfdFee) as stlAmount"
				+ " ,C2.bankCd,C2.bankName,C2.account,C2.accntHolder FROM ");
		query.append("(SELECT T1.*,IFNULL(T2.relsAmt,0) AS relsAmt,IFNULL(T2.relsFee,0) AS relsFee,IFNULL(T2.relsVat,0) AS relsVat,IFNULL(T2.relsCnt,0) AS relsCnt, ");
		query.append(" IFNULL(T3.deductAmount,0) as deductAmt, IFNULL(T4.manualRelsAmt,0) as manualRelsAmt, IFNULL(T5.ddctAmt,0) as ddctAmt, IF(T5.ddctAmt IS NULL, 'X', 'O') AS ddctTypeTemp ");
		query.append(" FROM (SELECT T6.mchtId, T6.name as mchtName, T6.ceoName,'지급대기' as `status` ,max(T6.stlDay) stlDay, min(T6.stlDay) stlStartDay , min(T6.trxDay) startDay ,max(T6.trxDay) endDay, T6.stlType,");
		query.append(" SUM(IF(T6.capType ='매입' and T6.risk ='',T6.amount,0)) as payAmt, SUM(IF(T6.capType ='매입' and T6.risk ='',T6.stlFee,0)) as payFee, SUM(IF(T6.capType ='매입' and T6.risk ='',T6.stlFeeVat,0)) as payVat, SUM(IF(T6.capType ='매입' and T6.risk ='' ,1,0)) as payCnt, ");
		query.append(" SUM(IF(T6.capType ='매입취소',T6.amount,0)) as rfdAmt, SUM(IF(T6.capType ='매입취소',T6.stlFee,0)) as rfdFee, SUM(IF(T6.capType ='매입취소',T6.stlFeeVat,0)) as rfdVat, SUM(IF(T6.capType ='매입취소',1,0)) rfdCnt, ");
		query.append(" SUM(IF(T6.capType ='매입' and T6.risk !='',T6.amount,0)) as holdAmt, SUM(IF(T6.capType ='매입' and T6.risk !='',T6.stlFee,0)) AS holdFee, SUM(IF(T6.capType ='매입' and T6.risk !='',T6.stlFeeVat,0)) as holdVat, SUM(IF(T6.capType ='매입' and T6.risk !='',1,0)) as holdCnt, ");
		
		query.append(" SUM(IF(T6.risk ='',T6.stlDistFee,0)) as distFee,");
		query.append(" SUM(IF(T6.risk ='',T6.stlAgencyFee,0)) as agencyFee,");
		query.append(" SUM(IF(T6.risk ='',T6.stlVanFee,0)) as vanFee,");
		query.append(" SUM(IF(T6.risk ='',T6.stlDiffAmt,0)) as diffAmt,");
		query.append(" SUM(IF(T6.risk ='',T6.benefit,0)) as benefit,");
		query.append(" MAX(T6.stlRate) as stlRate,");
		query.append(" MAX(T6.taxId) as taxId");
//		query.append(" FROM VW_TRX_CAP T6 left join PG_TRX_REALTIME_PAY T7 ON T6.trxId = T7.trxId WHERE T6.stlStatus='정산대기' AND T6.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND T6.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND T7.trxId IS null ");
		query.append(" FROM VW_TRX_CAP T6 WHERE T6.stlStatus='정산대기' AND T6.stlType not like 'A%' AND stlType != 'D+0' AND T6.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND T6.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			query.append(" AND T6.stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("settleSvc"))) {
			if(cpRequest.getKeyValue("settleSvc").equals("충전정산")) {
				query.append(" AND T6.stlType like 'C%' ");
			}else {
				query.append(" AND T6.stlType like 'D%' ");
			}
		}
		query.append(" GROUP BY T6.mchtId, T6.stlType ");
		query.append(" ) AS T1 LEFT OUTER JOIN ");
		query.append(" ( SELECT B.mchtId ,SUM(B.amount) as relsAmt , SUM(stlFee) as relsFee,  SUM(stlFeeVat) as relsVat , SUM(1) as relsCnt");
		query.append("   FROM PG_SETTLE_HOLD A, VW_TRX_CAP B  WHERE A.capId = B.capId and A.`status` ='반환요청' AND B.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND B.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
		query.append(" group by B.mchtId");
		query.append(" ) AS T2 ON T1.mchtId = T2.mchtId LEFT OUTER JOIN ");
		query.append(" ( SELECT mchtId,SUM(deductAmount) deductAmount FROM VW_COLLECT_SETTLE_DTL_STATUS WHERE status = '확정' and deductAmount < 0 and deductStlId ='' group by mchtId");
		query.append(" ) AS T3 ON T1.mchtId = T3.mchtId LEFT OUTER JOIN ");
		query.append(" ( SELECT mchtId,SUM(ABS(amount)) manualRelsAmt FROM PG_MCHT_DEPOSIT WHERE `depType` ='반환요청' AND stlId = '' AND status = '생성' group by mchtId");
		query.append(" ) AS T4 ON T1.mchtId = T4.mchtId ");
		query.append(" LEFT JOIN ( SELECT mchtId, SUM(ddctAmt) AS ddctAmt FROM PG_SETTLE_DDCT WHERE stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND stlStatus = '정산대기' GROUP BY mchtId) T5 ON T1.mchtId = T5.mchtId");
		
		query.append(" ) AS C1 LEFT OUTER JOIN PG_MCHT_TAX C2 ON C1.taxId = C2.taxId) A");
		
		dao.setTable(query.toString());
		dao.setColumns("A.*, case when ddctTypeTemp = 'X' then 'X' when (stlAmount - ddctAmt) < 0 then  'N' ELSE 'Y' END AS ddctType ");
		dao.setOrderBy("A.mchtName asc");
		
		cpRequest.deleteKeyData("stlEndDay");
		cpRequest.deleteKeyData("stlStartDay");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			cpRequest.deleteKeyData("stlType");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("settleSvc"))) {
			cpRequest.deleteKeyData("settleSvc");
		}
		RecordSet rset = dao.search(cpRequest.data);
		
		if (cpRequest.type.equalsIgnoreCase("list")) {
			insertMchtSettleTemp(rset.getRows(), request);
		}
		
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/make/list", "");
	}
 

	public int insertMchtSettleTemp(List<SharedMap<String, Object>> mchtSettleTempList, HttpServletRequest request) {
		int inserted = 0;
		logger.debug("insert MchtSettleTemp batch : {}", mchtSettleTempList.size());
		String query = "INSERT INTO `PG_SETTLE_MCHT_TEMP` (`idx`,`mchtId`,`status`,`stlDay`,`stlStartDay`,`startDay`,`endDay`,`payAmt`,`payFee`,`payVat`,`payCnt`,`rfdAmt`,`rfdFee`,`rfdVat`,`rfdCnt`,`holdAmt`,`holdFee`,`holdVat`,`holdCnt`,`relsAmt`,`relsFee`,`relsVat`,`relsCnt`,`distFee`,`agencyFee`,`vanFee`,`diffAmt`,`benefit`,`deductAmt`,`manualRelsAmt`,`manualDeductAmt`,`loanDeductAmt`,`stlAmount`,`ddctAmt`,`ddctType`,`taxId`,`bankCd`,`bankName`,`account`,`accntHolder`, `stlRate`,`stlType`, `settleSvc`,`regId`, `regDay`) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;

			for (SharedMap<String, Object> map : mchtSettleTempList) {
				int i = 1;
				pstmt.setString(i++, map.getString("idx"));
				pstmt.setString(i++, map.getString("mchtId"));
				pstmt.setString(i++, map.getString("status"));
				pstmt.setString(i++, map.getString("stlDay"));
				pstmt.setString(i++, map.getString("stlStartDay"));
				pstmt.setString(i++, map.getString("startDay"));
				pstmt.setString(i++, map.getString("endDay"));
				pstmt.setLong(i++, map.getLong("payAmt"));
				pstmt.setLong(i++, map.getLong("payFee"));
				pstmt.setLong(i++, map.getLong("payVat"));
				pstmt.setLong(i++, map.getLong("payCnt"));
				pstmt.setLong(i++, map.getLong("rfdAmt"));
				pstmt.setLong(i++, map.getLong("rfdFee"));
				pstmt.setLong(i++, map.getLong("rfdVat"));
				pstmt.setLong(i++, map.getLong("rfdCnt"));
				pstmt.setLong(i++, map.getLong("holdAmt"));
				pstmt.setLong(i++, map.getLong("holdFee"));
				pstmt.setLong(i++, map.getLong("holdVat"));
				pstmt.setLong(i++, map.getLong("holdCnt"));
				pstmt.setLong(i++, map.getLong("relsAmt"));
				pstmt.setLong(i++, map.getLong("relsFee"));
				pstmt.setLong(i++, map.getLong("relsVat"));
				pstmt.setLong(i++, map.getLong("relsCnt"));
				pstmt.setLong(i++, map.getLong("distFee"));
				pstmt.setLong(i++, map.getLong("agencyFee"));
				pstmt.setLong(i++, map.getLong("vanFee"));
				pstmt.setLong(i++, map.getLong("diffAmt"));
				pstmt.setLong(i++, map.getLong("benefit"));
				pstmt.setLong(i++, map.getLong("deductAmt"));
				pstmt.setLong(i++, map.getLong("manualRelsAmt"));
				pstmt.setLong(i++, map.getLong("manualDeductAmt"));
				pstmt.setLong(i++, map.getLong("loanDeductAmt"));
				pstmt.setLong(i++, map.getLong("stlAmount"));
				pstmt.setLong(i++, map.getLong("ddctAmt"));
				pstmt.setString(i++, map.getString("ddctType"));
				pstmt.setString(i++, map.getString("taxId"));
				pstmt.setString(i++, map.getString("bankCd"));
				pstmt.setString(i++, map.getString("bankName"));
				pstmt.setString(i++, map.getString("account"));
				pstmt.setString(i++, map.getString("accntHolder"));
				pstmt.setDouble(i++, map.getDouble("stlRate"));
				pstmt.setString(i++, map.getString("stlType"));
				if(map.getString("stlType").indexOf("C") > -1) {
					pstmt.setString(i++, "충전정산");
				}else {
					pstmt.setString(i++, "일반");
				}
				pstmt.setString(i++, SessionUtil.getUserId(request));
				pstmt.setString(i++, CommonUtil.getCurrentDate("yyyyMMdd"));
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch MchtSettleTemp error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
	
	public String AddDate(String strDate, int year, int month, int day) throws Exception { 
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd"); Calendar cal = Calendar.getInstance(); 
		Date dt = dtFormat.parse(strDate); 
		cal.setTime(dt); cal.add(Calendar.YEAR, year); 
		cal.add(Calendar.MONTH, month); 
		cal.add(Calendar.DATE, day); 
		return dtFormat.format(cal.getTime()); 
	}

	

	@RequestMapping(value = "/settle/mcht/make/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMcht(HttpServletRequest request, @PathVariable String status, @RequestBody String idx) throws Exception {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STATUS: {}, idx: {}", status, idx);
		resultMap.put("result", "NOK");
		resultMap.put("msg", "확정 또는 지불 상태를 확인해주세요.");

		DAO dao = new DAO();
		dao.setTable("PG_SETTLE_MCHT_TEMP");
		dao.setColumns("*");
		dao.addWhere("idx", idx, DAO.in);
		List<SharedMap<String, Object>> resultList = dao.search().getRows();

		for (SharedMap<String, Object> eachMap : resultList) {
			dao.initRecord();
			String stlId = TrxDAO.getSettleId();
			
			// 대출정산 추가
			LoanSettleDAO loanDAO = new LoanSettleDAO();
			SharedMap<String, Object> loanData = loanDAO.getLoanByMcthId(eachMap.getString("mchtId"));
			
			if(loanData != null && loanData.getInt("loanDay") < eachMap.getInt("stlDay")) {
				
				SharedMap<String, Object> loanStlMap = new SharedMap<String, Object>();
				
				// 공통값
				loanStlMap.put("loanId", loanData.getString("loanId"));
				loanStlMap.put("distId", loanData.getString("distId"));
				loanStlMap.put("agencyId", loanData.getString("agencyId"));
				loanStlMap.put("salesId", loanData.getString("salesId"));
				loanStlMap.put("submitId", loanData.getString("submitId"));
				loanStlMap.put("submitName", loanData.getString("submitName"));
				loanStlMap.put("submitGrade", loanData.getString("submitGrade"));
				loanStlMap.put("mchtId", loanData.getString("mchtId"));
				loanStlMap.put("settleType",loanData.getString("settleType"));
				loanStlMap.put("conCnt",loanData.getInt("conCnt"));
				loanStlMap.put("amount",loanData.getLong("amount"));
				loanStlMap.put("conAmt",loanData.getLong("conAmt"));
				loanStlMap.put("regId", eachMap.getString("regId"));
				loanStlMap.put("regDay", eachMap.getString("regDay"));
				
				// 현재 정산 전 날들(평일)의 정산이 없는 경우
				String BfStlDay = loanDAO.getBeforeStlDay(loanData.getString("loanId"));
				String weekDay = "";
				String holidayStatus = "";
				int i=1;
				while(!eachMap.getString("stlDay").equals(weekDay)) {
					weekDay = AddDate(BfStlDay, 0, 0, i);
					if(eachMap.getString("stlDay").equals(weekDay)) {
						break;
					}
					holidayStatus = loanDAO.getHoliday(weekDay);
					if(holidayStatus.equals("no")) {
						String loanStlId1 = loanDAO.getLoanSettleStlId();
						SharedMap<String,Object> loanDtlData1 = loanDAO.getBeforeLoanDtl(eachMap.getString("mchtId"));
						loanStlMap.put("loanStlId",loanStlId1);
						loanStlMap.put("stlId","");
						loanStlMap.put("trxDay",weekDay);
						loanStlMap.put("loanType","연체");
						loanStlMap.put("totPayAmt",loanDtlData1.getLong("totPayAmt"));
						loanStlMap.put("payAmt",0);
						loanStlMap.put("delayAmt",loanDtlData1.getLong("conAmt")+loanDtlData1.getLong("delayAmt"));
						loanStlMap.put("holdAmt",loanDtlData1.getLong("holdAmt"));
						loanStlMap.put("balance",loanDtlData1.getLong("balance"));
						loanStlMap.put("payCnt",loanDtlData1.getInt("payCnt"));
						loanStlMap.put("delayCnt",loanDtlData1.getInt("delayCnt")+1);
						loanStlMap.put("paySession",loanDtlData1.getInt("paySession")+1);
						loanStlMap.put("payCk","N");
						loanStlMap.put("delayCk","Y");
						loanDAO.updateLoan(loanStlMap);
						loanDAO.insertDtl(loanStlMap);
					}
					i++;
				}
				
				int intStlDay = Integer.parseInt(eachMap.getString("stlDay"));
				int intWeekDay = Integer.parseInt(weekDay);
				
				String loanStlId2 = loanDAO.getLoanSettleStlId();
				SharedMap<String,Object> loanDtlData2 = loanDAO.getBeforeLoanDtl(eachMap.getString("mchtId"));
				
				if(intStlDay == intWeekDay) {
					loanStlMap.put("loanStlId", loanStlId2);
					loanStlMap.put("stlId", stlId);
					loanStlMap.put("trxDay", intWeekDay);
					loanStlMap.put("paySession", loanDtlData2.getInt("paySession") + 1);
					
					long stlAmount = eachMap.getLong("stlAmount");
					long loanDeductAmt1 = eachMap.getLong("loanDeductAmt");
					long oldDelayAmt = loanDtlData2.getLong("delayAmt");
					
					// 이전 날들의 가맹점 정산이 없을 시 대출정산 차감금액, 가맹점 정산금액 재설정
					if(loanDtlData2.getString("stlId").equals("") && loanDtlData2.getString("loanType").equals("연체") && oldDelayAmt <= stlAmount) {
						eachMap.replace("loanDeductAmt", loanDeductAmt1);
						eachMap.replace("stlAmount", stlAmount);
					} else if(loanDtlData2.getString("stlId").equals("") && loanDtlData2.getString("loanType").equals("연체") && oldDelayAmt > stlAmount) {
						eachMap.replace("loanDeductAmt", -stlAmount);
						eachMap.replace("stlAmount", 0);
					}
					
					long loanDeductAmt2 = eachMap.getLong("loanDeductAmt");
					long oldHoldAmt = loanDtlData2.getLong("holdAmt");
					long newHoldAmt = oldHoldAmt - loanDeductAmt2;
					long conAmt = loanDtlData2.getLong("conAmt");
					long oldTotPayAmt = loanDtlData2.getLong("totPayAmt");
					long oldBalance = loanDtlData2.getLong("balance");
					int oldPayCnt = loanDtlData2.getInt("payCnt");
					int oldDelayCnt = loanDtlData2.getInt("delayCnt");
					
					// 정상 상환
					if(newHoldAmt >= conAmt + oldDelayAmt) {
						loanStlMap.put("loanType", "상환");
						loanStlMap.put("totPayAmt", oldTotPayAmt + conAmt + oldDelayAmt);
						loanStlMap.put("payAmt", conAmt + oldDelayAmt);
						loanStlMap.put("delayAmt", oldDelayAmt - loanStlMap.getLong("payAmt") + conAmt);
						if(loanDtlData2.getString("loanType").equals("연체")) {
							while(newHoldAmt >= 10000) {
								newHoldAmt -= 10000;
							}
							loanStlMap.put("holdAmt", newHoldAmt);
						} else {
							loanStlMap.put("holdAmt", oldHoldAmt);
						}
						loanStlMap.put("balance", oldBalance - conAmt - oldDelayAmt);
						loanStlMap.put("payCnt", loanStlMap.getInt("paySession"));
						loanStlMap.put("delayCnt", 0);
						loanStlMap.put("payCk","Y");
						loanStlMap.put("delayCk","N");
						
					// 약정금액만 처리하는 경우
					} else if (newHoldAmt >= conAmt && newHoldAmt < oldDelayAmt + conAmt){
						loanStlMap.put("loanType", "연체");
						loanStlMap.put("totPayAmt", oldTotPayAmt + conAmt);
						loanStlMap.put("payAmt", conAmt);
						loanStlMap.put("delayAmt", oldDelayAmt);
						loanStlMap.put("holdAmt", oldHoldAmt);
						loanStlMap.put("balance", oldBalance - conAmt);
						loanStlMap.put("payCnt", oldPayCnt + 1);
						loanStlMap.put("delayCnt", oldDelayCnt);
						loanStlMap.put("payCk","N");
						loanStlMap.put("delayCk","Y");
					// 보류금액 합계가 약정금액 미만이고, 만원 이상인 경우
					} else if (newHoldAmt >= 10000 && newHoldAmt < conAmt) {	
						loanStlMap.put("loanType", "연체");
						int payAmt = 0;
						while(newHoldAmt >= 10000) {
							payAmt += 10000;
							newHoldAmt -= 10000;
						}
						loanStlMap.put("totPayAmt",oldTotPayAmt + payAmt);
						loanStlMap.put("payAmt", payAmt);
						loanStlMap.put("delayAmt", oldDelayAmt + conAmt - payAmt);
						loanStlMap.put("holdAmt", newHoldAmt);
						loanStlMap.put("balance", oldBalance - payAmt);
						if(loanStlMap.getLong("delayAmt") == 0) {
							loanStlMap.put("payCnt", oldPayCnt + 1);
							loanStlMap.put("delayCnt", oldDelayCnt);
						}else {
							loanStlMap.put("payCnt", oldPayCnt);
							loanStlMap.put("delayCnt", oldDelayCnt + 1);
						}
						loanStlMap.put("payCk","N");
						loanStlMap.put("delayCk","Y");
					// 상환 할 금액이 없을 경우
					} else {	
						loanStlMap.put("loanType", "연체");
						loanStlMap.put("totPayAmt", oldTotPayAmt);
						loanStlMap.put("payAmt", 0);
						loanStlMap.put("delayAmt", oldDelayAmt + conAmt);
						loanStlMap.put("holdAmt", newHoldAmt);
						loanStlMap.put("balance", oldBalance);
						loanStlMap.put("payCnt", oldPayCnt);
						loanStlMap.put("delayCnt", oldDelayCnt + 1);
						loanStlMap.put("payCk","N");
						loanStlMap.put("delayCk","Y");
					}
				}
				
				// 상환완료일 설정
				if(loanStlMap.getLong("balance") == 0) {
					loanStlMap.put("lastPayDay", eachMap.getInt("stlDay"));
					loanDAO.updateLoanStatus(eachMap.getString("mchtId"), "미사용");
				} else {
					loanStlMap.put("lastPayDay", "");
				}
				
				loanDAO.updateLoan(loanStlMap);
				loanDAO.insertDtl(loanStlMap);
				
				// 대출정산 설정
				if(loanStlMap.getInt("payAmt") > 0) {
					SharedMap<String,Object> submitMngData = loanDAO.getSubmitMng(loanData.getString("submitGrade"), loanData.getString("submitId"));
					SharedMap<String,Object> submitData = loanDAO.getSubmit(loanData.getString("submitGrade"), loanData.getString("submitId"));
					loanDAO.insertStl(loanStlMap, submitData, submitMngData, eachMap);
				}
			}
			
			dao.setTable("PG_SETTLE_MCHT");
			dao.setRecord("stlId", stlId);
			dao.setRecord("mchtId", eachMap.getString("mchtId"));
			dao.setRecord("status", eachMap.getString("status"));
			dao.setRecord("stlDay", eachMap.getString("stlDay"));
			dao.setRecord("stlStartDay", eachMap.getString("stlStartDay"));
			dao.setRecord("startDay", eachMap.getString("startDay"));
			dao.setRecord("endDay", eachMap.getString("endDay"));
			dao.setRecord("payAmt", eachMap.getLong("payAmt"));
			dao.setRecord("payFee", eachMap.getLong("payFee"));
			dao.setRecord("payVat", eachMap.getLong("payVat"));
			dao.setRecord("payCnt", eachMap.getLong("payCnt"));
			dao.setRecord("rfdAmt", eachMap.getLong("rfdAmt"));
			dao.setRecord("rfdFee", eachMap.getLong("rfdFee"));
			dao.setRecord("rfdVat", eachMap.getLong("rfdVat"));
			dao.setRecord("rfdCnt", eachMap.getLong("rfdCnt"));
			dao.setRecord("holdAmt", eachMap.getLong("holdAmt"));
			dao.setRecord("holdFee", eachMap.getLong("holdFee"));
			dao.setRecord("holdVat", eachMap.getLong("holdVat"));
			dao.setRecord("holdCnt", eachMap.getLong("holdCnt"));
			dao.setRecord("relsAmt", eachMap.getLong("relsAmt") + eachMap.getLong("manualRelsAmt"));
			dao.setRecord("relsFee", eachMap.getLong("relsFee"));
			dao.setRecord("relsVat", eachMap.getLong("relsVat"));
			dao.setRecord("relsCnt", eachMap.getLong("relsCnt"));
			dao.setRecord("distFee", eachMap.getLong("distFee"));
			dao.setRecord("agencyFee", eachMap.getLong("agencyFee"));
			dao.setRecord("vanFee", eachMap.getLong("vanFee"));
			dao.setRecord("diffAmt", eachMap.getLong("diffAmt"));
			dao.setRecord("benefit", eachMap.getLong("benefit"));
			dao.setRecord("deductAmt", eachMap.getLong("deductAmt") + eachMap.getLong("manualDeductAmt"));
			dao.setRecord("loanDeductAmt", eachMap.getLong("loanDeductAmt"));
			dao.setRecord("stlAmount", eachMap.getLong("stlAmount"));
			dao.setRecord("ddctAmt", eachMap.getLong("ddctAmt"));
			dao.setRecord("ddctType", eachMap.getString("ddctType"));
			dao.setRecord("taxId", eachMap.getString("taxId"));
			dao.setRecord("bankCd", eachMap.getString("bankCd"));
			dao.setRecord("bankName", eachMap.getString("bankName"));
			dao.setRecord("account", eachMap.getString("account"));
			dao.setRecord("accntHolder", eachMap.getString("accntHolder"));
			dao.setRecord("stlRate", eachMap.getDouble("stlRate"));
			dao.setRecord("stlType", eachMap.getString("stlType"));
			dao.setRecord("settleSvc", eachMap.getString("settleSvc"));
			dao.setRecord("regId", eachMap.getString("regId"));
			dao.setRecord("regDay", eachMap.getString("regDay"));
			dao.setRecord("regDate", eachMap.getTimestamp("regDate"));

			if (dao.insert()) {
				dao.initRecord();
				if(dao.update("UPDATE PG_COLLECT_SETTLE_DTL A JOIN PG_COLLECT_SETTLE B ON A.collectId = B.collectId SET A.deductStlId ='"+ stlId +"' "
								+ "WHERE A.mchtId = '"+ eachMap.getString("mchtId") +"' AND B.status ='확정' AND A.deductAmount < 0 and A.deductStlId =''")) {
					logger.debug("UPDATE PG_COLLECT_SETTLE_DTL: TRUE");
				}
				if(dao.update("UPDATE PG_MCHT_DEPOSIT SET stlId ='"+ stlId +"', status='완료' WHERE mchtId = '"+ eachMap.getString("mchtId") +"' AND (`depType` ='반환요청' OR `depType` ='정산차감') and stlId = '' AND status != '완료' ")) {
					logger.debug("UPDATE PG_MCHT_DEPOSIT: TRUE");
				}
				
				dao.setTable("VW_TRX_CAP");
				
				// 가맹점 지급정산 - 리스크 여부와 상관없이 정상 가맹점 지급
				dao.setColumns("'" + stlId + "' stlId, capId,capType,stlStatus,risk,IF(risk != '' AND stlStatus = '정산대기','A','') isHoldId ");
//				dao.setColumns("'" + stlId + "' stlId, capId,capType,stlStatus,risk,'' as isHoldId ");
				if(!eachMap.isNullOrSpace("stlType")) {
					dao.setWhere(" (stlType = '" + eachMap.getString("stlType") + "' OR (capType ='매입' AND risk != '')) ");
				}
				dao.addWhere("stlDay", eachMap.getString("stlDay"), DAO.le);
				dao.addWhere("stlDay", eachMap.getString("stlStartDay"), DAO.ge);
				dao.addWhere("mchtId", eachMap.getString("mchtId"));
				dao.addWhere("stlStatus", "정산확정", DAO.ne);
				List<SharedMap<String, Object>> capList = dao.search().getRows();
				dao.initRecord();
				if (capList.size() > 0) {
					insertMchtSettleIdx(capList);
					
					dao.initRecord();
					if (eachMap.getLong("holdAmt") > 0) {
						insertMchtSettleHold(capList, eachMap.getString("regId"), eachMap.getString("regDay"), eachMap.getTimestamp("regDate")); // 순서 1
					}
					dao.initRecord();
					if (eachMap.getLong("relsAmt") > 0) {
						dao.update("UPDATE PG_SETTLE_HOLD A JOIN PG_TRX_CAP B ON A.capId = B.capId SET A.status = '반환완료', A.stlId ='" + stlId + "' WHERE A.status = '반환요청' AND B.mchtId ='" + eachMap.getString("mchtId") + "'");
					}
					dao.initRecord();
					if(updateMchtSettleCap(capList) != capList.size()) {
						updateMchtSettleCap(capList);
					}
					
					dao.initRecord();
					
					resultMap.put("result", "OK");
				}
				
				// 차감정산 추가
				if(eachMap.getString("ddctType").equals("Y")) {
					dao.setTable("PG_SETTLE_DDCT");
					dao.setRecord("stlStatus", "정산확정");
					dao.setRecord("stlId", stlId);
					dao.addWhere("mchtId", eachMap.getString("mchtId"),DAO.eq);
					dao.addWhere("stlDay", eachMap.getString("stlDay"), DAO.le);
					dao.addWhere("stlDay", eachMap.getString("stlStartDay"), DAO.ge);
					dao.update();
					dao.initRecord();
				}
				
			}
		}
		return resultMap;
	}

	public int insertMchtSettleIdx(List<SharedMap<String, Object>> mchtSettleTempList) {
		int inserted = 0;
		logger.debug("insert MchtSettleTemp batch : {}", mchtSettleTempList.size());
		String query = "INSERT INTO `PG_SETTLE_IDX` (`stlId`,`capId`,`capStatus`) " + "VALUES (?,?,?);";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;

			for (SharedMap<String, Object> map : mchtSettleTempList) {
				if (map.isNullOrSpace("isHoldId")) {
					int i = 1;
					pstmt.setString(i++, map.getString("stlId"));
					pstmt.setString(i++, map.getString("capId"));
					pstmt.setString(i++, map.getString("capType"));

					pstmt.addBatch();
					if (++count % batchSize == 0) {
						inserted += pstmt.executeBatch().length;
					}
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch MchtSettleTemp error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}

	public int updateMchtSettleCap(List<SharedMap<String, Object>> mchtSettleTempList) {
		int inserted = 0;
		logger.debug("update TRX_CAP_DTL batch : {}", mchtSettleTempList.size());
		String query = "UPDATE `PG_TRX_CAP_DTL` SET stlStatus=?, stlId =? " + "WHERE capId=?;";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 30;
			int count = 0;

			for (SharedMap<String, Object> map : mchtSettleTempList) {
				int i = 1;
				if (map.isNullOrSpace("isHoldId")) {
					pstmt.setString(i++, "정산확정");
					pstmt.setString(i++, map.getString("stlId"));
				} else {
					pstmt.setString(i++, "정산보류");
					pstmt.setString(i++, "");
				}
				pstmt.setString(i++, map.getString("capId"));
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("update batch TRX_CAP_DTL error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}

	public void insertMchtSettleHold(List<SharedMap<String, Object>> capList, String regId, String regDay, Timestamp regDate) {
		DAO dao = new DAO();
		for (SharedMap<String, Object> eachMap : capList) {
			if (!eachMap.isNullOrSpace("isHoldId")) {
				dao.setTable("PG_SETTLE_HOLD");
				dao.setRecord("holdId", TrxDAO.getHoldId());
				dao.setRecord("holdStlId", eachMap.getString("stlId"));
				dao.setRecord("capId", eachMap.getString("capId"));
				dao.setRecord("lastStatus", eachMap.getString("risk"));
				dao.setRecord("regId", regId);
				dao.setRecord("regDay", regDay);
				dao.setRecord("regDate", regDate);

				dao.insert();
				dao.initRecord();
			}
		}

	}

	@RequestMapping(value = "/settle/mcht/hold/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView holdList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SettleHoldDAO settleHoldDAO = new SettleHoldDAO();
		RecordSet rset = settleHoldDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleHoldDAO).setView(request, "/settle/hold/list", "");
	}

	@RequestMapping(value = "/settle/mcht/hold/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMchtHoldStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String holdId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("holdId : {}", holdId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE_HOLD");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("holdId", holdId, DAO.in);
		
		if(status.equals("즉시반환")) {
			dao.addWhere("status", "반환완료");
		} else if (status.equals("반환요청")) {
			dao.addWhere("status", "'반환요청','반환완료'", DAO.in);
		} else if (status.equals("보류")) {
			dao.addWhere("status", "'보류','반환완료'", DAO.in);
		} else {
			logger.error("정산 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			return resultMap;
		}

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			dao.initRecord();
			if (dao.update("UPDATE PG_SETTLE_HOLD SET status= '" + status + "' WHERE holdId IN (" + holdId + ")")) {
				if(status.equals("즉시반환")) {
					return directSettle(holdId, SessionUtil.getUserId(request));
				} else {
					resultMap.put("result", "OK");
				}
			} else {
				resultMap.put("result", "NOK");
				resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			}
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'" + status + "' 상태로 변경할 수 없는 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		return resultMap;
	}
	
	
	private SharedMap<String, Object> directSettle(String holdId, String userId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		resultMap.put("result", "NOK");
		
		DAO dao = new DAO();
		dao.setTable("VW_SETTLE_HOLD A JOIN PG_MCHT_TAX B ON A.taxId = B.taxId");
		dao.setColumns("A.mchtId, SUM(A.amount) as amount, SUM(A.stlAmount) as stlAmount, SUM(A.stlFee) as stlFee, SUM(A.stlFeeVat) as stlFeeVat, COUNT(*) as cnt, "
						+ " MAX(A.taxId) as taxId, SUM(A.distFee) as distFee, SUM(A.agencyFee) as agencyFee, SUM(A.vanFee) as vanFee, SUM(A.benefit) as benefit, MAX(stlRate) as stlRate, "
						+ " min(A.trxDay) startDay ,max(A.trxDay) endDay, "
						+ " B.bankCd,B.bankName,B.account,B.accntHolder");
		dao.addWhere("A.holdId", holdId, DAO.in);
		dao.setGroupBy("A.mchtId");
		dao.setOrderBy("");
		RecordSet rset = dao.search();
		if(rset.size() > 1) { // 가맹점이 여러개
			resultMap.put("msg", "하나의 가맹점만 선택할 수 있습니다.");
			return resultMap;
		}
		SharedMap<String, Object> eachMap = rset.getRow(0);
		
		dao.initRecord();
		String stlId = TrxDAO.getSettleId();
		dao.setTable("PG_SETTLE_MCHT");
		dao.setRecord("stlId", stlId);
		dao.setRecord("mchtId", eachMap.getString("mchtId"));
		dao.setRecord("status", "지급대기");
		dao.setRecord("stlDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		dao.setRecord("stlStartDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		dao.setRecord("startDay", eachMap.getString("startDay"));
		dao.setRecord("endDay", eachMap.getString("endDay"));
		dao.setRecord("payAmt", "0");
		dao.setRecord("payFee", "0");
		dao.setRecord("payVat", "0");
		dao.setRecord("payCnt", "0");
		dao.setRecord("rfdAmt", "0");
		dao.setRecord("rfdFee", "0");
		dao.setRecord("rfdVat", "0");
		dao.setRecord("rfdCnt", "0");
		dao.setRecord("holdAmt", "0");
		dao.setRecord("holdFee", "0");
		dao.setRecord("holdVat", "0");
		dao.setRecord("holdCnt", "0");
		dao.setRecord("relsAmt", eachMap.getLong("amount"));
		dao.setRecord("relsFee", eachMap.getLong("stlFee"));
		dao.setRecord("relsVat", eachMap.getLong("stlFeeVat"));
		dao.setRecord("relsCnt", eachMap.getLong("cnt"));
		dao.setRecord("distFee", eachMap.getLong("distFee"));
		dao.setRecord("agencyFee", eachMap.getLong("agencyFee"));
		dao.setRecord("vanFee", eachMap.getLong("vanFee"));
		dao.setRecord("benefit", eachMap.getLong("benefit"));
		dao.setRecord("deductAmt", "0");
		dao.setRecord("stlAmount", "0");
		dao.setRecord("taxId", eachMap.getString("taxId"));
		dao.setRecord("bankCd", eachMap.getString("bankCd"));
		dao.setRecord("bankName", eachMap.getString("bankName"));
		dao.setRecord("account", eachMap.getString("account"));
		dao.setRecord("accntHolder", eachMap.getString("accntHolder"));
		dao.setRecord("stlRate", eachMap.getDouble("stlRate"));
		dao.setRecord("regId", userId);
		dao.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		dao.setRecord("regDate", CommonUtil.getCurrentTimestamp());

		if (dao.insert()) {
			dao.initRecord();
			dao.setDebug(true);
			dao.setTable("VW_TRX_CAP A JOIN PG_SETTLE_HOLD B ON A.capId = B.capId");
			dao.setColumns("'" + stlId + "' stlId, A.capId,A.capType,A.stlStatus,A.risk, '' holdId");
			dao.addWhere("B.holdId", holdId, DAO.in);
			dao.setOrderBy("");
			List<SharedMap<String, Object>> capList = dao.search().getRows();
			dao.initRecord();
			if (capList.size() > 0) {
				dao.setDebug(true);
				insertMchtSettleIdx(capList);

				dao.initRecord();
				dao.setDebug(true);
				
				if(!dao.update("UPDATE PG_SETTLE_HOLD SET status = '반환완료', stlId = '" + stlId  + "' WHERE holdId IN (" + holdId +")")) {
					resultMap.put("result", "NOK");
					return resultMap;
				}
				dao.initRecord();
				dao.setDebug(true);
				if(updateMchtSettleCap(capList) != capList.size()) {
					updateMchtSettleCap(capList);
				}
				
				resultMap.put("result", "OK");
			}
		} else {
			resultMap.put("msg", "정산 생성에 실패했습니다.");
		}
		
		return resultMap;
	}

	@RequestMapping(value = "/settle/mcht/payout/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMchtPayout(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("stlId : {} => {}", stlId, status);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE_MCHT");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("stlId", stlId, DAO.in);

		if (status.equals("지급완료")) {
			dao.addWhere("status", "지급완료", DAO.eq);
		} else if (status.equals("지급보류")) {
			dao.addWhere("status", "'지급완료','지급보류'", DAO.in);
		} else if (status.equals("삭제")) {
			dao.addWhere("status", "'지급완료','삭제'", DAO.in);
		} else {
			logger.error("지급 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "지급 상태 변경에 실패하였습니다.");
			return resultMap;
		}
		
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			dao.initRecord();
			if (status.equals("삭제")) {
				dao.setTable("PG_SETTLE_MCHT");
				dao.addWhere("stlId", stlId, DAO.in);
				if (dao.delete()) {
					
					// 가맹점 정산 삭제 시 대출정산 삭제
					LoanSettleDAO loanDAO = new LoanSettleDAO();
					List<SharedMap<String, Object>> loanDtlData = loanDAO.getByDtlId(stlId);
					for(SharedMap<String,Object> data:loanDtlData) {
						SharedMap<String,Object> oldLoanDtl = loanDAO.getOldDtl(data.getString("loanStlId"), data.getString("loanId"));
						if(loanDtlData != null) {
							SharedMap<String, Object> loanMap = new SharedMap<String, Object>();

							// 기존 대출정산 관련 업데이트
							loanMap.put("loanId", oldLoanDtl.getString("loanId"));
							loanMap.put("totPayAmt", oldLoanDtl.getString("totPayAmt"));
							loanMap.put("balance", oldLoanDtl.getString("balance"));
							loanMap.put("paySession", oldLoanDtl.getInt("paySession"));
							loanMap.put("payCnt", oldLoanDtl.getInt("payCnt"));
							loanMap.put("delayCnt", oldLoanDtl.getInt("delayCnt"));
							loanDAO.updateLoan(loanMap);
							loanDAO.deleteStl(data.getString("loanStlId"));
							
							dao.setTable("PG_LOAN_DTL");
							dao.addWhere("loanStlId", data.getString("loanStlId"));
							dao.delete();
						}
					
					}
					
					resultMap.put("result", "OK");
				} else {
					resultMap.put("result", "NOK");
					resultMap.put("msg", "정산 데이터 삭제에 실패하였습니다.");
				}
			} else {
				if (dao.update("UPDATE PG_SETTLE_MCHT SET status= '" + status + "', payOutDate=CURRENT_TIMESTAMP WHERE stlId IN (" + stlId + ")")) {
					if (status.equals("지급완료")) {
						if(dao.update("UPDATE PG_TRX_CAP_DTL SET stlStatus='정산완료', payOutDay= '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
							new DepositDAO().setAddByMchtSettle(stlId, SessionUtil.getUserId(request));
							dao.initRecord();
							dao.update("UPDATE PG_SETTLE_DDCT set stlStatus = '정산완료', stlDay = '"+CommonUtil.getCurrentDate("yyyyMMdd")+"' WHERE stlId IN ("+stlId+")");
							// 충전정산 입금 처리
							List<SharedMap<String, Object>> chargeSettleList = new SettleMchtDAO().getByChargeSettleLists(stlId).getRows();
							if(chargeSettleList.size() > 0) {
								for(SharedMap<String, Object> settleMap:chargeSettleList) {
									long netAmount = 0;
									if(settleMap.isEquals("ddctType", "X")) {
										netAmount = settleMap.getLong("stlAmount") + (settleMap.getLong("relsAmt")-settleMap.getLong("relsFee")-settleMap.getLong("relsVat")) + settleMap.getLong("deductAmt") - settleMap.getLong("ddctAmt");
									}else {
										netAmount = settleMap.getLong("stlAmount") + (settleMap.getLong("relsAmt")-settleMap.getLong("relsFee")-settleMap.getLong("relsVat")) + settleMap.getLong("deductAmt");
									}
									
									// 지급금액이 0원이 아닌 정산만 충전정산액에 반영함
									if(netAmount != 0) {
										ChargeSettleDAO chargeSettleDAO = new ChargeSettleDAO();
										settleMap.put("trxId", chargeSettleDAO.getChargeSettleTrxId());
										if(settleMap.getLong("stlAmount") > 0) {
											settleMap.put("trxType", "입금");
										}else {
											settleMap.put("trxType", "출금");
										}
										settleMap.put("trxUnit", "신용카드정산");
										String regDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
										settleMap.put("trxDay", regDate.substring(0, 8));
										settleMap.put("trxTime", regDate.substring(8));
										settleMap.put("trackId", settleMap.getString("stlId"));
										settleMap.put("refId", settleMap.getString("stlId"));
										settleMap.put("netAmount", netAmount);
										settleMap.put("balance", chargeSettleDAO.getMchtBalance(settleMap.getString("mchtId")).getLong("balance")+settleMap.getLong("netAmount"));
										String stlDay = settleMap.getString("stlDay").substring(0, 4)+"-"+settleMap.getString("stlDay").substring(4,6)+"-"+settleMap.getString("stlDay").substring(6);
										settleMap.put("summary", stlDay+"정산일자 신용카드 정산금 지급");
										settleMap.put("regId", SessionUtil.getUserId(request));
										settleMap.put("regDay", regDate.substring(0, 8));
										
										chargeSettleDAO.insertChargeSettle(settleMap);
									}
								}
							}
							resultMap.put("result", "OK");
						}else {
							resultMap.put("result", "OK");
						}
					} else if (status.equals("지급보류")) {
						dao.initRecord();
						dao.update("UPDATE PG_SETTLE_DDCT SET stlStatus = '정산대기', stlId = '' WHERE stlId IN ("+stlId+")");
						resultMap.put("result", "OK");
					} else {
						resultMap.put("msg", "지급 상태 변경에 실패하였습니다(DB).");
					}
				} else {
					resultMap.put("result", "NOK");
					resultMap.put("msg", "지급 상태 변경에 실패하였습니다.");
				}
			}
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'" + status + "' 상태로 변경할 수 없는 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		return resultMap;
	}
	


	@RequestMapping(value = {"/settle/mcht/change/deduct"}, method = RequestMethod.POST)
    public @ResponseBody SharedMap<String, Object> insert(HttpServletRequest request) {
		SharedMap<String, Object> reqMap = new SharedMap<String, Object>();
		reqMap.put("amount", request.getParameter("amount"));
		reqMap.put("stlId", request.getParameter("stlId"));
		reqMap.put("summary", request.getParameter("summary"));
		
		if(reqMap.getLong("amount") > 0) {
			reqMap.put("amount", -reqMap.getLong("amount"));
		}
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		if(reqMap.isNullOrSpace("stlId")) {
			resMap.put("result", "NOK");
			resMap.put("msg", "정산 번호가 잘못되었습니다.");
			return resMap;
		}
		
		DAO dao = new DAO();
		if(!dao.update("UPDATE PG_SETTLE_MCHT SET deductAmt = '" + reqMap.getLong("amount") + "', summary = '" + CommonUtil.nToB(reqMap.getString("summary")) + "' "
						+ "WHERE stlId = '" + reqMap.getString("stlId") +"'")) {
			resMap.put("result", "NOK");
			resMap.put("msg", "입력에 실패했습니다.");
		} else {
			resMap.put("result", "OK");
		}
		return resMap;
    }
	
	// 터미널 정산 추가 - 20190104
	@RequestMapping(value = "/settle/tmn/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleTmnList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO dao = new SettleDAO();
		StringBuffer query = new StringBuffer();
		

		query.append(" (SELECT A.mchtId, A.tmnId, max(C.stlDay) AS stlDay, min(C.stlDay) AS stlStartDay, min(C.trxDay) AS startDay, max(C.trxDay) AS endDay, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk = '',1,0)) AS payCnt, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk = '',A.amount,0)) AS payAmount, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk = '',A.vat,0)) AS payVat, ");
		query.append(" SUM(IF(A.capType = '매입취소',1,0)) AS rfdCnt, ");
		query.append(" SUM(IF(A.capType = '매입취소',A.amount,0)) AS rfdAmount, ");
		query.append(" SUM(IF(A.capType = '매입취소',A.vat,0)) AS rfdVat, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk != '',1,0)) AS holdCnt, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk != '',A.amount,0)) AS holdAmount, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk != '',A.vat,0)) AS holdVat, ");
		query.append(" SUM(IF(B.risk = '',B.stlDistFee,0)) AS stlDistFee, ");
		query.append(" SUM(IF(B.risk = '',B.stlAgencyFee,0)) AS stlAgencyFee, ");
		query.append(" SUM(IF(B.risk = '',B.stlSalesFee,0)) AS stlSalesFee, ");
		query.append(" SUM(IF(B.risk = '',B.stlAmount,0)) AS stlMchtAmount, ");
		query.append(" SUM(IF(B.risk = '',C.stlAmount,0)) AS stlTmnAmount, ");
		query.append(" SUM(IF(B.risk = '',B.stlVanFee,0)) AS stlVanFee, ");
		query.append(" SUM(IF(B.risk = '',B.benefit,0)) AS benefit, ");
		query.append(" MAX(C.stlRate) AS stlRate, MAX(C.stlType) AS stlType ");
		query.append(" FROM PG_TRX_CAP A join PG_TRX_CAP_DTL B on A.capId = B.capId and B.stlDay >= '"+cpRequest.getKeyValue("stlStartDay")+"' AND B.stlDay <= '"+cpRequest.getKeyValue("stlEndDay")+"' ");
		query.append(" join PG_TRX_CAP_SUB C on A.capId = C.capId ");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			query.append(" AND C.stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("mchtId"))) {
			query.append(" AND C.mchtId LIKE '%"+ cpRequest.getKeyValue("mchtId")+"%'");
		}
		query.append(" JOIN PG_MCHT_MNG D on A.mchtId = D.mchtId AND D.settleTmnStatus = '사용' GROUP BY A.tmnId) E join VW_MCHT_TMN F on E.tmnId = F.tmnId ");
		
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("mchtName"))) {
			query.append(" AND F.mchtName LIKE '%"+ cpRequest.getKeyValue("mchtName")+"%'");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("tmnName"))) {
			query.append(" AND F.dtlName LIKE '%"+ cpRequest.getKeyValue("tmnName")+"%'");
		}
		
		dao.setTable(query.toString());
		dao.setColumns(" E.mchtId, F.mchtName AS mchtName, E.tmnId, F.dtlName AS tmnName, '지급대기' AS status,  E.stlDay,  E.stlStartDay, E.startDay, E.endDay, E.payCnt, E.payAmount, E.payVat, E.rfdCnt, E.rfdAmount, E.rfdVat, E.holdCnt, E.holdAmount, E.holdVat, E.stlDistFee, E.stlAgencyFee, E.stlSalesFee, "
						+ " (E.stlMchtAmount - E.stlTmnAmount) AS stlMchtFee, E.stlVanFee, E.benefit, E.stlTmnAmount,  E.stlRate AS stlTmnRate, E.stlType AS stlType, F.dtlBankCd AS tmnBankCd, F.dtlBankName AS tmnBankName, F.dtlAccount AS tmnAccount,  F.dtlAccntHolder AS tmnAccntHolder ");
		
		dao.setOrderBy("F.mchtName, F.dtlName asc");
		
		cpRequest.deleteKeyData("stlEndDay");
		cpRequest.deleteKeyData("stlStartDay");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			cpRequest.deleteKeyData("stlType");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("mchtId"))) {
			cpRequest.deleteKeyData("mchtId");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("mchtName"))) {
			cpRequest.deleteKeyData("mchtName");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("tmnName"))) {
			cpRequest.deleteKeyData("tmnName");
		}
		RecordSet rset = dao.search(cpRequest.data);

		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/tmn/list", "");
	}
	
	@RequestMapping(value = "/settle/pisp/form", method = RequestMethod.GET)
	public ModelAndView settleForm(HttpServletRequest request) {
		DAO dao = new DAO();
		List<SharedMap<String,Object>> months = dao.query("SELECT substr(regDay,1,6) as settleMonth FROM PG_TRX_PISP GROUP BY substr(regDay,1,6) ORDER BY  substr(regDay,1,6)").getRows();
		List<String> monthList = new ArrayList<String>();
		for(SharedMap<String,Object> m : months) {
			monthList.add(m.getString("settleMonth"));
		}
		
		
		return new ModelAndView("/settle/pisp/form","trxMonth",monthList);
  }
	
/*	@RequestMapping(value = "/settle/pisp/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView pispSettleList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
	    String thisMonth = cpRequest.getKeyValue("thisMonth");
	    cpRequest.deleteKeyData("thisMonth");
	    StringBuilder sb = new StringBuilder();
	  
	    DAO dao = new DAO();
	    dao.setDebug(true); 
	    sb.append("(SELECT SUM(IF(trxType='입금',1,0)) AS inCnt,SUM(IF(trxType='입금',amount, 0)) AS inAmt,SUM(IF(trxType='입금',fee, 0)) AS inFee,SUM(IF(trxType='입금',vanFee, 0)) AS inVanFee, ");
	    sb.append("SUM(IF(trxType='출금',1,0)) AS outCnt, SUM(IF(trxType='출금',amount, 0)) AS outAmt, SUM(IF(trxType='출금',fee, 0)) AS outFee,SUM(IF(trxType='출금',vanFee, 0)) AS outVanFee,");
	    sb.append("mchtId,regDay, SUBSTRING(regDay, 1,6) AS thisMonth FROM PG_TRX_PISP ");
	    sb.append("WHERE status='성공' AND SUBSTRING(regDay, 1,6)='"+thisMonth+"' GROUP BY mchtId) AS A ");
	    sb.append("LEFT OUTER JOIN (SELECT SUM(IF(status='성공',1,0)) AS fcsCnt, SUM(IF(status='성공', fee, 0)) AS fcsFee, "); 
	    sb.append("SUM(IF(status='성공', vanFee, 0)) AS fcsVanFee, mchtId, regDay FROM PG_TRX_PISP_FCS WHERE SUBSTRING(regDay, 1,6)='"+thisMonth+"' GROUP BY mchtId) AS B ON A.mchtId=B.mchtId");
	    sb.append(" A.thisMonth AS thisMonth,FN_MCHT_NAME(A.mchtId) as mchtName, A.mchtId AS mchtId, A.inCnt, A.inAmt,A.inFee,A.inVanFee, A.outCnt, A.outAmt, A.outFee,A.outVanFee, B.fcsCnt, B.fcsFee, B.fcsVanFee");
	    sb.append("A.thisMonth desc, A.mchtName asc");
	    dao.query(sb.toString());  
	    CPUtil.setDAO(dao, cpRequest.data); 
	    request.setAttribute("PISP_STL",  dao.query(sb.toString()).getRows()); 
	    cpRequest.page = CPUtil.correctPage(cpRequest.page);
	    RecordSet rset = dao.searchList(cpRequest.page.current, cpRequest.page.size,cpRequest.page.hash);	    
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request,"/settle/pisp/list","");
	  }*/
	
	@RequestMapping(value = "/settle/pisp/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settlePispList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		PispSettleDAO pispStlDAO = new PispSettleDAO();
		RecordSet rset = pispStlDAO.list(cpRequest.data,cpRequest.page);  
		return new CPRUtil(cpRequest).dataList(rset,pispStlDAO).setView(request,"/settle/pisp/list","");
	}

	@RequestMapping(value = "/settle/ddct/form", method = RequestMethod.GET)
	public ModelAndView settleDdctform(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("DDCTCODE", new MchtDdctDAO().getCode().getRows());
		return new ModelAndView("/settle/ddct/form");
	}
	@RequestMapping(value = "/settle/ddct/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleDdctList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SettleDdctDAO settleDdctDAO = new SettleDdctDAO();
		RecordSet rset = settleDdctDAO.list(cpRequest.data,cpRequest.page);  
		return new CPRUtil(cpRequest).dataList(rset,settleDdctDAO).setView(request,"/settle/ddct/list","");
	}
	
	@RequestMapping(value = "/settle/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> decide(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		DAO dao = new DAO();
		
		for(SharedMap<String, String> eachMap : requestList) {
				
			logger.debug("SETTLE SAVE = stlId: {}", eachMap.getString("stlId"));
			dao.setDebug(true);
			dao.setTable("PG_SETTLE");
			dao.setRecord("payOutAmt",eachMap.getLong("payOutAmt"));
			dao.setRecord("summary",eachMap.getString("summary"));
			dao.setRecord("regId",regId);
			dao.setRecord("regDay",regDay);
			dao.addWhere("stlId", eachMap.getString("stlId"));
			if(!dao.update()) {
				resultMap.put("result", "NOK");
    			resultMap.put("msg", eachMap.getString("stlId") + " DB 업데이트에 실패했습니다.");
    			break;
			}
				
			dao.initRecord();
		}
		if(!resultMap.getString("result").equals("NOK")) {
			resultMap.put("result", "OK");
		}else {
			resultMap.put("result", "NOK");
			if(resultMap.getString("msg").length() < 1) {
				resultMap.put("msg", "수정에 실패했습니다.");
			}
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/settle/phone/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settlePhoneList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		logger.info("settlePhoneList Call");
		
		request.setAttribute("SUMMAP", new SettlePhoneDAO().calcSettlePhoneList(cpRequest.data).getRow(0));
		
		SettlePhoneDAO settlePhoneDAO = new SettlePhoneDAO();
		RecordSet rset = settlePhoneDAO.list(cpRequest.data,cpRequest.page);
		
		return new CPRUtil(cpRequest).dataList(rset,settlePhoneDAO).setView(request,"/phone/settle/list","");
	}
	
	@RequestMapping(value = "/settle/phone/sunab/excel/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> add(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();

		String stlDay = CommonUtil.getCurrentDate("yyyyMMdd");
		
		SettlePhoneDAO dao = new SettlePhoneDAO();
		int errCnt = 0;
		
		logger.info("sunab excel COUNT : {}", requestList.size());
		
		if(requestList.size() > 0) {
			for(SharedMap<String, String> eachMap : requestList) {
				boolean sunabCheck = false;
				
				//결제금액과 수납금액이 차이가 있으면 미납으로 처리
				if(eachMap.getString("payAmt").equals(eachMap.getString("sunabAmt"))) {
					sunabCheck = true;
				}
				
				boolean updateCheck = dao.updateStlDay(eachMap.getString("trxId"), stlDay, sunabCheck);
				
				if(!updateCheck) {
					logger.info("PG_PHONE_CAP stlDay UPDATE FAIL : [{}]", eachMap.getString("trxId"));
				}
			}
			
			//매입원장에서 정산예정일자의 정산데이터 조회
			List<SharedMap<String,Object>> getPhoneSettleList = dao.getPhoneSettleList(stlDay);
			logger.info("GetPhoneSettleList COUNT : {}", getPhoneSettleList.size());
			
			if(getPhoneSettleList.size() > 0) {
				logger.info("==================================================");
				logger.info("수납정산 처리 시작");
				logger.info("==================================================");
				
				for(SharedMap<String,Object> data : getPhoneSettleList){
					
					//SharedMap<String,Object> settlePhoneMap	= dao.getSettlePhoneCheck(data.getString("mchtId"), stlDay);
					
					String stlId = dao.getSettleId();
					SharedMap<String,Object> mchtTaxMap		= dao.getMchtTaxByMchtId(data.getString("mchtId"));
					
					SharedMap<String,Object> settleData = new SharedMap<String, Object>();

					settleData.put("stlId"		, stlId);
					settleData.put("mchtId"		, data.getString("mchtId"));
					settleData.put("stlDay"		, data.getString("stlDay"));
					settleData.put("startDay"	, data.getString("startDay"));
					settleData.put("endDay"		, data.getString("endDay"));
					
					settleData.put("payAmt"		, data.getLong("payAmt"));
					settleData.put("payFee"		, data.getLong("payFee"));
					settleData.put("payVat"		, data.getLong("payVat"));
					settleData.put("payCnt"		, data.getLong("payCnt"));
					
					settleData.put("rfdAmt"		, data.getLong("rfdAmt"));
					settleData.put("rfdFee"		, data.getLong("rfdFee"));
					settleData.put("rfdVat"		, data.getLong("rfdVat"));
					settleData.put("rfdCnt"		, data.getLong("rfdCnt"));
					
					settleData.put("stlAmt"		, data.getLong("stlAmount"));
					settleData.put("benefit"	, data.getLong("benefit"));
					
					settleData.put("bankCd"		, mchtTaxMap.getString("bankCd"));
					settleData.put("bankName"	, mchtTaxMap.getString("bankName"));
					settleData.put("account"	, dao.getAESEnc(mchtTaxMap.getString("account")).replace("-", "").trim());
					settleData.put("accntHolder", dao.getAESEnc(mchtTaxMap.getString("accntHolder")));
					settleData.put("stlRate"	, data.getDouble("stlRate"));
					settleData.put("stlType"	, data.getString("stlType"));
					
					settleData.put("regId", "SYSTEM");
					settleData.put("regDate", CommonUtil.getCurrentDate("yyyyMMdd"));
					
					logger.info("stlId	: {}",stlId);
					logger.info("stlDay	: {}",stlDay);
					logger.info("stlType: {}",data.getString("stlType"));
					logger.info("mchtId	: {}",data.getString("mchtId"));
					
					if(!dao.insertSettlePhone(settleData)){
						logger.info("PG_SETTLE_PHONE 테이블 insert 실패 : [{}][{}]", data.getString("mchtId"), stlDay);
						errCnt++;
					}else {
						if(!dao.updatePhoneCapUpdate(stlId,stlDay,data.getString("stlType"),data.getString("mchtId"),"정산완료")){
							logger.info("PG_PHONE_CAP 테이블 update 실패 : [{}][{}]", data.getString("mchtId"), stlDay);
							errCnt++;
						}
					}
					
					logger.info("==================================================");
				}
				
				logger.info("수납정산 처리 종료");
				logger.info("==================================================");
			}else {
				logger.info("PG_PHONE_CAP 수납정산 데이터 미존재 확인요망 : [{}]", stlDay);
				errCnt++;
			}
		} else {
			logger.info("수납정산 Excel Data 이상, 확인요망!!!");
			errCnt++;
		}

		if(errCnt > 0) {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "수납정산에 실패했습니다.");
		}
			
		return resultMap;
	}
	
	@RequestMapping(value = "/settle/phone/sunab/excel/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settlePhoneSunabExcelList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		logger.info("settlePhoneSunabExcelList Call");
		
		request.setAttribute("SUMMAP", new SettlePhoneDAO().calcSettlePhoneSunabList(cpRequest.data).getRow(0));
		
		SettlePhoneDAO settlePhoneDAO = new SettlePhoneDAO();
		RecordSet rset = settlePhoneDAO.emptDataList(cpRequest.data,cpRequest.page);
		
		return new CPRUtil(cpRequest).dataList(rset,settlePhoneDAO).setView(request,"/phone/excel/list","");
	}
	
	@RequestMapping(value = "/settle/phone/sunab/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settlePhoneSunabList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		logger.info("settlePhoneSunabList Call");
		
		//request.setAttribute("SUMMAP", new SettlePhoneDAO().calcSettlePhoneList(cpRequest.data).getRow(0));
		
		SettlePhoneDAO settlePhoneDAO = new SettlePhoneDAO();
		RecordSet rset = settlePhoneDAO.snabAgainList(cpRequest.data,cpRequest.page);
		
		return new CPRUtil(cpRequest).dataList(rset,settlePhoneDAO).setView(request,"/phone/sunab/list","");
	}
	
	/*
	 * 지급대행정산 엑셀업로드 반영
	 */
	/*
	 * @RequestMapping(value = "/settle/excel/save", method =
	 * RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	 * public @ResponseBody SharedMap<String, Object>
	 * excelSttleAdd(HttpServletRequest request,@RequestBody List<SharedMap<String,
	 * String>> requestList) { SharedMap<String, Object> resultMap = new
	 * SharedMap<String, Object>();
	 * 
	 * CodeDAO codeDAO = new CodeDAO(); SettlePresidentDAO dao = new
	 * SettlePresidentDAO(); SharedMap<String,Object> map = new SharedMap<String,
	 * Object>();
	 * 
	 * for(SharedMap<String, String> eachMap : requestList) { String bankNm =
	 * eachMap.getString("bankNm"); String account = eachMap.getString("account");
	 * Long amount = eachMap.getLong("amount"); String holder =
	 * eachMap.getString("holder"); String receiverDisplay =
	 * eachMap.getString("receiverDisplay"); String senderDisplay =
	 * eachMap.getString("senderDisplay");
	 * 
	 * //지급대행 엑셀 정상시 응행명에 대한 은행코드 조회 String recvBankCd =
	 * codeDAO.getReceiveBankCd(bankNm);
	 * 
	 * //KSNET 지급대행 모계좌 K뱅크 은행코드 String sendBankCd = "089";
	 * 
	 * map.put("bankNm", bankNm); map.put("account", account); map.put("amount",
	 * amount); map.put("holder", holder); map.put("receiverDisplay",
	 * receiverDisplay); map.put("senderDisplay", senderDisplay);
	 * 
	 * dao.insertSettlePresident(resultMap);
	 * 
	 * logger.
	 * info("balanceTransfer: sendBankCd: {} , recvBankCd: {} , recvAccnt: {}, amount: {}, holder: {}, receiverDisplay: {}, senderDisplay: {}"
	 * , sendBankCd, recvBankCd, account, amount, holder, receiverDisplay,
	 * senderDisplay);
	 * 
	 * 
	 * }
	 * 
	 * return resultMap; }
	 */
}
