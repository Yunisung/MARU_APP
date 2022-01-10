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

public class PhoneDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.dao.PhoneDAO.class);
	private static final String TABLE = "VW_PHONE_PAY_LIST";
	private static final String COLUMNS = "*";
	
	public PhoneDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns("*");
		super.setOrderBy("regDate desc");
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("VW_PHONE_PAY_LIST");
		super.setColumns("trxId, mchtId, tmnId, trackId, status, amount, IF(prodType = '1', '실물', '컨텐츠')AS prodType, reqDay, reqTime, van, vanId, vanTrxId, rfdTrxId, rfdRegDay, rfdRegTime,"
				+ "FN_AES_DEC(payerName) AS payerName, FN_AES_DEC(payerEmail) AS payerEmail, FN_AES_DEC(payerTel) AS payerTel, resultCd, resultMsg, regDate,"
				+ "regDay, regTime, name, ceoName, capId, taxId, stlStatus, rfdType,"
				+ "capType, stlDay, IF(stlType = '0', '주정산', '수납정산')AS stlType, stlRate, stlFee, stlFeeVat, stlAmount, stlVanFee, stlVanRate, vat, benefit");
		super.setOrderBy("regDate desc");
		
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
	}
	
	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		RecordSet rset =  super.search();
		super.initRecord();
		return rset;	//LIST PAGING 검색 
	}
	
	public SharedMap<String,Object> getByMchtId(String mchtId){
		super.setTable("PG_MCHT_PHONE_MNG");
		super.addWhere("mchtId",mchtId.toLowerCase(),eq);
		super.setColumns("*");
		super.setOrderBy("regDate desc");
		return super.search().getRow(0);
	}
	
	public boolean modInfo(String modId, String date){
		super.setTable("PG_MCHT_PHONE_MNG");
		super.setRecord("modId", modId);
		super.setRecord("modDay", date);
		return super.update();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		super.setColumns("*");
		return search();
	}
	
	public RecordSet getByRootTrxId(String trxId){
		super.setTable("PG_PHONE_RFD");
		super.setColumns("*");
		super.addWhere("rootTrxId",trxId,eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
}
