package com.pgmate.app.ctl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.log.SysoCounter;
import com.pgmate.app.dao.AgencyDAO;
import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.TotLoadDAO;
import com.pgmate.app.dao.TotLoadDtlDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.app.export.TaxExport;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Files;
import com.pgmate.app.util.CPRUtil;
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
	
	//KJM : ONLINE 거래생성 submit
	@RequestMapping(value = "/trxoper/new/insert", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse insert(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		
		//KJM : 거래생성 정보 넣기위한 변수 선언
		SharedMap<String,Object> load = new SharedMap<String,Object>();
		load.put("mchtId", cpRequest.getValue("mchtId"));
		load.put("tmnId" , cpRequest.getValue("tmnId"));
		//KJM : 승인구분이 승인일 때 금액 그대로 넣어줌
		if(cpRequest.getValue("trnType").equals("승인")) {
			load.put("amount", cpRequest.getLongValue("amount"));
		//KJM : 승인 취소일 때
		} else {
			//KJM : 금액이 양수의 값일 때 음수로 바꿔줌
			if(cpRequest.getLongValue("amount") > 0) {
				load.put("amount", -cpRequest.getLongValue("amount"));
			//KJM : 음수일 땐 음수 그대로 넣어줌
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
		//KJM : SELECT A.van,B.vanId FROM PG_MCHT_TMN A, PG_VAN B WHERE A.vanIdx = B.idx AND A.status='사용' AND tmnId='tmnId' ORDER BY A.van asc
		//KJM : 터미널 정보와 van 정보 테이블을 이용하여 해당 터미널아이디에서 사용중인 van과 vanid 조회
		dao.setTable("PG_MCHT_TMN A, PG_VAN B ");
		dao.setColumns("A.van,B.vanId");
		dao.setWhere("A.vanIdx = B.idx AND A.status = '사용'");
		dao.addWhere("tmnId", load.getString("tmnId"), DAO.eq);
		dao.setOrderBy("A.van asc");
		//KJM : select 쿼리문 수행
		RecordSet rset = dao.search(); 
		dao.initRecord();
		//KJM : recordset형식의 컬럼들을 map형식으로 변환
		SharedMap<String,Object> tmnMap = rset.getRowFirst();
		
		load.put("van",tmnMap.getString("van") );
		load.put("vanId",tmnMap.getString("vanId") );
		load.put("rootTrxId", cpRequest.getValue("rootTrxId"));
		
		//KJM : insert 쿼리 수행할 데이터 변수 선언
		SharedMap<String,Object> data = new SharedMap<String,Object>();
		RecordSet rootCap = null;
		
		//KJM : 승인 구분이 승인일 때 금액 그대로, 승인취소일 때 음수의 금액으로 넣기
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
		
		//KJM : 할부기간 : 00패턴에 맞게 변환 후 넣어줌 (1->01, 10->10)
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
		data.put("bin", cpRequest.getValue("bin"));
		data.put("last4", cpRequest.getValue("last4"));			//KJM
		data.put("authCd", cpRequest.getValue("authCd"));		//승인번호
		data.put("trxDay", cpRequest.getValue("trxDay"));		//거래일자
		data.put("trxTime", cpRequest.getValue("trxTime"));
		data.put("rootTrxDay", cpRequest.getValue("rootTrxDay"));//취소시 원거래일자
		data.put("trackId", cpRequest.getValue("trackId"));		//주문번호
		data.put("vanTrxId", cpRequest.getValue("vanTrxId"));	//처리사 거래번호
		data.put("vanDay", cpRequest.getValue("vanDay"));		//van 또는 카드사 정산 예정일
		data.put("vanStlFee", cpRequest.getLongValue("vanStlFee"));//van 또는 카드사 수수료
		data.put("exeStatus","");								//처리결과(완료, 실패)
		data.put("regId", SessionUtil.getUserId(request));		//등록자아이디(기본값 : system)
		data.put("regDay",CommonUtil.getCurrentDate("yyyyMMdd"));
		
		
		DAO d = new DAO();
		//KJM : where : 배치처리인덱스='인덱스', van='van', 처리사거래번호='vanTrxId', 처리결과='완료'
		//KJM : 이미 거래 생성이 요청 된 거래 idx 조회
		d.setTable("PG_TRX_LOAD_DTL A, PG_TRX_LOAD B");
		d.setColumns("A.idx");
		d.addWhere("A.batchIdx = B.idx");
		d.addWhere("B.van", data.getString("van"));
		d.addWhere("A.vanTrxId", data.getString("vanTrxId"));
		d.addWhere("A.exeStatus", "완료", DAO.eq);
		d.setOrderBy("A.regDate asc");
		//KJM : select 쿼리 수행
		RecordSet r = d.search();
		//KJM : 쿼리 수행 값 있을 경우 (중복 거래 생성)
		if(r.size() > 0){
			//KJM : 에러메시지 생성, 봔환
			return new CPRUtil(cpRequest)
	        		.resultNOK("이미 거래 생성 요청된 거래입니다. VAN 거래번호 기준 ")
	        		.cpResponse();
		}
		
		TrxDAO trxDAO = new TrxDAO();
		//KJM : 배치처리인덱스 값 할당 (insert 수행 후 추가된 인덱스 값)
		long batchIdx = trxDAO.insertTrxLoad(load);
		//KJM : idx가 0일 경우 insert 안된것
		if(batchIdx == 0){
			//KJM : 에러메시지 생성, 반환
			return new CPRUtil(cpRequest)
	        		.resultNOK("거래 데이터 생성 실패",trxDAO.getError())
	        		.cpResponse();
		}
		//KJM : data에 batchIdx 값 할당
		data.put("batchIdx", batchIdx);
		
		//KJM : insert 정상 수행 시
		if(trxDAO.insertTrxLoadDtl(data)){
			//KJM : 정상 수행 메시지 생성, 반환
			return new CPRUtil(cpRequest)
	        		.resultOK("거래데이터가 생성되었습니다. 처리 목록에서 실행하여 주시기 바랍니다.")
	        		.cpResponse();
		}else{
			//KJM : 에러메시지 생성, 반환
			trxDAO.deleteTrxLoad(batchIdx);
			return new CPRUtil(cpRequest)
	        		.resultNOK("거래 데이터 생성 실패",trxDAO.getError())
	        		.cpResponse();
		}
		
	}
	
	//KJM : csv 파일 업로드 거래 재생성
	@ResponseBody
	@RequestMapping(value="/trxoper/uploadFile", method=RequestMethod.POST)
	//ajax로 보내준 파일을 multipartFile로 받아서 사용
	public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws IOException, ServletException {
		//컬럼명은 처리하지 않기 위한 플래그 선언
		boolean flag = false;
		String line;
		//EUC-KR로 인코딩 해주어야 한글이 안깨짐
		BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "EUC-KR"));
		//파일 한줄씩 읽어서 처리
		while((line=br.readLine()) != null) {
			//,를 기준으로 잘라 배열에 넣어준다
			String coll[] = line.split(",");
			CPRequest map = new CPRequest();
			//해당라인이 컬럼명일때 flag=false
			if(flag) {
				//터미널 아이디로 가맹점 아이디 조회하여 사용
				
				SharedMap<String, Object> dtlMap = tmnIdCheck(coll[13]);
				map.setData("mchtId", dtlMap.getString("mchtId"));
				System.out.println("mchtId : " + map.getValue("mchtId"));
				map.setData("tmnId", coll[13]);
				map.setData("trnType", coll[2]);
				map.setData("amount", coll[6]);
				map.setData("installment", coll[8]);
				if(map.getValue("trnType")=="승인") {
					map.setData("bin", coll[5].substring(0, 6));
					map.setData("last4", coll[5].substring(12));
					map.setData("authCd", coll[7]);
					map.setData("trxDay", coll[9].substring(0,8));
					map.setData("trxTime", coll[9].substring(9));
				}else {
					map.setData("trxDay", coll[10].substring(0,8));
					map.setData("trxTime", coll[10].substring(9));
					map.setData("rootTrxDay", coll[9].substring(0,8));
				}
				map.setData("vanTrxId", coll[3]);
				map.setData("trackId", coll[17]);
				
				insert(req, map);
			}
			flag = true;
		}
		return "ok";
		
	}
	
	//KJM : 터미널아이디로 가맹점 아이디 조회
	public SharedMap<String, Object> tmnIdCheck(String tmnId) {
		SharedMap<String, Object> map = new TrxDAO().getByTmnId(tmnId).getRowFirst();
		return map;
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
	
	//KJM : 거래관리 > 거래생성 조회 (리스트)
	@RequestMapping(value = "/trxoper/load/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView loadList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		//KJM : 로그인중인 계정 소속 구분
		SessionUtil.setSearchGrade(request, cpRequest);
		
		TotLoadDAO trxLoadDAO = new TotLoadDAO();
		//KJM : 조회한 리스트를 해당 경로로 보내준다
		RecordSet rset = trxLoadDAO.list(cpRequest.data,cpRequest.page);


		return new CPRUtil(cpRequest).dataList(rset,trxLoadDAO).setView(request,"/trxoper/load/list","");
	}
	// KBR : 거래관리 > 거래생성 요청 클릭 시 
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
	
	//KJM : 거래생성조회 > 상세내역
	@RequestMapping(value = "/trxoper/load/dtl/{idx}", method = RequestMethod.GET)
	public ModelAndView loadDtlForm(HttpServletRequest request, @PathVariable String idx) {
		//KJM : 해당 인덱스에 대한 상세내역 세팅
		//KJM : form에 인덱스 제공용으로 보냄 > form -> search돌림(인덱스 이용) -> list
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
			dao.setTable("(SELECT MAX(trxDay) as endDay, taxId, mchtId, name, sum(stlAgencyFee+benefit) amt FROM VW_TRX_CAP WHERE distId ='00' and vanStatus = '입금완료' and substr(stlVanDay,1,6) ='"+requestMap.getString("trxDay")+"' group by taxId) A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId LEFT JOIN PG_MCHT C ON A.mchtId = C.mchtId");
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
			dao.addWhere("vanStatus", "입금완료", DAO.eq);
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









