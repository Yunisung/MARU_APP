package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * 210812_PYS : 입금정산 그룹id
 * @author Administrator
 *
 */
public class CollectGroupDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.CollectGroupDAO.class );
	private static final String TABLE = "PG_COLLECT_GROUP";
	private static final String COLUMNS = "*";
	
	public CollectGroupDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CollectGroupDAO.COLUMNS);
	}
	
	
	/**
	 * 210812_PYS : 입금정산 그룹ID로 데이터 조회
	 * <pre>
	 * SELECT * FROM PG_COLLECT_GROUP WHERE colgId = 'colgId'
	 * </pre>
	 * @param colgId : Collect Group ID
	 * @return
	 */
	public RecordSet getByColgId(String colgId){
		addWhere("colgId",colgId,eq);
		return search();
	}
	
}