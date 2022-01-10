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
public class AllatUtil {
	private static Logger logger 	= LoggerFactory.getLogger( com.pgmate.app.util.AllatUtil.class ); 
	
	public AllatUtil() {
	}
	
	public String getParam(String vanId,String trackId,String amount){
		long epochTime 	= System.currentTimeMillis();
		SharedMap<String,Object> vanMap = new VanDAO().getByVanId(vanId).getRowFirst();
		amount = String.valueOf(Math.abs(Long.parseLong(amount)));
		String hash = getHash(vanId+vanMap.getString("cryptokey")+trackId+amount+epochTime);	
		String value = "shop_id="+vanId+"&order_number="+trackId+"&hash_value="+hash+"&current_time="+epochTime;
		return value;
	}
	
	private String getHash(String text){
		StringBuffer buf = new StringBuffer();
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("euc-kr"));
			byte[] digest = md.digest();
			
			for( int i = 0; i < digest.length; i++ ){
				if((0xff & digest[i]) < 0x10)
					buf.append("0" + Integer.toHexString(0xff & digest[i]));
				else
					buf.append(Integer.toHexString(0xff & digest[i]));
			}

			
		}catch(Exception e){
		}
		return buf.toString();
	}

}
