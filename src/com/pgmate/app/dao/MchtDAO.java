package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class MchtDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtDAO.class );
	private static final String TABLE = "VW_MCHT";
	private static final String COLUMNS = "mchtId,name,nick,status,bizType,bizCategory,distId,agencyId,salesId,idType,FN_MASK_IDENTIFY(identity) as identity,tel1,tel2,fax,zip,addr1,addr2,lat,lng,ceoName,FN_MASK_IDENTIFY(ceoIdentity) as ceoIdentity,ceoPhone,ceoTel,ceoZip,ceoAddr1,ceoAddr2,managerName,managerPhone,deposit,regId,regDay,regDate,salesName,agencyName,distName,aggregator,FN_AES_DEC(identity) AS decIdentity, mchtActiveDate";

	public MchtDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtDAO.COLUMNS);
	}
	
	public RecordSet getById(String mchtId){
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getMchtPwById(String mchtId){
		super.setTable("VW_MCHT_PW");
		super.setDebug(CPUtil.CP_DEBUG);
		super.setColumns("mchtId,mchtId as parentId,'가맹점' as grade, '일반' as role,name,status,regId,regDay,regDate,pwYn,pwStatus,pwRetry,pwDate");
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/*public RecordSet nonTranList(List<Data> datas,Page page){
		super.setTable("VW_TOT_CAP A LEFT JOIN VW_MCHT_NOT_DEPOSIT B ON A.mchtId = B.mchtId");
		super.setColumns("MAX(A.capDay) as lastCapDay, TO_DAYS(now()) - TO_DAYS(MAX(A.capDay)) AS period, B.*, FN_AES_DEC(B.identity) AS decIdentity");
		super.setWhere("A.saleCount > 0 AND A.capDay < DATE_FORMAT(date_add(now(), interval -1 month), '%Y%m%d')");
		super.setGroupBy("A.mchtId");
		super.setOrderBy("MAX(A.capDay) asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}*/
	public RecordSet nonTranList(List<Data> datas,Page page){
		super.setDebug(true);
		super.setTable("PG_TOT_CAP_CAPDAY A LEFT JOIN VW_MCHT_NOT_DEPOSIT B ON A.mchtId = B.mchtId");
		super.setColumns(" MAX(A.capDay) as lastCapDay, TO_DAYS(now()) - TO_DAYS(MAX(A.capDay)) AS period, B.*, FN_AES_DEC(B.identity) AS decIdentity");
		super.setWhere("A.payCnt > 0 AND A.capDay < DATE_FORMAT(date_add(now(), interval -1 month), '%Y%m%d')");
		super.setGroupBy("A.mchtId");
		super.setOrderBy("MAX(A.capDay) asc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	public RecordSet accntSeachList(List<Data> datas,Page page){
		super.setTable("PG_BANK_DATA");
		super.setColumns("idx, sendDate, custName as name, custBirthDay as birthday, custMobileNo as mobileno, custAccntNo as accntno");
		super.setOrderBy("idx desc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	public RecordSet exList(List<Data> datas,Page page){
//		super.setDebug(true);
//		super.setTable("( SELECT A.*, B.distNum, B.agencyNum, B.salesNum, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate,B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
//						"D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval, IF(D.authType = '0', '미사용', IF(D.authType = '1', 'API인증', IF(D.authType = '2', '통합인증', '미사용')))as authType	, 	if(E.payType IS NOT NULL, if(E.payType = 'KAKAO', '카카오페이', if(E.payType = 'NAVER', '네이버페이',IF(E.payType = 'PAYALL','카카오&네이버 페이',''))), '미사용') AS simpleType " +
//						"FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId LEFT JOIN PG_MCHT_SIMPLE_MNG E ON A.mchtId = E.mchtId) T");
//		super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as  sumRate");
		super.setTable("( SELECT A.*, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate,B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
				"D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval,IF(D.authType = '0', '미사용', IF(D.authType = '1', 'API인증', IF(D.authType = '2', '통합인증', '미사용')))as authType " +
				"FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId ) T");
		super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as sumRate");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public void updateMchtPwRetry(String id,int retry,String pwStatus){
		String q = "UPDATE PG_MCHT_PW SET pwStatus ='"+pwStatus+", pwRetry="+retry +" WHERE mchtId = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	public void updateMchtPwRetry(String id,int retry){
		String q = "UPDATE PG_MCHT_PW SET pwRetry="+retry +" WHERE mchtId = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	public RecordSet getHtById(String mchtId){
		setTable("HT_MCHT");
		setColumns("*");
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getPgById(String mchtId){
		setTable("PG_MCHT");
		setColumns("*");
		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getWebHook(List<Data> datas,Page page){
		super.setTable("VW_MCHT_WEBHOOK");
		super.setColumns("*");
		super.setOrderBy("idx desc");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet getWebHookByIdx(String idx){
		super.setTable("PG_MCHT_WEBHOOK");
		super.setColumns("*");
		addWhere("idx",idx.toLowerCase(),eq);
		return search();
	}
	
	//가맹점 정산내역 이메일 전송 =======================================================
	/*
	 * 가맹점 정산내역 LIST
	 */
	public List<SharedMap<String,Object>> getListForSend(String mchtId, String startDay, String endDay){
		String q = "SELECT startDay, stlDay, (payAmt+rfdAmt-holdAmt) AS amt, stlAmount FROM VW_SETTLE_MCHT "
				+ "WHERE stlDay >= '"+startDay+"' AND stlDay <= '"+endDay+"' AND mchtId = '"+mchtId.toLowerCase()+"' ORDER BY stlDay asc ";
		
		RecordSet rset = super.query(q);
		super.initRecord();
		return rset.getRows();
	}
	
	/*
	 * 가맹점 정산 정보 SELECT
	 */
	public RecordSet getIdForSend(String mchtId){
		super.setTable("PG_MCHT_TAX A JOIN PG_MCHT B ON A.mchtId = B.mchtId ");
		super.setColumns("A.mchtId, A.bankName, A.account, A.accntHolder, B.nick, B.idType, FN_AES_DEC(B.identity) AS identity");
		super.addWhere("A.mchtId", mchtId);
		super.setOrderBy("");	
		
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	/*
	 * 이메일 수신거부 처리
	 */
	public void updateEmailConfirm(String id){
		String q = "UPDATE PG_MCHT_SVC SET emailStatus = '미사용' WHERE mchtId = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	//가맹점 정산내역 이메일 전송 =======================================================
	
	public RecordSet mngList(List<Data> datas,Page page){
//		super.setDebug(true);
		super.setTable("( SELECT A.*, B.distNum, B.agencyNum, B.salesNum, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate,B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
						"D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval		" +
						"FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId) T");
		super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as  sumRate");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet mngVactList(List<Data> datas,Page page){
//		super.setDebug(true);
		super.setTable("( SELECT A.*, D.distNum, D.agencyNum, D.salesNum, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate,B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
						"D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval		" +
						"FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId) T");
		super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as  sumRate");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet mngSimpleList(List<Data> datas,Page page){
//		super.setDebug(true);
		super.setTable("( SELECT A.*, E.distNum, E.agencyNum, E.salesNum, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate,B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
						"D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval		" +
						"FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId LEFT JOIN PG_MCHT_SIMPLE_MNG E ON A.mchtId = E.mchtId) T");
		super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as  sumRate");
		page = CPUtil.correctPage(page);
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	public RecordSet getTaxData(String taxId){
		super.setTable("PG_MCHT_TAX");
		super.setColumns("*");
		addWhere("taxId",taxId,eq);
		
		return search();
	}
	
	/*
	 * 간편결제 미사용시 상태 중지 처리
	 */
	public void updateSimpleStatus(String id){
		String q = "UPDATE PG_MCHT_SIMPLE_MNG SET payStatus = '중지' WHERE mchtId = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	/*
	 * 간편결제 미사용시 상태 중지 처리
	 */
	public void updateSimpleSvc(String id){
		String q = "UPDATE PG_MCHT_SVC SET card3D = '미사용' WHERE mchtId = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
}