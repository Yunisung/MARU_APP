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
public class AgencyDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.AgencyDAO.class );
	private static final String TABLE = "VW_MAM_AGENCY";
	private static final String COLUMNS = "agencyId, name, `status`, bizType, bizCategory, distId, idType, FN_MASK_IDENTIFY(identity) as identity, email, tel1, tel2, fax, zip, addr1, addr2, lat, lng, ceoName, FN_MASK_IDENTIFY(ceoIdentity) as ceoIdentity, ceoPhone, ceoTel, ceoZip, ceoAddr1, ceoAddr2, managerName, managerPhone, regId, regDay, regDate, payStatus, distName, bankCd, bankName, account, accntHolder";
	
	public AgencyDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(AgencyDAO.COLUMNS);
	}
	
	/**
	 * 210812_PYS : VW_MAM_AGENCY에서 해당 ID 같은 에이전시의 정보를 조회
	 * @param agencyId 
	 * @return
	 */
	public RecordSet getById(String agencyId){
		addWhere("lower(agencyId)",agencyId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * 210812_PYS : 사용안함
	 * @param datas
	 * @return
	 */
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	/**
	 * 210812_PYS : Data클래스와 Page클래스를 이용하여 검색, 페이징 용도로 사용
	 * 추가 설명은 해당 클래스 안에 설명
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 210812_PYS : 에이전시 id와 이름 조회
	 * @return
	 */
	public List<SharedMap<String,Object>> getSelectOption(){
		String q = "SELECT agencyId as id ,name FROM VW_MAM_AGENCY ORDER BY name asc" ;
		return super.query(q).getRows();
	}
	
	/**
	 * 210812_PYS : 사용중인 에이전시 id와 이름 조회
	 * @return
	 */
	public List<SharedMap<String,Object>> getActiveSelectOption(){
		String q = "SELECT agencyId as id ,name FROM VW_MAM_AGENCY WHERE status='사용' ORDER BY name asc" ;
		return super.query(q).getRows();
	}
}