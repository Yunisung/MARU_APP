package com.pgmate.pay.bean;

import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class Public {

	public String trxId		= null;		//공통 거래 번호
	public String trackId	= null;		//공통 주문 번호
	public String udf		= null;		//사용자 정의 필드
	public String summary	= null;		//이외의 코멘트
	public String create	= null;		//응답 시간 
	public SharedMap<String,String> metadata = null;
	
	
	public Public() {
		// TODO Auto-generated constructor stub
	}

}
