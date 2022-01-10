package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

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
}