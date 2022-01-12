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
 * @author Administrator
 *
 */
//KJM : 유저의 주소, 접속 정보
public class UserAccessDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.UserAccessDAO.class );
	private static final String TABLE = "VW_USER_ACCESS";
	private static final String COLUMNS = "idx,id,name,IFNULL(ipAddr,'') ipAddr,IFNULL(userAgent,'') userAgent,regDay,regDate";

	public UserAccessDAO() {
		super(TABLE,true);
		super.setColumns(UserAccessDAO.COLUMNS);
	}
	

	
	public RecordSet getById(String memberId){
		addWhere("id",memberId,eq);
		return search();
	}
	
	//KJM : 해당하는 아이디의 접속정보 size수만큼 조회
	public RecordSet getById(String memberId,long size){
		addWhere("id",memberId,eq);
		return searchList(1, size, "");
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