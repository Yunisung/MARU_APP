package com.pgmate.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.map.SharedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirmUtil {
  private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.util.FirmUtil.class);

  private SharedMap<String, Object> resMap = new SharedMap<String, Object>();
  private static String host 	= "10.100.200.10"; //"203.245.13.63";
	private static int port 	= 10006;
	private static int newPort 	= 10026; //KSNET 자금이체 대행포트
	private static int timeout  = 40000;

  public FirmUtil() {
  }
  
  
  public FirmBean execute(String mAccnt,String bankCd,String action,String ip,String userId){
	  FirmBean firmBean = new FirmBean();
	  firmBean.mAccnt 	= mAccnt;
	  firmBean.msgType 	= action;
	  firmBean.bankCd 	= bankCd;
	  firmBean.userIp	= ip;
	  firmBean.userId 	= userId;
	  logger.info("요청:{}",action);
	  firmBean = comm(firmBean);
	  logger.info("응답:{},{}",firmBean.resultCd,firmBean.resultMsg);
	  return firmBean;
  }
  

	
	/**
	 * 은행통한 예금주조회
	 * @param bankCd
	 * @param userBankCd
	 * @param userAccount
	 */
  	public FirmBean holder(String bankCd, String userBankCd, String userAccount) {
	  
  		logger.info("예금주조회");
		FirmBean firmBean = new FirmBean();
		firmBean.bankCd 	= bankCd;
		firmBean.msgType 	= "0600400";
		firmBean.userId		= "SYSTEM";
		firmBean.data.put("bankCd", userBankCd);
		firmBean.data.put("account", userAccount);
		firmBean = comm(firmBean);
		logger.info("응답:{},{}",firmBean.resultCd,firmBean.resultMsg);
		return firmBean;
	}
	
	/**
	 * FCS 예금주조회
	 * @param bankCd
	 * @param userBankCd
	 * @param userAccount
	 */
  	public FirmBean holder(String userBankCd, String userAccount) {
  	  
  		logger.info("예금주조회");
		FirmBean firmBean = new FirmBean();
		firmBean.bankCd 	= "099";
		firmBean.msgType 	= "0600400";
		firmBean.userId		= "SYSTEM";
		firmBean.data.put("bankCd", userBankCd);
		firmBean.data.put("account", userAccount);
		firmBean = comm(firmBean);
		logger.info("응답:{},{}",firmBean.resultCd,firmBean.resultMsg);
		return firmBean;
	}
	
	
	/**
	 * 집계
	 * @param bankCd
	 */
  public SharedMap<String, Object> statistics(String bankCd) {
    logger.info("모계좌집계");
    FirmBean firmBean = new FirmBean();
    firmBean.bankCd = bankCd;
    firmBean.msgType = "0700100";
    firmBean.userId = "SYSTEM";
    firmBean = comm(firmBean);
    logger.info("응답:{},{}", firmBean.resultCd, firmBean.resultMsg);
    logger.info("idx:{},{}", firmBean.idx, firmBean.data.get("resData"));
    logger.info("data : {}", GsonUtil.toJson(firmBean.data));

    resMap.put("result", firmBean.resultCd);
    resMap.put("msg", firmBean.resultMsg);
    resMap.put("data", firmBean.data);
    return resMap;
  }
  
  public FirmBean transfer(String bankCd, String recvBankCd,String recvAccount,long amount,String recordInfo, String sender){
		FirmBean firmBean = new FirmBean();
		firmBean.bankCd 	= bankCd;
		firmBean.msgType 	= "0100100";
		firmBean.userId		= "SYSTEM";
		firmBean.data.put("amount",amount);
		firmBean.data.put("recvBankCd",recvBankCd);
		firmBean.data.put("recvAccount",recvAccount);
		firmBean.data.put("recordInfo",recordInfo);
		firmBean.data.put("procType","AS");
		firmBean.data.put("sender", sender);
	
		firmBean = newComm(firmBean);
		logger.info("응답:{},{}",firmBean.resultCd,firmBean.resultMsg);
		logger.info("idx:{},{}",firmBean.idx,firmBean.data.getLong("balance"));
		logger.info("data : {}",GsonUtil.toJson(firmBean.data));
		return firmBean;
		
	}
  	
	 /**
	 * 자금이체 잔액조회
	 * @param mAccnt
	 * @param bankCd
	 * @param action
	 * @param ip
	 * @param userId
	 * @return
	 */
	public FirmBean balanceCheck(String mAccnt,String bankCd,String action,String ip,String userId){
		  FirmBean firmBean = new FirmBean();
		  firmBean.mAccnt 	= mAccnt;
		  firmBean.msgType 	= action;
		  firmBean.bankCd 	= bankCd;
		  firmBean.userIp	= ip;
		  firmBean.userId 	= userId;
		  logger.info("요청:{}",action);
		  firmBean = comm(firmBean);
		  logger.info("응답:{},{}",firmBean.resultCd,firmBean.resultMsg);
		  return firmBean;
	  }
  
  	/**
  	 * 자금이체 이체
  	 * @param sendBankCd
  	 * @param recvBankCd
  	 * @param recvAccount
  	 * @param amount
  	 * @param sender
  	 * @param receiver
  	 * @return
  	 */
  	public FirmBean balanceTransfer(String sendBankCd, String recvBankCd,String recvAccount,long amount, String sender){
		FirmBean firmBean = new FirmBean();
		firmBean.bankCd 	= sendBankCd;
		firmBean.msgType 	= "0100100";
		firmBean.userId		= "SYSTEM";
		firmBean.data.put("amount",amount);
		firmBean.data.put("recvBankCd",recvBankCd);
		firmBean.data.put("recvAccount",recvAccount);
		firmBean.data.put("sender", sender);
		firmBean.data.put("procType","MT");
		
		firmBean = comm(firmBean);
		logger.info("응답:{},{}",firmBean.resultCd,firmBean.resultMsg);
		logger.info("idx:{},{}",firmBean.idx,firmBean.data.getLong("balance"));
		logger.info("data : {}",GsonUtil.toJson(firmBean.data));
		return firmBean;
	}
  
	public FirmBean comm(FirmBean firmBean){
		
		Socket socket = null;
		OutputStream output = null;
		InputStream input = null;
		String reqJson = GsonUtil.toJson(firmBean);
		String resJson = "";
		long time = System.currentTimeMillis();
		try{
//			if("0600300".equals(firmBean.msgType) && "089".equals(firmBean.bankCd)){
//				port = 10026;
//			}else {
//				port = 10006;
//			}
			port = 10006;
			socket = new Socket(host, port);
			socket.setSoTimeout(timeout);
			
			output = socket.getOutputStream();
			output.write(reqJson.getBytes());
			output.flush();
			
			input = socket.getInputStream();
		
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int bcount = 0;
			byte[] buf = new byte[2048];
			int read_retry_count = 0;
			while(true) {
				int n = input.read(buf);
			    if ( n > 0 ) { bcount += n; bout.write(buf,0,n); }
			    else if (n == -1) break;
			    else  { // n == 0
			if (++read_retry_count >= 5)
			  throw new IOException("inputstream-read-retry-count(5) exceed !");
			    }
			    if(input.available() == 0){ break; }
			}
			bout.flush();
			byte[] res = bout.toByteArray();
			bout.close();
			
			firmBean = (FirmBean)GsonUtil.fromJson(new String(res,"MS949"), FirmBean.class);
			resJson = GsonUtil.toJson(firmBean);
		}catch(Exception e){
			firmBean.resultCd = "XXXX";
			firmBean.resultMsg = "펌뱅킹 시스템과의 통신장애 :"+e.getMessage();
		}finally{
			logger.info("-> FIRM : [{}]",reqJson);
			logger.info("<- FIRM : [{}],{}",resJson,(System.currentTimeMillis()-time));
			
			try{
				if(input != null){ input.close();}
				if(output != null){ output.close();}
				if(socket != null){ socket.close();}
			}catch(Exception ex){
				
			}
		}
		
		return firmBean;
	}
	
	public FirmBean newComm(FirmBean firmBean){
		Socket socket = null;
		OutputStream output = null;
		InputStream input = null;
		String reqJson = GsonUtil.toJson(firmBean);
		String resJson = "";
		long time = System.currentTimeMillis();
		
		try{
			socket = new Socket(host, newPort);
			socket.setSoTimeout(timeout);
			
			output = socket.getOutputStream();
			output.write(reqJson.getBytes("EUC-KR"));
			output.flush();
			
			input = socket.getInputStream();
		
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int bcount = 0;
			byte[] buf = new byte[2048];
			int read_retry_count = 0;
			while(true) {
				int n = input.read(buf);
			    if ( n > 0 ) { bcount += n; bout.write(buf,0,n); }
			    else if (n == -1) break;
			    else  { // n == 0
			if (++read_retry_count >= 5)
			  throw new IOException("inputstream-read-retry-count(5) exceed !");
			    }
			    if(input.available() == 0){ break; }
			}
			bout.flush();
			byte[] res = bout.toByteArray();
			bout.close();
			
			firmBean = (FirmBean)GsonUtil.fromJson(new String(res,"MS949"), FirmBean.class);
			
		}catch(Exception e){
			firmBean.resultCd = "XXXX";
			firmBean.resultMsg = "펌뱅킹 시스템과의 통신장애 :"+e.getMessage();
		}finally{
			logger.info("-> FIRM : [{}]",reqJson);
			logger.info("<- FIRM : [{}],{}",resJson,(System.currentTimeMillis()-time));
			
			try{
				if(input != null){ input.close();}
				if(output != null){ output.close();}
				if(socket != null){ socket.close();}
			}catch(Exception ex){
				
			}
		}
		
		return firmBean;
	}

	
	public static String accountFormat(String bankCd,String account){
		account = account.replaceAll("[-]","").trim();
		int len = account.length();
		
		if(bankCd.equals("002")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{5})(\\d{1})","$1-$2-$3-$4");
			}else if(len == 14){
				if(account.startsWith("013")){
					return account.replaceAll("(\\d{3})(\\d{7})(\\d{1})(\\d{3})","$1-$2-$3-$4");
				}else{
					return account.replaceAll("(\\d{3})(\\d{8})(\\d{3})","$1-$2-$3");
				}
			}
		}else if(bankCd.equals("003")){
			if(len == 10){
				return account.replaceAll("(\\d{8})(\\d{2})","$1-$2");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{8})","$1-$2");
			}else if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})(\\d{1})","$1-$2-$3-$4");	
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{6})(\\d{2})(\\d{2})(\\d{1})","$1-$2-$3-$4-$5");	
			}
		}else if(bankCd.equals("004")){
			if(len == 11){
				return account;
			}else if(len == 12){
				if(account.substring(3, 5).equals("01")){
					return account.replaceAll("(\\d{3})(\\d{2})(\\d{4})(\\d{3})","$1-$2-$3-$4");
				}else{
					return account.replaceAll("(\\d{6})(\\d{2})(\\d{4})","$1-$2-$3");
				}
			}else if(len == 14){
				return account.replaceAll("(\\d{4})(\\d{2})(\\d{7})(\\d{1})","$1-$2-$3-$4");	
			}
		}else if(bankCd.equals("005")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else{
				return account.replaceAll("(\\d{3})(\\d{6})(\\d{3})", "$1-$2-$3");
			}
		}else if(bankCd.equals("007")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 12){
				if(account.substring(0, 3).equals("101") || account.substring(0, 3).equals("201")){
					return account.replaceAll("(\\d{3})(\\d{8})(\\d{1})",  "$1-$2-$3");
				}else{
					return account.replaceAll("(\\d{1})(\\d{11})", "$1-$2");
				}
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{8})(\\d{1})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("011")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 12){
				return account.replaceAll("(\\d{4})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 13){
				return account.replaceAll("(\\d{3})(\\d{4})(\\d{4})(\\d{1})(\\d{1})", "$1-$2-$3-$4-$5");
			}else if(len == 14){
				if(account.substring(6, 8).equals("64") || account.substring(6, 8).equals("65")){
					return account.replaceAll("(\\d{6})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
				}else{
					return account.replaceAll("(\\d{3})(\\d{4})(\\d{4})(\\d{2})(\\d{1})", "$1-$2-$3-$4-$5");
				}
			}
		}else if(bankCd.equals("012")){
			if(len == 13){
				return account.replaceAll("(\\d{3})(\\d{4})(\\d{4})(\\d{1})(\\d{1})", "$1-$2-$3-$4-$5");
			}else if(len == 14){
				if(account.substring(6,8).equals("51")){
					return account.replaceAll("(\\d{6})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
				}else if(account.substring(6,8).equals("66") || account.substring(6,8).equals("67")){
					return account.replaceAll("(\\d{6})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
				}else{
					return account.replaceAll("(\\d{3})(\\d{4})(\\d{4})(\\d{2})(\\d{1})", "$1-$2-$3-$4-$5");
				}
			}
		}else if(bankCd.equals("020")){
			if(len == 13){
				return account.replaceAll("(\\d{4})(\\d{3})(\\d{6})", "$1-$2-$3");
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{6})(\\d{2})(\\d{2})(\\d{1})", "$1-$2-$3-$4-$5");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})(\\d{1})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("023")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{9})", "$1-$2-$3");
			}
		}else if(bankCd.equals("027")){
			if(len == 13){
				return account.replaceAll("(\\d{3})(\\d{5})(\\d{2})(\\d{1})(\\d{2})", "$1-$2-$3-$4-$5");
			}else if(len == 12){
				return account.replaceAll("(\\d{1})(\\d{6})(\\d{1})(\\d{2})(\\d{2})", "$1-$2-$3-$4-$5");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{5})(\\d{2})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 10){
				if(account.substring(0,1).equals("5")){
					return account.replaceAll("(\\d{1})(\\d{6})(\\d{2})(\\d{1})", "$1-$2-$3-$4");	
				}else{
					return account.replaceAll("(\\d{2})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
				}
			}
		}else if(bankCd.equals("031")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}else if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})(\\d{1})", "$1-$2-$3-$4");	
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})(\\d{3})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("032")){
			if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})(\\d{1})", "$1-$2-$3-$4");	
			}else if(len == 13){
				return account.replaceAll("(\\d{3})(\\d{4})(\\d{4})(\\d{2})", "$1-$2-$3-$4");	
			}
		}else if(bankCd.equals("034")){
			if(len == 12){
				if(account.substring(3,6).equals("107") || account.substring(3,6).equals("108")){
					return account.replaceAll("(\\d{3})(\\d{3})(\\d{5})(\\d{1})", "$1-$2-$3-$4");	
				}else{
					return account.replaceAll("(\\d{1})(\\d{3})(\\d{9})", "$1-$2-$3");
				}
			}else if(len == 13){
				return account.replaceAll("(\\d{3})(\\d{3})(\\d{5})", "$1-$2-$3");	
			}
		}else if(bankCd.equals("035")){
			if(len==10){
				return account.replaceAll("(\\d{2})(\\d{2})(\\d{6})", "$1-$2-$3");
			}
		}else if(bankCd.equals("037")){
			if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{7})", "$1-$2-$3");
			}else if(len == 13){
				return account.replaceAll("(\\d{1})(\\d{3})(\\d{2})(\\d{7})", "$1-$2-$3-$4");	
			}
		}else if(bankCd.equals("039")){
			if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{7})", "$1-$2-$3");
			}else if(len == 13){
				return account.replaceAll("(\\d{3})(\\d{9})(\\d{1})", "$1-$2-$3");
			}
		}else if(bankCd.equals("045")){
			if(len == 13){
				if(account.substring(4,6).equals("09") || account.substring(4,6).equals("10") || account.substring(4,6).equals("13") || account.substring(4,6).equals("37")){
					return account.replaceAll("(\\d{4})(\\d{2})(\\d{6})(\\d{1})", "$1-$2-$3-$4");	
				}else{
					return account.replaceAll("(\\d{4})(\\d{8})(\\d{1})", "$1-$2-$3");
				}
			}else if(len == 14){
				return account.replaceAll("(\\d{4})(\\d{3})(\\d{6})(\\d{1})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("048")){
			if(len == 13){
				return account.replaceAll("(\\d{5})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 14){
				return account.replaceAll("(\\d{5})(\\d{2})(\\d{6})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 10){
				return account.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
			}else if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{3})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("050")){
			if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{2})(\\d{6})(\\d{1})", "$1-$2-$3-$4-$5");
			}
		}else if(bankCd.equals("054")){
			if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{5})(\\d{1})(\\d{3})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("055")){
			return account;
		}else if(bankCd.equals("057")){
			return account;
		}else if(bankCd.equals("060")){
			if(len == 12){
				return account.replaceAll("(\\d{4})(\\d{5})(\\d{2})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 14){
				return account.replaceAll("(\\d{4})(\\d{10})", "$1-$2");
			}
		}else if(bankCd.equals("062")){
			return account.replaceAll("(\\d{3})(\\d{9})(\\d{2})", "$1-$2-$3");
		}else if(bankCd.equals("064")){
			if(len == 13){
				return account.replaceAll("(\\d{5})(\\d{2})(\\d{6})", "$1-$2-$3");
			}else if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{7})", "$1-$2-$3");
			}  
		}else if(bankCd.equals("071")){
			return account.replaceAll("(\\d{6})(\\d{2})(\\d{6})", "$1-$2-$3");
		}else if(bankCd.equals("081")){
			return account.replaceAll("(\\d{3})(\\d{9})(\\d{2})", "$1-$2-$3");
		}else if(bankCd.equals("088")){
			if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{8})(\\d{1})", "$1-$2-$3");
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{3})(\\d{7})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{5})(\\d{1})", "$1-$2-$3-$4");
			}else if(len == 13){
				if(account.substring(3,5).equals("81")){
					return account.replaceAll("(\\d{3})(\\d{2})(\\d{7})(\\d{1})", "$1-$2-$3-$4");
				}else{
					return account.replaceAll("(\\d{3})(\\d{2})(\\d{8})", "$1-$2-$3");
				}
			}
		}else if(bankCd.equals("089")){
			if(len == 10){
				return account.replaceAll("(\\d{1})(\\d{9})", "$1-$2");
			}else if(len == 12){
				return account.replaceAll("(\\d{3})(\\d{3})(\\d{6})", "$1-$2-$3");
			}else if(len == 13){
				return account.replaceAll("(\\d{2})(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3-$4");
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{4})(\\d{3})(\\d{4})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("090")){
			return account.replaceAll("(\\d{4})(\\d{2})(\\d{7})", "$1-$2-$3");
		}else if(bankCd.equals("209")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}else if(len == 12){
				return account.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1-$2-$3");
			}
		}else if(bankCd.equals("218")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}else if(len == 9){
				return account.replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1-$2-$3");
			}
		}else if(bankCd.equals("230")){
			return account;
		}else if(bankCd.equals("238")){
			return account;
		}else if(bankCd.equals("240")){
			if(len == 8 || len == 10 || len == 12){
				return account;
			}else if(len == 14){
				return account.replaceAll("(\\d{1})(\\d{5})(\\d{8})", "$1-$2-$3");
			}
		}else if(bankCd.equals("243")){
			if(len == 10){
				return account.replaceAll("(\\d{8})(\\d{2})", "$1-$2");
			}else if(len == 12){
				return account.replaceAll("(\\d{8})(\\d{4})", "$1-$2");
			}else if(len == 14){
				return account.replaceAll("(\\d{8})(\\d{2})(\\d{4})", "$1-$2-$3");
			}
		}else if(bankCd.equals("247")){
			return account;
		}else if(bankCd.equals("261")){
			return account.replaceAll("(\\d{4})(\\d{4})(\\d{1})(\\d{1})", "$1-$2-$3-$4");
		}else if(bankCd.equals("262")){
			return account.replaceAll("(\\d{4})(\\d{4})(\\d{2})", "$1-$2-$3");
		}else if(bankCd.equals("263")){
			return account;
		}else if(bankCd.equals("264")){
			if(len == 10){
				return account.replaceAll("(\\d{4})(\\d{4})(\\d{2})", "$1-$2-$3");
			}else{
				return account.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
			}
		}else if(bankCd.equals("265")){
			return account;
		}else if(bankCd.equals("266")){
			return account;
		}else if(bankCd.equals("267")){
			if(len == 9){
				return account.replaceAll("(\\d{3})(\\d{6})", "$1-$2");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{6})(\\d{2})", "$1-$2-$3");
			}
		}else if(bankCd.equals("269")){
			if(len == 10){
				return account;
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{8})", "$1-$2");
			}else if(len == 13){
				return account;
			}else if(len == 14){
				return account.replaceAll("(\\d{3})(\\d{11})", "$1-$2");
			}
		}else if(bankCd.equals("270")){
			if(len == 8){
				return account.replaceAll("(\\d{7})(\\d{1})", "$1-$2");
			}else if(len == 10){
				return account.replaceAll("(\\d{7})(\\d{1})(\\d{2})", "$1-$2-$3");
			}else if(len == 11){
				return account.replaceAll("(\\d{8})(\\d{3})", "$1-$2");
			}else if(len == 14){
				return account.replaceAll("(\\d{8})(\\d{3})(\\d{3})", "$1-$2-$3");
			}
		}else if(bankCd.equals("278")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}
		}else if(bankCd.equals("279")){
			if(len == 9){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{4})", "$1-$2-$3");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{4})(\\d{2})", "$1-$2-$3-$4");
			}
		}else if(bankCd.equals("280")){
			return account;
		}else if(bankCd.equals("287")){
			if(len == 10){
				return account.replaceAll("(\\d{4})(\\d{4})(\\d{2})", "$1-$2-$3");
			}else if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}
		}else if(bankCd.equals("290")){
			return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
		}else if(bankCd.equals("291")){
			return account;
		}else if(bankCd.equals("292")){
			if(len == 11){
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})", "$1-$2-$3");
			}else if(len == 14){  
				return account.replaceAll("(\\d{3})(\\d{2})(\\d{6})(\\d{3})", "$1-$2-$3-$4");
			}
		}
			return account;  
		}

	
	
	public static void main(String[] args){  
		String bankCd = "292";
		System.out.println(FirmUtil.accountFormat(bankCd, "1234097891234"));
	}

}