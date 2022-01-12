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
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//KJM : ONLINE 거래 생성 또는 OFLINE 거래 업로드용 테이블
public class TotLoadDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TotLoadDAO.class );
	private static final String TABLE = "VW_TRX_LOAD";
	private static final String COLUMNS = "*";
	public TotLoadDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TotLoadDAO.COLUMNS);
	}
	
	//KJM : 해당 인덱스에 대한 거래생성 상세 조회
	public RecordSet getByIdx(String idx){
		addWhere("idx",idx,eq);
		return search();
	}
	
	public RecordSet getByDtlIdx(String idx){
		setTable("VW_TRX_LOAD_DTL");
		addWhere("idx",idx,eq);
		return search();
	}
	
	public RecordSet getByTrxIdx(String idx){
		setTable("PG_TRX_LOAD");
		//KJM : where idx 같은
		addWhere("idx",idx,eq);
		return search();
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
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	//KJM : 거래생성 리스트 가져옴
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	
	
	
	public int insert(List<SharedMap<String, Object>> eachList, String userId) {
		super.setTable("VW_MCHT_TMN");
		super.setColumns("*");
		super.addWhere("tmnId", eachList.get(0).getString("tmnId"));
		RecordSet rset = super.search();
		if(rset.size() < 1) {
			return 0;
		}
		SharedMap<String, Object> tmnMap = rset.getRow(0);
		long amount = 0;
		for(SharedMap<String, Object> eachMap : eachList) {
			amount += eachMap.getLong("amount");
		}
		
		super.initRecord();
		super.setTable("PG_TRX_LOAD");
		super.setRecord("mchtId", tmnMap.getString("mchtId"));
		super.setRecord("tmnId", tmnMap.getString("tmnId"));
		super.setRecord("amount", amount);
		super.setRecord("cnt", eachList.size());
		super.setRecord("van", tmnMap.getString("van"));
		super.setRecord("vanId", tmnMap.getString("vanId"));
		if(tmnMap.getString("vanId").equalsIgnoreCase("FACTORING")) {
			super.setRecord("vanType", "FACTORING");
		} else if(tmnMap.getString("vanId").equalsIgnoreCase("OFFLINE")) {
			super.setRecord("vanType", "OFFLINE");
		} else {
			super.setRecord("vanType", "ONLINE");
		}
		super.setRecord("summary", "SYSTEM EXCEL UPLOAD");
		super.setRecord("regId", userId);
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));

		if(super.insert()) {
			super.setTable("PG_TRX_LOAD");
			super.setColumns("idx");
			super.setOrderBy("idx desc");
			super.setLimit(1);
			String batchIdx = super.search().getRow(0).getString("idx");
			
			if(new TotLoadDtlDAO().insert(eachList, batchIdx, userId) < 1) {
				return 0;
			}
		} else {
			return 0;
		}
		return 1;
	}
}