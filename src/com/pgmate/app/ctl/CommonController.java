package com.pgmate.app.ctl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgmate.app.dao.*;
import com.pgmate.app.util.SQLInjectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class CommonController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.CommonController.class );
	
	@RequestMapping(value = {"/common/changeIdentity"})
    public ModelAndView changeIdentity(HttpServletRequest request) {
		return new ModelAndView("/common/changeIdentity");
    }
	
	// KBR 헤더 디버그 클릭 시 
	@RequestMapping(value = {"/common/debug"}, method = RequestMethod.GET)
    public @ResponseBody String debug(HttpServletRequest request) {
		if(CPUtil.CP_DEBUG){
			CPUtil.CP_DEBUG = false;
			SessionUtil.setAttribute(request, "CP_DEBUG",false);
			logger.debug("DEBUG : {}", CPUtil.CP_DEBUG);
			return "DEBUG 중지처리되었습니다.";
		}else{
			CPUtil.CP_DEBUG = true;
			SessionUtil.setAttribute(request, "CP_DEBUG",true);
			return "DEBUG 가 시작되었습니다.";
		}
	}
	
	// ======================================================= 수수료 변경 예약

	@RequestMapping(value = {"/member/vactRate/add/{mchtId}"})
	public ModelAndView vactRateAdd(HttpServletRequest request, @PathVariable String mchtId) {
		SharedMap<String,Object> result = null;
		result = new MchtDAO().getById(mchtId).getRowFirst();
		request.setAttribute("VACTMAP",new MchtVactDAO().getByMchtId(mchtId));
		request.setAttribute("PARENTID", mchtId);
		return new ModelAndView("/member/vactRate/add", "DATAMAP", result);
	}

	@RequestMapping(value = {"/member/vactRate/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse vactInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insert("PG_RESERVE_VACT_RATE", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
					.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
					.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
					.cpResponse();
		}
	}

	@RequestMapping(value = "/member/vactRate/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView vactList(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		ReserveVactRateDAO reserveVactRateDAO = new ReserveVactRateDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		//KJM : 상태 조건 안줬을 때
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			//KJM : 기본 상태 조회 조건 세팅
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		RecordSet rset = reserveVactRateDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, reserveVactRateDAO).setView(request,"/member/vactRate/list","");
	}

	@RequestMapping(value = "/member/vactRate/modify/{idx}", method = RequestMethod.GET)
	public ModelAndView vactModify(HttpServletRequest request, @PathVariable String idx) {
		return new ModelAndView("/member/vactRate/modify", "DATAMAP", new ReserveVactRateDAO().getByIdx(idx).getRowFirst());
	}

	@RequestMapping(value = {"/member/vactRate/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse vactUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if(cpDAO.updateByOper("PG_RESERVE_VACT_RATE", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK("수수료 예약 정보가 변경되었습니다.")
					.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
					.resultNOK("수수료 예약 정보 변경에 실패하였습니다.",cpDAO.getError())
					.cpResponse();
		}
	}

	//KJM : 멤버관리 > 수수료 변경 예약 리스트
	@RequestMapping(value = "/member/rate/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response,@RequestBody CPRequest cpRequest) {
		ReserveRateDAO  reserveRateDAO = new ReserveRateDAO();
		SessionUtil.setSearchGrade(request, cpRequest);
		//KJM : 상태 조건 안줬을 때
		if(CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))){
			//KJM : 기본 상태 조회 조건 세팅
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		RecordSet rset = reserveRateDAO.list(cpRequest.data,cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, reserveRateDAO).setView(request,"/member/rate/list","");
	}
	
	@RequestMapping(value = {"/member/rate/add/{grade}/{parentId}"})
    public ModelAndView rateAdd(HttpServletRequest request, @PathVariable String grade, @PathVariable String parentId) {
		SharedMap<String,Object> result = null;
		if(grade.equals("mcht")) {
			result = new MchtDAO().getById(parentId).getRowFirst();
			request.setAttribute("MCHTMAP",new MchtMngDAO().getById(parentId).getRowFirst());
			grade = "가맹점";
		} else if(grade.equals("agency")) {
			result = new AgencyDAO().getById(parentId).getRowFirst();
			grade = "에이전시";
		} else if(grade.equals("dist")) {
			result = new DistDAO().getById(parentId).getRowFirst();
			grade = "대행사";
		}
		request.setAttribute("GRADE", grade);
		request.setAttribute("PARENTID", parentId);
		return new ModelAndView("/member/rate/add", "DATAMAP", result);
    }
    
    @RequestMapping(value = "/member/rate/modify/{idx}", method = RequestMethod.GET)
    public ModelAndView mngModify(HttpServletRequest request, @PathVariable String idx) {
        return new ModelAndView("/member/rate/modify", "DATAMAP", new ReserveRateDAO().getByIdx(idx).getRowFirst());
    }
    
    // KBR 가맹점조회 > 리스트 > 지불 및 정산 > 수수료 변경 예약 클릭 > submit 
    @RequestMapping(value = {"/member/rate/insert"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if(cpDAO.insert("PG_RESERVE_RATE", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
					.resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK(CPUtil.RESULT_DATA_INFAIL,cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	@RequestMapping(value = {"/member/rate/update"}, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		
		if(cpDAO.updateAndBack("PG_RESERVE_RATE", SessionUtil.getUserId(request), cpRequest.data)){
			return new CPRUtil(cpRequest)
	        		.resultOK("수수료 예약 정보가 변경되었습니다.")
	        		.cpResponse();
		}else{
			return new CPRUtil(cpRequest)
	        		.resultNOK("수수료 예약 정보 변경에 실패하였습니다.",cpDAO.getError())
	        		.cpResponse();
		}
    }
	
	// KBR : 검색폼에서 이름또는 가맹점ID 검색한 정보를  ajax 통신  
	@RequestMapping(value = "/common/typeahead/{key}/{keyword}", method = RequestMethod.GET)
    public @ResponseBody String typeahead(HttpServletRequest request, @PathVariable String key, @PathVariable String keyword) {
		ArrayList<String> resultArray = new ArrayList<>();
		DAO dao = new DAO();
		CPSession session = SessionUtil.get(request);

		String changedKeyword = SQLInjectionUtil.xssChange(keyword);
		if(key.equalsIgnoreCase("mchtId")) {
			dao.setTable("PG_MCHT");
			dao.setColumns("mchtId as resKey");
			dao.addWhere("mchtId", changedKeyword, DAO.lk);
		} else if(key.equalsIgnoreCase("mchtName")) {
			dao.setTable("PG_MCHT");
			dao.setColumns("name as resKey");
			dao.addWhere("name", changedKeyword, DAO.lk);
		}else if(key.equalsIgnoreCase("mchtNameId")) {
			dao.setTable("PG_MCHT");
			dao.setColumns("concat(name,' ||',mchtId) as resKey");
			dao.addWhere("name", changedKeyword, DAO.lk);
		} else {
			return "";
		}
		if("대행사".equals(session.getGrade())) {
			dao.addWhere("distId",session.getParentId(),DAO.eq);
		}else if("에이전시".equals(session.getGrade())) {
			dao.addWhere("agencyId",session.getParentId(),DAO.eq);
		}else if("지사".equals(session.getGrade())) {
			dao.addWhere("salesId",session.getParentId(),DAO.eq);
		}else if("가맹점".equals(session.getGrade())) {
			dao.addWhere("mchtId",session.getParentId(),DAO.eq);
		}
		
		// KBR : 검색 키워드에 들어가는(like) 이름 또는 가맹점Id 모두 들고옴
		RecordSet rset = dao.search();
		while(rset.next()) {
			resultArray.add(rset.getString("resKey"));
		}
		
        return GsonUtil.toJson(resultArray);
    }
	
	//KJM : 거래생성 > ONLINE 거래생성 > 에이전시 하위 가맹점 조회 ajax 통신
	@RequestMapping(value = "/common/mchtList/{agencyId}", method = RequestMethod.GET)
    public @ResponseBody String getMchtList(HttpServletRequest request, @PathVariable String agencyId) {
		
		DAO dao = new DAO();
		dao.setTable("PG_MCHT");					//KJM : 가맹점 정보 테이블
		dao.setColumns("name,mchtId");				//이름, 아이디
		dao.addWhere("agencyId", agencyId, DAO.eq);	//where : agencyId 같은, 상태 = "사용"
		dao.addWhere("status", "사용", DAO.eq);
		dao.setLimit(999999);
		dao.setOrderBy("name asc");					//이름을 기준으로 오름차순
		
		//KJM : 조회 결과를 json 형식으로 보내준다
        return GsonUtil.toJson(dao.search().getRows());
    }
	
	@RequestMapping(value = "/common/mchtList/byvan/{van}", method = RequestMethod.GET)
    public @ResponseBody String getMchtByVanList(HttpServletRequest request, @PathVariable String van) {
		DAO dao = new DAO();
		dao.setTable("PG_MCHT_TMN A, PG_MCHT B");
		dao.setColumns("B.mchtId,B.name");
		dao.setWhere("A.mchtId = B.mchtId");
		dao.addWhere("A.status", "사용", DAO.eq);
		dao.addWhere("B.status", "사용", DAO.eq);
		if(!van.equals("DEFAULT")) {
			dao.addWhere("A.van", van, DAO.eq);
		}
		dao.setLimit(999999);
		dao.setGroupBy("B.mchtId");
		dao.setOrderBy("B.name asc");
		
        return GsonUtil.toJson(dao.search().getRows());
    }
	
	//KJM : 거래생성 > ONLINE 거래생성 > 가맹점 하위 터미널 조회 ajax 통신
	@RequestMapping(value = "/common/tmnList/{mchtId}", method = RequestMethod.GET)
    public @ResponseBody String getMchtTmnList(HttpServletRequest request, @PathVariable String mchtId) {
		
		DAO dao = new DAO();
		dao.setTable("PG_MCHT_TMN");			//KJM : 가맹점 터미널 기본정보 테이블
		dao.setColumns("tmnId,van,description");//단말기아이디, 사용 van, 취급품목
		dao.addWhere("mchtId", mchtId, DAO.eq);	//where : 단말기아이디 같은, 상태 = "사용"
		dao.addWhere("status", "사용", DAO.eq);
		dao.setLimit(999999);
		dao.setOrderBy("tmnId asc");			//단말기아이디를 기준으로 오름차순
		
		//KJM : 조회 결과를 json 형식으로 보내준다
		return GsonUtil.toJson(dao.search().getRows());
    }
}
