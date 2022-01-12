package com.pgmate.pay.bean;

import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class Base {

	public String trxId		= "";	//KJM : 거래번호
	public String trxType	= "";	//거래유형
	public String tmnId		= "";	//단말기아이디
	public String trackId	= "";	//일련번호
	public long amount		= 0;	//금액
	public String udf1		= "";	//소속 구분 (본사&대행사 , 그 외)
	public String udf2		= "";	//로그인중인 계정 아이디
	public SharedMap<String,String> metadata = null;
	
	
	public Base() {
		// TODO Auto-generated constructor stub
	}

}
