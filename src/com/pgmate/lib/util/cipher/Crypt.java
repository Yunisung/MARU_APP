package com.pgmate.lib.util.cipher;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author Administrator
 * CBC(Cipher Block Chaining), ECB(Electronic Code Block) 지원
 * OFB(Output FeedBack), CTR (CounTeR)  미지원
 */
public class Crypt {


	/*
	 * 암호문으로 KEY 생성 
	 */
	public static Key generateKey(String algorithm,byte[] keyData) throws Exception {
		//KJM : DES 알고리즘 사용 시
		if("DES".equals(algorithm)){
			//KJM : DESKeySpec => 8바이트를 DES 키의 키 데이터로 사용해, DESKeySpec 객체 생성
			KeySpec keySpec = new DESKeySpec(keyData);
			//KJM : 지정된 비밀키 알고리즘의 SecretKeyFactory 객체 생성
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			//KJM : 지정된 키 사양 (키 데이터)으로부터 SecretKey 객체 생성
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		//KJM : DESede, TripleDES 알고리즘 사용 시
		}else if("DESede".equals(algorithm) || "TripleDES".equals(algorithm)) {
			KeySpec keySpec = new DESedeKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		//KJM : 그 외의 경우 해당 정보로 secretkeyspec 생성자 생성
		}else{
			return new SecretKeySpec(keyData, algorithm);
		}
	}


	
	
	public static String encryptBase64(Key secretKeySpec,String transforamtion,byte[] iv,byte[] data) throws Exception {
		return Base64.encodeToString(encrypt(secretKeySpec,transforamtion, iv, data)); 
	}
	
	//KJM : 운용모드 구분 후 암호화 수행
	public static byte[] encrypt(Key secretKeySpec,String transforamtion,byte[] iv,byte[] data) throws Exception {
		
		/*
		 * KJM
		 * Cipher : Java에서 암호화, 복호화 하기위해 사용하는 클래스
		 * CBC : 암호문 블록에 평문 블록과 초기화 벡터를 XOR한 후 암호화 수행
		 * CFB : 초기화 벡터를 암호화한 후, 평문과 XOR를 하여 암호문 생성
		 */
		Cipher cipher = Cipher.getInstance(transforamtion) ;
		/*
		 * CBC와 CFB는 초기화 벡터를 사용하기 때문에 초기화 벡터(IV)를 사용하는 클래스 사용해줌
		 */
		if(transforamtion.indexOf("CBC") > -1 || transforamtion.indexOf("CFB") > -1){
			IvParameterSpec ivSpec = new IvParameterSpec(iv) ;
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec) ;
		}else if(transforamtion.indexOf("ECB") > -1){
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec) ;
		}else{
			
		}
		return cipher.doFinal(data) ;
	}
	
	
	
	
	public static String decryptBase64(Key secretKeySpec,String transforamtion,byte[] iv,String data) throws Exception {
		return new String(decrypt(secretKeySpec,transforamtion, iv,Base64.decode(data))); 
	}
	
	
	public static byte[] decrypt(Key secretKeySpec,String transforamtion,byte[] iv,byte[] data) throws Exception {
	
		Cipher cipher = Cipher.getInstance(transforamtion) ;
		if(transforamtion.indexOf("CBC") > -1 || transforamtion.indexOf("CFB") > -1){
			IvParameterSpec ivSpec = new IvParameterSpec(iv) ;
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec) ;
		}else if(transforamtion.indexOf("ECB") > -1){
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec) ;
		}else{
			
		}
		return cipher.doFinal(data) ;
	}
	
	
	public static String getHashToBase64(String plain,String algorithm) throws Exception{
		
		byte[] hashBytes = getHash(plain, algorithm);
		return Base64.encodeToString(hashBytes);
	}
	
	
	public static String getHashToHex(String plain,String algorithm) throws Exception{
		
		byte[] hashBytes = getHash(plain, algorithm);
		StringBuilder hexString = new StringBuilder();
    	for (int i=0;i<hashBytes.length;i++) {
    	  hexString.append(Integer.toHexString(0xFF & hashBytes[i]));
    	}
    	return hexString.toString();
	}
	
	/** 
	 * algorithm SHA-1, MD5, SHA-256
	 * @param plain
	 * @param algorithm
	 * @return
	 */
	public static byte[] getHash(String plain,String algorithm) throws Exception{
		MessageDigest md = MessageDigest.getInstance(algorithm);
		return md.digest();
	}
	

}
