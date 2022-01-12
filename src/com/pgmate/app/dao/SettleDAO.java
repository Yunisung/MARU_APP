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
//가맹점 정산
public class SettleDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.SettleDAO.class );
	private static final String TABLE = "VW_SETTLE";
	private static final String COLUMNS = "*";
	
	public SettleDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(SettleDAO.COLUMNS);
	}
	
	public RecordSet getById(String stlId){
		addWhere("stlId", stlId, eq);
		return search();
	}
	
	public RecordSet getByMemberId(String memberId){
		addWhere("memberId",memberId,eq);
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

	public void deleteSettleMchtTemp() {
		super.setTable("PG_SETTLE_MCHT_TEMP");
		super.addWhere("regDay < DATE_FORMAT(date_add(NOW(), interval -2 DAY),'%Y%m%d')");
		super.delete();
		super.initRecord();
		
	}
}