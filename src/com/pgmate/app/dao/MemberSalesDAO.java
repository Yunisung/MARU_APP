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
public class MemberSalesDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MemberSalesDAO.class );
	private static final String TABLE = "VW_MAM_SALES";
	private static final String COLUMNS = "salesId, agencyId, name, bizType, bizCategory, idType, FN_MASK_IDENTIFY(identity) as identity, status, email, tel1, tel2, fax, zip, addr1, addr2, lat, lng, ceoName, FN_MASK_IDENTIFY(ceoIdentity) as ceoIdentity, ceoPhone, ceoTel, ceoZip, ceoAddr1, ceoAddr2, managerName, managerPhone, regId, regDay, regDate, payStatus, agencyName, bankCd, bankName, account, accntHolder";
	
	public MemberSalesDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MemberSalesDAO.COLUMNS);
	}
	
	public RecordSet getById(String salesId){
		addWhere("lower(salesId)",salesId.toLowerCase(),eq);
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
	
	public List<SharedMap<String,Object>> getSelectOption(){
		String q = "SELECT salesId as id ,name FROM VW_MAM_SALES ORDER BY name asc" ;
		return super.query(q).getRows();
	}
	
	public SharedMap<String, Object> getParentsId(String salesId) {
		String q = "SELECT B.distId, A.agencyId, A.salesId FROM PG_MAM_SALES as A, PG_MAM_AGENCY as B WHERE A.agencyId = B.agencyId AND salesId = '"+salesId+"'";
		return super.query(q).getRowFirst();
	}
	
	public boolean insertDefault(String agencyId) {
		String query = "INSERT INTO PG_MAM_SALES (`salesId`,`agencyId`,`name`,`status`,`email`,`tel1`,`tel2`,`regId`,`regDay`,`regDate`) "
					 + "SELECT FN_GET_SALES_ID(), agencyId, '기본', status, email, tel1, tel2, regId, regDay, regDate "
					 + "FROM PG_MAM_AGENCY WHERE agencyId = '"+agencyId+"'";
		
		return new CPDAO().update(query);
	}
	
	public RecordSet getSalesMngByMchtId(String mchtId){
		String query = "select A.* from PG_MAM_SALES_MNG A left Join PG_MCHT B on A.salesId = B.salesId WHERE B.mchtId = '"+mchtId+"'";
		return super.query(query);
	}
}