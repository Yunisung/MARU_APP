package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class MchtTaxDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtTaxDAO.class );
	private static final String TABLE = "PG_MCHT_TAX";
	private static final String COLUMNS = "taxId, mchtId, name, compName, ceoName, idType, FN_MASK_IDENTIFY(identity) as identity, taxStatus, taxLimit, bankCd, bankName, account, accntHolder, accntCheck, accntDate, zip, addr1, addr2, email,regId, regDay, regDate";
	
	public MchtTaxDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtTaxDAO.COLUMNS);
	}
	
	public RecordSet getById(String taxId){
		addWhere("lower(taxId)",taxId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public List<SharedMap<String,Object>> getSelectOption(String mchtId){
		String q = "SELECT taxId as id ,name FROM PG_MCHT_TAX WHERE mchtId = '" + mchtId + "' ORDER BY name asc" ;
		return super.query(q).getRows();
	}
	
	public boolean insertDefault(String mchtId, String taxId, String email, String bankName, String bankCd, String account, String accntHolder) {
		String query = "INSERT INTO PG_MCHT_TAX	(`taxId`, `mchtId`, `name`, `compName`, `ceoName`, `idType`, `identity`, `taxStatus`, `taxLimit`, `bankCd`, "
						+ "`bankName`, `account`, `accntHolder`, `accntCheck`, `zip`, `addr1`, `addr2`, `email`, `regId`, `regDay`, `regDate`) "
						+ "SELECT '"+ taxId +"',A.mchtId,'기본',IF(A.idType = '주민번호', A.ceoName, A.name),A.ceoName,A.idType,A.identity,'사용', IF(A.idType = '주민번호', 24000000, 0),"
						+ "'"+bankCd+"','"+bankName+"','"+account+"','"+accntHolder+"','아니오',zip,addr1,addr2,'"+ email +"',A.regId,A.regDay,A.regDate "
						+ "FROM PG_MCHT A ,PG_MCHT_MNG B "
						+ "WHERE A.mchtId = B.mchtId AND A.mchtId = '"+ mchtId +"'";
		return new CPDAO().update(query);
	}
	
	public RecordSet getWithUsedLimit(String mchtId){
		super.setTable("VW_MCHT_TAX");
		super.setColumns("usedAmt, lastDate, taxLimit, taxId, mchtId, `name`, taxStatus, bankCd, bankName, account, accntHolder, accntCheck, accntDate, regDay");
		super.addWhere("mchtId", mchtId, DAO.eq);
		super.setOrderBy("regDate DESC, taxStatus DESC");
		return super.search();
	}
	
	public RecordSet getWithUsedLimit2(String mchtId){
		super.setTable("PG_MCHT_TAX");
		super.setColumns("*");
		super.addWhere("mchtId", mchtId, DAO.eq);
		super.setOrderBy("regDate DESC, taxStatus DESC");
		return super.search();
	}
	
	public RecordSet getHtByMchtId(String mchtId){
		setTable("HT_MCHT_TAX");
		setColumns("*");
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
}