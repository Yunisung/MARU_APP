package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * @author Administrator
 *
 */
public class OthTrnsDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.OthTrnsDAO.class );
	private static final String TABLE = "VW_OTH_PAY_LIST";
	private static final String COLUMNS = "*";

	public OthTrnsDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(OthTrnsDAO.COLUMNS);
		super.setOrderBy("reqDay desc, reqTime desc");
	}
	
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.search();	//LIST PAGING 검색 
	}
	
    	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}

	


}