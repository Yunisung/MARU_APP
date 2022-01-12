package com.pgmate.lib.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor.LAVENDER;
import com.itextpdf.text.List;
/**
 * @author Administrator
 *
 */

// 테이블을 구체화 시켜주는 클래스 
public class RecordSet extends DataSet {

	public boolean labelLower = false;
	
	public RecordSet(){
		
	}
	
	public RecordSet(ResultSet rs) throws SQLException {
		if(rs == null) return;
		// 구체적인 테이블의 칼럼값이 아닌 테이블의 정보를 담고 있는 객체 
		ResultSetMetaData meta = rs.getMetaData();
		// 컬럼 갯수 리턴
		int	max = meta.getColumnCount();
		
		// 컬럼의 갯수만큼 배열 공간 생성 
		this.columns = new String[max];
		
		// 생성된 배열에 컬럼 값 담기
		for(int i = 0; i < max; i++) {
			if(labelLower == true) {
				columns[i] = meta.getColumnLabel(i+1).toLowerCase();
			} else {
				columns[i] = meta.getColumnLabel(i+1);
			}
		}
		
		int j = 0;
		// 컬럼의 종류에 따라 컬럼과 데이터를 맵핑
		while(rs.next()) {
			// List에 공간 생성 후 0번째부터 순서대로 값을 넣기위해 idx값 설정
			this.addRow();
			for(int i = 1; i <= max; i++) {
				try {
					// 대용량 텍스트 데이터 타입(최대 4Gbyte) 일 경우 
					if(meta.getColumnType(i) == java.sql.Types.CLOB) {
						this.put(columns[i-1], rs.getString(i));
					}
					// KBR : java.sql.Types.TIMESTAMP = 데이터 필드 타입 (데이터 :2005)
					// KJM : 해당 컬럼값의 유형이 CLOB일 때(문자 대형 객체)
					if(meta.getColumnType(i) == java.sql.Types.CLOB) {
						this.put(columns[i-1], rs.getString(i));
					}
					else if(meta.getColumnType(i) == java.sql.Types.TIMESTAMP) {
						this.put(columns[i-1], rs.getTimestamp(i));
					}
					else {
						this.put(columns[i-1], rs.getObject(i));
						
					}
				// null 값일 경우 공백으로 
				} catch(Exception e) {
					this.put(columns[i-1], "");
				}
			} // for 종료 
			// 리스트에 저장되는 해쉬값의 idx번호 부여   
			this.put("_idx", ++j);
		} // while 종료 
		
		// idx값 -1로초기화 
		this.first();
	}// 메소드 종료

	public RecordSet(Map<String , String>  rs) throws SQLException {
		if(rs == null) return; 
		int j = 0;
		this.addRow();		
		for (String rediskey : rs.keySet()){		
	      this.put(rediskey, rs.get(rediskey));
	    }		
		this.prev();
//		this.first();
	}
	
}
