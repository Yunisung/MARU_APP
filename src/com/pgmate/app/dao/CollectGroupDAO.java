package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
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
	
	public RecordSet getByColgId(String colgId){
		addWhere("colgId",colgId,eq);
		return search();
	}
	
}