package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

public class CodeDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CodeDAO.class );
	private static final String TABLE = "PG_CODE";
	private static final String COLUMNS = "`idx`, `alias`, `code`, `codeName`";
	
	public CodeDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CodeDAO.COLUMNS);
	}
	
	public RecordSet getById(String idx){
		addWhere("lower(idx)",idx.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getBank(){
		addWhere("alias", "BANK", eq);
		setOrderBy("");
		return search();
	}
	
	public String getInfoBankSmsKey() {
		String smsKey = "";
		super.setTable("PG_SMS_TOKEN");
		super.setColumns("*");
		SharedMap<String, Object> map = super.search().getRowFirst();
		smsKey = map.getString("schema") + " " + map.getString("accessToken");
		super.initRecord();
		return smsKey;
	}
	
	/**
	 * 지급대행 엑셀 정상시 응행명에 대한 은행코드 조회
	 * @param bankNm
	 * @return
	 */
	public String getReceiveBankCd(String bankNm){
		String bankCd = "";
		
		super.setColumns("code");
		addWhere("alias", "BANK", eq);
		addWhere("codeName",bankNm, lk);

		SharedMap<String, Object> map = super.search().getRowFirst();
		bankCd = map.getString("codeName");
		super.initRecord();
		
		return bankCd;
	}
	
	/**
	 * 가상계좌 인증 원가 수수료 조회
	 * @param bankNm
	 * @return
	 */
	public RecordSet getOrgFee(String bankNm){
		addWhere("alias", "ORGFEE", eq);
		setOrderBy("");
		return search();
	}
}

