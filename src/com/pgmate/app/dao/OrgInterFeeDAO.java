package com.pgmate.app.dao;

import java.util.ArrayList;
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
public class OrgInterFeeDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.OrgInterFeeDAO.class );
	private static final String TABLE = "PG_ORG_FEE_INTEREST";
	private static final String COLUMNS = "*";
	
	public OrgInterFeeDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(OrgInterFeeDAO.COLUMNS);
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
	
	public List<SharedMap<String, Object>> vanList(){
		super.setColumns("van");
		super.setTable("PG_ORG_FEE_INTEREST");
		super.setOrderBy("van asc");
		RecordSet rset = super.search();
		
		
		List<SharedMap<String, Object>> list = new ArrayList<SharedMap<String, Object>>();
		for(SharedMap<String, Object> map:rset.getRows()) {
			list.add(map);
		}
		super.initRecord();
		return list;
	}
}