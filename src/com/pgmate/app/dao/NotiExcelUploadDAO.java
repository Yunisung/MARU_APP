
package com.pgmate.app.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

public class NotiExcelUploadDAO{
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.AgencyDAO.class );
	
	public void KsnetNotiUpload(String fileName ,HttpServletRequest req) {
		
		List<CPRequest> csvList = new ArrayList<CPRequest>();
		String path = fileName;
		logger.info("[FILE PATH ] => " + path);
		BufferedReader br = null;
		String line = "";
			
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "euc-kr"));
			// readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
			while ((line = br.readLine()) != null) {
				// 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
				String[] lineArr = line.split(","); 
				CPRequest map = new CPRequest();

				if (csvList.size() > 0) {

					map.setData("trnType", convert(lineArr[2]));
					map.setData("vanTrxId", convert(lineArr[3]));
					map.setData("rootTrxId", convert(lineArr[4]));

					// 앞 6자리 뒷4자리
					lineArr[5] = convert(lineArr[5]);
					String bin = lineArr[5].substring(0, 6);
					String last4 = lineArr[5].substring(12, lineArr[5].length());
					map.setData("bin", bin);
					map.setData("last4", last4);
					map.setData("amount",convert((lineArr[6])));
					map.setData("authCd", convert((lineArr[7])));
					map.setData("installment", convert((lineArr[8])));
					// 승인일자 ,시간
					lineArr[9] = convert(lineArr[9]);
					String day = lineArr[9].substring(0, 8);
					String time = lineArr[9].substring(9, lineArr[9].length());
					map.setData("trxDay", day);
					map.setData("trxTime", time);

					// 취소 시 원거래 일자 (취소가 아닐 시 null값)
					map.setData("rootTrxDay", "");
					map.setData("tmnId", convert((lineArr[13])));
					
					if (convert(lineArr[2]).equals("취소")) {
						// 취소 시 원거래 일자,시간
						lineArr[10] = convert(lineArr[10]);
						String rfd_day = lineArr[10].substring(0, 8);
						String rfd_time = lineArr[10].substring(9, lineArr[9].length());
						// 승인구분의 option값이 승인,승인취소로 돼 있어 그냥 취소로 넣으면 금액 iuput값 css 깨짐현상 
						map.getData("trnType").val = "승인취소";
						map.getData("trxDay").val = rfd_day;
						map.getData("trxTime").val = rfd_time;
						map.getData("rootTrxDay").val = day;
						// 취소일 경우 bin,last,authCd 빈값 
						map.getData("bin").val = "";
						map.getData("last4").val = "";
						map.getData("authCd").val = "";
					}
					insert(map, req);
				}
				csvList.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void GalxNotiUplaid(String fileName ,HttpServletRequest req) {
		
		List<CPRequest> csvList = new ArrayList<CPRequest>();
//		String path = fileName;
//		logger.info("[FILE PATH ] => " + path);
		BufferedReader br = null;
		String line = "";
		String path = "C:/Git_newSource/MARU_APP/web/notiFileUpload/신용카드_승인내역_20220411.csv";
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			// readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
			while ((line = br.readLine()) != null) {
				// 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
				String[] lineArr = line.split(","); 
				CPRequest map = new CPRequest();
				
				if (csvList.size() > 0) {
					if("승인 성공".equals(lineArr[29])) {
						lineArr[28] = "승인";
					}
					map.setData("trnType", convert(lineArr[29]));
					map.setData("vanTrxId", convert(lineArr[8]));
					map.setData("rootTrxId", convert(lineArr[11]));
					map.setData("bin", "");
					map.setData("last4", "");
					map.setData("amount",convert((lineArr[22])));
					map.setData("authCd", convert((lineArr[11])));
					map.setData("installment", convert((lineArr[19])));
					// 승인일자 ,시간
					map.setData("trxDay", convert(lineArr[2]).replace("/", ""));
					map.setData("trxTime", convert(lineArr[3]).replace(":", ""));

					// 취소 시 원거래 일자 (취소가 아닐 시 null값)
					map.setData("rootTrxDay", "");
					map.setData("tmnId", convert((lineArr[6])));
					
					if (convert(lineArr[29]).equals("승인취소 성공")) {
						// 승인구분의 option값이 승인,승인취소로 돼 있어 그냥 취소로 넣으면 금액 iuput값 css 깨짐현상 
						map.getData("trnType").val = "승인취소";
						map.getData("trxDay").val = convert(lineArr[4]).replace("/", "");
						map.getData("trxTime").val = convert(lineArr[6]).replace(":", "");
						map.getData("rootTrxDay").val = convert(lineArr[2]).replace("/", "");
					}
					insert(map, req);
				}
				csvList.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String convert(String str) {
		str = str.replaceAll("[\"=]", "");
		return str ;
	}
	
	public CPResponse insert( CPRequest cpRequest , HttpServletRequest req) {
		
		 DAO daoNoti = new DAO();
		 daoNoti.setTable("PG_TRX_REQ");
		 daoNoti.setColumns("trackId,mchtId,tmnId");
		 daoNoti.addWhere("trxId" , cpRequest.getValue("rootTrxId"));
		 RecordSet rset_noti = daoNoti.search(); 
		 daoNoti.initRecord();
		 
		 SharedMap<String,Object> tmnMap1 = rset_noti.getRowFirst();
		
		//KJM : 거래생성 정보 넣기위한 변수 선언
		SharedMap<String,Object> load = new SharedMap<String,Object>();
		load.put("mchtId", tmnMap1.getString("mchtId"));
		load.put("tmnId" , tmnMap1.getString("tmnId"));
		
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
		load.put("regId", SessionUtil.getUserId(req));
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
		data.put("rootTrxDay", cpRequest.getValue("rootTrxDay")); // 취소시 원거래일자
		data.put("trackId", tmnMap1.getString("trackId"));		//주문번호
		data.put("vanTrxId", cpRequest.getValue("vanTrxId"));	// 처리사 거래번호
		data.put("vanDay", cpRequest.getValue("vanDay"));		//van 또는 카드사 정산 예정일
		data.put("vanStlFee", cpRequest.getLongValue("vanStlFee"));//van 또는 카드사 수수료
		data.put("exeStatus","");								//처리결과(완료, 실패)
		data.put("regId", SessionUtil.getUserId(req));		//등록자아이디(기본값 : system)
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

	
	public static void main(String[] args) {
//		new NotiExcelUploadDAO().GalxNotiUplaid("",req);
	}
}
