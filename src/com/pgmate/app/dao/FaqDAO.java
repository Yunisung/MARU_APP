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
public class FaqDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.FaqDAO.class );
	private static final String TABLE = "PG_FAQ";
	private static final String COLUMNS = "idx, `status`, `category`, `title`, `pubDay`, `closeDay`, `regId`, `regDay`, `regDate`, `summary`";
	
	public FaqDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(FaqDAO.COLUMNS);
	}
	
	public RecordSet getById(String idx){
		addWhere("lower(idx)",idx.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet searchView(){
		setWhere(" status = '개시' AND pubDay <= DATE_FORMAT(now(),'%Y%m%d') AND closeDay >= DATE_FORMAT(now(),'%Y%m%d') ");
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