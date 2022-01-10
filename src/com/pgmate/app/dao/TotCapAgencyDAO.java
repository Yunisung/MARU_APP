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
public class TotCapAgencyDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TotCapAgencyDAO.class );
	private static final String TABLE = "VW_TOT_CAP_AGENCY";
	private static final String COLUMNS = "*";
	private static final String SUM_COLUMNS = "SUM(payAmt) as payAmt,SUM(payCnt) as payCnt,SUM(rfdAmt) as rfdAmt,SUM(rfdCnt) as rfdCnt,SUM(totalAmt) as totalAmt,SUM(totalCnt) as totalCnt,SUM(stlAmt) as stlAmt,SUM(stlFee) as stlFee,SUM(stlAgencyFee) as stlAgencyFee";
												

	public TotCapAgencyDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TotCapAgencyDAO.COLUMNS);
	}
	
	public RecordSet salesSum(List<Data> datas){
		Page page = new Page();
		page.size = 999999999;
		super.setColumns(SUM_COLUMNS);
		super.setOrderBy("");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	public RecordSet salesAgencyMonthlyList(List<Data> datas,Page page){
		super.setColumns(COLUMNS);
		super.setOrderBy("capMonth desc, agencyName asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
}