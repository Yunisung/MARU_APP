package com.pgmate.app.dao;

import java.sql.Timestamp;
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
public class UserDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.UserDAO.class );
	private static final String TABLE = "VW_USER_PW";
	private static final String COLUMNS = "id,pw,name,grade,role,parentId,status,phone,regId,regDay,regDate,pwYn,pwStatus,pwRetry,pwDate, showOthTrns, eformStatus, loanSettleStatus";

	public UserDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(UserDAO.COLUMNS);
	}
	

	
	public RecordSet getById(String userId){
		addWhere("id",userId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getParentsById(String userId){
		setTable("VW_USER");
		setColumns("distId, agencyId, id, pw, `name`, grade, role, parentId, `status`, phone, regId, regDay, regDate");
		return search();
	}

	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	
	
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		this.setTable("VW_USER");
		this.setColumns("distId, agencyId, id, pw, `name`, grade, role,showOthTrns, loanSettleStatus, parentId, `status`, phone, regId, regDay, regDate");
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public Timestamp getAccessById(String userId){
		String q = "SELECT regDate FROM PG_USER_ACCESS WHERE id = '"+userId+"' LIMIT 1";
		RecordSet rset = super.query(q);
		if(rset.size() > 0){
			return rset.getRow(0).getTimestamp("regDate");
		}else{
			return CommonUtil.getCurrentTimestamp();
		}
	}
	
	
	
	public void updateUserPwRetry(String id,int retry,String pwStatus){
		String q = "UPDATE PG_USER_PW SET pwStatus ='"+pwStatus+"', pwRetry="+retry +" WHERE lower(id) = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	public void updateUserPwRetry(String id,int retry){
		String q = "UPDATE PG_USER_PW SET pwRetry="+retry +" WHERE lower(id) = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	
	public void insertUserAcess(String id,String ipAddr,String userAgent){
		String q = "INSERT INTO PG_USER_ACCESS (id,ipAddr,userAgent,regDay) VALUES ('"+id+"','"+ipAddr+"','"+CommonUtil.cut(userAgent,180)+"',DATE_FORMAT(now(),'%Y%m%d'))";
		super.update(q);
	}
	
	
//  20190624 지불 및 정산 없을 시, 조회
//	public List<SharedMap<String,Object>> getChildForDist(String status){
//		String q = "SELECT distId as id ,name,`status` FROM PG_MAM_DIST WHERE distId != '0'" ;
//		if(CommonUtil.isNullOrSpace(status)){
//			q+= " ORDER BY name asc";
//		}else{
//			q+= " AND status = '"+status+"' ORDER BY name asc";
//		} 
//		
//		return super.query(q).getRows();
//	}
	
	
	public List<SharedMap<String,Object>> getChildForDist(String status){
		String q = "SELECT A.distId as id ,A.name, A.status FROM PG_MAM_DIST A, PG_MAM_DIST_MNG B WHERE A.distId != '0' AND A.distId = B.distId" ;
		if(CommonUtil.isNullOrSpace(status)){
			q+= " ORDER BY A.name asc";
		}else{
			q+= " AND A.status = '"+status+"' ORDER BY A.name asc";
		} 
		
		return super.query(q).getRows();
	}
	
	public List<SharedMap<String,Object>> getChildForAgency(String distId,String status){
		String q = "SELECT agencyId as id ,name,`status` FROM PG_MAM_AGENCY WHERE distId ='"+distId+"' AND agencyId !='0'";
		if(CommonUtil.isNullOrSpace(status)){
			q+= " ORDER BY name asc";
		}else{
			q+= " AND status = '"+status+"' ORDER BY name asc";
		} 
		return super.query(q).getRows();
	}
	
	
	public List<SharedMap<String,Object>> getChildForSales(String agencyId,String status){
		String q = "SELECT salesId as id ,name,`status` FROM PG_MAM_SALES WHERE agencyId ='"+agencyId+"' AND salesId !='0'";
		if(CommonUtil.isNullOrSpace(status)){
			q+= " ORDER BY name asc";
		}else{
			q+= " AND status = '"+status+"' ORDER BY name asc";
		} 
		return super.query(q).getRows();
	}
	
	public String getDistName(String distId){
		String q = "SELECT name FROM PG_MAM_DIST WHERE distId ='"+distId+"'";
		return super.query(q).getRow(0).getString("name");
	}
	
	public String getAgencyName(String agencyId){
		String q = "SELECT name FROM PG_MAM_AGENCY WHERE agencyId ='"+agencyId+"'";
		return super.query(q).getRow(0).getString("name");
	}


}