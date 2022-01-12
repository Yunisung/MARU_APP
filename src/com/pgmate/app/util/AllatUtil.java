package com.pgmate.app.util;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.dao.VanDAO;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//KJM : 올앳관련 기능
public class AllatUtil {
	private static Logger logger 	= LoggerFactory.getLogger( com.pgmate.app.util.AllatUtil.class ); 
	
	public AllatUtil() {
	}
	
	//KJM : van아이디, 주문번호, 금액 정보 넘겨줌
	public String getParam(String vanId,String trackId,String amount){
		//KJM : 현재시간
		long epochTime 	= System.currentTimeMillis();
		//KJM : van아이디와 일치하는 van 정보 가져와 넣음
		SharedMap<String,Object> vanMap = new VanDAO().getByVanId(vanId).getRowFirst();
		//KJM : 금액은 절대값으로 변환 후 문자열로 반환시킨다 (valueOf => null이면 문자열 "null" 반환)
		amount = String.valueOf(Math.abs(Long.parseLong(amount)));
		//KJM : vanID, cryptokey, 주문번호, 금액, 현재시간을 이용해 암호화 해시 값 구함
		String hash = getHash(vanId+vanMap.getString("cryptokey")+trackId+amount+epochTime);
		String value = "shop_id="+vanId+"&order_number="+trackId+"&hash_value="+hash+"&current_time="+epochTime;
		return value;
	}
	
	//KJM : 암호화 해시 값 구하기
	private String getHash(String text){
		StringBuffer buf = new StringBuffer();
		try{
			//KJM : MD5 md 생성
			//KJM : MD5 알고리즘을 이용함
			MessageDigest md = MessageDigest.getInstance("MD5");
			//KJM : text를 euc-kr로 인코딩한 후 이 바이트 데이터를 사용해 다이제스트 갱신
			md.update(text.getBytes("euc-kr"));
			//KJM : 바이트배열로 해시 반환
			byte[] digest = md.digest();
			
			for( int i = 0; i < digest.length; i++ ){
				//KJM : 0xff&byteNum => 양수인 byteNum / 0x10(16진수) => 10(10진수) 
				//KJM : digest[i]의 값이 10보다 작을 때
				if((0xff & digest[i]) < 0x10)
					//KJM : 앞자리에 0을 더한 문자열로 넣어짐 ( 01, 02,03,...)
					buf.append("0" + Integer.toHexString(0xff & digest[i]));
				else
					buf.append(Integer.toHexString(0xff & digest[i]));
			}
		}catch(Exception e){
		}
		return buf.toString();
	}

}
