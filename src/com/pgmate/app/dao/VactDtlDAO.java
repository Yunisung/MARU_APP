package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class VactDtlDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.VactDtlDAO.class );
	private static final String TABLE = "VW_VACT_DTL";
	private static final String COLUMNS = "*";
	/**
	 * 
	 */
	public VactDtlDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns("*");
		super.setOrderBy("regDay desc");
	}
	
	public List<SharedMap<String,Object>> getByMchtId(String mchtId){
		super.addWhere("mchtId",mchtId.toLowerCase(),eq);
		super.addWhere("vactType", "영구",eq );
		super.setOrderBy("regDate desc");
		return super.search().getRows();
	}

	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION
		return super.search();
	}
	
	public RecordSet list(List<Data> datas, Page page) {
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); //DATA to CONDITION
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

	public int insert(String mchtId, String bankCd, long cnt, String holderName, String userId) {
		super.setTable("VW_VACT_UNUSED");
		super.setColumns("*");
		super.addWhere("bankCd", bankCd);
		super.setLimit(cnt);

		List<SharedMap<String, Object>> targetList = super.search().getRows();
		if (holderName.isEmpty()) {
			super.setTable("PG_MCHT");
			super.setColumns("name");
			super.addWhere("mchtId", mchtId);
			holderName = super.search().getRow(0).getString("name");	
		}

		int inserted = 0;
		logger.debug("insert PG_VACT_DTL batch: {}", targetList.size());
		String query = "INSERT INTO PG_VACT_DTL (issueId, account, vactType, status, mchtId, holderName, amount, oper, expireAt, regId, regDay) "
		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?);";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;
			String expire = (Integer.parseInt(CommonUtil.getCurrentDate("yyyy")) + 1) + CommonUtil.getCurrentDate("MMdd");

			for (SharedMap<String, Object> map : targetList) {
				String issueId = TrxDAO.getIssueId();
				int i = 1;
				pstmt.setString(i++, issueId);
				pstmt.setString(i++, map.getString("account"));
				pstmt.setString(i++, "영구");
				pstmt.setString(i++, "대기");
				pstmt.setString(i++, mchtId);
				pstmt.setString(i++, holderName);
				pstmt.setString(i++, "0");
				pstmt.setString(i++, "ge");
				pstmt.setString(i++, expire + "00");
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
			logger.debug("insert batch PG_VACT_DTL error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
}

