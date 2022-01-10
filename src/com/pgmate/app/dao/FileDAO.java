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
public class FileDAO extends DAO{
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.FileDAO.class );
	private static final String TABLE = "PG_FILE";
	private static final String COLUMNS = "idx, grade, memberId, folderNm, fileNm, fileSize, status, regId, regDay, regDate";

	public FileDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(COLUMNS);
	}
	
	public RecordSet getByIdx(String idx){
		addWhere("idx",idx,eq);
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
	
	public RecordSet getByMchtId(String mchtId, String grade, long limit){
		//addWhere("grade","가맹점",eq);
		addWhere("memberId",mchtId,eq);
		
		if(!grade.equals("본사")) {
			addWhere("status", "사용", eq);
		} else {
			addWhere("status", "폐기", ne);
		}
		
		setLimit(limit);
		return search();
	}
}
