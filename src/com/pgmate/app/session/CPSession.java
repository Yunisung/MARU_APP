package com.pgmate.app.session;

import java.sql.Timestamp;
import java.util.List;

import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */

public class CPSession implements java.io.Serializable{

	
	private String userId		= "";
	private String role			= "";	//권한(일반,마스터,관리자)
	private String grade		= ""; 	//현재소속(본사,대행사,에이전시,지사)
	private String name 		= "";
	private String parentId		= "";
	private String parentName	= "";
	private String pwYn			= "";
	private Timestamp accessDate= null;		//접속 시간 
	private Timestamp lastAccessDate= null;		//접속 시간	
	private String targetURL	= "";
	private String regDay		= CommonUtil.getCurrentDate("yyyyMMdd");
	private String lastUrl 		= "";
	private String distId 		= "";
	private long danalFailCnt	= 0;
	private String aggregator 	= "N";
	private String webPay		= "";
	private String showOthTrns 	= "N";
	private String eformStatus 	= "N";
	private String loanSettleStatus 	= "N";

	

	private List<SharedMap<String,Object>> childList = null;
	private List<String> salesMonthList	= null;
	private List<SharedMap<String,Object>> newNoticeList = null;
	private List<SharedMap<String,Object>> vanList = null;
	

	public List<SharedMap<String,Object>> getVanList() {
		return vanList;
	}

	public void setVanList(List<SharedMap<String,Object>> vanList) {
		this.vanList = vanList;
	}

	public CPSession() {
		
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the pwYn
	 */
	public String getPwYn() {
		return pwYn;
	}

	/**
	 * @param pwYn the pwYn to set
	 */
	public void setPwYn(String pwYn) {
		this.pwYn = pwYn;
	}

	/**
	 * @return the accessDate
	 */
	public Timestamp getAccessDate() {
		return accessDate;
	}

	/**
	 * @param accessDate the accessDate to set
	 */
	public void setAccessDate(Timestamp accessDate) {
		this.accessDate = accessDate;
	}

	/**
	 * @return the lastAccessDate
	 */
	public Timestamp getLastAccessDate() {
		return lastAccessDate;
	}

	/**
	 * @param lastAccessDate the lastAccessDate to set
	 */
	public void setLastAccessDate(Timestamp lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	/**
	 * @return the targetURL
	 */
	public String getTargetURL() {
		return targetURL;
	}

	/**
	 * @param targetURL the targetURL to set
	 */
	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	/**
	 * @return the childList
	 */
	public List<SharedMap<String, Object>> getChildList() {
		return childList;
	}

	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<SharedMap<String, Object>> childList) {
		this.childList = childList;
	}
	
	public String getRegDay() {
		return regDay;
	}

	public String getLastUrl() {
		return lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}

	/**
	 * @return the salesMonthList
	 */
	public List<String> getSalesMonthList() {
		return salesMonthList;
	}

	/**
	 * @param salesMonthList the salesMonthList to set
	 */
	public void setSalesMonthList(List<String> salesMonthList) {
		this.salesMonthList = salesMonthList;
	}

	/**
	 * @param regDay the regDay to set
	 */
	public void setRegDay(String regDay) {
		this.regDay = regDay;
	}
	
	public List<SharedMap<String, Object>> getNewNoticeList() {
		return newNoticeList;
	}

	public void setNewNoticeList(List<SharedMap<String, Object>> newNoticeList) {
		this.newNoticeList = newNoticeList;
	}
	
	public String getDistId() {
		return distId;
	}

	public void setDistId(String distId) {
		this.distId = distId;
	}
	
	public long getDanalFailCnt() {
		return danalFailCnt;
	}

	public void setDanalFailCnt(long danalFailCnt) {
		this.danalFailCnt = danalFailCnt;
	}
	
	public String getAggregator() {
		return aggregator;
	}

	public void setAggregator(String aggregator) {
		this.aggregator = aggregator;
	}

	/**
	 * @return the webPay
	 */
	public String getWebPay() {
		return webPay;
	}

	/**
	 * @param webPay the webPay to set
	 */
	public void setWebPay(String webPay) {
		this.webPay = webPay;
	}

	public String getShowOthTrns() {
		return showOthTrns;
	}

	public void setShowOthTrns(String showOthTrns) {
		this.showOthTrns = showOthTrns;
	}
	
	/*
	 * @return the eformStatus
	 */
	public String getEformStatus() {
		return eformStatus;
	}

	public void setEformStatus(String eformStatus) {
		this.eformStatus = eformStatus;
	}

	public String getLoanSettleStatus() {
		return loanSettleStatus;
	}

	public void setLoanSettleStatus(String loanSettleStatus) {
		this.loanSettleStatus = loanSettleStatus;
	}
	
}
