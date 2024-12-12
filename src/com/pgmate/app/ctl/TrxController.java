package com.pgmate.app.ctl;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pgmate.app.dao.*;
import com.pgmate.app.hook.RiskChangeHook;
import com.pgmate.app.util.*;
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
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.pgmate.app.interceptor.SessionExclude;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.session.CPSession;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.cipher.SeedKisa;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.Card;

@Controller
public class TrxController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.TrxController.class );

	@RequestMapping(value = "/trx/pay/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView payList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("AMOUNT_SUM", new TrxPayDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

		TrxPayDAO trxPayDAO = new TrxPayDAO();
		RecordSet rset = trxPayDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxPayDAO).setView(request,"/trx/pay/list","");
	}

	@RequestMapping(value = "/trx/pay/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView payView(HttpServletRequest request, @PathVariable String trxId) {
		request.setAttribute("DATAMAP", new TrxPayDAO().getByTrxId(trxId).getRow(0));
        return new ModelAndView("/trx/pay/modal");
    }

	@RequestMapping(value = "/trx/err/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView errList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		TrxErrDAO trxErrDAO = new TrxErrDAO();
		RecordSet rset = trxErrDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxErrDAO).setView(request,"/trx/err/list","");
	}
	
	@RequestMapping(value = "/trx/err/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView errView(HttpServletRequest request, @PathVariable String trxId) {
		request.setAttribute("DATAMAP", new TrxErrDAO().getByTrxId(trxId).getRow(0));
        return new ModelAndView("/trx/err/modal");
    }
	

	@RequestMapping(value = "/trx/cap/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView capList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);

		cpRequest.replaceKeyName("amount", "abs(amount)");

		TrxCapDAO trxCapDAO1 = new TrxCapDAO();
		trxCapDAO1.setDebug(true);
		trxCapDAO1.addWhere("IFNULL(serviceType, '') != '월세앱'");
		request.setAttribute("AMOUNT_SUM", trxCapDAO1.trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

		TrxCapDAO trxCapDAO = new TrxCapDAO();
//		cpRequest.setData("capId", "", "", "desc", false);
		trxCapDAO.addWhere("IFNULL(serviceType, '') != '월세앱'");
		RecordSet rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/trx/cap/list","");
	}



	@RequestMapping(value = "/trx/cap/view/{capId}", method = RequestMethod.GET)
    public ModelAndView capView(HttpServletRequest request, @PathVariable String capId) {
		SharedMap<String, Object> res =  new TrxCapDAO().getByCapId(capId).getRow(0);
		
		// 다날 영수증 조회용 param
		if("DANAL".equals(res.getString("van"))){
			res.put("danalParam", Base64.encodeString(res.getString("vanID")+"|"+res.getString("vanTrxId")+"|"+res.getString("amount")));
		}

		// 올앳 영수증 조회용 거래번호
		if(res.startsWith("van", "ALLAT")){
			AllatUtil allatUtil = new AllatUtil();
			if(res.getString("rfdType").equals("부분")) {
				res = new TrxCapDAO().getByCapId2(res.getString("rootTrxId")).getRow(0);
			}
			if(res.getString("capType").equals("매입")){
				RecordSet rset = new TrxReqDAO().getTrxId(res.getString("trxId"));
				if(rset.size() > 0) {
					SharedMap<String, Object> reqMap =rset.getRowFirst();
					if(reqMap.isEquals("trxType", "WHTR")){
						res.put("allatParam", allatUtil.getParam(res.getString("vanId"), res.getString("trackId"),res.getString("amount")));
					} else {
						res.put("allatParam", allatUtil.getParam(res.getString("vanId"),res.getString("trxId"),res.getString("amount")));
					}
				}else {
					res.put("allatParam",allatUtil.getParam(res.getString("vanId"),res.getString("trackId"),res.getString("amount")));
				}
			}else{
				SharedMap<String, Object> reqMap = new TrxReqDAO().getTrxOrgReq(capId);
				if(reqMap != null) {
					if(reqMap.getString("trxType").equals("WHTR")){
						res.put("allatParam",allatUtil.getParam(res.getString("vanId"),reqMap.getString("trackId"),res.getString("amount")));
					} else {
						res.put("allatParam",allatUtil.getParam(res.getString("vanId"),reqMap.getString("trxId"),res.getString("amount")));
					}
				}else {
					SharedMap<String, Object> orgMap = new TrxCapDAO().getByCapId2(res.getString("rootTrxId")).getRowFirst();
					res.put("allatParam",allatUtil.getParam(res.getString("vanId"),orgMap.getString("trackId"),res.getString("amount")));
				}
			}
		}
		
		
		// 웰컴 영수증 조회용 param
		if(res.startsWith("van", "WELCOMESUB")){
			SharedMap<String, Object> van  = new TrxCapDAO().getByVanId(res.getString("vanId")).getRow(0);

			try {
				if(res.getString("capType").equals("매입")){
					res.put("hash_value", EncryptUtil.sha256(res.getString("vanTrxId") + van.getString("cryptoKey")));
				} else {
					SharedMap<String, Object> orgMap = new TrxCapDAO().getByCapId2(res.getString("rootTrxId")).getRowFirst();
					res.put("hash_value", EncryptUtil.sha256(orgMap.getString("vanTrxId") + van.getString("cryptoKey")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			//온라인거래일때만, 거래정보 조회해서 영수증 ID 받기
			if(van.getString("vanId").equals("welcome306")) {
				String search_uri = "https://payapi.welcomepayments.co.kr/api/search/order";

				String mid = van.getString("vanId");
				String order_no = "";
				if(res.getString("capType").equals("매입")) {
					order_no = res.getString("trxId");
				} else {
					SharedMap<String, Object> orgMap = new TrxCapDAO().getByCapId2(res.getString("rootTrxId")).getRowFirst();
					order_no = orgMap.getString("trxId");
				}

				String api_key = "59a30bd3e66d9a87c71b5d39e26ed42a";

				SharedMap<String, Object> reqMap = new SharedMap<>();
				reqMap.put("mid", mid);
				reqMap.put("order_no", order_no);

				String hash_value = "";
				try {
					hash_value = EncryptUtil.sha256(mid + order_no + api_key);
					reqMap.put("hash_value", hash_value);
				} catch (Exception e) {
					logger.error("WELCOME 영수증 조회 오류 : {}", e.getMessage());
				}

				SharedMap<String, Object> responseMap = new SharedMap<>();

				//통신
				responseMap = searchRequest(search_uri, reqMap.toJson());

				//결과값 처리
				if(responseMap.getString("result_code").equals("0000")) {
					String transaction_no = responseMap.getString("transaction_no");
					res.put("vanTrxId", transaction_no);

					try {
						res.put("hash_value", EncryptUtil.sha256(res.getString("vanTrxId") + api_key));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		
		//PYS : 갤럭시아 영수증 조회용
		if(res.startsWith("van", "GALAXIA")) {
			res.put("GalaxiaMID", res.getString("vanId"));
		}
		
		request.setAttribute("DATAMAP", res);
		request.setAttribute("DATAREFMAP", new TrxCapDAO().getByRootTrxId(res.getString("trxId")).getRow(0));

		request.setAttribute("DATATMNMAP", new MchtTmnDAO().getById(res.getString("tmnId")).getRow(0));
		
		request.setAttribute("IQR_MAP", new TrxIqrDAO().getByCapId(capId).getRows());
        return new ModelAndView("/trx/cap/modal");
    }

	private SharedMap<String, Object> searchRequest(String uri, String json) {
		String response = sendReq(json, uri);
		SharedMap<String, Object>  resultMap = getSearchValue(response);
		return resultMap;
	}

	private String sendReq(String sendMsg, String uri) {
		String result= "";

		String inputLine = null;
		StringBuffer outResult = new StringBuffer();

		try {
			logger.debug("START");
			URL u = new URL(uri);
			HttpsURLConnection huc = (HttpsURLConnection)u.openConnection();
			huc.setDoOutput(true);
			huc.setRequestMethod("POST");
			huc.setDoInput(true);
			huc.setRequestProperty("Content-Type", "application/json");
			huc.setRequestProperty("Accept", "*/*");
			huc.setRequestProperty("Accept-Charset", "UTF-8");

			logger.debug("send : [{}]", sendMsg);
			OutputStream os = huc.getOutputStream();
			os.write(sendMsg.getBytes("UTF-8"));

			os.flush();
			os.close();
			int responseCode = huc.getResponseCode();

			InputStream is = null;

			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, null);
			// No validation for now
			huc.setSSLSocketFactory(context.getSocketFactory());
			// Connect to host
			huc.connect();
			huc.setInstanceFollowRedirects(true);
			// Print response from host
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				// 정상 호출 200
				is = huc.getInputStream();
			} else {
				// 에러 발생
				is = huc.getErrorStream();
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));

//	    	String line;
			while((inputLine = rd.readLine()) != null ) {
				outResult.append(inputLine);
			}
			result = outResult.toString();
			rd.close();
			huc.disconnect();

			logger.debug("result : [{}]",result);
			logger.debug("END");

		} catch (UnknownHostException uhe) {
			result = "{\"result_code\":\"E999\",\"result_message\":\"Exception:"+uhe.getMessage()+"\"}";
			return result;
		} catch (IOException ioe) {
			result = "{\"result_code\":\"E999\",\"result_message\":\"Exception:"+ioe.getMessage()+"\"}";
			return result;
		} catch (Exception e) {
			result = "{\"result_code\":\"E999\",\"result_message\":\"Exception:"+e.getMessage()+"\"}";
			return result;
		}
		return result;
	}

	private SharedMap<String, Object> getSearchValue(String json) {
		SharedMap<String, Object> response = new SharedMap<>();

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

		response.put("result_code", jsonObject.get("result_code").getAsString());
		response.put("result_message", jsonObject.get("result_message").getAsString());

		if(response.getString("result_code").equals("0000")) {
			//성공
			response.put("transaction_no", jsonObject.get("transaction_no").getAsString());
			response.put("mid", jsonObject.get("mid").getAsString());
			response.put("pay_type", jsonObject.get("pay_type").getAsString());
			response.put("transaction_status", jsonObject.get("transaction_status").getAsString());
			response.put("order_no", jsonObject.get("order_no").getAsString());
			response.put("approval_no", jsonObject.get("approval_no").getAsString());
			response.put("approval_ymdhms", jsonObject.get("approval_ymdhms").getAsString());
			response.put("cancel_ymdhms", jsonObject.get("cancel_ymdhms").getAsString());
			response.put("amount", jsonObject.get("amount").getAsString());
			response.put("cancel_amount", jsonObject.get("cancel_amount").getAsString());
			response.put("remain_amount", jsonObject.get("remain_amount").getAsString());
			response.put("user_id", jsonObject.get("user_id").getAsString());
			response.put("user_name", jsonObject.get("user_name").getAsString());
			response.put("recipient_name", jsonObject.get("recipient_name").getAsString());
			response.put("recipient_address", jsonObject.get("recipient_address").getAsString());
			response.put("product_code", jsonObject.get("product_code").getAsString());
			response.put("product_name", jsonObject.get("product_name").getAsString());
			response.put("echo", jsonObject.get("echo").getAsString());
			response.put("fail_code", jsonObject.get("fail_code").getAsString());
			response.put("fail_message", jsonObject.get("fail_message").getAsString());
		}

		return response;
	}

	@RequestMapping(value = "/trx/io/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView ioList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		TrxIODAO trxIODAO = new TrxIODAO();
		RecordSet rset = trxIODAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxIODAO).setView(request,"/trx/io/list","");
	}

	@RequestMapping(value = "/trx/io/view/{trxId}", method = RequestMethod.GET)
  public ModelAndView ioView(HttpServletRequest request, @PathVariable String trxId) {
		SharedMap<String, Object> map = new TrxIODAO().getByTrxId(trxId).getRow(0);
		String regData = map.getString("regData");
		regData = regData.replaceAll("&#39;", "'");
		regData = regData.replaceAll("&#34;", "\"");
		Type type = new TypeToken<SharedMap<String, Object>>(){}.getType();
		SharedMap<String, Object> myMap = new Gson().fromJson(regData, type);
		if (myMap.get("pay") != null) {
			LinkedTreeMap<String, Object> pay = (LinkedTreeMap<String, Object>) myMap.get("pay");
			LinkedTreeMap<String, Object> card = (LinkedTreeMap<String, Object>) pay.get("card");
			String number = (String)card.get("number");
			if(!CommonUtil.isNullOrSpace(number)) {
				card.put("number", number.substring(0, 6) + "******" + (number.length() > 12 ? number.substring(12) : ""));
			}
			pay.put("card", card);
			myMap.put("pay", pay);
			map.put("regData", myMap.toJson());
		}
		request.setAttribute("DATAMAP", map);
		return new ModelAndView("/trx/io/modal");
  }

	@RequestMapping(value = "/trx/wh/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView whList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		cpRequest.replaceKeyValue("startDate", cpRequest.getKeyValue("startDate")+"000000");
		cpRequest.replaceKeyValue("endDate", cpRequest.getKeyValue("endDate")+"235959");
		cpRequest.replaceKeyName("startDate", "regDate");
		cpRequest.replaceKeyName("endDate", "regDate");

		TrxWHFailDAO trxWHDAO = new TrxWHFailDAO();
		RecordSet rset = trxWHDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxWHDAO).setView(request,"/trx/wh/list","");
	}

	@RequestMapping(value = "/trx/wh/view/{vanTrxId}", method = RequestMethod.GET)
    public ModelAndView whView(HttpServletRequest request, @PathVariable String vanTrxId) {
		request.setAttribute("DATAMAP", new TrxWHDAO().getByVanTrxId(vanTrxId).getRow(0));
        return new ModelAndView("/trx/wh/modal");
    }
	
    @RequestMapping(value = "/trx/cap/cancel/{trxId}", method = RequestMethod.GET)
    public @ResponseBody String cancelCap(HttpServletRequest request, @PathVariable String trxId) {
    	CPSession session = SessionUtil.get(request);
    	return new RefundUtil().execute(new TrxPayDAO().getByTrxId(trxId).getRow(0), SessionUtil.getUserId(request), session.getGrade());
    }
    
    @RequestMapping(value = "/trx/cap/cancel2/{trxId}/{amount}", method = RequestMethod.GET)
    public @ResponseBody String cancelCap2(HttpServletRequest request, @PathVariable String trxId, @PathVariable String amount) {
    	CPSession session = SessionUtil.get(request);
     	return new RefundUtil().execute(new TrxPayDAO().getByTrxId(trxId).getRow(0), amount,SessionUtil.getUserId(request), session.getGrade());
    }
    
    @RequestMapping(value = "/trx/wh/retry/{trxId}", method = RequestMethod.GET)
    public @ResponseBody String whForm(HttpServletRequest request, @PathVariable String trxId) {
		CPDAO dao = new CPDAO();
		dao.setTable("PG_TRX_WH");
		dao.setColumns("*");
		dao.addWhere("trxId", trxId, CPDAO.eq);
		dao.setGroupBy("vanTrxId");
		dao.setOrderBy("idx ASC");
		SharedMap<String, Object> resMap = dao.search().getRow(0);

		StringBuffer url = new StringBuffer();
		url.append(request.getServerName());
		if(resMap.getString("van").startsWith("FIRSTPAY")) {
			return new FirstPayUtil().trxRetry(resMap.getString("orgData"),trxId);
		}

        return DanalUtil.trxRetry(url.toString(), resMap.getString("orgData"), resMap.getString("van"),resMap.getString("vanId"));
    }

    @RequestMapping(value = "/trx/wh/retry", method = RequestMethod.GET)
    public @ResponseBody String whAll(HttpServletRequest request) {
		CPDAO dao = new CPDAO();
		List<SharedMap<String, Object>> resMap = dao.query("SELECT * FROM PG_TRX_WH WHERE SUBSTR(vanTrxId,1,8) > '20170305' GROUP BY vanTrxId ORDER BY vanTrxId ASC " ).getRows();
		//vanTrxId NOT IN (SELECT vanTrxId FROM PG_TRX_PAY) AND vanTrxId NOT IN (SELECT vanTrxId FROM PG_TRX_RFD) 
		StringBuffer url = new StringBuffer();
		url.append(request.getServerName());
		int i = 1;
		for(SharedMap<String, Object> each : resMap) {	
			//DanalUtil.trxRetry(url.toString(), each.getString("orgData"), each.getString("vanId"));
			logger.info("Cnt : {}", i++);
		}

		return CommonUtil.toString(i);
    }

    @RequestMapping(value = "/trx/aggregator/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView aggregatorList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		TrxCapSubDAO trxCapSubDAO = new TrxCapSubDAO();
		cpRequest.setData("capId", "", "", "desc", false);
		RecordSet rset = trxCapSubDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxCapSubDAO).setView(request,"/trx/aggregator/list","");
	}

	@RequestMapping(value = "/trx/aggregator/view/{capId}", method = RequestMethod.GET)
    public ModelAndView aggregatorView(HttpServletRequest request, @PathVariable String capId) {
		SharedMap<String, Object> res =  new TrxCapSubDAO().getByCapId(capId).getRow(0);
		request.setAttribute("DATAMAP", res);
		request.setAttribute("DATAREFMAP", new TrxCapSubDAO().getByRootTrxId(res.getString("trxId")).getRow(0));
        return new ModelAndView("/trx/aggregator/modal");
    }

	@RequestMapping(value = "/trx/rfd/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView refund(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("AMOUNT_SUM", new TrxRfdDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

		TrxRfdDAO trxPayDAO = new TrxRfdDAO();
		RecordSet rset = trxPayDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxPayDAO).setView(request,"/trx/rfd/list","");
	}

	@RequestMapping(value = "/trx/rfd/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView refundView(HttpServletRequest request, @PathVariable String trxId) {
		SharedMap<String, Object> res =  new TrxRfdDAO().getByTrxId(trxId).getRow(0);
        return new ModelAndView("/trx/rfd/modal", "DATAMAP", res);
    }

	@RequestMapping(value = "/trx/admin_cancel/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView adminCancel(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("AMOUNT_SUM", new TrxAdminCancelDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

		TrxAdminCancelDAO trxPayDAO = new TrxAdminCancelDAO();
		RecordSet rset = trxPayDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxPayDAO).setView(request,"/trx/admin_cancel/list","");
	}

	@RequestMapping(value = "/trx/today", method = RequestMethod.GET)
    public @ResponseBody SharedMap<String, Object> today(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("DATE_FORMAT(NOW(),'%Y%m%d') as today,");
		sb.append("FORMAT(SUM(IF(status = '승인' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS dayPay,");
		sb.append("FORMAT(SUM(IF(status = '승인' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS dayPayCnt,");
		sb.append("FORMAT(SUM(IF(status = '승인취소' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS dayRef,");
		sb.append("FORMAT(SUM(IF(status = '승인취소' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS dayRefCnt,");
		sb.append("FORMAT(SUM(IF(status = '승인' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS monthPay,");
		sb.append("FORMAT(SUM(IF(status = '승인' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS monthPayCnt,");
		sb.append("FORMAT(SUM(IF(status = '승인취소' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS monthRef,");
		sb.append("FORMAT(SUM(IF(status = '승인취소' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS monthRefCnt");
		CPDAO dao = new CPDAO();
		dao.setTable("PG_TRX_PAY");
		dao.setColumns(sb.toString());
		
        return dao.search().getRowFirst();
    }
	
	// RISK
	@RequestMapping(value = "/trx/risk/status", method = RequestMethod.POST)
    public @ResponseBody String whForm(HttpServletRequest request,
    		@RequestParam(value="idx") String idx,
    		@RequestParam(value="status") String status) {
		if(new DAO().update("UPDATE PG_TRX_RISK SET status='"+status+"' WHERE idx='"+idx+"'")) {
			return "OK";
		} else {
			return "NOK||파일 상태변경에 실패했습니다.";
		}
    }

	@RequestMapping(value = "/trx/risk/check/{trxId}", method = RequestMethod.GET)
	public @ResponseBody String riskCheck(HttpServletRequest request, @PathVariable String trxId) {
		RiskUtil risk = new RiskUtil(trxId);
		risk.start();
		return "OK";
	}

	// 카드번호 수기입력
	@RequestMapping(value = "/trx/card/scan", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<SharedMap<String, Object>> cardScan(HttpServletRequest request, @RequestBody List<SharedMap<String, String>> resMapList) {
		SharedMap<String, String> last4Map = new SharedMap<>();
		TrxPayDAO payDAO = new TrxPayDAO();
		StringBuffer sb = new StringBuffer();
		for(SharedMap<String,String> each : resMapList) {
			sb.append("'"+each.getString("tid")+ "',");
			last4Map.put(each.getString("tid"), each.getString("last4"));
		}
		String tidStr = sb.toString();
		tidStr = tidStr.substring(0, tidStr.length()-1);
		RecordSet rset = (RecordSet)new TrxPayDAO().isUnknownCardNoTrx(tidStr);
		List<SharedMap<String,Object>> trxMap = rset.getRows();
		if(trxMap != null && trxMap.size() > 0) {
			for(SharedMap<String, Object> each: trxMap) {
				each.put("last4", last4Map.getString(each.getString("vanTrxId")));
			}
		}
		return trxMap;
    }
	@RequestMapping(value = "/trx/card/update", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SharedMap<String, Object> cardUpdate(HttpServletRequest request, @RequestBody List<SharedMap<String, Object>> resMapList) {
		SharedMap<String, Object> result = new SharedMap<>();
		result.put("result", "OK");
		if(resMapList != null && resMapList.size() > 0) {
			for(SharedMap<String, Object> each : resMapList) {
				if(each.getString("last4").length() != 4 || CommonUtil.isNullOrSpace(each.getString("trxId"))) {
					result.put("result", "NOK");
				} else {
					if(!new CPDAO().update("UPDATE PG_TRX_CAP SET last4='" + each.getString("last4") + "' WHERE last4='' AND trxId='" + each.getString("trxId") + "'")) {
						logger.debug("PG_TRX_CAP UPDATE FAIL: {}", each.getString("trxId"));
						result.put("result", "NOK");
					}
					if(!new CPDAO().update("UPDATE PG_TRX_PAY SET last4='" + each.getString("last4") + "' WHERE last4='' AND trxId='" + each.getString("trxId") + "'")) {
						logger.debug("PG_TRX_PAY UPDATE FAIL: {}", each.getString("trxId"));
						result.put("result", "NOK");
					}
				}
			}
		} else {
			result.put("result", "NOK");
		}

		return result;
    }

	@RequestMapping(value = "/trx/status/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView statusList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);

		TrxCapDAO trxCapDAO1 = new TrxCapDAO();
		trxCapDAO1.setDebug(true);
		trxCapDAO1.addWhere("risk != '' AND capType='매입'");
		trxCapDAO1.addWhere("IFNULL(serviceType, '') != '월세앱'");
		request.setAttribute("AMOUNT_SUM", trxCapDAO1.trxSum(cpRequest.data,null).getRowFirst().getString("amount"));
		
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		trxCapDAO.setDebug(true);
		trxCapDAO.addWhere("risk != '' AND capType='매입'");
		trxCapDAO.addWhere("IFNULL(serviceType, '') != '월세앱'");
		RecordSet rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/trx/status/list","");
	}

	@RequestMapping(value = "/trx/cap/status/{capId}", method = RequestMethod.GET)
    public ModelAndView capStlStatus(HttpServletRequest request, @PathVariable String capId) {
		SharedMap<String, Object> res =  new TrxCapDAO().getByCapId(capId).getRow(0);
		request.setAttribute("DATAMAP", res);
        return new ModelAndView("/trx/cap/stlModal");
    }

	@RequestMapping(value = "/trx/cap/risk", method = RequestMethod.POST)
	public @ResponseBody String capChangeStlStatus(HttpServletRequest request) {
		String risk 	= CommonUtil.nToB(request.getParameter("risk"));
		String capArray = CommonUtil.nToB(request.getParameter("capId"));
		String summary 	= CommonUtil.nToB(request.getParameter("summary"));
		String result = "NOK:선택된 거래가 없습니다.";

		logger.debug("capId: [{}] , risk: [{}], {}", capArray, risk, summary);

		TrxIqrDAO iqrDAO = new TrxIqrDAO();
		TrxDAO trxDAO = new TrxDAO();
		MchtDAO mchtDAO = new MchtDAO();
		ChargeSettleDAO chargeSettleDAO = new ChargeSettleDAO();

		if(CommonUtil.isNullOrSpace(capArray)) {
			return result;
		}

		String[] capIdArray = CommonUtil.split(capArray, ",",true);

		ControllerUtil util = new ControllerUtil();
		
		int success = 0;
		int failure  = 0;

		StringBuilder rowResult = new StringBuilder();
		if(CommonUtil.isNullOrSpace(risk)){	//리스크 해지
			for(String capId : capIdArray) {
				capId = capId.replaceAll("'", "");
				String t = util.setRiskToNormal(capId);
				if(t.startsWith("OK")){
					// 23.11.02 월세앱 리스크 해제 노티 전송 추가
					// 월세앱 리스크 코드 주석처리
					/*SharedMap<String, Object> capMap = trxDAO.getRentCapById(capId);
					if(capMap != null) {
						logger.info("월세앱 거래건 있음");
						// 예약이체 insert
						boolean isExistRfdTrx = trxDAO.isExistsRfdTrx(capId);
						if(!isExistRfdTrx) {
							if (!"분납".equals(capMap.getString("billingMethod"))) {
								logger.info("충전정산 대상");
								SharedMap<String, Object> mchtTaxMap = mchtDAO.getMchtTaxByTaxId(capMap.getString("taxId"));
								SharedMap<String, Object> chargeSettleFirmMap = createChargeSettleFirmMap(capMap, trxDAO.getSender(capMap.getString("mchtId")), mchtTaxMap);
								chargeSettleDAO.insertChargeSettleFirm(chargeSettleFirmMap);
							}
						}
						// 노티전송
						String hookAddr = trxDAO.getHookAddr(capMap.getString("mchtId"));
						if (!CommonUtil.isNullOrSpace(hookAddr)) {
							String payLoad = setPayLoad(capMap, risk, "완료", "0000", "정상처리");
							capMap.put("payLoad", payLoad);
							new RiskChangeHook(hookAddr, capMap, "0").start();
						}
					}*/
					iqrDAO.insertRisk(capId, t.replaceAll("OK:", "")+" ,"+summary, SessionUtil.getUserId(request));
					success++;
				}else{
					failure++;
				}
				rowResult.append(t.replaceAll("NOK:", "").replaceAll("OK:", "")).append("<br/>");


			}
		//리스크 설정
		}else{
			for(String capId : capIdArray) {
				capId = capId.replaceAll("'", "");
				String t = util.setCaptureToRisk(capId,risk);
				if(t.startsWith("OK")){
					SharedMap<String, Object> capMap = trxDAO.getRentCapById(capId);
					if(capMap != null) {
						chargeSettleDAO.deleteStlFirmReserve(capMap);
						String hookAddr = trxDAO.getHookAddr(capMap.getString("mchtId"));
						if (!CommonUtil.isNullOrSpace(hookAddr)) {
							String payLoad = setPayLoad(capMap, risk, "완료", "0000", "정상처리");
							capMap.put("payLoad", payLoad);
							new RiskChangeHook(hookAddr, capMap, "0").start();
						}
					}
					iqrDAO.insertRisk(capId, t.replaceAll("OK:", "")+" ,"+summary, SessionUtil.getUserId(request));
					success++;
				}else{
					failure++;
				}
				rowResult.append(t.replaceAll("NOK:", "").replaceAll("OK:", "")).append("<br/>");

			}
		}

		rowResult.insert(0, "OK:성공건수 :"+success+", 실패건수 :"+failure+"<br/>");

		/*
		if(updateResult) {
			/*StringBuilder sb = new StringBuilder();
			sb.append(" INSERT INTO HT_TRX_CAP_DTL_RISK (`capId`,`stlStatus`,`stlAmount`,`stlRate`,`stlFee`,`stlFeeVat`,`stlType`,`stlDay`,`payOutDay`,`stlId`,`stlDistFee`,`stlDistRate`,`stlDistDay`,`stlDistId`,`stlAgencyFee`,`stlAgencyRate`,`stlAgencyDay`,`stlAgencyId`,`stlSalesFee`,`stlSalesRate`,`stlSalesDay`,`stlSalesId`,`van`,`vanId`,`vanTrxId`,`vanStatus`,`stlVanFee`,`stlVanRate`,`stlVanDay`,`benefit`,`taxId`,`regId`,`regDay`,`summary`,`risk`)");
			sb.append(" SELECT `capId`,`stlStatus`,`stlAmount`,`stlRate`,`stlFee`,`stlFeeVat`,`stlType`,`stlDay`,`payOutDay`,`stlId`,`stlDistFee`,`stlDistRate`,`stlDistDay`,`stlDistId`,`stlAgencyFee`,`stlAgencyRate`,`stlAgencyDay`,`stlAgencyId`,`stlSalesFee`,`stlSalesRate`,`stlSalesDay`,`stlSalesId`,`van`,`vanId`,`vanTrxId`,`vanStatus`,`stlVanFee`,`stlVanRate`,`stlVanDay`,`benefit`,`taxId`, '"+ SessionUtil.getUserId(request) +"','"+ CommonUtil.getCurrentDate("yyyyMMdd")+ "', '"+summary+"',`risk` "); 
			sb.append(" FROM PG_TRX_CAP_DTL "); 
			sb.append(" WHERE capId IN ("+capId+")");

			if(new DAO().insert(sb.toString())) {
				result = "OK";
			}*/
			//result = "OK";

		return rowResult.toString();
	}

	private SharedMap<String,Object> createChargeSettleFirmMap(SharedMap<String,Object> trxCapMap, String sender, SharedMap<String,Object> mchtTaxMap) {
		MchtDAO mchtDAO = new MchtDAO();
		SharedMap<String,Object> chargeSettleFirmMap = new SharedMap<String,Object>();
		String regDate = CommonUtil.getCurrentDate("yyyyMMddHHmmss");
		String pubTime = "004000";

		chargeSettleFirmMap.put("trxId"		, trxCapMap.getString("trxId"));
		chargeSettleFirmMap.put("transferType"	, "예약");
		chargeSettleFirmMap.put("mchtId"	, trxCapMap.getString("mchtId"));
		chargeSettleFirmMap.put("trackId"	, trxCapMap.getString("trackId"));
		chargeSettleFirmMap.put("pubDay"	, trxCapMap.getString("transferDay"));
		chargeSettleFirmMap.put("pubTime"	, pubTime);
		chargeSettleFirmMap.put("status"	, "대기");
		chargeSettleFirmMap.put("retry"		, 0);
		chargeSettleFirmMap.put("trxDay"	, regDate.substring(0, 8));
		chargeSettleFirmMap.put("trxTime"	, regDate.substring(8));
		chargeSettleFirmMap.put("amount"	, Math.abs(trxCapMap.getLong("amount")));
		chargeSettleFirmMap.put("fee"		, Math.abs(trxCapMap.getLong("stlFee")));
		chargeSettleFirmMap.put("feeVat"	, Math.abs(trxCapMap.getLong("stlFeeVat")));
		chargeSettleFirmMap.put("bankFee"	, 0);
		chargeSettleFirmMap.put("netAmount"	, Math.abs(trxCapMap.getLong("stlAmount")));
		chargeSettleFirmMap.put("balance"	, mchtDAO.getMchtBalance(trxCapMap.getString("mchtId")).getLong("balance")+Math.abs(trxCapMap.getLong("stlAmount")));
		chargeSettleFirmMap.put("resultCd"	, "");
		chargeSettleFirmMap.put("resultMsg"	, "");
		chargeSettleFirmMap.put("refId"		, trxCapMap.getString("capId"));
		chargeSettleFirmMap.put("rootTrxId"	, "");
		chargeSettleFirmMap.put("account"	, mchtTaxMap.getString("account"));
		chargeSettleFirmMap.put("bankCd"	, mchtTaxMap.getString("bankCd"));
		chargeSettleFirmMap.put("bankName"	, mchtTaxMap.getString("bankName"));
		chargeSettleFirmMap.put("holder"	, mchtTaxMap.getString("accntHolder"));
		chargeSettleFirmMap.put("recordInfo", sender);
		chargeSettleFirmMap.put("regId"		, trxCapMap.getString("mchtId"));
		chargeSettleFirmMap.put("regDay"	, regDate.substring(0, 8));

		return chargeSettleFirmMap;
	}

	@RequestMapping(value = "/trx/cap/iqr/{capId}", method = RequestMethod.POST)
	public @ResponseBody String capChangeStlStatus(HttpServletRequest request, @PathVariable String capId) {

		String summary = CommonUtil.nToB(request.getParameter("summary"));
		String telNo = CommonUtil.nToB(request.getParameter("telNo"));
		
		TrxIqrDAO iqrDAO = new TrxIqrDAO();

		String xss = SQLInjectionUtil.xssChange(summary);
		telNo = SQLInjectionUtil.xssChange(telNo);

		if(iqrDAO.insertNormal(capId, summary, telNo, SessionUtil.getUserId(request))){
			return "OK:"+xss;
		}else{
			return "NOK";
		}

	}

	@RequestMapping(value = "/trx/capdel/form", method = RequestMethod.GET)
	public ModelAndView capDelForm(HttpServletRequest request) {
		request.setAttribute("TODAY", CommonUtil.getCurrentDate("yyyyMMdd"));

		return new ModelAndView("/trx/capdel/form");
	}


	@RequestMapping(value = "/trx/capdel/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView capDelList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);

		TrxCapDAO trxCapDAO1 = new TrxCapDAO();
		trxCapDAO1.setDebug(true);
		trxCapDAO1.addWhere("IFNULL(serviceType, '') != '월세앱'");
		request.setAttribute("AMOUNT_SUM", trxCapDAO1.trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

		TrxCapDAO trxCapDAO = new TrxCapDAO();
		cpRequest.setData("capId", "", "", "desc", false);
		trxCapDAO.addWhere("IFNULL(serviceType, '') != '월세앱'");
		RecordSet rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/trx/capdel/list","");
	}

	@RequestMapping(value = "/trx/capdel/action", method = RequestMethod.POST)
	public @ResponseBody String capdelAction(HttpServletRequest request) {
		String capArray = CommonUtil.nToB(request.getParameter("capId"));
		String summary 	= CommonUtil.nToB(request.getParameter("summary"));
		String result = "NOK:선택된 거래가 없습니다.";

		logger.debug("DELETE capId: [{}] , {},{}", capArray, summary,SessionUtil.getUserId(request));


		if(CommonUtil.isNullOrSpace(capArray)) {
			return result;
		}

		if(new TrxDAO().deleteTrxCap(capArray)){
			return "OK:데이터가 삭제되었습니다.";
		}else{
			return "NOK:데이터가 삭제되지 않았습니다. 관리자에 문의 바랍니다.";
		}

	}
	

	@RequestMapping(value = "/trx/cap/daychange/{capId}", method = RequestMethod.POST)
	public @ResponseBody String capDayChange(HttpServletRequest request, @PathVariable String capId) {

		String summary = CommonUtil.nToB(request.getParameter("daysummary"));
		String stlDay = CommonUtil.nToB(request.getParameter("stlDay")).trim();
		String oldStlDay = CommonUtil.nToB(request.getParameter("oldStlDay"));
		String stlVanDay = CommonUtil.nToB(request.getParameter("stlVanDay")).trim();
		String oldStlVanDay = CommonUtil.nToB(request.getParameter("oldStlVanDay"));

		DAO dao = new DAO();
		dao.setTable("PG_CODE_HOLIDAY");
		dao.addWhere("days in ('"+stlDay+"','"+stlVanDay+"')");
		dao.setOrderBy("days asc");
		RecordSet rset = dao.search();
		if(stlDay.equals(stlVanDay)){
			if(rset.size() != 1){
				return "날짜 포맷이 잘못되었거나 유효하지 않은 날짜입니다.";
			}
		}else{
			if(rset.size() != 2){
				return "날짜 포맷이 잘못되었거나 유효하지 않은 날짜입니다.";
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("가맹점 정산일 기존 : "+oldStlDay+" 변경 :"+stlDay);
		sb.append("<br/>카드사 정산일 기존 : "+oldStlVanDay+" 변경 :"+stlVanDay);
		sb.append("<br/>변경되었습니다.");
		sb.append("<br/>"+summary);



		new TrxIqrDAO().insertNormal(capId, sb.toString(), "", SessionUtil.getUserId(request));
		if(new TrxDAO().updateDay(capId, stlDay, stlVanDay)){
			return sb.toString();
		}else{
			return "변경 실패 ";
		}



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

	@RequestMapping(value = "/trx/cap/collect/temp/{stlVanDay}/{mchtId}/{vanId}", method = RequestMethod.GET)
    public ModelAndView dayMchtIdAndVanIdForm(HttpServletRequest request, @PathVariable String stlVanDay, @PathVariable String mchtId, @PathVariable String vanId) {
		request.setAttribute("SEARCH_STL_VAN_DAY", stlVanDay);
		request.setAttribute("SEARCH_VAN_ID", vanId);
		request.setAttribute("SEARCH_MCHT_ID", mchtId);
        return new ModelAndView("/trx/collect/form");
    }
	@RequestMapping(value = "/trx/cap/diff/temp/{stlDiffVanDay}/{mchtId}/{vanId}", method = RequestMethod.GET)
	public ModelAndView dayMchtIdAndVanIdDiffForm(HttpServletRequest request, @PathVariable String stlDiffVanDay, @PathVariable String mchtId, @PathVariable String vanId) {
		request.setAttribute("SEARCH_STL_DIFF_VAN_DAY", stlDiffVanDay);
		request.setAttribute("SEARCH_VAN_ID", vanId);
		request.setAttribute("SEARCH_MCHT_ID", mchtId);
		return new ModelAndView("/trx/diff/form");
	}

	@RequestMapping(value = "/trx/cap/collect/{stlVanDay}/{mchtId}", method = RequestMethod.GET)
    public ModelAndView dayMchtIdForm(HttpServletRequest request, @PathVariable String stlVanDay, @PathVariable String mchtId) {
		request.setAttribute("FIX_SEARCH", true);
		request.setAttribute("SEARCH_STL_VAN_DAY", stlVanDay);
		request.setAttribute("SEARCH_MCHT_ID", mchtId);
        return new ModelAndView("/trx/cap/form");
    }

	@RequestMapping(value = "/trx/cap/collect/maded/{collectId}/{mchtId}", method = RequestMethod.GET)
    public ModelAndView madedMchtIdForm(HttpServletRequest request, @PathVariable String collectId, @PathVariable String mchtId) {
		request.setAttribute("FIX_SEARCH", true);
		request.setAttribute("SEARCH_COLLECT_ID", collectId);
		request.setAttribute("SEARCH_MCHT_ID", mchtId);
        return new ModelAndView("/trx/collect/form");
    }

	@RequestMapping(value = "/trx/cap/diff/maded/{collectId}/{mchtId}", method = RequestMethod.GET)
	public ModelAndView madedDiffMchtIdForm(HttpServletRequest request, @PathVariable String collectId, @PathVariable String mchtId) {
		request.setAttribute("FIX_SEARCH", true);
		request.setAttribute("SEARCH_COLLECT_ID", collectId);
		request.setAttribute("SEARCH_MCHT_ID", mchtId);
		return new ModelAndView("/trx/diffMade/form");
	}

	@RequestMapping(value = "/system/iqr/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView iqrList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		cpRequest.setData("iqrType", "일반", "eq", "", true);
		TrxIqrDAO trxiqrDAO = new TrxIqrDAO();
		RecordSet rset = trxiqrDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxiqrDAO).setView(request,"/system/iqr/list","");
	}

	@RequestMapping(value = "/system/risk/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView riskList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		cpRequest.setData("iqrType", "리스크", "eq", "", true);
		TrxIqrDAO trxiqrDAO = new TrxIqrDAO();
		RecordSet rset = trxiqrDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxiqrDAO).setView(request,"/system/risk/list","");
	}

	@RequestMapping(value = "/trx/cap/binupdate/{capId}", method = RequestMethod.POST)
	public @ResponseBody String binUpdate(HttpServletRequest request, @PathVariable String capId) {

		String summary 	= CommonUtil.nToB(request.getParameter("cardsummary"));
		String bin 		= CommonUtil.nToB(request.getParameter("bin")).trim();
		String last4 	= CommonUtil.nToB(request.getParameter("last4"));


		Card card = new Card();
		card.cardId 	= GenKey.genKeys(CPKEY.CARD,capId);
		card.bin		= bin;
		card.last4		= last4;
		TrxDAO trxDAO = new TrxDAO();
		SharedMap<String,Object> rootMap = new TrxCapDAO().getByCapId(capId).getRowFirst();
		SharedMap<String,Object> rfdMap = new TrxCapDAO().getByRootCapId(capId).getRowFirst();


		SharedMap<String,Object> issuerMap = trxDAO.getDBIssuer(card.bin);
		if(issuerMap != null){
			card.cardType = issuerMap.getString("type") ;
			card.issuer = issuerMap.getString("issuer");
			card.acquirer = issuerMap.getString("acquirer");
		}
		String encrypted = Base64.encodeToString(SeedKisa.encrypt(GsonUtil.toJson(card), ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16)));
		trxDAO.insertCard(card.cardId,encrypted);


		new TrxIqrDAO().insertNormal(capId, "카드번호 등록 :"+bin +" :"+card.cardId +" :"+summary, "", SessionUtil.getUserId(request));
		if(trxDAO.updateBin("PG_TRX_CAP",capId, card)){
			trxDAO.updateBin("PG_TRX_PAY", rootMap.getString("trxId"), card);
			if(rfdMap != null && rfdMap.size() > 0){
				trxDAO.updateBin("PG_TRX_CAP",rfdMap.getString("capId"), card);
				trxDAO.updateBin("PG_TRX_PAY",rfdMap.getString("trxId"), card);
			}
			return "카드번호가 변경되었습니다.";
		}else{
			return "카드번호 변경 실패 "+trxDAO.getError();
		}



	}

	@RequestMapping(value = "/trx/cap/cardTypeUpdate/{capId}", method = RequestMethod.POST)
	public @ResponseBody String cardTypeUpdate(HttpServletRequest request, @PathVariable String capId) {

		String summary 	= CommonUtil.nToB(request.getParameter("cardTypeSummary"));
		String cardType	= CommonUtil.nToB(request.getParameter("cardType")).trim();
		String newCardType 	= CommonUtil.nToB(request.getParameter("newCardType")).trim();

		SharedMap<String,Object> rootMap = new TrxCapDAO().getByCapId(capId).getRowFirst();
		SharedMap<String,Object> mchtMap = new MchtDAO().getById(rootMap.getString("mchtId")).getRowFirst();
		if(cardType.equals(newCardType)){
			return "카드타입 변경 실패 (같은 유형의 카드 타입입니다.)";
		}else if(rootMap == null || rootMap.size() == 0){
			return "원거래를 찾을 수 없습니다.";
		}else if(mchtMap.getString("distId").equals("16") || rootMap.getString("vanId").equals("FACTORING")){
			return "선정산 대상 가맹점은 해당하지 않습니다.";
		}else if(!rootMap.getString("van").equals("DAOU")){
			return "다우 외에는 적용되지 않습니다.";
		}else{

			DAO dd = new DAO();
			dd.setTable("PG_ORG_FEE");
			dd.setColumns("*");
			dd.addWhere("van", rootMap.getString("van"), DAO.eq);
			SharedMap<String,Object> orgFeeMap = dd.search().getRowFirst();
			if(orgFeeMap == null){
				return "원가수수료를 찾을 수 없습니다.";
			}

			DAO dao = new DAO();
			double rate = 0;
			if(newCardType.equals("체크")){
				rate = orgFeeMap.getDouble("checkRate");
			}else{
				rate = orgFeeMap.getDouble("creditRate");
			}
			double fee = new ControllerUtil().calcRoundUpFeeVat(rootMap.getLong("amount"),rate);

			if(dao.update("UPDATE PG_TRX_CAP set cardType='"+newCardType+"' WHERE capId ='"+capId+"'")){
				dao.update("UPDATE PG_TRX_CAP_DTL set stlVanFee="+fee+", stlVanRate ="+rate+" WHERE capId ='"+capId+"'");

				if(rootMap.getLong("amount") < 0){
					dao.update("UPDATE PG_TRX_RFD set cardType='"+newCardType+"' WHERE trxId ='"+rootMap.getString("trxId")+"'");
				}else{
					dao.update("UPDATE PG_TRX_PAY set cardType='"+newCardType+"' WHERE trxId ='"+rootMap.getString("trxId")+"'");
				}
				new TrxIqrDAO().insertNormal(capId, "카드타입 변경 :"+cardType +" :"+newCardType +" :"+summary, "", SessionUtil.getUserId(request));
				return "카드타입및 수수료가 변경되었습니다.";
			}else{
				return "카드타입 및 수수료 변경 실패 관리자에 문의 바랍니다."+dao.getError();
			}

		}




	}

	@RequestMapping(value = "/trx/collect/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView collectCapList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset;

		for(Data data :  cpRequest.data) {
			logger.debug("CPRequest {}, {}", data.name, data.val);
		}

		if(cpRequest.getKeyData("collectId") != null) {
			request.setAttribute("AMOUNT_SUM", new TrxCapDAO().trxSumWithCollectId(cpRequest.data,null).getRowFirst().getString("amount"));
			rset = trxCapDAO.withCollectIdList(cpRequest.data,cpRequest.page);
		} else {
			request.setAttribute("AMOUNT_SUM", new TrxCapDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));
			rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
		}

		return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/trx/cap/list","");
	}

	@RequestMapping(value = "/trx/diff/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView diffCapList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset;

		for(Data data :  cpRequest.data) {
			logger.debug("CPRequest {}, {}", data.name, data.val);
		}

		if(cpRequest.getKeyData("collectId") != null) {
			request.setAttribute("AMOUNT_SUM", new TrxCapDAO().trxSumWithCollectIdDiff(cpRequest.data,null).getRowFirst().getString("amount"));
			rset = trxCapDAO.withCollectIdDiffList(cpRequest.data,cpRequest.page);
		} else {
			request.setAttribute("AMOUNT_SUM", new TrxCapDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));
			rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
		}

		return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/trx/cap/list","");
	}

	  @RequestMapping(value = "/trx/pisp/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	  public ModelAndView pispList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
	    SessionUtil.setSearchGrade(request, cpRequest);

		PispDAO pispDAO = new PispDAO();
		//KJM : 거래내역 리스트 가져옴
//		RecordSet rset = pispDAO.list(cpRequest.data, cpRequest.page);
		//230621 테이블 미존재로 빈값 넘겨주게 수정
		RecordSet rset = new RecordSet();
		//KJM : 가져온 데이터 view에 넘겨줌
		return new CPRUtil(cpRequest).dataList(rset, pispDAO).setView(request, "/trx/pisp/list", "");
	}

	//KJM : 거래관리 > 지급대행 관리 > 인증내역 조회 리스트
	@RequestMapping(value = "/trx/pisp/fcs/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView pispFcsList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);

		PispFcsDAO pispFcsDAO = new PispFcsDAO();
		//230621 테이블 미존재로 빈값 넘겨주게 수정
//		RecordSet rset = pispFcsDAO.list(cpRequest.data, cpRequest.page);
		RecordSet rset = new RecordSet();
		return new CPRUtil(cpRequest).dataList(rset, pispFcsDAO).setView(request, "/trx/pisp/fcs/list", "");
	}


	  // 영수증 조회 양식 추가
	  @RequestMapping(value = "/receiptForm", method = RequestMethod.GET)
	  @SessionExclude
	  public String receiptForm(){
		    return"/trx/cap/receiptForm";
		}

	  // 영수증 조회 추가
	  @SuppressWarnings("finally")
	  // 파라미터 값으로 영수증 조회(kspay, allat)
	  @RequestMapping(value = "/receipt", method = RequestMethod.GET)
	  @SessionExclude
	  public ModelAndView receipt(HttpServletRequest request,
			  @RequestParam(value="trxId", required=false, defaultValue="0") String trxId,
			  @RequestParam(value="mchtId", required=false, defaultValue="0") String mchtId,
			  @RequestParam(value="tmnId", required=false, defaultValue="0") String tmnId,
			  @RequestParam(value="trackId", required=false, defaultValue="0") String trackId,
			  @RequestParam(value="authCd", required=false, defaultValue="0") String authCd,
			  @RequestParam(value="amount", required=false, defaultValue="0") String amount,
			  @RequestParam(value="regDay", required=false, defaultValue="0") String regDay
	  ) {

//		  // 금액이 음수일 경우 양수로 바꿈
//		  if(!amount.equals("0") && amount.startsWith("-")) {
//			  amount = amount.substring(1);
//		  }

		  // trxId로만 영수증 조회
		  if(!trxId.equals("0")) {
			  // 금액이 음수일 경우 양수로 바꿈
			  if(!amount.equals("0") && amount.startsWith("-")) {
				  amount = amount.substring(1);
			  }

			  try {
				  SharedMap<String, Object> rfd = new TrxCapDAO().getByTrxIdRfd(trxId).getRow(0);
				  // 취소 원짱의 trxId가 입력될 경우 원거래 번호를 담는다.
				  if(rfd != null) {
					  trxId = rfd.getString("rootTrxId");
				  }

				  SharedMap<String, Object> res = new TrxCapDAO().getByTrxId2(trxId).getRow(0);

				  // 올앳 영수증 조회용 거래번호
				  if(res.startsWith("van", "ALLAT")){
					  AllatUtil allatUtil = new AllatUtil();

					  RecordSet rset = new TrxReqDAO().getTrxId(res.getString("trxId"));
					  if(rset.size() > 0) {
						  SharedMap<String, Object> reqMap =rset.getRowFirst();
						  if(reqMap.isEquals("trxType", "WHTR")){
							  res.put("allatParam", allatUtil.getParam(res.getString("vanId"), res.getString("trackId"),res.getString("amount")));
						  } else {
							  res.put("allatParam", allatUtil.getParam(res.getString("vanId"),res.getString("trxId"),res.getString("amount")));
						  }
					  }

				  }

				  //웰컴 서브 영수증 조회용
				  if(res.startsWith("van", "WELCOMESUB")){
					  SharedMap<String, Object> van  = new TrxCapDAO().getByVanId(res.getString("vanId")).getRow(0);

					  try {
						  res.put("hash_value", EncryptUtil.sha256(res.getString("vanTrxId") + van.getString("cryptoKey")));
					  } catch (Exception e) {
						  e.printStackTrace();
					  }


					  //온라인거래일때만, 거래정보 조회해서 영수증 ID 받기
					  if(van.getString("vanId").equals("welcome306")) {
						  String search_uri = "https://payapi.welcomepayments.co.kr/api/search/order";

						  String mid = van.getString("vanId");
						  String order_no = res.getString("trxId");

						  String api_key = "59a30bd3e66d9a87c71b5d39e26ed42a";

						  SharedMap<String, Object> reqMap = new SharedMap<>();
						  reqMap.put("mid", mid);
						  reqMap.put("order_no", order_no);

						  String hash_value = "";
						  try {
							  hash_value = EncryptUtil.sha256(mid + order_no + api_key);
							  reqMap.put("hash_value", hash_value);
						  } catch (Exception e) {
							  logger.error("WELCOME 영수증 조회 오류 : {}", e.getMessage());
						  }

						  SharedMap<String, Object> responseMap = new SharedMap<>();

						  //통신
						  responseMap = searchRequest(search_uri, reqMap.toJson());

						  //결과값 처리
						  if(responseMap.getString("result_code").equals("0000")) {
							  String transaction_no = responseMap.getString("transaction_no");
							  res.put("vanTrxId", transaction_no);

							  try {
								  res.put("hash_value", EncryptUtil.sha256(res.getString("vanTrxId") + api_key));
							  } catch (Exception e) {
								  e.printStackTrace();
							  }

						  }
					  }
				  }

				  request.setAttribute("DATAMAP", res);

			  } catch (NullPointerException e) {
				  throw e;
			  } finally {
				  return new ModelAndView("/trx/cap/receipt");
			  }


		  }else {
			// 6가지로 영수증 조회
			  try {

				  SharedMap<String, Object> res = new SharedMap<String, Object>();

				  if(!mchtId.equals("0") && tmnId.equals("0")) {
					  res =  new TrxCapDAO().getByMchtId(mchtId, trackId, authCd, amount, regDay).getRow(0);

					  if(res == null) {
						 res = new TrxCapDAO().getByMchtIdRfd(mchtId, trackId, authCd, amount, regDay).getRow(0);
					  }

				  } else if(mchtId.equals("0") && !tmnId.equals("0")) {
					  res =  new TrxCapDAO().getByTmnId(tmnId, trackId, authCd, amount, regDay).getRow(0);

					  if(res == null) {
						 res = new TrxCapDAO().getByTmnIdRfd(tmnId, trackId, authCd, amount, regDay).getRow(0);
					  }
				  } else {
					  res =  new TrxCapDAO().getBySix(mchtId, tmnId, trackId, authCd, amount, regDay).getRow(0);

					  if(res == null) {
						 res = new TrxCapDAO().getBySixRfd(mchtId, tmnId, trackId, authCd, amount, regDay).getRow(0);
					  }
				  }

				  // 올앳 영수증 조회용 거래번호
				  if(res.startsWith("van", "ALLAT")){
					  AllatUtil allatUtil = new AllatUtil();

					  RecordSet rset = new TrxReqDAO().getTrxId(res.getString("trxId"));
					  if(rset.size() > 0) {
						  SharedMap<String, Object> reqMap =rset.getRowFirst();
						  if(reqMap.isEquals("trxType", "WHTR")){
							  res.put("allatParam", allatUtil.getParam(res.getString("vanId"), res.getString("trackId"),res.getString("amount")));
						  } else {
							  res.put("allatParam", allatUtil.getParam(res.getString("vanId"),res.getString("trxId"),res.getString("amount")));
						  }
					  }
				  }

				  request.setAttribute("DATAMAP", res);
			  } catch (NullPointerException e) {
				  throw e;
			  } finally {
				  return new ModelAndView("/trx/cap/receipt");
			  }
		  }
	  }

	  @RequestMapping(value = "/trx/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	  public ModelAndView search(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
	    SessionUtil.setSearchGrade(request, cpRequest);

	    TrxCapDAO dao = new TrxCapDAO();
	    RecordSet rset = dao.getTrxMchtList(cpRequest.data, cpRequest.page);
	    return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/trx/excel/list", "");
	  }

	  @RequestMapping(value = "/trx/excel/search", method = RequestMethod.POST)
	  public ModelAndView excelSearch(HttpServletRequest request, @RequestBody List<SharedMap<String, String>> requestList) {
			RecordSet rset = new RecordSet();
			TrxCapDAO dao = new TrxCapDAO();
			CPRequest cpRequest = new CPRequest();

			logger.info("거래내역 가맹점 정보 조회수 : {}", requestList.size());

			if(requestList.size() > 0) {
				cpRequest.type = "list";

				for(SharedMap<String, String> eachMap : requestList) {
					String trxDay = eachMap.getString("trxDay");
					String authCd = eachMap.getString("authCd");
					String vanTrxId = eachMap.getString("vanTrxId");

					SharedMap<String, Object> data = dao.getTrxMchtData(trxDay, authCd);

					if(!data.isEmpty()) {
						rset.addRow();
						rset.put("trxDay", trxDay);
						rset.put("authCd", authCd);
						rset.put("vanTrxId", vanTrxId);
						rset.put("mchtId", data.getString("mchtId"));
						rset.put("mchtName", data.getString("name"));
						rset.put("tmnId", data.getString("tmnId"));
						rset.put("tmnDesc", data.getString("tmnDesc"));

						logger.info("매출일자 : [{}], 승인번호 : [{}], 거래번호 : [{}], 가맹점ID : [{}], 가맹점명 : [{}], 터미널ID : [{}], 추가정보 : [{}]",
								trxDay, authCd, vanTrxId, data.getString("mchtId"), data.getString("name"), data.getString("tmnId"), data.getString("tmnDesc"));
					}else {
						logger.info("거래내역 가맹점 정보 조회 오류 내역 : [{}], [{}], [{}]", trxDay, authCd, vanTrxId);
					}
				}
			}

			return new CPRUtil(cpRequest).dataList2(rset).setView(request,"/trx/excel/list","");
	  }

	public String setPayLoad(SharedMap<String, Object> sharedMap, String risk, String status, String resultCd, String resultMsg){
		SharedMap<String, String> payLoadMap = new SharedMap<String, String>();

		payLoadMap.put("mchtId",sharedMap.getString("mchtId"));
		payLoadMap.put("capId",sharedMap.getString("capId"));
		payLoadMap.put("capType", sharedMap.getString("capType"));
		payLoadMap.put("amount",sharedMap.getString("amount"));
		payLoadMap.put("authCd", sharedMap.getString("authCd"));
		payLoadMap.put("risk", risk);
		payLoadMap.put("trxDay",CommonUtil.getCurrentDate("yyyyMMdd"));
		payLoadMap.put("trxTime",CommonUtil.getCurrentDate("HHmmss"));
		payLoadMap.put("status",status);
		payLoadMap.put("trackId",sharedMap.getString("trackId"));
		payLoadMap.put("resultCd",resultCd);
		payLoadMap.put("resultMsg",resultMsg);
		String payLoad = CommonUtil.toQueryString(payLoadMap,"UTF-8");
		return payLoad;
	}

	@RequestMapping(value = "/trx/noti/list", method = RequestMethod.POST)
	public ModelAndView notiList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		TrxDAO trxDAO = new TrxDAO();
		RecordSet rset = trxDAO.getTrxNotiList(cpRequest.data,cpRequest.page);

		return new CPRUtil(cpRequest).dataList(rset,trxDAO).setView(request,"/trx/noti/list","");
	}

	@RequestMapping(value = {"/trx/noti/retry/{idx}"}, method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> notiRetry(HttpServletRequest request, @PathVariable String idx) {
		SharedMap<String, Object> resultMap = new SharedMap<>();
		logger.info("idx : {}", idx);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_TRX_NTS_PG");
		dao.setRecord("retry", 0);
		dao.setRecord("status", "전송실패");
		dao.addWhere("idx", idx, DAO.in);

		if(dao.update()) {
			resultMap.put("result", "OK");
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "재전송 실패했습니다.");
		}

		return resultMap;
	}
}