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
public class TotCapDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TotCapDAO.class );
	private static final String TABLE = "VW_TOT_CAP";
	private static final String COLUMNS = "idx, capDay, capMonth, mchtId, distId, agencyId, salesId, stlDay, saleAmount, saleCount, saleStlAmount, saleStlFee, saleStlFeeVat, saleStlDistFee, saleStlAgencyFee, saleStlSalesFee, saleBenefit, rfdAmount, rfdCount, rfdStlAmount, rfdStlFee, rfdStlFeeVat, rfdStlDistFee, rfdStlAgencyFee, rfdStlSalesFee, rfdBenefit, rfdedAmount, rfdedCount, rfdedStlAmount, rfdedStlFee, rfdedStlFeeVat, rfdedStlDistFee, rfdedStlAgencyFee, rfdedStlSalesFee, rfdedBenefit, distFee, agencyFee, salesFee, benefit, regDate, name, salesName, agencyName, distName ";
	private static final String SUM_COLUMNS = "capDay,capMonth,mchtId,distId,agencyId,salesId,stlDay,SUM(saleAmount) as saleAmount,SUM(saleCount) as saleCount,SUM(saleStlAmount) as saleStlAmount,SUM(saleStlFee) as saleStlFee,SUM(saleStlFeeVat) as saleStlFeeVat,SUM(saleStlDistFee) as saleStlDistFee,SUM(saleStlAgencyFee) as saleStlAgencyFee,SUM(saleStlSalesFee) as saleStlSalesFee,SUM(saleBenefit) as saleBenefit,SUM(rfdAmount) as rfdAmount,SUM(rfdCount) as rfdCount,SUM(rfdStlAmount) as rfdStlAmount,SUM(rfdStlFee) as rfdStlFee,SUM(rfdStlFeeVat) as rfdStlFeeVat,SUM(rfdStlDistFee) as rfdStlDistFee,SUM(rfdStlAgencyFee) as rfdStlAgencyFee,SUM(rfdStlSalesFee) as rfdStlSalesFee,SUM(rfdBenefit) as rfdBenefit,SUM(rfdedAmount) as rfdedAmount,SUM(rfdedCount) as rfdedCount,SUM(rfdedStlAmount) as rfdedStlAmount,SUM(rfdedStlFee) as rfdedStlFee,SUM(rfdedStlFeeVat) as rfdedStlFeeVat,SUM(rfdedStlDistFee) as rfdedStlDistFee, "+
												"SUM(rfdedStlAgencyFee) as rfdedStlAgencyFee,SUM(rfdedStlSalesFee) as rfdedStlSalesFee,SUM(rfdedBenefit) as rfdedBenefit,SUM(distFee) as distFee,SUM(agencyFee) as agencyFee,SUM(salesFee) as salesFee,SUM(benefit) as benefit,name,salesName,agencyName,distName "	;

	public TotCapDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TotCapDAO.COLUMNS);
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("mchtId",mchtId,eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId, long limit){
		addWhere("mchtId",mchtId,eq);
		setOrderBy("capDay desc");
		setLimit(limit);
		return search();
	}
	
	public RecordSet getByDistId(String distId){
		addWhere("distId",distId,eq);
		return search();
	}

	public RecordSet getByAgencyId(String agencyId){
		addWhere("agencyId",agencyId,eq);
		return search();
	}
	
	public RecordSet getBySalesId(String salesId){
		addWhere("salesId",salesId,eq);
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
	
	public RecordSet salesIssuerList(List<Data> datas,Page page){
		String columns = "trxMonth, FN_SPLIT_STRING(issuer, ',', 1) as issuer , "
				+" sum(if(capType ='매입',1,0)) as saleCount,sum(if(capType ='매입',amount,0)) as saleAmount, "
				+" sum(if(capType !='매입',1,0)) as rfdCount,sum(if(capType !='매입',amount,0)) as rfdAmount ";
		super.setColumns(columns);
		super.setTable("VW_TRX_CAP_ISSUER");
		super.setGroupBy("trxMonth, FN_SPLIT_STRING(issuer, ',', 1)");
		super.setOrderBy("trxMonth desc,issuer desc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
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
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capDay, mchtId");
		super.setOrderBy("capDay desc, name asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesMchtMonthlyList(List<Data> datas,Page page){
		super.setColumns(SUM_COLUMNS);
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capMonth, mchtId");
		super.setOrderBy("capMonth desc, name asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesAgencyDailyList(List<Data> datas, Page page) {
		super.setColumns(SUM_COLUMNS);
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capDay, agencyId");
		super.setOrderBy("capDay desc, agencyName asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesAgencyMonthlyList(List<Data> datas,Page page){
		super.setColumns(SUM_COLUMNS);
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capMonth, agencyId");
		super.setOrderBy("capMonth desc, agencyName asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesDistDailyList(List<Data> datas, Page page) {
		super.setColumns(SUM_COLUMNS);
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capDay, distId");
		super.setOrderBy("capDay desc, distName asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesDistMonthlyList(List<Data> datas,Page page){
		super.setColumns(SUM_COLUMNS);
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capMonth, distId");
		super.setOrderBy("capMonth desc, distName asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesSalesMonthlyList(List<Data> datas,Page page){
		super.setColumns(SUM_COLUMNS);
		super.addWhere("(saleCount > 0 OR rfdCount > 0 OR rfdedCount > 0)");
		super.setGroupBy("capMonth, salesId");
		super.setOrderBy("capMonth desc, salesName asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);									//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet salesDailyTrandList(String startDay, String endDay){
		super.setColumns("capDay as days, SUM(saleAmount) as amt, SUM(benefit) as val");
		super.addWhere("capDay", startDay, ge);
		super.addWhere("capDay", endDay, le);
		super.setGroupBy("capDay");
		super.setOrderBy("capDay asc");
		return search();
	}
	
	public RecordSet salesMchtRank(String startDay, String endDay){
		super.setColumns("mchtId, name, SUM(saleAmount) as amt");
		super.addWhere("capDay", startDay, ge);
		super.addWhere("capDay", endDay, le);
		super.setGroupBy("mchtId");
		super.setLimit(10);
		super.setOrderBy("amt desc");
		return search();
	}
	
}