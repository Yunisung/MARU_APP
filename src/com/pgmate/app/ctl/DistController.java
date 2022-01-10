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

@Controller
public class DistController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.DistController.class);

	@RequestMapping(value = { "/member/dist/form" })
	public ModelAndView distForm() {
		return new ModelAndView("/member/dist/form");
	}

	@RequestMapping(value = { "/member/dist/add" })
	public ModelAndView distAdd(HttpServletRequest request) {
		return new ModelAndView("/member/dist/add");
	}

	@RequestMapping(value = "/member/dist/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		DistDAO distDAO = new DistDAO();

		if (CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))) {
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		// identity는 암호화되어 있으므로 복호화해서 비교해야 함.
		cpRequest.replaceKeyValue("identity", distDAO.getAESEnc(cpRequest.getValue("identity")));
		cpRequest.replaceKeyValue("ceoIdentity", distDAO.getAESEnc(cpRequest.getValue("ceoIdentity")));

		RecordSet rset = distDAO.list(cpRequest.data, cpRequest.page);
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

	@RequestMapping(value = "/member/dist/view/{distid}", method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request, @PathVariable String distid) throws Exception {
		request.setAttribute("DATAMAP", new DistDAO().getById(distid).getRow(0));
		request.setAttribute("DATAMNGMAP", new DistMngDAO().getById(distid).getRow(0));
		
		return new ModelAndView("/member/dist/view");
	}

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

		if (cpDAO.updateAndBack("PG_MAM_DIST", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("사용자 정보가 변경되었습니다.").cpResponse();
		} else {
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
	@RequestMapping(value = { "/member/dist/mng/add/{distid}" })
	public ModelAndView distMngAdd(HttpServletRequest request, @PathVariable String distid) {
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		return new ModelAndView("/member/dist/mng/add", "DATAMAP", new DistDAO().getById(distid).getRow(0));
	}

	@RequestMapping(value = "/member/dist/mng/modify/{distid}", method = RequestMethod.GET)
	public ModelAndView mngModify(HttpServletRequest request, @PathVariable String distid) {
		request.setAttribute("BANK_OPTION", new CodeDAO().getBank().getRows());
		request.setAttribute("LIMITMAP", new MngViewDAO().getByChildrenLimit(distid).getRowFirst());
		return new ModelAndView("/member/dist/mng/modify", "DATAMAP", new DistMngDAO().getById(distid).getRow(0));
	}

	@RequestMapping(value = {
			"/member/dist/mng/insert" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse mngInsert(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();
		if (cpDAO.insert("PG_MAM_DIST_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK(CPUtil.RESULT_DATA_INSERTED).redirect(cpRequest.redirect)
					.cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK(CPUtil.RESULT_DATA_INFAIL, cpDAO.getError()).cpResponse();
		}
	}

	@RequestMapping(value = {
			"/member/dist/mng/update" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse mngUpdate(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if (cpDAO.updateAndBack("PG_MAM_DIST_MNG", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("사용자 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("사용자 정보 변경에 실패하였습니다.", cpDAO.getError()).cpResponse();
		}
	}
}
