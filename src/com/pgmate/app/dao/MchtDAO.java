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
//KJM : 가맹점 정보
public class MchtDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtDAO.class );
	private static final String TABLE = "VW_MCHT"; // 가맹점 정보 view
	/**
	 * 210809_PYS : VW_MCHT를 확인해보니 * 을 써도 동일한 결과가 나옴 ( KBR : 암호화 차이)
	 */
	private static final String COLUMNS = "mchtId,name,nick,status,bizType,bizCategory,"
			+ "distId,agencyId,salesId,idType,FN_MASK_IDENTIFY(identity) as identity,tel1,"
			+ "tel2,fax,zip,addr1,addr2,lat,lng,ceoName,FN_MASK_IDENTIFY(ceoIdentity) as "
			+ "ceoIdentity,ceoPhone,ceoTel,ceoZip,ceoAddr1,ceoAddr2,managerName,managerPhone,"
			+ "deposit,regId,regDay,regDate,salesName,agencyName,"
			+ "distName,aggregator,FN_AES_DEC(identity) AS decIdentity";
	
	
	// KBR : 생성과 동시에 테이블,컬럼 셋팅
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
		
		// KBR : 페이지 값 예외처리
		page = CPUtil.correctPage(page);
		// KBR : sql 문 셋팅
		CPUtil.setDAO(this, datas);				//DATA to CONDITION
		
		return super.searchList(page.current, page.size,page.hash);	//LIST AND PAGING 검색
		
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
	
	// KBR : 리스트 갯수 조회  
	public RecordSet exList(List<Data> datas,Page page){
		
		// KBR : 상태가 사용인것 리스트 
		super.setTable("(SELECT A.*, B.diffType,B.settleType, B.rate, B.loanRate, B.distRate,B.agencyRate,B.salesRate,B.limitOnce, C.bankName, C.account, C.accntHolder " + 
						"FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용') T");
		super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as  sumRate");
		// KBR : page값 조건에 맞게 셋팅
		page = CPUtil.correctPage(page);
		// KBR : sql문 완성 시키는 메소드
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
	
	public RecordSet getIdByType(String id) {
		
		if(id.contains("D")) {
			super.setTable("PG_MCHT");
			super.setColumns("distId");
			super.addWhere("lower(mchtId)",id.substring(0, id.length()-1),eq);
			
		} else if(id.contains("A")) {
			super.setTable("PG_MCHT");
			super.setColumns("agencyId");
			super.addWhere("lower(mchtId)",id.substring(0, id.length()-1),eq);
			
		} else if(id.contains("S")) {
			super.setTable("PG_MCHT");
			super.setColumns("salesId");
			super.addWhere("lower(mchtId)",id.substring(0, id.length()-1),eq);
			
		} else if(id.contains("M")) {
			super.setTable("PG_MCHT");
			super.setColumns("mchtId");
			super.addWhere("lower(mchtId)",id.substring(0, id.length()-1),eq);
			
		} else if(id.contains("T")) {
			super.setTable("PG_MCHT_TMN");
			super.setColumns("tmnId");
			super.addWhere("lower(mchtId)",id.substring(0, id.length()-1),eq);
		}
		
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
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
}