package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.cache.Cache;
import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//KJM : 가맹점 터미널 정보
public class MchtTmnDAO extends DAO{
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtTmnDAO.class );
	private static final String TABLE = "VW_MCHT_TMN";
	private static final String COLUMNS = "*";
	
	public MchtTmnDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtTmnDAO.COLUMNS);
	}
	
	//KJM : 단말기아이디에 대한 터미널 정보
	public RecordSet getById(String tmnId){
		addWhere("tmnId",tmnId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
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

	public boolean insertDefault(String tmnId, String pk, String mchtId, String taxId, String serial) {
		String query = "INSERT INTO PG_MCHT_TMN	(`tmnId`,`mchtId`,`taxId`,`status`, `serial`,`payKey`,`activeDate`,`regId`,`regDay`,`regDate`) "
					 + "SELECT '"+tmnId+"', mchtId, '"+taxId+"', '사용','"+serial+"','"+pk+"',regDay,regId,regDay,regDate "
					 + "FROM PG_MCHT WHERE mchtId = '"+mchtId+"'";
		
		return new CPDAO().update(query); 
	}
	
	/**
	 * 터미널에서 웹결제창이 '사용'이고 터미널상태가 '사용'일때 결제키를 리턴
	 * <pre>
	 * SELECT * FROM VW_MCHT_TMN WHERE LOWER(mchtId) = '아이디' AND STATUS = '사용' AND webpay = '사용'
	 * </pre>
	 * @param mchtId
	 * @return
	 */
	public String getWebPay(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		addWhere("status","사용",eq);
		addWhere("webPay","사용",eq);
		RecordSet rset = search();
		if(rset.size() == 0){
			return "";
		}else{
			return rset.getRow(0).getString("payKey");
		}
		
	}

	public String getTmnWebPay(String tmnId){
		addWhere("lower(tmnId)",tmnId.toLowerCase(),eq);
		addWhere("status","사용",eq);
		addWhere("webPay","사용",eq);
		RecordSet rset = search();
		if(rset.size() == 0){
			return "";
		}else{
			return rset.getRow(0).getString("payKey");
		}
		
	}
	
	public RecordSet getHtByMchtId(String mchtId) {
		setTable("HT_MCHT_TMN");
		setColumns("*");
		addWhere("lower(mchtId)", mchtId.toLowerCase(), eq);
		return search();
	}

	public RecordSet getHtVactByMchtId(String mchtId) {
		setTable("HT_MCHT_MNG_VACT");
		setColumns("*");
		addWhere("lower(mchtId)", mchtId.toLowerCase(), eq);
		return search();
	}
	
	public RecordSet getPgByMchtId(String mchtId) {
		setTable("PG_MCHT_TMN");
		setColumns("*");
		addWhere("lower(mchtId)", mchtId.toLowerCase(), eq);
		return search();
	}

	public RecordSet getVactByMchtId(String mchtId) {
		setTable("PG_MCHT_MNG_VACT");
		setColumns("*");
		addWhere("lower(mchtId)", mchtId.toLowerCase(), eq);
		return search();
	}
	
	public String getNewId() {
		//KBR : 왼쪽으로 0을 넣어서 총 6자리 수를 String으로 만들기
		return "TMN" + String.format("%06d", Integer.parseInt(getFunction("FN_NEXTVAL", "TERMINAL")));
	}
	
	public List<SharedMap<String, Object>> getTmnList(String tmnArr) {
		super.setDebug(true);
		super.setTable("VW_MCHT_TMN");
		super.setColumns("*");
		super.addWhere("tmnId",tmnArr,in);
		RecordSet rset = super.search();
		super.initRecord();
		return rset.getRows();
	}
	
	public int insetMchtTmnBatch(List<SharedMap<String, Object>> tmnLists) {
		int inserted = 0;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query = "INSERT INTO `PG_MCHT_TMN` (`tmnId`, `mchtId`, `taxId`, `status`, `serial`, `payKey`, `activeDate`, `apiMaxInstall`, `webPay`, `appDirect`, `semiAuth`, `refundType`, `van`, `vanIdx`, `ccType`, `description`, `payLimit`, `limitAmount`, `limitStartTime`, `limitEndTime`, `regId`, `regDay`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
		try {
			int batchSize = 100;
			int count = 0;
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			for(SharedMap<String, Object> map:tmnLists) {
				int i =1;
				pstmt.setString(i++, map.getString("tmnId"));
				pstmt.setString(i++, map.getString("mchtId"));
				pstmt.setString(i++, map.getString("taxId"));
				pstmt.setString(i++, map.getString("status"));
				pstmt.setString(i++, map.getString("serial"));
				pstmt.setString(i++, map.getString("payKey"));
				pstmt.setString(i++, map.getString("activeDate"));
				pstmt.setInt(i++, map.getInt("apiMaxInstall"));
				pstmt.setString(i++, map.getString("webPay"));
				pstmt.setString(i++, map.getString("appDirect"));
				pstmt.setString(i++, map.getString("semiAuth"));
				pstmt.setString(i++, map.getString("refundType"));
				pstmt.setString(i++, map.getString("van"));
				pstmt.setInt(i++, map.getInt("vanIdx"));
				pstmt.setString(i++, map.getString("ccType"));
				pstmt.setString(i++, map.getString("description"));
				pstmt.setString(i++, map.getString("payLimit"));
				pstmt.setInt(i++, map.getInt("limitAmount"));
				pstmt.setString(i++, map.getString("limitStartTime"));
				pstmt.setString(i++, map.getString("limitEndTime"));
				pstmt.setString(i++, map.getString("regId"));
				pstmt.setInt(i++, map.getInt("regDay"));
				
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			inserted += pstmt.executeBatch().length;
			conn.commit();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			db.close(pstmt);
			db.close(conn);
		}
		logger.info("inserted tmn [{}]",inserted);
		return inserted;
	}

	public SharedMap<String, Object> getBank(String bankCd) {
		String key = "PG_CODE_BANK_" + bankCd;
		logger.info(key);
		if (Cache.map.containsKey(key)) {
			return Cache.map.getUnchecked(key);
		} else {
			super.setTable("PG_CODE");
			super.setColumns("*");
			super.addWhere("code", bankCd, eq);
			super.addWhere("alias", "BANK", eq);
			super.setOrderBy("");
			RecordSet rset = super.search();
			super.initRecord();
			if(rset.size() > 0){
			return Cache.map.put(key, rset.getRow(0));
			}else{
				return new SharedMap<String,Object>();
			}
		}
	}

	public int insetMchtTmnDtlBatch(List<SharedMap<String, Object>> dtlList) {
		int inserted = 0;
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query = "INSERT INTO `PG_MCHT_TMN_DTL` (`tmnId`, `name`, `rate`, `ceoName`, `identity`, `ceoPhone`, `email`, `bankCd`, `bankName`, `account`, `accntHolder`, `tel`, `zip`, `addr1`, `addr2`, `rctName`, `rctCeoName`, `rctIdentity`, `rctTelNo`, `rctAddr`, `regId`, `regDay`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
		try {
			int batchSize = 100;
			int count = 0;
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			for(SharedMap<String, Object> map:dtlList) {
				int i =1;
				pstmt.setString(i++, map.getString("tmnId"));
				pstmt.setString(i++, map.getString("name"));
				pstmt.setDouble(i++, map.getDouble("rate"));
				pstmt.setString(i++, map.getString("ceoName"));
				pstmt.setString(i++, map.getString("identity"));
				pstmt.setString(i++, map.getString("ceoPhone"));
				pstmt.setString(i++, map.getString("email"));
				pstmt.setString(i++, map.getString("bankCd"));
				pstmt.setString(i++, map.getString("bankName"));
				pstmt.setString(i++, map.getString("account"));
				pstmt.setString(i++, map.getString("accntHolder"));
				pstmt.setString(i++, map.getString("tel"));
				pstmt.setString(i++, map.getString("zip"));
				pstmt.setString(i++, map.getString("addr1"));
				pstmt.setString(i++, map.getString("addr2"));
				pstmt.setString(i++, map.getString("rctName"));
				pstmt.setString(i++, map.getString("rctCeoName"));
				pstmt.setString(i++, map.getString("rctIdentity"));
				pstmt.setString(i++, map.getString("rctTelNo"));
				pstmt.setString(i++, map.getString("rctAddr"));
				pstmt.setString(i++, map.getString("regId"));
				pstmt.setInt(i++, map.getInt("regDay"));
				
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}
			inserted += pstmt.executeBatch().length;
			conn.commit();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			db.close(pstmt);
			db.close(conn);
		}
		logger.info("inserted tmn dtl [{}]",inserted);
		return inserted;
	}
}