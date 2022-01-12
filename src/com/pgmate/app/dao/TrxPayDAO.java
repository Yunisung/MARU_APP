package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//KJM : 승인거래 내역
public class TrxPayDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxPayDAO.class );
	private static final String TABLE = "VW_TRX_PAY_LIST";
	private static final String COLUMNS = "*";

	public TrxPayDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxPayDAO.COLUMNS);
		//KJM : CONCAT : 문자열 합치기 (regDay + regTime)
		super.setOrderBy("CONCAT(regDay, regTime) desc");
	}
	
	//KJM : 거래번호에 대한 승인거래 정보
	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet isUnknownCardNoTrx(String tid) {
		super.setTable("VW_TRX_PAY");
		super.addWhere("vanTrxId",tid,in);
		super.addWhere("last4","",eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	
	//KJM : 승인내역 리스트 조회
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	//KJM : 매입현황 리스트의 총 금액 합계
	public RecordSet trxSum(List<Data> datas,Page page) {
		//KJM : 금액의 합계를 amount 컬럼명으로 받겠다
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		RecordSet rset =  super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색 
	}

}