package com.pgmate.app.model.ajax;

import java.util.List;

import com.pgmate.lib.util.map.SharedMap;

public class TmnList{
	
	public String mchtId = "";
	public String taxId = "";
	public String activeDate = "";
	public String apiMaxInstall = "";
	public String webPay = "";
	public String appDirect = "";
	public String semiAuth = "";
	public String refundType = "";
	public String van = "";
	public String vanIdx = "";
	public String ccType = "";
	public String payLimit = "";
	public String limitAmount = "";
	public String limitStartTime = "";
	public String limitEndTime = "";

	public List<SharedMap<String, Object>> list = null;
	
	public TmnList() {
		// TODO Auto-generated constructor stub
	}

	
}
