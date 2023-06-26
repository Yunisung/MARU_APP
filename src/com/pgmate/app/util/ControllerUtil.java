package com.pgmate.app.util;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;

import com.pgmate.app.dao.MchtMngDAO;
import com.pgmate.app.dao.TrxCapDAO;
import com.pgmate.app.dao.TrxDAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class ControllerUtil {
	static public String identityMasking(String orgStr){
		//주민번호
		if(orgStr.matches("^\\d{6}\\-[1-4]\\d{6}$")){
			orgStr = orgStr.substring(0, 8) + "******";
		}
		
		return orgStr;
	}
	
	/**
	 * 아래거 테스트할때는 가급적 가맹점 수수료 높은 넘으로 하도록 .잘 되는지 테스트는 못했으니. 
	 * @param capId
	 * @param risk
	 * @return
	 */
	public String setCaptureToRisk(String capId,String risk){
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset = trxCapDAO.getByCapId(capId);
		
		RecordSet rset2 = trxCapDAO.getByRootCapId2(capId);
		
		if(rset2.size() > 0){
			return "NOK:"+capId+":이미취소처리된 거래입니다.";
		}
		
		//지급일자 
		if(rset.size() == 0){
			return "NOK:"+capId+":검색된 거래가 없습니다.";
		}
		
		TrxDAO trxDAO = new TrxDAO();
		
		SharedMap<String,Object> trxCapMap		= rset.getRow(0);
		
		
		if(trxCapMap.getString("stlStatus").equals("정산보류")){
			return "NOK:"+capId+":정산보류 건은 리스크 해지만 가능합니다.";
		}
		
		if(trxCapMap.getString("stlStatus").equals("정산대기")){
			SharedMap<String,Object> updateMap	= new SharedMap<String,Object>();
			
			//현재 리스크가 없는 거래 
			if(trxCapMap.isNullOrSpace("risk")){
				if(trxCapMap.getString("stlType").equals("D+1")){
					if(trxCapMap.isEquals("vanId", "FACTORING")){
						//거래 정보를 원복한다.
						updateMap.put("stlRate"		, trxCapMap.getDouble("stlRate")-trxCapMap.getDouble("stlLoanRate"));			//선정산 수수료 적용
						updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));
						updateMap.put("stlFeeVat"	, calcVat(updateMap.getLong("stlFee")));
						updateMap.put("stlAgencyFee", 0);
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));
						updateMap.put("risk", risk);
						updateMap.put("capId", capId);
						
						if(trxDAO.updateTrxCapDtlWithAgency(updateMap)){
							return "OK:"+capId+":RISK 설정 ,D+1 ,"+risk +",수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						}else{
							return "NOK:"+capId+":RISK 설정 실패 DB 오류";
						}
					}else{
						if(trxCapMap.isEquals("vanId", "OFFLINE")){
							updateMap.put("stlRate"		, trxCapMap.getDouble("stlRate")-trxCapMap.getDouble("stlLoanRate"));						//선정산수수료차감.
							updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));					//산정된 rate 로 수수료 계산
							updateMap.put("stlFeeVat"	, 0);																						//OFF 는 VAT  없음
						}else{
							updateMap.put("stlRate"		, trxCapMap.getDouble("stlRate")-trxCapMap.getDouble("stlLoanRate"));						//선정산 수수료 차감
							updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));					//산정된 rate 로 수수료 계산
							updateMap.put("stlFeeVat"	, calcVat(updateMap.getLong("stlFee")));													//수수료에 해당하는 VAT 계산
						}
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));
						updateMap.put("risk", risk);
						updateMap.put("capId", capId);
						if(trxDAO.updateTrxCapDtl(updateMap)){
							return "OK:"+capId+":RISK 설정 ,D+1 ,"+risk +",수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						}else{
							return "NOK:"+capId+":RISK 설정 실패 DB 오류";
						}
					}
				}else{
					updateMap.put("risk", risk);
					updateMap.put("capId", capId);
					if(trxDAO.updateTrxCapDtlToRisk(updateMap)){
						return "OK:"+capId+":RISK 설정 ,"+trxCapMap.getString("stlType")+" ,"+risk +", 수수료변경없음";
					}else{
						return "NOK:"+capId+":RISK 설정 실패 DB 오류";
					}
				}
			}else{	//리스크 관리 항목 변경 건 
				updateMap.put("risk", risk);
				updateMap.put("capId", capId);
				if(trxDAO.updateTrxCapDtlToRisk(updateMap)){
					return "OK:"+capId+":RISK 변경 ,"+trxCapMap.getString("risk")+"->"+risk +", 수수료변경없음";
				}else{
					return "NOK:"+capId+":RISK 설정 실패 DB 오류";
				}
			}
			
			
			
		}else{
			return "NOK:"+capId+":정산대기가 아닌 거래는 변경할 수 없습니다.";
		}
		
	}
	
	public String setRiskToNormal(String capId){
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		RecordSet rset = trxCapDAO.getByCapId(capId);
		
		if(rset.size() == 0){
			return "NOK:"+capId+":검색된 거래가 없습니다.";
		}
		
		
		TrxDAO trxDAO = new TrxDAO();
		SharedMap<String,Object> trxCapMap		= rset.getRow(0);
		
		if(trxCapMap.getString("stlStatus").equals("정산대기") || trxCapMap.getString("stlStatus").equals("정산보류")){
			if(trxCapMap.isNullOrSpace("risk")){
				return "NOK:"+capId+":현재 RISK설정된 거래가 아닙니다.";
			}else{
				SharedMap<String,Object> updateMap	= new SharedMap<String,Object>();
				if(trxCapMap.getString("stlType").equals("D+1")){
					if(trxCapMap.isEquals("vanId", "FACTORING")){
						//거래 정보를 원복한다.
						updateMap.put("stlRate"			, trxCapMap.getDouble("stlRate")+trxCapMap.getDouble("stlLoanRate"));			//선정산 수수료 적용
						updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));
						updateMap.put("stlFeeVat"	, calcVat(updateMap.getLong("stlFee")));
						updateMap.put("stlAgencyFee", calcFeeVat(trxCapMap.getLong("amount"),trxCapMap.getDouble("stlAgencyRate")));
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));
						updateMap.put("risk", "");
						updateMap.put("capId", capId);
						
						if(trxDAO.updateTrxCapDtlWithAgency(updateMap)){
							return "OK:"+capId+":RISK 해지 ,D+1 ,수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						}else{
							return "NOK:"+capId+":RISK 해지 실패 DB 오류";
						}
					}else{
						if(trxCapMap.isEquals("vanId", "OFFLINE")){
							
							long normalFee 				= calcFee(trxCapMap.getLong("amount"), trxCapMap.getDouble("stlRate"));			//VAN수수료
							long loanFee	 			= calcFee(trxCapMap.getLong("amount"), trxCapMap.getDouble("stlLoanRate"));		//선정산 수수료
							long loanFeeVat				= calcVat(loanFee);																//VAT는 선정산 수수료만 적용
							
							updateMap.put("stlRate"		, trxCapMap.getDouble("stlRate")+trxCapMap.getDouble("stlLoanRate") + trxCapMap.getDouble("stlInterRate"));			//선정산 수수료 적용
							updateMap.put("stlFee"		, normalFee+loanFee);
							updateMap.put("stlFeeVat"	, loanFeeVat);
						}else{
							updateMap.put("stlRate"		, trxCapMap.getDouble("stlRate")+trxCapMap.getDouble("stlLoanRate") + trxCapMap.getDouble("stlInterRate"));			//선정산 수수료 적용
							updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));		//정산 수수료 반영
							updateMap.put("stlFeeVat"	, calcVat(updateMap.getLong("stlFee")));										//VAT 산정 
						}
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));	
						updateMap.put("risk", "");
						updateMap.put("capId", capId);
						
						if(trxDAO.updateTrxCapDtl(updateMap)){
							return "OK:"+capId+":RISK 해지 ,D+1 ,수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						}else{
							return "NOK:"+capId+":RISK 해지 실패 DB 오류";
						}
					}
				}else{
					updateMap.put("risk", "");
					updateMap.put("capId", capId);
					if(trxDAO.updateTrxCapDtlToRisk(updateMap)){
						return "OK:"+capId+":RISK 해지 ,"+trxCapMap.getString("stlType")+" 수수료 변경없음";
					}else{
						return "NOK:"+capId+":RISK 해지 실패 DB 오류";
					}
				}
				
			}
		}else{
			return "NOK:"+capId+":정산대기가 아닌 거래는 변경할 수 없습니다.";
		}
		
	}
	
	public long calcFee(long amount,double rate){
		rate = rateFormat(rate);
		long decimal = 10000;
		if(amount < 0){
			return -new Double(Math.round(-amount*(rate *decimal))).longValue()/decimal;
		}else{
			return new Double(Math.round(amount*(rate *decimal))).longValue()/decimal;
			
		}
	}
	
	
	public long calcRootVat(long amount){
		if(amount < 0){
			return -new Double(-amount *10 /110).longValue();
		}else{
			return new Double(amount *10 /110).longValue();
		}
	}
	
	public long calcVat(long fee){
		if(fee < 0){
			return -new Double(-fee *10 /100).longValue();
		}else{
			return new Double(fee *10 /100).longValue();
		}
	}
	
	public long calcFeeVat(long amount,double rate){
		long fee = calcFee(amount,rate);
		return fee+ calcVat(fee);
	}
	
	public long calcDefaultFeeVat(long amount,double rate){
		long decimal = 1000;
		long fee = 0;
		if(amount < 0){
			fee = -new Double(-amount*(rate *decimal)).longValue()/decimal;
		}else{
			fee = new Double(amount*(rate *decimal)).longValue()/decimal;
		}
		long vat = calcVat(fee);
		return fee+vat;
	}
	
	public long calcDanalFeeVat(long amount,double rate){
		long decimal = 1000;
		long fee = 0;
		if(amount < 0){
			fee = -new Double(-amount*(rate *decimal)).longValue()/decimal;
		}else{
			fee = new Double(amount*(rate *decimal)).longValue()/decimal;
		}
		long vat = calcVat(fee);
		return fee+vat;
	}
	
	
	
	public long calcRoundTrimFee(long amount,double rate){
		rate = rateFormat(rate);
		
		if(amount < 0){
			return -new Double(-amount*rate).longValue();
		}else{
			return new Double(amount*rate).longValue();
		}
	
	}
	
	public long calcRoundTrimVat(long fee){
		
		if(fee < 0){
			return -new Double(-fee*0.1).longValue();
		}else{
			return new Double(fee*0.1).longValue();
		}
	}
	
	public long calcRoundUpFee(long amount,double rate){
		rate = rateFormat(rate);
		if(amount < 0){
			return -Math.round(-amount*rate);
		}else{
			return Math.round(amount*rate);
		}
	}
	
	public long calcRoundUpVat(long fee){
		
		if(fee < 0){
			return -Math.round(-fee*0.1);
		}else{
			return Math.round(fee*0.1);
		}
	}
	
	public long calcRoundUpFeeVat(long amount,double rate){
		long fee = calcRoundUpFee(amount,rate);
		return fee + calcRoundUpVat(fee);
	}
	
	
	
	
	public long calcDanalFee(long amount,double rate){
		rate = rateFormat(rate);
		long decimal = 1000;
		if(amount < 0){
			return -new Double(-amount*(rate *decimal)).longValue()/decimal;
		}else{
			return new Double(amount*(rate *decimal)).longValue()/decimal;
		}
	}
	
	public long calcDanalVat(long fee){
		if(fee < 0){
			return -new Double(-fee *10 /100).longValue();
		}else{
			return new Double(fee *10 /100).longValue();
		}
	}
	
	
	
	public double rateFormat(double rate){
		String pattern = "#.#####";
		DecimalFormat format = new DecimalFormat(pattern);
		return new Double(format.format(rate)).doubleValue();
	}
	
	

	
	
	public String calcDay(String settleType,String today){
		TrxDAO trxDAO = new TrxDAO();
		int term = 1;
		if(settleType.startsWith("D")){
			term = CommonUtil.parseInt(settleType.replaceAll("D[+]", ""));
			String day =  trxDAO.getSettleDay(today, term);
			/*
			//오늘 정산 예정일이지만 8시 이후에 요청된 거래는 자동으로 내일로 정산일정이 밀린다.
			if(day.equals(currentDay) && CommonUtil.parseInt(CommonUtil.getCurrentDate("HH")) > 8){
				day =  trxDAO.getSettleDay(currentDay,1);
			}*/
			return day;
		}else if(settleType.startsWith("M")){
			term = CommonUtil.parseInt(settleType.replaceAll("M[+]", ""));
			String nextMonth = CommonUtil.getOpDate(GregorianCalendar.MONTH,1,today).substring(0,6);
			return trxDAO.getSettleDay(nextMonth+CommonUtil.zerofill(term,2));
		}else{
			term = CommonUtil.parseInt(settleType.replaceAll("D[+]", ""));
			String day =  trxDAO.getSettleDay(today, term);
			/*
			if(day.equals(currentDay) && CommonUtil.parseInt(CommonUtil.getCurrentDate("HH")) > 8){
				day =  trxDAO.getSettleDay(currentDay,1);
			}*/
			return day;
		}
	}
	
	
	
	public static void main(String[] args) {
		//System.out.println(ControllerUtil.identityMasking("8170218-1402919"));
	}
}
