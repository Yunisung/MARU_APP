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
public class TrxReqDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxReqDAO.class );
	private static final String TABLE = "PG_TRX_REQ";
	private static final String COLUMNS = "*";

	
	public TrxReqDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxReqDAO.COLUMNS);
	}
	
	public boolean isTrxType(String trxId, String trxType) {
		super.addWhere("trxId", trxId);
		super.addWhere("trxType", trxType);
		RecordSet rset = super.search();
		super.initRecord();
		if (rset.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet getTrxId(String trxId) {
		super.addWhere("trxId", trxId);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}

	public SharedMap<String, Object> getTrxOrgReq(String capId) {
		String q = "select * from ";
		q += "PG_TRX_REQ where trxId = ";
		q += "(select B.trxId from VW_TRX_CAP A left join VW_TRX_CAP B on A.rootTrxId = B.capId where A.capId = '"+capId+"') ";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0);
		
	}
	

}