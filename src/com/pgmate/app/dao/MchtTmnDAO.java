package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;

/**
 * @author Administrator
 *
 */
public class MchtTmnDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtTmnDAO.class );
	private static final String TABLE = "VW_MCHT_TMN";
	private static final String COLUMNS = "*";
	
	public MchtTmnDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtTmnDAO.COLUMNS);
	}
	
	public RecordSet getById(String tmnId){
		addWhere("tmnId",tmnId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getByMchtId(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
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

	public boolean insertDefault(String tmnId, String pk, String mchtId, String taxId, String serial) {
		String query = "INSERT INTO PG_MCHT_TMN	(`tmnId`,`mchtId`,`taxId`,`status`, `serial`,`payKey`,`activeDate`,`regId`,`regDay`,`regDate`) "
					 + "SELECT '"+tmnId+"', mchtId, '"+taxId+"', '사용','"+serial+"','"+pk+"',regDay,regId,regDay,regDate "
					 + "FROM PG_MCHT WHERE mchtId = '"+mchtId+"'";
		
		return new CPDAO().update(query); 
	}
	
	public String getWebPay(String mchtId){
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		addWhere("status","사용",eq);
		addWhere("webPay","사용",eq);
		RecordSet rset = search();
		if(rset.size() == 0){
			return "";
		}else{
			return rset.getRow(0).getString("payKey");
		}
		
	}

	public String getTmnWebPay(String tmnId){
		addWhere("lower(tmnId)",tmnId.toLowerCase(),eq);
		addWhere("status","사용",eq);
		addWhere("webPay","사용",eq);
		RecordSet rset = search();
		if(rset.size() == 0){
			return "";
		}else{
			return rset.getRow(0).getString("payKey");
		}
		
	}
	
	public RecordSet getHtByMchtId(String mchtId) {
		setTable("HT_MCHT_TMN");
		setColumns("*");
		addWhere("lower(mchtId)", mchtId.toLowerCase(), eq);
		return search();
	}
	
	public String getNewId() {
		return "TMN" + String.format("%06d", Integer.parseInt(getFunction("FN_NEXTVAL", "TERMINAL")));
	}
}