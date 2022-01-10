package com.pgmate.app.ctl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.pgmate.app.dao.TotCapAgencyDAO;
import com.pgmate.app.dao.TotCapDAO;
import com.pgmate.app.dao.TotCapDayDAO;
import com.pgmate.app.dao.TotCapDistDAO;
import com.pgmate.app.dao.TotCapMonthlyDAO;
import com.pgmate.app.dao.TotCapSalesDAO;
import com.pgmate.app.dao.TotCapSubDAO;
import com.pgmate.app.dao.TrxDailyDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class SalesController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.SalesController.class );
	
	@RequestMapping(value = "/sales/issuer/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsIssuerList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		
		TotCapDAO totCapDAO = new TotCapDAO();
		RecordSet rset = totCapDAO.salesIssuerList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDAO).setView(request,"/sales/issuer/list","");
	}
	
	@RequestMapping(value = "/sales/daily/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDailyList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapDAO totCapDAO = new TotCapDAO();
		RecordSet rset = totCapDAO.salesDailyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDAO).setView(request,"/sales/daily/list","");
	}
	
	@RequestMapping(value = "/sales/monthly/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsMonthlyList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapMonthlyDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapMonthlyDAO totCapMonthlyDAO = new TotCapMonthlyDAO();
		RecordSet rset = totCapMonthlyDAO.salesMonthlyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapMonthlyDAO).setView(request,"/sales/monthly/list","");
	}
	
	@RequestMapping(value = "/sales/mcht/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsMchtList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapDayDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapDayDAO totCapDayDAO = new TotCapDayDAO();
		RecordSet rset = totCapDayDAO.salesMchtDailyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDayDAO).setView(request,"/sales/mcht/list","");
	}
	
	@RequestMapping(value = "/sales/mchtMonthly/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsMchtMonthlyList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapMonthlyDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapMonthlyDAO totCapMonthlyDAO = new TotCapMonthlyDAO();
		RecordSet rset = totCapMonthlyDAO.salesMchtMonthlyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapMonthlyDAO).setView(request,"/sales/mchtMonthly/list","");
	}
	
	@RequestMapping(value = "/sales/agency/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsAgencyList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapAgencyDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapAgencyDAO totAgencyCapDAO = new TotCapAgencyDAO();
		RecordSet rset = totAgencyCapDAO.salesAgencyMonthlyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totAgencyCapDAO).setView(request,"/sales/agency/list","");
	}
	
	@RequestMapping(value = "/sales/dist/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDistList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapDistDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapDistDAO totCapDistDAO = new TotCapDistDAO();
		RecordSet rset = totCapDistDAO.salesDistMonthlyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDistDAO).setView(request,"/sales/dist/list","");
	}
	
	@RequestMapping(value = "/sales/sales/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsSalesList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapSalesDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapSalesDAO totCapSalesDAO = new TotCapSalesDAO();
		RecordSet rset = totCapSalesDAO.salesSalesMonthlyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapSalesDAO).setView(request,"/sales/sales/list","");
	}
	
	@RequestMapping(value = "/sales/aggregator/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsSubList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapSubDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapSubDAO totCapDAO = new TotCapSubDAO();
		RecordSet rset = totCapDAO.salesTmnDailyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDAO).setView(request,"/sales/aggregator/list","");
	}
	
	@RequestMapping(value = "/sales/loan/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsLoanList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		request.setAttribute("SUMMAP", new TotCapDAO().salesSum(cpRequest.data).getRow(0));
		
		TotCapDAO totCapDAO = new TotCapDAO();
		RecordSet rset = totCapDAO.salesMchtDailyList(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,totCapDAO).setView(request,"/sales/loan/list","");
	}
	
	@RequestMapping(value = "/sales/trand/list", method = RequestMethod.POST)
	public @ResponseBody List<SharedMap<String,Object>> saelsTrandDay(HttpServletRequest request) {
		TotCapDAO totCapDAO = new TotCapDAO();
		String curDay = CommonUtil.getCurrentDate("yyyyMMdd");
		String endDay = CommonUtil.getOpDate(Calendar.DATE, -1 ,curDay);
		
		String startDay = CommonUtil.getOpDate(Calendar.DATE, -31 ,endDay);
		if(Integer.parseInt(startDay) < 20180131) startDay = "20180131";
		return totCapDAO.salesDailyTrandList(startDay, endDay).getRows();
	}
	
	@RequestMapping(value = "/sales/trand/mcht", method = RequestMethod.POST)
	public @ResponseBody List<SharedMap<String,Object>> saelsTrandMcht(HttpServletRequest request) {
		TotCapDAO totCapDAO = new TotCapDAO();
		String curDay = CommonUtil.getCurrentDate("yyyyMMdd");
		String endDay = CommonUtil.getOpDate(Calendar.DATE, -1 ,curDay);
		
		String startDay = CommonUtil.getOpDate(Calendar.DATE, -31 ,endDay);
		if(Integer.parseInt(startDay) < 20171220) {
			startDay = "20171219";
		}
		
		return totCapDAO.salesMchtRank(startDay, endDay).getRows();
	}
	
	@RequestMapping(value = "/sales/dailys/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDailyS(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		TrxDailyDAO trxDailyDAO = new TrxDailyDAO();
		RecordSet rset = trxDailyDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxDailyDAO).setView(request,"/sales/dailys/list","");
	}
	
	@RequestMapping(value = "/sales/dailys/dtl/{days}", method = RequestMethod.GET)
    public ModelAndView viewTab(HttpServletRequest request, @PathVariable String days) {
        return new ModelAndView("/sales/dailys/dtl/form", "DATAMAP", new TrxDailyDAO().getDailyCap(days));
    }   
	
	//정산일기준 일마감 통계  dailysByStlDay
	@RequestMapping(value="/sales/dailysByStlDay/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView dailysByStlDay(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		TrxDailyDAO trxDailyDAO = new TrxDailyDAO();
		RecordSet rset = trxDailyDAO.dailysByStlDay(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, trxDailyDAO).setView(request, "/sales/dailysByStlDay/list", "");
	}
	
	//정산일기준 일마감 통계  dailysByStlDay
	@RequestMapping(value="/sales/dailysByStlDay/dtl/form/{stlDay}", method=RequestMethod.GET)
	public ModelAndView dailysByStlDayDTL(HttpServletRequest request, @PathVariable String stlDay) {
		return new ModelAndView("/sales/dailysByStlDay/dtl/form", "DATAMAP", new TrxDailyDAO().dailysByStlDayDTL(stlDay));
	}
	
	//승인일기준 일마감 통계
	@RequestMapping(value = "/sales/dailysByCapDay/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saelsDailysByCapDay(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		TrxDailyDAO trxDailyDAO = new TrxDailyDAO();
		RecordSet rset = trxDailyDAO.listByCapDay(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset,trxDailyDAO).setView(request,"/sales/dailysByCapDay/list","");
	}
	//승인일기준 일마감 통계 가맹점 기준
	@RequestMapping(value = "/sales/dailysByCapDay/dtl/{capDay}", method = RequestMethod.GET)
    public ModelAndView viewTabByCapDay(HttpServletRequest request, @PathVariable String capDay) {
        return new ModelAndView("/sales/dailysByCapDay/dtl/form", "DATAMAP", new TrxDailyDAO().getDailyCapByCapDay(capDay));
    }   
	
	@RequestMapping(value = "/sales/settle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView salesSettle(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		 return new ModelAndView("/sales/settle/list", "DATAMAP", new TrxDailyDAO().getSettle(cpRequest.getKeyValue("startStlDay"), cpRequest.getKeyValue("endStlDay")));
	}
	
	@RequestMapping(value = "/sales/collect/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView salesCollect(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		return new ModelAndView("/sales/collect/list", "DATAMAP", new TrxDailyDAO().getCollect(cpRequest.getKeyValue("startCollectDay"), cpRequest.getKeyValue("endCollectDay")));
	}
	
	@RequestMapping(value = "/sales/collect/mcht/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView salesCollectMcht(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
		RecordSet rset = new TrxDailyDAO().getCollectWithMcht(cpRequest.data);
		return new ModelAndView("/sales/collect/mcht/list", "DATAMAP", rset.getRows());
	}
	
	@RequestMapping(value = "/sales/settle/calendar", method = RequestMethod.GET)
	public ModelAndView settleCalander(HttpServletRequest request){
		CPSession cpSession = SessionUtil.get(request);
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
		dao.setDebug(true);
		dao.setTable("VW_TRX_CAP_CALENDAR");
		dao.setColumns(sb.toString());
		if(whereStr != "") {
			dao.setWhere("regDay > DATE_FORMAT(LAST_DAY(date_add(NOW(), interval -1 month)), '%Y%m%d') and regDay <= DATE_FORMAT(LAST_DAY(NOW()), '%Y%m%d') AND " + whereStr + " = '" + cpSession.getParentId() + "'");
		} else {
			dao.setWhere("regDay > DATE_FORMAT(LAST_DAY(date_add(NOW(), interval -1 month)), '%Y%m%d') and regDay <= DATE_FORMAT(LAST_DAY(NOW()), '%Y%m%d')");
		}
		
		request.setAttribute("PAYMAP", dao.search().getRowFirst());
//		if (cpSession.getGrade().equals("본사")){
//			initAdmin(request, cpSession);
			return new ModelAndView("/sales/settle/calendar");
//		}
	}
	
	
	@RequestMapping(value = "/sales/settle/getCalendar", method = RequestMethod.GET)
	public @ResponseBody List<SharedMap<String, Object>> getCalendar (HttpServletRequest request) {
		
		List<SharedMap<String, Object>> result = new ArrayList<SharedMap<String, Object>>();
		CPDAO settleDAO = new CPDAO();
		StringBuffer query = new StringBuffer();
		query.append("select A.*,B.*, A.stlVanAmount - B.stlAmount AS difference from ");
		query.append("(select SUM(stlVanAmount) as stlVanAmount, stlVanDay from VW_TRX_CAP_CALENDAR ");
		query.append("where regDay > DATE_FORMAT(LAST_DAY(NOW() - interval 2 month),'%Y%m%d') and stlVanDay > DATE_FORMAT(LAST_DAY(NOW() - interval 1 month),'%Y%m%d') AND stlVanDay <= DATE_FORMAT(LAST_DAY(NOW()),'%Y%m%d') group by stlVanDay) A ");
		query.append("LEFT JOIN (select SUM(stlAmount) as stlAmount, stlDay from VW_TRX_CAP_CALENDAR ");
		query.append("where regDay > DATE_FORMAT(LAST_DAY(NOW() - interval 2 month),'%Y%m%d') and stlDay > DATE_FORMAT(LAST_DAY(NOW() - interval 1 month),'%Y%m%d') AND stlDay <= DATE_FORMAT(LAST_DAY(NOW()),'%Y%m%d') ");
		query.append("group by stlDay) B on A.stlVanDay = B.stlDay ");
		
		
		List<SharedMap<String, Object>> resList = settleDAO.query(query.toString()).getRows();
		settleDAO.initRecord();
		for (SharedMap<String, Object> each : resList) {
			if (each.getLong("stlVanAmount") > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlVanDay = each.getString("stlVanDay");
				stlVanDay = stlVanDay.substring(0, 4) + "-" + stlVanDay.substring(4, 6) + "-" + stlVanDay.substring(6, 8);
				detail.put("title", "입금 ￦" + nf.format(Long.parseLong(each.getString("stlVanAmount"))));
				detail.put("start", stlVanDay);
				detail.put("end", stlVanDay);
				detail.put("backgroundColor", "#4B77BE");
				result.add(detail);
			}
			if (each.getLong("stlAmount") > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlDay = each.getString("stlDay");
				stlDay = stlDay.substring(0, 4) + "-" + stlDay.substring(4, 6) + "-" + stlDay.substring(6, 8);
				detail.put("title", "정산 ￦" + nf.format(Long.parseLong(each.getString("stlAmount"))));
				detail.put("start", stlDay);
				detail.put("end", stlDay);
				detail.put("backgroundColor", "#1BA39C");
				result.add(detail);
			}
			if (Math.abs(each.getLong("difference")) > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlDay = each.getString("stlDay");
				stlDay = stlDay.substring(0, 4) + "-" + stlDay.substring(4, 6) + "-" + stlDay.substring(6, 8);
				detail.put("title", "차액 ￦" + nf.format(Long.parseLong(each.getString("difference"))));
				detail.put("start", stlDay);
				detail.put("end", stlDay);
				detail.put("backgroundColor", "#C8D046");
				result.add(detail);
			}
			
		}
		
		return result;
	}

	
	@RequestMapping(value = "/sales/settle/getSettleData", method = RequestMethod.GET)
	public @ResponseBody SharedMap<String, Object> getSettleData (HttpServletRequest request ) {
		
		SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
		
		CPSession cpSession = SessionUtil.get(request);
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
		dao.setDebug(true);
		dao.setTable("VW_TRX_CAP_CALENDAR");
		dao.setColumns(sb.toString());
		if(whereStr != "") {
			dao.setWhere("regDay > DATE_FORMAT(LAST_DAY(date_add(NOW(), interval -1 month)), '%Y%m%d') and regDay <= DATE_FORMAT(LAST_DAY(NOW()), '%Y%m%d') AND " + whereStr + " = '" + cpSession.getParentId() + "'");
		} else {
			dao.setWhere("regDay > DATE_FORMAT(LAST_DAY(date_add(NOW(), interval -1 month)), '%Y%m%d') and regDay <= DATE_FORMAT(LAST_DAY(NOW()), '%Y%m%d')");
		}
		
		resultMap.put("PAYMAP", dao.search().getRowFirst().toJson());
		
		return resultMap;
	}
	
	private void initAdmin(HttpServletRequest request, CPSession cpSession) {
		
		List<SharedMap<String, Object>> result = new ArrayList<SharedMap<String, Object>>();
		CPDAO settleDAO = new CPDAO();
		StringBuffer query = new StringBuffer();
		query.append("select A.*,B.*, A.stlVanAmount - B.stlAmount AS difference from ");
		query.append("(select SUM(stlVanAmount) as stlVanAmount, stlVanDay from VW_TRX_CAP_CALENDAR ");
		query.append("where regDay > DATE_FORMAT(LAST_DAY(NOW() - interval 2 month),'%Y%m%d') and stlVanDay > DATE_FORMAT(LAST_DAY(NOW() - interval 1 month),'%Y%m%d') AND stlVanDay <= DATE_FORMAT(LAST_DAY(NOW()),'%Y%m%d') group by stlVanDay) A ");
		query.append("LEFT JOIN (select SUM(stlAmount) as stlAmount, stlDay from VW_TRX_CAP_CALENDAR ");
		query.append("where regDay > DATE_FORMAT(LAST_DAY(NOW() - interval 2 month),'%Y%m%d') and stlVanDay > DATE_FORMAT(LAST_DAY(NOW() - interval 1 month),'%Y%m%d') AND stlVanDay <= DATE_FORMAT(LAST_DAY(NOW()),'%Y%m%d') ");
		query.append("group by stlDay) B on A.stlVanDay = B.stlDay ");
		
		
		List<SharedMap<String, Object>> resList = settleDAO.query(query.toString()).getRows();
		settleDAO.initRecord();
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
			}
			if (Math.abs(each.getLong("difference")) > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				SharedMap<String, Object> detail = new SharedMap<String, Object>();
				String stlDay = each.getString("stlDay");
				stlDay = stlDay.substring(0, 4) + "-" + stlDay.substring(4, 6) + "-" + stlDay.substring(6, 8);
				detail.put("title", "차액 ￦" + nf.format(Integer.parseInt(each.getString("difference"))));
				detail.put("start", stlDay);
				detail.put("end", stlDay);
				detail.put("backgroundColor", "#C8D046");
				result.add(detail);
			}
			
		}
		request.setAttribute("SETTLEJSON", GsonUtil.toJson(result, false, ""));
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
}
