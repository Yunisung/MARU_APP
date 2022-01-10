package com.pgmate.pay.bean;

import java.io.ByteArrayOutputStream;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class FirstBody {

	public String cardNo		= "";	//카드번호
	public String expireDate	= "";	//유효기간 SPACE
	public String inputType		= "";   //입력구분 S:swipe, K:key-in, C: IC
	public String installment	= "";	//할부개월 or 현금영수증 구분(개인:00, 사업자:01)
	public String amount		= "";	//거래금액
	public String serviceCharge	= "";	//봉사료
	public String tax			= "";	//세금
	public String rfdType		= "";	//취소구분
	public String authCd		= "";	//승인번호
	public String authDate		= "";	//승인일자(취소시 원거래)
	public String mchtNo		= "";	//가맹점번호
	public String cardName		= "";	//카드명
	public String issuerCode	= "";	//발급사코드
	public String issuer		= "";	//발급사명
	public String acquirerCode	= "";	//매입사코드
	public String acquirer		= "";	//매입사명
	public String fdikCode		= "";	//fdik 내부 응답코드(성공:0000)
	public String cardResultCode= "";	//카드사응답코드(space)
	public String balance		= "";	//잔액
	public String cardType		= "";	//카드구분(00:신용, 01:체크, 02:선불)
	public String gasCode		= "";	//주유소코드
	public String filler2		= "";	//예비필드
	public String filler3		= "";	//예비필드
	public String cr			= "";	//0x0D
	
	public FirstBody() {
		// TODO Auto-generated constructor stub
	}
	
	public FirstBody(String transaction){
		this(transaction.getBytes());
	}
	
	public FirstBody(byte[] transaction){
		cardNo		= CommonUtil.toString(transaction,0,20).trim();
		expireDate	= CommonUtil.toString(transaction,20,4).trim();
		inputType   = CommonUtil.toString(transaction,24,1).trim();
		installment	= CommonUtil.toString(transaction,25,2).trim();
		amount		= CommonUtil.toString(transaction,27,10).trim();
		serviceCharge	= CommonUtil.toString(transaction,37,10).trim();
		tax	= CommonUtil.toString(transaction,47,10).trim();
		rfdType	= CommonUtil.toString(transaction,57,1).trim();
		authCd	= CommonUtil.toString(transaction,58,12).trim();
		authDate	= CommonUtil.toString(transaction,70,6).trim();
		mchtNo	= CommonUtil.toString(transaction,76,15).trim();
		cardName	= CommonUtil.toString(transaction,91,32).trim();
		issuerCode	= CommonUtil.toString(transaction,123,2).trim();
		issuer	= CommonUtil.toString(transaction,125,8).trim();
		acquirerCode	= CommonUtil.toString(transaction,133,2).trim();
		acquirer	= CommonUtil.toString(transaction,135,8).trim();
		fdikCode	= CommonUtil.toString(transaction,143,4).trim();
		cardResultCode		= CommonUtil.toString(transaction,147,2).trim();
		balance		= CommonUtil.toString(transaction,149,10).trim();
		cardType		= CommonUtil.toString(transaction,159,2).trim();
		gasCode	= CommonUtil.toString(transaction,161,5).trim();
		filler2		= CommonUtil.toString(transaction,166,33).trim();
		filler3 = CommonUtil.toString(transaction,199,100).trim();
		
	}
	
	public String getBody(){

		StringBuffer transaction = new StringBuffer();
		try {
			transaction.append(CommonUtil.byteFiller(cardNo,20));
			transaction.append(CommonUtil.byteFiller(expireDate,4));
			transaction.append(CommonUtil.byteFiller(inputType,1));
			transaction.append(CommonUtil.byteFiller(installment,2));
			transaction.append(CommonUtil.byteFiller(amount,10));
			transaction.append(CommonUtil.byteFiller(serviceCharge,10));
			transaction.append(CommonUtil.byteFiller(tax,10));
			transaction.append(CommonUtil.byteFiller(rfdType,1));
			transaction.append(CommonUtil.byteFiller(authCd,12));
			transaction.append(CommonUtil.byteFiller(authDate,6));
			transaction.append(CommonUtil.byteFiller(mchtNo,15));
			transaction.append(CommonUtil.byteFiller(cardName,32));
			transaction.append(CommonUtil.byteFiller(issuerCode,2));
			transaction.append(CommonUtil.byteFiller(issuer,8));
			transaction.append(CommonUtil.byteFiller(acquirerCode,2));
			transaction.append(CommonUtil.byteFiller(acquirer,8));
			transaction.append(CommonUtil.byteFiller(fdikCode,4));
			transaction.append(CommonUtil.byteFiller(cardResultCode,2));
			transaction.append(CommonUtil.byteFiller(balance,10));
			transaction.append(CommonUtil.byteFiller(cardType,2));
			transaction.append(CommonUtil.byteFiller(gasCode,5));
			transaction.append(CommonUtil.byteFiller(filler2,33));
			transaction.append(CommonUtil.byteFiller(filler3,100));
		}catch (Exception e) {
			// TODO: handle exception
		}
		return transaction.toString();
	}
	
	public String convert(byte[] str,int start,int end){
		String s = "";
		  ByteArrayOutputStream os = new ByteArrayOutputStream();
		  try{
		  os.write(str,start,end);
		  s = os.toString("euc-kr");
		  }catch(Exception e){}
		  return s;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getInstallment() {
		return installment;
	}

	public void setInstallment(String installment) {
		this.installment = installment;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getRfdType() {
		return rfdType;
	}

	public void setRfdType(String rfdType) {
		this.rfdType = rfdType;
	}

	public String getAuthCd() {
		return authCd;
	}

	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	public String getAuthDate() {
		return authDate;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public String getMchtNo() {
		return mchtNo;
	}

	public void setMchtNo(String mchtNo) {
		this.mchtNo = mchtNo;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getIssuerCode() {
		return issuerCode;
	}

	public void setIssuerCode(String issuerCode) {
		this.issuerCode = issuerCode;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAcquirerCode() {
		return acquirerCode;
	}

	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}

	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}

	public String getFdikCode() {
		return fdikCode;
	}

	public void setFdikCode(String fdikCode) {
		this.fdikCode = fdikCode;
	}

	public String getCardResultCode() {
		return cardResultCode;
	}

	public void setCardResultCode(String cardResultCode) {
		this.cardResultCode = cardResultCode;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getGasCode() {
		return gasCode;
	}

	public void setGasCode(String gasCode) {
		this.gasCode = gasCode;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}

	public String getFiller3() {
		return filler3;
	}

	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}

	public String getCr() {
		return cr;
	}

	public void setCr(String cr) {
		this.cr = cr;
	}
	

	

}
