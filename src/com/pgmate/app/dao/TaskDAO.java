package com.pgmate.app.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
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
public class TaskDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.TaskDAO.class );
	private static final String TABLE = "VW_TASK";
	private static final String COLUMNS = "idx, task, targetId, targetGrade, status, regId, regDay, regDate, grade, userName, parentId, gradeName";
	
	public TaskDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(TaskDAO.COLUMNS);
	}
	
	public RecordSet getById(String idx){
		addWhere("lower(idx)",idx.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getList(String userId, String grade){
		if(!grade.equals("본사")) {
			setWhere("targetId IN ('', '"+userId+"') AND targetGrade IN ('','"+ grade +"')");
		}
		
		addWhere("status", "삭제", ne);
		return search();
	}
	
	public List<SharedMap<String,Object>> replaceDate(List<SharedMap<String,Object>> targetList) {
		for(SharedMap<String,Object> each : targetList) {
			String curDate = CommonUtil.getCurrentDate("yyyyMMdd");
			
			if(curDate.equals(each.getString("regDay"))) {
				String curHour = CommonUtil.getCurrentDate("yyyyMMddHH");
				SimpleDateFormat df= new SimpleDateFormat("yyyyMMddHH");
				Date regDate = each.getTimestamp("regDate");
				String hour = df.format(regDate);
				if(curHour.equals(hour)) {
					String curMin = CommonUtil.getCurrentDate("yyyyMMddHHmm");
					SimpleDateFormat dfMin= new SimpleDateFormat("yyyyMMddHHmm");
					String minute = dfMin.format(regDate);
					if(curMin.equals(minute)) {
						each.put("date", "지금");
					} else {
						each.put("date", (Long.valueOf(curMin) - Long.valueOf(minute)) + " 분 전");
					}
				} else {
					each.put("date", (Integer.valueOf(curHour) - Integer.valueOf(hour)) + " 시간 전");
				}

			} else if(curDate.substring(0, 6).equals(each.getString("regDay").substring(0, 6))) {
				each.put("date", (Integer.valueOf(curDate) - Integer.valueOf(each.getString("regDay"))) + " 일 전");
			} else {
				each.put("date", each.getString("regDay").substring(0, 4) + "-" + each.getString("regDay").substring(4, 6)+ "-" + each.getString("regDay").substring(6, 8));
			}
		}
		
		return targetList;
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