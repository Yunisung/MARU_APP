package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * @author Administrator
 *
 */
public class HTDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.HTDAO.class );
	

	public HTDAO() {
		super.setOrderBy("idx desc");
	}
	

	
	public RecordSet getUserById(String userId){
		super.setTable("HT_USER");
		addWhere("id",userId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getUserById(String userId,long size){
		super.setTable("HT_USER");
		addWhere("id",userId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	
	public RecordSet getMamByAgencyId(String agencyId){
		super.setTable("HT_MAM_AGENCY");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMamByAgencyId(String agencyId,long size){
		super.setTable("HT_MAM_AGENCY");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	public RecordSet getMamMngByAgencyId(String agencyId){
		super.setTable("HT_MAM_AGENCY_MNG");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMamMngByAgencyId(String agencyId,long size){
		super.setTable("HT_MAM_AGENCY_MNG");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	public RecordSet getMamByDistId(String distId){
		super.setTable("HT_MAM_DIST");
		addWhere("distId",distId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMamByDistId(String distId,long size){
		super.setTable("HT_MAM_DIST");
		addWhere("distId",distId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	public RecordSet getMamMngByDistId(String distId){
		super.setTable("HT_MAM_DIST_MNG");
		addWhere("distId",distId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMamMngByDistId(String distId,long size){
		super.setTable("HT_MAM_DIST_MNG");
		addWhere("distId",distId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	
	public RecordSet getMamBySalesId(String salesId){
		super.setTable("HT_MAM_SALES");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMamBySalesId(String salesId,long size){
		super.setTable("HT_MAM_SALES");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	public RecordSet getMamMngBySalesId(String salesId){
		super.setTable("HT_MAM_SALES_MNG");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMamMngBySalesId(String salesId,long size){
		super.setTable("HT_MAM_SALES_MNG");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	public RecordSet getMchtByMchtId(String mchtId){
		super.setTable("HT_MCHT");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMchtByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	public RecordSet getMchtMngByMchtId(String mchtId){
		super.setTable("HT_MCHT_MNG");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMchtMngByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT_MNG");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	
	public RecordSet getMchtTaxByMchtId(String mchtId){
		super.setTable("HT_MCHT_TAX");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMchtTaxByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT_TAX");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	
	public RecordSet getMchtTmnByMchtId(String mchtId){
		super.setTable("HT_MCHT_TMN");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMchtTmnByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT_TMN");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	

}