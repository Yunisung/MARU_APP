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
	 * 인증수수료 관련
	 */
	public RecordSet authList(List<Data> datas, Page page) {
		super.setTable("PG_TOTAL_AUTH");

		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); //DATA to CONDITION
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

	public SharedMap<String,Object> getTotalAuth(String authId) {
		super.setTable("PG_TOTAL_AUTH a, PG_CODE b");
		super.setColumns("FN_AES_DEC(bankAccount) AS withdrawAccountDec, bankCd as withdrawBankCd, b.codeName as withdrawBankNm, holderName");
		super.addWhere("a.bankCd = b.code and b.alias = 'BANK'");
		super.addWhere("authId", authId);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	public RecordSet getVactAuth(String totalAuthId) {
		super.setTable("PG_VACT_AUTH");
		super.setColumns("*");
		super.setWhere("totalAuthId ='"+totalAuthId+"'");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public SharedMap<String,Object> getBankName(String bankCd){
		super.setTable("PG_CODE");
		super.setColumns("*");
		super.addWhere("`alias`","BANK", eq);
		super.addWhere("code", bankCd, eq);
		super.setOrderBy("");
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	public RecordSet getAuthFeeSum(List<Data> datas, String authType, String mchtId) {
		super.setTable("PG_TOTAL_AUTH");
		super.setColumns("COUNT(*) AS count, SUM(authFee) + SUM(authFeeVat) AS authFeeSum");
		CPUtil.setDAO(this, datas);

		if(!authType.equals("")) {
			super.addWhere("authType", authType, eq);
		}

		if(!mchtId.equals("")) {
			super.addWhere("mchtId", mchtId, eq);
		}

		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public boolean updateAuthStlDay(String authId, String stlDay) {
		this.setTable("PG_TOTAL_AUTH");
		this.setRecord("stlDay", stlDay);
		this.setWhere("authId IN (" + authId + ")");

		boolean updated = super.update();

		super.initRecord();

		return updated;
	}

	public boolean updateAuthStlStatus(String authId) {
		this.setTable("PG_TOTAL_AUTH");
		this.setRecord("stlStatus", "정산완료");
		this.setWhere("authId IN (" + authId + ")");

		boolean updated = super.update();

		super.initRecord();

		return updated;
	}

	public boolean updateAuthSummary(String authId, String summary) {
		this.setTable("PG_TOTAL_AUTH");
		this.setRecord("summary", summary);
		this.setWhere("authId IN (" + authId + ")");

		boolean updated = super.update();

		super.initRecord();

		return updated;
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

	public boolean updateBlackReason(String idx, String reason) {
		this.setTable("PG_VACT_REG_BLACKLIST");
		this.setRecord("reason", reason);
		this.setWhere("idx IN (" + idx + ")");

		boolean updated = super.update();

		super.initRecord();

		return updated;
	}

	/**
	 * 출금계좌정보 조회
	 * @param account
	 * @return
	 */
	public SharedMap<String, Object> withdrawAccount(String account){
		super.setTable("HT_VACT_REG a, PG_CODE b");
		super.setColumns("FN_AES_DEC(withdrawAccount) AS withdrawAccountDec, withdrawBankCd, b.codeName as withdrawBankNm, holderName");
		super.addWhere("a.withdrawBankCd = b.code and b.alias = 'BANK'");
		super.addWhere("account",account);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}
}

