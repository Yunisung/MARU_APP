package com.pgmate.app.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
//KJM : 미반영 거래
public class TrxWHFailDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxWHFailDAO.class );
	private static final String TABLE = "VW_TRX_WH_FAIL";
	private static final String COLUMNS = "trxId, tmnId, reqData,trxType, resData, van, vanId, vanTrxId, regDate";

	public TrxWHFailDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxWHFailDAO.COLUMNS);
		super.setOrderBy("regDate desc");
	}

	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByVanTrxId(String vanTrxId){
		setColumns("trxId, tmnId, trxType, reqData, van, vanId, vanTrxId, regDate");
		addWhere("vanTrxId",vanTrxId,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	//KJM : 미반영된 거래 리스트 조회
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	

}