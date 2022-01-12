package com.pgmate.app.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class TrxDailyDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TrxDailyDAO.class );
	SharedMap<String,Object> dailyMap = new SharedMap<String,Object>();
	
	public TrxDailyDAO() {
		// TODO Auto-generated constructor stub
		super.setOrderBy("days desc");
	}
	
	
	public RecordSet search(List<Data> datas){
		super.setTable("VW_DAILY_S");
		super.setDebug(CPUtil.CP_DEBUG);
		super.setColumns("*");
		
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	//정산일기준 일마감 통계  dailysByStlDay
	public RecordSet dailysByStlDay(List<Data> datas, Page page) {
		
		super.initRecord();
		super.setTable("PG_TOT_CAP_STLDAY");
		
		StringBuilder sb = new StringBuilder();
		sb.append("stlDay, SUM(payCnt) AS payCnt, SUM(payAmt) AS payAmt, SUM(rfdCnt) AS rfdCnt, SUM(rfdAmt) AS rfdAmt, SUM(totalCnt) AS totalCnt, "
				+ "SUM(totalAmt) AS totalAmt, SUM(stlVanFee) AS stlVanFee, SUM(stlVanAmt + stlDiffVanAmt) AS stlVanAmt, SUM(stlFee + stlFeeVat) AS stlFee, "
				+ "SUM(stlAmt) AS stlAmt, SUM(profit) AS profit, SUM(stlDistFee + stlDiffDistFee) AS stlDistFee, SUM(stlAgencyFee + stlDiffAgencyFee) AS stlAgencyFee, "
				+ "SUM(stlSalesFee + stlDiffSalesFee) AS stlSalesFee, SUM(stlDistFee + stlDiffDistFee + stlAgencyFee + stlDiffAgencyFee+ stlSalesFee + stlDiffSalesFee) AS totSalesFee, "
				+ "SUM(stlDiffAmt) AS stlDiffAmt, SUM(benefit) AS benefit");
		
		// B: 컬럼 셋팅 
		super.setColumns(sb.toString());
		// B: 하루 총 매출금액 추출위해 그룹으로 묶기
		super.setGroupBy("stlDay");
		// B: 날짜 내림차순
		super.setOrderBy("stlDay desc");

		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);	//DATA to CONDITION 
		
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}

	public List<SharedMap<String, Object>> dailysByStlDayDTL(String stlDay) {
		super.initRecord();
		super.setTable("PG_TOT_CAP_STLDAY A LEFT JOIN PG_MCHT B ON A.mchtId = B.mchtId");
		StringBuilder sb = new StringBuilder();
		sb.append("A.*, B.name, SUM(A.stlVanAmt + A.stlDiffVanAmt) AS stlVanAmt, SUM(A.stlFee + A.stlFeeVat) AS stlFee,");
		sb.append("SUM(A.stlDistFee + A.stlDiffDistFee) AS stlDistFee, SUM(A.stlAgencyFee + A.stlDiffAgencyFee) AS stlAgencyFee,");
		sb.append("SUM(A.stlSalesFee + A.stlDiffSalesFee) AS stlSalesFee,");
		sb.append("SUM(A.stlDistFee + A.stlDiffDistFee + A.stlAgencyFee + A.stlDiffAgencyFee+ A.stlSalesFee + A.stlDiffSalesFee) AS totSalesFee");
		super.setColumns(sb.toString());		
		super.addWhere("stlDay", stlDay, eq);
		super.setGroupBy("A.mchtId");
		super.setOrderBy("stlDay desc");
		
		RecordSet rset = super.search();
		super.initRecord();
		
		return rset.getRows();
	}
	//정산일기준 일마감 통계  dailysByStlDay
	
	//승인일기준 일마감 통계  listByCapDay
	public RecordSet listByCapDay(List<Data> datas,Page page){
		super.setTable("PG_TOT_CAP_CAPDAY");
		super.setDebug(CPUtil.CP_DEBUG);
		super.setColumns("capDay, " + 
				"SUM(payCnt) AS payCnt, " + 
				"SUM(payAmt) AS payAmt, " + 
				"SUM(rfdCnt) AS rfdCnt, " + 
				"SUM(rfdAmt) AS rfdAmt, " + 
				"SUM(totalCnt) AS totalCnt, " + 
				"SUM(totalAmt) AS totalAmt, " + 
				"SUM(stlVanFee) AS stlVanFee, " + 
				"SUM(stlVanAmt + stlDiffVanAmt) AS stlVanAmt, " + 
				"SUM(stlFee + stlFeeVat) AS stlFee,	" + 
				"SUM(stlAmt) AS stlAmt,	" + 
				"SUM(profit) AS profit,	" + 
				"SUM(stlDistFee + stlDiffDistFee) AS stlDistFee, " + 
				"SUM(stlAgencyFee + stlDiffAgencyFee) AS stlAgencyFee, " + 
				"SUM(stlSalesFee + stlDiffSalesFee) AS stlSalesFee,	" + 
				"SUM(stlDistFee + stlDiffDistFee + stlAgencyFee + stlDiffAgencyFee+ stlSalesFee + stlDiffSalesFee) AS totSalesFee, " + 
				"SUM(stlDiffAmt) AS stlDiffAmt, " +
				"SUM(benefit) AS benefit");
		super.setGroupBy("capDay");
		super.setOrderBy("capDay desc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	//승인일기준 일마감 통계 가맹점 기준  getDailyCapByCapDay
	public List<SharedMap<String, Object>> getDailyCapByCapDay(String capDay) {
		super.initRecord();
		super.setTable("PG_TOT_CAP_CAPDAY A LEFT JOIN PG_MCHT B ON A.mchtId = B.mchtId");
		super.setColumns("A.*, B.name, "
				+ "SUM(A.payCnt) AS payCnt, " + 
				"SUM(A.payAmt) AS payAmt, " + 
				"SUM(A.rfdCnt) AS rfdCnt, " + 
				"SUM(A.rfdAmt) AS rfdAmt, " + 
				"SUM(A.totalCnt) AS totalCnt, " + 
				"SUM(A.totalAmt) AS totalAmt, " + 
				"SUM(A.stlVanFee) AS stlVanFee, " + 
				"SUM(A.stlVanAmt + stlDiffVanAmt) AS stlVanAmt, " + 
				"SUM(A.stlFee + stlFeeVat) AS stlFee,	" + 
				"SUM(A.stlAmt) AS stlAmt,	" + 
				"SUM(A.profit) AS profit,	" + 
				"SUM(A.stlDistFee + A.stlDiffDistFee) AS stlDistFee, " + 
				"SUM(A.stlAgencyFee + A.stlDiffAgencyFee) AS stlAgencyFee, " + 
				"SUM(A.stlSalesFee + A.stlDiffSalesFee) AS stlSalesFee,	" + 
				"SUM(A.stlDistFee + A.stlDiffDistFee + A.stlAgencyFee + A.stlDiffAgencyFee + A.stlSalesFee + A.stlDiffSalesFee) AS totSalesFee, " + 
				"SUM(A.benefit) AS benefit");
		super.addWhere("A.capDay", capDay, eq);
		super.setGroupBy("A.mchtId");
		super.setOrderBy("A.capDay desc");
		super.setLimit(999999);
		RecordSet rset = super.search();
		super.initRecord();
		
		return rset.getRows();
	}
	
	public RecordSet list(List<Data> datas,Page page){
		super.setTable("VW_DAILY_S");
		super.setDebug(CPUtil.CP_DEBUG);
		super.setColumns("*");
		super.setOrderBy("days asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public List<SharedMap<String, Object>> getDailyCap(String day) {
		super.initRecord();
		super.setTable("PG_TRX_CAP");
		super.setColumns("mchtId, FN_MCHT_NAME(mchtId) name,	SUM(IF(capType = '매입',amount,0)) AS trxCapAmt ,		SUM(IF(capType = '매입',1,0)) AS trxCapCnt ,	SUM(IF(capType = '매입',0,amount)) AS trxRfdAmt ,	SUM(IF(capType = '매입',0,1)) AS trxRfdCnt ");
		super.addWhere("trxDay", day, eq);
		super.setGroupBy("mchtId");
		super.setLimit(999999);
		RecordSet rset = super.search();
		super.initRecord();
		
		if(rset.size() > 0){
			for(SharedMap<String,Object> data : rset.getRows()){
				
				dailyMap.put(data.getString("mchtId"),data);
			}
		}
		
		return getDailyIn(day);
	}
	
	private List<SharedMap<String, Object>> getDailyIn(String day) {
		super.initRecord();
		super.setTable("VW_TRX_CAP");
		super.setColumns("mchtId, FN_MCHT_NAME(mchtId) name , SUM(IF(capType = '매입',amount,0)) AS inCapAmt ,	SUM(IF(capType = '매입',1,0)) AS inCapCnt ,	SUM(IF(capType = '매입',0,amount)) AS inRfdAmt ,	SUM(IF(capType = '매입',0,1)) AS inRfdCnt ,	SUM(IF(capType = '매입',stlVanFee,0)) AS inVanCapFee ,	SUM(IF(capType = '매입',0,stlVanFee)) AS inVanRfdFee,	SUM(stlVanFee) as inVanFee ,	SUM(amount-stlVanFee) as inVanInAmt ");
		super.addWhere("stlVanDay", day, eq);
		super.setGroupBy("mchtId");
		super.setLimit(999999);
		RecordSet rset = super.search();
		super.initRecord();
		
		if(rset.size() > 0){
			for(SharedMap<String,Object> data : rset.getRows()){
				if(dailyMap.containsKey(data.getString("mchtId"))){
					SharedMap<String,Object> dd = (SharedMap<String,Object>)dailyMap.get(data.getString("mchtId"));
					dd.putAll(data);
					dailyMap.put(data.getString("mchtId"), dd);
				}else{
					
					dailyMap.put(data.getString("mchtId"),data);
				}
			}
		}
		
		return getDailyOut(day);
	}
	
	private List<SharedMap<String, Object>> getDailyOut(String day) {
		List<SharedMap<String, Object>> sum = new ArrayList<SharedMap<String, Object>>();
		super.initRecord();
		super.setTable("PG_SETTLE_MCHT");
		StringBuilder sb = new StringBuilder();
		sb.append("mchtId,FN_MCHT_NAME(mchtId) name,sum(payAmt+relsAmt) outPayAmt, sum(payCnt+relsCnt) outPayCnt,sum(payFee+payVat+relsFee+relsVat) outPayFee,");
		sb.append(" sum(rfdAmt) outRfdAmt, sum(rfdCnt) outRfdCnt,sum(rfdFee+rfdVat) outRfdFee ,");
		sb.append("sum(payFee+payVat+relsFee+relsVat+rfdFee+rfdVat) outAllFee,");
		sb.append("sum(vanFee) outVanFee,");
		sb.append("sum(distFee) outDistFee,");
		sb.append("sum(agencyFee) outAgencyFee,");
		sb.append("sum(stlAmount+relsAmt-relsFee-relsVat+deductAmt)  outPayOutAmt,sum(benefit) outBenefit");
		super.setColumns(sb.toString());
		super.addWhere("stlDay", day, eq);
		super.setGroupBy("mchtId");
		super.setLimit(999999);
		RecordSet rset = super.search();
		super.initRecord();
		
		if(rset.size() > 0){
			for(SharedMap<String,Object> data : rset.getRows()){
				if(dailyMap.containsKey(data.getString("mchtId"))){
					SharedMap<String,Object> dd = (SharedMap<String,Object>)dailyMap.get(data.getString("mchtId"));
					dd.putAll(data);
					dailyMap.put(data.getString("mchtId"), dd);
				}else{
					dailyMap.put(data.getString("mchtId"), data);
				}
			}
		}
		for(String key : dailyMap.keySet()){
			sum.add((SharedMap<String,Object>)dailyMap.get(key));
		}
		return sum;
	}
	
	
	
	public List<SharedMap<String, Object>> getSettle(String startDay,String endDay) {
		
		super.initRecord();
		super.setTable("PG_SETTLE_MCHT");
		StringBuilder sb = new StringBuilder();
		sb.append("stlDay  days ,sum(payCnt) payCnt,sum(payAmt) payAmt, sum(rfdCnt) rfdCnt,sum(rfdAmt) rfdAmt ,");
		sb.append("sum(holdCnt) holdCnt,sum(holdAmt) holdAmt, sum(payAmt+rfdAmt) salesAmt, sum(payFee+payVat+rfdFee+rfdVat+relsFee+relsVat)  fee,");
		sb.append("sum(vanFee) vanFee,sum(agencyFee) agencyFee,sum(distFee) distFee, sum(benefit) benefit,sum(stlAmount) stlAmount,");
		sb.append("sum(relsAmt-relsFee-relsVat)  overAmt, sum(deductAmt) deductAmt , sum(stlAmount+(relsAmt-relsFee- relsVat)+deductAmt) payoutAmt  ");
		
		super.setColumns(sb.toString());
		if(!CommonUtil.isNullOrSpace(startDay)){
			super.addWhere("stlDay", startDay, ge);
		}
		if(!CommonUtil.isNullOrSpace(endDay)){
			super.addWhere("stlDay", endDay, le);
		}
		super.setGroupBy("stlDay");
		super.setOrderBy("stlDay asc");
		super.setLimit(999999);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRows();
	}
	
	
	public List<SharedMap<String, Object>> getCollect(String startDay,String endDay) {
		super.initRecord();
		super.setTable("( SELECT collectDay , sum(amount) amount, sum(stlVanFee) stlVanFee , sum(calcAmount) calcAmount, sum(collectAmount) collectAmount, sum(deductAmount) deductAmount  FROM VW_COLLECT_SETTLE_DTL_STATUS  WHERE status='확정'  group by collectDay) A");
		super.setColumns("A.*, IF(deductAmount < 0 ,deductAmount,0) as minusAmount,IF(deductAmount < 0 ,0,deductAmount) as plusAmount");
		if(!CommonUtil.isNullOrSpace(startDay)){
			super.addWhere("A.collectDay", startDay, ge);
		}
		if(!CommonUtil.isNullOrSpace(endDay)){
			super.addWhere("A.collectDay", endDay, le);
		}
		super.setOrderBy("A.collectDay asc");
		super.setLimit(999999);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRows();
	}
	
	public RecordSet getCollectWithMcht(List<Data> datas) {
		super.initRecord();
		super.setTable(" VW_COLLECT_SETTLE_DTL_STATUS A JOIN VW_MCHT B ON A.mchtId = B.mchtId ");
		super.setColumns(" A.*, IF(deductAmount < 0 ,deductAmount,0) as minusAmount,IF(deductAmount < 0 ,0,deductAmount) as plusAmount, B.name, B.ceoName, B.distId, B.agencyId, B.salesId ");
		super.setWhere(" A.status = '확정' ");
		super.setOrderBy("A.collectDay asc");
		super.setLimit(999999);
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public static void main(String[] args){
		TrxDailyDAO t = new TrxDailyDAO();
		List<SharedMap<String, Object>> dd= t.getDailyCap("20171117");
		System.out.println(dd.size());
		System.out.println(GsonUtil.toJson(dd));
	}
	

}
