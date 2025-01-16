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
public class TrxPayDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxPayDAO.class );
	private static final String TABLE = "VW_TRX_PAY_LIST";
	private static final String COLUMNS = "*";

	public TrxPayDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxPayDAO.COLUMNS);
		super.setOrderBy("CONCAT(regDay, regTime) desc");
	}
	
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

	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		RecordSet rset =  super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색 
	}

	public RecordSet getProductByTrxId(String trxId) {
		super.setTable("VW_TRX_PAY_PRD");
		super.addWhere("trxId",trxId,eq);
		return search();
	}

	public RecordSet getLoadMap(String vanTrxId) {
		DAO dao = new DAO();
		dao.setDebug(true);
		dao.setTable("PG_TRX_LOAD_KSNET_ONLINE");
		dao.setColumns("*");
		dao.addWhere("transactionNo", vanTrxId, eq);
		RecordSet rset = dao.search();
		dao.initRecord();
		return rset;
	}

}