package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class ChargeSettleNotiDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( ChargeSettleNotiDAO.class );
	private static final String TABLE = "PG_CHARGE_SETTLE_NOTI";
	private static final String COLUMNS = "*";


	public ChargeSettleNotiDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(ChargeSettleNotiDAO.COLUMNS);
	}
	
	public RecordSet getById(String trxId){
		addWhere("trxId", trxId, eq);
		return search();
	}
	

}