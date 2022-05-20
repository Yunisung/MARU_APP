package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class RealtimePayOutDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.RealtimePayOutDAO.class );
	private static final String TABLE = "PG_REALTIME_PAYOUT";
	private static final String COLUMNS = "*";

	public RealtimePayOutDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(RealtimePayOutDAO.COLUMNS);
		super.setOrderBy("regDate desc");
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet calcRealTimeList(List<Data> datas) {
		super.setTable("(SELECT A.*, B.name FROM PG_REALTIME_PAYOUT A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A ");
		super.setColumns("sum(amount) as amtSum, sum(stlFee + stlFeeVat) as vatSum, sum(payOutFee + payOutFeeVat) as payOutVatSum, sum(payOutAmount) as payOutAmountSum");

		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 실시간정산 데이터 검색
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("(SELECT A.*, B.name FROM PG_REALTIME_PAYOUT A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A ");
		super.setColumns("A.trxId, A.mchtId,A.name,A.tmnId,A.trackId,A.trxDay,A.trxTime,IF(A.payType = 'C','카드','가상계좌') as payType,A.authCd,A.trxType,A.amount,A.stlFee,A.stlFeeVat,"
				+ "A.stlAmount,A.payOutFee,A.payOutFeeVat,A.bankFee,A.payOutAmount,A.cancelAmount,A.bankCd,A.bankName,FN_MASK_IDENTIFY(A.account) as maskAccount, "
				+ "FN_AES_DEC(A.account) AS account,FN_AES_DEC(A.accntHolder) AS accntHolder,A.payOutDay,A.payOutTime,A.resultCd,A.resultMsg,"
				+ "A.sendCnt,A.sendCheck,A.cancelMemo,A.regId,A.regDate");
		super.setOrderBy("concat(A.payOutDay,A.payOutTime) desc");
		
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 실시간정산 전송횟수 초기화
	 * @param trxId
	 * @return
	 */
	public boolean updateCnt(String trxId){		
		String q = "update PG_REALTIME_PAYOUT"
				+ "	   set sendCnt = '0', resultCd = '9999', resultMsg = '재전송요청', sendCheck = 'N' "
				+ "  where trxId = '" + trxId + "' AND resultCd != '0000' AND sendCnt = 3";
		
		boolean updateChk = super.update(q);
		super.initRecord();
		
		return updateChk;
	}
	
	/**
	 * 실시간정산 취소 입금금액, 메모 업데이트
	 * @param trxId
	 * @param amt
	 * @param memo
	 * @return
	 */
	public boolean cancelUpdate(String trxId,String amt,String memo){	
		super.setTable("PG_REALTIME_PAYOUT");
		super.setRecord("cancelAmount", amt);
		super.setRecord("cancelMemo", CommonUtil.cut(memo,4096));
		super.setWhere("trxId = '" + trxId + "'");
		
		boolean updateChk = super.update();
		super.initRecord();
		
		return updateChk;
	}
	
	/**
	 * 취소메모 검색
	 * @param trxId
	 * @return
	 */
	public String selectMemo(String trxId) {
		String q = "SELECT cancelMemo FROM PG_REALTIME_PAYOUT WHERE trxId = '"+trxId+"'";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("cancelMemo");
	}
	
	public RecordSet calcAutoList(List<Data> datas) {
		super.setTable("(SELECT A.*, B.name, B.distId, B.agencyId, B.salesId FROM PG_SETTLE_AUTO A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A");
		super.setColumns("sum(payAmt+rfdAmt) as amtSum, "
				+ "SUM((payFee+payVat)+(rfdFee+rfdVat)) as vatSum, "
				+ "SUM(IF(payAmt + rfdAmt - (authFee + authFeeVat) = 0, 0, payOutFee + payOutFeeVat)) as payOutVatSum, "
				+ "SUM(IF(authFee + authFeeVat = 0, 0,authFee + authFeeVat)) as authFeeSum, "
				+ "sum(deductAmt) as deductAmtSum, sum(minusAmt) as minusAmtSum, sum(payOutAmount) as payOutAmountSum");

		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 자동정산 데이터 검색
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet autoList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("(SELECT A.*, B.name, B.distId, B.agencyId, B.salesId FROM PG_SETTLE_AUTO A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A ");
		super.setColumns("A.stlId, A.stlType, A.status, A.mchtId, A.name, A.stlDay, A.payType, A.startDay, A.endDay, concat(A.startDay,'~',A.endDay) as payTerm, A.payAmt, A.payFee, A.payVat, A.payCnt, "
				+ "A.distId, A.agencyId, A.salesId, "
				+ "A.rfdAmt, A.rfdFee, A.rfdVat, A.rfdCnt, A.payOutFee, A.payOutFeeVat, A.deductAmt, A.minusAmt, A.payOutAmount, "
				+ "A.bankCd, A.bankName,FN_MASK_IDENTIFY(A.account) as maskAccount, FN_AES_DEC(A.account) AS account,FN_AES_DEC(A.accntHolder) AS accntHolder, "
				+ "A.payCnt + A.rfdCnt as totCnt, A.payAmt + A.rfdAmt as totAmt, (A.payFee + A.payVat) + (A.rfdFee + A.rfdVat) as totalFee, "
				+ "IF(A.payAmt + A.rfdAmt = 0, 0, A.bankFee) as bankFee, "
				+ "authFee + authFeeVat as authFee, "
				+ "IF(A.payAmt + A.rfdAmt = 0, 0, A.payOutFee + A.payOutFeeVat) as totPayOutFee, "
				+ "IF((A.payAmt - A.payFee - A.payVat) + (A.rfdAmt - A.rfdVat - A.rfdFee) - (A.authFee + A.authFeeVat) = 0, 0, 	(A.payAmt - A.payFee - A.payVat) + (A.rfdAmt - A.rfdVat - A.rfdFee) - (A.payOutFee + A.payOutFeeVat) - (A.authFee + A.authFeeVat)) as stlAmount, "
				+ "A.stlRate, A.stlType, A.payOutDay, A.payOutTime, A.sendCnt, A.resultCd, A.resultMsg, A.minusAmtMemo, A.deductAmtMemo, DATE_FORMAT(now(), '%H%i%s') as nowTime");
		super.setOrderBy("A.stlId desc");
		
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 자동정산 차감금액, 차감메모 업데이트
	 * @param stlId
	 * @param amt
	 * @param memo
	 * @return
	 */
	public boolean minusAmtUpdate(String stlId,String amt,String memo,String payOutAmount){	
		super.setTable("PG_SETTLE_AUTO");
		super.setRecord("minusAmt", amt);
		super.setRecord("payOutAmount", payOutAmount);
		super.setRecord("minusAmtMemo", CommonUtil.cut(memo,4096));
		super.setWhere("stlId = '" + stlId + "'");
		
		boolean updateChk = super.update();
		super.initRecord();
		
		return updateChk;
	}
	
	/**
	 * 차감금액 메모 검색
	 * @param stlId
	 * @return
	 */
	public String selectMinusAmtMemo(String stlId) {
		String q = "SELECT minusAmtMemo FROM PG_SETTLE_AUTO WHERE stlId = '"+stlId+"'";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("minusAmtMemo");
	}
	
	/**
	 * 자동정산 예수금, 예수금메모 업데이트
	 * @param stlId
	 * @param amt
	 * @param memo
	 * @return
	 */
	public boolean deductAmtUpdate(String stlId,String amt,String memo,String payOutAmount){	
		super.setTable("PG_SETTLE_AUTO");
		super.setRecord("deductAmt", amt);
		super.setRecord("payOutAmount", payOutAmount);
		super.setRecord("deductAmtMemo", CommonUtil.cut(memo,4096));
		super.setWhere("stlId = '" + stlId + "'");
		
		boolean updateChk = super.update();
		super.initRecord();
		
		return updateChk;
	}
	
	/**
	 * 예수금 메모 검색
	 * @param stlId
	 * @return
	 */
	public String selectDeductAmtMemo(String stlId) {
		String q = "SELECT deductAmtMemo FROM PG_SETTLE_AUTO WHERE stlId = '"+stlId+"'";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("deductAmtMemo");
	}
	
	/**
	 * 자동정산 전송횟수 초기화
	 * @param trxId
	 * @return
	 */
	public boolean updateAutoCnt(String stlId){		
		String q = "update PG_SETTLE_AUTO"
				+ "	   set sendCnt = '0', resultCd = '9999', resultMsg = '재전송요청', status = '지급대기' "
				+ "  where stlId = '" + stlId + "'";
		
		boolean updateChk = super.update(q);
		super.initRecord();
		
		return updateChk;
	}
	
	/**
	 * 자동정산 출금 결과 업데이트
	 * @param stlId : 정산번호
	 * @param payOutDay : 출금일자
	 * @param payOutTime : 출금시간
	 * @param resCd : 출금 결과코드
	 * @param resMsg : 출금 결과메세지
	 * @return
	 */
	public boolean updateAutoPayOutRes(String stlId, String stlStatus, String payOutDay, String payOutTime, String bankCd, String bankName, String account, String accntHolder, String resCd, String resMsg){
		String q = "UPDATE PG_SETTLE_AUTO "
				+ "    SET status='"+stlStatus+"', payOutDay='"+payOutDay+"',payOutTime='"+payOutTime+"',bankCd='"+bankCd+"',bankName='"+bankName+"',account='"+account+"',accntHolder='"+accntHolder+"',"
				+ "        resultCd='"+resCd+"', resultMsg='"+resMsg+"', sendCnt=sendCnt+1"
				+ "	 WHERE stlId = '"+stlId+"'";
		
		boolean updateed =  super.update(q);

		super.initRecord();
		return updateed;
	}
	
	
	/**
	 * 자동정산 출금 결과 매입데이터 업데이트
	 * @param stlId : 정산번호
	 * @param stlDay : 정산예정일자
	 * @param stlType : 정산타입
	 * @param mchtId : 가맹점아이디
	 * @param payOutDay : 출금일자
	 * @return
	 */
	public boolean updateAutoPayOutCapUpdate(String stlId, String stlDay, String stlType, String mchtId, String payOutDay, String stlStatus){
		String q = "UPDATE PG_TRX_CAP_DTL "
				+ "    SET stlId = '" + stlId +"', stlStatus = '" + stlStatus + "', risk = '', payOutDay='"+payOutDay+"'"
				+ "	 WHERE capId in "
				+ "		("
				+ "		 select B.capId "
				+ "		   from PG_TRX_CAP A, PG_TRX_CAP_DTL B" 
				+ "		  where A.capId = B.capId and A.mchtId = '" + mchtId + "' and B.stlDay = '" + stlDay + "' and B.stlType = '" + stlType + "' and B.stlStatus != '정산완료'"
				+ "		)"; 
		
		boolean updateed =  super.update(q);

		super.initRecord();
		return updateed;
	}
	
	/**
	 * 가맹점 아이디로 가맹점 TAX 정보(PG_MCHT_TAX) 테이블 데이터 조회
	 * @param mchtId : 가맹점 아이디
	 * @return
	 */
	public SharedMap<String, Object> getMchtTaxByMchtId(String mchtId) {
		super.setTable("PG_MCHT_TAX");
		super.setColumns("*");
		super.addWhere("mchtId", mchtId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0){
			return rset.getRow(0);
		}else{
			return new SharedMap<String,Object>();
		}
	}
}