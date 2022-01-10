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
public class TotCapSubDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TotCapSubDAO.class );
	private static final String TABLE = "VW_TOT_CAP_SUB";
	private static final String COLUMNS = "idx, capDay, capMonth, tmnId, mchtId, stlDay, saleAmount, saleCount, saleStlAmount, saleStlFee, saleStlFeeVat, saleBenefit, rfdAmount, rfdCount, rfdStlAmount, rfdStlFee, rfdStlFeeVat, rfdBenefit, rfdedAmount, rfdedCount, rfdedStlAmount, rfdedStlFee, rfdedStlFeeVat, rfdedBenefit, benefit, regDate, name, ";
	private static final String SUM_COLUMNS = "capDay,capMonth,tmnId,mchtId,stlDay,SUM(saleAmount) as saleAmount,SUM(saleCount) as saleCount,SUM(saleStlAmount) as saleStlAmount,SUM(saleStlFee) as saleStlFee,SUM(saleStlFeeVat) as saleStlFeeVat,SUM(saleBenefit) as saleBenefit,SUM(rfdAmount) as rfdAmount,SUM(rfdCount) as rfdCount,SUM(rfdStlAmount) as rfdStlAmount,SUM(rfdStlFee) as rfdStlFee,SUM(rfdStlFeeVat) as rfdStlFeeVat,SUM(rfdBenefit) as rfdBenefit,SUM(rfdedAmount) as rfdedAmount,SUM(rfdedCount) as rfdedCount,SUM(rfdedStlAmount) as rfdedStlAmount,SUM(rfdedStlFee) as rfdedStlFee,SUM(rfdedStlFeeVat) as rfdedStlFeeVat,"+
												"SUM(rfdedBenefit) as rfdedBenefit,SUM(benefit) as benefit,mchtName,dtlName,vanId,vanName ";

	public TotCapSubDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TotCapSubDAO.COLUMNS);
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("mchtId",mchtId,eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId, long limit){
		addWhere("mchtId",mchtId,eq);
		setLimit(limit);
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
	
	public RecordSet salesDailyList(List<Data> datas, Page page) {
		super.setColumns(SUM_COLUMNS);
		super.setGroupBy("capDay");
		super.setOrderBy("capDay desc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesMonthlyList(List<Data> datas,Page page){
		super.setColumns(SUM_COLUMNS);
		super.setGroupBy("capMonth");
		super.setOrderBy("capMonth desc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesSum(List<Data> datas){
		Page page = new Page();
		page.size = 999999999;
		super.setColumns(SUM_COLUMNS);
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesMchtDailyList(List<Data> datas, Page page) {
		super.setColumns(SUM_COLUMNS);
		super.setGroupBy("capDay, mchtId");
		super.setOrderBy("capDay desc, name asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesTmnDailyList(List<Data> datas, Page page) {
		super.setColumns(SUM_COLUMNS);
		super.setGroupBy("capDay, tmnId");
		super.setOrderBy("capDay desc");
		page = CPUtil.correctPage(page);
		
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
}