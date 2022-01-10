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
 * @author Administrator
 *
 */
public class CollectTempDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CollectTempDAO.class );
	private static final String TABLE = "VW_SETTLE_COLLECT_TEMP";
	private static final String COLUMNS = "*";
	
	public CollectTempDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CollectTempDAO.COLUMNS);
	}
	
	public RecordSet search(List<Data> datas){	
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	/*
	public RecordSet makeSearch(List<Data> datas){
		super.setTable("(SELECT stlVanDay, van, vanId, mchtId, MAX(tmnId) as tmnId, name,distId,agencyId,salesId,SUM(amount) as amount, SUM(stlVanFee) as stlVanFee, SUM(amount-stlVanFee) as collectAmount,"
						+" SUM(IF(risk != '' AND capType = '매입', 0, amount - stlVanFee)) AS MARUAmount"
						+" FROM VW_TRX_CAP"
						+" WHERE vanStatus = '입금대기'"
						+" GROUP BY stlVanDay, van, vanId, mchtId"
						+" ) A JOIN PG_VAN B ON A.vanId = B.vanId AND A.van = B.van");
		super.setColumns("A.*, B.name as vanName ");
		super.setOrderBy("A.name asc");
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}*/
	
	public RecordSet makeSearch(List<Data> datas){
		super.setTable("(SELECT C.stlVanDay, C.van, C.vanId, B.mchtId, MAX(B.tmnId) as tmnId, SUM(B.amount) as amount, SUM(C.stlVanFee) as stlVanFee, SUM(B.amount-C.stlVanFee) as collectAmount, "
//						+" SUM(IF(C.risk != '' AND B.capType = '매입', 0, B.amount - C.stlVanFee)) AS MARUAmount "
						+" SUM(B.amount - C.stlVanFee) AS MARUAmount "
						+" FROM PG_TRX_CAP B join PG_TRX_CAP_DTL C on B.capId = C.capId "
						+" WHERE C.vanStatus = '입금대기' "
						+" GROUP BY C.stlVanDay, C.van, C.vanId, B.mchtId "
						+" ) A JOIN PG_VAN E ON A.vanId = E.vanId AND A.van = E.van"
						+ " join PG_MCHT D on A.mchtId = D.mchtId");
		super.setColumns("A.*,D.name,D.ceoName,D.distId,D.agencyId,D.salesId,E.name as vanName");
		super.setOrderBy("D.name asc");
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
}