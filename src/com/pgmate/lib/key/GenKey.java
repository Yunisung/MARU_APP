package com.pgmate.lib.key;

import java.util.Random;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class GenKey {

	private static int KEY_COUNT = 18;
	private static int KEY_RANGE = 104;
	
	//KJM : key를 이용한 일련번호
	public static String genKeys(Enum<CPKEY> key,String value){
		
		//KJM : key가 빈값일 경우
		if(key.toString().equals("")){
			//KJM : 랜덤 일련번호 생성
			return makeSerialNumber(value, randomKeyAccess());
		}else{
			//KJM : key가 존재 할 때 key + 랜덤 일련번호
			return key.toString()+makeSerialNumber(value, randomKeyAccess());
		}
	}
	
	public static String genInterMsgKeys(String value){
		return CPKEY.INTER_MSG.toString()+CommonUtil.getCurrentDate("yyyyMMddHHmm")+"_"+makeSerialNumber(value, randomKeyAccess());
	}
	
    // KBR : 랜덤 키 생성
    private static Integer[] randomKeyAccess(){
    	
    	Integer[] cnt = new Integer[KEY_COUNT];
    	
    	Random random = new Random();
    	
    	//KJM : 0~103까지의 랜덤한 숫자 배열(18사이즈)
    	for(int i =0 ; i < KEY_COUNT ; ++i) {
    		cnt[i] = random.nextInt(KEY_RANGE);
    	}
    	return cnt;
	}
    
    // KBR : 일련번호 생성 메소드
    private static synchronized String makeSerialNumber (String value, Integer[] keys){
		 
    	StringBuilder sb = new StringBuilder();
    	
    	//KJM : value + 나노세컨즈 ???
    	value +=System.nanoTime();
    	
    	String serial = "";
    	
    	try{
    		//KJM : 알고리즘별로 암호화 된 value
    		serial = SecurityHash(value,"MD2") +  SecurityHash(value,"MD5") +SecurityHash(value,"SHA1");
    	}catch(Exception e){}
    	
    	//KJM : 18자리의 일련번호 생성
    	//KJM : 000-00000-00-00000
    	for(int i = 0 ; i < keys.length ;++i) {
    		//KJM : serial문자열의 keys[i]번째 문자
    		sb.append( serial.charAt(keys[i]));
    		if(i == 3 || i == 9 || i == 12 )  sb.append("-");
    	}
    	
    	return sb.toString();
	  }
    
    private static String SecurityHash(String stringInput, String algorithmName )throws Exception {
	  
    	String hexMessageEncode = "";
    	
    	//KJM : 문자들이 아스키코드로 배열에 들어감
    	byte[] buffer = stringInput.getBytes();
    	
    	//KJM : 암호화 인스턴스 생성
    	java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance(algorithmName);
    	//KJM : 해시값(다이제스트) 갱신
    	messageDigest.update(buffer);
    	
    	//KJM : 바이트배열로 해시 반환
    	byte[] messageDigestBytes = messageDigest.digest();
    	
    	for (int index=0; index < messageDigestBytes.length ; index ++){
    		//KJM : 해시값 int 형으로 반환
    		int countEncode = messageDigestBytes[index] & 0xff;
    		//KJM : int형의 해시값을 16진수로 변환한 값의 길이가 1일 때 hexMessageEncode에 "0" 추가
    		if (Integer.toHexString(countEncode).length() == 1) hexMessageEncode = hexMessageEncode + "0";
    		//KJM : int형의 해시값을 16진수로 변환한 값을 hexMassageEncode에 추가
    		hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
    	}
    	
    	return hexMessageEncode;
    }
    
    

    
	public static void main(String[] args) {
		try{
		long time = System.currentTimeMillis();
		System.out.println(GenKey.genKeys(CPKEY.TRX_ID, "MPUNION"));
		System.out.println(GenKey.genKeys(CPKEY.SECRET_KEY, "kollshop"));
		System.out.println(GenKey.genKeys(CPKEY.PUBLIC_KEY, "kollshop"));
		System.out.println(GenKey.genKeys(CPKEY.SECRET_KEY, "pertechhk"));
		System.out.println(GenKey.genKeys(CPKEY.PUBLIC_KEY, "pertechhk"));
		System.out.println(GenKey.genKeys(CPKEY.SECRET_KEY, "gsshkglea"));
		System.out.println(GenKey.genKeys(CPKEY.PUBLIC_KEY, "gsshkglea"));
		
		}catch(Exception e){e.printStackTrace();}


	}

}
