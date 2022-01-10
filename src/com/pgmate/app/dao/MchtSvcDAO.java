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
public class MchtSvcDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtSvcDAO.class );
	private static final String TABLE = "PG_MCHT_SVC";
	private static final String COLUMNS = "*";
	/**
	 * 
	 */
	public MchtSvcDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns("*");
		super.setOrderBy("regDay desc");
	}
	
	public SharedMap<String,Object> getByMchtId(String mchtId){
		super.addWhere("mchtId",mchtId.toLowerCase(),eq);
		return super.search().getRow(0);
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
