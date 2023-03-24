package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * @author Administrator
 *
 */

public class MchtMngDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtMngDAO.class );
	private static final String TABLE = "PG_MCHT_MNG";
	private static final String COLUMNS = "*";
	
	public MchtMngDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtMngDAO.COLUMNS);
	}
	
	public RecordSet getById(String mchtId){
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
	
	public RecordSet getHtById(String mchtId){
		// KBR : 가맹점 지불 및 정산 히스토리 테이블
//		setTable("HT_MCHT_MNG");
//		setColumns("*");
//		addWhere("lower(mchtId)",mchtId.toLowerCase(),eq);
//		return search();
		super.setTable("(SELECT * FROM  HT_MCHT_TMN where mchtId ='" + mchtId + "' " +
				"UNION ALL " +
				"SELECT '' AS idx, tmnId, mchtId, taxId, status, serial, payKey, activeDate, apiMaxInstall" +
				", webPay, appDirect, semiAuth" +
				", refundType, van, vanIdx, ccType, description, minAmount, payLimit, limitAmount, limitStartTime" +
				", limitEndTime, regId, regDay, regDate, '' as summary " +
				"FROM PG_MCHT_TMN WHERE mchtId ='" + mchtId + "') AS HT_TMN " +
				"LEFT OUTER JOIN PG_MCHT_TMN ON HT_TMN.tmnId = PG_MCHT_TMN.tmnId ");
		super.setColumns("HT_TMN.*, PG_MCHT_TMN.tmnId AS originId, PG_MCHT_TMN.regDate AS activeDate ");
		super.setOrderBy("PG_MCHT_TMN.regDate DESC, PG_MCHT_TMN.tmnId DESC, HT_TMN.regDate DESC ");
		return super.search();
	}

	public void updateLoanStatus(String mchtId){
		String q = "UPDATE PG_MCHT_MNG SET loanSettleStatus = '사용' WHERE mchtId='"+mchtId+"'";
		super.update(q);
	}
	
	public void updateDiffType(String diffType, String mchtId){
		String q = "UPDATE PG_MCHT_MNG SET diffType = '"+diffType+"' WHERE mchtId='"+mchtId+"'";
		super.update(q);
		super.setDebug(true);
	}
}