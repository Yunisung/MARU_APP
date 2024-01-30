package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.lib.util.lang.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class MchtChargeSettleDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtChargeSettleDAO.class );
	private static final String TABLE = "PG_MCHT_CHARGE_MNG";
	private static final String COLUMNS = "*";

	public MchtChargeSettleDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtChargeSettleDAO.COLUMNS);
	}
	
	public RecordSet getById(String mchtId){
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public SharedMap<String, Object> getBalance(String mchtId){
		super.setTable("PG_MCHT_BALANCE");
		super.setColumns("*");
		super.addWhere("mchtId",mchtId,eq);
		super.setOrderBy("");
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	public boolean updateTransferKey(String transferKey, String regId, String mchtId) {
		this.setXssChange(false);
		this.setTable("PG_MCHT_CHARGE_MNG");
		this.setRecord("transferKey", transferKey);
		this.setRecord("regId", regId);
		this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return super.update();
	}
}