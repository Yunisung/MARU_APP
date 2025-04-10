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

	public RecordSet joinList(List<Data> datas, Page page) {
		super.setTable("(SELECT B.fee, B.rate, (B.fee * 0.1) AS feeVat, (A.amount * B.rate) AS rateAmt, TRUNCATE(((A.amount * B.rate) * 0.1),0) rateVat, A.* FROM VW_VACT_TRX A LEFT JOIN PG_MCHT_MNG_VACT B ON A.mchtId = B.mchtId) C");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); //DATA to CONDITION
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

	public RecordSet listWithDecAccount(List<Data> datas, Page page){
		page = CPUtil.correctPage(page);

		super.setTable("(SELECT A.*, FN_AES_DEC(withdrawAccount) as decWithdrawAccount, FN_AES_DEC(holder) as decHolder " +
				"FROM (SELECT V.*, W.withdrawBankName, W.holder, W.withdrawAccount " +
				"FROM VW_VACT_TRX V LEFT JOIN PG_VACT_TRX_WITHDRAW W ON V.vactId = W.vactId ) A) B");
		super.setColumns("B.*");
		super.setOrderBy("regDate DESC");

		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색

	}

	/*public RecordSet listWithDecAccount(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);

		super.setTable(
				"(" +
				"SELECT A.*,\n" +
						"   (SELECT withdrawBankCd\n" +
						"      FROM HT_VACT_REG WHERE trackId != '' AND regDay <= '20230714' AND account = A.account ORDER BY regDate DESC LIMIT 1 ) AS wBankCd,\n" +
						"   (SELECT FN_AES_DEC(withdrawAccount)\n" +
						"      FROM HT_VACT_REG WHERE trackId != '' AND regDay <= '20230714' AND account = A.account ORDER BY regDate DESC LIMIT 1 ) AS wAccount,\n" +
						"   (SELECT holderName\n" +
						"      FROM HT_VACT_REG WHERE trackId != '' AND regDay <= '20230714' AND account = A.account ORDER BY regDate DESC LIMIT 1 ) AS holderName   \n" +
						"  FROM VW_VACT_TRX AS A    \n" +
						" WHERE A.regDay = '20230714' AND A.trxType = '입금'" +
				") AS E");
		super.setColumns("E.*, (SELECT codeName FROM PG_CODE WHERE alias='BANK' AND code=E.wBankCd) as wBankNm");
		super.setOrderBy("E.regDate desc");

		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색

	}*/

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
		//23.06.13 인증수수료 조회 > 가상계좌 모계좌 은행 검색 되도록 쿼리 수정
		super.setTable("(SELECT a.*, b.vactBankCd FROM PG_TOTAL_AUTH a INNER JOIN PG_MCHT_MNG_VACT b ON a.mchtId = b.mchtId) c");
		super.setColumns("c.*");

		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); //DATA to CONDITION
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

	public SharedMap<String,Object> getTotalAuth(String authId) {
		super.setTable("PG_TOTAL_AUTH a, PG_CODE b");
		super.setColumns("FN_AES_DEC(bankAccount) AS withdrawAccountDec, bankCd as withdrawBankCd, b.codeName as withdrawBankNm, holderName,FN_AES_DEC(identity) as identity");
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

	public String getBankName(String bankCd){
		super.setTable("PG_CODE");
		super.setColumns("*");
		super.addWhere("`alias`","BANK", eq);
		super.addWhere("code", bankCd, eq);
		super.setOrderBy("");
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst().getString("codeName");
	}

	public RecordSet getAuthFeeSum(List<Data> datas, String authType, String mchtId) {
		super.setTable("(SELECT a.*, b.vactBankCd FROM PG_TOTAL_AUTH a INNER JOIN PG_MCHT_MNG_VACT b ON a.mchtId = b.mchtId) c");
		super.setColumns("COUNT(c.authId) AS count, SUM(c.authFee) + SUM(c.authFeeVat) AS authFeeSum");
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
		super.setTable("PG_VACT_REG_BLACKLIST");
		super.setColumns("idx, bankCd, FN_AES_DEC(account) AS account, bankName, holderName, FN_AES_DEC(identity) as identity, reason, regDate");
		super.setWhere("useYn = 'Y'");
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

	public boolean isBlackListByIdentity(String holderName, String identity) {
		this.setTable("PG_VACT_REG_BLACKLIST");

		super.addWhere("holderName", holderName);
		super.addWhere("identity", identity);
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
		super.setColumns("FN_AES_DEC(withdrawAccount) AS withdrawAccountDec, withdrawBankCd, b.codeName as withdrawBankNm, holderName, FN_AES_DEC(identity) as identity");
		super.addWhere("a.withdrawBankCd = b.code and b.alias = 'BANK'");
		super.addWhere("account",account);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}

	public int countVactStatusNotiFail(String mchtId) {
		super.setTable("PG_VACT_STATUS_NOTI");
		super.setColumns("COUNT(*) AS cnt");
		super.addWhere("status = '전송실패'");
		super.addWhere("vactAccount IN (SELECT account FROM PG_VACT_DTL WHERE mchtId = '" + mchtId + "')");
		super.addWhere("mchtId", mchtId);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst().getInt("cnt");
	}

	public RecordSet errorList(List<Data> datas, Page page) {
		super.setDebug(true);
//		super.setTable("PG_VACT_IO a, PG_CODE b");
		super.setTable("(SELECT bankCd, b.codeName as bankNm, account, reqData, resultCd, resultMsg, regDate FROM PG_VACT_IO a, PG_CODE b WHERE  a.bankCd = b.code and b.alias = 'BANK' AND a.resultCd != '0000' )A LEFT OUTER JOIN VW_VACT_DTL B ON A.account = B.account");
		super.setColumns("A.*, B.mchtId, B.mchtName");
//		super.setWhere("a.bankCd = b.code and b.alias = 'BANK' AND a.resultCd != '0000'");
		super.setOrderBy("A.regDate desc");

		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

	public List<SharedMap<String, Object>> getUnUsedMAccount(String bankCd) {
		super.setTable("VW_VACT_UNUSED");
		super.setColumns("issuerBank, bankCd, mAccount, COUNT(*) AS cnt");
		super.addWhere("mAccount IN (SELECT DISTINCT(mAccount) FROM PG_VACT WHERE bankCd = '"+ bankCd + "')");
		super.setGroupBy("mAccount");
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRows();
	}

	public RecordSet getVactNotiList(List<Data> datas, Page page) {
		super.setTable("VW_VACT_TRX");
		super.setColumns("*");
		super.setOrderBy("regDate desc");

		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);
	}

	public RecordSet riskTrxSum(List<Data> datas,Page page) {
		super.setTable("PG_VACT_TRX_RISK");
		super.setColumns("SUM(amount) AS amount");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.search();	//LIST PAGING 검색
	}

	public RecordSet riskTrxList(List<Data> datas, Page page){
		page = CPUtil.correctPage(page);
		super.setDebug(true);
		super.setTable("(SELECT B.codeName, C.name as mchtName, FN_AES_DEC(withdrawAccount) AS decWithdrawAccount, A.* FROM PG_VACT_TRX_RISK A LEFT OUTER JOIN PG_CODE B ON A.bankCd=B.code AND B.alias='BANK' " +
				"LEFT OUTER JOIN PG_MCHT C ON A.mchtId=C.mchtId " +
				") D");
		super.setColumns("D.*");
		super.setOrderBy("vactId DESC");

		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색

	}

	public RecordSet riskRegList(List<Data> datas, Page page){
		page = CPUtil.correctPage(page);
		super.setDebug(true);
		super.setTable("(SELECT A.*, FN_AES_DEC(withdrawAccount) AS decWithdrawAccount, (SELECT codeName FROM PG_CODE B WHERE A.bankCd=B.code) AS bankName, " +
				"(SELECT codeName FROM PG_CODE B WHERE A.withdrawBankCd=B.code) AS withdrawBankName FROM PG_VACT_REG_RISK A) C");
		super.setColumns("C.*");
		super.setOrderBy("regDate DESC");

		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색

	}

	public String getAESEnc(String value){
		super.setDebug(true);
		String query = "SELECT FN_AES_ENC('"+value+"') withdrawAccount";
		RecordSet rset = query(query);
		if(rset.size() ==0) {
			return "";
		} else {
			rset.next();
			return rset.getString("withdrawAccount");
		}
	}
}

