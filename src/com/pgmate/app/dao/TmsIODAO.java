package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SQLInjectionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class TmsIODAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TmsIODAO.class );
	private static final String TABLE = "PG_TMS_PAY";
	private static final String COLUMNS = "*";

	public TmsIODAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TmsIODAO.COLUMNS);
		super.setOrderBy("regDate desc");
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

	
//	public static void setDAO(DAO dao,List<Data> datas){
//		if(datas == null){
//			return;
//		}
//		StringBuilder orderBy = new StringBuilder(); 
//		for(Data data:datas){
//			if(data.key){
//				if(!CommonUtil.toString(data.val).equals("")) {
//					String str = CommonUtil.toString(data.val);
//					String convaerted = SQLInjectionUtil.changeValue(str);
//					if(!str.equalsIgnoreCase(convaerted)) {
//						data.val = convaerted;
//						logger.warn("==== SQL INJECTION C HECK : {} => {}", str, convaerted);
//					}
//				}
//				dao.addWhere(data.name,data.val,data.oper);
//			}else{
//				dao.setRecord(data.name, data.val);
//			}
//			if(!data.order.equals("")){
//				if(orderBy.length() !=0){
//					orderBy.append(",");
//				}
//				orderBy.append(data.name);
//				orderBy.append(" ");
//				orderBy.append(data.order);
//			}
//		}
//		if(orderBy.toString().trim().length() > 1){
//			dao.setOrderBy(orderBy.toString());
//		}
//	}
	


}