package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.FitcollaboResult;
import com.pgmate.pay.bean.Response;
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
public class TrxRfdDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxRfdDAO.class );
	private static final String TABLE = "VW_TRX_RFD_LIST";
	private static final String COLUMNS = "*";

	public TrxRfdDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxRfdDAO.COLUMNS);
	}

	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByOrdTrxId(String orgTrxId){
		addWhere("orgTrxId",orgTrxId,eq);
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
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(RfdAmount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		RecordSet rset =  super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색
	}

	public RecordSet getByRootTrxId(String trxId) {
		super.setTable("PG_TRX_RFD");
		addWhere("rootTrxId", trxId);
		return super.search();
	}

	public boolean insertTrxAdminRfd(SharedMap<String, Object> trxMap, FitcollaboResult resp, String regId) {
		super.setTable("PG_TRX_ADMIN_RFD");
		super.setRecord("orgTrxId", trxMap.getString("trxId"));
		super.setRecord("vanTrxId", trxMap.getString("vanTrxId"));
		super.setRecord("vanId", trxMap.getString("vanId"));
		super.setRecord("vanResultCd", resp.resultCode);
		super.setRecord("vanResultMsg", resp.resultMessage);
		super.setRecord("amount", -Long.parseLong(trxMap.getString("amount")));
		super.setRecord("resultCd", resp.resultCode);
		super.setRecord("trxId", "");
		super.setRecord("regId", regId);
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		super.setRecord("regDate", CommonUtil.getCurrentTimestamp());

		boolean insert = super.insert();
		logger.info("set PG_TRX_ADMIN_RFD insert : {}", insert);
		super.initRecord();
		return insert;
	}
}