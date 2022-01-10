package com.pgmate.pay.bean;
/**
 * @author Administrator
 *
 */
public class Account extends Public{

	public String walletId	= null;				//파트너사에 등록된 월렛ID
	public String userId	= null;				//파트너사에 등록된 사용자ID
	public String bankCd	= null;				//은행코드
	public String account	= null;				//계좌번호 - 제외
	public String beneficiary = null;	
	public String accntId	 = null;
	public boolean accountVerify= false;		//계좌 인증 관련 FLG
	
	
	
	public Account() {
		// TODO Auto-generated constructor stub
	}
}
