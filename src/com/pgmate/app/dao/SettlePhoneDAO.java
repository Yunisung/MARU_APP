package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class SettlePhoneDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.SettlePhoneDAO.class );
	private static final String TABLE = "PG_SETTLE_PHONE";
	private static final String COLUMNS = "*";

	public SettlePhoneDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(SettlePhoneDAO.COLUMNS);
		super.setOrderBy("regDate desc");
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
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
	
	/**
	 * 정산아이디 생성
	 * @return
	 */
	public synchronized static String getSettleId() {
		return "S" + getFunction("FN_NEXTVAL2", "SETTLE");
	}
	
	public RecordSet calcSettlePhoneList(List<Data> datas) {
		super.setTable("PG_SETTLE_PHONE");
		super.setColumns("sum(payAmt+rfdAmt) as amtSum, sum(payFee + payVat) + sum(rfdFee + rfdVat) as vatSum, sum(stlAmt) as stlAmtSum");

		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 휴대폰정신 데이터 검색
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("(SELECT A.*, B.name FROM PG_SETTLE_PHONE A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A ");
		super.setColumns("A.stlId, A.mchtId,A.name,A.stlDay, A.startDay, A.endDay, A.payAmt, A.payFee, A.payVat, A.payCnt, A.rfdAmt, A.rfdFee, A.rfdVat, A.rfdCnt,"
				+ "(A.payCnt + A.rfdCnt) as totCnt, (A.payAmt + A.rfdAmt) as totAmt, (A.payFee + A.payVat) + (A.rfdFee + A.rfdVat) as totalFee,"
				+ "A.stlAmt,A.benefit,A.bankCd,A.bankName,FN_MASK_IDENTIFY(A.account) as maskAccount,FN_AES_DEC(A.account) AS account,FN_AES_DEC(A.accntHolder) AS accntHolder, "
				+ "A.stlRate,A.stlType, A.regId,A.regDate");
		super.setOrderBy("A.stlId");
		
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	
	/**
	 * 가맹점 아이디로 가맹점 TAX 정보(PG_MCHT_TAX) 테이블 데이터 조회
	 * @param mchtId : 가맹점 아이디
	 * @return
	 */
	public SharedMap<String, Object> getMchtTaxByMchtId(String mchtId) {
		super.setTable("PG_MCHT_TAX");
		super.setColumns("*");
		super.addWhere("mchtId", mchtId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0){
			return rset.getRow(0);
		}else{
			return new SharedMap<String,Object>();
		}
	}
	
	/**
	 * 휴대폰 매입원장에 거래번호로 정산일자 업데이트
	 * @return
	 */
	public boolean updateStlDay(String trxId, String stlDay, boolean sunabCheck){
		super.setTable("PG_PHONE_CAP");
		super.setRecord("stlDay",stlDay);
		if(!sunabCheck) {
			super.setRecord("stlStatus","미납");
		}else {
			super.setRecord("stlStatus","정산대기");
		}
		super.addWhere("trxId", trxId);
		super.addWhere("stlStatus != '정산완료'");
		super.addWhere("stlYn", "Y");
		
		boolean updated =  super.update();
		
		super.initRecord();
		return updated;
	}
	
	/**
	 * 휴대폰 매입원장에서 정산예정일자의 정산데이터 조회
	 * @return
	 */
	public List<SharedMap<String,Object>> getPhoneSettleList(String stlDay){
		String q = "select X.* "
				+"    from "
				+"	  (SELECT mchtId,stlDay,min(trxDay) startDay,max(trxDay) endDay, "
				+"		  	  SUM(IF(capType ='매입',amount,0)) as payAmt, SUM(IF(capType ='매입',stlFee,0)) as payFee, SUM(IF(capType ='매입',stlFeeVat,0)) as payVat, SUM(IF(capType ='매입',1,0)) as payCnt,"
				+"		  	  SUM(IF(capType ='매입취소',amount,0)) as rfdAmt, SUM(IF(capType ='매입취소',stlFee,0)) as rfdFee, SUM(IF(capType ='매입취소',stlFeeVat,0)) as rfdVat, SUM(IF(capType ='매입취소',1,0)) as rfdCnt,"
				+"			  SUM(stlAmount) as stlAmount, sum(benefit) as benefit, MAX(stlRate) as stlRate, stlType"
				+"	 	 FROM PG_PHONE_CAP "
				+"  	where stlDay = '" + stlDay + "' and stlType = '1' and stlStatus = '정산대기' and stlYn = 'Y'"
				+"  	group by mchtId, stlDay, stlType"
				+"  	order by mchtId) X;";
		
		RecordSet rset = super.query(q);
		super.initRecord();
		
		return rset.getRows();
	}
	
	/**
	 * 수납정산 휴대폰정산 테이블 insert전에 이미 같은달에 들어간 데이터 있는지 확인
	 * @param mchtId
	 * @param stlDay
	 * @return
	 */
	public SharedMap<String, Object> getSettlePhoneCheck(String mchtId, String stlDay) {
		super.setTable("PG_SETTLE_PHONE");
		super.setColumns("*");
		super.addWhere("mchtId", mchtId, eq);
		super.addWhere("left(stlDay,6)", stlDay, eq);
		
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0){
			return rset.getRow(0);
		}else{
			return new SharedMap<String,Object>();
		}
	}
	
	/**
	 * 휴대폰정산 데이터 (PG_SETTLE_PHONE) 테이블 INSERT
	 * @param data
	 * @return
	 */
	public boolean insertSettlePhone(SharedMap<String,Object> data){
		super.setTable("PG_SETTLE_PHONE");
		
		for(String key : data.keySet()){
			super.setRecord(key, data.get(key));
		}
		
		boolean inserted =  super.insert();
		
		super.initRecord();
		return inserted;
	}
	
	/**
	 * 수납정산 매입데이터 업데이트
	 * @param stlId : 정산번호
	 * @param stlDay : 정산예정일자
	 * @param stlType : 정산타입
	 * @param mchtId : 가맹점아이디
	 * @return
	 */
	public boolean updatePhoneCapUpdate(String stlId, String stlDay, String stlType, String mchtId, String stlStatus){
		String q = "UPDATE PG_PHONE_CAP "
				+ "    SET stlId = '" + stlId +"', stlStatus = '" + stlStatus + "'"
				+ "	 WHERE mchtId = '" + mchtId + "' and stlDay = '" + stlDay + "' and stlType = '" + stlType + "' and stlStatus != '정산완료';";
		
		boolean updateed =  super.update(q);

		super.initRecord();
		return updateed;
	}
	
	/**
	 * 수납정산 등록 결과 
	 * @param stlDay
	 * @return
	 */
	public RecordSet calcSettlePhoneSunabList(List<Data> datas) {
		String stlDay = "";
		
		for(Data data:datas){
			stlDay = data.val.toString();
		}
		
		super.setTable("PG_PHONE_CAP");
		super.setColumns("count(*) as totCnt,ifnull(sum(IF(stlStatus ='정산완료',1,0)),0) as sunabCnt,ifnull(sum(IF(stlStatus ='미납',1,0)),0) as minabCnt,ifnull(sum(IF(stlStatus ='정산대기',1,0)),0) as emptCnt ");
		super.addWhere("left(trxDay,6) = '" +stlDay + "'");
		super.addWhere("stlType", "1");
		super.addWhere("stlStatus", "정산완료");
		super.addWhere("stlYn", "Y");
		
		Page page = new Page();
		page.size = 999999999;
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 휴대폰정신 누락거래 데이터 검색
	 * @param datas
	 * @param page
	 * @return
	 */
	public RecordSet emptDataList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		String stlDay = "";
		
		for(Data data:datas){
			stlDay = data.val.toString();
		}
		
		super.setTable("(SELECT A.*, B.name FROM PG_PHONE_CAP A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A");
		super.setColumns("A.*");
		super.addWhere("left(A.trxDay,6) = '" +stlDay + "'");
		super.addWhere("A.stlType", "1");
		super.addWhere("A.stlStatus", "정산대기");
		super.addWhere("stlYn", "Y");
		super.setOrderBy("A.stlId");
		
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet calcSettleSnabAgainList(List<Data> datas) {
		super.setTable("PG_SETTLE_PHONE");
		super.setColumns("sum(payAmt+rfdAmt) as amtSum, sum(payFee + payVat) + sum(rfdFee + rfdVat) as vatSum, sum(stlAmt) as stlAmtSum");

		Page page = new Page();
		page.size = 999999999;
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 휴대폰 수납정산 누적수납 조회
	 * @return
	 */
	public RecordSet snabAgainList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		String trxDay = "";
		String mchtId = "";
		String mchtName = "";
		
		for(Data data:datas){
			if("trxDay".equals(data.name)) {
				trxDay = data.val.toString();
				trxDay = trxDay + "01";
			}else if("mchtId".equals(data.name)) {
				mchtId = data.val.toString();
			}else if("name".equals(data.name)) {
				mchtName = data.val.toString();
			}
		}
		
		super.setTable("(SELECT left(A.trxDay,6) as trxDay, A.name, A.mchtId, SUM(A.amount) as amount,"
					+ 			"sum(IF(stlDay ='" + trxDay + "', IF(stlStatus ='정산완료',A.amount,0), 0)) as m0_sunab, "
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 1 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m1_sunab, "
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 2 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m2_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 3 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m3_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 4 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m4_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 5 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m5_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 6 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m6_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 7 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m7_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 8 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m8_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 9 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m9_sunab,"
					+ 			"sum(IF(left(stlDay,6) = DATE_FORMAT(DATE_ADD('" + trxDay + "', INTERVAL 10 MONTH), '%Y%m'), IF(stlStatus ='정산완료',A.amount,0), 0)) as m10_sunab"
					+ 	" FROM (SELECT A.*, B.name FROM PG_PHONE_CAP A JOIN VW_MCHT B ON A.mchtId = B.mchtId) A"
					+  " where left(trxDay,6) = left('" + trxDay + "',6) and stlType = '1' and stlYn = 'Y'"
					+  " group by mchtId"
					+  " order by stlId"
					+ ") B ");
		super.setColumns("trxDay, mchtId, name, amount,"
					+ 	 "m0_sunab, ifnull((m0_sunab / amount * 100),0) as m0_percent, m1_sunab, ifnull((m1_sunab / amount * 100),0) as m1_percent, m2_sunab, ifnull((m2_sunab / amount * 100),0) as m2_percent,"
					+ 	 "m3_sunab, ifnull((m3_sunab / amount * 100),0) as m3_percent, m4_sunab, ifnull((m4_sunab / amount * 100),0) as m4_percent, m5_sunab, ifnull((m5_sunab / amount * 100),0) as m5_percent,"
					+ 	 "m6_sunab, ifnull((m6_sunab / amount * 100),0) as m6_percent, m7_sunab, ifnull((m7_sunab / amount * 100),0) as m7_percent, m8_sunab, ifnull((m8_sunab / amount * 100),0) as m8_percent,"
					+	 "m9_sunab, ifnull((m9_sunab / amount * 100),0) as m9_percent, m10_sunab, ifnull((m10_sunab / amount * 100),0) as m10_percent,"
					+	 "(amount - m0_sunab - m1_sunab - m2_sunab - m3_sunab - m4_sunab - m5_sunab - m6_sunab - m7_sunab - m8_sunab - m9_sunab - m10_sunab) as minabAmt,"
					+	 "(m0_sunab + m1_sunab + m2_sunab + m3_sunab + m4_sunab + m5_sunab + m6_sunab + m7_sunab + m8_sunab + m9_sunab + m10_sunab) as SunabSumAmt,"
					+	 "ifnull(((m0_sunab + m1_sunab + m2_sunab + m3_sunab + m4_sunab + m5_sunab + m6_sunab + m7_sunab + m8_sunab + m9_sunab + m10_sunab) / amount * 100),0) as tot_percent");
		
		if(!"".equals(mchtId)) {
			super.addWhere("mchtId like '%" + mchtId + "%'");
		}
		if(!"".equals(mchtName)) {
			super.addWhere("name like '%" + mchtName + "%'");
		}
		super.setOrderBy("mchtId");
		
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
}