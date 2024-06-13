package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class TrxIqrDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxIqrDAO.class );
	private static final String TABLE = "VW_TRX_IQR";
	private static final String COLUMNS = "idx,capId,iqrType,telNo,summary,regId,regDay,regDate,mchtId,mchtName,agencyId,agencyName,trxDay,regName";
	
	public TrxIqrDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxIqrDAO.COLUMNS);
	}
	
	public RecordSet getByCapId(String capId){
		addWhere("capId",capId,eq);
		super.setOrderBy("idx desc");
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
	
	public boolean insert(SharedMap<String,Object> map){
		super.setTable("PG_TRX_IQR");
		super.setRecord("capId", map.getString("capId"));
		if(map.containsKey("iqrType")){
			super.setRecord("iqrType", map.getString("iqrType"));
		}else{
			super.setRecord("iqrType", "일반문의");
		}
		super.setRecord("telNo", map.getString("telNo"));
		super.setRecord("summary", CommonUtil.cut(map.getString("summary"),4096));
		super.setRecord("regId", map.getString("regId"));
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		boolean inserted = super.insert();
		return inserted;
	}
	
	
	public boolean insertNormal(String capId,String summary,String telNo,String regId){
		super.setTable("PG_TRX_IQR");
		super.setXssChange(true);
		super.setRecord("capId", capId);
		super.setRecord("iqrType", "일반");
		super.setRecord("telNo", telNo);
		super.setRecord("summary", CommonUtil.cut(summary,4096));
		super.setRecord("regId", regId);
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		boolean inserted = super.insert();
		return inserted;
	}
	
	public boolean insertRisk(String capId,String summary,String regId){
		super.setTable("PG_TRX_IQR");
		super.setRecord("capId", capId);
		super.setRecord("iqrType", "리스크");
		super.setRecord("telNo", "");
		super.setRecord("summary", summary);
		super.setRecord("regId", regId);
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		boolean inserted = super.insert();
		super.initRecord();
		return inserted;
	}
	
	
}