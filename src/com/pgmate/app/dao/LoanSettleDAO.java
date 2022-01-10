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

public class LoanSettleDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.LoanSettleDAO.class );
	private static final String TABLE = "VW_LOAN";
	private static final String COLUMNS = "*";
	
	
	public LoanSettleDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(LoanSettleDAO.COLUMNS);
	}
	
	public synchronized String getLoanSettleStlId() {
		return "LS" + getFunction("FN_NEXTVAL2", "LN_SETTLE");
	}

	public SharedMap<String, Object> getSubmitUser(String id) {
		super.setTable("PG_USER");
		super.addWhere("id", id);
		return search().getRow(0);
	}
	
	public SharedMap<String, Object> getMcht(String mchtId) {
		super.setTable("PG_MCHT");
		super.addWhere("mchtId", mchtId);
		return search().getRow(0);
	}
	
	public List<SharedMap<String,Object>> getStlList() {
		super.setTable("PG_LOAN_SETTLE");
		return search().getRows();
	}
	
	public SharedMap<String, Object> getSubmitMng(String grade, String id) {
		if(grade.equals("대행사")) {
			super.setTable("PG_MAM_DIST_MNG");
			super.addWhere("distId", id);
		} else if (grade.equals("에이전시")) {
			super.setTable("PG_MAM_AGENCY_MNG");
			super.addWhere("agencyId", id);
		} else if (grade.equals("지사")) {
			super.setTable("PG_MAM_SALES_MNG");
			super.addWhere("salesId", id);
		} 
		
		return search().getRow(0);
	}
	
	public SharedMap<String, Object> getSubmit(String grade, String id) {
		if(grade.equals("대행사")) {
			super.setTable("PG_MAM_DIST");
			super.addWhere("distId", id);
		} else if (grade.equals("에이전시")) {
			super.setTable("PG_MAM_AGENCY");
			super.addWhere("agencyId", id);
		} else if (grade.equals("지사")) {
			super.setTable("PG_MAM_SALES");
			super.addWhere("salesId", id);
		} 
		
		return search().getRow(0);
	}
	
	public boolean insertLoanStl(SharedMap<String, Object> loanStlData) {
		super.setTable("PG_LOAN_DTL");
		super.setRecord("loanStlId", loanStlData.getString("loanStlId"));
		super.setRecord("loanId", loanStlData.getString("loanId"));
		super.setRecord("distId", loanStlData.getString("distId"));
		super.setRecord("agencyId", loanStlData.getString("agencyId"));
		super.setRecord("salesId", loanStlData.getString("salesId"));
		super.setRecord("loanType", loanStlData.getString("loanType"));
		super.setRecord("submitGrade", loanStlData.getString("submitGrade"));
		super.setRecord("submitId", loanStlData.getString("submitId"));
		super.setRecord("submitName", loanStlData.getString("submitName"));
		super.setRecord("mchtId", loanStlData.getString("mchtId"));
		super.setRecord("trxDay", loanStlData.getString("trxDay"));
		super.setRecord("conCnt", loanStlData.getString("conCnt"));
		super.setRecord("amount", loanStlData.getString("amount"));
		super.setRecord("conAmt", loanStlData.getString("conAmt"));
		super.setRecord("balance", loanStlData.getString("balance"));
		super.setRecord("regId", loanStlData.getString("regId"));
		super.setRecord("regDay", loanStlData.getString("regDay"));
		
		boolean result = super.insert();
		super.initRecord();
		return result;
	}
	
	public boolean insertStl(SharedMap<String, Object> loanStlData, SharedMap<String, Object> submitData,  SharedMap<String, Object> submitMngData, SharedMap<String, Object> eachMap) {
		super.setTable("PG_LOAN_SETTLE");
		super.setRecord("loanStlId", loanStlData.getString("loanStlId"));
		super.setRecord("stlId", loanStlData.getString("stlId"));
		super.setRecord("distId", loanStlData.getString("distId"));
		super.setRecord("agencyId", loanStlData.getString("agencyId"));
		super.setRecord("salesId", loanStlData.getString("salesId"));
		super.setRecord("grade", loanStlData.getString("submitGrade"));
		super.setRecord("memberId", loanStlData.getString("submitId"));
		super.setRecord("name", submitData.getString("name"));
		super.setRecord("ceoName", submitData.getString("ceoName"));
		super.setRecord("payStatus", "지급대기");
		super.setRecord("startDay", eachMap.getString("startDay"));
		super.setRecord("endDay", eachMap.getString("endDay"));
		super.setRecord("payAmt", loanStlData.getLong("payAmt"));
		super.setRecord("payOutDay", eachMap.getInt("stlDay"));
		super.setRecord("bankCd", submitMngData.getString("bankCd"));
		super.setRecord("bankName", submitMngData.getString("bankName"));
		super.setRecord("account", submitMngData.getString("account"));
		super.setRecord("accntHolder", submitMngData.getString("accntHolder"));
		super.setRecord("regId", loanStlData.getString("regId"));
		super.setRecord("regDay", loanStlData.getString("regDay"));
		
		boolean result = super.insert();
		super.initRecord();
		return result;
	}
	
	public RecordSet loanList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		super.setTable("VW_LOAN");
		super.setColumns("*, FN_MASK_IDENTIFY(identity) as maskidentity");
		super.setOrderBy("loanDay desc");
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
		
	}

	public RecordSet loanDtlList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		super.setTable("VW_LOAN_DTL");
		super.setOrderBy("trxDay asc");
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
		
	}
	
	public RecordSet loanStlList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		super.setTable("VW_LOAN_DTL");
		super.addWhere("loanType", "대출실행", ne);
		super.setOrderBy("trxDay asc");
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
		
	}
	
	public RecordSet loanStl(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		super.setTable("PG_LOAN_SETTLE");
//		super.addWhere("loanType", "대출실행", ne);
		super.setOrderBy("payOutDay asc");
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
		
	}
	
	public SharedMap<String, Object> getLoanByMcthId(String mchtId) {
		super.setTable("VW_LOAN");
		super.addWhere("mchtId", mchtId);
		super.addWhere("lastPayDay", "");
		return search().getRow(0);
	}
	
	public SharedMap<String, Object> getBeforeLoanDtl(String mchtId) {
		super.setTable("VW_LOAN_DTL");
		super.addWhere("mchtId", mchtId);
		super.setOrderBy("trxDay desc, regDate desc");
		return search().getRow(0);
	}
	
	public SharedMap<String, Object> getOldDtl(String loanStlId, String loanId) {
		super.setTable("VW_LOAN_DTL");
		super.addWhere("loanId", loanId, eq);
		super.addWhere("loanStlId", loanStlId, ne);
		super.setOrderBy("trxDay desc, regDate desc");
		return search().getRow(0);
	}
	
	public String getBeforeStlDay(String loanId) {
		super.setTable("VW_LOAN_DTL");
		super.addWhere("loanId", loanId);
		super.addWhere("loanType", "중도상환", ne);
		super.setOrderBy("trxDay desc, regDate desc");
		return search().getRow(0).getString("trxDay");
	}
	
	public String getHoliday(String days){
		super.setTable("PG_CODE_HOLIDAY");
		super.addWhere("days", days); 
		super.setOrderBy("");
		return search().getRowFirst().getString("status");
	}
	
	public boolean insertDtl(SharedMap<String,Object> loanStlMap){
		super.setTable("PG_LOAN_DTL");
		super.setRecord("loanStlId", loanStlMap.getString("loanStlId"));
		super.setRecord("loanId", loanStlMap.getString("loanId"));
		super.setRecord("distId", loanStlMap.getString("distId"));
		super.setRecord("agencyId", loanStlMap.getString("agencyId"));
		super.setRecord("salesId", loanStlMap.getString("salesId"));
		super.setRecord("loanType", loanStlMap.getString("loanType"));
		super.setRecord("submitGrade", loanStlMap.getString("submitGrade"));
		super.setRecord("submitId", loanStlMap.getString("submitId"));
		super.setRecord("submitName", loanStlMap.getString("submitName"));
		super.setRecord("mchtId", loanStlMap.getString("mchtId"));
		super.setRecord("stlId", loanStlMap.getString("stlId"));
		super.setRecord("settleType", loanStlMap.getString("settleType"));
		super.setRecord("trxDay", loanStlMap.getString("trxDay"));
		super.setRecord("conCnt", loanStlMap.getLong("conCnt"));
		super.setRecord("amount", loanStlMap.getLong("amount"));
		super.setRecord("conAmt", loanStlMap.getLong("conAmt"));
		super.setRecord("totPayAmt", loanStlMap.getLong("totPayAmt"));
		super.setRecord("prepayAmt", loanStlMap.getLong("prepayAmt"));
		super.setRecord("payAmt", loanStlMap.getLong("payAmt"));
		super.setRecord("delayAmt", loanStlMap.getLong("delayAmt"));
		super.setRecord("holdAmt", loanStlMap.getLong("holdAmt"));
		super.setRecord("balance", loanStlMap.getLong("balance"));
		super.setRecord("paySession", loanStlMap.getInt("paySession"));
		super.setRecord("payCnt", loanStlMap.getInt("payCnt"));
		super.setRecord("delayCnt", loanStlMap.getInt("delayCnt"));
		super.setRecord("payCk", loanStlMap.getString("payCk"));
		super.setRecord("delayCk", loanStlMap.getString("delayCk"));
		super.setRecord("summary", loanStlMap.getString("summary"));
		super.setRecord("regId", loanStlMap.getString("regId"));
		super.setRecord("regDay", loanStlMap.getString("regDay"));
		
		boolean inserted = super.insert();
		logger.info("set trx : {}", inserted);
		super.initRecord();
		return inserted;
	}
	
	public List<SharedMap<String, Object>> getByDtlId(String stlId) {
		super.setTable("VW_LOAN_DTL");
		super.addWhere("stlId", stlId, in);
		return search().getRows();
	}
	
	public SharedMap<String, Object> getByLoanId(String loanId) {
		super.setTable("VW_LOAN");
		super.addWhere("loanId", loanId);
		super.addWhere("lastPayDay", "");
		return search().getRowFirst();
	}

	public void updateLoan(SharedMap<String,Object> data){
		super.setTable("PG_LOAN");
		super.setRecord("lastPayDay", data.getString("lastPayDay"));
		super.setRecord("totPayAmt"	, data.getLong("totPayAmt"));
		super.setRecord("balance"	, data.getLong("balance"));
		super.setRecord("paySession", data.getInt("paySession"));
		super.setRecord("payCnt"	, data.getInt("payCnt"));
		super.setRecord("delayCnt"	, data.getInt("delayCnt"));
		super.addWhere("loanId", data.getString("loanId"));
		super.update();
		super.initRecord();
	}
	
	public void updateLoanStl(SharedMap<String,Object> data){
		super.setTable("PG_LOAN_DTL");
		super.setRecord("trxDay"	, data.getString("trxDay"));
		super.setRecord("conAmt"	, data.getLong("conAmt"));
		super.setRecord("amount"	, data.getLong("amount"));
		super.setRecord("balance"	, data.getLong("balance"));
		super.setRecord("conCnt"	, data.getInt("conCnt"));
		super.setRecord("regId"	, data.getString("regId"));
		super.setRecord("regDay"	, data.getString("regDay"));
		super.addWhere("loanId", data.getString("loanId"));
		super.update();
		super.initRecord();
	}
	
	public void deleteStl(String loanStlId) {
		super.setTable("PG_LOAN_SETTLE");
		super.addWhere("loanStlId", loanStlId, eq);
		super.addWhere("payStatus", "지급완료", ne);
		super.delete();
		super.initRecord();
		
	}
	
	public RecordSet calcSettleList(List<Data> datas) {
		super.setTable("VW_LOAN_DTL");
		super.setColumns("sum(totAmt) as totSum, sum(stlAmount) as stlSum, sum(payAmt+prepayAmt) as paySum");

		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet calcAmountList(List<Data> datas) {
		super.setTable("VW_LOAN_DTL");
		super.setColumns("sum(amount) as amountSum");
		super.addWhere("loanType", "대출실행");

		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet calcSum(List<Data> datas) {
		super.setTable("VW_LOAN");
		super.setColumns("sum(amount) as amtSum, sum(totPayAmt) as paySum, sum(balance) as balSum");
		
		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet getById(String mchtId){
		super.setTable("VW_LOAN");
		super.addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public List<SharedMap<String,Object>> getMchtList(String id){
		super.setTable("VW_LOAN");
		super.addWhere("submitId",id,eq);
		super.addWhere("lastPayDay","", eq);
		return search().getRows();
	}
	
	public List<SharedMap<String,Object>> getAllMchtList(){
		super.setTable("VW_LOAN");
		super.setColumns("*");
		super.addWhere("lastPayDay","", eq);
		return search().getRows();
	}
	
	public RecordSet getLoanList(String mchtId) {
		super.setTable("VW_LOAN");
		super.addWhere("mchtId", mchtId);
		super.addWhere("lastPayDay", "", eq);
		super.setOrderBy("");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	public boolean prepayUpdate(SharedMap<String, Object> requestMap, SharedMap<String, Object> beforeMap) {
		super.setTable("PG_LOAN");
		
		if(requestMap.getString("prepayType").equals("연체차감")) {
			super.setRecord("totPayAmt", beforeMap.getLong("totPayAmt")+beforeMap.getLong("delayAmt"));
			super.setRecord("balance", beforeMap.getLong("balance")-beforeMap.getLong("delayAmt"));
			super.setRecord("delayCnt", 0);
			super.setRecord("payCnt", beforeMap.getInt("payCnt")+requestMap.getInt("delayCnt"));
		} else if(requestMap.getString("prepayType").equals("횟수차감")) {
			super.setRecord("totPayAmt", beforeMap.getLong("totPayAmt")+requestMap.getLong("prepayAmt"));
			super.setRecord("balance", beforeMap.getLong("balance")-requestMap.getLong("prepayAmt"));
			super.setRecord("payCnt", beforeMap.getInt("payCnt")+requestMap.getInt("prepayCnt"));
			super.setRecord("paySession", beforeMap.getInt("paySession")+requestMap.getInt("prepayCnt"));
		} else { // 전액차감
			super.setRecord("lastPayDay", CommonUtil.getCurrentDate("yyyyMMdd"));
			super.setRecord("totPayAmt", beforeMap.getLong("totPayAmt")+requestMap.getLong("prepayAmt"));
			super.setRecord("balance", 0);
			super.setRecord("payCnt", beforeMap.getInt("payCnt")+requestMap.getInt("maxCnt"));
			super.setRecord("paySession", beforeMap.getInt("paySession")+requestMap.getInt("maxCnt"));
		}
		
		super.addWhere("loanId", requestMap.getString("loanId"));
		
		boolean update = super.update();
		super.initRecord();
		return update;
		
		
	}
	
	
	public boolean prepayInsert(SharedMap<String, Object> requestMap, SharedMap<String, Object> beforeMap, String regId) {
		super.setTable("PG_LOAN_DTL");
		super.setRecord("loanStlId", requestMap.getString("loanStlId"));
		super.setRecord("distId", beforeMap.getString("distId"));
		super.setRecord("agencyId", beforeMap.getString("agencyId"));
		super.setRecord("salesId", beforeMap.getString("salesId"));
		super.setRecord("loanId", beforeMap.getString("loanId"));
		super.setRecord("submitGrade", beforeMap.getString("submitGrade"));
		super.setRecord("submitId", beforeMap.getString("submitId"));
		super.setRecord("submitName", beforeMap.getString("submitName"));
		super.setRecord("mchtId", beforeMap.getString("mchtId"));
		super.setRecord("stlId", "");
		super.setRecord("settleType", beforeMap.getString("settleType"));
		super.setRecord("trxDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		super.setRecord("conCnt", beforeMap.getInt("conCnt"));
		super.setRecord("amount", beforeMap.getLong("amount"));
		super.setRecord("conAmt", beforeMap.getLong("conAmt"));
		super.setRecord("payAmt", 0);
		super.setRecord("loanType", "중도상환");
		super.setRecord("totPayAmt", beforeMap.getLong("totPayAmt")+requestMap.getLong("prepayAmt"));
		super.setRecord("prepayAmt", requestMap.getLong("prepayAmt"));
		super.setRecord("delayAmt", 0);
		super.setRecord("balance", beforeMap.getLong("balance")-requestMap.getLong("prepayAmt"));
		
		if(requestMap.getString("prepayType").equals("연체차감")) {
			super.setRecord("delayCnt", 0);
			super.setRecord("payCnt", beforeMap.getInt("payCnt")+beforeMap.getInt("delayCnt"));
			super.setRecord("paySession", beforeMap.getInt("paySession"));
		} else if(requestMap.getString("prepayType").equals("횟수차감")) {
			super.setRecord("delayCnt", 0);
			super.setRecord("payCnt", beforeMap.getInt("payCnt")+requestMap.getInt("prepayCnt"));
			super.setRecord("paySession", beforeMap.getInt("paySession")+requestMap.getInt("prepayCnt"));
		} else { // 전액차감
			super.setRecord("delayCnt", 0);
			super.setRecord("payCnt", beforeMap.getInt("payCnt")+requestMap.getInt("maxCnt"));
			super.setRecord("paySession", beforeMap.getInt("paySession")+requestMap.getInt("maxCnt"));
		}
		
		super.setRecord("payCk", "Y");
		super.setRecord("delayCk", "N");
		super.setRecord("regId", regId);
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		
		boolean insert = super.insert();
		super.initRecord();
		return insert;
		
		
	}
	
	public void updateLoanStatus(String mchtId, String status){
		String q = "UPDATE PG_MCHT_MNG SET loanSettleStatus = '" + status + "' WHERE mchtId='" + mchtId + "'";
		super.update(q);
	}
	


	
}
