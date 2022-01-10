package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.DataSet;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class MchtDdctDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtDdctDAO.class );
	private static final String TABLE = "VW_MCHT_DDCT";
	private static final String COLUMNS = "*";
	
	public MchtDdctDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtDdctDAO.COLUMNS);
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getDdctId(String ddctId) {
		addWhere("ddctId",ddctId,eq);
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

	public RecordSet getCode() {
		super.setTable("PG_CODE");
		super.setColumns("*");
		super.addWhere("alias","DDCT",eq);
		super.setOrderBy("code asc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public String getSettleDay(String today) {	
		String q = "SELECT days FROM PG_CODE_HOLIDAY WHERE days >= '"+today+"' AND status ='no' limit 1";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("days");
	}

	public synchronized static String getDdctId() {
		return "D" + getFunction("FN_NEXTVAL2", "DDCT");
	}
	
	public synchronized static String getSettleSchId(int i) {
		return "SC"+ CommonUtil.getCurrentDate("yyyyMMddHHmmss")+ CommonUtil.zerofill(i, 3);
	}
	
	public static String getFunction(String function, String value) {
		String returnVal = "";
		String query = "SELECT " + function + "(?) as val";

		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rset = null;

		try {

			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, value);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				returnVal = rset.getString(1);
			}
			conn.commit();
		} catch (Exception t) {
			
		} finally {
			db.close(conn, pstmt, rset);
		}
		return returnVal;
	}

	public RecordSet getSchedule(String ddctId) {
		super.setTable("PG_SETTLE_DDCT");
		super.setColumns("*");
		super.addWhere("ddctId",ddctId,eq);
		super.setOrderBy("stlDay asc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public RecordSet getScheduleDate(String ddctId) {
		super.setTable("PG_SETTLE_DDCT");
		super.setColumns("MIN(stlDay) as startDate");
		super.addWhere("ddctId",ddctId,eq);
		super.addWhere("stlStatus","정산대기",eq);
		super.setOrderBy("stlDay asc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public long getCompleteAmt(String ddctId) {
		super.setTable("PG_SETTLE_DDCT");
		super.setColumns("SUM(ddctAmt) AS amt");
		super.addWhere("ddctId", ddctId, eq);
		super.addWhere("stlStatus","정산완료", eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst().getLong("amt");
	}

	public String getStartMonth(String ddctId) {
		super.setTable("PG_SETTLE_DDCT");
		super.setColumns("MIN(stlDay) as startDate");
		super.addWhere("ddctId",ddctId,eq);
		super.addWhere("stlStatus","정산대기",eq);
		super.setOrderBy("stlDay asc");
		RecordSet rset = super.search();
		super.initRecord();
		String startDate = rset.getRowFirst().getString("startDate");
		if(CommonUtil.isNullOrSpace(startDate)) {
			super.setTable("PG_SETTLE_DDCT");
			super.setColumns("MAX(stlDay) as startDate");
			super.addWhere("ddctId",ddctId,eq);
			super.addWhere("stlStatus","정산완료",eq);
			super.setOrderBy("stlDay asc");
			RecordSet rset2 = super.search();
			super.initRecord();
			startDate = rset2.getRowFirst().getString("startDate");
			
			Date date = CommonUtil.getDate("yyyyMMdd", startDate);
			Calendar cal= Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			startDate = format.format(cal.getTime());
		}
		
		return startDate.substring(0, 6);
	}

	public boolean ddctDelete(String ddctId) {
		super.setTable("PG_MCHT_DDCT");
		super.addWhere("ddctId", ddctId);
		boolean result = super.delete();
		super.initRecord();
		return result;
	}

	public String getSettleNextDay(String stlDay) {
	
		String q = "SELECT days FROM PG_CODE_HOLIDAY WHERE days > '"+stlDay+"' AND status ='no' limit 1";
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRow(0).getString("days");
	}

	public SharedMap<String, Object> getScheduleByScheId(String scheId) {
		super.setTable("PG_SETTLE_DDCT");
		super.setColumns("*");
		super.addWhere("scheId", scheId);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRowFirst();
	}
	
	public boolean updateSchedule(SharedMap<String, Object> map) {
		super.setTable("PG_SETTLE_DDCT");
		super.setRecord("stlDay", map.getString("stlDay"));
		super.setRecord("ddctAmt", map.getLong("ddctAmt"));
		super.setRecord("regId", map.getString("regId"));
		super.setRecord("regDay", map.getString("regDay"));
		super.addWhere("scheId", map.getString("scheId"));
		boolean result = super.update();
		super.initRecord();
		if(result) {
			return updateTotalAmt(map);
		}else {
			return result;
		}
	}

	private boolean updateTotalAmt(SharedMap<String, Object> map) {
		long totalAmt = super.query("SELECT SUM(ddctAmt) AS totalAmt FROM PG_SETTLE_DDCT where ddctId = '"+map.getString("ddctId")+"'").getRowFirst().getLong("totalAmt");
		super.initRecord();
		super.setTable("PG_MCHT_DDCT");
		super.setRecord("totalAmt", totalAmt);
		super.addWhere("ddctId", map.getString("ddctId"), eq);
		boolean result = super.update();
		super.initRecord();
		return result;
		
	}

}