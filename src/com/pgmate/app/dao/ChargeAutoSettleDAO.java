package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class ChargeAutoSettleDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.ChargeAutoSettleDAO.class );
	private static final String TABLE = "VW_CHARGE_SETTLE_AUTO";
	private static final String COLUMNS = "*";
	
	
	public ChargeAutoSettleDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(ChargeAutoSettleDAO.COLUMNS);
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
	
	public RecordSet sumAmount(List<Data> datas){
		super.setColumns("SUM(stlAmt) as stlAmt");
		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(totAmt) AS amountSum, SUM(stlFee) AS feeSum, SUM(stlAmount) AS stlAmountSum");
		super.setOrderBy("");
		super.setLimit(0);
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		RecordSet rset = super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색 
	}
}