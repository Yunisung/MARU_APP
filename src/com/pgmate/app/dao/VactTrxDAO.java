package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class VactTrxDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.VactTrxDAO.class );
	private static final String TABLE = "VW_VACT_TRX";
	private static final String COLUMNS = "*";

	public VactTrxDAO() {
		super(TABLE, CPUtil.CP_DEBUG);
		super.setColumns("*");
		super.setOrderBy("regDate desc");
	}

	public RecordSet getById(String vactId){
		addWhere("vactId",vactId,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION
		return super.search();
	}
	
	public RecordSet list(List<Data> datas, Page page) {
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); //DATA to CONDITION
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

	public RecordSet trxSum(List<Data> datas,Page page) {
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.search();	//LIST PAGING 검색 
	}
	
	/**
	 * 블랙리스트 조회
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet blackList(List<Data> datas, Page page) {
		super.setTable("PG_VACT_REG_BLACKLIST a, PG_CODE b");
		super.setColumns("a.idx, a.bankCd, FN_AES_DEC(a.account) AS account, b.codeName as bankNm, a.reason, a.regDate");
		super.setWhere("a.bankCd = b.code and b.alias = 'BANK' and a.useYn = 'Y'");
		super.setOrderBy("regDate desc");
		
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); 
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}
	
	/**
	 * 블랙리스트 삭제 업데이트
	 * @param trxId
	 * @return
	 */
	public boolean updateUseYn(String idx, String regid){		
		this.setTable("PG_VACT_REG_BLACKLIST");
		this.setRecord("useYn", "N");
		this.setRecord("modId", regid);
		this.setRecord("modDate", CommonUtil.getCurrentTimestamp());
		
		this.setWhere("idx = '" + idx + "'");
		
		//업데이트 실행 
		boolean updateChk = super.update();
		
		super.initRecord();
		
		return updateChk;
	}
	
	public boolean isBlackList(String bankCd, String account) {
		this.setTable("PG_VACT_REG_BLACKLIST");
		
		super.addWhere("bankCd", bankCd);
		super.addWhere("account", account);
		super.addWhere("useYn", 'Y');
		
		RecordSet rset = super.search();
		
		super.initRecord();
		if (rset.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 출금계좌정보 조회
	 * @param account
	 * @return
	 */
	public SharedMap<String, Object> withdrawAccount(String account){
		super.setTable("PG_VACT_REG");
		super.setColumns("FN_AES_DEC(withdrawAccount) AS withdrawAccountDec");
		super.addWhere("account",account);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}
}

