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
public class MngViewDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MngViewDAO.class );
	private static final String TABLE = "VW_MAM_MNG";
	private static final String COLUMNS = "id, distId, agencyId, grade, rate, limitOnce, limitDay, limitMonth, payStatus, loanSettleStatus, settleType, wireFee, bankCd, bankName, account, accntHolder, accntCheck, accntDate, regId, regDay, regDate";
	
	public MngViewDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MngViewDAO.COLUMNS);
	}
	
	public RecordSet getById(String id){
		addWhere("lower(id)",id.toLowerCase(),eq);
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
	
	public RecordSet getByChildrenLimit(String id) {
		setColumns("MAX(limitOnce) AS limitOnce, SUM(limitDay) AS limitDay, SUM(limitMonth) AS limitMonth");
		addWhere("lower(parentId)",id.toLowerCase(),eq);
		return search();
	}
	
}