package com.pgmate.app.ctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.pgmate.app.dao.MchtDdctDAO;
import com.pgmate.app.dao.PispSettleDAO;
import com.pgmate.app.dao.SettleDAO;
import com.pgmate.app.dao.SettleDdctDAO;
import com.pgmate.app.dao.SettleHoldDAO;
import com.pgmate.app.dao.SettleMchtDAO;
import com.pgmate.app.dao.SettleSubDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.app.dao.VactSettleDAO;
import com.pgmate.app.dao.VactSettleMchtDAO;
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
public class VactSettleController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.VactSettleController.class);

	@RequestMapping(value = "/vactSettle/mcht/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleMchtList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		VactSettleMchtDAO settleDAO = new VactSettleMchtDAO();
		cpRequest.setData("stlAmount", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("mchtName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/vactSettle/mcht/list", "");
	}
	
	@RequestMapping(value = "/vactSettle/mcht/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> decide(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		DAO dao = new DAO();
		
		for(SharedMap<String, String> eachMap : requestList) {
				
			logger.debug("VACT MCHT SETTLE SAVE = stlId: {}", eachMap.getString("stlId"));
			dao.setDebug(true);
			dao.setTable("PG_VACT_SETTLE_MCHT");
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
	
	@RequestMapping(value = "/vactSettle/status/mcht/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleSubStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_VACT_SETTLE_MCHT");
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
			if (dao.update("UPDATE PG_VACT_SETTLE_MCHT SET status = '" + status + "' WHERE stlId IN (" + stlId + ")")) {
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

	@RequestMapping(value = "/vactSettle/paystatus/mcht/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleSubPayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_VACT_SETTLE_MCHT");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("status", "확정", DAO.ne);
		dao.addWhere("stlId", stlId, DAO.in);

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_VACT_SETTLE_MCHT SET payStatus = '" + status + "', payOutDay = '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
				
				// 충전정산 입금 처리
				List<SharedMap<String, Object>> chargeSettleList = new VactSettleMchtDAO().getbyChargeSettleLists(stlId).getRows();
				if(chargeSettleList.size() > 0) {
					for(SharedMap<String, Object> settleMap:chargeSettleList) {
						if(settleMap.getLong("payOutAmt") != 0) {
							ChargeSettleDAO chargeSettleDAO = new ChargeSettleDAO();
							settleMap.put("trxId", chargeSettleDAO.getChargeSettleTrxId());
							if(settleMap.getLong("stlAmount") > 0) {
								settleMap.put("trxType", "입금");
							}else {
								settleMap.put("trxType", "출금");
							}
							settleMap.put("trxUnit", "가상계좌정산");
							String regDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
							settleMap.put("trxDay", regDate.substring(0, 8));
							settleMap.put("trxTime", regDate.substring(8));
							settleMap.put("trackId", settleMap.getString("stlId"));
							settleMap.put("refId", settleMap.getString("stlId"));
							settleMap.put("netAmount", settleMap.getLong("payOutAmt"));
							settleMap.put("balance", chargeSettleDAO.getMchtBalance(settleMap.getString("mchtId")).getLong("balance")+settleMap.getLong("netAmount"));
							String stlDay = settleMap.getString("stlDay").substring(0, 4)+"-"+settleMap.getString("stlDay").substring(4,6)+"-"+settleMap.getString("stlDay").substring(6);
							settleMap.put("summary", stlDay+" 정산일자 가상계좌 정산금 지급");
							settleMap.put("regId", SessionUtil.getUserId(request));
							settleMap.put("regDay", regDate.substring(0, 8));
							
							chargeSettleDAO.insertChargeSettle(settleMap);
						}
					}
				}
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

	
	@RequestMapping(value = "/vactSettle/mcht/export", method = RequestMethod.POST)
	public @ResponseBody Object settleSubExport(HttpServletRequest request, @RequestParam String bankCd, @RequestParam String stlId) {
		CPDAO dao = new CPDAO();
		dao.setTable("VW_VACT_SETTLE_MCHT");
		dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmount,accntHolder,'' as a,'' as b,'사업자' as memberName, stlId");
		dao.addWhere("status", "확정", DAO.eq);
		dao.addWhere("stlId", stlId, DAO.in);
		dao.addWhere("settleSvc", "일반", DAO.eq);
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
			boolean headerView = false;
			if (bankCd.equals("020")) {
				doc.title = "우리은행";
				thead.put("bankCd", "bankCd");
				thead.put("account", "account");
				thead.put("stlAmount", "stlAmt");
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
				thead.put("dtlName", "dtlName");
			} else {
				doc.title = "하나은행";
				thead.put("bankCd", "입금은행코드");
				thead.put("account", "입금계좌번호");
				thead.put("stlAmt", "이체금액");
				thead.put("accntHolder", "예상예금주");
				thead.put("dtlName", "memberName");
				thead.put("stlId", "보내는분 통장표시내용");
				headerView = true;
			}
			doc.title += " 정산 지급 데이터";

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
	
	@RequestMapping(value = "/vactSettle/dist/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDistList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		VactSettleDAO settleDAO = new VactSettleDAO();
		cpRequest.setData("grade", "대행사", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/vactSettle/dist/list", "");
	}

	@RequestMapping(value = "/vactSettle/agency/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAgencyList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		VactSettleDAO settleDAO = new VactSettleDAO();
		cpRequest.setData("grade", "에이전시", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/vactSettle/agency/list", "");
	}

	@RequestMapping(value = "/vactSettle/sales/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleSalesList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		VactSettleDAO settleDAO = new VactSettleDAO();
		cpRequest.setData("grade", "지사", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/vactSettle/sales/list", "");
	}

	@RequestMapping(value = "/vactSettle/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> vactDecide(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		String regId = SessionUtil.getUserId(request);
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
		DAO dao = new DAO();
		
		for(SharedMap<String, String> eachMap : requestList) {
				
			logger.debug("VACT SETTLE SAVE = stlId: {}", eachMap.getString("stlId"));
			dao.setDebug(true);
			dao.setTable("PG_VACT_SETTLE");
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
	
	@RequestMapping(value = "/vactSettle/payout/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/vactSettle/payout/list", "");
	}

	@RequestMapping(value = "/vactSettle/detail", method = RequestMethod.POST)
	public ModelAndView detail(HttpServletRequest request, @RequestParam String grade, @RequestParam String stlId) throws Exception {
		String columns = "vactId,issueId,mchtId,mchtName,amount,issuerBank,account,vactType,sender,trxType,rootVactId,regDate,stlType";
		LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
		thead.put("vactId", "거래번호");
		thead.put("mchtId", "가맹점ID");

		thead.put("mchtName", "가맹점");
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
		thead.put("amount", "금액");
		thead.put("issuerBank", "가상계좌발행은행");
		thead.put("account", "가상계좌번호");
		thead.put("vactType", "발행타입");
		thead.put("sender", "보낸사람");
		thead.put("trxType", "거래구분");
		thead.put("rootTrxId", "원거래번호");
		thead.put("stlType", "정산일 기준");

		if (grade.equals("stlDistId")) {
			columns += ", stlDistFee, stlDistFeeVat, stlDistDay, stlDistId";
			thead.put("stlDistFee", "정산 수수료");
			thead.put("stlDistFeeVat", "정산 수수료 Vat");
			thead.put("stlDistDay", "정산예정일");
			thead.put("stlDistId", "정산 ID");
		} else if (grade.equals("stlAgencyId")) {
			columns += ", stlAgencyFee, stlAgencyFeeVat, stlAgencyDay, stlAgencyId";
			thead.put("stlAgencyFee", "정산 수수료");
			thead.put("stlAgencyFeeVat", "정산 수수료 Vat");
			thead.put("stlAgencyDay", "정산예정일");
			thead.put("stlAgencyId", "정산 ID");
		} else if (grade.equals("stlSalesId")) {
			columns += ", stlSalesFee, stlSalesFeeVat, stlSalesDay, stlSalesId";
			thead.put("stlSalesFee", "정산 수수료");
			thead.put("stlSalesFeeVat", "정산 수수료 Vat");
			thead.put("stlSalesDay", "정산예정일");
			thead.put("stlSalesId", "정산 ID");
		} else if (grade.equals("stlId")) {
			columns += ",stlAmount, stlFee, stlFeeVat, stlDay, stlId";
			thead.put("stlAmount", "정산 금액");
			thead.put("stlFee", "정산 수수료");
			thead.put("stlFeeVat", "정산 수수료 VAT");
			thead.put("stlDay", "정산예정일");
			thead.put("stlId", "정산 ID");
		}

		thead.put("regDate", "거래일시");
		
		CPDAO dao = new CPDAO();
		dao.setTable("VW_VACT_TRX");
		dao.setColumns(columns);
		dao.addWhere(grade, stlId, CPDAO.eq);

		RecordSet recordSet = dao.search();
		if (recordSet.size() > 0) {

			CPDocument doc = new CPDocument();
			doc.title = "가상계좌 정산 대상 거래";
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
	
	@RequestMapping(value = "/vactSettle/modal/{stlId}", method = RequestMethod.GET)
	public ModelAndView settleView(HttpServletRequest request, @PathVariable String stlId) {
		request.setAttribute("DATAMAP", new SettleDAO().getById(stlId).getRow(0));
		return new ModelAndView("/vactSettle/modal");
	}

	//TODO
	@RequestMapping(value = "/vactSettle/status/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_VACT_SETTLE");
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
			if (dao.update("UPDATE PG_VACT_SETTLE SET status = '" + status + "' WHERE stlId IN (" + stlId + ")")) {
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

	@RequestMapping(value = "/vactSettle/paystatus/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settlePayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_VACT_SETTLE");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("status", "확정", DAO.ne);
		dao.addWhere("stlId", stlId, DAO.in);

		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_VACT_SETTLE SET payStatus = '" + status + "', payOutDay = '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
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

	@RequestMapping(value = "/vactSettle/export", method = RequestMethod.POST)
	public @ResponseBody Object settleExport(HttpServletRequest request, @RequestParam String bankCd, @RequestParam String stlId) {
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("VW_VACT_SETTLE");
		//dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,CONCAT('(정산)',memberName) as memberName, stlId");
		dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,'사업자' as memberName, stlId");
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
			boolean headerView = false;
			
			logger.debug("bankCd [{}]",bankCd);
			
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

	@RequestMapping(value = "/vactSettle/payoutsub/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView payoutSubList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleSubDAO settleDAO = new SettleSubDAO();
		cpRequest.setData("status", "확정", "eq", "", true);
		cpRequest.setData("payStatus", "대기", "eq", "", true);

		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/vactSettle/payoutsub/list", "");
	}

	@RequestMapping(value = "/vactSettle/detail/sub", method = RequestMethod.POST)
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

	@RequestMapping(value = "/vactSettle/modal/sub/{stlId}", method = RequestMethod.GET)
	public ModelAndView settleSubView(HttpServletRequest request, @PathVariable String stlId) {
		request.setAttribute("DATAMAP", new SettleSubDAO().getById(stlId).getRow(0));
		return new ModelAndView("/vactSettle/modal");
	}

	@RequestMapping(value = "/vactSettle/calc/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView calcList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		request.setAttribute("SUMMAP", new TrxCapDAO().calcPaySum(cpRequest.data).getRow(0));

		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset = trxCapDAO.calcPayList(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, trxCapDAO).setView(request, "/vactSettle/calc/list", "");
	}

	// ==============================

	@RequestMapping(value = "/vactSettle/taxexport/{date}", method = RequestMethod.POST)
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
			senderMap.put("identity", "4198800046");
			senderMap.put("compName", "㈜광원");
			senderMap.put("ceoName", "강승구");
			senderMap.put("addr1", "서울특별시 서초구 서초대로");
			senderMap.put("addr2", "54길 46 2층");
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
	
}
