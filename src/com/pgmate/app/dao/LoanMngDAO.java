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
public class LoanMngDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.LoanMngDAO.class );
	private static final String TABLE = "PG_LOAN_MNG";
	private static final String COLUMNS = "*";
	
	public LoanMngDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(LoanMngDAO.COLUMNS);
	}
	
	public RecordSet getById(String loanId){
		addWhere("lower(loanId)",loanId.toLowerCase(),eq);
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
}