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
public class VactSettleMchtDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.VactSettleMchtDAO.class );
	private static final String TABLE = "VW_VACT_SETTLE_MCHT";
	private static final String COLUMNS = "*";
	
	public VactSettleMchtDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(VactSettleMchtDAO.COLUMNS);
	}
	
	public RecordSet getById(String stlId){
		addWhere("stlId", stlId, eq);
		return search();
	}
	
	public RecordSet getByMmchtId(String mchtId){
		addWhere("mchtId",mchtId,eq);
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
	
	public RecordSet sumAmount(List<Data> datas){
		super.setColumns("SUM(stlAmt) as stlAmt");
		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}

	public RecordSet getbyLists(String stlId) {
		addWhere("stlId", stlId, in);
		return search();
	}
	
	public RecordSet getbyChargeSettleLists(String stlId) {
		addWhere("stlId", stlId, in);
		addWhere("settleSvc", "충전정산", eq);
		return search();
	}
}