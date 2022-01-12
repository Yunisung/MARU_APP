package com.pgmate.lib.util.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.BeanUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */

// KBR : DB와 연결 객체 생성후 DB연결하여 데이터 
public abstract class DBManager {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.db.DBManager.class);
	// KBR : DB 연결을 위한 정보 셋팅
	private static DBConfigBean dbConfigBean 		= null;
	private static DBManager dbManager 				= null;
	// KBR 로그를 쌓을 지 여부 
	private boolean debug							= false;
	private String error						= "";
	// KBR DB와의 연결 시간
	private int defaultTimeout						= 90000;  
	
	
	public static DBManager getManager(String dbName) throws Exception {
		
		dbConfigBean = ConfigLoader.getConfig().db;
		
		if(dbManager == null) {
			getInstance();
		}
		return dbManager;
	}
	
	private static void getInstance() throws Exception {
		synchronized(DBManager.class) {
			try{
				if(dbConfigBean.getDbType().equals("WEB")){
					dbManager = new JNDIManager(dbConfigBean);
				}else{
					dbManager = new JDBCManager(dbConfigBean);
				}
			}catch(Throwable ex){
				logger.debug("DB INSTANCE LOAD ERROR DEFAULT_DB CONFIG: {}, {} ",ex.getMessage(),BeanUtil.toString(dbConfigBean),null);
				throw new Exception("#### Can't initiate DB Connection Manager"+ex.getMessage(),ex);
			}
			
		}
	}
	
	public void close(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(ResultSet rSet){
		if(rSet != null){
			try{
				rSet.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(Statement stmt){
		if(stmt != null){
			try{
				stmt.close();
			}catch(SQLException e){}
		}
	}
		
	public void close(PreparedStatement pstmt){
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(CallableStatement cstmt){
		if(cstmt != null){
			try{
				cstmt.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(Connection conn,PreparedStatement pstmt,ResultSet rset){
		close(rset);
		close(pstmt);
		close(conn);
	}
	
	public void close(Connection conn,Statement stmt,ResultSet rset){
		close(rset);
		close(stmt);
		close(conn);
	}
	
	public void close(Connection conn,CallableStatement stmt,ResultSet rset){
		close(rset);
		close(stmt);
		close(conn);
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	public String getError(){
		return this.error;
	}
	
	//쿼리문 수행 후 수행결과로 Int타입의 값 반환
	public int preparedExecuteUpdate(String query) throws SQLException{
		
		PreparedStatement pstmt = null;
		Connection 	conn			= null;
		int result = 0;
		
		try {
			if(debug) logger.debug("query : [{}] ",query);
			conn		= getConnection();
			pstmt		= conn.prepareStatement(query);
			result  	= pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(pstmt);
			close(conn);
		}
		return result;
	}
	
	//쿼리문 수행 후 수행결과로 Int타입의 값 반환
	public int preparedExecuteUpdate(String query,SharedMap<String,Object> record) throws SQLException{
		PreparedStatement pstmt = null;
		Connection 	conn = null;
		int result = 0;
		
		// prapared 객체 값 셋팅위해 생성
		DBUtil dbUtil = new DBUtil();
		
		try {
			if(debug){logger.debug("query : [{}] ",query);}
			//getConnection() : Connection 객체 가져옴
			conn = getConnection();
			//만들어진 쿼리문 넣기
			pstmt = conn.prepareStatement(query);
			//쿼리문에 value 값 넣기
			dbUtil.setValues(pstmt, record);
			
			/*	
			 *  INSERT / DELETE / UPDATE 관련 구문에서는 반영된 레코드의 건수를 반환합니다.
			 *  CREATE / DROP 관련 구문에서는 -1 반환
			 */
			result  = pstmt.executeUpdate();
			
			//트랜잭션의 commit 수행
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			//초기화 진행
			close(pstmt);
			close(conn);
		}
		return result;
	}
	
	//테이블 마지막행 추가
	public long preparedExecuteUpdateAndLastIdx(String query,SharedMap<String,Object> record) throws SQLException{
		PreparedStatement pstmt = null;
		Connection 	conn			= null;
		ResultSet rset			= null;
		long result = 0;
		DBUtil dbUtil = new DBUtil();
		try {
			if(debug){logger.debug("query : [{}] ",query);}
			conn		= getConnection();
			//만들어진 쿼리문에 value값 세팅
			pstmt		= conn.prepareStatement(query);
			dbUtil.setValues(pstmt, record);
			//쿼리문 수행
			result  	= pstmt.executeUpdate();
			//테이블의 마지막 auto_increment 값 리턴(idx)
			rset		= pstmt.executeQuery("SELECT LAST_INSERT_ID() ");
			
			//추가된 인덱스 값 있을 경우
			while(rset.next()){
				//추가된 인덱스 값 result에 넣음
				result = rset.getLong(1);
			}
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		return result;
	}

	public int statementExecuteUpdate(String query)throws Exception{
		Statement stmt 	= null;
		Connection conn = null;
		int result = 0;

		try {
			if(debug){logger.debug("query : [{}] ",query);}
			conn		= getConnection();
			stmt		= conn.createStatement();
			result  	= stmt.executeUpdate(query);
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(stmt);
			close(conn);
		}
		return result;
	}
	
	public void setAutoCommit(Connection conn,boolean autoCommit) throws SQLException{
		conn.setAutoCommit(autoCommit);
	}
	
	public RecordSet statementExecute(String query)throws Exception{
		return statementExecute(query,defaultTimeout);
	}
	
	// 쿼리문 수행 후 객체의 값 반환
	public RecordSet statementExecute(String query,int timeout) throws Exception {
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = null;
		try {
			if(debug) logger.debug("query : [{}] ",query);
			conn = DBFactory.getInstance().getConnection();
			stmt = conn.createStatement();
			// KBR : 쿼리값을 더이상 받아오지 않는 타임아웃을 설정한다
			stmt.setQueryTimeout(timeout);
			// KBR : 쿼리값 ResultSet 타입으로 받아옴 
			stmt.executeQuery(query);
			// KBR : 테이블 형태로 받아온 값을 RecodeSet 객체에 담기
			rset = stmt.getResultSet();
			// KRB : row 값이 있을 경우
			if(rset != null){
				records = new RecordSet(rset);
			}
		}catch(SQLException e) {
			// KBR :  SQL 쿼리문 수행 중 발생한 Exception의 메시지 반환
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
			throw e;
		}finally{
			close(conn, stmt, rset);
		}
		// KBR 값이 없느면 널 값 셋팅 
		if(records == null) records = null;
		//	if(records == null) records = new RecordSet(null);
		return records;
	}
	
	public RecordSet preparedStatementExecute(String query)throws Exception{
		return preparedStatementExecute(query,defaultTimeout);
	}
	
	public RecordSet preparedStatementExecute(String query,int timeout) throws Exception {
		PreparedStatement pstmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = null;
		try {
			if(debug) logger.debug("query : [{}] ",query);
			
			conn = DBFactory.getInstance().getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setQueryTimeout(timeout);
			pstmt.executeQuery();
			rset = pstmt.getResultSet();
			
			if(rset != null){
				records = new RecordSet(rset);
			}
			
		}catch(SQLException e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
			throw e;
		}finally{
			close(conn, pstmt, rset);
		}
		
		if(records == null) records = null;  // records = new RecordSet(null);
		return records;
	}
	
	public abstract Connection getConnection() throws SQLException;
	public abstract void shutdown() throws SQLException;
	public abstract String status() throws SQLException;
	
}
