package com.pgmate.app.ctl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.interceptor.SessionExclude;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */

@Controller
public class InitController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.InitController.class);
	private static int PortCheck = 0 ;
	
	
	@RequestMapping(value = {"/init" })
	@SessionExclude
	public ModelAndView init(HttpServletRequest request) {
		
		if (!SessionUtil.isLive(request)) {
//			SessionUtil.addLoginRequest(request.getRemoteAddr(), request);
			PortCheck = request.getServerPort();
			return new ModelAndView("/sso/websso","port", PortCheck);			
		} else {
			
//			if(SessionUtil.checkLoginPort(request.getRemoteAddr(), request) == false) {
//				SessionUtil.destroyById(userId, request);
//				return new ModelAndView("/sso/websso","port", PortCheck);
//			}
			
//			request.getSession().setAttribute(userId, cpSession);
//			int localPort = request.getServerPort();
			
			
			// KBR : portCheck 코드 추가
//			if(request.getServerPort() != PortCheck) {
//				SessionUtil.destroy(request);
//			}
			
//			if(!SessionUtil.addLoginRequest(request.getRemoteAddr(), request)) {
//				SessionUtil.destroy(SessionUtil.getLoginRequest(request.getRemoteAddr()));
//			}
		
			CPSession cpSession = SessionUtil.get(request);
			String userId = cpSession.getUserId();
			
			/*
			if (cpSession.getUserId() == "jhryu") {
				CPUtil.CP_DEBUG = true;
			} */
			
			if (request.getServerName().indexOf("cp.cyrexpay.com") > -1 || cpSession.getUserId() == "yhbae" || cpSession.getUserId() == "ginaida") {
				CPUtil.CP_DEBUG = true;
			} 
			if (cpSession.getGrade().equals("본사")){
				initAdmin(request, cpSession);
				return new ModelAndView("/main/admin");
			}
			StringBuffer sb = new StringBuffer();
			sb.append("DATE_FORMAT(NOW(),'%Y%m%d') as today,");
			sb.append("FORMAT(SUM(IF(capType = '매입' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS dayPay,");
			sb.append("FORMAT(SUM(IF(capType = '매입' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS dayPayCnt,");
			sb.append("FORMAT(SUM(IF(capType = '매입취소' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS dayRef,");
			sb.append("FORMAT(SUM(IF(capType = '매입취소' AND regDay = DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS dayRefCnt,");
			sb.append("FORMAT(SUM(IF(capType = '매입' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS monthPay,");
			sb.append("FORMAT(SUM(IF(capType = '매입' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS monthPayCnt,");
			sb.append("FORMAT(SUM(IF(capType = '매입취소' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), amount, 0)),0) AS monthRef,");
			sb.append("FORMAT(SUM(IF(capType = '매입취소' AND regDay >= DATE_FORMAT(NOW(),'%Y%m01') AND regDay <= DATE_FORMAT(NOW(),'%Y%m%d'), 1, 0)),0) AS monthRefCnt");
			
			String whereStr = "";
			if (cpSession.getGrade().equals("대행사")) {
				whereStr = "distId";
			} else if (cpSession.getGrade().equals("에이전시")) {
				whereStr = "agencyId";
			} else if (cpSession.getGrade().equals("지사")) {
				whereStr = "salesId";
			} else if (cpSession.getGrade().equals("가맹점")) {
				whereStr = "mchtId";
			}
			
			CPDAO dao = new CPDAO();
			dao.setTable("VW_TRX_CAP");
			dao.setColumns(sb.toString());
			if(whereStr != "") {
				dao.setWhere("SUBSTR(trxDay,1,6) = DATE_FORMAT(NOW(),'%Y%m') AND " + whereStr + " = '" + cpSession.getParentId() + "'");
			} else {
				dao.setWhere("SUBSTR(trxDay,1,6) = DATE_FORMAT(NOW(),'%Y%m')");
			}
			
			request.setAttribute("PAYMAP", dao.search().getRowFirst());
			logger.debug("GRADE: {} ", cpSession.getGrade());
			if (cpSession.getGrade().equals("가맹점")) {
				initMcht(request, cpSession);
				return new ModelAndView("/main/mcht");
			} else if (cpSession.getGrade().equals("터미널")){
				dao.setTable("VW_MCHT_TMN");
				dao.setColumns("*");
				dao.setWhere("status IN ('대기','예비')");
				dao.setLimit(500);
				return new ModelAndView("/main/open", "TMNMAP", dao.search().getRows());
			} else if (cpSession.getGrade().equals("하위가맹점")) {
				return new ModelAndView("/subMcht/trx/form");
			} else {
				initMember(request, cpSession, whereStr);
				return new ModelAndView("/main/member");
			}
		}
	}
	
	private void initAdmin(HttpServletRequest request, CPSession cpSession) {
		CPDAO dao = new CPDAO();
		//dao.setDebug(true);
		dao.setTable("PG_REGISTER_STATS");
		dao.setColumns("SUM(IF(grade = 'mcht', 1, 0)) as mchtCnt, " +
				"SUM(IF(grade = 'mcht' AND regDay > LAST_DAY(NOW() - interval 1 month), 1, 0)) as mchtMonthCnt, " +
				"SUM(IF(grade = 'tmn', 1, 0)) as tmnCnt, " +
				"SUM(IF(grade = 'tmn' AND regDay > LAST_DAY(NOW() - interval 1 month), 1, 0)) as tmnMonthCnt, " +
				"SUM(IF(grade = 'agency', 1, 0)) as agencyCnt, " +
				"SUM(IF(grade = 'agency' AND regDay > LAST_DAY(NOW() - interval 1 month), 1, 0)) as agencyMonthCnt, " +
				"SUM(IF(grade = 'dist', 1, 0)) as distCnt, " +
				"SUM(IF(grade = 'dist' AND regDay > LAST_DAY(NOW() - interval 1 month), 1, 0)) as distMonthCnt ");
		//dao.setWhere("status = '사용' OR status = '예비'");
		dao.setOrderBy("");
		request.setAttribute("REG_STATS_MAP", dao.search().getRowFirst());

		/*
		List<SharedMap<String, Object>> result = new ArrayList<SharedMap<String, Object>>();
		CPDAO settleDAO = new CPDAO();
		settleDAO.setTable("VW_TRX_CAP");
		settleDAO.setColumns("SUM(stlVanAmount) as stlVanAmount, stlVanDay");
		settleDAO.setWhere("stlVanDay > LAST_DAY(NOW() - interval 1 month) AND stlVanDay <= LAST_DAY(NOW())");
		settleDAO.setGroupBy("stlVanDay");
		List<SharedMap<String, Object>> resList = settleDAO.search().getRows();
		
		for (SharedMap<String, Object> each : resList) {
			if (each.getLong("stlVanAmount") > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlVanDay = each.getString("stlVanDay");
				stlVanDay = stlVanDay.substring(0, 4) + "-" + stlVanDay.substring(4, 6) + "-" + stlVanDay.substring(6, 8);
				detail.put("title", "입금 ￦" + nf.format(Integer.parseInt(each.getString("stlVanAmount"))));
				detail.put("start", stlVanDay);
				detail.put("end", stlVanDay);
				detail.put("backgroundColor", "#4B77BE");
				result.add(detail);
			}
		}
		
		CPDAO settleDAO2 = new CPDAO();
		settleDAO2.setTable("VW_TRX_CAP");
		settleDAO2.setColumns("SUM(stlAmount) as stlAmount, stlDay");
		settleDAO2.setWhere("stlVanDay > LAST_DAY(NOW() - interval 1 month) AND stlVanDay <= LAST_DAY(NOW())");
		settleDAO2.setGroupBy("stlDay");
		List<SharedMap<String, Object>> resList2 = settleDAO2.search().getRows();
		for (SharedMap<String, Object> each : resList2) {
			if (each.getLong("stlAmount") > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlDay = each.getString("stlDay");
				stlDay = stlDay.substring(0, 4) + "-" + stlDay.substring(4, 6) + "-" + stlDay.substring(6, 8);
				detail.put("title", "정산 ￦" + nf.format(Integer.parseInt(each.getString("stlAmount"))));
				detail.put("start", stlDay);
				detail.put("end", stlDay);
				detail.put("backgroundColor", "#1BA39C");
				result.add(detail);
				for (SharedMap<String, Object> each2 : resList) {
					if (each2.getString("stlVanDay").equals(each.getString("stlDay"))) {
						SharedMap<String, Object> detail2 = new SharedMap<String, Object>();
						detail2.put("title", "차액 ￦" + nf.format(each2.getLong("stlVanAmount") - each.getLong("stlAmount")));
						detail2.put("start", stlDay);
						detail2.put("end", stlDay);
						detail2.put("backgroundColor", "#C8D046");
						result.add(detail2);
					}
				}
			}
		}
		
		request.setAttribute("SETTLEJSON", GsonUtil.toJson(result, false, ""));
		*/
	}
	
	private void initMember(HttpServletRequest request, CPSession cpSession, String whereStr) {
		CPDAO dao = new CPDAO();
		/*
		dao.setTable("VW_MCHT_TAX A LEFT JOIN VW_MCHT B ON A.mchtId = B.mchtId");
		dao.setColumns("remainLimit, usedAmt, taxLimit, lastDate, taxId, A.mchtId, A.name as taxName, taxStatus, B.name, nick, status, distId, agencyId, salesId");
		if(whereStr != "") {
			dao.setWhere(whereStr + " = '" + cpSession.getParentId() + "'");
		}
		dao.addWhere("idType", "주민번호", CPDAO.eq);
		dao.addWhere("taxStatus", "사용", CPDAO.eq);
		dao.addWhere("taxLimit", "0", CPDAO.ne);
		dao.setOrderBy("remainLimit ASC");
		request.setAttribute("DATATAXMAP", dao.search().getRows());
		dao.initRecord();
		*/
		
		dao.setTable("`PG_MCHT_MNG` `A` left join `PG_TRX_CAP` `B` on `A`.`mchtId` = `B`.`mchtId` and substr(`B`.`regDay`,1,4) = substr(curdate(),1,4) left join PG_MCHT C on A.mchtId = C.mchtId");
		dao.setColumns("if(`A`.`limitYear` = 0,0,`A`.`limitYear` - ifnull(sum(`B`.`amount`),0)) AS `remainLimit`,ifnull(sum(`B`.`amount`),0) AS `usedAmt`,ifnull(max(`B`.`regDate`),0) AS `lastDate`,`A`.`limitYear` AS `limitYear`,`A`.`mchtId` AS `mchtId`,`C`.`name` AS `name`,`A`.`regDay` AS `regDay`,`A`.`regDate` AS `regDate`");
		if(whereStr != "") {
			dao.addWhere("C."+whereStr + " = '" + cpSession.getParentId() + "'");
		}
		dao.addWhere("A.limitYear > 0");
		dao.setGroupBy("`A`.`mchtId`");
		dao.setOrderBy("remainLimit ASC");
		request.setAttribute("DATAYEARMAP", dao.search().getRows());
		dao.initRecord();
		TrxCapDAO capDAO = new TrxCapDAO();
		if(whereStr != "") {
			capDAO.setWhere(whereStr + " = '" + cpSession.getParentId() + "'");
		}
		capDAO.addWhere("regDay", CommonUtil.getCurrentDate("yyyyMMdd"), CPDAO.eq);
		capDAO.setLimit(15);
		
//		CPDAO danalDAO = new CPDAO();
//		danalDAO.setTable("VW_TRX_WH_FAIL");
//		danalDAO.setColumns("COUNT(*) as cnt");
//		cpSession.setDanalFailCnt(danalDAO.search().getRow(0).getLong("cnt"));
		
		request.setAttribute("DATACAPMAP", capDAO.search().getRows());
	}
	
	private void initMcht(HttpServletRequest request, CPSession cpSession) {
		CPDAO settleDAO = new CPDAO();
		settleDAO.setTable("VW_SETTLE");
		settleDAO.setColumns("stlAmt, stlDay, payStatus");
		settleDAO.setWhere(" mchtId = '" + cpSession.getParentId() + "'");

		List<SharedMap<String, Object>> resList = settleDAO.search().getRows();
		List<SharedMap<String, Object>> result = new ArrayList<SharedMap<String, Object>>();
		for (SharedMap<String, Object> each : resList) {
			if(each.getLong("stlAmt") > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlDay = each.getString("stlDay");
				stlDay = stlDay.substring(0, 4) + "-" + stlDay.substring(4, 6) + "-" + stlDay.substring(6, 8);
				detail.put("title", nf.format(Integer.parseInt(each.getString("stlAmt"))) + "￦");
				detail.put("start", stlDay);
				detail.put("end", stlDay);
				if (each.getString("payStatus").equals("지급완료")) {
					detail.put("backgroundColor", "#ACB5C3");
				} else {
					detail.put("backgroundColor", "#4C87B9");
				}
				
				result.add(detail);
			}
		}
		request.setAttribute("SETTLEJSON", GsonUtil.toJson(result, false, ""));
	}

	@RequestMapping(value = { "/dev" })
	@SessionExclude
	public ModelAndView dev(HttpServletRequest request) {
		UserDAO userDAO = new UserDAO();

		String accessGrade = "USER";
		String id = "ginaida@trustmate.net";

		RecordSet rset = userDAO.getById(id);
		SharedMap<String, Object> memberMap = rset.getRow(0);

		CPSession cpSession = new CPSession();
		SessionUtil.initSessionData(cpSession, memberMap);
		
		cpSession.setAccessDate(CommonUtil.getCurrentTimestamp());
		if (accessGrade.equals("USER")) {
			cpSession.setLastAccessDate(userDAO.getAccessById(rset.getString("id")));
		} else {
			cpSession.setLastAccessDate(CommonUtil.getCurrentTimestamp());
		}

		if (cpSession.getGrade().equals("본사")) {
			cpSession.setTargetURL("");
		} else if (cpSession.getGrade().equals("대행사")) {
			cpSession.setTargetURL("");
		} else if (cpSession.getGrade().equals("에이전시")) {
			cpSession.setTargetURL("");
		} else { // 가맹점
			cpSession.setParentName(memberMap.getString("name"));
			cpSession.setTargetURL("");
		}

		// ACCESS 기록 추가
		if (accessGrade.equals("USER")) {
			// userDAO.insertUserAcess(rset.getString("id"),request.getRemoteAddr(),"DEV
			// SESSION");
		}

		// Session 생성
		SessionUtil.create(request, cpSession);
		SessionUtil.setAttribute(request, "endDate", CommonUtil.getCurrentDate("yyyyMMdd"));

		return new ModelAndView("/main");

	}
}
