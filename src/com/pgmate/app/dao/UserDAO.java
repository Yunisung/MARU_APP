package com.pgmate.app.dao;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
//KJM : PG 관리자 로그인 계정
public class UserDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.UserDAO.class );
	private static final String TABLE = "VW_USER_PW";
	private static final String COLUMNS = "id,pw,name,grade,role,parentId,status,phone,regId,regDay,regDate,pwYn,pwStatus,pwRetry,pwDate, showOthTrns, eformStatus, loanSettleStatus";

	public UserDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(UserDAO.COLUMNS);
	}
	

	/**
	 * 210812_PYS : VW_USER_PW에서 id값이 같은걸 조회
	 * <pre>
	 * SELECT * FROM VW_USER_PW WHERE id = 'userId'
	 * </pre>
	 * @param userId
	 * @return
	 */
	public RecordSet getById(String userId){
		// kbr : addWhere (column , value, operator)
		addWhere("id",userId.toLowerCase(),eq);
		return search();
	}
	
	/**
	 * 210809_PYS : VW_USER 데이터 조회
	 * <pre>
	 * SELECT * FROM VW_USER
	 * </pre>
	 * @param userId : 안씀
	 * @return
	 */
	public RecordSet getParentsById(String userId){
		setTable("VW_USER");
		setColumns("distId, agencyId, id, pw, `name`, grade, role, parentId, `status`, phone, regId, regDay, regDate");
		return search();
	}

	
	/**
	 * 210812_PYS : 사용하는곳 없음
	 * @param datas
	 * @return
	 */
	public RecordSet search(List<Data> datas){
		CPUtil.setDAO(this, datas);			//DATA to CONDITION 
		return super.search();				//단일 검색
	}
	
	
	
	/**
	 * 210821_PYS : VW_USER에 있는 값들의 갯수
	 * @param datas
	 * @param page
	 * @return
	 */
	// KJM : SELECT * FROM VW_USER
	public RecordSet list(List<Data> datas,Page page){
		page = CPUtil.correctPage(page);
		//KJM : 테이블, 컬럼 세팅
		this.setTable("VW_USER");
		this.setColumns("distId, agencyId, id, pw, `name`, grade, role,showOthTrns, loanSettleStatus, parentId, `status`, phone, regId, regDay, regDate");
		//KJM : where절 세팅
		CPUtil.setDAO(this, datas);				//DATA to CONDITION 
		//KJM : DB접속, 조회 기능 수행
		return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색 
	}
	
	/**
	 * 218909_PYS : PG_USER_ACCESS에서 등록시간(regDate) 조회, 데이터가 없을때 현재시간 리턴
	 * <pre>
	 * SELECT regDate FROM PG_USER_ACCESS WHERE id = 'userId' LIMIT 1
	 * </pre>
	 * @param userId : 아이디
	 * @return
	 */
	public Timestamp getAccessById(String userId){
		String q = "SELECT regDate FROM PG_USER_ACCESS WHERE id = '"+userId+"' LIMIT 1";
		RecordSet rset = super.query(q);
		if(rset.size() > 0){
			return rset.getRow(0).getTimestamp("regDate");
		}else{
			return CommonUtil.getCurrentTimestamp();
		}
	}
	
	
	/**
	 * 210809_PYS : VW_USER_PW에서 pwStatus와 retry를 변경할때 사용
	 * <pre>
	 * UPDATE PG_USER_PW SET pwStatus ='pwStatus', pwRetry="retry" WHERE lower(id) = 'id.toLowerCase()'
	 * </pre>
	 * @param id : 아이디
	 * @param retry : 비밀번호 입력실패횟수
	 * @param pwStatus : 비밀번호상태(사용, 만료)
	 */
	public void updateUserPwRetry(String id,int retry,String pwStatus){
		String q = "UPDATE PG_USER_PW SET pwStatus ='"+pwStatus+"', pwRetry="+retry +" WHERE lower(id) = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	/**
	 * 210809_PYS : VW_USER_PW에서 retry를 변경할때 사용 
	 * <pre>
	 * UPDATE PG_USER_PW SET pwRetry="retry" WHERE lower(id) = 'id.toLowerCase()'
	 * </pre>
	 * @param id
	 * @param retry
	 */
	public void updateUserPwRetry(String id,int retry){
		String q = "UPDATE PG_USER_PW SET pwRetry="+retry +" WHERE lower(id) = '"+id.toLowerCase()+"'";
		super.update(q);
	}
	
	/** 
	 * 210809_PYS : ID 접속기록을 추가할때 사용
	 * <pre>
	 * INSERT INTO PG_USER_ACCESS (id,ipAddr,userAgent,regDay) VALUES ('id','ipAddr','CommonUtil.cut(userAgent,180)',DATE_FORMAT(now(),'%Y%m%d'))
	 * </pre>
	 * @param id : ID
	 * @param ipAddr : IP 주소
	 * @param userAgent : request.getHeader("User-Agent")
	 */
	public void insertUserAcess(String id,String ipAddr,String userAgent){
		String q = "INSERT INTO PG_USER_ACCESS (id,ipAddr,userAgent,regDay) VALUES ('"+id+"','"+ipAddr+"','"+CommonUtil.cut(userAgent,180)+"',DATE_FORMAT(now(),'%Y%m%d'))";
		super.update(q);
	}
	
	
//  20190624 지불 및 정산 없을 시, 조회
//	public List<SharedMap<String,Object>> getChildForDist(String status){
//		String q = "SELECT distId as id ,name,`status` FROM PG_MAM_DIST WHERE distId != '0'" ;
//		if(CommonUtil.isNullOrSpace(status)){
//			q+= " ORDER BY name asc";
//		}else{
//			q+= " AND status = '"+status+"' ORDER BY name asc";
//		} 
//		
//		return super.query(q).getRows();
//	}
	
	
	/**
	 * 210809_PYS : 지불 및 정산정보가 있는 대행사 조회
	 * <pre>
	 * SELECT A.distId as id ,A.name, A.status FROM PG_MAM_DIST A, PG_MAM_DIST_MNG B WHERE A.distId != '0' AND A.distId = B.distId ORDER BY A.name asc
	 * </pre>
	 * @param status : 대행사 상태 (예비, 사용, 중지, 폐기)
	 * @return
	 */
	// KBR : 로그인 업체의 바로 하위 업체 id , name, status 정보 
	public List<SharedMap<String,Object>> getChildForDist(String status){
		String q = "SELECT A.distId as id ,A.name, A.status FROM PG_MAM_DIST A, PG_MAM_DIST_MNG B WHERE A.distId != '0' AND A.distId = B.distId" ;
		
		if(CommonUtil.isNullOrSpace(status)){
			q+= " ORDER BY A.name asc";
		}else{
			q+= " AND A.status = '"+status+"' ORDER BY A.name asc";
		} 
		
		return super.query(q).getRows();
	}
	
	/**
	 * 210809_PYS : 같은 대행사ID를 쓰는 에이전시 조회
	 * <pre>
	 * SELECT agencyId as id ,name,`status` FROM PG_MAM_AGENCY WHERE distId ='대행사ID' AND agencyId !='0' AND status = '에이전시상태' ORDER BY name asc
	 * </pre>
	 * @param distId : 대행사ID
	 * @param status : 에이전시 상태 (예비, 사용, 중지, 폐기)
	 * @return
	 */
	public List<SharedMap<String,Object>> getChildForAgency(String distId,String status){
		String q = "SELECT agencyId as id ,name,`status` FROM PG_MAM_AGENCY WHERE distId ='"+distId+"' AND agencyId !='0'";
		//KJM : status가 null이거나 빈값일 경우
		if(CommonUtil.isNullOrSpace(status)){
			//KJM : name을 기준으로 오름차순 정렬
			q+= " ORDER BY name asc";
		}else{
			//KJM : 상태값 조건과 정렬문 붙여줌
			q+= " AND status = '"+status+"' ORDER BY name asc";
		} 
		return super.query(q).getRows();
	}
	
	/**
	 * 210809_PYS : 같은 에이전시ID를 쓰는 지사 조회
	 * <pre>
	 * SELECT salesId as id ,name,`status` FROM PG_MAM_SALES WHERE agencyId ='에이전시ID' AND salesId !='0' ORDER BY name asc
	 * </pre>
	 * @param agencyId : 에이전시ID
	 * @param status : 지사 상태 (예비, 사용, 중지, 폐기)
	 * @return
	 */
	public List<SharedMap<String,Object>> getChildForSales(String agencyId,String status){
		String q = "SELECT salesId as id ,name,`status` FROM PG_MAM_SALES WHERE agencyId ='"+agencyId+"' AND salesId !='0'";
		//KJM : 상태값이 null이거나 빈값일 경우
		if(CommonUtil.isNullOrSpace(status)){
			//KJM : 이름을 기준으로 오름차순 정렬
			q+= " ORDER BY name asc";
		}else{
			//KJM : 상태값 조건과 정렬문 추가
			q+= " AND status = '"+status+"' ORDER BY name asc";
		}
		//KJM : select 쿼리문 수행 후 결과 리스트 반환
		return super.query(q).getRows();
	}
	
	/**
	 * 210809_PYS : 대행사 이름 조회
	 * <pre>
	 * SELECT name FROM PG_MAM_DIST WHERE distId ='대행사ID'
	 * </pre>
	 * @param distId : 대행사ID
	 * @return
	 */
	public String getDistName(String distId){
		String q = "SELECT name FROM PG_MAM_DIST WHERE distId ='"+distId+"'";
		return super.query(q).getRow(0).getString("name");
	}
	
	/**
	 * 210809_PYS : 에이전시 이름 조회
	 * <pre>
	 * SELECT name FROM PG_MAM_AGENCY WHERE agencyId ='에이전시ID'
	 * </pre>
	 * @param agencyId : 에이전시 ID
	 * @return
	 */
	public String getAgencyName(String agencyId){
		String q = "SELECT name FROM PG_MAM_AGENCY WHERE agencyId ='"+agencyId+"'";
		return super.query(q).getRow(0).getString("name");
	}
	
	
	
	
	
	

}