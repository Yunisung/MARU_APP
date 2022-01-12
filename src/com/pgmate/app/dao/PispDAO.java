package com.pgmate.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
//KJM : 지급대행 서비스 거래내역
public class PispDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.PispDAO.class );
	private static final String TABLE = "VW_TRX_PISP";
	private static final String COLUMNS = "*";

	public PispDAO() {
		super(TABLE, CPUtil.CP_DEBUG);
		super.setDebug(true);
		super.setColumns("*");
		super.setOrderBy("regDay desc");
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION
		return super.search();
	}
	
	//KJM : 지급대행 서비스 거래내역 리스트 조회
	public RecordSet list(List<Data> datas, Page page) {
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas); //DATA to CONDITION
		return super.searchList(page.current, page.size, page.hash); //LIST PAGING
	}

}

