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
 * 210812_PYS : 가맹점 예수금 관리
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
	
	/**
	 * 210812_PYS : 예수금번호로 예수금 조회
	 * <pre>
	 * SELECT * FROM PG_MCHT_DEPOSIT WHERE depositId = 'depositId'
	 * </pre>
	 * @param depositId : 예수금 번호
	 * @return
	 */
	public RecordSet getById(String depositId){
		addWhere("depositId",depositId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * 210812_PYS : 가맹점 아이디로 예수금 조회
	 * <pre>
	 * SELECT * FROM PG_MCHT_DEPOSIT WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점 아이디
	 * @return
	 */
	public RecordSet getByMchtId(String mchtId){
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * 210812_PYS : data클래스로 예수즘 조회
	 * @param datas
	 * @return
	 */
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	/**
	 * 210812_PYS : data클래스로 예수즘 조회 (페이징)
	 * @param datas
	 * @return
	 */
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 210812_PYS : 입금차액이 0보다 크고 가맹점ID가 'collectId'인 가맹점을 예수금 관리 테이블(PG_MCHT_DEPOSIT)에 추가
	 * @param collectId : 가맹점ID
	 * @param userId : 로그인ID
	 * @return
	 */
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
	
	/**
	 * 210812_PYS : 반환완료된 매입건을 예수금 관리 테이블(PG_MCHT_DEPOSIT)에 추가
	 * @param stlId : 반환 정산번호
	 * @param userId : 로그인ID
	 * @return
	 */
	//KJM : 반환완료된 매입건에 대한 리스트를 조회 한 뒤 조회 된 리스트를 예수금 관리 테이블에 추가해 줌
	public int setAddByMchtSettle(String stlId, String userId) {
		super.setTable("PG_SETTLE_HOLD A JOIN VW_TRX_CAP B ON A.capId = B.capId");
		super.setColumns("B.mchtId, '반환' as depType, -B.stlAmount as amount, '정산' as createType, A.stlId as createtId, '정산반환' as summary");
		super.setWhere("A.stlId IN (" + stlId +")");
		super.addWhere("A.status", "반환완료");
		super.setOrderBy("");
		//KJM : select 쿼리문 수행
		RecordSet rset =  super.search();
		//KJM : 조회 된 리스트가 없으면 0반환, 있으면 예수금 관리 테이블에 추가
		if(rset.size() < 1) {
			return 0;
		}
		super.initRecord();
		return insertDeposit(rset.getRows(), userId);
	}
	
	
	/**
	 * 210812_PYS : 가맹점 예수금 관리 테이블(PG_MCHT_DEPOSIT)에 데이터 추가
	 * @param eachList : 추가하는 데이터
	 * @param userId : 로그인ID
	 * @return
	 */
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
				//KJM : 등록일이 없을 경우 현재날짜 값 넣어줌
				if(!map.isNullOrSpace("regDay")) {
					pstmt.setString(i++, map.getString("regDay"));
				} else {
					pstmt.setString(i++, CommonUtil.getCurrentDate("yyyyMMdd"));
				}
				
				//KJM : 수기생성이고 수기반환일 경우 등록일과 시간값, 아니면 빈값 넣어줌
				if(map.isEquals("createType", "수기") && map.getString("depType").equals("수기반환")) {
					pstmt.setString(i++, map.getString("regDay"));
					pstmt.setString(i++, CommonUtil.getCurrentDate("HHmmss"));
				} else {
					pstmt.setString(i++, "");
					pstmt.setString(i++, "");
				}
				
				//KJM : 상태값이 없을 경우 '생성', 있을 경우 해당하는 상태값으로 값 세팅
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




