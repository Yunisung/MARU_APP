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
public class MchtDiffDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.MchtDiffDAO.class );
	
	private static final String TABLE = "PG_MCHT_DIFF_DOWNLOAD"; // 카드사 영중소 가맹점 등록 결과 ( 데이터 없음)
	private static final String COLUMNS = "*";
	
	public MchtDiffDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(MchtDiffDAO.COLUMNS);
	}
	
	
	public RecordSet getDiffUpload(String mchtId){
		// KBR : 영중소 보냈을 때 업데이트 되는 테이블
		super.setTable("PG_MCHT_DIFF_UPLOAD");
		super.setColumns("*");
		super.addWhere("mchtId",mchtId.toLowerCase(),eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	public RecordSet getMcht(String mchtId) {
		super.setTable("PG_MCHT A LEFT JOIN PG_MCHT_TAX B ON A.mchtId = B.mchtId");
		super.setColumns("A.mchtId, A.nick AS mchtName, FN_AES_DEC(A.identity) AS mchtCompNo, A.bizType, A.zip, A.addr1, A.addr2, replace(A.tel1,'-','') AS mchtPhone, A.ceoName AS mchtCeo, B.email AS mchtEmail");
		super.addWhere("A.mchtId",mchtId,eq);
		super.setOrderBy("A.regDate desc");
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
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
	
	// KBR : maru_app 사용 안함 
	public boolean insertDiffUpload(SharedMap<String, Object> mchtMap) {
		String query = "INSERT INTO `PG_MCHT_DIFF_UPLOAD` (`mchtId`, `mchtCompNo`, `bizType`, `mchtName`, `mchtAddr`, `mchtCeo`, `mchtPhone`, `mchtEmail`, `regDay`) VALUES "
				     + "('"+mchtMap.getString("mchtId")+"', '"+mchtMap.getString("mchtCompNo")+"', '"+mchtMap.getString("bizType")+"', '"+mchtMap.getString("mchtName")+"', '"+mchtMap.getString("addr1")+mchtMap.getString("addr2")+"', '"+mchtMap.getString("mchtCeo")+"', '"+mchtMap.getString("mchtPhone")+"', '"+mchtMap.getString("mchtEmail")+"', '"+mchtMap.getString("regDay")+"')";
		
		return new CPDAO().update(query); 
	}


	public RecordSet getDiffDownload(String mchtId) {
		super.setTable("PG_MCHT_DIFF_DOWNLOAD");
		super.setColumns("*");
		super.addWhere("mchtId",mchtId, eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}


	public RecordSet getDiffType(String mchtId) {
		
		// identity값 
		String bizno = getMchtBizNo(mchtId);
		
		super.setTable("PG_BIZNO"); // 영중소 사업자 번호 리스트 테이블
		super.setColumns("mchtType"); // mchtType == 영주소 구분 칼럼
		super.addWhere("bizno",bizno,eq); // bizno == 사업자번호 칼럼 
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	// 영중소 가맹점 확인 메소드
	public RecordSet getDiffTypeByIdentity(String identity) {
		super.setTable("PG_BIZNO"); // 영중소 사업자 번호 리스트 테이블
		super.setColumns("mchtType");
		super.addWhere("bizno",identity,eq);
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}

	// mchtID(가맹점ID) 의 identity 값 return
	private String getMchtBizNo(String mchtId) {
		
		super.setTable("PG_MCHT");
		super.setColumns("FN_AES_DEC(identity) AS identity");
		super.addWhere("mchtId",mchtId,eq);
		
		RecordSet rset = super.search();
		
		super.initRecord();
		
		return rset.getRowFirst().getString("identity");
		
	}
	

	
} 