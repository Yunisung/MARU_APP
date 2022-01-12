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
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class MchtInterestDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtInterestDAO.class );
	private static final String TABLE = "VW_MCHT_INTEREST"; // 상점 무담 무이자 모든 리스트  
	private static final String COLUMNS = "*";
	
	public MchtInterestDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtInterestDAO.COLUMNS);
		super.setOrderBy("acqName asc"); // 매입사 오름차순
	}
	
	public RecordSet getById(String taxId){
		addWhere("lower(taxId)",taxId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getModifyList(String mchtId){
		super.setTable("(SELECT * FROM PG_CODE WHERE alias = 'ACQUIRER') A LEFT JOIN VW_MCHT_INTEREST B ON A.code = B.acquirer and B.mchtId = '"+mchtId+"'");
		super.setColumns("A.codeName AS acqName, A.code AS acquirer, B.mchtId, B.authType, B.m02, B.m03,B.m04,B.m05,B.m06,B.m07,B.m08,B.m09,B.m10,B.m11,B.m12,B.m13,B.m14,B.m15,B.m16,B.m17,B.m18,B.m19,B.m20,B.m21,B.m22,B.m23,B.m24 ");
		super.setOrderBy("acqName asc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
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
	
	public RecordSet acquirerList() {
		super.setTable("PG_CODE");
		super.setColumns("*");
		super.addWhere("alias", "ACQUIRER");
		super.setOrderBy("codeName asc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
}