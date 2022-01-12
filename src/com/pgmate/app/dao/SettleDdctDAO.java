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
//KJM : 차감정산 관련 DAO
public class SettleDdctDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.SettleDdctDAO.class );
	private static final String TABLE = "VW_SETTLE_DDCT";
	private static final String COLUMNS = "*";
	private static final String ORDER_BY = "stlDay desc";
	
	public SettleDdctDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(SettleDdctDAO.COLUMNS);
		super.setOrderBy(SettleDdctDAO.ORDER_BY);
	}
	
	public RecordSet getById(String stlId){
		addWhere("stlId", stlId, eq);
		return search();
	}
	
	public RecordSet getByMchtId(String memberId){
		addWhere("mchtId",memberId,eq);
		return search();
	}
	
	public RecordSet getByGrade(String grade){
		addWhere("grade",grade,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	//KJM : 차감정산 리스트 조회
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
}