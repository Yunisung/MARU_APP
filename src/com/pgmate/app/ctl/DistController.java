package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;

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

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.CodeDAO;
import com.pgmate.app.dao.DistDAO;
import com.pgmate.app.dao.DistMngDAO;
import com.pgmate.app.dao.HTDAO;
import com.pgmate.app.dao.MngViewDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

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

@Controller
public class DistController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.DistController.class);

	//KJM : 멤버 관리 > 대행사관리 > 대행사조회
	@RequestMapping(value = { "/member/dist/form" })
	public ModelAndView distForm() {
		return new ModelAndView("/member/dist/form");
	}
	
	//KJM : 멤버 관리 > 대행사관리 > 대행사등록 화면 이동
	@RequestMapping(value = { "/member/dist/add" })
	public ModelAndView distAdd(HttpServletRequest request) {
		return new ModelAndView("/member/dist/add");
	}
	
	//KJM : 대행사 조회 리스트
	@RequestMapping(value = "/member/dist/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		DistDAO distDAO = new DistDAO();
		
		//KJM : 조회 조건에 상태값 안 줬을 경우
		if (CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))) {
			//KJM : 상태 != 폐기 세팅
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		// identity는 암호화되어 있으므로 복호화해서 비교해야 함.
		cpRequest.replaceKeyValue("identity", distDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity", distDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));

		/*
		 * KJM : 조회 조건 안줬을 경우 "상태 != 폐기"인 리스트 값 들고옴
		 * 조회 조건 줬을 경우 조회 조건에 맞는 리스트 들고옴
		 */
		RecordSet rset = distDAO.list(cpRequest.data, cpRequest.page);
		
		
		//KJM : 조회 된 리스트들 지정한 경로에 뿌려준다
		return new CPRUtil(cpRequest).dataList(rset, distDAO).setView(request, "/member/dist/list", "");
	}

	@RequestMapping(value = "/member/dist/idList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView idList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		DistDAO distDAO = new DistDAO();

		if (CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))) {
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		
		RecordSet rset = distDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, distDAO).setView(request, "/member/dist/idList", "");
	}
	
	//KJM : 멤버관리 > 대행사 페이지
	@RequestMapping(value = "/member/dist/view/{distid}", method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request, @PathVariable String distid) throws Exception {
		request.setAttribute("DATAMAP", new DistDAO().getById(distid).getRow(0));
		request.setAttribute("DATAMNGMAP", new DistMngDAO().getById(distid).getRow(0));
		
		return new ModelAndView("/member/dist/view");
	}

	//KJM : 멤버관리 > 대행사 정보 수정
	@RequestMapping(value = "/member/dist/modify/{distid}", method = RequestMethod.GET)
	public ModelAndView modify(HttpServletRequest request, @PathVariable String distid) {
		return new ModelAndView("/member/dist/modify", "DATAMAP", new DistDAO().getById(distid).getRow(0));
	}

	@RequestMapping(value = {
			"/member/dist/insert" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse insert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {

		CPDAO cpDAO = new CPDAO();
		DistDAO distDAO = new DistDAO();
		String lastDistId = distDAO.getLastDistId();
	
		// identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity", cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity", cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));
		
		// distId 순번
		String str = lastDistId;
		
		char str1 = str.toCharArray()[0];
		char str2 = str.toCharArray()[1];
		
		int strInt1 = (int)str.toCharArray()[0];
		int strInt2 = (int)str.toCharArray()[1];
		
		String result = "";
		
		// 01 to 99
		if(strInt1>=48 && strInt1 <= 57 && strInt2>=48 && strInt2 <=57) {
			// 99 -> AA
			if(strInt1 == 57 && strInt2 == 57) {
				strInt1 = 65;
				strInt2 = 65;
				
				str1 = (char)strInt1;
				str2 = (char)strInt2;
				
				result = String.valueOf(str1)+String.valueOf(str2);
				
				cpRequest.setData("distId",result);
			
			}else {
				cpRequest.setData("distId", cpDAO.getFunction("FN_GET_DIST_ID"));
			}
		// AA to ZZ
		}else if(strInt1>=65 && strInt1<=90) {
			if(strInt2>=65 && strInt2<90) {
				strInt2++;
			} else if(strInt2==90){
				strInt2 = 65;
				strInt1++;
			}
			
			str1 = (char)strInt1;
			str2 = (char)strInt2;
			
			result = String.valueOf(str1)+String.valueOf(str2);
			
			cpRequest.setData("distId",result);
		}
		
		if (cpDAO.insert("PG_MAM_DIST", SessionUtil.getUserId(request), cpRequest.data)) {
			SessionUtil.initSessionData(request);
			return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
					.cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK(CPUtil.RESULT_DATA_INFAIL, cpDAO.getError()).cpResponse();
		}
		
	}

	@RequestMapping(value = {
			"/member/dist/update" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		// identity 입력시 암호화하여 넣어야함.
		cpRequest.replaceValue("identity", cpDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceValue("ceoIdentity", cpDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));

		//KJM : update 쿼리문 수행
		if (cpDAO.updateAndBack("PG_MAM_DIST", SessionUtil.getUserId(request), cpRequest.data)) {
			//KJM : update 성공 시 성공 메시지 전달
			return new CPRUtil(cpRequest).resultOK("사용자 정보가 변경되었습니다.").cpResponse();
		} else {
			//KJM : update 실패 시 실패 메시지 전달
			return new CPRUtil(cpRequest).resultNOK("사용자 정보 변경에 실패하였습니다.", cpDAO.getError()).cpResponse();
		}
	}

	@RequestMapping(value = "/member/dist/ht/list/{distId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView htList(HttpServletRequest request, @RequestBody CPRequest cpRequest,
			@PathVariable String distId) {

		HTDAO htDAO = new HTDAO();
		RecordSet rset = htDAO.getUserById(distId, 10);
		return new CPRUtil(cpRequest).dataList(rset, htDAO).setView(request, "/member/dist/ht/list", "");
	}

	// ======================================================= 관리정보
	//KJM : 멤버관리 > 대행사 지불 및 정산정보 등록 화면 이동
	@RequestMapping(value = { "/member/dist/mng/add/{distid}" })
	public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String distid) {
		//KJM : 은행 선택 옵션에 들어갈 값 세팅해줌
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/member/dist/mng/add", "DATAMAP", new DistDAO().getById(distid).getRow(0));
		
	}

	//KJM : 멤버관리 > 대행사 지불 및 정산정보 수정 화면 이동
	@RequestMapping(value = "/member/dist/mng/modify/{distid}", method = RequestMethod.GET)
	public ModelAndView mngModify(HttpServletRequest request, @PathVariable String distid) {
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		request.setAttribute("LIMITMAP", new MngViewDAO().getByChildrenLimit(distid).getRowFirst());
		return new ModelAndView("/member/dist/mng/modify", "DATAMAP", new DistMngDAO().getById(distid).getRow(0));
	}
	
	//KJM : 멤버관리 > 대행사 지불 및 정산정보 등록
	@RequestMapping(value = {
			"/member/dist/mng/insert" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		//KJM : insert 쿼리문 수행
		if (cpDAO.insert("PG_MAM_DIST_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			//KJM : 대행사 등록 후 지불정산 등록까지 완료해야 세션 초기화하여 대행사 다시 들고옴
			return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
					.cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK(CPUtil.RESULT_DATA_INFAIL, cpDAO.getError()).cpResponse();
		}
	}

	//KJM : 멤버관리 > 대행사 지불 및 정산정보 수정
	@RequestMapping(value = {
			"/member/dist/mng/update" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		//KJM : update 쿼리문 수행
		if (cpDAO.updateAndBack("PG_MAM_DIST_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("사용자 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("사용자 정보 변경에 실패하였습니다.", cpDAO.getError()).cpResponse();
		}
	}
}
