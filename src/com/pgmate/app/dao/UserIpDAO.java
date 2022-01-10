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
public class UserIpDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.UserIpDAO.class );
	private static final String TABLE = "PG_USER_IP";
	private static final String COLUMNS = "*";

	public UserIpDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(UserIpDAO.COLUMNS);
	}
	
	public boolean insertIp(String id,String ipAddr,String userAgent){
		String q = "INSERT INTO PG_USER_IP (id,ipAddr,userAgent, expireDay,regDay) VALUES ('"+id+"','"+ipAddr+"','"+CommonUtil.cut(userAgent,180)+"',DATE_FORMAT(DATE_ADD(now(), INTERVAL 90 DAY),'%Y%m%d'),DATE_FORMAT(now(),'%Y%m%d'))";
		return super.update(q);
	}
	
	public boolean updateExpireDay(String id, String ipAddr) {
		String q = "UPDATE PG_USER_IP SET expireDay = DATE_FORMAT(DATE_ADD(now(), INTERVAL 90 DAY),'%Y%m%d') WHERE id = '"+id+"' AND ipAddr = '" + ipAddr + "' ";
		return super.update(q);
	}
	
	public RecordSet getByActiveIp(String userId) {
		setWhere("(id = '"+ userId +"' AND expireDay > DATE_FORMAT(now(),'%Y%m%d')) OR id = 'default' ");
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		this.setTable("VW_USER");
		this.setColumns("distId, agencyId, id, pw, `name`, grade, role, parentId, `status`, phone, regId, regDay, regDate");
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	

}