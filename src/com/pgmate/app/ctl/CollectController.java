package com.pgmate.app.ctl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.CollectGroupDAO;
import com.pgmate.app.dao.CollectSettleDAO;
import com.pgmate.app.dao.CollectSettleMchtDAO;
import com.pgmate.app.dao.CollectTempDAO;
import com.pgmate.app.dao.DepositDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.app.dao.VanDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class CollectController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.CollectController.class );
	
	@RequestMapping(value = {"/collect/excel/form"})
    public ModelAndView excelForm(HttpServletRequest request) {
		request.setAttribute("VAN_OPTION", new VanDAO().vanList().getRows());
		request.setAttribute("VANID_OPTION", new VanDAO().getVanId("").getRows());
		
        return new ModelAndView("/collect/excel/form");
    }
	
	@RequestMapping(value = "/collect/make/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		String category = cpRequest.getKeyValue("category");
		String categoryId = cpRequest.getKeyValue("categoryId");
		String collectDay = cpRequest.getKeyValue("collectDay");
		String collectId = cpRequest.getKeyValue("collectId");
//		DAO dao = new DAO();
//		dao.setTable("VW_TRX_CAP A LEFT JOIN VW_COLLECT_SETTLE_DTL B ON A.vanId = B.vanId AND A.mchtId = B.mchtId AND A.stlVanDay = B.collectDay ");
//		dao.setColumns(" A.stlVanDay,A.van,A.vanId,A.mchtId,A.name,MAX(A.tmnId) as tmnId, A.distId,A.agencyId,A.salesId,"
//						+ "SUM(IF(risk != '' AND capType = '매입', 0, A.amount)) as amount,"
//						+ "SUM(IF(risk != '' AND capType = '매입', 0, A.stlVanFee)) as stlVanFee,"
//						+ "SUM(A.amount-A.stlVanFee) as collectAmt,"
//						+ "SUM(IF(risk != '' AND capType = '매입', 0, A.amount-A.stlVanFee)) AS kwonAmt,"
//						+ "IF(B.amount IS NOT NULL , 'TRUE', 'FALSE') as isSaved," 
//						+ "B.amount as savedAmount, B.stlVanFee as savedStlVanFee, B.calcAmount as savedCalcAmount, B.collectAmount as savedCollectAmount, B.deductAmount as savedDeductAmount, B.summary");
//		logger.debug("COLLECT TEST collectId {}", collectId);
//		/*if(collectId != null && collectId.length() > 1) {
//			dao.addWhere("collectId", collectId);
//		}*/
//		dao.addWhere("stlVanDay", collectDay);
//		dao.addWhere("vanStatus", "입금대기", DAO.eq);
//		if(category.equals("van")) {
//			dao.addWhere("van", categoryId);
//		} else if(category.equals("vanId")) {
//			dao.addWhere("A.vanId", categoryId);
//		} else if(category.equals("mcht")) {
//			dao.addWhere("A.mchtId", categoryId);
//		}
//		dao.setGroupBy("stlVanDay, van, vanId, mchtId");
//		dao.setOrderBy("name asc");
//		
//		RecordSet rset = dao.search();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select A.*, IF(B.amount IS NOT NULL , 'TRUE', 'FALSE') as isSaved, B.amount as savedAmount, B.stlVanFee as savedStlVanFee, B.calcAmount as savedCalcAmount, B.collectAmount as savedCollectAmount, B.deductAmount as savedDeductAmount, B.summary ");
		sb.append("from (select stlVanDay,van,vanId,mchtId,name,MAX(tmnId) as tmnId, distId,agencyId,salesId, ");
		sb.append("SUM(IF(risk != '' AND capType = '매입', 0, amount)) as amount, ");
		sb.append("SUM(IF(risk != '' AND capType = '매입', 0, stlVanFee)) as stlVanFee, ");
		sb.append("SUM(amount-stlVanFee) as collectAmt, ");
		sb.append("SUM(IF(risk != '' AND capType = '매입', 0, amount-stlVanFee)) AS kwonAmt from VW_TRX_CAP ");
		sb.append("WHERE stlVanDay = '"+collectDay+"' ");
		sb.append("AND vanStatus = '입금대기' ");
		
		if(category.equals("van")) {
			sb.append("AND van = '"+categoryId+"' ");
		}else if(category.equals("vanId")) {
			sb.append("AND vanId = '"+categoryId+"' ");
		}else if(category.equals("mcht")) {
			sb.append("AND mchtId = '"+categoryId+"' ");
		}else if(category.equals("mchtId")) {
		sb.append("AND mchtId = '"+categoryId+"' ");
		}
		
		sb.append("group by stlVanDay, van, vanId, mchtId) A ");
		sb.append("LEFT JOIN VW_COLLECT_SETTLE_DTL B ON A.vanId = B.vanId AND A.mchtId = B.mchtId AND A.stlVanDay = B.collectDay order by A.name asc");
		DAO dao = new DAO();
		RecordSet rset = dao.query(sb.toString());
		dao.initRecord();
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request,"/collect/make/list","");
	}
	
	@RequestMapping(value = "/collect/detail/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView detailList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		String collectId = cpRequest.getKeyValue("collectId");

		DAO dao = new DAO();

		dao.setTable("VW_COLLECT_SETTLE_DTL");
		dao.setColumns("*");
		dao.addWhere("collectId", collectId);
		dao.setOrderBy("name asc");
		
		RecordSet rset = dao.search();
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request,"/collect/detail/list","");
	}
	
	@RequestMapping(value = {"/collect/temp/form"})
    public ModelAndView tempForm(HttpServletRequest request) {
		request.setAttribute("VAN_OPTION", new VanDAO().vanList().getRows());
		request.setAttribute("VANID_OPTION", new VanDAO().getVanId("").getRows());
		
        return new ModelAndView("/collect/temp/form");
    }
    
	
	@RequestMapping(value = "/collect/temp/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView tempList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		CollectTempDAO dao = new CollectTempDAO();
		if(cpRequest.getKeyValue("stlVanDay").indexOf("-") > -1) {
			cpRequest.replaceKeyValue("stlVanDay", cpRequest.getKeyValue("stlVanDay").replaceAll("-", ""));
		}
		
		RecordSet rset = dao.makeSearch(cpRequest.data);
		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request,"/collect/temp/list","");
	}
    
    @RequestMapping(value = {"/collect/collect/form"})
    public ModelAndView collectForm(HttpServletRequest request) {
		request.setAttribute("VAN_OPTION", new VanDAO().vanList().getRows());
		request.setAttribute("VANID_OPTION", new VanDAO().getVanId("").getRows());
		
        return new ModelAndView("/collect/collect/form");
    }
    
	
	@RequestMapping(value = "/collect/collect/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView collectList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		CollectSettleDAO collectDAO = new CollectSettleDAO();
		if(cpRequest.getKeyValue("stlVanDay").indexOf("-") > -1) {
			cpRequest.replaceKeyValue("stlVanDay", cpRequest.getKeyValue("stlVanDay").replaceAll("-", ""));
		}
		collectDAO.setOrderBy("regDate desc, name asc");
		RecordSet rset = collectDAO.search(cpRequest.data);
		return new CPRUtil(cpRequest).dataList(rset, collectDAO).setView(request,"/collect/collect/list","");
	}
	
	@RequestMapping(value = "/collect/collect/view/{collectId}", method = RequestMethod.GET)
    public ModelAndView errView(HttpServletRequest request, @PathVariable String collectId) {
		DAO dao = new DAO();
		dao.setTable("VW_COLLECT_SETTLE");
		dao.setColumns("*");
		dao.addWhere("collectId", collectId);
		SharedMap<String, Object> colMap = dao.search().getRow(0);
		request.setAttribute("COLLECT_MAP", colMap);
		if(colMap.getString("status").equals("생성")) {
			return new ModelAndView("/collect/make/form");
		} else {
			return new ModelAndView("/collect/detail/form");
		}
    }
	
	@RequestMapping(value = "/collect/group/find/{idx}/{category}", method = RequestMethod.GET)
    public @ResponseBody SharedMap<String, Object> findGroupForCategory(HttpServletRequest request, @PathVariable String idx, @PathVariable String category) {
		SharedMap<String, Object> resultMap = new SharedMap<>();
		DAO dao = new DAO();
		dao.setTable("PG_COLLECT_GROUP");
		dao.setColumns("*");
		dao.addWhere("status", "사용", DAO.eq);
		dao.addWhere("category", category, DAO.eq);
		
    	RecordSet rset = dao.search();
        if(rset.size() > 0) {
        	List<SharedMap<String, Object>> selectOptionList = rset.getRows();
        	resultMap.put(idx, selectOptionList);
        }
        
		return resultMap;
    }
	
	@RequestMapping(value = "/collect/group/find", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> findGroup(HttpServletRequest request,@RequestBody SharedMap<String, SharedMap<String, String>> requestMap) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();

		Iterator<String> keys = requestMap.keySet().iterator();
        while( keys.hasNext() ){
        	String key = keys.next();
        	if(!getCollectGroup(key, requestMap.get(key), resultMap)) {
        		continue;
        	}
        }

		return resultMap;
	}
	
	private boolean getCollectGroup(String key, SharedMap<String, String> eachMap, SharedMap<String, Object> resultMap) {
		boolean result = false;
		DAO dao = new DAO();
		dao.initRecord();
    	dao.setTable("PG_COLLECT_SETTLE");
    	dao.setColumns("collectId");
    	dao.addWhere("collectId", eachMap.getString("collectId"));
    	RecordSet rset = dao.search();
    	if(rset.size() > 0) {
    		resultMap.put(key, "DUPLICATION");
    		return false;
    	}
    	dao.initRecord();
    	dao.setTable("PG_COLLECT_GROUP");
		dao.setColumns("*");
		
		String sender = eachMap.getString("sender");
		String category = "";
		dao.addWhere("sender", sender, DAO.eq);
    	RecordSet rset2 = dao.search();
    	
		dao.initRecord();
		dao.setTable("PG_COLLECT_GROUP");
		dao.setColumns("*");
		dao.addWhere("status", "사용", DAO.eq);
		if(!sender.isEmpty() && rset2.size() > 0) {
			category = rset2.getRow(0).getString("category");
			dao.addWhere("category", category, DAO.eq);
		} else {
			dao.addWhere("category", "vanId", DAO.eq);
		}
		
    	RecordSet rset3 = dao.search();
        if(rset3.size() > 0) {
        	List<SharedMap<String, Object>> selectOptionList = rset3.getRows();
        	for(SharedMap<String, Object> eachOptionMap : selectOptionList) {
        		if(eachOptionMap.getString("sender").equalsIgnoreCase(sender)) {
        			eachOptionMap.put("selected", true);
        		}
        	}
        	resultMap.put(key, selectOptionList);
        	result = true;
        }
        
		return result;
	}
	
	/*
	 * 입금정산 엑셀업로드 반영
	 */
	@RequestMapping(value = "/collect/add", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> add(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		
		DAO dao = new DAO();
		for(SharedMap<String, String> eachMap : requestList) {
			
			// 입금예정액 가져오기
			CollectGroupDAO collectGroupDAO = new CollectGroupDAO();
			SharedMap<String,Object> sharedMap = collectGroupDAO.getByColgId(eachMap.getString("colgId")).getRowFirst();
			CPDAO cpDAO = new CPDAO();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT SUM(IF(risk != '' AND capType = '매입', 0, amount-stlVanFee)) AS kwonAmt FROM VW_TRX_CAP ");
			sb.append("WHERE stlVanDay = '"+eachMap.getString("collectDay")+"' ");
			sb.append("AND vanStatus = '입금대기' ");

			if(sharedMap.isEquals("category", "van")) {
				sb.append("AND van = '"+sharedMap.getString("categoryId")+"'" );
			}else if(sharedMap.isEquals("category", "vanId")) {
				sb.append("AND vanId = '"+sharedMap.getString("categoryId")+"'" );
			}else if(sharedMap.isEquals("category", "mcht")) {
				sb.append("AND mchtId = '"+sharedMap.getString("categoryId")+"'" );
			}else if(sharedMap.isEquals("category", "mchtId")) {
				sb.append("AND mchtId = '"+sharedMap.getString("categoryId")+"'" );
			}
			long MARUAmt = cpDAO.query(sb.toString()).getRowFirst().getLong("MARUAmt");
			cpDAO.initRecord();
			
			dao.setTable("PG_COLLECT_SETTLE");
			dao.setRecord("collectId", eachMap.getString("collectId"));
    		dao.setRecord("colgId", eachMap.getString("colgId"));
    		dao.setRecord("collectDay", eachMap.getString("collectDay"));
    		dao.setRecord("collectTime", eachMap.getString("collectTime"));
    		dao.setRecord("stlVanAmount", MARUAmt);
    		dao.setRecord("collectAmount", eachMap.getString("collectAmount"));
    		dao.setRecord("collectAmt", 0);
    		dao.setRecord("summary", eachMap.getString("summary"));
    		dao.setRecord("regId", SessionUtil.getUserId(request));
    		dao.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
    		if(dao.insert()) {
    			resultMap.put("result", "OK");
    		} else {
    			resultMap.put("result", "NOK");
    			resultMap.put("msg", eachMap.getString("collectId") + " DB 입력에 실패했습니다.");
    			break;
    		}
    		dao.initRecord();
		}
    	
		return resultMap;
	}
	/*	가맹점별 입금 정보 저장
	 * 
	 */	
	@RequestMapping(value = "/collect/decide/{collectId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> decide(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList, @PathVariable String collectId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		
		DAO dao = new DAO();
		dao.setTable("PG_COLLECT_SETTLE");
		dao.setColumns("*");
		dao.addWhere("collectId", collectId);
		SharedMap<String, Object> collectMap = dao.search().getRow(0);
		dao.initRecord();
		String vanId = "";
		String vanIdRisk = "";
		String mchtId = "";
		String mchtIdRisk = "";

		HashSet<String> mchtSet = new HashSet<String>();
		HashSet<String> mchtRiskSet = new HashSet<String>();
		int riskCount = 0;
		
		for(SharedMap<String, String> eachMap : requestList) {
				
			logger.debug("MCHT DECIDE = mchtId: {}, vanId: {}", eachMap.getString("mchtId"), eachMap.getString("vanId"));
//			dao.setDebug(true);
			dao.setTable("PG_COLLECT_SETTLE_DTL");
			dao.setColumns("idx");
			dao.addWhere("collectId", eachMap.getString("collectId"));
			dao.addWhere("vanId", eachMap.getString("vanId"));
			dao.addWhere("mchtId", eachMap.getString("mchtId"));
			RecordSet alreadyRest = dao.search();
				
			dao.initRecord();
			dao.setTable("PG_COLLECT_SETTLE_DTL");
			dao.setRecord("collectId", eachMap.getString("collectId"));
			dao.setRecord("vanId", eachMap.getString("vanId"));
    		dao.setRecord("mchtId", eachMap.getString("mchtId"));
    		dao.setRecord("amount", eachMap.getString("amount"));
    		dao.setRecord("stlVanFee", eachMap.getString("stlVanFee"));
    		dao.setRecord("calcAmount", eachMap.getString("calcAmount"));
    		dao.setRecord("collectAmount", eachMap.getString("collectAmount"));
    		dao.setRecord("deductAmount", eachMap.getString("deductAmount"));
    		dao.setRecord("summary", eachMap.getString("summary"));
    		dao.setRecord("regId", SessionUtil.getUserId(request));
    		dao.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
				
    		if(alreadyRest.size() > 0) {
				dao.addWhere("collectId", eachMap.getString("collectId"));
				dao.addWhere("vanId", eachMap.getString("vanId"));
				dao.addWhere("mchtId", eachMap.getString("mchtId"));
				if(!dao.update()) {
					resultMap.put("result", "NOK");
	    			resultMap.put("msg", eachMap.getString("collectId") + " - " + eachMap.getString("vanId") + " - " + eachMap.getString("mchtId") + " DB 업데이트에 실패했습니다.");
	    			break;
				}
				
				dao.initRecord();
				dao.setTable("PG_COLLECT_SETTLE_IDX");
				dao.addWhere("collectId", collectId);
				dao.addWhere("vanId", eachMap.getString("vanId"));
				dao.addWhere("mchtId", eachMap.getString("mchtId"));
				if(!dao.delete()) {
					resultMap.put("result", "NOK");
					resultMap.put("msg", " IDX DB 제거에 실패했습니다.");
					//break;
				}
				
				dao.initRecord();
				dao.setTable("PG_COLLECT_SETTLE_RISK_IDX");
				dao.setColumns("*");
				dao.addWhere("collectId", collectId);
				dao.addWhere("vanId", eachMap.getString("vanId"));
				dao.addWhere("mchtId", eachMap.getString("mchtId"));
				dao.setOrderBy("");
				RecordSet alreadyRiskSet = dao.search();
				if(alreadyRiskSet.size() > 0){
					if(!dao.delete()) {
						resultMap.put("result", "NOK");
						resultMap.put("msg", " RISK IDX DB 제거에 실패했습니다.");
						//break;
					}
				}
			} else if(!dao.insert()) {
				resultMap.put("result", "NOK");
    			resultMap.put("msg", eachMap.getString("collectId") + " - " + eachMap.getString("vanId") + " - " + eachMap.getString("mchtId") + " DB 입력에 실패했습니다.");
    			break;
			}
    		vanId += "'"+eachMap.getString("vanId")+"',";
    		mchtSet.add(eachMap.getString("mchtId"));
    		/*
    		if(eachMap.getLong("deductAmount") > 0) {
    			vanIdRisk += "'"+eachMap.getString("vanId")+"',";
        		mchtRiskSet.add(eachMap.getString("mchtId"));
        		riskCount ++;
    		}*/
    		
    		/*
    		dao.initRecord();
    		dao.setTable("VW_TRX_CAP");
    		dao.setColumns("capId, vanId, mchtId");
    		dao.addWhere("stlVanDay", collectMap.getString("collectDay"));
    		dao.addWhere("vanId", eachMap.getString("vanId"));
    		dao.addWhere("mchtId", eachMap.getString("mchtId"));
    		dao.addWhere("vanStatus", "입금완료", DAO.ne);
    		dao.addWhere("risk", "", DAO.eq);
    		RecordSet rset = dao.search();
    		if(rset.size() > 0) {
    			insertCollectSettleIdx(rset.getRows(), collectId);
    		}
    		dao.initRecord();
    		if(eachMap.getLong("deductAmount") > 0) {
    			// 리스크 있는 거래
        		dao.setTable("VW_TRX_CAP");
        		dao.setColumns("capId, vanId, mchtId");
        		dao.addWhere("stlVanDay", collectMap.getString("collectDay"));
        		dao.addWhere("vanId", eachMap.getString("vanId"));
        		dao.addWhere("mchtId", eachMap.getString("mchtId"));
        		dao.addWhere("risk", "", DAO.ne);
        		RecordSet rset2 = dao.search();
        		if(rset2.size() > 0) {
        			insertCollectSettleRiskIdx(rset2.getRows(), collectId);
        		}
        		dao.initRecord();
    		}*/
    		
		}
		vanId = vanId.substring(0, vanId.length()-1);
		Iterator<String> itr = mchtSet.iterator();
		while(itr.hasNext()){
			mchtId += "'"+itr.next()+"',";
		}
		mchtId = mchtId.substring(0, mchtId.length()-1);
		dao.initRecord();
		dao.setDebug(true);
		dao.setTable("VW_TRX_CAP");
		dao.setColumns("capId, vanId, mchtId");
		dao.addWhere("stlVanDay", collectMap.getString("collectDay"));
		dao.addWhere("vanId IN ("+vanId+")");
		dao.addWhere("mchtId IN ("+mchtId+")");
		dao.addWhere("vanStatus", "입금완료", DAO.ne);
//		dao.addWhere("risk", "", DAO.eq);
		RecordSet rset = dao.search();
		if(rset.size() > 0) {
			insertCollectSettleIdx(rset.getRows(), collectId);
		}
		dao.initRecord();
		/*
		if(riskCount > 0){
			vanIdRisk = vanIdRisk.substring(0, vanIdRisk.length()-1);
			Iterator<String> itr2 = mchtSet.iterator();
			while(itr2.hasNext()){
				mchtIdRisk += "'"+itr2.next()+"',";
			}
			mchtIdRisk = mchtIdRisk.substring(0, mchtIdRisk.length()-1);
			
			// 리스크 있는 거래
    		dao.setTable("VW_TRX_CAP");
    		dao.setColumns("capId, vanId, mchtId");
    		dao.addWhere("stlVanDay", collectMap.getString("collectDay"));
    		dao.addWhere("vanId IN ("+vanIdRisk+")");
    		dao.addWhere("mchtId IN ("+mchtIdRisk+")");
    		dao.addWhere("risk", "", DAO.ne);
    		RecordSet rset2 = dao.search();
    		if(rset2.size() > 0) {
    			insertCollectSettleRiskIdx(rset2.getRows(), collectId);
    		}
    		dao.initRecord();
		}*/
		
		if(!resultMap.getString("result").equals("NOK")) {
			resultMap.put("result", "OK");
		} else {
			resultMap.put("result", "NOK");
			if(resultMap.getString("msg").length() < 1) {
				resultMap.put("msg", collectId + " 업데이트에 실패했습니다.");
			}
		}
    	// PG_COLLECT_SETTLE의 collectAmt 업데이트
		CPDAO cpDAO = new CPDAO();
		String query = "SELECT SUM(collectAmount) AS collectAmt FROM PG_COLLECT_SETTLE_DTL WHERE collectId = '"+collectId+"'";
		long collectAmt = cpDAO.query(query).getRowFirst().getLong("collectAmt");
		cpDAO.initRecord();
		cpDAO.update("UPDATE PG_COLLECT_SETTLE set collectAmt = "+collectAmt+" WHERE collectId = '"+collectId+"'");
		cpDAO.initRecord();
		
		return resultMap;
	}
	
	public int insertCollectSettleIdx(List<SharedMap<String, Object>> capList, String collectId) {
		int inserted = 0;
		logger.debug("insert PG_COLLECT_SETTLE_IDX batch : {}", capList.size());
		String query = "INSERT INTO `PG_COLLECT_SETTLE_IDX` (`collectId`, `capId`, `vanId`, `mchtId`) " + "VALUES (?,?,?,?);";

		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;

			for (SharedMap<String, Object> map : capList) {
				int i = 1;
				pstmt.setString(i++, collectId);
				pstmt.setString(i++, map.getString("capId"));
				pstmt.setString(i++, map.getString("vanId"));
				pstmt.setString(i++, map.getString("mchtId"));
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch PG_COLLECT_SETTLE_IDX error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
	
	public int insertCollectSettleRiskIdx(List<SharedMap<String, Object>> capList, String collectId) {
		int inserted = 0;
		logger.debug("insert PG_COLLECT_SETTLE_RISK_IDX batch : {}", capList.size());
		String query = "INSERT INTO `PG_COLLECT_SETTLE_RISK_IDX` (`collectId`, `capId`, `vanId`, `mchtId`) " + "VALUES (?,?,?,?);";
		
		DBManager db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);

			int batchSize = 100;
			int count = 0;

			for (SharedMap<String, Object> map : capList) {
				int i = 1;
				pstmt.setString(i++, collectId);
				pstmt.setString(i++, map.getString("capId"));
				pstmt.setString(i++, map.getString("vanId"));
				pstmt.setString(i++, map.getString("mchtId"));
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					inserted += pstmt.executeBatch().length;
				}
			}

			inserted += pstmt.executeBatch().length;
			conn.commit();
		} catch (Exception e) {
			logger.debug("insert batch PG_COLLECT_SETTLE_RISK_IDX error : {}", CommonUtil.getExceptionMessage(e));
		} finally {
			db.close(pstmt);
			db.close(conn);
		}
		return inserted;
	}
	
	
	@RequestMapping(value = "/collect/finish/{collectId}", method = RequestMethod.GET)
	public @ResponseBody SharedMap<String, Object> finish(HttpServletRequest request, @PathVariable String collectId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		logger.debug("FINISH : {}", collectId);
		CPSession session = SessionUtil.get(request);
		if(!session.getGrade().equals("본사") || !session.getRole().equals("마스터")) {
			logger.debug("권한 없음 실패");
			resultMap.put("result", "NOK");
			resultMap.put("msg", "접근할 수 있는 권한이 없습니다.");
			return resultMap;
		}
		
		DAO dao = new DAO();
		dao.setTable("PG_COLLECT_SETTLE_DTL");
		dao.setColumns("SUM(IF(deductAmount > 0, deductAmount,0)) as remainingAmount, SUM(IF(deductAmount < 0, deductAmount,0)) as deductAmount");
		dao.addWhere("collectId", collectId);
		RecordSet rset = dao.search();
		if(rset.size() < 1) {
			resultMap.put("result", "NOK");
			resultMap.put("msg", collectId + " 업데이트에 실패했습니다.");
			return resultMap;
		}
		SharedMap<String, Object> sumMap = rset.getRow(0);
		if(sumMap.isNullOrSpace("deductAmount")) {
			sumMap.put("deductAmount", "0");
		}
		if(sumMap.isNullOrSpace("remainingAmount")) {
			sumMap.put("remainingAmount", "0");
		}
		dao.initRecord();
		if(dao.update("UPDATE PG_COLLECT_SETTLE SET status = '확정', deductAmount = '"+sumMap.getString("deductAmount")+"', remainingAmount = '"+sumMap.getString("remainingAmount")+"' WHERE status = '생성' AND collectId ='" + collectId + "'")) {
			resultMap.put("result", "OK");
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", collectId + " 업데이트에 실패했습니다.");
			return resultMap;
		}
		
		if(new TrxDAO().updateCollectedCapDtl(collectId) > 0) {
			resultMap.put("result", "OK");
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", collectId + " 업데이트에 실패했습니다.");
		}
		
		if(new TrxDAO().updateCollectedCapDtl(collectId, true) < 1) {
			logger.debug("리스크 거래 없음.");
		} else {
			new DepositDAO().setAddByCollect(collectId, SessionUtil.getUserId(request));
		}
		
		return resultMap;
	}
	
	
	@RequestMapping(value = "/collect/delete/array", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> deleteCollectArray(HttpServletRequest request, @RequestBody SharedMap<String, Object> reuqestMap) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		resultMap.put("result", "NOK");
		List<String> collectIds = (List<String>)reuqestMap.get("collectIds");
		logger.debug("collectIdList: {}", collectIds.toString());
		
		CollectSettleDAO collectSettleDAO = new CollectSettleDAO();
		for(String collectId : collectIds) {
			RecordSet rset = collectSettleDAO.getById(collectId);
			if(collectId.length() > 0 && rset.size() > 0) {
				if(rset.getRow(0).getString("status").equals("생성")) {
					DAO dao = new DAO();
					dao.setTable("PG_COLLECT_SETTLE");
					dao.addWhere("collectId", collectId, DAO.eq);
					if(dao.delete()) {
						resultMap.put("result", "OK");
					} else {
						resultMap.put("msg", "DB 삭제에 실패했습니다.");
						break;
					}
				} else {
					resultMap.put("msg", "삭제 할 수 없는 상태입니다(" + rset.getString("status") + ").");
					break;
				}
			} else {
				resultMap.put("msg", "해당하는 항목이 없습니다.");
				break;
			}
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/collect/delete/{collectId}", method = RequestMethod.GET)
	public @ResponseBody SharedMap<String, Object> deleteCollect(HttpServletRequest request, @PathVariable String collectId) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		resultMap.put("result", "NOK");
		
		CollectSettleDAO collectSettleDAO = new CollectSettleDAO();
		RecordSet rset = collectSettleDAO.getById(collectId);
		if(collectId.length() > 0 && rset.size() > 0) {
			if(rset.getRow(0).getString("status").equals("생성")) {
				DAO dao = new DAO();
				dao.setTable("PG_COLLECT_SETTLE");
				dao.addWhere("collectId", collectId, DAO.eq);
				if(dao.delete()) {
					resultMap.put("result", "OK");
				} else {
					resultMap.put("msg", "DB 삭제에 실패했습니다.");
				}
			} else {
				resultMap.put("msg", "삭제 할 수 없는 상태입니다(" + rset.getString("status") + ").");
			}
		} else {
			resultMap.put("msg", "해당하는 항목이 없습니다.");
		}
		return resultMap;
	}
	
	/* 마이너스 입금정산 생성 */
	@RequestMapping(value = "/collect/minus/add", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SharedMap<String, Object> minusAdd(HttpServletRequest request,@RequestBody SharedMap<String, String> requestMap) {
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		DAO dao = new DAO();
		dao.setTable("PG_COLLECT_GROUP");
		dao.setColumns("colgId");
		dao.addWhere("categoryId", requestMap.getString("categoryId"));
		RecordSet rset = dao.search();
		if(rset.size() < 1) {
			resultMap.put("result", "NOK");
			resultMap.put("msg", requestMap.getString("collectId") + "올바른 대상이 없습니다.");
			return resultMap;
		}
		String colgId = rset.getRowFirst().getString("colgId");
		logger.debug("colgId {}", colgId);
		
		dao.setTable("PG_COLLECT_SETTLE");
		dao.setRecord("collectId", requestMap.getString("collectId"));
		dao.setRecord("colgId", colgId);
		dao.setRecord("collectDay", requestMap.getString("collectDay"));
		dao.setRecord("collectTime", requestMap.getString("collectTime"));
		dao.setRecord("stlVanAmount", requestMap.getString("collectAmount"));
		dao.setRecord("collectAmount", requestMap.getString("collectAmount"));
		dao.setRecord("collectAmt", 0);
		dao.setRecord("summary", requestMap.getString("summary"));
		dao.setRecord("regId", SessionUtil.getUserId(request));
		dao.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		if(dao.insert()) {
			resultMap.put("result", "OK");
		} else {
			resultMap.put("result", "NOK");
			resultMap.put("msg", requestMap.getString("collectId") + " DB 입력에 실패했습니다.");
		}
		dao.initRecord();
		
		
		return resultMap;
	}
	
	@RequestMapping(value = "/collect/mcht/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView mchtList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		CollectSettleMchtDAO settleMchtDAO = new CollectSettleMchtDAO();		
		RecordSet rset = settleMchtDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, settleMchtDAO).setView(request,"/collect/mcht/list","");
	}
}






