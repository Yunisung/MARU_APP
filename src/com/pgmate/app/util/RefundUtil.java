package com.pgmate.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.Refund;
import com.pgmate.pay.bean.Request;
import com.pgmate.pay.bean.Response;
import com.pgmate.pay.bean.Result;

/**
 * @author Administrator
 *
 */
//KJM : 거래 취소 기능
public class RefundUtil {

	/**
	 * 
	 */
	public RefundUtil() {
	
	}
	
	
	public String execute(SharedMap<String,Object> trxMap,String regId, String grade){
		Request request = new Request();
		String res = "";
		Refund refund = new Refund();
		refund.trxType 	= "ONTR";
		refund.tmnId  	= trxMap.getString("tmnId");
		refund.trackId	= GenKey.genKeys(CPKEY.REFUND,"ADMIN");
		refund.amount	= trxMap.getLong("amount");
		refund.rootTrxId= trxMap.getString("trxId");
		if("본사".equals(grade) || "대행사".equals(grade)){
			refund.udf1     = "ADMINWEB";
		} else {
			refund.udf1     = "";
		}
		refund.udf2     = regId;
		request.refund = refund;
		
		SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
		if(!tmnMap.getString("status").equals("사용")) {
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
			response.result = result;
			res = GsonUtil.toJson(response);
			return res;
		}
		
		String reqJson = GsonUtil.toJson(request);
		
		res = comm(reqJson,tmnMap.getString("payKey"));
		if(res.startsWith("CONNECT")){
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "통신장애로 인한 취소 실패 ";
			response.result = result;
			res = GsonUtil.toJson(response);
		}else{
			Response resp = (Response)GsonUtil.fromJson(res, Response.class);
			try{
				if(resp.refund != null){
					if(!CommonUtil.isNullOrSpace(resp.refund.trxId)){
						String q = "INSERT INTO PG_TRX_ADMIN_RFD SELECT NULL,rootTrxId,vanTrxId,vanId,vanResultCd,vanResultMsg,rfdAmount,resultCd,trxId,'"+regId+"', '" + CommonUtil.getCurrentDate("yyyyMMdd") + "', regDate FROM PG_TRX_RFD  WHERE trxId = '" + resp.refund.trxId + "'";
						new DAO().update(q);
					}
				}
			}catch(Exception e){}
		}
		return res;
	
	}
	
	//KJM : 매입현황 > 매입내역 상세정보 > 거래 취소 수행
	public String execute(SharedMap<String, Object> trxMap, String amount, String regId, String grade) {
		Request request = new Request();
		String res = "";
		Refund refund = new Refund();
		refund.trxType 	= "ONTR";	//KJM : 수기
		refund.tmnId  	= trxMap.getString("tmnId");
		refund.trackId	= GenKey.genKeys(CPKEY.REFUND,"ADMIN"); //KJM : rfd_000-00000-00-00000
		refund.amount	= Long.parseLong(amount);
		refund.rootTrxId= trxMap.getString("trxId");
		if("본사".equals(grade) || "대행사".equals(grade)){
			refund.udf1     = "ADMINWEB";
		} else {
			refund.udf1     = "";
		}
		refund.udf2     = regId;
		//KJM : 세팅한 refund 객체 request 클래스에 대입
		request.refund = refund;
		
		//KJM : 가맹점 단말기 정보 가져옴
		SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
		//KJM : 단말기 사용 상태가 아닐 때
		if(!tmnMap.getString("status").equals("사용")) {
			Response response = new Response();
			Result result = new Result();
			//KJM : 에러메시지 세팅
			result.resultCd = "XXXX";
			result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
			response.result = result;
			//KJM : 세팅한 데이터들 json형식으로 변환
			res = GsonUtil.toJson(response);
			//KJM : 에러 메시지 반환
			return res;
		}
		
		//KJM : 받아온 데이터들 json 형식으로 변환
		String reqJson = GsonUtil.toJson(request);
		
		//KJM : 받은 데이터와 온라인 결제키를 이용해 HTTP 통신을 통한 값 가져옴
		res = comm(reqJson,tmnMap.getString("payKey"));
		//KJM : "CONNECT ERROR" 경우
		if(res.startsWith("CONNECT")){
			//KJM : 에러 메시지 데이터 세팅
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "통신장애로 인한 취소 실패 ";
			response.result = result;
			//KJM : res에 json 형식의 에러 메시지 배정
			res = GsonUtil.toJson(response);
		}else{
			//KJM : res의 json형태의 데이터를 Response 클래스형으로 반환
			Response resp = (Response)GsonUtil.fromJson(res, Response.class);
			try{
				//KJM : 원거래 정보 존재할 때
				if(resp.refund != null){
					//KJM : 거래번호가 null이거나 빈값이 아닐 때
					if(!CommonUtil.isNullOrSpace(resp.refund.trxId)){
						//KJM : insert 쿼리문 작성
						//KJM : 결제취소원장(PG_TRX_RFD)의 거래번호 정보를 사이트 통한 거래취소 요청 정보(PG_TRX_ADMIN_RFD)에 넣는다
						String q = "INSERT INTO PG_TRX_ADMIN_RFD SELECT NULL,rootTrxId,vanTrxId,vanId,vanResultCd,vanResultMsg,rfdAmount,resultCd,trxId,'"+regId+"', '" + CommonUtil.getCurrentDate("yyyyMMdd") + "', regDate FROM PG_TRX_RFD  WHERE trxId = '" + resp.refund.trxId + "'";
						new DAO().update(q);
					}
				}
			}catch(Exception e){}
		}
		//KJM : http 통신 결과 값 or 에러 메시지 반환
		return res;
	}
	
	//KJM : POST 방식의 데이터 전송 세팅
	public String comm(String request,String payKey){
		//KJM : refund 확인필 / update문도 확인...
		String paymentUrl = "http://127.0.0.1:10002/api/refund";
		StringBuilder result = new StringBuilder();
		URL url = null;
		//KJM : http 프로토콜 사용 connection
		HttpURLConnection conn = null;
		
		System.setProperty("https.protocols", "TLSv1.2");
		
		long time = System.currentTimeMillis();
		try {
			System.out.println("LOCAL >> PAYMENT ["+request+"]");
			//KJM : url 클래스 생성
			url = new URL(paymentUrl);
			
			
			//KJM : 선언한 url 클래스에서 connection 설정
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");	//KJM : 요청방식(POST)
			conn.setUseCaches(false);		//캐시기능 제거
			conn.setDoInput(true);			//?
			conn.setDoOutput(true);			//출력 스트림 사용
			conn.setConnectTimeout(10000);	//서버 접속시 연결 시간
			conn.setReadTimeout(60000);		//읽을 때 연결 시간
			
			//KJM : RequestHeader 값 세팅
			conn.setRequestProperty("Content-Type", "application/json");	//json 형식으로 전송
			conn.setRequestProperty("Authorization", payKey);				//허용되는 키..?
			conn.setRequestProperty("Connection", "close");					//?
			
			//KJM : request body에 data 담을 OutputStream 객체 생성
			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes("utf-8"));	//request body에 data 세팅
			os.flush();								//request body에 data 입력
			os.close();								//os 종료
			
			//KJM : 내용 읽어오기 위한 br 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			//KJM : result에 읽어온 내용 담는다
			while ((line = br.readLine()) != null)
				result.append(line+"\n");
			
			br.close();
			
		} catch(Exception e) {
			result.append("CONNECT ERROR ["+e.getMessage()+"] "+paymentUrl);
			System.out.println("PAYMENT URL REQUEST ERROR =["+e.getMessage()+"]");
			
		}finally{
			System.out.println("ElapsedTime : "+(long)(System.currentTimeMillis()-time)+"msec"); //KJM : 통신 시간
			System.out.println("LOCAL << PAYMENT ["+result.toString()+"]");
			//KJM : connection 접속 종료
			conn.disconnect();
		}
		//KJM : 통신 내용 반환
		return result.toString();
	}
		
	//휴대폰 결제 승인취소 API 적용====================================================================================
	public String phoneExecute(SharedMap<String, Object> trxMap, String amount, String regId, String grade) {
		Request request = new Request();
		String res = "";
		Refund refund = new Refund();
		refund.tmnId  	= trxMap.getString("tmnId");
		refund.trackId	= GenKey.genKeys(CPKEY.REFUND,"ADMIN");
		refund.amount	= Long.parseLong(amount);
		refund.rootTrxId= trxMap.getString("trxId");
		if("본사".equals(grade)){
			refund.udf1     = "ADMINWEB";
		}  else	if("대행사".equals(grade)){
			refund.udf1     = "DISTWEB";
		} else {
			refund.udf1     = "";
		}
		refund.udf2     = regId;
		request.refund = refund;
		
		SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
		if(!tmnMap.getString("status").equals("사용")) {
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
			response.result = result;
			res = GsonUtil.toJson(response);
			return res;
		}
		
		String reqJson = GsonUtil.toJson(request);
		
		res = phoneRef(reqJson,tmnMap.getString("payKey"));
		if(res.startsWith("CONNECT")){
			Response response = new Response();
			Result result = new Result();
			result.resultCd = "XXXX";
			result.resultMsg= "통신장애로 인한 취소 실패 ";
			response.result = result;
			res = GsonUtil.toJson(response);
		}else{
			Response resp = (Response)GsonUtil.fromJson(res, Response.class);
			try{
				if(resp.refund != null){
					if(!CommonUtil.isNullOrSpace(resp.refund.trxId)){
						String q = "INSERT INTO PG_TRX_ADMIN_RFD SELECT NULL,rootTrxId,vanTrxId,vanId,vanResultCd,vanResultMsg,rfdAmount,resultCd,trxId,'"+regId+"', '" + CommonUtil.getCurrentDate("yyyyMMdd") + "', regDate FROM PG_PHONE_RFD WHERE trxId = '" + resp.refund.trxId + "'";
						new DAO().update(q);
					}
				}
			}catch(Exception e){}
		}
		return res;
	}



	
	
	
	public String phoneRef(String request,String payKey){
		//개발
		//String paymentUrl = "https://svcapidev.mtouch.com/api/phone/refund";
		//운영
		String paymentUrl = "http://pgwas1:10002/api/phone/refund";
		
		StringBuilder result = new StringBuilder();
		URL url = null;
		HttpURLConnection conn = null;
		
		System.setProperty("https.protocols", "TLSv1.2");
		
		long time = System.currentTimeMillis();
		try {
			System.out.println("LOCAL >> PAYMENT ["+request+"]");
			url = new URL(paymentUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(60000);
			
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", payKey);
			conn.setRequestProperty("Connection", "close");
			
			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes("utf-8"));
			os.flush();
			os.close();
			
			BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream()));
			
			String line;
			while ((line = br.readLine()) != null)
				result.append(line+"\n");
			
			br.close();

		} catch(Exception e) {
			result.append("CONNECT ERROR ["+e.getMessage()+"] "+paymentUrl);
			System.out.println("PAYMENT URL REQUEST ERROR =["+e.getMessage()+"]");
			
		}finally{
			System.out.println("ElapsedTime : "+(long)(System.currentTimeMillis()-time)+"msec");
			System.out.println("LOCAL << PAYMENT ["+result.toString()+"]");
			conn.disconnect();
		}
		return result.toString();
	}
	//휴대폰 결제 승인취소 API 적용====================================================================================
}
