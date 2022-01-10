package com.pgmate.app.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.RandomUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

public class EformDAO extends DAO {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.EformDAO.class );
	private static final String TABLE = "PG_EFORM";
	private static final String COLUMNS = "*";
	
	public EformDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(EformDAO.COLUMNS);
	}
	
	//전자계약서 전송내역 DB INSERT
	public boolean insert(SharedMap<String, Object> map) {
		super.setTable("PG_EFORM");
	
		super.setRecord("idx", RandomUtil.getRandomString("E", 2));
		super.setRecord("id", map.get("id"));
		super.setRecord("name", map.get("name"));
		super.setRecord("grade", map.get("grade"));
		super.setRecord("doc_name", map.get("doc_name"));
		super.setRecord("receiver_meta_id", map.get("receiver_meta_id"));
		super.setRecord("receiver_name", map.get("receiver_name"));
		super.setRecord("receiver_mobile", map.get("receiver_mobile"));
		super.setRecord("receiver_email", map.get("receiver_email"));
		//super.setRecord("receiver_password", map.get("receiver_password"));
		super.setRecord("send_type", map.get("send_type"));
		//super.setRecord("password", map.get("password"));
		super.setRecord("expiration_date", map.get("expiration_date"));
		super.setRecord("items", map.get("items"));
		super.setRecord("auth_phone", map.get("auth_phone"));
		super.setRecord("title", map.get("title"));
		super.setRecord("mail_title", map.get("mail_title"));
		super.setRecord("mail_content", map.get("mail_content"));
		super.setRecord("form_id", map.get("form_id"));
		super.setRecord("regId", map.get("regId"));
		super.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		super.setRecord("regTime", CommonUtil.getCurrentDate("HHmmss"));
		
		boolean inserted = super.insert();
		super.initRecord();
		return inserted;
	}
	
	//전자계약소 발송 > 해당 userId에 저장된 계약서 조회
	public RecordSet getEformById(String id){
		super.setTable("PG_EFORM_AUTH");
		super.setColumns("*");
		super.addWhere("lower(id)",id,eq);
		
		RecordSet rset = super.search();
		super.initRecord();
		return rset;
	}
	
	//전자계약서 권한 조회 : eformStatus : '사용' userId 전체 조회
	public RecordSet authList(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("PG_USER");
		super.setColumns("*");
		super.addWhere("eformStatus","사용");
		super.setOrderBy("regDate desc");

		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
	}
	
	//전자계약서 내역 조회 : 전송 내역 전체 조회
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		
		super.setTable("PG_EFORM");
		super.setColumns("*");
		super.setOrderBy("regDate desc");
		
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
	}
	
	//전자계약서 내역 조회 > 상세 보기. modal
	public RecordSet getDetail(String idx){
		super.setTable("PG_EFORM");
		super.setColumns("*");
		super.addWhere("idx",idx,eq);
		
		return search();
	}

	//해당 userId 앞으로 계약서 저장
	public void authInsert(SharedMap<String, Object> map) {
		super.setTable("PG_EFORM_AUTH");
		super.setRecord("idx", RandomUtil.getRandomString("A", 2));
		super.setRecord("id", map.get("id"));
		super.setRecord("name", map.get("name"));
		super.setRecord("grade", map.get("grade"));
		super.setRecord("parentId", map.get("parentId"));
		super.setRecord("form_id", map.get("form_id"));
		super.setRecord("doc_name", map.get("doc_name"));
		super.setRecord("regId", map.get("regId"));
		
		super.insert();
	}
	
	//전자계약서 사용 여부 수정 (사용/중지)
	public void authModify(String eformStatus, String id) {
		super.setTable("PG_USER");
		super.setRecord("eformStatus", eformStatus);
		super.addWhere("id", id);
		
		super.update();
	}
	
	///해당 userId 앞으로 저장된 계약서 개별 삭제
	public void authDelete(String idx) {
		super.setTable("PG_EFORM_AUTH");
		super.addWhere("idx", idx);
		
		super.delete();
	}
	
	//전자계약 상태 `중지` 변경시, 해당 id로 저장된 계약서  일괄 삭제
	public void listDelete(String id) {
		super.setTable("PG_EFORM_AUTH");
		super.addWhere("lower(id)",id,eq);
		
		super.delete();
	}
	
	//대행사 중지시 하위 업체 계약서 일괄 delete
	public void listDeleteBelow(String parentId) {
		super.setTable("PG_EFORM_AUTH");
		super.addWhere("parentId",parentId,eq);
		
		super.delete();
	}
	
	//대행사 상태 변경시 하위 업체 eformStatus 일괄 변경
	public void updateBelow(String eformStatus, String parentId) {
		super.setTable("PG_USER");
		super.setRecord("eformStatus", eformStatus);
		super.addWhere("parentId",parentId,eq);
		
		super.update();
	}
	
	//업체 아이디로 로그인시, 전송 내역 확인.
	public RecordSet memList(List<Data> datas, Page page, String id){
		page = CPUtil.correctPage(page);
		
		super.setTable("PG_EFORM");
		super.setColumns("*");
		super.addWhere("lower(id)",id,eq);
		super.setOrderBy("regDate desc");
		
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
	}
	
	//대행사 하위 업체 check
	public List<SharedMap<String,Object>> checkId(String parentId) {
		String q = "SELECT * FROM VW_MAM_SALES WHERE distId = '"+parentId+"'";
		
		RecordSet rset = super.query(q);
		super.initRecord();
		
		return rset.getRows();
	}
	
	//대행사 하위 업체 check
	public String checkDistId(String parentId, String grade) {
		String q = "";
		
		if("에이전시".equals(grade)) {
			q = "SELECT distId FROM VW_MAM_SALES WHERE agencyId = '"+parentId+"'";
			
		} else if("지사".equals(grade)) {
			q = "SELECT distId FROM VW_MAM_SALES WHERE salesId = '"+parentId+"'";
		}
		
		return super.query(q).getRow(0).getString("distId");
	}
	
	//체크할 대행사의 eformStatus 사용 여부 체크
	public String checkStatus(String distId) {
		String q = "select eformStatus from PG_USER WHERE parentId = '"+distId+"'";
		
		return super.query(q).getRow(0).getString("eformStatus");
	}
}
