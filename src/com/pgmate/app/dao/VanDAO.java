package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;

/**
 * @author Administrator
 *
 */
public class VanDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.VanDAO.class );
	private static final String TABLE = "PG_VAN";
	private static final String COLUMNS = "idx, status, name, van, vanid, cryptokey, secondkey, settleType, apiService, diffSettle, aid, regId, regDay, regDate";
	
	public VanDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(VanDAO.COLUMNS);
	}

	public RecordSet getByIdx(String idx){
		addWhere("lower(idx)",idx,eq);
		return search();
	}

	public RecordSet getByVanId(String vanid){
		addWhere("lower(vanid)",vanid.toLowerCase(),eq);
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
	
	public RecordSet vanList() {
		setColumns("van");
		setGroupBy("van");
		return search();
	}
	
	public RecordSet getVanId(String van) {
		setColumns("vanId, name");
		if(!van.isEmpty()) {
			addWhere("van", van, eq);
		}
		setOrderBy("name asc");
		return search();
	}
	
	public RecordSet danalVanId() {
		setColumns("idx, name,vanId,status");
		addWhere("van", "DANAL", eq);
		setOrderBy("idx asc");
		return search();
	}
	
	
	public RecordSet getVan(String van) {
		setColumns("idx, name,vanId,status");
		addWhere("van", van, eq);
		setOrderBy("idx asc");
		return search();
	}
	
	public RecordSet daouVanId() {
		setColumns("idx, name,vanId,status");
		addWhere("van", "DAOU", eq);
		setOrderBy("idx asc");
		return search();
	}
	
	public RecordSet niceVanId() {
		setColumns("idx, name,vanId,status");
		addWhere("van", "NICE", eq);
		addWhere("status", "예비", eq);
		setOrderBy("idx asc");
		return search();
	}
	
	public boolean updateUsed(String vanIdx) {
		String query = "UPDATE PG_VAN SET status = '사용' WHERE idx = '"+vanIdx+ "'";
		String query2 = "UPDATE PG_VAN SET status = '예비' WHERE status = '사용' AND idx NOT IN (SELECT DISTINCT vanIdx FROM PG_MCHT_TMN)";
		boolean res = new CPDAO().update(query);
		res = new CPDAO().update(query2);
		return res;
	}
}