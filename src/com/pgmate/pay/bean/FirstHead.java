
package com.pgmate.pay.bean;


import com.pgmate.lib.util.lang.CommonUtil;

public class FirstHead {

	public String headerName 	= "";		// 가맹점 inital
	public String serviceType 	= "";		// 서비스종류 CC:신용카드, KK:현금영수증거래
	public String trxType 		= "";		// 업무구분 D1:신용카드 승인, D2:신용카드 승인취소, S1:신용카드 승인, S2: IC신용카드 승인취소,  K1:현금영수증 승인, K2: 현금영수증 승인취소
	public String terminalId	= "";		// 단말기 번호
	public String trnDate		= "";		// 거래일시
	public String transNo		= "";		// 거래 sequence 번호
	public String resultCd		= "";		// 응답코드 요청시 space, 응답시 SET
	public String filler1		= "";		// 예비필드(거래고유번호)
	
	public String trxId			= "";
	
	public FirstHead(){	
	}
	
	public FirstHead(String transaction){
		this(transaction.getBytes());
	}
	
	public FirstHead(byte[] transaction){
		headerName 		= CommonUtil.toString(transaction,0,4).trim();		
		serviceType		= CommonUtil.toString(transaction,4,2).trim();
		trxType			= CommonUtil.toString(transaction,6,2).trim();
		terminalId		= CommonUtil.toString(transaction,8,8).trim();
		trnDate			= CommonUtil.toString(transaction,16,12).trim();
		transNo			= CommonUtil.toString(transaction,28,6).trim();
		resultCd		= CommonUtil.toString(transaction,34,2).trim();
		filler1			= CommonUtil.toString(transaction,36,14).trim();
		
	}
	
	public String getHeader(){

		StringBuffer transaction = new StringBuffer();
		transaction.append(CommonUtil.byteFiller(headerName	,4));
		transaction.append(CommonUtil.byteFiller(serviceType,2));
		transaction.append(CommonUtil.byteFiller(trxType	,2));
		transaction.append(CommonUtil.byteFiller(terminalId	,8));
		transaction.append(CommonUtil.byteFiller(trnDate	,12));
		transaction.append(CommonUtil.byteFiller(transNo	,6));
		transaction.append(CommonUtil.byteFiller(resultCd	,2));
		transaction.append(CommonUtil.byteFiller(filler1    ,14));
		
		return transaction.toString();
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTrnDate() {
		return trnDate;
	}

	public void setTrnDate(String trnDate) {
		this.trnDate = trnDate;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getResultCd() {
		return resultCd;
	}

	public void setResultCd(String resultCd) {
		this.resultCd = resultCd;
	}

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}



	
	
	
}
