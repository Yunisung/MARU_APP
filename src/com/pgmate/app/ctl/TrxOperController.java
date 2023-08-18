package com.pgmate.app.ctl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.AgencyDAO;
import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.NotiExcelUploadDAO;
import com.pgmate.app.dao.TotLoadDAO;
import com.pgmate.app.dao.TotLoadDtlDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.app.export.TaxExport;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Files;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class TrxOperController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.TrxOperController.class );
	
	@RequestMapping(value = "/trxoper/new/form", method = RequestMethod.GET)
	public ModelAndView createForm(HttpServletRequest request) {
		request.setAttribute("AGENT_LIST", new AgencyDAO().getActiveSelectOption());
		return new ModelAndView("/trxoper/new/form");
	}
	
	
	@RequestMapping(value = "/trxoper/new/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse insert(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		
		SharedMap<String,Object> load = new SharedMap<String,Object>();
		load.put("mchtId", cpRequest.getValue("mchtId"));
		load.put("tmnId" , cpRequest.getValue("tmnId"));
		if(cpRequest.getValue("trnType").equals("승인")) {
			load.put("amount", cpRequest.getLongValue("amount"));
		} else {
			if(cpRequest.getLongValue("amount") > 0) {
				load.put("amount", -cpRequest.getLongValue("amount"));
			} else {
				load.put("amount", cpRequest.getLongValue("amount"));
			}
			
		}
		
		load.put("cnt", 1);
		load.put("vanType","ONLINE");
		load.put("status","등록");
		load.put("regId", SessionUtil.getUserId(request));
		load.put("regDay",CommonUtil.getCurrentDate("yyyyMMdd"));
		
		
		DAO dao = new DAO();
		dao.setTable("PG_MCHT_TMN A, PG_VAN B ");
		dao.setColumns("A.van,B.vanId");
		dao.setWhere("A.vanIdx = B.idx");
		dao.addWhere("tmnId", load.getString("tmnId"), DAO.eq);
		dao.setOrderBy("A.van asc");
		RecordSet rset = dao.search(); 
		dao.initRecord();
		SharedMap<String,Object> tmnMap = rset.getRowFirst();
		load.put("van",tmnMap.getString("van") );
		load.put("vanId",tmnMap.getString("vanId") );
		load.put("rootTrxId", cpRequest.getValue("rootTrxId"));
		

		SharedMap<String,Object> data = new SharedMap<String,Object>();
		RecordSet rootCap = null;
		
		data.put("trnType", cpRequest.getValue("trnType"));
		if(cpRequest.getValue("trnType").equals("승인")) {
			data.put("amount", cpRequest.getLongValue("amount"));
		} else {
			if(cpRequest.getLongValue("amount") > 0) {
				data.put("amount", -cpRequest.getLongValue("amount"));
			} else {
				data.put("amount", cpRequest.getLongValue("amount"));
			}
			rootCap = new TrxCapDAO().getByTrxId(cpRequest.getValue("rootTrxId"));
		}
		
		data.put("installment", CommonUtil.zerofill(cpRequest.getLongValue("installment"),2));
		if(rootCap != null) {
			data.put("bin", rootCap.getRowFirst().getString("bin"));
			data.put("last4", rootCap.getRowFirst().getString("last4"));
			data.put("authCd", rootCap.getRowFirst().getString("authCd"));
			data.put("rootTrxDay", rootCap.getRowFirst().getString("trxDay"));
		} else {
			data.put("bin", cpRequest.getValue("bin"));
			data.put("last4", cpRequest.getValue("last4"));
			data.put("authCd", cpRequest.getValue("authCd"));
			data.put("rootTrxDay", "");
		}
		
		data.put("trxDay", cpRequest.getValue("trxDay"));
		data.put("trxTime", cpRequest.getValue("trxTime"));
		data.put("trackId", cpRequest.getValue("trackId"));
		data.put("vanTrxId", cpRequest.getValue("vanTrxId"));
		data.put("vanDay", cpRequest.getValue("vanDay"));
		data.put("vanStlFee", cpRequest.getLongValue("vanStlFee"));
		data.put("exeStatus","");
		data.put("regId", SessionUtil.getUserId(request));
		data.put("regDay",CommonUtil.getCurrentDate("yyyyMMdd"));
		

		DAO d = new DAO();
		d.setTable("PG_TRX_LOAD_DTL A, PG_TRX_LOAD B");
		d.setColumns("A.idx");
		d.addWhere("A.batchIdx = B.idx");
		d.addWhere("B.van", data.getString("van"));
		d.addWhere("A.vanTrxId", data.getString("vanTrxId"));
		d.addWhere("A.exeStatus", "완료", DAO.eq);
		d.setOrderBy("A.regDate asc");
		RecordSet r = d.search();
		if(r.size() > 0){
			return new CPRUtil(cpRequest)
	        		.resultNOK("이미 거래 생성 요청된 거래입니다. VAN 거래번호 기준 ")
	        		.cpResponse();
		}
		
		
		
		TrxDAO trxDAO = new TrxDAO();
		long batchIdx = trxDAO.insertTrxLoad(load);
		if(batchIdx == 0){
			return new CPRUtil(cpRequest)
	        		.resultNOK("거래 데이터 생성 실패",trxDAO.getError())
	        		.cpResponse();
		}
		data.put("batchIdx", batchIdx);
		
		
		if(trxDAO.insertTrxLoadDtl(data)){
			
			return new CPRUtil(cpRequest)
	        		.resultOK("거래데이터가 생성되었습니다. 처리 목록에서 실행하여 주시기 바랍니다.")
	        		.cpResponse();
		}else{
			trxDAO.deleteTrxLoad(batchIdx);
			return new CPRUtil(cpRequest)
	        		.resultNOK("거래 데이터 생성 실패",trxDAO.getError())
	        		.cpResponse();
		}
		
	}
	
	
	// 22.03 cvs 파일로 일괄 업로드 개발
	@ResponseBody
	@RequestMapping(value = "/KsnetExcelUpload" ,method = RequestMethod.POST )
	public String KsnetExUpload  (@RequestParam("fileUpload") MultipartFile file  ,HttpServletRequest req) throws IOException {
		
		// 파일 업로드 경로 지정
		String FILE_SERVER_PATH = CPUtil.getCanonicalWebPath() + File.separator + "notiFileUpload" ;
		// 파일 생성 
		file.transferTo(new File(FILE_SERVER_PATH, file.getOriginalFilename()));
		logger.info("[FILE_SERVER_PATH] : " + FILE_SERVER_PATH);
		
		NotiExcelUploadDAO addNoti = new NotiExcelUploadDAO();
		addNoti.KsnetNotiUpload(FILE_SERVER_PATH + File.separator + file.getOriginalFilename() ,req);
		return "거래데이터가 생성되었습니다. 처리 목록에서 실행하여 주시기 바랍니다.";
	}
	
	// 22.04 cvs 파일로 일괄 업로드 개발
	@ResponseBody
	@RequestMapping(value = "/GalaxExcelUpload" ,method = RequestMethod.POST )
	public String GalaxExUpload  (@RequestParam("fileUpload") MultipartFile file  ,HttpServletRequest req) throws IOException {
		
		// 파일 업로드 경로 지정
		String FILE_SERVER_PATH = CPUtil.getCanonicalWebPath() + File.separator + "notiFileUpload"+ File.separator ;
		// 파일 생성 
		file.transferTo(new File(FILE_SERVER_PATH, file.getOriginalFilename()));
		logger.info("[FILE_SERVER_PATH] 지정경로 파일 생성 완료 =>" + FILE_SERVER_PATH);
		
		NotiExcelUploadDAO addNoti = new NotiExcelUploadDAO();
		addNoti.KsnetNotiUpload(FILE_SERVER_PATH + File.separator + file.getOriginalFilename() ,req);
		return "거래데이터가 생성되었습니다. 처리 목록에서 실행하여 주시기 바랍니다.";
	}

	
	
	/*@RequestMapping(value = "    ", method = RequestMethod.GET)
    public ModelAndView riskHistory(HttpServletRequest request, @PathVariable String capId) {
		DAO dao = new DAO();
		dao.setTable("HT_TRX_CAP_DTL");
		dao.setColumns("*");
		dao.addWhere("capId", capId);
		
		request.setAttribute("DATAMAP", dao.search().getRows());
        return new ModelAndView("/trx/static/modal");
    } */
	
	@RequestMapping(value = "/trxoper/load/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loadList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		
		TotLoadDAO trxLoadDAO = new TotLoadDAO();
		RecordSet rset = trxLoadDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxLoadDAO).setView(request,"/trxoper/load/list","");
	}
	
	
	
	@RequestMapping(value = "/trxoper/load/status/{idx}", method = RequestMethod.POST)
	public @ResponseBody String capdelAction(HttpServletRequest request,@PathVariable long idx) {
		if(idx == 0){
			return "NOK:업로드 항목이 선택되지 않았습니다.";
		}else{
			if(new TrxDAO().updateTrxLoad(idx,"요청")){
				return "OK:데이터 처리가 요청되었습니다.";
			}else{
				return "OK:데이터 처리 요청 실패 관리자에게 문의하여 주시기 바랍니다.";
			}
		}	
	}
	
	@RequestMapping(value = "/trxoper/load/dtl/{idx}", method = RequestMethod.GET)
	public ModelAndView loadDtlForm(HttpServletRequest request, @PathVariable String idx) {
		
		request.setAttribute("LOAD_MAP", new TotLoadDAO().getByIdx(idx).getRow(0));
		return new ModelAndView("/trxoper/load/dtl/form");
	}
	
	@RequestMapping(value = "/trxoper/load/dtl/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loadDtlList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		
		TotLoadDtlDAO trxLoadDtlDAO = new TotLoadDtlDAO();
		RecordSet rset = trxLoadDtlDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxLoadDtlDAO).setView(request,"/trxoper/load/dtl/list","");
	}
	
	@RequestMapping(value = "/trxoper/load/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> loadInsert(HttpServletRequest request,@RequestBody List<SharedMap<String, Object>> requestList) {
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		
		if(new TotLoadDAO().insert(requestList, SessionUtil.getUserId(request)) < 1 ) {
			resMap.put("result", "NOK");
		} else {
			resMap.put("result", "OK");
		}
		
		return resMap;
	}
	
	@RequestMapping(value = "/trxoper/load/delete/{idx}", method = RequestMethod.POST)
	public @ResponseBody String loadDelete(HttpServletRequest request, @PathVariable long idx) {
		if(idx == 0){
			return "NOK:삭제 항목이 선택되지 않았습니다.";
		}else{
			if(new TrxDAO().deleteTrxLoad2(idx)){
				return "OK:데이터가 삭제되었습니다.";
			}else{
				return "OK:데이터 삭제 실패 관리자에게 문의하여 주시기 바랍니다.";
			}
		}	
		
	}
	
	@RequestMapping(value = "/trxoper/new/modify/{idx}", method = RequestMethod.GET)
	public ModelAndView loadModifyForm(HttpServletRequest request, @PathVariable String idx) {
		
		request.setAttribute("LOADDTL_MAP", new TotLoadDAO().getByDtlIdx(idx).getRow(0));
		request.setAttribute("LOAD_MAP", new TotLoadDAO().getByIdx(idx).getRow(0));
		request.setAttribute("TRXLOAD_MAP", new TotLoadDAO().getByTrxIdx(idx).getRow(0));
		return new ModelAndView("/trxoper/new/modify");
	}
	
	@RequestMapping(value = "/trxoper/new/update", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse loadUpdate(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		
		SharedMap<String,Object> load = new SharedMap<String,Object>();
		load.put("idx", cpRequest.getKeyValue("idx"));
		load.put("mchtId", cpRequest.getValue("mchtId"));
		load.put("tmnId" , cpRequest.getValue("tmnId"));
		if(cpRequest.getValue("trnType").equals("승인")) {
			load.put("amount", cpRequest.getLongValue("amount"));
		} else {
			if(cpRequest.getLongValue("amount") > 0) {
				load.put("amount", -cpRequest.getLongValue("amount"));
			} else {
				load.put("amount", cpRequest.getLongValue("amount"));
			}
			
		}
		
		load.put("cnt", 1);
		load.put("vanType","ONLINE");
		load.put("status","등록");
		load.put("regId", SessionUtil.getUserId(request));
		load.put("regDay",CommonUtil.getCurrentDate("yyyyMMdd"));
		
		
		DAO dao = new DAO();
		dao.setTable("PG_MCHT_TMN A, PG_VAN B ");
		dao.setColumns("A.van,B.vanId");
		dao.setWhere("A.vanIdx = B.idx");
		dao.addWhere("tmnId", load.getString("tmnId"), DAO.eq);
		dao.setOrderBy("A.van asc");
		RecordSet rset = dao.search(); 
		dao.initRecord();
		SharedMap<String,Object> tmnMap = rset.getRowFirst();
		load.put("van",tmnMap.getString("van") );
		load.put("vanId",tmnMap.getString("vanId") );
		load.put("rootTrxId", cpRequest.getValue("rootTrxId"));
		

		SharedMap<String,Object> data = new SharedMap<String,Object>();
		RecordSet rootCap = null;
		
		data.put("trnType", cpRequest.getValue("trnType"));
		if(cpRequest.getValue("trnType").equals("승인")) {
			data.put("amount", cpRequest.getLongValue("amount"));
		} else {
			if(cpRequest.getLongValue("amount") > 0) {
				data.put("amount", -cpRequest.getLongValue("amount"));
			} else {
				data.put("amount", cpRequest.getLongValue("amount"));
			}
			rootCap = new TrxCapDAO().getByTrxId(cpRequest.getValue("rootTrxId"));
		}
		
		data.put("installment", CommonUtil.zerofill(cpRequest.getLongValue("installment"),2));
		if(rootCap != null) {
			data.put("bin", rootCap.getRowFirst().getString("bin"));
			data.put("last4", rootCap.getRowFirst().getString("last4"));
			data.put("authCd", rootCap.getRowFirst().getString("authCd"));
			data.put("rootTrxDay", rootCap.getRowFirst().getString("trxDay"));
		} else {
			data.put("bin", cpRequest.getValue("bin"));
			data.put("last4", cpRequest.getValue("last4"));
			data.put("authCd", cpRequest.getValue("authCd"));
			data.put("rootTrxDay", "");
		}
		
		data.put("trxDay", cpRequest.getValue("trxDay"));
		data.put("trxTime", cpRequest.getValue("trxTime"));
		data.put("trackId", cpRequest.getValue("trackId"));
		data.put("vanTrxId", cpRequest.getValue("vanTrxId"));
		data.put("vanDay", cpRequest.getValue("vanDay"));
		data.put("vanStlFee", cpRequest.getLongValue("vanStlFee"));
		data.put("exeStatus","");
		data.put("regId", SessionUtil.getUserId(request));
		data.put("regDay",CommonUtil.getCurrentDate("yyyyMMdd"));
		

		DAO d = new DAO();
		d.setTable("PG_TRX_LOAD_DTL A, PG_TRX_LOAD B");
		d.setColumns("A.idx");
		d.addWhere("A.batchIdx = B.idx");
		d.addWhere("B.van", data.getString("van"));
		d.addWhere("A.vanTrxId", data.getString("vanTrxId"));
		d.addWhere("A.exeStatus", "완료", DAO.eq);
		d.setOrderBy("A.regDate asc");
		RecordSet r = d.search();
		if(r.size() > 0){
			return new CPRUtil(cpRequest)
	        		.resultNOK("이미 거래 생성 요청된 거래입니다. VAN 거래번호 기준 ")
	        		.cpResponse();
		}
		
		
		
		TrxDAO trxDAO = new TrxDAO();
		boolean updated = trxDAO.updateTrxLoad2(load);
		if(updated == false){
			return new CPRUtil(cpRequest)
	        		.resultNOK("거래 데이터 수정 실패",trxDAO.getError())
	        		.cpResponse();
		}
		data.put("batchIdx", cpRequest.getValue("batchIdx"));
		
		
		if(trxDAO.updateTrxLoadDtl(data)){
			
			return new CPRUtil(cpRequest)
	        		.resultOK("거래데이터가 수정되었습니다. 처리 목록에서 실행하여 주시기 바랍니다.")
	        		.cpResponse();
		}else{
//			trxDAO.deleteTrxLoad2(cpRequest.getValue("batchIdx"));
			return new CPRUtil(cpRequest)
	        		.resultNOK("거래 데이터 수정 실패",trxDAO.getError())
	        		.cpResponse();
		}
		
	}
	
	
	@RequestMapping(value = "/trxoper/load/multi/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> loadMultiInsert(HttpServletRequest request,@RequestBody List<SharedMap<String, Object>> requestList) {
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();
		
		List<String> tmnIdArray = new ArrayList<String>();
		for(SharedMap<String, Object> eachMap : requestList) {
			String tmnId = eachMap.getString("tmnId");
			if(tmnId.length() > 0 && tmnIdArray.indexOf(tmnId) == -1) {
				tmnIdArray.add(tmnId);
			}
		}
		
		for(String tmnId : tmnIdArray) {
			List<SharedMap<String, Object>> insertList = new ArrayList<SharedMap<String, Object>>();
			RecordSet rset = new MchtTmnDAO().getById(tmnId);
			logger.debug("TMN ID SIZE: {} = {}", tmnId, rset.size());
			if(rset.size() < 1) {
				resMap.put("result", "NOK");
				resMap.put("msg", "유효하지 않은 터미널 ID 입니다. " + tmnId);
				return resMap;
			}
			
			SharedMap<String, Object> eachTmnMap = rset.getRow(0);
			
			for(SharedMap<String, Object> eachMap : requestList) {
				if(eachMap.getString("tmnId").equals(tmnId)) {
					eachMap.put("mchtId", eachTmnMap.getString("mchtId"));
					insertList.add(eachMap);
				}
			}
			
			if(new TotLoadDAO().insert(insertList, SessionUtil.getUserId(request)) < 1 ) {
				resMap.put("result", "NOK");
				resMap.put("msg", "DB 업로드에 실패했습니다. " + tmnId);
				return resMap;
			} else {
				resMap.put("result", "OK");
			}
			
		}
		
		
		
		
		return resMap;
	}
	
	@RequestMapping(value = "/settle/tax/export", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView taxExport(HttpServletRequest request,@RequestBody SharedMap<String, Object> requestMap) {
		CPDAO dao = new CPDAO();
		
		if(requestMap.getString("distType").equalsIgnoreCase("true")) {
			//dao.setTable("(SELECT MAX(trxDay) as endDay, taxId, mchtId, name, sum(stlAgencyFee+benefit) amt FROM VW_TRX_CAP WHERE distId ='00' and vanStatus = '입금완료' and substr(stlVanDay,1,6) ='"+requestMap.getString("trxDay")+"' group by taxId) A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId LEFT JOIN PG_MCHT C ON A.mchtId = C.mchtId");
			dao.setTable("(SELECT MAX(trxDay) as endDay, taxId, mchtId, name, sum(stlAgencyFee+benefit) amt FROM VW_TRX_CAP WHERE distId ='00' and substr(stlVanDay,1,6) ='"+requestMap.getString("trxDay")+"' group by taxId) A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId LEFT JOIN PG_MCHT C ON A.mchtId = C.mchtId");
			dao.setColumns("A.endDay, A.taxId, A.mchtId, A.name, "
							+ "A.taxId, A.mchtId, A.name, TRUNCATE(A.amt*10/110,0) as stlFeeVat ,(A.amt-TRUNCATE(A.amt*10/110,0)) as stlFee, "
							+ "FN_AES_DEC(B.identity) as identity, B.ceoName, B.compName, B.addr1, B.addr2, B.email ,C.bizCategory, C.bizType");
			dao.setOrderBy("A.name");
		} else {
			dao.setTable("VW_TRX_CAP A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId LEFT JOIN PG_MCHT C ON A.mchtId = C.mchtId");
			dao.setColumns("MAX(trxDay) as endDay, A.taxId, A.mchtId, A.name, SUM(amount) AS amount, SUM(vat) AS vat, "
							+ "TRUNCATE(SUM(stlFee+stlFeeVat)*10/110,0) AS stlFeeVat, "
							+ "(SUM(stlFee+stlFeeVat)-TRUNCATE(SUM(stlFee+stlFeeVat)*10/110,0)) AS stlFee, "
							+ "FN_AES_DEC(B.identity) as identity, B.ceoName, B.compName, B.addr1, B.addr2, B.email ,C.bizCategory, C.bizType");
			
			dao.setWhere("SUBSTR(A.stlVanDay,1,6) = '" + requestMap.getString("trxDay") + "'");
			dao.addWhere("A.distId", "00", DAO.ne);
			//dao.addWhere("vanStatus", "입금완료", DAO.eq);
			dao.setGroupBy("taxId");
			dao.setOrderBy("A.name");
		}
		

		RecordSet rset = dao.search();

		if (rset.size() > 0) {
			List<SharedMap<String, Object>> targetList = rset.getRows();
			SharedMap<String, Object> senderMap = new SharedMap<String, Object>();
			senderMap.put("identity", requestMap.getString("identity"));
			senderMap.put("compName", requestMap.getString("compName"));
			senderMap.put("ceoName", requestMap.getString("ceoName"));
			senderMap.put("addr1", requestMap.getString("addr1"));
			senderMap.put("addr2", requestMap.getString("addr2"));
			senderMap.put("bizCategory", requestMap.getString("bizCategory"));
			senderMap.put("bizType", requestMap.getString("bizType"));
			senderMap.put("email", requestMap.getString("email"));
			senderMap.put("distType", requestMap.getString("distType"));

			TaxExport taxExport = new TaxExport();

			String link = "";
			try {
				link = taxExport.makeTaxExcel(senderMap, targetList, SessionUtil.getUserId(request));
			} catch (Exception e) {
				logger.debug("엑셀 출력 오류 {}", e);
				SharedMap<String, String> resMap = new SharedMap<String, String>(); 
				resMap.put("file", "");
				resMap.put("msg", "출력할 수 없습니다.");
				return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(resMap));
			}

			CPResponse cpResponse = new CPResponse();
			Files file = new Files();
			file.link = link;
			cpResponse.file = file;
			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		SharedMap<String, String> resMap = new SharedMap<String, String>(); 
		resMap.put("file", "");
		return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(resMap));
	}
	
	@RequestMapping(value = "/tmnId/check/{tmnId}", method = RequestMethod.GET)
	public @ResponseBody Object tmnIdCheck(HttpServletRequest request, @PathVariable String tmnId) {
		SharedMap<String, Object> map = new TrxDAO().getByTmnId(tmnId).getRowFirst();
		return map;
	}
	
}









