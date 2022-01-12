package com.pgmate.app.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.DataSet;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//KJM : 매입내역
@SuppressWarnings("serial")
public class TrxCapDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxCapDAO.class );
	private static final String COLUMNS = "*";
	
	//KJM : 테이블명, 컬럼, 정렬기준 기본 세팅
	public TrxCapDAO() {
//		super(table,CPUtil.CP_DEBUG);
		super.setColumns(TrxCapDAO.COLUMNS);
		//KJM : 수신일자 , 수신시간
		super.setOrderBy("regDay desc, regTime desc");
	}
	
	/**
	 * 21/12/30 추가
	 * KBR : 취소된 모든 금액 검색 
	 * @param trxId
	 * @return
	 */
	public long getRfdAmtBytrxId(String trxId) {
		
		super.setTable("PG_TRX_RFD");
		super.setColumns(" SUM(rfdAmount) as RDFAMT ");
		super.addWhere("rootTrxId", trxId, eq);
		super.addWhere("status", "완료", eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRow(0).getLong("RDFAMT");
		
	}


	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByRootCapId(String capId){
		addWhere("rootTrxId",capId,eq);
		return search();
	}
	
	//KJM : 취소된 매입번호 리스트
	public RecordSet getByRootCapId2(String capId){
		//KJM : VW_TRX_CAP(매입내역) 테이블을 기준으로 PG_TRX_RFD(결제 취소 내역) 테이블을 합친다
		super.setTable("VW_TRX_CAP A left join PG_TRX_RFD B on A.trxId = B.trxId");
		//KJM : 매입내역 *
		super.setColumns("A.*");
		//KJM : 해당 매입거래번호와 결제취소내역 테이블의 거래번호가 일치하는 것만
		super.addWhere("B.rootTrxId = (SELECT trxId FROM VW_TRX_CAP WHERE capId ='" + capId + "')");
		//KJM : select 쿼리문 수행
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	//KJM : 취소된 매입번호 정보
	public RecordSet getByRootTrxId(String trxId){
		//KJM : VW_TRX_CAP(매입내역) 테이블을 기준으로 PG_TRX_RFD(결제 취소 내역) 테이블을 합친다 (거래번호)
		super.setTable("VW_TRX_CAP A left join PG_TRX_RFD B on A.trxId = B.trxId");
		super.setColumns("A.*");
		//KJM : 원거래번호가 거래번호와 같은 컬럼만
		super.addWhere("B.rootTrxId",trxId,eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	//KJM : 해당 매입번호에 대한 매입내역 리스트
	public RecordSet getByCapId(String capId){
		//KJM : capId = 'capId'
		addWhere("capId",capId,eq);
		return search();
	}
	
	public RecordSet getByCapId2(String rootTrxId){
		super.setTable("VW_TRX_CAP");
		super.addWhere("capId='"+rootTrxId+"'");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	// where 가맹점아이디, 터미널아이디, 주문번호, 승인번호, 금액, 거래일자가 일치하는 것
	public RecordSet getBySix(String mchtId, String tmnId, String trackId, String authCd, String amount, String regDay){
		super.setTable("PG_TRX_PAY");
		addWhere("mchtId",mchtId,eq);
		addWhere("tmnId",tmnId,eq);
		addWhere("trackId",trackId,eq);
		addWhere("authCd",authCd,eq);
		addWhere("amount",amount,eq);
		addWhere("regDay",regDay,eq);
		RecordSet rset = super.search();
		return rset;
	}
	
	public DataSet getByMchtId(String mchtId, String trackId, String authCd, String amount, String regDay) {
		super.setTable("PG_TRX_PAY");
		addWhere("mchtId",mchtId,eq);
		addWhere("trackId",trackId,eq);
		addWhere("authCd",authCd,eq);
		addWhere("amount",amount,eq);
		addWhere("regDay",regDay,eq);
		RecordSet rset = super.search();
		return rset;
	}

	public DataSet getByTmnId(String tmnId, String trackId, String authCd, String amount, String regDay) {
		super.setTable("PG_TRX_PAY");
		addWhere("tmnId",tmnId,eq);
		addWhere("trackId",trackId,eq);
		addWhere("authCd",authCd,eq);
		addWhere("amount",amount,eq);
		addWhere("regDay",regDay,eq);
		RecordSet rset = super.search();
		return rset;
	}

	public RecordSet getByTrxId2(String trxId){
		super.setTable("PG_TRX_PAY");
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByTrxIdRfd(String trxId){
		super.setTable("PG_TRX_RFD");
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getBySixRfd(String mchtId, String tmnId, String trackId, String authCd, String amount, String regDay){
		super.setTable("PG_TRX_PAY A left join PG_TRX_RFD B on A.trxId = B.rootTrxId");
		setColumns("A.*");
		addWhere("B.mchtId",mchtId,eq);
		addWhere("B.tmnId",tmnId,eq);
		addWhere("B.trackId",trackId,eq);
		addWhere("B.authCd",authCd,eq);
		//addWhere("B.rootAmount",amount,eq);
		addWhere("B.rfdAmount",amount,eq);
		addWhere("B.regDay",regDay,eq);
		RecordSet rset = super.search();
		return rset;
	}
	
	public DataSet getByMchtIdRfd(String mchtId, String trackId, String authCd, String amount, String regDay) {
		super.setTable("PG_TRX_PAY A left join PG_TRX_RFD B on A.trxId = B.rootTrxId");
		setColumns("A.*");
		addWhere("B.mchtId",mchtId,eq);
		addWhere("B.trackId",trackId,eq);
		addWhere("B.authCd",authCd,eq);
		//addWhere("B.rootAmount",amount,eq);
		addWhere("B.rfdAmount",amount,eq);
		addWhere("B.regDay",regDay,eq);
		RecordSet rset = super.search();
		return rset;
	}

	public DataSet getByTmnIdRfd(String tmnId, String trackId, String authCd, String amount, String regDay) {
		super.setTable("PG_TRX_PAY A left join PG_TRX_RFD B on A.trxId = B.rootTrxId");
		setColumns("A.*");
		addWhere("B.tmnId",tmnId,eq);
		addWhere("B.trackId",trackId,eq);
		addWhere("B.authCd",authCd,eq);
		//addWhere("B.rootAmount",amount,eq);
		addWhere("B.rfdAmount",amount,eq);
		addWhere("B.regDay",regDay,eq);
		RecordSet rset = super.search();
		return rset;
	}

	public RecordSet getByMchtId(String mchtId, long limit){
		addWhere("mchtId",mchtId,eq);
		setLimit(limit);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("mchtId",mchtId,eq);
		return search();
	}
	
	public RecordSet getByTmnId(String tmnId){
		addWhere("tmnId",tmnId,eq);
		setOrderBy("");
		setLimit(1);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	/**
	 * 210809_PYS : 월별 매입내역 조회
	 * <pre>
	 * 매입 시작일부터 현재까지의 개월을 리턴한다.
	 * ex) 21년 1월부터 4월까지 매입이 잡혀있을경우 
	 * {202101, 202102, 202103, 202104} 
	 * </pre>
	 * @return
	 */
	public List<String> salesMonthList(){
		super.setColumns("SUBSTR(MIN(regDay),1,6) as trxMonth");
		super.setTable("PG_TRX_CAP");
		super.setOrderBy("");
		RecordSet rset = super.search();
		String trxMonth = rset.getRow(0).getString("trxMonth");
		String currentMonth = CommonUtil.getCurrentDate("yyyyMM");
		
		List<String> list = new ArrayList<String>();
		if(trxMonth.length() < 1) {
		} else if(trxMonth.equals(currentMonth)){
			list.add(currentMonth);
		}else{
			LocalDate date1 = LocalDate.parse(trxMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
	        LocalDate date2 = LocalDate.parse(currentMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
	        long gap = ChronoUnit.MONTHS.between(date1, date2);
			
	        for(long i=0;i<gap+1 ;i++){
	        	list.add(date1.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyyMM")));
	        }	
		}
		super.initRecord();
		return list;
	}
	
	//KJM : 매입현황조회 리스트 가져옴
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		//KJM : exel, pdf 파일 요청의 경우 page.size = 100000
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet calcPayList(List<Data> datas,Page page) {
		super.setColumns("trxDay, taxId, stlDay, mchtId, name, SUM(amount) AS amount, SUM(vat) AS vat, " +
						"SUM(stlAmount) AS stlAmount, AVG(stlRate) as stlRate, SUM(1) AS stlCount, SUM(stlFee) AS stlFee, SUM(stlFeeVat) AS stlFeeVat, " +
						"SUM(stlDistFee) AS stlDistFee, AVG(stlDistRate) AS stlDistRate," +
						"SUM(stlAgencyFee) AS stlAgencyFee, AVG(stlAgencyRate) AS stlAgencyRate, " +
						"SUM(stlSalesFee) AS stlSalesFee, AVG(stlSalesRate) AS stlSalesRate," +
						"SUM(stlLoanFee) AS stlLoanFee, SUM(benefit) AS benefit");
		super.setGroupBy("taxId, trxDay");
		
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet calcPaySum(List<Data> datas) {
		super.setColumns("SUM(amount) AS amount, SUM(vat) AS vat, " +
						"SUM(stlAmount) AS stlAmount, AVG(stlRate) as stlRate, SUM(1) AS stlCount, SUM(stlFee) AS stlFee, SUM(stlFeeVat) AS stlFeeVat, " +
						"SUM(stlDistFee) AS stlDistFee, AVG(stlDistRate) AS stlDistRate," +
						"SUM(stlAgencyFee) AS stlAgencyFee, AVG(stlAgencyRate) AS stlAgencyRate, " +
						"SUM(stlSalesFee) AS stlSalesFee, AVG(stlSalesRate) AS stlSalesRate," +
						"SUM(stlLoanFee) AS stlLoanFee, SUM(benefit) AS benefit");
		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet calcPayTaxList(List<Data> datas,Page page) {
		super.setColumns("trxDay, taxId, stlDay, mchtId, name, SUM(amount) AS amount, SUM(vat) AS vat, " +
						"SUM(stlAmount) AS stlAmount, AVG(stlRate) as stlRate, SUM(1) AS stlCount, SUM(stlFee) AS stlFee, SUM(stlFeeVat) AS stlFeeVat, " +
						"SUM(stlDistFee) AS stlDistFee, AVG(stlDistRate) AS stlDistRate," +
						"SUM(stlAgencyFee) AS stlAgencyFee, AVG(stlAgencyRate) AS stlAgencyRate, " +
						"SUM(stlSalesFee) AS stlSalesFee, AVG(stlSalesRate) AS stlSalesRate," +
						"SUM(stlLoanFee) AS stlLoanFee, SUM(benefit) AS benefit");
		super.setGroupBy("taxId, trxDay");
		
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	//KJM : 금액 총합계
	public RecordSet trxSum(List<Data> datas,Page page) {
		//KJM : (금액)amount의 합계 결과를 amount 컬럼명으로 받겠다
		super.setColumns("SUM(amount) AS amount");
		super.setOrderBy("");
		super.setLimit(0);
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		RecordSet rset = super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색 
	}
	
	public RecordSet withCollectIdList(List<Data> datas,Page page){
		super.setTable("VW_TRX_CAP A JOIN PG_COLLECT_SETTLE_IDX B ON A.capId = B.capId");
		super.setColumns("A.*, B.collectId as collectId");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet trxSumWithCollectId(List<Data> datas,Page page) {
		super.setTable("VW_TRX_CAP A JOIN PG_COLLECT_SETTLE_IDX B ON A.capId = B.capId");
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet withCollectIdDiffList(List<Data> datas,Page page){
		super.setTable("VW_TRX_CAP A JOIN PG_COLLECT_DIFF_IDX B ON A.capId = B.capId");
		super.setColumns("A.*, B.collectId as collectId");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet trxSumWithCollectIdDiff(List<Data> datas,Page page) {
		super.setTable("VW_TRX_CAP A JOIN PG_COLLECT_DIFF_IDX B ON A.capId = B.capId");
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	

	public RecordSet getByVanId(String vanId){
		super.setTable("PG_VAN");
		addWhere("vanId",vanId,eq);
		super.setOrderBy("");
		return search();
	}

	/**
	 * 거래내역 가맹점 정보 조회
	 * @param mchtId
	 * @param stlDay
	 * @return
	 */
	public RecordSet getTrxMchtList(List<Data> datas,Page page) {
		super.setTable("VW_TRX_CAP");
		super.setColumns("mchtId, name, tmnId, tmnDesc");
		super.addWhere("capId", "", eq);

		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 거래내역 가맹점 정보 조회
	 * @param mchtId
	 * @param stlDay
	 * @return
	 */
	public SharedMap<String, Object> getTrxMchtData(String trxDay, String authCd) {
		super.setTable("VW_TRX_CAP");
		super.setColumns("mchtId, name, tmnId, tmnDesc");
		super.addWhere("trxDay", trxDay, eq);
		super.addWhere("authCd", authCd, eq);
		
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0){
			return rset.getRow(0);
		}else{
			return new SharedMap<String,Object>();
		}
	}


}