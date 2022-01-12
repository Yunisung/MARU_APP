package com.pgmate.pay.bean;

/**
 * @author Administrator
 *
 */
//KJM : 받는 데이터 클래스 모음
public class Request {
	public Pay pay			= null;	//KJM : 주문자 정보
	public Refund refund 	= null;	//취소 정보
	public Account accnt 	= null;	//계좌 정보?
	public Request() {
	}

}
