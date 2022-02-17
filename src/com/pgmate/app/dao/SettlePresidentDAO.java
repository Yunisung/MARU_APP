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


// KBR 해당 클래스 사용안한다함 (테이블도 삭제요청)
public class SettlePresidentDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.SettlePresidentDAO.class );
	private static final String TABLE = "PG_SETTLE_PRESIDENT";
	private static final String COLUMNS = "*";
	
	public SettlePresidentDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(SettlePresidentDAO.COLUMNS);
	}
	
	public long insertSettlePresident(SharedMap<String,Object> map){
		super.setRecord("payOutDay"		, map.getString("payOutDay"));
		super.setRecord("amount"		, map.getLong("amount"));
		super.setRecord("bankCd"		, map.getString("bankCd"));
		super.setRecord("bankName"		, map.getString("bankName"));
		super.setRecord("account"		, map.getString("account"));
		super.setRecord("accntHolder"	, map.getString("accntHolder"));
		super.setRecord("resultCd"		, map.getString("resultCd"));
		super.setRecord("resultMsg"		, map.getString("resultMsg"));

		long idx = super.insertAndLastIdx();
		super.initRecord();
		return idx;
	}
}