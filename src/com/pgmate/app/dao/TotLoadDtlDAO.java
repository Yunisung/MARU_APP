package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
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
public class TotLoadDtlDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TotLoadDtlDAO.class );
	private static final String TABLE = "VW_TRX_LOAD_DTL";
	private static final String COLUMNS = "*";
	public TotLoadDtlDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TotLoadDtlDAO.COLUMNS);
	}
	
	public RecordSet getByIdx(String idx){
		addWhere("idx",idx,eq);
		return search();
	}
	
	public RecordSet getByBatchIdx(String batchIdx){
		addWhere("batchIdx",batchIdx,eq);
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
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public int insert(List<SharedMap<String, Object>> eachList, String batchIdx, String userId) {
		int inserted = 0;
		logger.debug("insert PG_TRX_LOAD_DTL batch : {}", eachList.size());
		String query = "INSERT INTO `PG_TRX_LOAD_DTL` (batchIdx,trnType,trackId,amount,installment,cardType,bin,last4,authCd,trxDay,trxTime,rootTrxDay,vanTrxId,vanDay,vanStlFee,issuer,acquirer,regId,regDay) "
						 + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
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
				pstmt.setString(i++, batchIdx);
				pstmt.setString(i++, map.getString("trnType"));
				pstmt.setString(i++, map.getString("trackId"));
				if(map.isEquals("trnType", "승인취소") && map.getLong("amount") > 0) {
					pstmt.setLong(i++, -map.getLong("amount"));
				} else {
					pstmt.setLong(i++, map.getLong("amount"));
				}
				pstmt.setString(i++, map.getString("installment"));
				pstmt.setString(i++, CommonUtil.nToB(map.getString("cardType")));
				pstmt.setString(i++, map.getString("bin"));
				pstmt.setString(i++, map.getString("last4"));
				pstmt.setString(i++, map.getString("authCd"));
				pstmt.setString(i++, map.getString("trxDay"));
				pstmt.setString(i++, map.getString("trxTime"));
				pstmt.setString(i++, CommonUtil.nToB(map.getString("rootTrxDay")));
				pstmt.setString(i++, map.getString("vanTrxId"));
				pstmt.setString(i++, CommonUtil.nToB(map.getString("vanDay")));
				pstmt.setLong(i++, 	 map.getLong("vanStlFee"));
				
				pstmt.setString(i++, CommonUtil.nToB(map.getString("issuer")));
				pstmt.setString(i++, CommonUtil.nToB(map.getString("acquirer")));
				
				pstmt.setString(i++, userId);
				pstmt.setString(i++, CommonUtil.getCurrentDate("yyyyMMdd"));
				
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch PG_TRX_LOAD_DTL error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
}