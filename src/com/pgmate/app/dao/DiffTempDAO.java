package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class DiffTempDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.DiffTempDAO.class );
	private static final String TABLE = "VW_SETTLE_COLLECT_TEMP";
	private static final String COLUMNS = "*";
	
	public DiffTempDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(DiffTempDAO.COLUMNS);
	}
	
	public RecordSet search(List<Data> datas){	
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet makeSearch(List<Data> datas){
		super.setTable("(SELECT C.stlDiffVanDay, C.van, C.vanId, B.mchtId, MAX(B.tmnId) as tmnId, SUM(B.amount) as amount, SUM(C.stlDiffAmt) as stlDiffAmt, SUM(C.stlDiffVanAmt) as stlDiffVanAmt "
						+" FROM PG_TRX_CAP B join PG_TRX_CAP_DTL C on B.capId = C.capId "
						+" WHERE C.stlDiffStatus = '입금대기' "
						+" AND C.vanId IN ('2010000007','2010000008') "
						+" GROUP BY C.stlDiffVanDay, C.van, C.vanId, B.mchtId "
						+" ) A JOIN PG_VAN E ON A.vanId = E.vanId AND A.van = E.van "
						+ " join PG_MCHT D on A.mchtId = D.mchtId");
		super.setColumns("A.*,D.name,D.ceoName,D.distId,D.agencyId,D.salesId,E.name as vanName");
		super.setOrderBy("A.stlDiffVanDay desc, D.name asc");
		
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
}