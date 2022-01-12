package com.pgmate.app.dao;

import java.util.ArrayList;
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
 * 210812_PYS : 대행사 정보, 지불, 정산 조회
 * @author Administrator
 *
 */
public class DistDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.DistDAO.class );
	private static final String TABLE = "VW_MAM_DIST";
	private static final String COLUMNS = "distId, name, `status`, bizType, bizCategory, idType, FN_MASK_IDENTIFY(identity) as identity, email, tel1, tel2, fax, zip, addr1, addr2, lat, lng, ceoName, FN_MASK_IDENTIFY(ceoIdentity) as ceoIdentity, ceoPhone, ceoTel, ceoZip, ceoAddr1, ceoAddr2, managerName, managerPhone, regId, regDay, regDate, payStatus, bankCd, bankName, account, accntHolder";
	
	public DistDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(DistDAO.COLUMNS);
	}
	
	/**
	 * 210812_PYS : 대행사ID로 조회
	 * <pre>
	 * SELECT * FROM VW_MAM_DIST 
	 * </pre>
	 * @param distId : 대행사ID
	 * @return
	 */
	public RecordSet getById(String distId){
		//KJM : lower(distId) = 'distId'
		addWhere("lower(distId)",distId.toLowerCase(),eq);
		return search();
	}
	
	/** 
	 * 210812_PYS : Data클래스로 조회
	 * @param datas
	 * @return
	 */
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	/** 
	 * 210812_PYS : Data클래스로 조회 (페이징)
	 * @param datas
	 * @return
	 */
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public List<SharedMap<String,Object>> getSelectOption(){
		String q = "SELECT distId as id ,name FROM VW_MAM_DIST ORDER BY name asc" ;
		return super.query(q).getRows();
	}
	
	/**
	 * 210812_PYS : 총판 수수료 조회
	 * @param distId : 대행사ID
	 * @return
	 */
	public List<SharedMap<String,Object>> getRateOption(String distId){
		String q = "SELECT rate FROM PG_MAM_DIST_MNG WHERE distId = '"+ distId +"' ORDER BY rate asc";
		SharedMap<String,Object> res = super.query(q).getRow(0);
		String rateString = res.getString("rate");
		
		List<SharedMap<String,Object>> resList = new ArrayList<SharedMap<String,Object>>();
		if(rateString.length() > 0) {
			for(String each : rateString.split(",")) {
				SharedMap<String,Object> eachMap = new SharedMap<String, Object>();
				eachMap.put("rate", each);
				eachMap.put("ratePer", String.format("%.3f", Double.parseDouble(each)*100));
				resList.add(eachMap);
			}
		}
		return resList;
	}
	

	public String getLastDistId(){
		String q = "SELECT distId FROM PG_MAM_DIST ORDER BY distId desc limit 1" ;
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("distId");
	}
}