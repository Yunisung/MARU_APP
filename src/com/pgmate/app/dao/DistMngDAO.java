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
public class DistMngDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.DistMngDAO.class );
	private static final String TABLE = "PG_MAM_DIST_MNG";
	private static final String COLUMNS = "distId, payStatus, loanSettleStatus, settleType, rate, wireFee, limitOnce, limitDay, limitMonth, bankCd, bankName, account, accntHolder, accntCheck, accntDate, regId, regDay, regDate";
	
	public DistMngDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(DistMngDAO.COLUMNS);
	}
	
	public RecordSet getById(String distId){
		addWhere("lower(distId)",distId.toLowerCase(),eq);
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
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet getDistMngByPayType(String distId, String payType){
		setColumns("num as distNum, settleName as distSettleName, payStatus as distPayStatus");
		addWhere("lower(distId)",distId.toLowerCase(),eq);
		addWhere("payStatus","사용");
		addWhere("payType",payType,eq);
		setOrderBy("payStatus asc, settleName asc");
		return search();
	}
}