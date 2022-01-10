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
public class CollectDiffKcpDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CollectDiffKcpDAO.class );
	private static final String TABLE = "PG_TRX_DIFF";
	private static final String COLUMNS = "trxDay,mchtId,amount,resultCd,resultMsg";

	public CollectDiffKcpDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CollectDiffKcpDAO.COLUMNS);
		
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	
	public RecordSet list(List<Data> datas,Page page){
		super.setTable("(select a.trxDay,a.recordType,a.mchtId,a.trxId,a.amount,a.resultCd,b.codeName as resultMsg,a.regDate "
						+ "from PG_TRX_DIFF a, PG_CODE b "
						+ "where concat('01', a.resultCd) = b.code) T");
		super.setColumns("T.*");
		super.setWhere("T.recordType = 'F'");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
}