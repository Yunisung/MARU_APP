package com.pgmate.app.ctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

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

import com.itextpdf.text.log.SysoCounter;
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
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Files;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.KSignUtil;
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
	
	// KBR 대행사 정산 조회 
	@RequestMapping(value = "/settle/dist/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDistList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		// data.name ~ data.key 값 셋팅 
		cpRequest.setData("grade", "대행사", "eq", "", true);		// grade == 대행사
		cpRequest.setData("stlAmt", "0", "ne", "", true);		// 지급예정액이 존재 하는것 
		cpRequest.setData("stlDay", "", "", "desc", false); 	// 매입날짜 (20211022형식) order by 
		cpRequest.setData("memberName", "", "", "asc", false);	// 대행사 이름 order by 
		//데이터,페이지 셋팅 후 결과 값 
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		// 응답객체에 반환값 셋팅 후 엑셀 또는 PDF체크 후 해당 URL로 반환
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/dist/list", "");
	}
	// KBR 에이전시 정산 조회 
	@RequestMapping(value = "/settle/agency/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAgencyList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		// data.name ~ data.key 값 셋팅 
		cpRequest.setData("grade", "에이전시", "eq", "", true);
		cpRequest.setData("stlAmt", "0", "ne", "", true);
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("memberName", "", "", "asc", false);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/agency/list", "");
	}
	// KBR 지사 정산 조회 
	@RequestMapping(value = "/settle/sales/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleSalesList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO settleDAO = new SettleDAO();
		// data.name ~ data.key 값 셋팅 
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
	
	//KJM : 가맹점 정산 조회 리스트
	@RequestMapping(value = "/settle/mcht/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		//KJM : VW_SETTLE_MCHT
		SettleMchtDAO settleDAO = new SettleMchtDAO();
		//KJM : 리스트 개수 기본 값 200 맞춰줌
		if(cpRequest.page.size ==20){
			cpRequest.page.size = 200;
		}
		cpRequest.setData("stlType", "C+0", "ne", "", true);
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		
		
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/mcht/list", "");
	}

	//KJM : 가맹점 정산 생성 > 정산 내역 생성(리스트)
	@RequestMapping(value = "/settle/mcht/make/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView makeSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		//KJM : 로그인 계정 소속 구분
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO dao = new SettleDAO();
		StringBuffer query = new StringBuffer();
		
		/* KJM
		 * uuid : 128비트 수이며, 32자리의 16진수로 표현된 고유 아이디 ex) f189f4ae-af11-11e7-b252-186590cec0c1
		 * SELECT 오늘날짜(000000), idx(8자리), C1, 실지급액((총 거래금액 - 총 수수료 - 수수료 VAT) + (매입취소금액 - 매입취소수수료 - 취소 수수료 VAT)), 은행코드, 은행이름, 계좌번호, 예금주
		 * SELECT T1, 보류반환 거래금액(null일때 0), 보류반환 수수료(null-0), 보류반환 VAT(null-0), 보류반환건수(null-0), 차감금액(null-0), 수기반환요청금액(null-0), 차감정산대상여부(차감정산금액이 null-'X','O')
		 */
		query.append("( SELECT concat(DATE_FORMAT(now(),'%y%m%d'), substr(uuid(),1,8)) as idx ,C1.*,(payAmt - payFee - payVat) + (rfdAmt - rfdVat - rfdFee) as stlAmount"
				+ " ,C2.bankCd,C2.bankName,C2.account,C2.accntHolder FROM ");
		query.append("(SELECT T1.*,IFNULL(T2.relsAmt,0) AS relsAmt,IFNULL(T2.relsFee,0) AS relsFee,IFNULL(T2.relsVat,0) AS relsVat,IFNULL(T2.relsCnt,0) AS relsCnt, ");
		query.append(" IFNULL(T3.deductAmount,0) as deductAmt, IFNULL(T4.manualRelsAmt,0) as manualRelsAmt, IFNULL(T5.ddctAmt,0) as ddctAmt, IF(T5.ddctAmt IS NULL, 'X', 'O') AS ddctTypeTemp ");
		//KJM : --------------T1 시작-------------------
		//KJM : 가맹점아이디, 가맹점이름, 대표이름, 지급대기(status), 정산예정일, 입금일, 정산주기
		query.append(" FROM (SELECT mchtId, name as mchtName, ceoName,'지급대기' as `status` ,max(stlDay) stlDay, min(stlDay) stlStartDay , min(trxDay) startDay ,max(trxDay) endDay, stlType,");
		//KJM : 리스크 없는 매입건의 금액, 수수료, 수수료 VAT, 건수
		query.append(" SUM(IF(capType ='매입' and risk ='',amount,0)) as payAmt, SUM(IF(capType ='매입' and risk ='',stlFee,0)) as payFee, SUM(IF(capType ='매입' and risk ='',stlFeeVat,0)) as payVat, SUM(IF(capType ='매입' and risk ='' ,1,0)) as payCnt, ");
		//KJM : 매입취소건의 금액, 수수료, 수수료 VAT, 건수
		query.append(" SUM(IF(capType ='매입취소',amount,0)) as rfdAmt, SUM(IF(capType ='매입취소',stlFee,0)) as rfdFee, SUM(IF(capType ='매입취소',stlFeeVat,0)) as rfdVat, SUM(IF(capType ='매입취소',1,0)) rfdCnt, ");
		//KJM : 리스크 있는 매입건의 금액, 수수료, 수수료 VAT, 건수
		query.append(" SUM(IF(capType ='매입' and risk !='',amount,0)) as holdAmt, SUM(IF(capType ='매입' and risk !='',stlFee,0)) as holdFee, SUM(IF(capType ='매입' and risk !='',stlFeeVat,0)) as holdVat, SUM(IF(capType ='매입' and risk !='',1,0)) as holdCnt, ");
		//KJM : 리스크 없는 거래의 대행사 수수료, 에이전시 수수료, 입금 수수료, 본사기준 차액정산액, 본사수익
		query.append(" SUM(IF(risk ='',stlDistFee,0)) as distFee,");
		query.append(" SUM(IF(risk ='',stlAgencyFee,0)) as agencyFee,");
		query.append(" SUM(IF(risk ='',stlVanFee,0)) as vanFee,");
		query.append(" SUM(IF(risk ='',stlDiffAmt,0)) as diffAmt,");
		query.append(" SUM(IF(risk ='',benefit,0)) as benefit ,");
		//KJM : 가맹점 정산 수수료 최대값, taxId
		query.append(" MAX(stlRate) as stlRate,");
		query.append(" MAX(taxId) as taxId");
		//KJM : VW_TRX_CAP > 정산대기 상태이고 입력한 정산 예정일에 일치하는 
		query.append(" FROM VW_TRX_CAP WHERE stlStatus='정산대기' AND stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
		//KJM : 조회 조건에 정산 주기를 선택했을 경우
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			//KJM : 선택한 정산주기를 where에 적용
			query.append(" AND stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
		}
		//KJM : ---------------T1 마지막---------------------
		//KJM : 가맹점아이디, 정산 유형 기준으로 그룹 정렬
		query.append(" GROUP BY mchtId, stlType ");
		query.append(" ) AS T1 LEFT OUTER JOIN ");
		//KJM : ---------------T2 시작----------------------
		//KJM : 매입내역뷰.가맹점아이디, 매입내역뷰.총금액, 수수료, 수수료 VAT, 거래건수
		query.append(" ( SELECT B.mchtId ,SUM(B.amount) as relsAmt , SUM(stlFee) as relsFee,  SUM(stlFeeVat) as relsVat , SUM(1) as relsCnt");
		//KJM : 정산 지급보류 관리(A), 거래관리뷰(B) 매입거래번호가 같고 반환요청이면서 조회조건에 맞는 정산예정일인 건을 거래관리뷰의 가맹점아이디를 기준으로 그룹정렬
		query.append("   FROM PG_SETTLE_HOLD A, VW_TRX_CAP B  WHERE A.capId = B.capId and A.`status` ='반환요청' AND B.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND B.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
		query.append(" group by B.mchtId");
		//KJM : ---------------T2 마지막---------------------
		//KJM : t1과 t2의 가맹점 아이디를 기준으로 연결
		query.append(" ) AS T2 ON T1.mchtId = T2.mchtId LEFT OUTER JOIN ");
		//KJM : 입급정산 관련 내역
		query.append(" ( SELECT mchtId,SUM(deductAmount) deductAmount FROM VW_COLLECT_SETTLE_DTL_STATUS WHERE status = '확정' and deductAmount < 0 and deductStlId ='' group by mchtId");
		query.append(" ) AS T3 ON T1.mchtId = T3.mchtId LEFT OUTER JOIN ");
		//KJM : 가맹점 예수금 관리
		query.append(" ( SELECT mchtId,SUM(ABS(amount)) manualRelsAmt FROM PG_MCHT_DEPOSIT WHERE `depType` ='반환요청' AND stlId = '' AND status = '생성' group by mchtId");
		query.append(" ) AS T4 ON T1.mchtId = T4.mchtId ");
		//KJM : 차감정산스케쥴
		query.append(" LEFT JOIN ( SELECT mchtId, SUM(ddctAmt) AS ddctAmt FROM PG_SETTLE_DDCT WHERE stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND stlStatus = '정산대기' GROUP BY mchtId) T5 ON T1.mchtId = T5.mchtId");
		query.append(" ) AS C1 LEFT OUTER JOIN PG_MCHT_TAX C2 ON C1.taxId = C2.taxId) A");

		dao.setTable(query.toString());
		//KJM : ddctType(차감정산대상여부)은 ddctTypeTemp가 'X'이면 'X', (실지급액-차감정산금액)이 0보다 작으면 'N', 아니면 'Y'
		dao.setColumns("A.*, case when ddctTypeTemp = 'X' then 'X' when (stlAmount - ddctAmt) < 0 then  'N' ELSE 'Y' END AS ddctType ");
		dao.setOrderBy("A.mchtName asc");
		
		//KJM : 조회 조건에 정산예정일과 정산주기 선택했을 경우 data값 삭제 (이미 쿼리문에 적용 되어있음, where절에 중복 적용 방지)
		cpRequest.deleteKeyData("stlEndDay");
		cpRequest.deleteKeyData("stlStartDay");
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			cpRequest.deleteKeyData("stlType");
		}
		//KJM : select 쿼리문 수행
		RecordSet rset = dao.search(cpRequest.data);
		
		//KJM : list 요청일 때 temp 테이블에 조회된 데이터들 넣어줌
		if (cpRequest.type.equalsIgnoreCase("list")) {
			insertMchtSettleTemp(rset.getRows(), request);
		}
		
		//가맹점 정산생성 - 계좌정보 복호화
		SharedMap<String, Object> result = rset.getRow();
		KSignUtil.getInstance().Decrypt(rset.getRows(), "account");
		//KJM : 가맹점대표자명 복호화
		KSignUtil.getInstance().Decrypt(rset.getRows(), "ceoName");
		KSignUtil.getInstance().Decrypt(rset.getRows(), "bankName");
		
		//KJM : 조회된 리스트를 지정된 url에 보내준다
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/make/list", "");
	}
	
	//KJM : 가맹점 정산 TEMP 테이블 insert
	public int insertMchtSettleTemp(List<SharedMap<String, Object>> mchtSettleTempList, HttpServletRequest request) {
		int inserted = 0;
		logger.debug("insert MchtSettleTemp batch : {}", mchtSettleTempList.size());
		String query = "INSERT INTO `PG_SETTLE_MCHT_TEMP` (`idx`,`mchtId`,`status`,`stlDay`,`stlStartDay`,`startDay`,`endDay`,`payAmt`,`payFee`,`payVat`,`payCnt`,`rfdAmt`,`rfdFee`,`rfdVat`,`rfdCnt`,`holdAmt`,`holdFee`,`holdVat`,`holdCnt`,`relsAmt`,`relsFee`,`relsVat`,`relsCnt`,`distFee`,`agencyFee`,`vanFee`,`diffAmt`,`benefit`,`deductAmt`,`manualRelsAmt`,`manualDeductAmt`,`stlAmount`,`ddctAmt`,`ddctType`,`taxId`,`bankCd`,`bankName`,`account`,`accntHolder`, `stlRate`,`stlType`, `regId`, `regDay`) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String keyList[] = {"bankCd", "bankName", "account", "accntHolder"};

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			
			//KJM : 100개 단위로 로우를 커밋하겠다.
			int batchSize = 100;
			int count = 0;
			
			for (SharedMap<String, Object> map : mchtSettleTempList) {
				
				//KJM : list<sharedmap<>> 형식 암호화
			    for(int i = 0 ; i < keyList.length ; i++) {
		               CPRequest list = new CPRequest();
		               String data = map.getString(keyList[i]);
		               list.setData(keyList[i], data);
		               KSignUtil.getInstance().Encrypt(list, keyList[i]);
		               Data tmp = list.getData(keyList[i]);
		               map.replace(tmp.name, tmp.val);
	            }
				
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
				pstmt.setString(i++, SessionUtil.getUserId(request));
				pstmt.setString(i++, CommonUtil.getCurrentDate("yyyyMMdd"));
				/*
				 * KJM : addBatch : pstmt에서 제공.
				 * 쿼리를 실행하지 않고 쿼리 구문을 메모리에 올려두었다가 실행 명령이 있으면 한번에 DB쪽으로 날려준다.
				 * 대량의 데이터를 처리할 때 사용
				 * addBatch : 쿼리 추가
				 */
				pstmt.addBatch();
				//KJM : 100개 단위로 커밋
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			//KJM : 커밋되지 못한 나머지 단위들 커밋 / 커밋된 개수 inserted에 넣음
			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch MchtSettleTemp error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
		
		//pys : PG_SETTLE 관련 암호화
		//없어도 될꺼같아서 주석처리
//			for(SharedMap<String, Object> map : mchtSettleTempList) {
//				if(map.getString("account").isEmpty() == false) {
//					String data = map.getString("account");
//					KSignUtil.getInstance().Encrypt(data, "account");
//					map.replace("account", data);
//				}
//			}
	}
		
	//KJM : 대행사 정산 카테고리에 은행 지급(지금 주석처리 되어있음)이라는 메뉴에서 리스트 가져옴
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
	
	//KJM : 가맹점 정산 대상거래 엑셀파일 다운로드(뷰 파일에서 사용되는 기능이 없음)
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
			columns += ", stlSalesFee, stlSalesRate, stlDiffAgencyFee, stlDiffAgencyRate, stlSalesDay, stlSalesId";
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
	
	//KJM : 엑셀 다운로드(사용안함)
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
	
	//KJM : 대표가맹점 정산 정산내역 상세정보
	@RequestMapping(value = "/settle/modal/{stlId}", method = RequestMethod.GET)
	public ModelAndView settleView(HttpServletRequest request, @PathVariable String stlId) {
		request.setAttribute("DATAMAP", new SettleDAO().getById(stlId).getRow(0));
		return new ModelAndView("/settle/modal");
	}
	// KBR 대행사 정산조회 > 보류 ,확정
	//KJM : 대표가맹점의 경우 정산 상태 변경으로 사용 / 가맹점의 경우 settleMcht 메서드 사용..
	@RequestMapping(value = "/settle/status/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		dao.setTable("PG_SETTLE");			// 테이블 셋팅
		dao.setColumns("count(1) as cnt");	// null 포함 모든 값 count
		dao.addWhere("stlId", stlId, DAO.in);	// 전달받은 정산번호와 같은것	

		// payStatus = 지불 상태 
		// status = 확정 상태
		if (status.equals("확정") || status.equals("보류")) { // 확정상태가 확정이거나 보류일 때
			dao.addWhere("status", "대기", DAO.ne);
		} else if (status.equals("대기")) {				   // 확정상태가 대기일 때
			dao.addWhere("payStatus", "대기", DAO.ne);
		} else {
			logger.error("정산 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			return resultMap;
		}
		
		// 선택한 확정상태가 기존값과 틀릴경우 업데이트
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			// stlId = 정산번호 
			// status = 변경 할 확정 상태 (확성 or 보류 or 대기 )
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
	// KBR 대행사 정산 조회 > 선택항목 지급완료처리
	//KJM : 영업대행 정산 지급상태 변경
	@RequestMapping(value = "/settle/paystatus/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settlePayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		
		dao.setTable("PG_SETTLE");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("status", "확정", DAO.ne);	// 확정상태가 확정이 아닌것 
		dao.addWhere("stlId", stlId, DAO.in);	// 정산번호 

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
	
	//KJM : 은행 지급 데이터 다운로드
	@RequestMapping(value = "/settle/export", method = RequestMethod.POST)
	public @ResponseBody Object settleExport(HttpServletRequest request, @RequestParam String bankCd, @RequestParam String stlId) {
		//KJM : 정산번호 로그 확인
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		
		dao.setTable("VW_SETTLE");
		//dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,CONCAT('(정산)',memberName) as memberName, stlId");
		dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,stlAmt,accntHolder,'' as a,'' as b,'사업자' as memberName, stlId");
		//KJM : 정산번호가 같으면서 정산상태가 확정인 것
		dao.addWhere("status", "확정", DAO.eq);
		dao.addWhere("stlId", stlId, DAO.in);
		RecordSet recordSet = dao.search();
		
		if(recordSet.size() < 1) {
			dao.initRecord();
			//KJM : 가맹점 정산 뷰
			dao.setTable("VW_SETTLE_MCHT");
			//KJM : 은행코드, 은행이름, 계좌번호, 정산금액, 예금주, a, b, memberName, 정산번호
			dao.setColumns("bankCd,bankName,REPLACE(account,'-','') as account,(stlAmount + (relsAmt - relsFee - relsVat) + deductAmt) as stlAmt,accntHolder,'' as a,'' as b,'사업자' as memberName, stlId");
			//KJM : 정산번호가 같은 
			dao.addWhere("stlId", stlId, DAO.in);
			recordSet = dao.search();
		}
		
		//KJM : 조회된 리스트가 있을 경우
		if (recordSet.size() > 0) {
			String filePath = "webexport";
			//KJM : 다운로드 받을 파일 경로 세팅
			//KJM : File.separator => 파일구분자(\, /, :)를 대신해서 사용(시스템에 맞는 파일구분자로 저장 됨)
			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
			CPUtil.setTemplateDirectory(filePath);
			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

			CPDocument doc = new CPDocument();

			XlsExport export = new XlsExport(doc);
			String link = "";
			LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
			//KJM : 엑셀파일에 컬럼명 넣어주기 위한 변수(true일 때 컬럼명 넣어줌)
			boolean headerView = false;
			
			//KJM : 은행마다 지급 데이터 양식이 다르므로 다른 컬럼들 세팅
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
			//KJM : title 예시 : "00은행 정산 지급 데이터_오늘날짜"
			doc.title += " 정산 지급 데이터_" + CommonUtil.getCurrentDate("yyMMddhhmmss");
			
			try {
				//KJM : 엑셀파일 정의후 파일 다운로드 링크 생성
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
           		//KJM : 조회된 리스트가 없을 경우 메시지만 반환
		SharedMap<String,Object> map = new SharedMap<String,Object>();
		map.put("message", "다운로드할 내역이 없습니다.");
		return map;
		
		
	}

	// ========================================================== 대표가맹점 정산
	//KJM : 대표가맹점 정산 리스트 조회
	@RequestMapping(value = "/settle/aggregator/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAggregatorList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		//KJM : 로그인 계졍 소속 구분
		SessionUtil.setSearchGrade(request, cpRequest);
		CPDAO dao = new CPDAO();
		//KJM : 대표가맹점 정산 뷰 테이블 사용
		dao.setTable("VW_SETTLE_SUB");
		//KJM : 정산예정일이 가장 늦은 날의 정산예정일, 총 거래금액, 평균수수료, VAT, 총 거래건수, 총 매입취소금액, 총 매입취소수수료, 총 매입취소VAT, 총 매입취소건수, 총 정산금액
		dao.setColumns("stlDay, SUM(payAmt) as payAmt, SUM(payFee) as payFee, SUM(payVat) as payVat, SUM(payCnt) as payCnt, SUM(rfdAmt) as rfdAmt, SUM(rfdFee) as rfdFee, SUM(rfdVat) as rfdVat, SUM(rfdCnt) as rfdCnt, SUM(stlAmt) as stlAmt");
		dao.setWhere("stlDay = (SELECT MAX(stlDay) FROM VW_SETTLE_SUB)");
		CPSession cpSession = SessionUtil.get(request);
		//KJM : 현재 로그인 계정의 소속이 가맹점일 때 가맹점아이디가 같은 조건 추가
		if (cpSession.getGrade().equals("가맹점")) {
			dao.addWhere("mchtId", cpSession.getParentId(), CPDAO.eq);
		}
		
		//KJM : 가장 늦은 정산예정일에 대한 정산 정보 세팅
		request.setAttribute("SUMMAP", dao.search().getRow(0));
		
		//KJM : VW_SETTLE_SUB 
		SettleSubDAO settleDAO = new SettleSubDAO();
		//KJM : 쿼리문의 정렬값 주기 위한 데이터 세팅
		cpRequest.setData("stlDay", "", "", "desc", false);
		cpRequest.setData("dtlName", "", "", "asc", false);
		//KJM : select 쿼리 수행 후 조회된 리스트 경로에 보내줌
		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/aggregator/list", "");
	}
	
	//KJM : 대행사 정산 > 은행지급(주석처리) 리스트 조회
	@RequestMapping(value = "/settle/payoutsub/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView payoutSubList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleSubDAO settleDAO = new SettleSubDAO();
		cpRequest.setData("status", "확정", "eq", "", true);
		cpRequest.setData("payStatus", "대기", "eq", "", true);

		RecordSet rset = settleDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleDAO).setView(request, "/settle/payoutsub/list", "");
	}
	
	//KJM : 대표가맹점 대상거래 엑셀다운로드 ajax 통신
	//KJM : 대표가맹점 정산 대상거래 엑셀파일 다운로드 ajax 통신
	@RequestMapping(value = "/settle/detail/sub", method = RequestMethod.POST)
	public @ResponseBody Object detailSub(HttpServletRequest request, @RequestParam String grade, @RequestParam String stlId) {
		String columns = "capId,trxId,mchtId,name,(SELECT name FROM PG_MCHT_TMN_DTL B WHERE VW_TRX_CAP_SUB.tmnId = B.tmnId) AS tmnName," + "tmnId,trackId,capType,rfdType,rootTrxId,amount,vat,issuer,authCd,trxDay,regTime,stlType,"
						+ "stlAmount, stlRate, stlFee, stlFeeVat, stlDay, stlId ";
		//KJM : 엑셀 파일의 컬럼명들 세팅
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
		//KJM : 대표가맹점 하위터미널의 매입내역 테이블 사용
		dao.setTable("VW_TRX_CAP_SUB");
		dao.setColumns(columns);
		dao.addWhere(grade, stlId, CPDAO.eq);
		
		//KJM : select 쿼리 수행 후 조회 된 리스트 가져옴
		RecordSet recordSet = dao.search();
		//KJM : 조회 된 리스트가 있을 경우
		if (recordSet.size() > 0) {
			String filePath = "webexport";
			//KJM : 다운로드 받을 파일 경로 세팅
			//KJM : File.separator => 파일구분자(\, /, :)를 대신해서 사용(시스템에 맞는 파일구분자로 저장 됨)
			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
			//KJM : 폴더 생성
			CPUtil.setTemplateDirectory(filePath);
			//KJM : 파일 경로에 오늘날짜를 더해서 완성시킨다
			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;
			
			//KJM : doc = 파일 정의(이름, 설명, 작성자)
			CPDocument doc = new CPDocument();
			//KJM : 파일 이름 정의
			doc.title = "정산 대상 거래";
			//KJM : 파일 경로의 폴더 확인 및 생성 및 문서 타이틀,설명,작성자 셋팅
			XlsExport export = new XlsExport(doc);
			String link = "";

			try {
				//KJM : 엑셀파일 생성
				link = export.makeExcel(thead, recordSet, true, true);
			} catch (Exception e) {
				link = e.getMessage();
			}
			
			CPResponse cpResponse = new CPResponse();
			//KJM : 파일과 링크 세팅
			Files file = new Files();
			file.link = link;
			cpResponse.file = file;
			
			//KJM : json형식으로 변환 후 반환
			return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
		}
		//KJM : 조회 된 리스트 없을 경우 메시지만 반환
		SharedMap<String,Object> map = new SharedMap<String,Object>();
		map.put("message", "다운로드할 내역이 없습니다.");
		return map;
	}
	
	//KJM : 대표가맹점 정산내역 상세정보 modal
	//KJM : 대표가맹점 정산 조회 > 정산번호에 대한 상세정보 모달
	@RequestMapping(value = "/settle/modal/sub/{stlId}", method = RequestMethod.GET)
	public ModelAndView settleSubView(HttpServletRequest request, @PathVariable String stlId) {
		request.setAttribute("DATAMAP", new SettleSubDAO().getById(stlId).getRow(0));
		return new ModelAndView("/settle/modal");
	}
	
	//KJM : 대표가맹점 정산 상태 변경(확정) ajax 통신
	//KJM : 대표가맹점 정산 확정 ajax 통신
	@RequestMapping(value = "/settle/status/sub/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleSubStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		//KJM : 정산번호 확인
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		//KJM : 대표가맹점 정산 테이블 사용
		dao.setTable("PG_SETTLE_SUB");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("stlId", stlId, DAO.in);
		
		//KJM : 변경할 정산 상태에 맞게 조건문 세팅(조건에 해당하는 거래는 상태 변경 안됨)
		if (status.equals("확정") || status.equals("보류")) {
			//KJM : 상태 != '대기'
			dao.addWhere("status", "대기", DAO.ne);
		} else if (status.equals("대기")) {
			//KJM : 지급상태 != '대기'
			dao.addWhere("payStatus", "대기", DAO.ne);
		//KJM : 잘못된 선택 시 에러 result 바로 반환
		} else {
			logger.error("정산 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			return resultMap;
		}
		
		//KJM : 조회되는 거래가 없을 경우(건수로 판단)
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			//KJM : 대표가맹점 정산 테이블 수정 후 result 반환
			if (dao.update("UPDATE PG_SETTLE_SUB SET status = '" + status + "' WHERE stlId IN (" + stlId + ")")) {
				resultMap.put("result", "OK");
			//KJM : 테이블 수정 실패 시
			} else {
				resultMap.put("result", "NOK");
				resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");

			}
		//KJM : 조회되는 거래가 있을 경우(대기상태 아닌 거래)
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'대기' 상태가 아닌 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		return resultMap;
	}
	
	//KJM : 대표가맹점 정산 상태 변경(지급완료) ajax 통신
	//KJM : 대표가맹점 정산 지급완료 ajax 통신
	@RequestMapping(value = "/settle/paystatus/sub/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleSubPayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STLID : {}", stlId);
		CPDAO dao = new CPDAO();
		//KJM : 대표가맹점 정산 테이블 사용
		dao.setTable("PG_SETTLE_SUB");
		dao.setColumns("count(1) as cnt");
		//KJM : 상태!='확정'
		dao.addWhere("status", "확정", DAO.ne);
		dao.addWhere("stlId", stlId, DAO.in);
		
		//KJM : 조건에 맞는 리스트가 없다면 정산상태 변경
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			if (dao.update("UPDATE PG_SETTLE_SUB SET payStatus = '" + status + "', payOutDay = '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
				resultMap.put("result", "OK");
			//KJM : 수정 실패 시
			} else {
				resultMap.put("result", "NOK");
			}
		//KJM : 리스트가 있다면 수정 불가, 에러 메시지 보내줌
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "확정 또는 지불 상태를 확인해주세요.");
		}
		return resultMap;
	}
	
	//KJM : 대표가맹점 정산 은행지급 데이터 다운로드(사용안함)
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

	//KJM : 가맹점 매입일 기준 정산 (사용안함)
	@RequestMapping(value = "/settle/calc/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView calcList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		request.setAttribute("SUMMAP", new TrxCapDAO().calcPaySum(cpRequest.data).getRow(0));

		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset = trxCapDAO.calcPayList(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, trxCapDAO).setView(request, "/settle/calc/list", "");
	}

	//KJM : 가맹점 매입일 기준 정산 > 세금계산서 출력(사용안함)
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
	
	
//	@RequestMapping(value = "/settle/mcht/make/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ModelAndView makeSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
//		SessionUtil.setSearchGrade(request, cpRequest);
//		SettleDAO dao = new SettleDAO();
//		StringBuffer query = new StringBuffer();
//		
//		
//		query.append("(SELECT T1.*,IFNULL(T2.relsAmt,0) AS relsAmt,IFNULL(T2.relsFee,0) AS relsFee,IFNULL(T2.relsVat,0) AS relsVat,IFNULL(T2.relsCnt,0) AS relsCnt, ");
//		query.append(" IFNULL(T3.deductAmount,0) as deductAmt, IFNULL(T4.manualRelsAmt,0) as manualRelsAmt, IFNULL(T5.ddctAmt,0) as ddctAmt ");
//		query.append(" FROM (SELECT mchtId, name as mchtName, ceoName,'지급대기' as `status` ,max(stlDay) stlDay, min(stlDay) stlStartDay , min(trxDay) startDay ,max(trxDay) endDay, stlType,");
//		query.append(" SUM(IF(capType ='매입' and risk ='',amount,0)) as payAmt, SUM(IF(capType ='매입' and risk ='',stlFee,0)) as payFee, SUM(IF(capType ='매입' and risk ='',stlFeeVat,0)) as payVat, SUM(IF(capType ='매입' and risk ='' ,1,0)) as payCnt, ");
//		query.append(" SUM(IF(capType ='매입취소',amount,0)) as rfdAmt, SUM(IF(capType ='매입취소',stlFee,0)) as rfdFee, SUM(IF(capType ='매입취소',stlFeeVat,0)) as rfdVat, SUM(IF(capType ='매입취소',1,0)) rfdCnt, ");
//		query.append(" SUM(IF(capType ='매입' and risk !='',amount,0)) as holdAmt, SUM(IF(capType ='매입' and risk !='',stlFee,0)) as holdFee, SUM(IF(capType ='매입' and risk !='',stlFeeVat,0)) as holdVat, SUM(IF(capType ='매입' and risk !='',1,0)) as holdCnt, ");
//		
//		query.append(" SUM(IF(risk ='',stlDistFee,0)) as distFee,");
//		query.append(" SUM(IF(risk ='',stlAgencyFee,0)) as agencyFee,");
//		query.append(" SUM(IF(risk ='',stlVanFee,0)) as vanFee,");
//		query.append(" SUM(IF(risk ='',stlDiffAmt,0)) as diffAmt,");
//		query.append(" SUM(IF(risk ='',benefit,0)) as benefit ,");
//		query.append(" MAX(stlRate) as stlRate,");
//		query.append(" MAX(taxId) as taxId");
//		query.append(" FROM VW_TRX_CAP WHERE stlStatus='정산대기' AND stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
//		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
//			query.append(" AND stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
//		}
//		query.append(" GROUP BY mchtId, stlType ");
//		query.append(" ) AS T1 LEFT OUTER JOIN ");
//		query.append(" ( SELECT B.mchtId ,SUM(B.amount) as relsAmt , SUM(stlFee) as relsFee,  SUM(stlFeeVat) as relsVat , SUM(1) as relsCnt");
//		query.append("   FROM PG_SETTLE_HOLD A, VW_TRX_CAP B  WHERE A.capId = B.capId and A.`status` ='반환요청' AND B.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND B.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
//		query.append(" group by B.mchtId");
//		query.append(" ) AS T2 ON T1.mchtId = T2.mchtId LEFT OUTER JOIN ");
//		query.append(" ( SELECT mchtId,SUM(deductAmount) deductAmount FROM VW_COLLECT_SETTLE_DTL_STATUS WHERE status = '확정' and deductAmount < 0 and deductStlId ='' group by mchtId");
//		query.append(" ) AS T3 ON T1.mchtId = T3.mchtId LEFT OUTER JOIN ");
//		query.append(" ( SELECT mchtId,SUM(ABS(amount)) manualRelsAmt FROM PG_MCHT_DEPOSIT WHERE `depType` ='반환요청' AND stlId = '' AND status = '생성' group by mchtId");
//		query.append(" ) AS T4 ON T1.mchtId = T4.mchtId ");
//		query.append(" LEFT JOIN ( SELECT mchtId, SUM(ddctAmt) AS ddctAmt FROM PG_SETTLE_DDCT WHERE stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND stlStatus = '정산대기' GROUP BY mchtId) T5 ON T1.mchtId = T5.mchtId");
//		query.append(" ) AS C1 LEFT OUTER JOIN PG_MCHT_TAX C2 ON C1.taxId = C2.taxId");
//		
//		dao.setTable(query.toString());
//		dao.setColumns("concat(DATE_FORMAT(now(),'%y%m%d'), substr(uuid(),1,8)) as idx ,C1.*,(payAmt - payFee - payVat) + (rfdAmt - rfdVat - rfdFee) as stlAmount"
//				+ " ,C2.bankCd,C2.bankName,C2.account,C2.accntHolder");
//		dao.setOrderBy("C1.mchtName asc");
//		
//		cpRequest.deleteKeyData("stlEndDay");
//		cpRequest.deleteKeyData("stlStartDay");
//		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
//			cpRequest.deleteKeyData("stlType");
//		}
//		RecordSet rset = dao.search(cpRequest.data);
//		
//		if (cpRequest.type.equalsIgnoreCase("list")) {
//			insertMchtSettleTemp(rset.getRows(), request);
//		}
//		
//		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/make/list", "");
//	}
	
	//KJM : 가맹점 정산 상태변경 ajax 통신
	@RequestMapping(value = "/settle/mcht/make/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMcht(HttpServletRequest request, @PathVariable String status, @RequestBody String idx) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("STATUS: {}, idx: {}", status, idx);
		//KJM : 기본 result 값 세팅
		resultMap.put("result", "NOK");
		resultMap.put("msg", "확정 또는 지불 상태를 확인해주세요.");

		DAO dao = new DAO();
		//KJM : 가맹점 정산 TEMP 테이블 사용
		dao.setTable("PG_SETTLE_MCHT_TEMP");
		dao.setColumns("*");
		//KJM : in연산자의 경우 idx가 여러개일 수 있어 사용
		dao.addWhere("idx", idx, DAO.in);
		
		//KJM : pg_settle_mcht_temp 복호화
//		List<String> keyList = new ArrayList<>();
//		keyList.add("bankCd");
//		keyList.add("bankName");
//		keyList.add("account");
//		keyList.add("accntHolder");
		
		//KJM : 해당 인덱스에 대한 리스트 가져옴
		List<SharedMap<String, Object>> resultList = dao.search().getRows();
		
//		KSignUtil.getInstance().Decrypt(resultList, keyList);
		
		//pys : PG_SETTLE 관련 복호화
		//없어도 될꺼같아서 주석
//		for(SharedMap<String, Object> map : resultList) {
//			if(map.getString("account").isEmpty() == false) {
//				String data = map.getString("account");
//				KSignUtil.getInstance().Encrypt(data, "account");
//				map.replace("account", data);
//			}
//		}
		
		//KJM : 위에서 가져온 리스트에서 반복문 수행
		for (SharedMap<String, Object> eachMap : resultList) {
			//KJM : 이전의 db 쿼리 정보 초기화
			dao.initRecord();
			//KJM : 정산번호 (S000000000000) 생성된 값 받음
			String stlId = TrxDAO.getSettleId();
			//KJM : 가맹점 정산 테이블 사용
			dao.setTable("PG_SETTLE_MCHT");
			dao.setRecord("stlId", stlId);
			dao.setRecord("mchtId", eachMap.getString("mchtId"));			//가맹점아이디
			dao.setRecord("status", eachMap.getString("status"));			//상태
			dao.setRecord("stlDay", eachMap.getString("stlDay"));			//정산예정일
			dao.setRecord("stlStartDay", eachMap.getString("stlStartDay"));	//정산시작일
			dao.setRecord("startDay", eachMap.getString("startDay"));		//정산거래일
			dao.setRecord("endDay", eachMap.getString("endDay"));			//정산거래 종료일
			dao.setRecord("payAmt", eachMap.getLong("payAmt"));				//총 거래금액
			dao.setRecord("payFee", eachMap.getLong("payFee"));				//총 수수료
			dao.setRecord("payVat", eachMap.getLong("payVat"));				//VAT
			dao.setRecord("payCnt", eachMap.getLong("payCnt"));				//거래건수
			dao.setRecord("rfdAmt", eachMap.getLong("rfdAmt"));				//매입취소금액
			dao.setRecord("rfdFee", eachMap.getLong("rfdFee"));				//매입취소 수수료
			dao.setRecord("rfdVat", eachMap.getLong("rfdVat"));				//매입취소 VAT
			dao.setRecord("rfdCnt", eachMap.getLong("rfdCnt"));				//취소 거래건수
			dao.setRecord("holdAmt", eachMap.getLong("holdAmt"));			//지급보류 거래금액
			dao.setRecord("holdFee", eachMap.getLong("holdFee"));			//지급보류 수수료
			dao.setRecord("holdVat", eachMap.getLong("holdVat"));			//지급보류 VAT
			dao.setRecord("holdCnt", eachMap.getLong("holdCnt"));			//지급보류 건수
			dao.setRecord("relsAmt", eachMap.getLong("relsAmt") + eachMap.getLong("manualRelsAmt"));//보류반환거래금액+수기반환요청금액
			dao.setRecord("relsFee", eachMap.getLong("relsFee"));			//보류반환거래금액 수수료
			dao.setRecord("relsVat", eachMap.getLong("relsVat"));			//보류반환거래금액 수수료 VAT
			dao.setRecord("relsCnt", eachMap.getLong("relsCnt"));			//보류반환거래건수
			dao.setRecord("distFee", eachMap.getLong("distFee"));			//입금수수료/입금(대사)수수료
			dao.setRecord("agencyFee", eachMap.getLong("agencyFee"));		//에이전시 수수료/입금(대사)수수료
			dao.setRecord("vanFee", eachMap.getLong("vanFee"));				//van수수료/입금(대사)수수료
			dao.setRecord("diffAmt", eachMap.getLong("diffAmt"));			//영중소차액정산액
			dao.setRecord("benefit", eachMap.getLong("benefit"));			//수익
			dao.setRecord("deductAmt", eachMap.getLong("deductAmt") + eachMap.getLong("manualDeductAmt"));//차감 금액 + 수기차감금액
			dao.setRecord("stlAmount", eachMap.getLong("stlAmount"));		//실지급액
			dao.setRecord("ddctAmt", eachMap.getLong("ddctAmt"));			//차감정산금액
			dao.setRecord("ddctType", eachMap.getString("ddctType"));		//차감정산대상여부
			dao.setRecord("taxId", eachMap.getString("taxId"));				//taxId
			dao.setRecord("bankCd", eachMap.getString("bankCd"));			//은행코드
			dao.setRecord("bankName", eachMap.getString("bankName"));		//은행이름
			dao.setRecord("account", eachMap.getString("account"));			//계좌번호
			dao.setRecord("accntHolder", eachMap.getString("accntHolder"));	//예금주
			dao.setRecord("stlRate", eachMap.getDouble("stlRate"));			//정산수수료율
			dao.setRecord("stlType", eachMap.getString("stlType"));			//정산유형
			dao.setRecord("regId", eachMap.getString("regId"));				//확정자
			dao.setRecord("regDay", eachMap.getString("regDay"));			//등록일
			dao.setRecord("regDate", eachMap.getTimestamp("regDate"));		//등록일시
			
			//KJM : insert 쿼리문 수행
			if (dao.insert()) {
				//KJM : 사용했던 쿼리문 초기화
				dao.initRecord();
				
				//KJM : 해당 가맹점아이디에 입금정산할 내역이 있으면 update
				//KJM : UPDATE 입금정산상세(A), 입금정산(B) 입금번호가 같은것을 기준으로 SET 과입정산아이디=정산번호 WHERE 가맹점아이디가 같고 확정이면서 입금 차액이 0보다 작고 과입정산아이디가 없는것
				if(dao.update("UPDATE PG_COLLECT_SETTLE_DTL A JOIN PG_COLLECT_SETTLE B ON A.collectId = B.collectId SET A.deductStlId ='"+ stlId +"' "
								+ "WHERE A.mchtId = '"+ eachMap.getString("mchtId") +"' AND B.status ='확정' AND A.deductAmount < 0 and A.deductStlId =''")) {
					logger.debug("UPDATE PG_COLLECT_SETTLE_DTL: TRUE");
				}
				
				//KJM : UPDATE 가맹점 예수금 관리 SET 반환된 정산번호=정산번호, 상태=완료 WHERE 가맹점 아이디가 같고 구분이 반환요청이거나 정산차감이면서 반환된 정산번호가 없고 상태가 완료가 아닌것(생성, 폐기)
				if(dao.update("UPDATE PG_MCHT_DEPOSIT SET stlId ='"+ stlId +"', status='완료' WHERE mchtId = '"+ eachMap.getString("mchtId") +"' AND (`depType` ='반환요청' OR `depType` ='정산차감') and stlId = '' AND status != '완료' ")) {
					logger.debug("UPDATE PG_MCHT_DEPOSIT: TRUE");
				}
				
				//KJM : 리스크 있는 거래 리스트 조회
				//KJM : 매입 거래 뷰 (정산번호, 거래번호, 거래유형, 정산상태, 리스크, (리스크가 있고 정산대기인 거래이면'A', 아니면'')=isHoldId)
				dao.setTable("VW_TRX_CAP");
				dao.setColumns("'" + stlId + "' stlId, capId,capType,stlStatus,risk,IF(risk != '' AND stlStatus = '정산대기','A','') isHoldId ");
				//KJM : 정산유형이 있을 경우 WHERE 정산유형 = '정산유형' OR 거래유형이 매입이고 리스크가 있는 거래
				if(!eachMap.isNullOrSpace("stlType")) {
					dao.setWhere(" (stlType = '" + eachMap.getString("stlType") + "' OR (capType ='매입' AND risk != '')) ");
				}
				dao.addWhere("stlDay", eachMap.getString("stlDay"), DAO.le);
				dao.addWhere("stlDay", eachMap.getString("stlStartDay"), DAO.ge);
				dao.addWhere("mchtId", eachMap.getString("mchtId"));
				//KJM : 정산상태 != '정산확정'
				dao.addWhere("stlStatus", "정산확정", DAO.ne);
				List<SharedMap<String, Object>> capList = dao.search().getRows();
				dao.initRecord();
				if (capList.size() > 0) {
					insertMchtSettleIdx(capList);
					
					dao.initRecord();
					//KJM : 지급보류 거래금액이 0보다 클 때
					if (eachMap.getLong("holdAmt") > 0) {
						insertMchtSettleHold(capList, eachMap.getString("regId"), eachMap.getString("regDay"), eachMap.getTimestamp("regDate")); // 순서 1
					}
					dao.initRecord();
					//KJM : 보류반환 거래금액이 0보다 클 때
					if (eachMap.getLong("relsAmt") > 0) {
						//KJM : '반환요청'인 지급보류건에 대해 '반환완료'로 수정
						dao.update("UPDATE PG_SETTLE_HOLD A JOIN PG_TRX_CAP B ON A.capId = B.capId SET A.status = '반환완료', A.stlId ='" + stlId + "' WHERE A.status = '반환요청' AND B.mchtId ='" + eachMap.getString("mchtId") + "'");
					}
					dao.initRecord();
					if(updateMchtSettleCap(capList) != capList.size()) {
						updateMchtSettleCap(capList);
					}
					dao.initRecord();
					//KJM : 정상적으로 수정이 완료되었으면 result : OK 보내줌
					resultMap.put("result", "OK");
				}
				
				// 차감정산 추가
				//KJM : 차감정산 대상인 거래 건 
				if(eachMap.getString("ddctType").equals("Y")) {
					//KJM : 차감정산 스케쥴 테이블 사용
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
		
	//KJM : 가맹점 정산 TEMP 테이블 insert
	//KJM : 지급정산 거래 인덱스 테이블 값 추가
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
				//KJM : 지급보류번호가 없을 때('A') = 리스크가 없는 거래건
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
	
	//KJM : 매입내역 상세 테이블 수정
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
			
			//KJM : 30개씩 처리
			int batchSize = 30;
			int count = 0;

			for (SharedMap<String, Object> map : mchtSettleTempList) {
				int i = 1;
				//KJM : 리스크 없을 때
				if (map.isNullOrSpace("isHoldId")) {
					pstmt.setString(i++, "정산확정");
					pstmt.setString(i++, map.getString("stlId"));
				//KJM : 리스크 있을 때
				} else {
					pstmt.setString(i++, "정산보류");
					pstmt.setString(i++, "");
				}
				//KJM : 매입번호
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
	
	//KJM : 예수금 관리 테이블 insert
	//KJM : 정산 지급보류(예수금) 관리
	
	//KJM : 정산 지급보류 관리
	public void insertMchtSettleHold(List<SharedMap<String, Object>> capList, String regId, String regDay, Timestamp regDate) {
		DAO dao = new DAO();
		for (SharedMap<String, Object> eachMap : capList) {
			//KJM : 리스크 있는 거래건에 대해서
			if (!eachMap.isNullOrSpace("isHoldId")) {
				//KJM : 지급보류(예수금) 관리 테이블
				dao.setTable("PG_SETTLE_HOLD");
				//KJM : getHoldId = S + 현재날짜 + 6자리 자동증가값(000000)
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
	
	//KJM : 지급보류 리스트 조회
	//KJM : 지급 보류 조회 리스트 
	@RequestMapping(value = "/settle/mcht/hold/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView holdList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SettleHoldDAO settleHoldDAO = new SettleHoldDAO();
		//KJM : select 쿼리문 수행 후 조회된 리스트를 지정 경로에 보내줌
		RecordSet rset = settleHoldDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleHoldDAO).setView(request, "/settle/hold/list", "");
	}
	
	//KJM : 지급보류 상태 변경 ajax 통신
	//KJM : 지급보류 상태변경 ajax 통신
	@RequestMapping(value = "/settle/mcht/hold/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMchtHoldStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String holdId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		//KJM : 지급보류 아이디 확인
		logger.debug("holdId : {}", holdId);
		CPDAO dao = new CPDAO();
		//KJM : 정산 지급보류 관리 테이블 사용
		dao.setTable("PG_SETTLE_HOLD");
		dao.setColumns("count(1) as cnt");
		dao.addWhere("holdId", holdId, DAO.in);
		
		//KJM : 사용자가 선택한 상태변경 확인 후 조건문 세팅
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
		
		//KJM : select 쿼리 수행 후 조회된 리스트가 없을 경우 상태변경 진행
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			//KJM : 이전 사용 쿼리문 초기화 후 쿼리문 새로 세팅
			dao.initRecord();
			//KJM : 지급보류 관리 테이블 수정
			if (dao.update("UPDATE PG_SETTLE_HOLD SET status= '" + status + "' WHERE holdId IN (" + holdId + ")")) {
				//KJM : 즉시반환일 경우 추가적으로 메서드 수행
				if(status.equals("즉시반환")) {
					return directSettle(holdId, SessionUtil.getUserId(request));
				} else {
					resultMap.put("result", "OK");
				}
			} else {
				resultMap.put("result", "NOK");
				resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
			}
		//KJM : 조회된 리스트가 있을 경우 상태변경 진행 불가
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'" + status + "' 상태로 변경할 수 없는 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		return resultMap;
	}
	
	//KJM : 지급보류 상태 변경(즉시반환) > 가맹점 정산 테이블 수정
	//KJM : 지급보류 상태변경 > 즉시반환
	private SharedMap<String, Object> directSettle(String holdId, String userId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		resultMap.put("result", "NOK");
		
		DAO dao = new DAO();
		//KJM : 지급보류, 가맹점 TAX 정보 테이블 이용
		dao.setTable("VW_SETTLE_HOLD A JOIN PG_MCHT_TAX B ON A.taxId = B.taxId");
		//KJM : 가맹점 정보, 지급보류 정보(금액, 수수료...)
		dao.setColumns("A.mchtId, SUM(A.amount) as amount, SUM(A.stlAmount) as stlAmount, SUM(A.stlFee) as stlFee, SUM(A.stlFeeVat) as stlFeeVat, COUNT(*) as cnt, "
						+ " MAX(A.taxId) as taxId, SUM(A.distFee) as distFee, SUM(A.agencyFee) as agencyFee, SUM(A.vanFee) as vanFee, SUM(A.benefit) as benefit, MAX(stlRate) as stlRate, "
						+ " min(A.trxDay) startDay ,max(A.trxDay) endDay, "
						+ " B.bankCd,B.bankName,B.account,B.accntHolder");
		dao.addWhere("A.holdId", holdId, DAO.in);
		dao.setGroupBy("A.mchtId");
		dao.setOrderBy("");
		//KJM : select 쿼리문 수행
		RecordSet rset = dao.search();
		if(rset.size() > 1) { // 가맹점이 여러개
			resultMap.put("msg", "하나의 가맹점만 선택할 수 있습니다.");
			return resultMap;
		}
		SharedMap<String, Object> eachMap = rset.getRow(0);

		dao.initRecord();
		//KJM : 정산번호 생성
		String stlId = TrxDAO.getSettleId();
		//KJM : 가맹점 정산 테이블 사용
		//KJM : 가맹점정보와 보류반환 정보로 넣어줌
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
		dao.setRecord("relsAmt", eachMap.getLong("amount"));			//보류반환금액
		dao.setRecord("relsFee", eachMap.getLong("stlFee"));			//보류반환수수료
		dao.setRecord("relsVat", eachMap.getLong("stlFeeVat"));			//보류반환부가세
		dao.setRecord("relsCnt", eachMap.getLong("cnt"));				//보류반환건수
		dao.setRecord("distFee", eachMap.getLong("distFee"));			//대행사수수료
		dao.setRecord("agencyFee", eachMap.getLong("agencyFee"));		//에이전시수수료
		dao.setRecord("vanFee", eachMap.getLong("vanFee"));				//Van수수료
		dao.setRecord("benefit", eachMap.getLong("benefit"));			//본사수익
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
		
		//KJM : insert 쿼리문이 정상적으로 수행 되었을 때
		if (dao.insert()) {
			dao.initRecord();
			//KJM : sql 쿼리문과 경과시간 확인하기위해 디버그 true로 세팅
			dao.setDebug(true);
			//KJM : 해당 매입번호의 매입관련, 지급보류 테이블 사용
			dao.setTable("VW_TRX_CAP A JOIN PG_SETTLE_HOLD B ON A.capId = B.capId");
			dao.setColumns("'" + stlId + "' stlId, A.capId,A.capType,A.stlStatus,A.risk, '' holdId");
			dao.addWhere("B.holdId", holdId, DAO.in);
			dao.setOrderBy("");
			//KJM : select 쿼리문 수행
			List<SharedMap<String, Object>> capList = dao.search().getRows();
			dao.initRecord();
			//KJM : 조회 된 리스트 있을 경우
			if (capList.size() > 0) {
				dao.setDebug(true);
				insertMchtSettleIdx(capList);

				dao.initRecord();
				dao.setDebug(true);
				
				//KJM : 지급보류 관리 테이블의 상태와 반환 정산번호 수정
				if(!dao.update("UPDATE PG_SETTLE_HOLD SET status = '반환완료', stlId = '" + stlId  + "' WHERE holdId IN (" + holdId +")")) {
					resultMap.put("result", "NOK");
					return resultMap;
				}
				dao.initRecord();
				dao.setDebug(true);
				//KJM : insert된 데이터 개수가 다르면
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
	
	//KJM : 가맹점 정산 지급상태 변경 ajax 통신
	//KJM : 가맹점정산 지급 상태변경 ajax 통신
	
	//KJM : 가맹점 정산 조회 지급상태변경 ajax 통신
	@RequestMapping(value = "/settle/mcht/payout/{status}", method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> settleMchtPayout(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
		//KJM : 기능 수행 결과 담을 resultMap 선언
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("stlId : {} => {}", stlId, status);
		CPDAO dao = new CPDAO();
		//KJM : 가맹점 정산 테이블 사용
		dao.setTable("PG_SETTLE_MCHT");
		//KJM : 데이터 갯수 가져오기
		dao.setColumns("count(1) as cnt");
		dao.addWhere("stlId", stlId, DAO.in);
		
		//KJM : 사용자가 선택한 정산기능에 이미 설정돼있는 거래건 찾기위한 조건
		if (status.equals("지급완료")) {
			dao.addWhere("status", "지급완료", DAO.eq);
		} else if (status.equals("지급보류")) {
			dao.addWhere("status", "'지급완료','지급보류'", DAO.in);
		} else if (status.equals("삭제")) {
			dao.addWhere("status", "'지급완료','삭제'", DAO.in);
		} else {
			//KJM : 위의 기능 외의 상태 변경 요청 시 에러 메시지 보내줌
			logger.error("지급 상태 변경 요청 이상 => {}", status);
			resultMap.put("result", "NOK");
			resultMap.put("msg", "지급 상태 변경에 실패하였습니다.");
			return resultMap;
		}
		
		//KJM : 조회된 리스트가 없을 경우 정상적으로 변경 수행
		if (dao.search().getRowFirst().getInt("cnt") == 0) {
			dao.initRecord();
			//KJM : 정산 삭제로 변경 시
			if (status.equals("삭제")) {
				dao.setTable("PG_SETTLE_MCHT");
				dao.addWhere("stlId", stlId, DAO.in);
				//KJM : delete 쿼리문 수행
				if (dao.delete()) {
					resultMap.put("result", "OK");
				} else {
					resultMap.put("result", "NOK");
					resultMap.put("msg", "정산 데이터 삭제에 실패하였습니다.");
				}
			//KJM : 정산 지급완료, 지급보류로 변경 시
			} else {
				//KJM : 가맹점 정산 테이블의 지급상태(완료, 보류)와 실지급일 수정
				if (dao.update("UPDATE PG_SETTLE_MCHT SET status= '" + status + "', payOutDate=CURRENT_TIMESTAMP WHERE stlId IN (" + stlId + ")")) {
					//KJM : 지급완료
					if (status.equals("지급완료")) {
						//KJM : 매입내역상세 테이블의 지급상태와 실지급일 수정
						if(dao.update("UPDATE PG_TRX_CAP_DTL SET stlStatus='정산완료', payOutDay= '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
							//KJM : 반환완료된 매입건에 대한 내용 예수금 관리 테이블에 추가
							new DepositDAO().setAddByMchtSettle(stlId, SessionUtil.getUserId(request));
							dao.initRecord();
							//KJM : 차감정산 스케쥴의 정산여부와 차감정산예정일 수정
							dao.update("UPDATE PG_SETTLE_DDCT set stlStatus = '정산완료', stlDay = '"+CommonUtil.getCurrentDate("yyyyMMdd")+"' WHERE stlId IN ("+stlId+")");
							resultMap.put("result", "OK");
						//KJM : 매입내역상세테이블이 수정안되었다면 다른테이블 수정하지 않고 바로 수행결과만 담음
						}else {
							resultMap.put("result", "OK");
						}
					//KJM : 지급보류
					} else if (status.equals("지급보류")) {
						dao.initRecord();
						//KJM : 차감정산 스케쥴의 정산여부와 정산번호 수정(차감금액 있는 거래 건의 경우 수정)
						dao.update("UPDATE PG_SETTLE_DDCT SET stlStatus = '정산대기', stlId = '' WHERE stlId IN ("+stlId+")");
						resultMap.put("result", "OK");
					} else {
						resultMap.put("msg", "지급 상태 변경에 실패하였습니다(DB).");
					}
				//KJM : 지급완료, 보류 상태 변경 실패 시
				} else {
					resultMap.put("result", "NOK");
					resultMap.put("msg", "지급 상태 변경에 실패하였습니다.");
				}
			}
		//KJM : 조회된 리스트가 있을 경우 상태변경 수행 x
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", "'" + status + "' 상태로 변경할 수 없는 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
		}
		//KJM : 수행결과 보내줌
		return resultMap;
	}
	
	//KJM : 가맹점 정산 > 차감 금액 수정
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
	
	//KJM : 터미널상점 정산 조회 리스트
	// 터미널 정산 추가 - 20190104
	//KJM : 터미널상점 정산 조회 리스트
	@RequestMapping(value = "/settle/tmn/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleTmnList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		//KJM : 로그인 계정 소속 구분
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO dao = new SettleDAO();
		StringBuffer query = new StringBuffer();
		
		//KJM : 가맹점의 하위터미널 별로 나타내야하기 때문에 매출정보의 합계를 구한 값을 보여줌
		query.append(" (SELECT A.mchtId, A.tmnId, max(C.stlDay) AS stlDay, min(C.stlDay) AS stlStartDay, min(C.trxDay) AS startDay, max(C.trxDay) AS endDay, ");
		query.append(" SUM(IF(A.capType = '매입' and B.risk = '',1,0)) AS payCnt, ");				//매입건수
		query.append(" SUM(IF(A.capType = '매입' and B.risk = '',A.amount,0)) AS payAmount, ");	//매입금액
		query.append(" SUM(IF(A.capType = '매입' and B.risk = '',A.vat,0)) AS payVat, ");			//부가세
		query.append(" SUM(IF(A.capType = '매입취소',1,0)) AS rfdCnt, ");							//매입취소건수				
		query.append(" SUM(IF(A.capType = '매입취소',A.amount,0)) AS rfdAmount, ");				//매입취소금액
		query.append(" SUM(IF(A.capType = '매입취소',A.vat,0)) AS rfdVat, ");						//매입취소부가세
		query.append(" SUM(IF(A.capType = '매입' and B.risk != '',1,0)) AS holdCnt, ");			//지급보류건수
		query.append(" SUM(IF(A.capType = '매입' and B.risk != '',A.amount,0)) AS holdAmount, ");	//지급보류금액
		query.append(" SUM(IF(A.capType = '매입' and B.risk != '',A.vat,0)) AS holdVat, ");		//지급보류부가세
		query.append(" SUM(IF(B.risk = '',B.stlDistFee,0)) AS stlDistFee, ");					//대행사수수료
		query.append(" SUM(IF(B.risk = '',B.stlAgencyFee,0)) AS stlAgencyFee, ");				//에이전시수수료
		query.append(" SUM(IF(B.risk = '',B.stlSalesFee,0)) AS stlSalesFee, ");					//지사수수료
		query.append(" SUM(IF(B.risk = '',B.stlAmount,0)) AS stlMchtAmount, ");					//가맹점정산금액
		query.append(" SUM(IF(B.risk = '',C.stlAmount,0)) AS stlTmnAmount, ");					//하위터미널 정산금액
		query.append(" SUM(IF(B.risk = '',B.stlVanFee,0)) AS stlVanFee, ");						//입금수수료
		query.append(" SUM(IF(B.risk = '',B.benefit,0)) AS benefit, ");							//본사수익
		query.append(" MAX(C.stlRate) AS stlRate, MAX(C.stlType) AS stlType ");					//최대값 가맹점정산비율, 정산유형
		//KJM : A(매입내역), B(매입내역상세) 매입번호 기준, 정산예정일자
		query.append(" FROM PG_TRX_CAP A join PG_TRX_CAP_DTL B on A.capId = B.capId and B.stlDay >= '"+cpRequest.getKeyValue("stlStartDay")+"' AND B.stlDay <= '"+cpRequest.getKeyValue("stlEndDay")+"' ");
		//KJM : C(대표가맹점 하위터미널 매입내역) 매입번호 기준
		query.append(" join PG_TRX_CAP_SUB C on A.capId = C.capId ");
		//KJM : 조회조건에 정산주기, 가맹점id, 가맹점명, 터미널상점명 있을 경우에 맞게 세팅
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
			query.append(" AND C.stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("mchtId"))) {
			query.append(" AND C.mchtId LIKE '%"+ cpRequest.getKeyValue("mchtId")+"%'");
		}
		//KJM : D(가맹점지불정산), F(터미널 뷰)
		query.append(" JOIN PG_MCHT_MNG D on A.mchtId = D.mchtId AND D.settleTmnStatus = '사용' GROUP BY A.tmnId) E join VW_MCHT_TMN F on E.tmnId = F.tmnId ");
		
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("mchtName"))) {
			query.append(" AND F.mchtName LIKE '%"+ cpRequest.getKeyValue("mchtName")+"%'");
		}
		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("tmnName"))) {
			query.append(" AND F.dtlName LIKE '%"+ cpRequest.getKeyValue("tmnName")+"%'");
		}
		//KJM : 위에서 세팅한 쿼리문이 테이블자리로 들어가고, 컬럼과 정렬 직접 지정
		dao.setTable(query.toString());
		dao.setColumns(" E.mchtId, F.mchtName AS mchtName, E.tmnId, F.dtlName AS tmnName, '지급대기' AS status,  E.stlDay,  E.stlStartDay, E.startDay, E.endDay, E.payCnt, E.payAmount, E.payVat, E.rfdCnt, E.rfdAmount, E.rfdVat, E.holdCnt, E.holdAmount, E.holdVat, E.stlDistFee, E.stlAgencyFee, E.stlSalesFee, "
						+ " (E.stlMchtAmount - E.stlTmnAmount) AS stlMchtFee, E.stlVanFee, E.benefit, E.stlTmnAmount,  E.stlRate AS stlTmnRate, E.stlType AS stlType, F.dtlBankCd AS tmnBankCd, F.dtlBankName AS tmnBankName, F.dtlAccount AS tmnAccount,  F.dtlAccntHolder AS tmnAccntHolder ");
		dao.setOrderBy("F.mchtName, F.dtlName asc"); //사업자이름, 하위가맹점
		
		//KJM : 조회 조건으로 들어온 정산예정일, 정산주기, 가맹점명, 터미널상점명 데이터값 삭제
		//KJM : 위에 쿼리문 세팅에서 다 사용하였기 때문에(조건문 중복 세팅 방지)
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
		
		//KJM : select 쿼리문 수행 후 조회된 리스트가져옴
		RecordSet rset = dao.search(cpRequest.data);
		//KJM : 조회 된 리스트 지정 경로로 보내줌
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/tmn/list", "");
	}
	
	//KJM : 지급대행 정산 폼 이동
	//KJM : 지급대행 정산 폼 이동
	@RequestMapping(value = "/settle/pisp/form", method = RequestMethod.GET)
	public ModelAndView settleForm(HttpServletRequest request) {
		DAO dao = new DAO();
		//KJM : 지급대행 서비스 거래내역에서 수신일자 가져옴
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
	
	//KJM : 지급대행 정산 리스트 조회
	
	//KJM : 지급대행 정산 리스트 조회
	@RequestMapping(value = "/settle/pisp/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settlePispList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		PispSettleDAO pispStlDAO = new PispSettleDAO();
		//KJM : select 쿼리문 수행 후 조회 된 리스트 지정 경로로 보내줌
		RecordSet rset = pispStlDAO.list(cpRequest.data,cpRequest.page);  
		return new CPRUtil(cpRequest).dataList(rset,pispStlDAO).setView(request,"/settle/pisp/list","");
	}
	
	//KJM : 가맹점 차감정산 조회 폼이동
	//KJM : 가맹점 차감정산 조회 폼 이동
	@RequestMapping(value = "/settle/ddct/form", method = RequestMethod.GET)
	public ModelAndView settleDdctform(HttpServletRequest request, HttpServletResponse response) {
		//KJM : 정기차감항목 리스트 가져옴(단말기 통신비, 유심비, 대금, 기타)
		request.setAttribute("DDCTCODE", new MchtDdctDAO().getCode().getRows());
		return new ModelAndView("/settle/ddct/form");
	}
	
	//KJM : 가맹점 차감정산 조회 리스트
	//KJM : 가맹점 차감정산 조회 리스트
	@RequestMapping(value = "/settle/ddct/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView settleDdctList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		//KJM : VW_SETTLE_DDCT / * / stlDay(차감정산 예정일) desc
		SettleDdctDAO settleDdctDAO = new SettleDdctDAO();
		//KJM : select쿼리문 수행 후 조회 된 리스트를 해당 경로에 보내준다.
		RecordSet rset = settleDdctDAO.list(cpRequest.data,cpRequest.page);  
		return new CPRUtil(cpRequest).dataList(rset,settleDdctDAO).setView(request,"/settle/ddct/list","");
	}
	
	// KBR 대행사 정산조회 > 저장 클릭 시 
	//KJM : 영업대행 정산 실지급액 수정(사용안함)
	@RequestMapping(value = "/settle/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> decide(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		String regId = SessionUtil.getUserId(request);	// 로그인 한 유저 아이디
		String regDay = CommonUtil.getCurrentDate("yyyyMMdd"); // 년월일 8자리로 
		DAO dao = new DAO();
		
		for(SharedMap<String, String> eachMap : requestList) {	// 전달 받은 리스트 전체를 돌면서 값 셋팅 
				
			logger.debug("SETTLE SAVE = stlId: {}", eachMap.getString("stlId"));
			dao.setDebug(true);
			dao.setTable("PG_SETTLE");								 // 테이블 명 셋팅
			dao.setRecord("payOutAmt",eachMap.getLong("payOutAmt")); // 실지급액 
			dao.setRecord("summary",eachMap.getString("summary"));   // 비고
			dao.setRecord("regId",regId);						     // 등록자아이디
			dao.setRecord("regDay",regDay);							 // 등록일
			dao.addWhere("stlId", eachMap.getString("stlId"));		 // 정산번호를 where로 설정
			
			// 업데이트 실패 시 
			if(!dao.update()) {
				resultMap.put("result", "NOK");
    			resultMap.put("msg", eachMap.getString("stlId") + " DB 업데이트에 실패했습니다.");
    			break;
			}
			// Record 초기화
			dao.initRecord();
		}
		// 응답 결과 셋팅
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
	
//	@RequestMapping(value = "/settle/mcht/make/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ModelAndView makeSettleList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
//		SessionUtil.setSearchGrade(request, cpRequest);
//		SettleDAO dao = new SettleDAO();
//		StringBuffer query = new StringBuffer();
//		
//		
//		query.append("(SELECT T1.*,IFNULL(T2.relsAmt,0) AS relsAmt,IFNULL(T2.relsFee,0) AS relsFee,IFNULL(T2.relsVat,0) AS relsVat,IFNULL(T2.relsCnt,0) AS relsCnt, ");
//		query.append(" IFNULL(T3.deductAmount,0) as deductAmt, IFNULL(T4.manualRelsAmt,0) as manualRelsAmt, IFNULL(T5.ddctAmt,0) as ddctAmt ");
//		query.append(" FROM (SELECT mchtId, name as mchtName, ceoName,'지급대기' as `status` ,max(stlDay) stlDay, min(stlDay) stlStartDay , min(trxDay) startDay ,max(trxDay) endDay, stlType,");
//		query.append(" SUM(IF(capType ='매입' and risk ='',amount,0)) as payAmt, SUM(IF(capType ='매입' and risk ='',stlFee,0)) as payFee, SUM(IF(capType ='매입' and risk ='',stlFeeVat,0)) as payVat, SUM(IF(capType ='매입' and risk ='' ,1,0)) as payCnt, ");
//		query.append(" SUM(IF(capType ='매입취소',amount,0)) as rfdAmt, SUM(IF(capType ='매입취소',stlFee,0)) as rfdFee, SUM(IF(capType ='매입취소',stlFeeVat,0)) as rfdVat, SUM(IF(capType ='매입취소',1,0)) rfdCnt, ");
//		query.append(" SUM(IF(capType ='매입' and risk !='',amount,0)) as holdAmt, SUM(IF(capType ='매입' and risk !='',stlFee,0)) as holdFee, SUM(IF(capType ='매입' and risk !='',stlFeeVat,0)) as holdVat, SUM(IF(capType ='매입' and risk !='',1,0)) as holdCnt, ");
//		
//		query.append(" SUM(IF(risk ='',stlDistFee,0)) as distFee,");
//		query.append(" SUM(IF(risk ='',stlAgencyFee,0)) as agencyFee,");
//		query.append(" SUM(IF(risk ='',stlVanFee,0)) as vanFee,");
//		query.append(" SUM(IF(risk ='',stlDiffAmt,0)) as diffAmt,");
//		query.append(" SUM(IF(risk ='',benefit,0)) as benefit ,");
//		query.append(" MAX(stlRate) as stlRate,");
//		query.append(" MAX(taxId) as taxId");
//		query.append(" FROM VW_TRX_CAP WHERE stlStatus='정산대기' AND stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
//		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
//			query.append(" AND stlType = '"+ cpRequest.getKeyValue("stlType")+"'");
//		}
//		query.append(" GROUP BY mchtId, stlType ");
//		query.append(" ) AS T1 LEFT OUTER JOIN ");
//		query.append(" ( SELECT B.mchtId ,SUM(B.amount) as relsAmt , SUM(stlFee) as relsFee,  SUM(stlFeeVat) as relsVat , SUM(1) as relsCnt");
//		query.append("   FROM PG_SETTLE_HOLD A, VW_TRX_CAP B  WHERE A.capId = B.capId and A.`status` ='반환요청' AND B.stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND B.stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' ");
//		query.append(" group by B.mchtId");
//		query.append(" ) AS T2 ON T1.mchtId = T2.mchtId LEFT OUTER JOIN ");
//		query.append(" ( SELECT mchtId,SUM(deductAmount) deductAmount FROM VW_COLLECT_SETTLE_DTL_STATUS WHERE status = '확정' and deductAmount < 0 and deductStlId ='' group by mchtId");
//		query.append(" ) AS T3 ON T1.mchtId = T3.mchtId LEFT OUTER JOIN ");
//		query.append(" ( SELECT mchtId,SUM(ABS(amount)) manualRelsAmt FROM PG_MCHT_DEPOSIT WHERE `depType` ='반환요청' AND stlId = '' AND status = '생성' group by mchtId");
//		query.append(" ) AS T4 ON T1.mchtId = T4.mchtId ");
//		query.append(" LEFT JOIN ( SELECT mchtId, SUM(ddctAmt) AS ddctAmt FROM PG_SETTLE_DDCT WHERE stlDay >='" + cpRequest.getKeyValue("stlStartDay") + "' AND stlDay <='" + cpRequest.getKeyValue("stlEndDay") +"' AND stlStatus = '정산대기' GROUP BY mchtId) T5 ON T1.mchtId = T5.mchtId");
//		query.append(" ) AS C1 LEFT OUTER JOIN PG_MCHT_TAX C2 ON C1.taxId = C2.taxId");
//		
//		dao.setTable(query.toString());
//		dao.setColumns("concat(DATE_FORMAT(now(),'%y%m%d'), substr(uuid(),1,8)) as idx ,C1.*,(payAmt - payFee - payVat) + (rfdAmt - rfdVat - rfdFee) as stlAmount"
//				+ " ,C2.bankCd,C2.bankName,C2.account,C2.accntHolder");
//		dao.setOrderBy("C1.mchtName asc");
//		
//		cpRequest.deleteKeyData("stlEndDay");
//		cpRequest.deleteKeyData("stlStartDay");
//		if(!CommonUtil.isNullOrSpace(cpRequest.getKeyValue("stlType"))) {
//			cpRequest.deleteKeyData("stlType");
//		}
//		RecordSet rset = dao.search(cpRequest.data);
//		
//		if (cpRequest.type.equalsIgnoreCase("list")) {
//			insertMchtSettleTemp(rset.getRows(), request);
//		}
//		
//		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/settle/make/list", "");
//	}
}
