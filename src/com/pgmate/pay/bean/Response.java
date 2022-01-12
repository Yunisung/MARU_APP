package com.pgmate.pay.bean;

/**
 * @author Administrator
 *
 */
//KJM : 보내주는 데이터 클래스 모음
public class Response {
	public Result result	= null;	//KJM : 반환 메시지
	public Pay  pay 		= null;	//주문자 정보
	public Refund refund	= null;	//취소 정보
	public Settle settle	= null;	//?
	
	public Response() {
	}

}
