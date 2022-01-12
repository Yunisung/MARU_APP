package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
// 테이블 insert , update , delete, 히스토리 
public class CPDAO extends DAO{

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CPDAO.class );
	
	public CPDAO() {
		this(CPUtil.CP_DEBUG);
	}
	
	// 생성자 호출 후 바로 디버그값 셋팅 (기본값 true)
	public CPDAO(boolean debug) {
		this.setDebug(debug);
	}
	
	/**
	 * 210812_PYS : 테이블 값 추가
	 * <pre>
	 * INSERT 'table' INTO 'datas'
	 * </pre>
	 * @param table : 추가할 테이블
	 * @param datas : 추가할 데이터
	 * @return
	 */
	public boolean insert(String table,List<Data> datas){
		this.setTable(table);
		CPUtil.setDAO(this, datas);
		return super.insert();
	}
	
	/**
	 * 210812_PYS : 테이블 값 수정
	 * <pre>
	 * UPDATE 'table' SET 'datas'
	 * </pre>
	 * @param table : 수정할 테이블
	 * @param datas : 수정할 데이터
	 * @return
	 */
	public boolean update(String table,List<Data> datas){
		this.setTable(table);
		CPUtil.setDAO(this, datas);
		return super.update();
	}
	
	/**
	 * 210812_PYS : 테이블 값 추가 (등록 시간 추가)
	 * @param table : 추가할 테이블
	 * @param regId : 추가하는 ID
	 * @param datas : 추가할 데이터
	 * @return
	 */
	// regId = 등록하는 업체 아이디
	public boolean insert(String table, String regId, List<Data> datas){
		/*	
		 *	쿼리문에 사용 될 테이블명, 업체 아이디, 현재 날짜 세팅 후
		 *	DAO로 입력받은 데이터들과 보낸다
		 */
		
		this.setTable(table);
		this.setRecord("regId", regId);
		this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		CPUtil.setDAO(this, datas);
		
		return super.insert();
	}	
	
	/**
	 * 210812_PYS : 테이블 값 수정 (등록 ID 추가 )
	 * @param table : 수정할 테이블
	 * @param regId : 등록ID
	 * @param datas : 수정할 데이터
	 * @return
	 */
	public boolean update(String table, String regId, List<Data> datas){		
		
		this.setTable(table);
		this.setRecord("regId", regId); // B : 등록한 사람 
		//this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		CPUtil.setDAO(this, datas);
		return super.update();
	}
	
	/**
	 * 210812_PYS : 히스토리 추가할때 사용
	 * @param table : 해당 테이블에 데이터를 수정하고 그값을 HT_로 시작하는 테이블에 저장
	 * @param regId : 업데이트 한 사람 ID 
	 * @param datas : 수정하는 값들
	 * @return
	 */
	
	// 업데이트 및 백업
	public boolean updateAndBack(String table, String regId, List<Data> datas){
		// 1.summary(코멘트)가 있을 경우 실행 
		String summary 	= "";
		for(Data data : datas){
			if(data.name.equals("summary")){
				summary = CommonUtil.toString(data.val);
				datas.remove(data); // 값을 유동적으로 주기 위한 처리 
				break;
			}
		}
		
		// 2. 히스토리 내역 추가 
		// SELECT * FROM "+table + " WHERE 0=1 쿼리문을 통해 칼럼명만 추출 
		String columns =this.getColumns(table);
		// 히스토리 내역 생성 쿼리  
		String q = "INSERT INTO "+table.replace("PG_", "HT_")+" ("+columns+",summary) SELECT "+columns+",? FROM "+table +" WHERE ";
		// prepared 값 셋팅
		this.setTable(table);
		this.setRecord("regId", regId);										//등록한 업체 아이디
		this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));	//등록일
		this.setRecord("regDate", CommonUtil.getCurrentTimestamp());		//등록일시
		//특수문자 제거, where ~ orderBy 셋팅 
		CPUtil.setDAO(this, datas);
		//기존 쿼리에 where절 추가 
		q+=super.where.toString();
		
		//기존 정보 BACKUP TABLE 로 이동
		long lastIdx = -1; 
		
		//where 조건이 있을 경우 
		if(super.where.length() > 3){
			// 히스토리 내역 생성 
			lastIdx = super.updateAndLastIdx(q,summary);
		}
		
		//3.업데이트
		//업데이트 실행 성공 시 true, 실패 시 false
		boolean updated = super.update();
		
		//업데이트 실패하면 히스토리 해당 레코드 삭제 
		if(!updated && lastIdx != -1){
			q = "DELETE FROM "+table.replace("PG_", "HT_") +" WHERE idx ="+lastIdx;
			super.update(q);
		}
		return updated;
	}
	
	/**
	 * 210812_PYS : 테이블 값 삭제
	 * @param table : 삭제할 테이블
	 * @param datas : 삭제할 데이터
	 * @return
	 */
	public boolean delete(String table,List<Data> datas){
		this.setTable(table);
		CPUtil.setDAO(this, datas);
		return super.delete();
	}
	
	/**
	 * 210812_PYS : 사용안하는중, 히스토리 내역 불러오는듯
	 * <pre>
	 * SELECT * FROM 'table' WHERE 'column' = 'key'
	 * </pre>
	 * @param table
	 * @param column
	 * @param key
	 * @return
	 */
	public RecordSet getHistory(String table,String column,Object key){
		addWhere(column,key,eq);
		setOrderBy("idx desc");
		return super.search();
	}
	
	public boolean insert2(String table, String regId, List<Data> datas){
		this.setTable(table);
		this.setRecord("regId", regId);
		CPUtil.setDAO(this, datas);
		return super.insert();
	}	
}
