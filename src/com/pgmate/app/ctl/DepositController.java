package com.pgmate.app.ctl;

import java.util.ArrayList;
import java.util.List;

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
import com.pgmate.app.dao.DepositDAO;
import com.pgmate.app.dao.DistDAO;
import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

@Controller
public class DepositController {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.DepositController.class);

	@RequestMapping(value = "/deposit/add/{mchtId}", method = RequestMethod.GET)
	public ModelAndView distAdd(HttpServletRequest request, @PathVariable String mchtId) {
		logger.debug("Deposit Add {}", mchtId);
		return new ModelAndView("/deposit/modal", "DATAMAP", new MchtDAO().getById(mchtId).getRow(0));
	}

	@RequestMapping(value = "/deposit/form/{mchtId}", method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request, @PathVariable String mchtId) {
		return new ModelAndView("/deposit/form", "DATAMAP", new MchtDAO().getById(mchtId).getRow(0));
	}

	@RequestMapping(value = "/deposit/modify/{depositId}", method = RequestMethod.GET)
	public ModelAndView modify(HttpServletRequest request, @PathVariable String depositId) {
		return new ModelAndView("/deposit/modify", "DATAMAP", new DistDAO().getById(depositId).getRow(0));
	}

	@RequestMapping(value = "/deposit/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView list(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		DepositDAO depositDAO = new DepositDAO();
		if (CommonUtil.isNullOrSpace(cpRequest.getKeyValue("status"))) {
			cpRequest.setData("status", "폐기", "ne", "", true);
		}
		cpRequest.setData("depType", "정산차감", "ne", "", true);

		RecordSet rset = depositDAO.list(cpRequest.data, cpRequest.page);
		return new CPRUtil(cpRequest).dataList(rset, depositDAO).setView(request, "/deposit/list", "");
	}

	@RequestMapping(value = { "/deposit/insert" }, method = RequestMethod.POST)
	public @ResponseBody SharedMap<String, Object> insert(HttpServletRequest request) {
		SharedMap<String, Object> reqMap = new SharedMap<String, Object>();
		reqMap.put("summary", request.getParameter("summary"));
		reqMap.put("amount", request.getParameter("amount"));
		reqMap.put("depType", request.getParameter("depType"));
		reqMap.put("mchtId", request.getParameter("mchtId"));
		reqMap.put("regDay", request.getParameter("regDay").replaceAll("-", ""));
		reqMap.put("createType", "수기");
		reqMap.put("createtId", "");

		if (!reqMap.getString("depType").equals("보류") && reqMap.getLong("amount") > 0) {
			reqMap.put("amount", -reqMap.getLong("amount"));
		}

		if (!reqMap.getString("depType").equals("반환요청") && !reqMap.getString("depType").equals("정산차감")) {
			reqMap.put("status", "완료");
		}

		List<SharedMap<String, Object>> eachList = new ArrayList<SharedMap<String, Object>>();
		eachList.add(reqMap);
		SharedMap<String, Object> resMap = new SharedMap<String, Object>();

		if (new DepositDAO().insertDeposit(eachList, SessionUtil.getUserId(request)) < 1) {
			resMap.put("result", "NOK");
			resMap.put("msg", "입력에 실패했습니다.");
		} else {
			resMap.put("result", "OK");
		}

		return resMap;
	}

	@RequestMapping(value = {"/deposit/update" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CPResponse update(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		CPDAO cpDAO = new CPDAO();

		if (cpDAO.updateAndBack("PG_MCHT_DEPOSIT", SessionUtil.getUserId(request), cpRequest.data)) {
			return new CPRUtil(cpRequest).resultOK("사용자 정보가 변경되었습니다.").cpResponse();
		} else {
			return new CPRUtil(cpRequest).resultNOK("사용자 정보 변경에 실패하였습니다.", cpDAO.getError()).cpResponse();
		}
	}
}
