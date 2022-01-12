package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * 210812_PYS : 영중소 차액 입금 정산
 * <pre>
 * DB에 데이터가 없어서 정확한 파악 불가능
 * </pre>
 * @author Administrator
 *
 */
public class CollectDiffDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CollectDiffDAO.class );
	private static final String TABLE = "VW_COLLECT_DIFF";
	private static final String COLUMNS = "*";
	
	public CollectDiffDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CollectDiffDAO.COLUMNS);
		super.setOrderBy("collectDay desc, name asc");
	}
	
	/**
	 * 210812_PYS : 아이디로 조회
	 * <pre>
	 * SELECT * FROM VW_COLLECT_DIFF WHERE collectId = 'collectId'
	 * </pre>
	 * @param collectId
	 * @return
	 */
	public RecordSet getById(String collectId){
		addWhere("collectId",collectId,eq);
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
}