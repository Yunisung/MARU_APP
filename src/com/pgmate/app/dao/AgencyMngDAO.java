package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * 210812_PYS : 에이전시 지불 및 정산관리
 * @author Administrator
 *
 */
public class AgencyMngDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.AgencyMngDAO.class );
//	private static final String COLUMNS = "agencyId, payStatus, loanSettleStatus, settleType, rate, diff1DistRate, diff2DistRate, diff3DistRate, diff4DistRate, diff1AgencyRate, diff2AgencyRate, diff3AgencyRate, diff4AgencyRate, wireFee, limitOnce, limitDay, limitMonth, bankCd, bankName, account, accntHolder, accntCheck, accntDate, regId, regDay, regDate";
	private static final String TABLE = "PG_MAM_AGENCY_MNG"; // 에이전시 지불 및 정산 정보
	private static final String COLUMNS = "*";
	
	public AgencyMngDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(AgencyMngDAO.COLUMNS);
	}
	
	
	/**
	 * 210812_PYS : 에이전시 정산 데이터 아이디로 조회
	 *<pre>
	 *SELECT * FROM PG_MAM_AGENCY_MNG WHERE agencyId = 'agencyId'
	 *</pre>
	 * @param agencyId
	 * @return
	 */
	public RecordSet getById(String agencyId){
		addWhere("lower(agencyId)",agencyId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByNum(String index){
		addWhere("num",index,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	/**
	 * 210812_PYS : Data클래스와 Page클래스로 DB 조회, 사용하는곳 없음
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
//	public RecordSet getAgencyMngByPayType(String agencyId, String payType){
//		setColumns("num as agencyNum, settleName as agencySettleName, payStatus as agencyPayStatus");
//		addWhere("lower(agencyId)",agencyId.toLowerCase(),eq);
//		addWhere("payStatus","사용");
//		addWhere("payType", payType, eq);
//		setOrderBy("payStatus asc, settleName asc");
//		return search();
//	}
	public RecordSet getAgencyMngByPayType(String agencyId, String payType){
		setColumns("agencyId as agencyNum, settleType as agencySettleName, payStatus as agencyPayStatus");
		addWhere("lower(agencyId)",agencyId.toLowerCase(),eq);
		addWhere("payStatus","사용");
//		addWhere("payType", payType, eq);
		setOrderBy("payStatus asc, settleType asc");
		return search();
	}
}