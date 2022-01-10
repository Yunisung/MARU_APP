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
public class MchtFeeTemplateDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtFeeTemplateDAO.class );
	private static final String TABLE = "PG_MCHT_FEE_TEMPLATE";
	private static final String COLUMNS = "*";
	
	public MchtFeeTemplateDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtFeeTemplateDAO.COLUMNS);
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
	
	public RecordSet getFeeTemplate(String idx) {
		super.setTable("PG_MCHT_FEE_TEMPLATE");
		super.setColumns("*");
		super.addWhere("idx",idx,eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public RecordSet getFeeTemplete() {
		String q = "SELECT * FROM PG_MCHT_FEE_TEMPLATE ORDER BY idx asc";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset;
	}
}