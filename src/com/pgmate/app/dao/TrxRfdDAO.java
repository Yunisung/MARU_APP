package com.pgmate.app.dao;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
//KJM : 결제 취소
public class TrxRfdDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxRfdDAO.class );
	private static final String TABLE = "VW_TRX_RFD_LIST";
	private static final String COLUMNS = "*";

	public TrxRfdDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxRfdDAO.COLUMNS);
	}
	
	//KJM : 거래번호에 따른 결제 취소 정보
	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByOrdTrxId(String orgTrxId){
		addWhere("orgTrxId",orgTrxId,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	//KJM : 승인 취소된 내역 리스트 가져옴
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(RfdAmount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		RecordSet rset =  super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색 
	}
}