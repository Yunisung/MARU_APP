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
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class MchtPispDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtPispDAO.class );
	private static final String TABLE = "PG_MCHT_MNG_PISP";
	private static final String COLUMNS = "*";
	/**
	 * 
	 */
	public MchtPispDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns("*");
		super.setOrderBy("regDay desc");
	}
	
	public SharedMap<String,Object> getByMchtId(String mchtId){
		super.addWhere("mchtId",mchtId.toLowerCase(),eq);
		return super.search().getRow(0);
	}

	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION
		return super.search();
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING
	}
	
	
	
	public SharedMap<String,Object> setPispAccount(String mchtId){
		SharedMap<String,Object> accntMap = getUnusedAccount();
		
		SharedMap<String,Object> temp = super.query("SELECT concat('VI',FN_NEXTVAL2('VACT')) as issueId, FN_MCHT_NAME('"+mchtId+"') as mchtName ").getRow(0);
		accntMap.put("issueId",temp.getString("issueId"));
		accntMap.put("mchtName",temp.getString("mchtName"));
	
		
		super.initRecord();
		super.setTable("PG_VACT_DTL");
		super.setRecord("issueId", accntMap.getString("issueId"));
		super.setRecord("`account`", accntMap.getString("account"));
		super.setRecord("vactType", "영구");
		super.setRecord("`status`", "발행");
		super.setRecord("mchtId", mchtId);
		super.setRecord("holderName", accntMap.getString("mchtName"));
		super.setRecord("amount", 0);
		super.setRecord("oper", "ge");
		super.setRecord("trackId","PISP_"+CommonUtil.getCurrentDate("yyMMddHHmmss"));
		super.setRecord("oper", "ge");
		super.setRecord("expireAt", "2030123100");
		super.setRecord("udf1","pisp");
		super.setRecord("udf2","");
		super.setRecord("reason","");
		super.setRecord("regId","SYSTEM");
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		super.insert();
		
		return accntMap;
	}
	
	
	public SharedMap<String,Object> getUnusedAccount(){
		super.setTable("PG_VACT A LEFT OUTER JOIN PG_VACT_DTL B ON A.account = B.account");
		super.setColumns("A.account , A.bankCd");
		super.setWhere("B.account IS NULL AND A.pisp ='Y'");
		super.setOrderBy("A.regDate asc");
		super.setLimit(1);
		
		RecordSet rset = super.search();
		super.initRecord();
		if(rset.size() > 0) {
			return rset.getRow(0);
		}else {
			return new SharedMap<String,Object>();
		}
	}

}

