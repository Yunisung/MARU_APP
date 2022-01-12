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
//KJM : 가맹점 정산 관련 
public class SettleMchtDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.SettleMchtDAO.class );
	private static final String TABLE = "VW_SETTLE_MCHT";
//	private static final String COLUMNS = "*";
	private static final String COLUMNS = "*, concat(substr(stlDay,1,4),'-',substr(stlDay,5,2),'-',substr(stlDay,7)) AS stlDay2, "
			+ "concat(substr(startDay,1,4),'-',substr(startDay,5,2),'-',substr(startDay,7), ' ~ ', substr(endDay,1,4),'-',substr(endDay,5,2),'-',substr(endDay,7)) as payTerm, "
			+ "(payAmt+rfdAmt) as totalAmt, (payFee + payVat) + (rfdFee + rfdVat) + (relsFee + relsVat) AS totalFee, (agencyFee+distFee) AS totalDistFee, "
			+ "case when ddctType = 'Y' then (relsAmt - relsFee - relsVat) - ddctAmt else relsAmt - relsFee - relsVat end AS minusAmt, "
			+ "case when ddctType = 'Y' then stlAmount + (relsAmt - relsFee - relsVat) + deductAmt - ddctAmt else stlAmount + (relsAmt - relsFee - relsVat) + deductAmt end AS payOutAmt";
	
	
	public SettleMchtDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(SettleMchtDAO.COLUMNS);
	}
	
	public RecordSet getById(String stlId){
		addWhere("stlId", stlId, eq);
		return search();
	}
	
	public RecordSet getByLists(String stlId){
		addWhere("stlId", stlId, in);
		return search();
	}
	
	public RecordSet getByChargeSettleLists(String stlId){
		addWhere("stlId", stlId, in);
		addWhere("settleSvc", "충전정산", eq);
		addWhere("stlType", "C+0", ne);
		return search();
	}
	
	public RecordSet getByMchtId(String memberId){
		addWhere("mchtId",memberId,eq);
		return search();
	}
	
	public RecordSet getByGrade(String grade){
		addWhere("grade",grade,eq);
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
	
	public RecordSet sumAmount(List<Data> datas){
		super.setColumns("SUM(stlAmt) as stlAmt");
		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
}