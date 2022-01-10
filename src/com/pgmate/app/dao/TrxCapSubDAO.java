package com.pgmate.app.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
public class TrxCapSubDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxCapSubDAO.class );
	private static final String TABLE = "VW_TRX_CAP_SUB";
	private static final String COLUMNS = "*";

	public TrxCapSubDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TrxCapSubDAO.COLUMNS);
	}
	

	public RecordSet getByTrxId(String trxId){
		addWhere("trxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByRootTrxId(String trxId){
		addWhere("rootTrxId",trxId,eq);
		return search();
	}
	
	public RecordSet getByCapId(String capId){
		addWhere("capId",capId,eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId, long limit){
		addWhere("mchtId",mchtId,eq);
		setLimit(limit);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("mchtId",mchtId,eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public List<String> salesMonthList(){
		super.setColumns("SUBSTR(MIN(trxDay),1,6) as trxMonth");
		RecordSet rset = super.search();
		String trxMonth = rset.getRow(0).getString("trxMonth");
		String currentMonth = CommonUtil.getCurrentDate("yyyyMM");
		
		List<String> list = new ArrayList<String>();
		if(trxMonth.length() < 1) {
		} else if(trxMonth.equals(currentMonth)){
			list.add(currentMonth);
		}else{
			LocalDate date1 = LocalDate.parse(trxMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
	        LocalDate date2 = LocalDate.parse(currentMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
	        long gap = ChronoUnit.MONTHS.between(date1, date2);
			
	        for(long i=0;i<gap+1 ;i++){
	        	list.add(date1.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyyMM")));
	        }	
		}
		return list;
	}
	
	
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	

}