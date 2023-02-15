package com.pgmate.lib.dao;

import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.util.cipher.Crypt;
import com.pgmate.lib.util.db.DBConfigBean;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * 210812_PYS : SQL문을 한꺼번에 만들어 조회할려고 만든듯
 * @author Administrator
 *
 */
public class DAO  implements java.io.Serializable {

	private static Logger logger 			= LoggerFactory.getLogger(com.pgmate.lib.dao.DAO.class);
	private static DBConfigBean configBean 	= null;

	/**
	 * eq : " = "
	 */
	public static String eq= "eq";
	/**
	 * ne : " != "
	 */
	public static String ne= "ne";
	/**
	 * gt : " > "
	 */
	public static String gt= "gt";
	/**
	 * ge : " >= "
	 */
	public static String ge= "ge";
	/**
	 * lt : " < "
	 */
	public static String lt= "lt";
	/**
	 * le : " <= "
	 */
	public static String le= "le";
	/**
	 * lk : " LIKE "
	 */
	public static String lk= "lk";
	/**
	 * in : " IN "
	 */
	public static String in= "in";
	/**
	 * ni : " NOT IN "
	 */
	public static String ni= "ni";
	/**
	 * bt : " BETWEEN "
	 */
	public static String bt= "bt";
	/** 
	 * fneq : " = "
	 */
	public static String fneq= "fneq";
	/**
	 * fnne : " != "
	 */
	public static String fnne= "fnne";
	/**
	 * fngt : " > "
	 */
	public static String fngt= "fngt";
	/**
	 * fnge : " >= "
	 */
	public static String fnge= "fnge";
	/**
	 * fnlt : " < "
	 */
	public static String fnlt= "fnlt";
	/**
	 * fnle : " <= "
	 */
	public static String fnle= "fnle";
			
	
	private String error	= "";
	private boolean debug	= false;
	private String columns	= "*";
	private String table	= "";
	
	private String join		= "";
	private String orderBy	= "regDate DESC";
	private String groupBy	= "";
	private long limit = 0;
	public StringBuilder where		= new StringBuilder();
	private StringBuilder sql		= new StringBuilder();
	private SharedMap<String,Object> record= new SharedMap<String,Object>(); // KBR : prepared 사용 시 각 컬럼의 값을 직접 사용한다.
	
	private long total		= 0;
	private double totalSum	= 0;
	private String hash		= "";
	private static Key secretKeySpec = null;
	
	//  생성자 생성되면 
	public DAO() {
		//  매개변수 2개(String , boolean타입 ) 생성자 실행 됨
		this("",CPUtil.CP_DEBUG);		
	}
	
	/**
	 * TABLE 및 DEBUG = false 
	 * @param table
	 */
	public DAO(String table){
		this(table,false);
	}
	
	/**
	 * TABLE, DEBUG 여부 지정
	 * @param table
	 * @param debug
	 */
	
	//  DAO 생성자 실행 시 동시 실행 메소드
	public DAO(String table,boolean debug){
		
		if(configBean == null){
			configBean = ConfigLoader.getConfig().db;
		}
		
		if(secretKeySpec == null){
			try{
			// 암호화에 사용되는 secretKeySpec 생성
			// ByteUtil.toBytes() => 16진수의 문자열을 2자리씩 나눠 byte로 변환하여 넣은 배열
			secretKeySpec = Crypt.generateKey("DES", ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16));
			}catch(Exception e){}
		}
		
		this.table = table;
		this.debug = debug;
		
	}

	public DAO(String table , boolean debug, DBConfigBean db){
		if(configBean == null){
			configBean = db;
		}
		if(secretKeySpec == null){
			try{
			secretKeySpec = Crypt.generateKey("DES", ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16));
			}catch(Exception e){}
		}
		this.table = table;
		this.debug = debug;
	}
	
	
	/**
	 * JDBC Error Message 전달 
	 * @return
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * SQL QUERY + Elapsed Time 확인
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * SELECT A,B,C 등 사용 시 A,B,C 로 지정하여 반환하고자 하는 COLUMN 을 comma 로 구분하여 전달한다.
	 * @param columns 
	 */
	public void setColumns(String columns) {
		this.columns = columns;
	}


	/**
	 * @param TABLE 이름
	 */
	public void setTable(String table) {
		this.table = table;
	}
	
	public String getTable() {
		return table;
	}
		
	/**
	 * @param orderby :  orderBy 구문은 생략 후   "ABC desc,CDB asc" 형식으로 전달.  
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}


	/**
	 * @param groupBy 구문은 생략 후 "ABC, DEF "형식으로 전달 
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}


	/**
	 * type : INNER, OUTER ,condition : TabA.Colmun =TabB.Colmun 
	 * @param 
	 */
	public void setJoin(String type,String condition) {
		this.join = type+" JOIN "+table+" ON "+condition;
	}
	
	public void setLimit(long limit) {
		this.limit = limit;
	}


	/**
	 * ABC = 'CDE' AND BCD ='ABC'
	 * @param WEHRE 조건에 대한 값을 초기화 하여 재 지정한다.
	 */
	public void setWhere(String where) {
		this.where = new StringBuilder().append(" ").append(where);
	}
	
	/**
	 * ABC='CDE' 등이며 
	 * @param WEHRE 조건을 append 하여 저장한다. 기본적으로 기 조건과는 AND 로 지정된다.
	 */
	public void addWhere(String where) {
		// where절이 있는 지 확인 후 없으면 추가한다
		if(this.where.length() ==0){
			setWhere(where);
		// 이미 where절이 존재하면 "AND"와 같이 추가한다
		//KJM : 기존 where AND 추가 where
		}else{
			this.where.append(" AND ").append(where);
		}
	}
	
	public void addWhere(String column, Object value) {
		//idx_key = table+"_"+value;		
		addWhere(column,value,eq);
	}
		
	
	/**
	 * COLUMN , VALUE, Operator 를 지정하여 조건 절에 APPEND 한다.
	 * Operator 는 eq = , gt > , ge >= , lt < , le <= , lk like 를 지원하며 그외의 연산자는 축약어가 아닌 직접 지정하면 된다. 
	 * @param column
	 * @param value
	 * @param operatorator
	 */
	
	public void addWhere(String column,Object value,String operator){
		
		// 1.컬럼, 값 true 
		if(!column.equals("") && value !=null){
			// 2. 정수  or 실수 
			if(value instanceof java.lang.Long || value instanceof java.math.BigInteger || value instanceof java.lang.Integer || value instanceof java.lang.Double){
				if(!operator.equals(lk)){
					if(operator.equals(in) || operator.equals(ni)){
						//KJM : column IN (value)
						addWhere(column +getOperator(operator)+"("+CommonUtil.toString(value)+")"); // 우리가 지금 보는 최종값 ... !
					}else{
						addWhere(column +getOperator(operator)+CommonUtil.toString(value));
					}
				}
			// 3. data type 
			}else if(value instanceof java.sql.Timestamp){
				if(!operator.equals(lk)){
					addWhere("DATE_FORMAT("+column +",'%y%m%d%H%i%s')"+getOperator(operator)+"'"+CommonUtil.timestampToString((Timestamp)value,"yyMMddHHmmss")+"'");
				}
			// 4. String type
			}else{
				if(operator.equals(lk)){
					// column like %value%
					addWhere(column +getOperator(operator)+"'%"+CommonUtil.toString(value)+"%'");
				}else if(operator.equals(in) || operator.equals(ni)){
					// column in/not in(value)
					addWhere(column +getOperator(operator)+"("+CommonUtil.toString(value)+")");
				}else if(operator.equals(bt)){
					String[] scope = CommonUtil.adjustArray(CommonUtil.split(CommonUtil.toString(value), ",", true),2);
					// column between scope[0] AND scope[1]
					addWhere(column +getOperator(operator)+scope[0]+" AND "+scope[1]);
				}else if(operator.startsWith("fn")){	// fn~ => 연산자
					addWhere(column +getOperator(operator)+CommonUtil.toString(value));
					//  eq 
				}else{
					addWhere(column +getOperator(operator)+"'"+CommonUtil.toString(value)+"'");
				}
			}
		}
	
	}
	
	/**
	 * prepared insert,update 사용 시 각 컬럼의 값을 직접 사용한다.
	 * @param column
	 * @param value
	 */
	// B :data.list 에 넣는게 아니라 단독적으로 값이 필요할 때 record 사용함 
	public void setRecord(String column,Object value){
		
		record.put(column, value);
	}
	
	/**
	 * prepared insert,update 사용 시 oper 조건에 따라 입력값을 변경한다.
	 * @param column
	 * @param value
	 */
	public void setRecord(String column,Object value,String oper){
		if(oper.equals("percent")) {
			Double d = Double.valueOf((String)value);
			value = String.format("%.5f", d / 100);
		}
		
		if(oper.equals("comma")) {
			value = (Object)String.valueOf(value).replace(",", "");
		}
		record.put(column, value);
	}
	
	/**
	 * 페이징 시 재 검색조건 가져올때.
	 * @return
	 */
	public String getHash(){
		return this.hash;
	}
	
	/**
	 *지정된 TABLE, JOIN , ORDER BY , GROUP BY 으로 SELECT 쿼리를 할 경우 사용한다.
	 * @return
	 */
	public RecordSet search(){
		return query(searchInit());
	}
	
	
	// SELECT 쿼리문 작성
	// select 문부터 쿼리를 순서대로 만드는 목적으로 사용
	private String searchInit(){
		sql.append("SELECT ").append(columns).append(" FROM ").append(this.table).append(this.join);
		if(where.length() > 1){ sql.append(" WHERE ").append(where.toString());}
		if(groupBy.length() > 1){ sql.append(" GROUP BY ").append(groupBy);}
		if(orderBy.length() > 1){ sql.append(" ORDER BY ").append(orderBy);}
		// LIMIT
		if(limit > 0 ) { sql.append(" LIMIT ").append(limit); }
		// 완성된 쿼리문 문자열 형식으로 반환
		//  toStrin`g = 로깅 또는 디버그에 정확한 문자열 전달하기 위해
		return sql.toString();
	}
	
	private String searchTable(){
		return "SELECT "+columns+" FROM "+this.table+this.join;
	}
	
	private String searchCondition(){
		StringBuilder buf = new StringBuilder();
		// buf = "WHERE 조건" + "GROUP BY 조건" + "ORDER BY 조건"
		if(where.length() > 1){ buf.append(" WHERE ").append(where.toString());}
		if(groupBy.length() > 1){ buf.append(" GROUP BY ").append(groupBy);}
		if(orderBy.length() > 1){ buf.append(" ORDER BY ").append(orderBy);}
		return buf.toString();
	}
	
	private String searchCountCondition(){
		StringBuilder buf = new StringBuilder();
		if(where.length() > 1){ buf.append(" WHERE ").append(where.toString());}
		if(groupBy.length() > 1){ buf.append(" GROUP BY ").append(groupBy);}
		return buf.toString();
	}
	
	/**
	 * ROW 수를 조회할 때 사용한다.
	 * @return
	 */
	public long getCount(){
		this.columns = " COUNT(*) AS CNT ";
		RecordSet rset = query(searchInit());
		if(rset == null || !rset.next()) {
			return 0;
		} else {
			return rset.getLong("CNT");
		}
	}

	/**
	 * 지정한 WHERE 절로 ROW 수를 조회할 때 사용한다.
	 * @param where
	 * @return
	 */
	public long getCount(String initWhere){
		this.where = new StringBuilder();
		this.where.append(initWhere);
		return getCount();
	}
	
	
	/**
	 *
	 * 페이징 관련된 값 + 페이지 HASH 를 사용해서 검색 할 경우 
	 * ★★ 지정된 TABLE, JOIN , ORDER BY , GROUP BY 으로 SELECT 쿼리를 할 경우 사용한다.★★
	 * @return
	 * @param current
	 * @param size
	 * @param hash
	 * @return
	 */
	public RecordSet searchList(long current,long size,String hashVal){
		
		if(current == 0){current = 1;}
		if(size == 0){size = 20;}
		
		String countQuery 	= "";
		String query 		= "";
		
		// 페이징에 필요한 값 셋팅 
		if(hashVal.trim().equals("")){
			// countQuery : 총 리스트가 몇개인지 확인
			countQuery = "SELECT COUNT(*) AS TOTAL FROM "+this.table+this.join;
			// searchCondition() : where , groupBy , orderby 순서대로 있는지 체크하여 쿼리 생성 후 리턴 
			String condition = searchCondition();
			// where절 존재할 때 countQuery뒤에 붙여 쿼리문 완성시킨다 (  order by , group by 존재시에도 )
			if(condition.length() > 1){ countQuery += condition; }
			if(!groupBy.equals("")){
				// 그룹 일 경우 countQuery 테이블 값 현재까지 만든 값으로 변경하여 완성
				countQuery = "SELECT COUNT(*) AS TOTAL FROM ( "+countQuery +") BC";
			}
			// CUBRID : query = "SELECT * FROM (" +searchInit() + ") LIMIT "+(size*(current-1))+","+(size);
			// searchInit() : select ~ orderby 까지 연결하여 String값 반환 
			query = searchInit() +" LIMIT "+(size*(current-1))+","+(size);
			
			//HASH 값에 대한 암호화 및 BASE64 처리
			//쿼리문을 암호화 한 상태로 처리 좀 더 보안적인 부분 강화 목적
			cryptHash(condition);
		// HASH 값 있을 경우
		}else{
			this.hash = hashVal;
			//HASH 복호화 하여 사용한다.
			String hashWhere = decryptDES(hashVal);
			if(debug) {logger.debug("query hash : [{}] ",hashWhere);}
			countQuery = "SELECT COUNT(*) AS TOTAL FROM "+this.table+this.join;
			if(hashWhere.length() > 1){ countQuery += hashWhere; }
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL FROM ( "+countQuery +") BC";
			}
			// CUBRID query = "SELECT * FROM (" +searchTable()+" "+hashWhere + ") LIMIT "+(size*(current-1))+","+(size);
			query = searchTable() +" "+hashWhere +" LIMIT "+(size*(current-1))+","+(size);
		}
		long startsTime = System.currentTimeMillis();
		
		DBManager db	= null;
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = new RecordSet();
		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			// 쿼리 수행을 위한 Statement 객체 생성
			stmt = conn.createStatement();
			stmt.executeQuery(countQuery);
			System.out.println("query : " +countQuery);
			rset = stmt.getResultSet();
			if(rset.next()){
				// total 변수에 전체 레코드의 개수(TOTAL) 값 넣음
				total 	= rset.getLong("TOTAL");
			}
			//ResultSet,Statement 초기화
			rset.close();
			stmt.close();
			stmt = conn.createStatement();
			stmt.executeQuery(query);
			rset = stmt.getResultSet();
			// 반환된 레코드가 존재 할 때
			if(rset != null){
				records = new RecordSet(rset);
			}
		}catch(Exception e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
		}finally{
			if(debug) {
				logger.debug("query count : [{}] ",countQuery);
				logger.debug("query  : [{}] ",query);
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			// db관련 객체들 초기화
			db.close(conn, stmt, rset);
			init();
		}
		return records;
	}
	
	public RecordSet searchList2(long current,long size,String hashVal,String sumColumn){
		if(current == 0){current = 1;}
		if(size == 0){size = 20;}
		
		String countQuery 	= "";
		String query 		= "";
		
		
		if(hashVal.trim().equals("")){
			
			countQuery = "SELECT COUNT(*) AS TOTAL,SUM("+sumColumn+") AS TOTAL_AMT FROM "+this.table+this.join;
			String condition = searchCondition();
			if(condition.length() > 1){ countQuery += condition; }
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL, TOTAL_AMT FROM ( "+countQuery +") BC";
			}
			//CUBRID : query = "SELECT * FROM (" + searchInit() + ")  LIMIT "+(size*(current-1))+","+(size);
			query = searchInit() + " LIMIT "+(size*(current-1))+","+(size);
			//HASH 값에 대한 암호화 및 BASE64 처리
			cryptHash(condition);
			
		}else{
			this.hash = hashVal;
			//HASH 복호화 하여 사용한다.
			String hashWhere = decryptDES(hashVal);
			
			if(debug) {logger.debug("query hash : [{}] ",hashWhere);}
			
			countQuery = "SELECT COUNT(*) AS TOTAL,SUM("+sumColumn+") AS TOTAL_AMT FROM "+this.table+this.join;
			
			if(hashWhere.length() > 1){ countQuery += hashWhere; }
			
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL, TOTAL_AMT FROM ( "+countQuery +") BC";
			}
			//CUBRID query = "SELECT * FROM (" +searchTable()+" "+hashWhere + ") LIMIT "+(size*(current-1))+","+(size);
			query = searchTable()+" "+hashWhere + " LIMIT "+(size*(current-1))+","+(size);
		}
		
		long startsTime = System.currentTimeMillis();
		
		DBManager db	= null;
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = new RecordSet();
		
		try {
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			stmt = conn.createStatement();
			//  리스트 갯수 
			stmt.executeQuery(countQuery);
			rset = stmt.getResultSet();
			//  쿼리 반환된 값 셋팅
			if(rset.next()){
				total 	= rset.getLong("TOTAL");
				totalSum = rset.getDouble("TOTAL_AMT");
				System.out.println("totalSum = " + totalSum);
			}
			//ResultSet,Statement 초기화
			rset.close();
			stmt.close();
			
			stmt = conn.createStatement();
			//  sql 전체 쿼리값 셀렉트
			stmt.executeQuery(query);
			rset = stmt.getResultSet();
			if(rset != null){
				//  값 담기 
				records = new RecordSet(rset);
			}
		}catch(Exception e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
		}finally{
			if(debug) {
				logger.debug("query count : [{}] ",countQuery);
				logger.debug("query  : [{}] ",query);
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			db.close(conn, stmt, rset);
			//  쿼리값 모두 초기화
			init();
		}

		return records;
		
	}
	
	
	// insert 쿼리문 수행
	public boolean insert(){
		sql.append("INSERT INTO " + this.table + " (");
		// 입력값이 들어있는 리스트의 size
		int max = record.size();
		//  record에 입력받은 값 만큼 증가 할 변수 선언
		int k=0;
		// 입력받은 레코드 수 만큼 반복문 실행
		for (Iterator<String> iterator = record.keySet().iterator(); iterator.hasNext();) {
			// key = 컬럼명
			String key =  (String) iterator.next();
			sql.append(key);
			// 마지막 순서가 아니면 "," 붙임
			if (k < (max - 1)) {
				sql.append(",");
			}
			k++;
		}
		// sql = "INSERT INTO 테이블명 (COULUMN1, COLUMN2...) VALUES("
		sql.append(") VALUES (");
		// 입력받은 레코드 수 만큼 "?" 붙이기
		for(int i=0; i<max; i++) {
			sql.append("?");
			if(i < (max - 1)) sql.append(",");
		}
		sql.append(")");
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{			
			// 쿼리문 수행후 int형의 반환값 받음
			ret = DBFactory.getInstance().preparedExecuteUpdate(sql.toString(),record);
		}catch(Exception e){
			// 에러발생 시 에러메시지 생성 후 반환
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){ 		
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			// DAO 객체 세팅
			init();
		}
		// insert가 정상적으로 되었으면 ret = 1
		return ret > 0 ? true : false;		
	}
	
	// 테이블 마지막에 1행 추가
	public long insertAndLastIdx(){
		// INSERT INTO TABLE (
		sql.append("INSERT INTO " + this.table + " (");
		
		int max = record.size();
		int k=0;
		// iterator 변수 = map객체의 key값 반환
		for (Iterator<String> iterator = record.keySet().iterator(); iterator.hasNext();) {
			String key =  (String) iterator.next();
			sql.append(key);
			// 마지막순서가 아닌 키값들 뒤에 ',' 붙여줌 (키1,키2,키3,...)
			if(k < (max - 1)) {sql.append(",");}
			k++;
		}
		// INSERT INTO TABLE (key,key,...) VALUES (
		sql.append(") VALUES (");
		
		for(int i=0; i<max; i++) {
			sql.append("?");
			if(i < (max - 1)) sql.append(",");
		}
		// INSERT INTO TABLE (key,key,...) VALUES (?,?,...)
		sql.append(")");
		
		long ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			// insert후 추가된 인덱스 값 가져옴
			ret=DBFactory.getInstance().preparedExecuteUpdateAndLastIdx(sql.toString(), record);
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){ 		
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
		
		// insert 정상 수행 => 마지막에 추가된 인덱스값 / 수행 실패 => 0 반환
		return ret ;		
	}
	
	public boolean insert(String query){
		
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			ret=DBFactory.getInstance().preparedExecuteUpdate(query );
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	// update 쿼리문 수행
	public boolean update(){
		// 1. 업데이트 쿼리문 시작
		sql.append("UPDATE " + this.table + " SET ");
		
		int max = record.size();
		int k=0;
		
		// 2. column=?'value'셋팅
		for (Iterator<String> iterator = record.keySet().iterator(); iterator.hasNext();) {
			String key =  (String) iterator.next();
			sql.append(key + "=?");
			//  마지막 조건문에 "," 붙이지 않기 위한 조건문
			if(k < (max - 1)) {sql.append(",");}
			k++;
			// result : column=?,column=?,column=? ... 
		}
		
		// 3. where 셋팅
		if(where.length() > 1){sql.append(" WHERE " + this.where);}
		
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			// 4.결과 값 int로 return 
			ret=DBFactory.getInstance().preparedExecuteUpdate(sql.toString(),record);
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			// 5.쿼리문 초기화
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	// delete 쿼리문 수행
	public boolean delete() {
		
		sql.append("DELETE FROM " + this.table + " WHERE " + this.where.toString());
		
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			// 쿼리문 수행결과의 int값 받음
			ret=DBFactory.getInstance().preparedExecuteUpdate(sql.toString(),record );
		}catch(Exception e){
			// 에러 발생 시 Excepction 메시지 반환
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug) {
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
		
		// 정상적으로 delete 되었다면 ret = 1 이상
		return ret > 0 ? true : false;
	}
	

	//KJM : update 쿼리문 수행 (결제 취소 후 결제정보 insert할 때 사용)
	//  update만 수행하는건 아닌듯 ㅠㅠ (현재 터미널 폐기 delete도 수행 / 메소드 = tmnUpdate) (todo-list insert 도 여기서 이뤄짐)
	public boolean update(String query){
		

		// KBR : UPDATE 쿼리문에 where절이 없으면 false 
		if(query.toUpperCase().indexOf("UPDATE ") > -1 && query.toUpperCase().indexOf("WHERE ") < 0){
			logger.debug("UPDATE ERROR WHERE IS NULL [{}]" ,query.toString() );
			return false;
		}
		
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			// 쿼리문 수행
			ret=DBFactory.getInstance().preparedExecuteUpdate(query);
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	// 기존 정보 백업 테이블에 저장
	public long updateAndLastIdx(String query,String summary){
		
		// 쿼리문에 WHERE절이 존재하지 않을 경우 에러로그 띄운 후 0 반환
		if(query.toUpperCase().indexOf("WHERE ") < 0){
			logger.debug("UPDATE ERROR WHERE IS NULL [{}]" ,sql.toString() );
			return 0;
		}
		
		long lastIdx = 0;
		
		DBManager db 			= null;
		PreparedStatement pstmt = null;
		Connection 	conn		= null;
		ResultSet rset			= null;
		
		int result = 0;
		try {
			
			db 			= DBFactory.getInstance();
			conn		= db.getConnection();
			// query = "INSERT INTO HT_USER ([columns],summary) SELECT [columns],? FROM PG_USER WHERE id = 'id'
			pstmt		= conn.prepareStatement(query);
			// summary 값 세팅
			pstmt.setString(1,summary);
			// insert 쿼리 수행
			result  	= pstmt.executeUpdate();
			// 마지막으로 insert 된 id
			// insert 시 auto_increment로 설정된 값 증가 (해당하는 숫자 값 리턴) insert가 일어나지 않으면 0 리턴 
			rset		= pstmt.executeQuery("SELECT LAST_INSERT_ID() ");
			
			while(rset.next()){
				
				// 1번째 열 이름의 값을 java 프로그래밍 언어의 long으로 검색
				//  해당 컬럼의 auto_increment값을 가져온다 ( 즉, 해당 테이블의 첫번째에 있는 idx 값 리턴 )  
				lastIdx = rset.getLong(1);
				
			}
			conn.commit();
			
		}catch(Exception t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}, query : {}",error,query);
		}finally {
			db.close(conn, pstmt, rset);
		}
		if(debug) {
			logger.debug("query : [{}] ",query);
			logger.debug("generated key : [{}] ",lastIdx);
		}
		return lastIdx;
	}
	
	
	/**
	  * 직접 쿼리로 SELECT 를 요청 할 때 사용한다.
	 * @param query
	 * @return
	 */
	public RecordSet query(String query){
		
//		System.out.println(" query : : " + query);
		
		DBManager db = null;
		RecordSet rset= new RecordSet();
		
		long startsTime = System.currentTimeMillis();
		
		try {
			db = DBFactory.getInstance();
			db.setDebug(debug);
			// 쿼리 수행 결과 담음
			rset = db.statementExecute(query);
			
		}catch(Exception e) {
			this.error = db.getError();
		}finally{
			if(debug) {
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			// DAO 멤버변수 초기화
			init();
		}
		return rset;
	}
	
	
	
	public long getSeqCurrent(String name){
		String query = "SELECT curVal FROM PG_SEQ WHERE name ='"+name+"'";
		RecordSet rset = query(query);
		if(rset.size() ==0) {
			return 0;
		} else {
			rset.next();
			return rset.getLong("curVal");
		}
	}
	
	public long getSeqNext(String name){
		String query = "SELECT FN_NEXTVAL('"+name+"') as CNT";
		RecordSet rset = query(query);
		if(rset.size() == 0) {
			return 0;
		} else {
			rset.next();
			return rset.getLong("CNT");
		}
	}
	
	/**
	 * 210812_PYS : VW_USER_PW에서 해당 패스워드가 있는지 조회
	 * @param value
	 * @return
	 */
	// db내장함수인 password() 사용하여 패스워드 암호화 값 반환
	public String getPassword(String value){
		String query = "SELECT password('"+value+"') pw";
		RecordSet rset = query(query);
		// 반환된 암호화 값 없으면 빈값 반환
		if(rset.size() ==0) {
			return "";
		} else {
			rset.next();
			return rset.getString("pw");
		}
	}

		/*
		 *  query = "SELECT FN_AES_ENC(value) pw"  --함수 호출
		 * pw ==> 앞에 as 생략됨, 결과값을 pw란 컬럼명으로 받겠다
		 * FN_AES_ENC 함수
		 * BEGIN
			DECLARE result VARCHAR(500);								 -- 변수 선언
			SELECT HEX(AES_ENCRYPT(str, 'TEST1234567890')) INTO result ; -- HEX : 16진수 변환, AES_ENCRYPT(암호화할 문자열, 암호화 키) : 암호화, INTO result : 조회한 컬럼 변수에 넣기 
			return IFNULL(result,'');									 -- IFNULL : 첫번째 매개값이 null이면 두번째 매개값을 반환
		   END
		 */
	
	// value 암호화 
	public String getAESEnc(String value){
		String query = "SELECT FN_AES_ENC('"+value+"') pw";
		// 쿼리 수행 결과 값
		RecordSet rset = query(query);
		// 쿼리 수행 결과 값 없을 때 "" 반환
		if(rset.size() ==0) {
			return "";
		} else {
			/*	
			 *	next() : 선택되는 행 바꾸기위해 사용
			 *	처음상태는 아무것도 가리키지 않는 상태, netx()를 이용하여 다음행 선택
			 *	다음행이 있을 경우 true, 없을 경우 false 반환
			 */
			rset.next();
			//  암호화 되어있는 값 리턴
			return rset.getString("pw");
		}
	}
	
	public String getAESDec(String value){
		
		String query = "SELECT FN_AES_DEC('"+value+"') pw";
		
		RecordSet rset = query(query);
		
		if(rset.size() ==0) {
			return "";
		} else {
			rset.next();
			return rset.getString("pw");
		}
	}
	
	// 새로운 대행사 번호 부여
	//  새로운 일련번호(터미널 ID, 또는 일련번호 등등) 생성 메소드 
	//  [String ... value] = String으로 된 인자를 여러개 받는 (1,2, ... 100 ) 형태의 파라메터
	public String getFunction(String function,String... value){
		
		String returnVal = "";
		
		String query = "SELECT "+function+"( ";
		
		// KBR: value의 값이 여러개일 경우 ?,?,? 이런 형태로 추가 해주기 위한 과정 
		for(String val : value){
			query +="?,";
		}
		
		// query = "SELECT FN_GET_DIST_ID(?) as val"
		//  마지막 "," 를 짤라서 쿼리문 완성
		query = query.substring(0,query.length()-1) +") as val";
		
		DBManager db 			= null;
		PreparedStatement pstmt = null;
		Connection 	conn		= null;
		ResultSet rset			= null;

		try {
			
			db 			= DBFactory.getInstance();
			conn		= db.getConnection();
			// 쿼리문 주입
			pstmt		= conn.prepareStatement(query);
			// 쿼리문에 변수 넣기
			// KBR: 함수에 인자값 전달
			for(int i=0;i<value.length;i++){
//				System.out.println("value[i] :" +value[i]);
				pstmt.setString(i+1,value[i]);
			}
			
			rset		= pstmt.executeQuery();
			
			while(rset.next()){
				// 해당 순서의 열에있는 데이터를 String형으로 받아옴(distId)
				//  테이블의 첫번째 컬럼 데이터 값
				returnVal = rset.getString(1);
			}
			// DB commit 수행
			conn.commit();
		}catch(Exception t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}, query : {}",error,query);
		}finally {
			db.close(conn, pstmt, rset);
		}
		if(debug) {
			logger.debug("query : [{}] ",query);
		}
		return returnVal;
	}
	
	
	/**
	  * PAGING 검색 후 총 조회 결과 회신
	 * @return total 
	 */
	public long getTotal() {
		return total;
	}
	
	/**
	 * PAGING 검색 후 총 조회 결과 회신
	 * @return total 
	 */
	public double getTotalSum() {
		return totalSum;
	}

	
	private static String getOperator(String cond){
		cond = cond.toLowerCase();
		if(cond.equals(eq) || cond.equals(fneq)){
			return " = ";
		}else if(cond.equals(ne) || cond.equals(fnne)){
			return " != ";
		}else if(cond.equals(gt) || cond.equals(fngt)){
			return " > ";
		}else if(cond.equals(ge) || cond.equals(fnge)){
			return " >= ";
		}else if(cond.equals(lt) || cond.equals(fnlt)){
			return " < ";
		}else if(cond.equals(le) || cond.equals(fnle)){
			return " <= ";
		}else if(cond.equals(lk)){
			return " LIKE ";
		}else if(cond.equals(in)){
			return " IN ";
		}else if(cond.equals(ni)){
			return " NOT IN ";
		}else if(cond.equals(bt)){
			return " BETWEEN ";
		}else{
			return cond;
		}
	}
	
	// table의 컬럼정보 추출
	public String getColumns(String table){
		
		StringBuffer sb = new StringBuffer();
		
		DBManager db	= null;
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		
		try {
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			stmt = conn.createStatement();
			// WHERE 0=1 => 컬럼명만 결과로 나옴 
			stmt.executeQuery("SELECT * FROM "+table + " WHERE 0=1");
			rset = stmt.getResultSet();
			
			ResultSetMetaData metaData = rset.getMetaData();
			// 받아온 칼럼의 갯수
			int rowCount = metaData.getColumnCount();
			
			for(int i=0;i<rowCount;i++){
				// 칼럼이름을 "," 더해서 추가하기 (name,age ... )
				sb.append(metaData.getColumnName(i+1)+",");
			}	
		}catch(Exception e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
		}finally{
			db.close(conn, stmt, rset);
		}
		
		// 칼럼 값 확인 
		if(sb.length() == 0){
			return "";
		}else{
			// sb의 마지막 문자인 "," 삭제 후 문자열 형식으로 반환
			return sb.toString().substring(0, sb.length()-1);
		}
	}

	// WHERE  regDay <= '20210820' AND status != '폐기' ORDER BY regDate DESC
	private void cryptHash(String condition){
		try{
			/*
			 *  "DES/ECB/PKCS5Padding" => DES알고리즘/ECB모드/패딩
			 * 암호 알고리즘 : 문자를 다른 문자로 바꾸고, 문자들의 순서를 바꿈 이를 여러번 수행할 수록 안전함
			 * 	DES : 대칭형 암호화 알고리즘으로, 암호화 및 복호화 키가 동일 함
			 * 운용 모드 : 데이터를 블록으로 나누어 처리하고 합치는 것
			 * 	ECB : 블록 단위로 처리한 결과를 이어붙이는 방법 (단순하지만 같은 값을 갖는 원문 블록은 같은 암호 블록을 출력하며, 원문의 패턴이 그대로 드러남)
			 * 패딩 : 마지막 블록의 빈 부분을 채워주는 방식
			 * 	PKCS5 : 8바이트 블록의 암호 알고리즘 가정, 원문의 길이가 K 바이트이면 마지막 블록은 K mod 8의 크기 가짐 -> 패딩의 크기는 8 - (K mod 8)가 됨, 단순히 패딩 크기의 값을 갖는 바이트를 크기만큼 반복
			 */
			this.hash =  Crypt.encryptBase64(secretKeySpec, "DES/ECB/PKCS5Padding",null, condition.getBytes());
		}catch(Exception e){}
	}
	
	private String decryptDES(String condition){
		String des = "";
		if(condition.trim().equals("")){
			return "";
		}
		try{
			des = Crypt.decryptBase64(secretKeySpec, "DES/ECB/PKCS5Padding",null, condition);
		}catch(Exception e){}
		return des;
	}
	
	public String toString(RecordSet rset){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nsize : "+rset.size());
		sb.append("\ncolumn : "+CommonUtil.arrayToString(rset.getColumns()));
		
		while(rset.next()){
			ConcurrentHashMap<String,Object> row = rset.getRow();
			sb.append("\n row : "+rset.getIdx());
			for (Entry<String, Object> entry : row.entrySet()) {
				sb.append("\n   "+entry.getKey()+":"+entry.getValue());
	        }
		}
		return sb.toString();
	}
	
	//KJM : db연결 되어있는지 확인
	public boolean dbPing(){
		RecordSet rset = query("SELECT 1+1 AS CNT");
		rset.next();
		if(rset.getInt("CNT") == 2){
			return true;
		}else{
			return false;
		}
		
	}
	

	// 객체 초기화
	private void init(){
		error	= "";
		debug	= false;
		//columns	= "*";
		//table	= "";
		
		join		= "";
		orderBy	= "regDate DESC";
		groupBy	= "";
		where.setLength(0);
		sql.setLength(0);
		//record.clear();
		
		//total		= 0;
		limit = 0;
		hash = "";
	}
	
	public void initRecord(){
		record.clear();
		where.setLength(0);
		sql.setLength(0);
		join		= "";
		orderBy	= "regDate DESC";
		groupBy	= "";
		limit = 0;
	}
	
	public static void main(String[] args) {
		DAO dao = new DAO();
	}
	
}

