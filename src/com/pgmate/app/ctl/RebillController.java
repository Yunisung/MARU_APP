package com.pgmate.app.ctl;

import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.RebillDAO;
import com.pgmate.app.dao.TrxErrDAO;
import com.pgmate.app.dao.VactDtlDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RebillController {
    private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.RebillController.class);

    @RequestMapping(value = "/rebill/reg/list", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView regList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);

        //터미널아이디로 접속한 경우 가맹점아이디로 검색
        CPSession cpSession = SessionUtil.get(request);
        if(cpSession.getGrade().equals("하위가맹점")) {
            MchtTmnDAO mDAO = new MchtTmnDAO();
            SharedMap<String, Object> map = mDAO.getById(cpRequest.getKeyValue("tmnId")).getRowFirst();
            cpRequest.replaceKeyName("tmnId", "mchtId");
            cpRequest.replaceKeyValue("mchtId",	map.getString("mchtId"));
        }


        RebillDAO rebillDAO = new RebillDAO();
        RecordSet rset = rebillDAO.regList(cpRequest.data, cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset, rebillDAO).setView(request, "/rebill/reg/list", "");
    }

    @RequestMapping(value = "/rebill/err/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView errList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);
        TrxErrDAO trxErrDAO = new TrxErrDAO();
        RecordSet rset = trxErrDAO.list(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,trxErrDAO).setView(request,"/trx/err/list","");
    }
}
