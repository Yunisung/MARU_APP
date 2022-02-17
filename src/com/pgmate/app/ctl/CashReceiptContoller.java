package com.pgmate.app.ctl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

import com.pgmate.app.dao.CashReceiptDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class CashReceiptContoller {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.CashReceiptContoller.class);
	
	private String type = ""; //API - test, real
	private String mid = "";
	private String ApiURL = "";
	
	//현금영수증 조회 페이지
	@RequestMapping(value = "/cashReceipt/list", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView receiptList(HttpServletRequest request, @RequestBody CPRequest cpRequest) throws Exception {
		SessionUtil.setSearchGrade(request, cpRequest);
		
		CashReceiptDAO cashReceiptDAO = new CashReceiptDAO();
		RecordSet rset = cashReceiptDAO.list(cpRequest.data,cpRequest.page);
		
		return new CPRUtil(cpRequest).dataList(rset,cashReceiptDAO).setView(request,"/cashReceipt/list","");
    }
	
	//현금영수증 상세 조회 페이지
	@RequestMapping(value = "/cashReceipt/view/{cashId}", method = RequestMethod.GET)
    public ModelAndView cashReceiptView(HttpServletRequest request, @PathVariable String cashId) {
		SharedMap<String, Object> res =  new CashReceiptDAO().getByCashId(cashId).getRow(0);
		
		request.setAttribute("DATAMAP", res);
		request.setAttribute("DATAORG", new CashReceiptDAO().getByorgAuthNo(res.getString("orgAuthNo")).getRow(0));
        return new ModelAndView("/cashReceipt/modal");
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cashReceipt/insert", method = RequestMethod.POST)
	public @ResponseBody JSONObject receiptAdd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject apiRes = new JSONObject();
		JSONObject result = new JSONObject();
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		String payKey = request.getHeader("Authorization");
		
		try {
			configSetting();
			
			logger.info("----- CASH RECEIPT TEST SEND START -----");
			
			String checkSvcAmt = request.getParameter("svcAmt");
			String checkAssort = request.getParameter("assort");
			
			data.put("mchtId", request.getParameter("mchtId"));
			data.put("assort", checkAssort);
			data.put("cashType", request.getParameter("cashType"));
			data.put("trxId", request.getParameter("trxId")); //가상계좌 발급번호 issueId
			data.put("transNo", request.getParameter("transNo"));
			data.put("ordNm", request.getParameter("ordNm"));
			data.put("taxYn", request.getParameter("taxYn"));
			data.put("amt", request.getParameter("amt"));
			data.put("vat", request.getParameter("vat"));
			data.put("purpose", request.getParameter("purpose"));
			data.put("identity", request.getParameter("identity"));
			data.put("identityGb", request.getParameter("identityGb"));
			data.put("deductionType", request.getParameter("deductionType"));
			data.put("bizRegNo", request.getParameter("bizRegNo"));
			data.put("trDt", request.getParameter("trDt"));
			data.put("regInfo", "ADMIN");
			
			if (checkSvcAmt == null || "".equals(checkSvcAmt)) {
				data.put("svcAmt", "0");
			} else {
				data.put("svcAmt", checkSvcAmt);
			}
			
			if ("1".equals(checkAssort)) {
				data.put("orgAuthNo", request.getParameter("orgAuthNo"));
				data.put("orgTrDt", request.getParameter("orgTrDt"));
			}
	
			result = exec(ApiURL, data, payKey);
			
			String cashId = (String) result.get("cashId");
			String authNo = (String) result.get("authNo");
			String resultCd = (String) result.get("resultCd");
			String resultMsg = (String) result.get("resultMsg");
			String trDt = (String) result.get("trDt");
			String regDate = (String) result.get("regDate");
			
			logger.info("CashReceipt TEST RES : [{}][{}]", cashId, result.toJSONString());
			
			apiRes.put("cashId", cashId);
			apiRes.put("authNo", authNo);
			apiRes.put("resultCd", resultCd);
			apiRes.put("resultMsg", resultMsg);
			apiRes.put("trDt", trDt);
			apiRes.put("regDate", regDate);
			
			if("1".equals(checkAssort)) {
				String amt = (String) result.get("amt");
				String vat = (String) result.get("vat");
				String svcAmt =  (String) result.get("svcAmt");
				
				apiRes.put("amt", amt);
				apiRes.put("vat", vat);
				apiRes.put("svcAmt", svcAmt);
			}
		} catch (Exception e) {
			apiRes.put("cashId", "");
			apiRes.put("resultCd", "2222");
			apiRes.put("resultMsg", "시스템 오류");
			apiRes.put("regDate", CommonUtil.getCurrentTimestamp());
	
			logger.error(e.getMessage(), e);
			logger.error(" ----- CASH RECEIPT TEST SEND ERROR -----");
		}
		
		logger.info("CASH RECEIPT SEND RES : " + apiRes.toJSONString());
		logger.info(" ----- CASH RECEIPT TEST SEND END -----");
		
		return apiRes;
	}
	
	/**
	 * 유효성 체크
	 * 
	 * @param request
	 * @return
	 */
	private String valid(HttpServletRequest request) {
		String resMsg = "";
		String regExp = "^[0-9]*$";

		if (request.getParameter("mchtId") == null || "".equals(request.getParameter("mchtId"))) {
			resMsg = "가맹점 아이디 파라메터 오류 (mchtId) " + request.getParameter("mchtId");
		} else if (request.getParameter("assort") == null || "".equals(request.getParameter("assort"))
				|| (!"0".equals(request.getParameter("assort")) && !"1".equals(request.getParameter("assort")))) {
			resMsg = "승인구분 파라메터 오류 (assort)" + request.getParameter("assort");
		} else if (request.getParameter("transNo") == null || "".equals(request.getParameter("transNo"))) {
			resMsg = "주문번호 파라메터 오류 (transNo)" + request.getParameter("transNo");
		} else if ("1".equals(request.getParameter("assort")) && (request.getParameter("orgTrDt") == null || "".equals(request.getParameter("orgTrDt")))) {
			resMsg = "원거래  승인일시 파라메터 오류 (orgTrDt)" + request.getParameter("orgTrDt");
		} else if ("1".equals(request.getParameter("assort")) && (request.getParameter("orgAuthNo") == null || "".equals(request.getParameter("orgAuthNo")))) {
			resMsg = "원거래 승인번호 파라메터 오류 (orgAuthNo)" + request.getParameter("orgAuthNo");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("trDt") == null || "".equals(request.getParameter("trDt")) || request.getParameter("trDt").length() != 14)) {
			resMsg = "거래일시 파라메터 오류 (trDt)" + request.getParameter("trDt");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("amt") == null || "".equals(request.getParameter("amt")))) {
			resMsg = "공급가액 파라메터 오류 (amt)" + request.getParameter("amt");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("vat") == null || "".equals(request.getParameter("vat")))) {
			resMsg = "부가세 파라메터 오류 (vat)" + request.getParameter("vat");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("bizRegNo") == null || "".equals(request.getParameter("bizRegNo")))) {
			resMsg = "사업자번호 파라메터 오류 (bizRegNo)" + request.getParameter("bizRegNo");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("cashType") == null || "".equals(request.getParameter("cashType")))) {
			resMsg = "현금영수증타입 파라메터 오류 (cashType)" + request.getParameter("cashType");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("purpose") == null || "".equals(request.getParameter("purpose"))
				|| (!"0".equals(request.getParameter("purpose")) && !"1".equals(request.getParameter("purpose"))))) {
			resMsg = "용도구분 파라메터 오류 (purpose)" + request.getParameter("purpose");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("identityGb") == null || "".equals(request.getParameter("identityGb")) || (!"1".equals(request.getParameter("identityGb"))
				&& !"3".equals(request.getParameter("identityGb")) && !"4".equals(request.getParameter("identityGb"))))) {
			resMsg = "등록번호 구분 파라메터 오류 (identityGb)" + request.getParameter("identityGb");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("identity") == null || "".equals(request.getParameter("identity")))) {
			resMsg = "등록번호 파라메터 오류 (identity)" + request.getParameter("identity");
		} else if ("0".equals(request.getParameter("cashType")) && (request.getParameter("trxId") == null || "".equals(request.getParameter("trxId")))) {
			resMsg = "가상계좌 거래번호 파라메터 오류 (trxId) " + request.getParameter("trxId");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("amt").matches(regExp) == false)) {
			resMsg = "공급가액 파라메터 오류 (amt)" + request.getParameter("amt");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("vat").matches(regExp) == false)) {
			resMsg = "부가세 파라메터 오류 (vat)" + request.getParameter("vat");
		} else if ("0".equals(request.getParameter("assort")) && (request.getParameter("bizRegNo").matches(regExp) == false)) {
			resMsg = "사업자번호 파라메터 오류 (bizRegNo)" + request.getParameter("bizRegNo");
		}
		
		if (!"".equals(resMsg)) {
			logger.info(resMsg);
		}

		return resMsg;
	}

	/*
	 * Authorization
	 */
	public SharedMap<String, Object> authorization(String payKey, String mchtId) {
		CashReceiptDAO cashReceiptDAO = new CashReceiptDAO();
		SharedMap<String, Object> cashKey = new SharedMap<String, Object>();;
		
		if (CommonUtil.isNullOrSpace(payKey)) {
			cashKey = cashReceiptDAO.getPayKeyByMchtId(mchtId);
			logger.debug("authorized by mchtId : {}", mchtId);
		}
		
		// Authorization Header 값 비교
		cashKey = cashReceiptDAO.getPayKeyByKey(payKey);
		if (payKey == null) {
			logger.debug("Unauthorized {} : [{}] , Unregistered authorization key ", payKey);
		}
		
		return cashKey;
	}
	
	/*//관리자 페이지 현금영수증 등록/취소 로컬 테스트
		@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
		@RequestMapping(value = "/cashReceipt/insert", method = RequestMethod.POST)
		public @ResponseBody JSONObject receiptAdd(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			String payKey = request.getHeader("Authorization");
			
			JSONObject apiRes = new JSONObject();
			JSONObject result = new JSONObject();
			CashReceiptDAO cashReceiptDAO = new CashReceiptDAO();
			SharedMap<String, Object> map = new SharedMap<String, Object>();
			SharedMap<String, Object> data = new SharedMap<String, Object>();
			
			try {
				configSetting();
				
				logger.info("----- CASH RECEIPT TEST INSERT START -----");
				
				String validRes = valid(request);
				
				if (!"".equals(validRes)) {
					apiRes.put("resultCd", "2223");
					apiRes.put("resultMsg", validRes);
				} else {
					String assort = request.getParameter("assort");
					String orgAuthNo = request.getParameter("orgAuthNo");
					String transNo = request.getParameter("transNo");
					String amt = request.getParameter("amt");
					String mchtId = request.getParameter("mchtId");
					String trDt = request.getParameter("trDt");
					String orgTrDt = request.getParameter("orgTrDt");
					String trxId = request.getParameter("trxId");
					String taxYn = request.getParameter("taxYn");
					SharedMap<String,Object> cashReceiptStatus = cashReceiptDAO.cashReceiptStatus(mchtId);
					SharedMap<String, Object> key = authorization(payKey, mchtId);
					int checkTransNo = cashReceiptDAO.duplicateCheck(transNo);
					
					if ("미사용".equals(cashReceiptStatus) || cashReceiptStatus == null || cashReceiptStatus.size() == 0) {
						apiRes.put("resultCd", "1000");
						apiRes.put("resultMsg", "현금영수증 사용 가맹점이 아닙니다.");
						
						logger.info("CashReceipt cashReceiptYn  : [{}][{}]", mchtId, cashReceiptStatus);
					} else if (payKey == null || payKey.length() == 0 || key == null || key.size() == 0) {
						apiRes.put("resultCd", "2227");
						apiRes.put("resultMsg", "가맹점 paykey 오류");
		
						logger.info("CashReceipt authorization  : [{}][{}]", mchtId, payKey);
					} else {
						if (checkTransNo != 0) {
							apiRes.put("resultCd", "2224");
							apiRes.put("resultMsg", "동일한 transNo가 존재합니다.");
							
							logger.info("CashReceipt duplicateCheck  : [{}][{}]", mchtId, transNo);
						} else if ("0".equals(request.getParameter("cashType")) && !"0".equals(cashReceiptDAO.duplicateTrxId(trxId))){
							apiRes.put("resultCd", "2225");
							apiRes.put("resultMsg", "동일한 trxId가 존재합니다.");
							
							logger.info("CashReceipt duplicateTrxId  : [{}][{}]", mchtId, trxId);
						} else {
							logger.info("CashReceipt Req - assort : " + assort + ", transNo : " + transNo + ", amt : " + amt);
							
							data.put("cashId", cashReceiptDAO.getCashId());
							data.put("mid", mid); //세틀뱅크와 계약맺은 가맹점 아이디
							data.put("mchtId", mchtId);
							data.put("assort", assort);
							data.put("transNo", transNo);
							data.put("ordNm", request.getParameter("ordNm"));
							data.put("amt", request.getParameter("amt"));
							data.put("vat", request.getParameter("vat"));
							data.put("svcAmt", request.getParameter("svcAmt"));
							data.put("purpose", request.getParameter("purpose"));
							data.put("identity", cashReceiptDAO.getAESEnc(request.getParameter("identity")));
							data.put("identityGb", request.getParameter("identityGb"));
							data.put("deductionType", request.getParameter("deductionType"));
							data.put("bizRegNo", request.getParameter("bizRegNo"));
						
							if(data.get("svcAmt") == null || "".equals(data.get("svcAmt"))) {
								data.put("svcAmt", "0");
							}
							
							if(taxYn == null || "".equals(taxYn)) {
								data.put("taxYn", "N");
							} else {
								data.put("taxYn", taxYn);
							}
							
							if ("0".equals(assort)) {
								data.put("cashType", request.getParameter("cashType"));
								data.put("trDt", trDt.substring(0, 8));
								data.put("trTime", trDt.substring(8, 14));
								data.put("orgTrDt", trDt);
								
								// 가상계좌의 경우 입금 받은 후 현금영수증 처리가 되도록 StateCd을 '입금대기'으로 설정
								if ("0".equals(request.getParameter("cashType"))) {
									data.put("stateCd", "입금대기");
									data.put("trxId", request.getParameter("trxId")); //가상계좌 발급번호 issueId
								} else {
									data.put("stateCd", "전송대기");
									data.put("trxId", "");
								}
							} else if ("1".equals(assort)) {
								SharedMap<String,Object> authNoData = cashReceiptDAO.getOrgDataAuthNo(orgAuthNo);
								
								map.put("authNo", request.getParameter("orgAuthNo"));
								map.put("orgTrDt", orgTrDt);
								
								data.put("stateCd", "전송대기");
								data.put("trDt", orgTrDt.substring(0, 8));
								data.put("trTime", orgTrDt.substring(8, 14));
								data.put("orgAuthNo", orgAuthNo);
								data.put("orgTrDt", orgTrDt);
								data.put("cashType", "1");
								data.put("trxId", request.getParameter("trxId"));
								data.put("amt", authNoData.get("amt"));
								data.put("vat", authNoData.get("vat"));
								data.put("svcAmt", authNoData.get("svcAmt"));
								data.put("purpose", authNoData.get("purpose"));
								data.put("identityGb", authNoData.get("identityGb"));
								data.put("deductionType", authNoData.get("deductionType"));
								
								// 가상계좌의 경우 현금영수증 타입 0:가상계좌
								if (authNoData.get("cashType") == "0") {
									data.put("cashType", "0");
								}
								
								if(data.get("amt") == null || "".equals(data.get("amt"))) {
									data.put("amt", "0");
									data.put("vat", "0");
									data.put("svcAmt", "0");
								}
							}
							
							data.put("resultCd", "0000");
							data.put("resultMsg", "성공");
							
							//요청 정보 저장
							if(cashReceiptDAO.insertReceipt(data)) {
								//가상계좌 승인 요청은 저장 후 분 단위 배치를 진행한다.
								if("0".equals(request.getParameter("cashType"))) {
									apiRes.put("authNo", data.get("authNo"));
									apiRes.put("cashId", data.get("cashId"));
									apiRes.put("stateCd", data.get("stateCd"));
									apiRes.put("orgTrDt", trDt);
									apiRes.put("resultCd", "0000");
									apiRes.put("resultMsg", "가상계좌 거래 등록 성공");
								} else {
									//일반 거래, 가상계좌 취소 요청은 실시간 전송
									map.put("mid", mid); // 세틀뱅크와 계약맺은 가맹점 아이디
									map.put("assort", assort);
									map.put("transNo", transNo);
									map.put("taxYn", taxYn);
									map.put("amt", amt);
									map.put("vat", request.getParameter("vat"));
									map.put("svcAmt", request.getParameter("svcAmt"));
									map.put("ordNm", request.getParameter("ordNm"));
									map.put("bizRegNo", request.getParameter("bizRegNo"));
									map.put("purpose", request.getParameter("purpose"));
									map.put("identityGb", request.getParameter("identityGb"));
									map.put("identity", request.getParameter("identity"));
									map.put("deductionType", request.getParameter("deductionType"));
		
									//Optional Parameter NULL CHECK
									if ("0".equals(assort)) {
										map.put("trDt", trDt);
										if(request.getParameter("svcAmt") == null || "".equals(request.getParameter("svcAmt"))){
											map.put("svcAmt", "0");
										} else if(request.getParameter("deductionType") == null || "".equals(request.getParameter("deductionType"))) {
											map.put("deductionType", "");
										} else if(request.getParameter("ordNm") == null || "".equals(request.getParameter("ordNm"))) {
											map.put("ordNm", "");
										} else if(request.getParameter("taxYn") == null || "".equals(request.getParameter("taxYn"))) {
											map.put("taxYn", "N"); //과세구분 미입력시 과세(N)로 지정
										}
									}else if("1".equals(assort)) {
										if(request.getParameter("svcAmt") == null || "".equals(request.getParameter("svcAmt"))){
											map.put("svcAmt", "0");
										} else if(request.getParameter("deductionType") == null || "".equals(request.getParameter("deductionType"))) {
											map.put("deductionType", "");
										} else if(request.getParameter("ordNm") == null || "".equals(request.getParameter("ordNm"))) {
											map.put("ordNm", "");
										} else if(request.getParameter("amt") == null || "".equals(request.getParameter("amt"))) {
											map.put("amt", "");
										} else if(request.getParameter("vat") == null || "".equals(request.getParameter("vat"))) {
											map.put("vat", "");
										} else if(request.getParameter("bizRegNo") == null || "".equals(request.getParameter("bizRegNo"))) {
											map.put("bizRegNo", "");
										} else if(request.getParameter("purpose") == null || "".equals(request.getParameter("purpose"))) {
											map.put("purpose", "");
										} else if(request.getParameter("identityGb") == null || "".equals(request.getParameter("identityGb"))) {
											map.put("identityGb", "");
										} else if(request.getParameter("identity") == null || "".equals(request.getParameter("identity"))) {
											map.put("identity", "");
										} else if(request.getParameter("taxYn") == null || "".equals(request.getParameter("taxYn"))) {
											map.put("taxYn", "N"); //과세구분 미입력시 과세(N)로 지정
										}
									}
									
									result = exec(ApiURL, map, payKey);
									
									String cashId = (String) data.get("cashId");
									String authNo = (String) result.get("authNo");
									String resultCd = (String) result.get("resultCd");
									String resultMsg = (String) result.get("resultMsg");
									String trTime = (String) result.get("trTime");
									String stateCd = "전송성공";
									String errCd = "";
									
									logger.info("CashReceipt TEST RES : [{}][{}]", cashId, result.toJSONString());
									
									if (!"0000".equals(resultCd)) {
										stateCd = "전송실패";
										errCd = "9999";
									}
									
									apiRes.put("trDt", data.get("trDt"));
									apiRes.put("trTime", data.get("trTime"));
									apiRes.put("orgTrDt", trTime);
									
									if ("1".equals(assort)) {
										apiRes.put("orgAuthNo", orgAuthNo);
										
										if (!"0000".equals(resultCd)) {
											apiRes.put("trDt", CommonUtil.getCurrentDate("yyyyMMdd"));
											apiRes.put("trTime", CommonUtil.getCurrentDate("HHmmss"));
											apiRes.put("orgTrDt", data.get("orgTrDt"));
										} else {
											apiRes.put("trDt", trTime.substring(0, 8));
											apiRes.put("trTime", trTime.substring(8, 14));
										}
									}
									apiRes.put("authNo", authNo);
									apiRes.put("cashId", cashId);
									apiRes.put("resultCd", resultCd);
									apiRes.put("resultMsg", resultMsg);
									apiRes.put("stateCd", stateCd);
									apiRes.put("errCd", errCd);
									cashReceiptDAO.updateReceipt(apiRes);
								}
							} else {
								apiRes.put("resultCd", "2228");
								apiRes.put("resultMsg", "현금영수증 DB INSERT 오류");
								
								logger.info("현금영수증 DB TEST insert 오류 확인요망!!!");
							}
						}
					}
				}
				apiRes.put("cashId", apiRes.get("cashId"));
				apiRes.put("authNo", apiRes.get("authNo"));
				apiRes.put("resultCd", apiRes.get("resultCd"));
				apiRes.put("resultMsg", apiRes.get("resultMsg"));
				apiRes.put("trDt", apiRes.get("orgTrDt"));
				apiRes.put("regDate", CommonUtil.getCurrentTimestamp());
				if("1".equals(request.getParameter("assort"))) {
					apiRes.put("amt", data.get("amt"));
					apiRes.put("vat", data.get("vat"));
					apiRes.put("svcAmt", data.get("svcAmt"));
				}
				
			} catch (Exception e) {
				apiRes.put("cashId", "");
				apiRes.put("resultCd", "2222");
				apiRes.put("resultMsg", "시스템 오류");
				apiRes.put("regDate", CommonUtil.getCurrentTimestamp());
		
				logger.error(e.getMessage(), e);
				logger.error(" ----- CASH RECEIPT TEST INSERT ERROR -----");
			}
			
			logger.info("CASH RECEIPT INSERT TEST RES : " + apiRes.toJSONString());
			logger.info(" ----- CASH RECEIPT TEST INSERT END -----");
			
			return apiRes;
		}*/
	
	/**
	 * 현금영수증 등록 API호출
	 * 
	 * @param urlAddr
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONObject exec(String urlAddr, Map<String, Object> parameters, String paykey) throws Exception{
		JSONObject apiRes = new JSONObject();
		JSONParser jParser = new JSONParser();
		
        StringBuffer buffer = new StringBuffer();

        for(Map.Entry<String,Object> param : parameters.entrySet()) {
		    if(buffer.length() != 0) buffer.append('&');
		    buffer.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		    buffer.append('=');
		    buffer.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
        logger.error("postData : [{}]",buffer);
		//byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	
		URL url = new URL(urlAddr);
		
		logger.info("urlAddr : [{}]",urlAddr);
		//logger.error("postData : [{}]",postData);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", paykey);
		/*conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.getOutputStream().write(postDataBytes);*/
	
		try {
			if (conn.getResponseCode() != 200) {
				apiRes.put("resultCd", "9999");
				apiRes.put("resultMsg", "Connection Error : " +conn.getResponseCode());
				apiRes.put("authNo", "");
				apiRes.put("trTime", "");
				
				logger.error("exec Connection error : " + conn.getResponseCode());
			} else {
				OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		        PrintWriter writer = new PrintWriter(outStream);
		        writer.write(buffer.toString());
		        writer.flush();
		        
		        logger.info("buffer.toString() : " + buffer.toString());
				
		        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        StringBuffer stringBuffer = new StringBuffer();
		        String inputLine;

		        while ((inputLine = bufferedReader.readLine()) != null)  {
		             stringBuffer.append(inputLine);
		        }
		        bufferedReader.close();

		        String result = stringBuffer.toString();
		        logger.error("result : " + result);
		        
				apiRes = (JSONObject) jParser.parse(result);
				logger.info("Connection apiRes TEST : " + apiRes);
			}
		} catch (Exception e) {
			apiRes.put("resultCd", "9999");
			apiRes.put("resultMsg", e.getMessage());
			apiRes.put("authNo", "");
			apiRes.put("trTime", "");
			
			logger.error("exec TEST Exception : [{}], [{}]", e.getMessage(), e);
		}
		
		return apiRes;
	}
	
	/**
     * config 파일 읽어서 변수에 세팅
     */
    public void configSetting() {
    	try{
            //프로퍼티 파일 위치
    		//운영
            //String propFile = "/home/kwon/KWON_APP/conf/cashReceipt.properties"; 
    		//테스트
    		//String propFile = "/home/KWON/KWON_APP/conf/cashReceipt.properties"";
    		//로컬
    		String propFile = "C:/01/KWON_APP/conf/cashReceipt.properties";
    		 
            // 프로퍼티 객체 생성
            Properties props = new Properties();
            
            // 프로퍼티 파일 스트림에 담기
            FileInputStream fis = new FileInputStream(propFile);

            // 프로퍼티 파일 로딩
            props.load(new java.io.BufferedInputStream(fis));
            
            // 항목 읽기
            type = props.getProperty("type");
            mid = props.getProperty("mid");
            ApiURL = props.getProperty(type + "_ApiURL");
            
            logger.info("type : [{}], mid : [{}], ApiURL : [{}]", type, mid, ApiURL);
        }catch(Exception e){
        	logger.info(e.getMessage(), e);
        }
    }
}
