package com.pgmate.test;

import com.pgmate.app.util.KSignUtil;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args){
//		System.out.println(CommonUtil.diffOfDay("20170401", "20170403", "yyyyMMdd"));
//		String tk = GenKey.genKeys(CPKEY.CASH_TRANSFER, "winnerscom");
//		System.out.println(tk);

		ksignTest();
	}

	public static void ksignTest() {
		String key = KSignUtil.getInstance().Encrypt("ct_8124-bc2e24-d90-e2c6a");
		KSignUtil.getInstance().Decrypt(key);
	}

}
