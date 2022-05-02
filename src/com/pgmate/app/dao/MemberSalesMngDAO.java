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
public class MemberSalesMngDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MemberSalesMngDAO.class );
	private static final String TABLE = "PG_MAM_SALES_MNG";
//	private static final String COLUMNS = "salesId, payStatus, loanSettleStatus, settleType, rate, diff1Rate, diff1CheckRate, diff2Rate, diff2CheckRate, diff3Rate, diff3CheckRate, diff4Rate, diff4CheckRate, wireFee, bankCd, bankName, account, accntHolder, accntCheck, accntDate, regId, regDay, regDate";
	private static final String COLUMNS = "*";
	
	public MemberSalesMngDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MemberSalesMngDAO.COLUMNS);
	}
	
	public RecordSet getById(String salesId){
		addWhere("lower(salesId)",salesId.toLowerCase(),eq);
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
	
	public RecordSet getSalesMngByPayType(String salesId, String payType){
		setColumns("num as salesNum, settleName as salesSettleName, payStatus as salesPayStatus");
		addWhere("lower(salesId)",salesId.toLowerCase(),eq);
		addWhere("payStatus","사용");
		addWhere("payType", payType, eq);
		setOrderBy("payStatus asc, settleName asc");
		return search();
	}
}