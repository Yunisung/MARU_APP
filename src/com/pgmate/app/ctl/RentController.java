package com.pgmate.app.ctl;

import com.pgmate.app.dao.*;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.AllatUtil;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.EncryptUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.cipher.Base64;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RentController {

    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.RentController.class );

    @RequestMapping(value = {"/rent/mcht/form"})
    public ModelAndView form(HttpServletRequest request) {
        return new ModelAndView("/rent/mcht/form");
    }

    @RequestMapping(value = "/rent/mcht/list", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, @RequestBody CPRequest cpRequest) {
        RentDAO rentDAO = new RentDAO();
        SessionUtil.setSearchGrade(request, cpRequest);
        cpRequest.replaceKeyValue("identity",rentDAO.getAESEnc(cpRequest.getKeyValue("identity")));
        cpRequest.replaceKeyValue("ceoIdentity",rentDAO.getAESEnc(cpRequest.getKeyValue("ceoIdentity")));
        RecordSet rset = rentDAO.exList(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,rentDAO).setView(request,"/rent/mcht/list","");
    }

    @RequestMapping(value = "/rent/cap/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView capList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);

        cpRequest.replaceKeyName("amount", "abs(amount)");

        request.setAttribute("AMOUNT_SUM", new TrxCapDAO().trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

        TrxCapDAO trxCapDAO = new TrxCapDAO();
//		cpRequest.setData("capId", "", "", "desc", false);
        RecordSet rset = trxCapDAO.list(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,trxCapDAO).setView(request,"/rent/cap/list","");
    }

    @RequestMapping(value = "/rent/cap/view/{capId}", method = RequestMethod.GET)
    public ModelAndView capView(HttpServletRequest request, @PathVariable String capId) {
        SharedMap<String, Object> res =  new TrxCapDAO().getByCapId(capId).getRow(0);

        //PYS : 갤럭시아 영수증 조회용
        if(res.startsWith("van", "GALAXIA")) {
            res.put("GalaxiaMID", res.getString("vanId"));
        }

        request.setAttribute("DATAMAP", res);
        request.setAttribute("DATAREFMAP", new TrxCapDAO().getByRootTrxId(res.getString("trxId")).getRow(0));

        request.setAttribute("DATATMNMAP", new MchtTmnDAO().getById(res.getString("tmnId")).getRow(0));

        request.setAttribute("IQR_MAP", new TrxIqrDAO().getByCapId(capId).getRows());
        return new ModelAndView("/rent/cap/modal");
    }

}
