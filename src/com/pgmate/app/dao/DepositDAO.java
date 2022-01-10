package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class DepositDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.DepositDAO.class );
	private static final String TABLE = "PG_MCHT_DEPOSIT";
	private static final String COLUMNS = "*";
	
	public DepositDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(DepositDAO.COLUMNS);
	}
	
	public RecordSet getById(String depositId){
		addWhere("depositId",depositId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("mchtId",mchtId.toLowerCase(),eq);
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
	
	public int setAddByCollect(String collectId, String userId) {
		super.setTable("VW_COLLECT_SETTLE_DTL A JOIN VW_TRX_CAP B ON A.collectDay = B.stlVanDay AND A.mchtId = B.mchtId AND A.vanId = B.vanId AND B.risk <> ''");
		super.setColumns("A.mchtId, '보류' as depType, SUM(B.stlAmount) as amount, '입금' as createType, A.collectId as createtId, A.summary");
		super.setWhere("A.deductAmount > 0");
		super.addWhere("A.collectId", collectId);
		super.setGroupBy("A.collectId, A.mchtId");
		super.setOrderBy("");
		RecordSet rset =  super.search();
		if(rset.size() < 1) {
			return 0;
		}
		super.initRecord();
		return insertDeposit(rset.getRows(), userId);
	}
	
	public int setAddByMchtSettle(String stlId, String userId) {
		super.setTable("PG_SETTLE_HOLD A JOIN VW_TRX_CAP B ON A.capId = B.capId");
		super.setColumns("B.mchtId, '반환' as depType, -B.stlAmount as amount, '정산' as createType, A.stlId as createtId, '정산반환' as summary");
		super.setWhere("A.stlId IN (" + stlId +")");
		super.addWhere("A.status", "반환완료");
		super.setOrderBy("");
		RecordSet rset =  super.search();
		if(rset.size() < 1) {
			return 0;
		}
		super.initRecord();
		return insertDeposit(rset.getRows(), userId);
	}
	
	public int insertDeposit(List<SharedMap<String, Object>> eachList, String userId) {
		int inserted = 0;
		logger.debug("insert PG_MCHT_DEPOSIT batch : {}", eachList.size());
		String query = "INSERT INTO `PG_MCHT_DEPOSIT` (depositId, mchtId, depType, amount, createType, createtId, summary, regId, regDay, outDay, outTime, status) "
						+ " VALUES ( FN_NEXTVAL2('DEPOSIT') ,?,?,?,?,?,?,?,?,?,?,?);";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;

			for (SharedMap<String, Object> map : eachList) {
				int i = 1;
				pstmt.setString(i++, map.getString("mchtId"));
				pstmt.setString(i++, map.getString("depType"));
				pstmt.setString(i++, map.getString("amount"));
				pstmt.setString(i++, map.getString("createType"));
				pstmt.setString(i++, map.getString("createtId"));
				pstmt.setString(i++, map.getString("summary"));
				pstmt.setString(i++, userId);
				if(!map.isNullOrSpace("regDay")) {
					pstmt.setString(i++, map.getString("regDay"));
				} else {
					pstmt.setString(i++, CommonUtil.getCurrentDate("yyyyMMdd"));
				}
				
				if(map.isEquals("createType", "수기") && map.getString("depType").equals("수기반환")) {
					pstmt.setString(i++, map.getString("regDay"));
					pstmt.setString(i++, CommonUtil.getCurrentDate("HHmmss"));
				} else {
					pstmt.setString(i++, "");
					pstmt.setString(i++, "");
				}
				
				if(map.isNullOrSpace("status")) {
					pstmt.setString(i++, "생성");
				} else {
					pstmt.setString(i++, map.getString("status"));
				}
				
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch PG_MCHT_DEPOSIT error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}

}




