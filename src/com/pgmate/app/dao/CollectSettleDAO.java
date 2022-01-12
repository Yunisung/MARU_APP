package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * 210812_PYS : 입금정산
 * @author Administrator
 *
 */
public class CollectSettleDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CollectSettleDAO.class );
	private static final String TABLE = "VW_COLLECT_SETTLE";
	private static final String COLUMNS = "*";
	
	public CollectSettleDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CollectSettleDAO.COLUMNS);
		super.setOrderBy("collectDay desc, name asc");
	}
	
	/**
	 * 210812_PYS : collectID로 데이터조회
	 * <pre>
	 * SELECT * FROM VW_COLLECT_SETTLE WHERE collectId = 'collectId' ORDER BY collectDay desc, name asc
	 * </pre>
	 * @param collectId
	 * @return
	 */
	public RecordSet getById(String collectId){
		addWhere("collectId",collectId,eq);
		return search();
	}
	
	/** 
	 * 210812_PYS : Data클래스로 조회
	 * @param datas
	 * @return
	 */
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