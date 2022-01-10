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
public class MchtInterTemplateDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtInterTemplateDAO.class );
	private static final String TABLE = "PG_MCHT_INTER_TEMPLATE";
	private static final String COLUMNS = "*";
	
	public MchtInterTemplateDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtInterTemplateDAO.COLUMNS);
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
	
	public RecordSet getList(){
		return super.search();	 
	}
	
	public RecordSet getInterTemplate(String templateId) {
		super.setTable("PG_MCHT_INTER_TEMPLATE");
		super.setColumns("*");
		super.addWhere("templateId",templateId,eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	public RecordSet getModifyList(String templateId){
		super.setTable("(SELECT * FROM PG_CODE WHERE alias = 'ACQUIRER') A LEFT JOIN PG_MCHT_INTER_TEMPLATE_DTL B ON A.code = B.acquirer and B.templateId = '"+templateId+"'");
		super.setColumns("A.codeName AS acqName, A.code AS acquirer, B.templateId, B.authType, B.m02, B.m03,B.m04,B.m05,B.m06,B.m07,B.m08,B.m09,B.m10,B.m11,B.m12,B.m13, B.m14,B.m15,B.m16,B.m17,B.m18,B.m19,B.m20,B.m21,B.m22,B.m23,B.m24 ");
		super.setOrderBy("acqName asc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	public String getTemplateId() {
		return "T" + getFunction("FN_NEXTVAL2", "TEMPLATE");
	}
}