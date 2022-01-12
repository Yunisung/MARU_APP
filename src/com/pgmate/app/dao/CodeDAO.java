package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

/**
 * 210812_PYS : 은행코드조회
 * @author pys
 *
 */
public class CodeDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CodeDAO.class );
	private static final String TABLE = "PG_CODE"; // CODE 정의 테이블
	private static final String COLUMNS = "`idx`, `alias`, `code`, `codeName`";
	
	public CodeDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CodeDAO.COLUMNS);
	}
	
	/**
	 * 210812_PYS : idx값으로 은행 코드 조회
	 * <pre>
	 * SELECT * FROM PG_CODE WHERE idx = 'idx'
	 * </pre>
	 * @param idx
	 * @return
	 */
	public RecordSet getById(String idx){
		addWhere("lower(idx)",idx.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * 210812_PYS : 별칭이 은행인것만 조회
	 * <pre>
	 * SELECT * FROM PG_CODE WHERE alias = 'BANK'
	 * </pre>
	 * @return
	 */
	public RecordSet getBank(){
		addWhere("alias", "BANK", eq);
		setOrderBy("");
		return search();
	}
	
	/**
	 * 210812_PYS : 사용안함, DB에도 데이터 없음, 테이블정의서에도 설명이 없음
	 * @return
	 */
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
}

