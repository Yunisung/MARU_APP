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
//KJM : 수수료 관련 계산 기능
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
	//KJM : 리스크 변경
	public String setCaptureToRisk(String capId,String risk){
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		//KJM : 매입번호에 대한 내역 리스트
		RecordSet rset = trxCapDAO.getByCapId(capId);
		//KJM : 취소된 매입거래번호 가져옴
		RecordSet rset2 = trxCapDAO.getByRootCapId2(capId);
		
		//KJM : 해당 매입번호가 취소된 매입거래일 경우
		if(rset2.size() > 0){
			return "NOK:"+capId+":이미취소처리된 거래입니다.";
		}
		
		//지급일자 
		//KJM : 매입번호로 되있는 거래 없을 경우
		if(rset.size() == 0){
			return "NOK:"+capId+":검색된 거래가 없습니다.";
		}
		
		TrxDAO trxDAO = new TrxDAO();
		
		//KJM : 매입번호에 대한 매입내역 리스트 map형식으로 저장
		SharedMap<String,Object> trxCapMap		= rset.getRow(0);
		
		//KJM : 정산상태가 정산보류일 경우
		if(trxCapMap.getString("stlStatus").equals("정산보류")){
			return "NOK:"+capId+":정산보류 건은 리스크 해지만 가능합니다.";
		}
		
		//KJM : 정산상태가 정산대기일 경우
		if(trxCapMap.getString("stlStatus").equals("정산대기")){
			SharedMap<String,Object> updateMap	= new SharedMap<String,Object>();
			
			//현재 리스크가 없는 거래 
			if(trxCapMap.isNullOrSpace("risk")){
				//KJM : 가맹점 정산 유형이 D+1일 때
				if(trxCapMap.getString("stlType").equals("D+1")){
					//KJM : vanId가 FACTORING일 때
					if(trxCapMap.isEquals("vanId", "FACTORING")){
						//거래 정보를 원복한다.
						updateMap.put("stlRate"		, trxCapMap.getDouble("stlRate")-trxCapMap.getDouble("stlLoanRate"));			//선정산 수수료 적용
						updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));		//KJM : 가맹점 수수료
						updateMap.put("stlFeeVat"	, calcVat(updateMap.getLong("stlFee")));										//KJM : 가맹점 수수료 VAT(부가가치세)
						updateMap.put("stlAgencyFee", 0);																			//KJM : 대리점 수수료		
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));	//KJM : 가맹점 정산금액
						updateMap.put("risk", risk);	//KJM : 리스크
						updateMap.put("capId", capId);	//KJM : 매입거래번호
						
						//KJM : 매입히스토리가 정상적으로 수정 되었을 때
						if(trxDAO.updateTrxCapDtlWithAgency(updateMap)){
							return "OK:"+capId+":RISK 설정 ,D+1 ,"+risk +",수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						//KJM : 수정 실패 시
						}else{
							return "NOK:"+capId+":RISK 설정 실패 DB 오류";
						}
					//KJM : vanId가 FACTORING이 아닐 때
					}else{
						//KJM : vanId가 OFFLINE일 때
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
						//KJM : 매입히스토리가 정상적으로 수정 되었을 때
						if(trxDAO.updateTrxCapDtl(updateMap)){
							return "OK:"+capId+":RISK 설정 ,D+1 ,"+risk +",수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						//KJM : 수정 실패 시
						}else{
							return "NOK:"+capId+":RISK 설정 실패 DB 오류";
						}
					}
				//KJM : 가맹점 정산 유형이 D+1이 아닐 때
				}else{
					updateMap.put("risk", risk);
					updateMap.put("capId", capId);
					//KJM : 매입 히스토리 수정 성공 시
					if(trxDAO.updateTrxCapDtlToRisk(updateMap)){
						return "OK:"+capId+":RISK 설정 ,"+trxCapMap.getString("stlType")+" ,"+risk +", 수수료변경없음";
					//KJM : 매입 히스토리 수정 실패 시
					}else{
						return "NOK:"+capId+":RISK 설정 실패 DB 오류";
					}
				}
			//KJM : 리스크 있는 거래일 때(리스크 변경)
			}else{	//리스크 관리 항목 변경 건 
				updateMap.put("risk", risk);
				updateMap.put("capId", capId);
				//KJM : 매입 히스토리 수정 성공 시
				if(trxDAO.updateTrxCapDtlToRisk(updateMap)){
					return "OK:"+capId+":RISK 변경 ,"+trxCapMap.getString("risk")+"->"+risk +", 수수료변경없음";
				//KJM : 매입 히스토리 수정 실패 시
				}else{
					return "NOK:"+capId+":RISK 설정 실패 DB 오류";
				}
			}
		//KJM : 정산상태가 정산대기 아닐 경우
		}else{
			return "NOK:"+capId+":정산대기가 아닌 거래는 변경할 수 없습니다.";
		}
		
	}
	
	//KJM : 리스크 해제
	public String setRiskToNormal(String capId){
		TrxCapDAO trxCapDAO = new TrxCapDAO();
		//KJM : 해당 매입번호에 대한 내역 가져옴
		RecordSet rset = trxCapDAO.getByCapId(capId);
		
		//KJM : 매입내역 없을 경우
		if(rset.size() == 0){
			return "NOK:"+capId+":검색된 거래가 없습니다.";
		}
		
		TrxDAO trxDAO = new TrxDAO();
		//KJM : 컬럼들 map형식으로 넣음
		SharedMap<String,Object> trxCapMap		= rset.getRow(0);
		
		//KJM : 해당 매입건의 정산상태가 정산대기이거나 정산보류일 때
		if(trxCapMap.getString("stlStatus").equals("정산대기") || trxCapMap.getString("stlStatus").equals("정산보류")){
			//KJM : 해당 매입건의 리스크 상태가 없을 때
			if(trxCapMap.isNullOrSpace("risk")){
				return "NOK:"+capId+":현재 RISK설정된 거래가 아닙니다.";
			//KJM : 해당 매입건의 리스크 상태가 있을 때
			}else{
				SharedMap<String,Object> updateMap	= new SharedMap<String,Object>();
				//KJM : 가맹점 정산 유형이 D+1일 때
				if(trxCapMap.getString("stlType").equals("D+1")){
					//KJM : vanId(처리사 아이디)가 FACTORING일 때 ???????????
					if(trxCapMap.isEquals("vanId", "FACTORING")){
						//거래 정보를 원복한다.
						updateMap.put("stlRate"			, trxCapMap.getDouble("stlRate")+trxCapMap.getDouble("stlLoanRate"));			//선정산 수수료 적용
						updateMap.put("stlFee"		, calcFee(trxCapMap.getLong("amount"), updateMap.getDouble("stlRate")));			//KJM : 가맹점 수수료
						updateMap.put("stlFeeVat"	, calcVat(updateMap.getLong("stlFee")));											//KJM : 가맹점 수수료 VAT
						updateMap.put("stlAgencyFee", calcFeeVat(trxCapMap.getLong("amount"),trxCapMap.getDouble("stlAgencyRate")));	//KJM : 대리점 수수료
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));//KJM : 가맹점 정산금액
						updateMap.put("risk", "");		//KJM : 리스크
						updateMap.put("capId", capId);	//KJM : 매입거래번호
						
						//KJM : 매입히스토리가 정상적으로 수정 되었을 때
						if(trxDAO.updateTrxCapDtlWithAgency(updateMap)){
							return "OK:"+capId+":RISK 해지 ,D+1 ,수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						//KJM : 수정 실패 시
						}else{
							return "NOK:"+capId+":RISK 해지 실패 DB 오류";
						}
					//KJM : vanId(처리사 아이디)가 FACTORING이 아닐 때
					}else{
						//KJM : vanId가 OFFLINE일 때
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
						updateMap.put("stlAmount"	, trxCapMap.getLong("amount")-updateMap.getLong("stlFee")-updateMap.getLong("stlFeeVat"));	//KJM : 가맹점 정산금액
						updateMap.put("risk", "");	//KJM : 리스크
						updateMap.put("capId", capId);	//KJM : 매입거래번호
						
						//KJM : 매입히스토리가 정상적으로 수정 되었을 때
						if(trxDAO.updateTrxCapDtl(updateMap)){
							return "OK:"+capId+":RISK 해지 ,D+1 ,수수료:"+(trxCapMap.getLong("stlFee")+trxCapMap.getLong("stlFeeVat")) +"->"+(updateMap.getLong("stlFee")+updateMap.getLong("stlFeeVat") );
						//KJM : 수정 실패 시
						}else{
							return "NOK:"+capId+":RISK 해지 실패 DB 오류";
						}
					}
				//KJM : 가맹점 정산유형이 D+1이 아닐 때
				}else{
					updateMap.put("risk", "");
					updateMap.put("capId", capId);
					//KJM : 매입히스토리가 정상적으로 수정 되었을 때
					if(trxDAO.updateTrxCapDtlToRisk(updateMap)){
						return "OK:"+capId+":RISK 해지 ,"+trxCapMap.getString("stlType")+" 수수료 변경없음";
					//KJM : 수정 실패 시
					}else{
						return "NOK:"+capId+":RISK 해지 실패 DB 오류";
					}
				}
				
			}
		//KJM : 해당 매입건의 정산상태가 정산대기이거나 정산보류가 아닐 때
		}else{
			return "NOK:"+capId+":정산대기가 아닌 거래는 변경할 수 없습니다.";
		}
		
	}
	
	//KJM : 금액, 수수료율이용해서 수수료구하기
	public long calcFee(long amount,double rate){
		//KJM : 수수료율을 문자패턴에 맞게 변환한 값 받음
		rate = rateFormat(rate);
		long decimal = 10000;
		//KJM : 금액이 0보다 작을 때
		if(amount < 0){
			//KJM : Math.round() : 입력값을 반올림한 수와 가장 가까운 정수 값 반환
			return -new Double(Math.round(-amount*(rate *decimal))).longValue()/decimal;
		//KJM : 금액이 0보다 클 때
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
	
	
	//KJM : 수수료율 정해진 문자패턴에 맞게 변환
	public double rateFormat(double rate){
		//KJM : 10진수, 빈자리는 채우지 않음
		String pattern = "#.#####";
		DecimalFormat format = new DecimalFormat(pattern);
		//KJM : rate가 1.23456789이라면 1.23457으로 변환
		//KJM : 변환된 값을 double형식으로 넘겨줌
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
