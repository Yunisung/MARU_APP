package com.pgmate.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * @author Administrator
 *
 */
public class HTDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.HTDAO.class );
	

	public HTDAO() {
		super.setOrderBy("idx desc");
	}
	

	/**
	 * pys : 관리자 로그인계정 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_USER
	 * WHERE id = 'userId'
	 * </pre>
	 * @param userId : 아이디
	 * @return
	 */
	public RecordSet getUserById(String userId){
		super.setTable("HT_USER");
		addWhere("id",userId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 관리자 로그인계정 조회(원하는 갯수만큼)
	 * <pre>
	 * SELECT *
	 * FROM HT_USER
	 * WHERE id = 'userId'
	 * </pre>
	 * @param userId : 아이디
	 * @param size : 표시수
	 * @return
	 */
	public RecordSet getUserById(String userId,long size){
		super.setTable("HT_USER");
		addWhere("id",userId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 에이전시 정보 조회
	 * <pre>
	 * SELECT * 
	 * FROM HT_MAM_AGENCY
	 * WHERE agencyId = 'agencyId'
	 * </pre>
	 * @param agencyId : 에이전시ID
	 * @return
	 */
	public RecordSet getMamByAgencyId(String agencyId){
		super.setTable("HT_MAM_AGENCY");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return search();
	}
	
	/** 
	 * pys : 에이전서 정보 조회(원하는 만큼)
	 * <pre>
	 * SELECT * 
	 * FROM HT_MAM_AGENCY
	 * WHERE agencyId = 'agencyId'
	 * @param agencyId : 에이전시ID
	 * @param size : 갯수
	 * @return
	 */
	public RecordSet getMamByAgencyId(String agencyId,long size){
		super.setTable("HT_MAM_AGENCY");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/** 
	 * pys : 에이전시 정산 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_AGENCY_MNG
	 * WHERE agencyId = 'agencyId'
	 * </pre>
	 * @param agencyId : 에이전시ID
	 * @return
	 */
	public RecordSet getMamMngByAgencyId(String agencyId){
		super.setTable("HT_MAM_AGENCY_MNG");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 에이전시 정산 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_AGENCY_MNG
	 * WHERE agencyId = 'agencyId'
	 * </pre>
	 * @param agencyId : 에이전시ID
	 * @param size : 갯수
	 * @return
	 */
	public RecordSet getMamMngByAgencyId(String agencyId,long size){
		super.setTable("HT_MAM_AGENCY_MNG");
		addWhere("agencyId",agencyId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 대행사 정보 히스토리 조회
	 * <pre>
	 * SELECT * 
	 * FROM HT_MAM_DIST
	 * WHERE distId = 'distId'
	 * </pre>
	 * @param distId : 대행사ID
	 * @return
	 */
	public RecordSet getMamByDistId(String distId){
		super.setTable("HT_MAM_DIST");
		addWhere("distId",distId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 대행사 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_DIST
	 * WHERE distId = 'distId'
	 * </pre>
	 * @param distId : 대행사ID
	 * @param size : 갯수
	 * @return
	 */
	public RecordSet getMamByDistId(String distId,long size){
		super.setTable("HT_MAM_DIST");
		addWhere("distId",distId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 대행사 정산 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_DIST_MNG
	 * WHERE distId = 'distId'
	 * </pre>
	 * @param distId : 대행사ID
	 * @return
	 */
	public RecordSet getMamMngByDistId(String distId){
		super.setTable("HT_MAM_DIST_MNG");
		addWhere("distId",distId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 대행사 정산 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_DIST_MNG
	 * WHERE distId = 'distId'
	 * </pre>
	 * @param distId : 대행사ID
	 * @param size
	 * @return
	 */
	public RecordSet getMamMngByDistId(String distId,long size){
		super.setTable("HT_MAM_DIST_MNG");
		addWhere("distId",distId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 지사 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_SALES
	 * WHERE salesId = 'salesId'
	 * </pre>
	 * @param salesId : 지사ID
	 * @return
	 */
	public RecordSet getMamBySalesId(String salesId){
		super.setTable("HT_MAM_SALES");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 지사 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_SALES
	 * WHERE salesId = 'salesId'
	 * </pre>
	 * @param salesId : 지사ID
	 * @param size
	 * @return
	 */
	public RecordSet getMamBySalesId(String salesId,long size){
		super.setTable("HT_MAM_SALES");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 지사 정산 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_SALES_MNG
	 * WHERE salesId = 'salesId'
	 * </pre>
	 * @param salesId : 지사ID
	 * @return
	 */
	public RecordSet getMamMngBySalesId(String salesId){
		super.setTable("HT_MAM_SALES_MNG");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 지사 정산 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MAM_SALES_MNG
	 * WHERE salesId = 'salesId'
	 * </pre>
	 * @param salesId : 지사ID 
	 * @param size : 갯수
	 * @return
	 */
	public RecordSet getMamMngBySalesId(String salesId,long size){
		super.setTable("HT_MAM_SALES_MNG");
		addWhere("salesId",salesId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 가맹점 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT
	 * WHERE mchtId = 'mchtId'
	 * </pre> 
	 * @param mchtId : 가맹점ID
	 * @return
	 */
	public RecordSet getMchtByMchtId(String mchtId){
		super.setTable("HT_MCHT");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 가맹점 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @param size : 갯수
	 * @return
	 */
	public RecordSet getMchtByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 가맹점 정산정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT_MNG
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @return
	 */
	public RecordSet getMchtMngByMchtId(String mchtId){
		super.setTable("HT_MCHT_MNG");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	/** 
	 * pys : 가맹점 정산정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT_MNG
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @param size
	 * @return
	 */
	public RecordSet getMchtMngByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT_MNG");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 가맹점 TAX 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT_TAX
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @return
	 */
	public RecordSet getMchtTaxByMchtId(String mchtId){
		super.setTable("HT_MCHT_TAX");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 가맹점 TAX 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT_TAX
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @param size 
	 * @return
	 */
	public RecordSet getMchtTaxByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT_TAX");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	/**
	 * pys : 가맹점 터미널 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT_TMN
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @return
	 */
	public RecordSet getMchtTmnByMchtId(String mchtId){
		super.setTable("HT_MCHT_TMN");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * pys : 가맹점 터미널 정보 히스토리 조회
	 * <pre>
	 * SELECT *
	 * FROM HT_MCHT_TMN
	 * WHERE mchtId = 'mchtId'
	 * </pre>
	 * @param mchtId : 가맹점ID
	 * @param size 
	 * @return
	 */
	public RecordSet getMchtTmnByMchtId(String mchtId,long size){
		super.setTable("HT_MCHT_TMN");
		addWhere("mchtId",mchtId.toLowerCase(),eq);
		return searchList(1,size,"");
	}
	
	

}