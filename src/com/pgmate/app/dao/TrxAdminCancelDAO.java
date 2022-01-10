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
public class TrxAdminCancelDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxAdminCancelDAO.class );
	private static final String TABLE = "VW_TRX_ADMIN_RFD";
	private static final String COLUMNS = "*";

	public TrxAdminCancelDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxAdminCancelDAO.COLUMNS);
	}
	

	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
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

}