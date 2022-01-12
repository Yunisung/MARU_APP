package com.pgmate.app.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class NoticeDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.NoticeDAO.class );
	private static final String TABLE = "PG_NOTICE";
	private static final String COLUMNS = "`idx`, `status`, `title`, `pubDay`, `closeDay`, `regId`, `regDay`, `regDate`, `summary`";
	
	public NoticeDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(NoticeDAO.COLUMNS);
	}
	
	public RecordSet getById(String idx){
		addWhere("lower(idx)",idx.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * 210809_PYS : PB_NOTICE에서 생성된지 27일이 지나지 않은 공지사항들을 리턴한다
	 * <pre>
	 * SELECT * FROM PG_NOTICE WHERE STATUS = '개시' AND closeDay > '현재날짜' AND pubDay > '현재날짜-27일'
	 * </pre>
	 * @return
	 */
	public RecordSet getByNew(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -27);
		addWhere("status","개시",eq);
		addWhere("closeDay",CommonUtil.getCurrentDate("yyyyMMdd"),gt);
		addWhere("pubDay",CommonUtil.getDateString("yyyyMMdd", cal.getTime(), null),gt);
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
	//공지사항 전송 =======================================================
	/*
	 * 개별 전송
	 */
	public SharedMap<String,Object> checkId(String id, String grade){
		SharedMap<String,Object> map = new SharedMap<String,Object>();
		RecordSet rset;
		
		if(grade == "대행사" || "대행사".equals(grade)) {
			super.setTable("PG_MAM_DIST");
			super.setColumns("email, tel1, tel2");
			super.addWhere("lower(distId)",id,eq);
			
			rset = super.search();
			if(rset.size() > 0){
				map.put("grade", "대행사");
				map.put("email", rset.getRowFirst().getString("email"));
				map.put("tel1", rset.getRowFirst().getString("tel1"));
				map.put("tel2", rset.getRowFirst().getString("tel2"));
			}
		} else if(grade == "에이전시" || "에이전시".equals(grade)) {
			super.setTable("PG_MAM_AGENCY");
			super.setColumns("email, tel1, tel2");
			super.addWhere("lower(agencyId)",id,eq);
			
			rset = super.search();
			if(rset.size() > 0){
				map.put("grade", "에이전시");
				map.put("email", rset.getRowFirst().getString("email"));
				map.put("tel1", rset.getRowFirst().getString("tel1"));
				map.put("tel2", rset.getRowFirst().getString("tel2"));
			}
		} else if(grade == "지사" || "지사".equals(grade)) {
			super.setTable("PG_MAM_SALES");
			super.setColumns("email, tel1, tel2");
			super.addWhere("lower(salesId)",id,eq);
			
			rset = super.search();
			if(rset.size() > 0){
				map.put("grade", "지사");
				map.put("email", rset.getRowFirst().getString("email"));
				map.put("tel1", rset.getRowFirst().getString("tel1"));
				map.put("tel2", rset.getRowFirst().getString("tel2"));
			}
		}
		super.setOrderBy("");
		super.initRecord();
		
		return map;
	}
	
	/*
	 * 전체 전송
	 */
	public List<SharedMap<String,Object>> checkIdAll(String grade){
		String q = "SELECT DISTINCT email, tel1, tel2 FROM PG_NOTICE_LIST WHERE grade = '"+grade+"'";
		
		RecordSet rset = super.query(q);
		super.initRecord();
		
		return rset.getRows();
	}
	
	/*
	 * 공지사항 전송 내역 INSERT 
	 */
	public boolean insertNotice(SharedMap<String, Object> map) {
		super.setTable("PG_NOTICE_SEND");
	
		super.setRecord("grade", map.getString("grade"));
		super.setRecord("content", map.getString("content"));
		super.setRecord("email", map.getString("email"));
		super.setRecord("tel1", map.getString("tel1"));
		if(map.getString("tel2") != null || map.getString("tel2") != "") {
			super.setRecord("tel2", map.getString("tel2"));
		}
		super.setRecord("regId", map.getString("regId"));
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		super.setRecord("regTime", CommonUtil.getCurrentDate("HHmmss"));
		
		boolean inserted = super.insert();
		logger.info("INSERT PG_NOTICE_SEND : {}", inserted);
		
		super.initRecord();
		return inserted;
	}
	
	/*
	 * 공지사항 전송 내역 조회
	 */
	public RecordSet sendList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("PG_NOTICE_SEND");
		super.setColumns("*");
		super.setOrderBy("regDay desc, regTime desc");
		
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet sendListMcht(){
		super.setTable("PG_NOTICE_SEND");
		super.setColumns("*");
		super.addWhere("grade","가맹점");
		super.setLimit(1);
		super.setOrderBy("regDay desc, regTime desc");
		
		return search();
	}
	//공지사항 전송 =======================================================
}