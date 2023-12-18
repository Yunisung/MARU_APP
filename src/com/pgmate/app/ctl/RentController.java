package com.pgmate.app.ctl;

import com.pgmate.app.dao.*;
import com.pgmate.app.export.CPDocument;
import com.pgmate.app.export.XlsExport;
import com.pgmate.app.hook.MemSettleHook;
import com.pgmate.app.hook.RiskChangeHook;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.model.ajax.Files;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class RentController {

    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.RentController.class );

    // 가맹점 조회
    @RequestMapping(value = {"/rent/mcht/form"})
    public ModelAndView mchtForm(HttpServletRequest request) {
        return new ModelAndView("/rent/mcht/form");
    }

    @RequestMapping(value = "/rent/mcht/list", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView mchtList(HttpServletRequest request, HttpServletResponse response, @RequestBody CPRequest cpRequest) {
        RentDAO rentDAO = new RentDAO();
        SessionUtil.setSearchGrade(request, cpRequest);
        cpRequest.replaceKeyValue("identity",rentDAO.getAESEnc(cpRequest.getKeyValue("identity")));
        cpRequest.replaceKeyValue("ceoIdentity",rentDAO.getAESEnc(cpRequest.getKeyValue("ceoIdentity")));
        RecordSet rset = rentDAO.exList(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,rentDAO).setView(request,"/rent/mcht/list","");
    }

    // 매입현황조회
    @RequestMapping(value = {"/rent/cap/form"})
    public ModelAndView capForm(HttpServletRequest request) {
        return new ModelAndView("/rent/cap/form");
    }

   @RequestMapping(value = "/rent/cap/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView capList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
        RentDAO rentDAO = new RentDAO();
        SessionUtil.setSearchGrade(request, cpRequest);

        cpRequest.replaceKeyName("amount", "abs(amount)");

        request.setAttribute("AMOUNT_SUM", rentDAO.trxSum(cpRequest.data,null).getRowFirst().getString("amount"));

        RecordSet rset = rentDAO.list(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,rentDAO).setView(request,"/rent/cap/list","");
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

    // 거래내역
    @RequestMapping(value = {"/rent/trx/form"})
    public ModelAndView trxForm(HttpServletRequest request) {
        return new ModelAndView("/rent/trx/form");
    }

    @RequestMapping(value = "/rent/trx/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView trxList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);
        RentDAO rentDAO = new RentDAO();

        request.setAttribute("SUMMAP", rentDAO.depositSum(cpRequest.data).getRow(0));

        RecordSet rset = rentDAO.listWithDecAccount(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,rentDAO).setView(request,"/rent/trx/list","");
    }

    @RequestMapping(value = "/rent/trx/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView trxView(HttpServletRequest request, @PathVariable("trxId") String trxId) {
        request.setAttribute("DATAMAP", new ChargeSettleDAO().getById(trxId).getRow(0));
        request.setAttribute("DATANOTIMAP", new ChargeSettleNotiDAO().getById(trxId).getRow(0));
        return new ModelAndView("/rent/trx/modal");
    }

    @RequestMapping(value = "/rent/trx/transfer/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView transferView(HttpServletRequest request, @PathVariable("trxId") String trxIdList) {
        // 거래아이디 리스트 중 마지막 ',' 제거
        trxIdList = trxIdList.substring(0, trxIdList.length() - 1);

        String[] trxIdArray = CommonUtil.split(trxIdList, ",",true);
        RentDAO rentDAO = new RentDAO();

        request.setAttribute("DATAMAP", new ChargeSettleDAO().getById("T231116043912").getRow(0));
        request.setAttribute("DATANOTIMAP", new ChargeSettleNotiDAO().getById("T231116043912").getRow(0));
        request.setAttribute("SUMAMT", rentDAO.getSumAmt(trxIdList));
        request.setAttribute("TRXIDLIST", trxIdList);
        return new ModelAndView("/rent/trx/transferModal");
    }

    @RequestMapping(value = "/rent/trx/transfer/{trxId}", method = RequestMethod.POST)
    public @ResponseBody String transfer(HttpServletRequest request, @PathVariable("trxId") String trxIdList) {
        String[] trxIdArray = CommonUtil.split(trxIdList, ",",true);
        logger.info("trxId : {}", trxIdArray[0]);
        RentDAO rentDAO = new RentDAO();

        String transferType = CommonUtil.nToB(request.getParameter("transferType"));
        String pubDay = "";
        String pubTime = "";

        String toDay = CommonUtil.getCurrentDate("yyyyMMdd");

        if(transferType.equals("예약")) {
            pubTime = CommonUtil.nToB(request.getParameter("pubTime"));
            pubDay = CommonUtil.nToB(request.getParameter("pubDay"));

            if(Integer.valueOf(pubDay) <= Integer.valueOf(toDay)) {
                return "NOK:이체예정일자는 익일부터 가능합니다.";
            }
        }

        Long amount = Long.valueOf(CommonUtil.nToB(request.getParameter("amount")));

        logger.info("data :: {} : {} : {}", pubDay, pubTime, amount);

        // 분납 거래건 valid
        List<SharedMap<String,Object>> sharedMapList = null;

        // 이체 처리 된 거래건인지
//        sharedMapList = rentDAO.isCSReserve(trxIdList);
//        if(sharedMapList.size() > 0) {
//            sharedMapList.clear();
//            return "NOK:이체처리된 거래건입니다.";
//        }

        // 같은 가맹점인지
        sharedMapList = rentDAO.isSingleMchtId(trxIdList);
        if(sharedMapList.size() > 0) {
            String firstMchtId = sharedMapList.get(0).getString("mchtId");
            for(SharedMap<String,Object> map : sharedMapList) {
                if(!map.getString("mchtId").equals(firstMchtId)) {
                    return "NOK:같은 가맹점 아님";
                }
            }
            sharedMapList.clear();
        }

        // 같은 계좌인지
        sharedMapList = rentDAO.isSameAccount(trxIdList);
        if(sharedMapList.size() > 0) {
            String firstAccount = sharedMapList.get(0).getString("account");
            for(SharedMap<String,Object> map : sharedMapList) {
                if(!map.getString("account").equals(firstAccount)) {
                    return "NOK:같은 계좌번호 아님";
                }
            }
            sharedMapList.clear();
        }

        // 리스크 거래건인지
        sharedMapList = rentDAO.isRiskTrx(trxIdList);
        if(sharedMapList.size() > 0) {
            for(SharedMap<String,Object> map : sharedMapList) {
                if(!map.getString("risk").equals("")) {
                    return "NOK:리스크 거래건";
                }
            }
            sharedMapList.clear();
        }

        SharedMap<String, Object> capMap;
        SharedMap<String, Object> mchtTaxMap;
        List<SharedMap<String, Object>> insertMapList = new ArrayList<>();

        // 분납 합산건 생성
        capMap = rentDAO.getCapMapByTrxId(trxIdArray[0].replaceAll("'",""));
        mchtTaxMap = rentDAO.getTaxMapByMchtId(capMap.getString("mchtId"));
        SharedMap<String, Object> newMap = setInsertNewMap(trxIdList,transferType, pubDay, pubTime, amount, capMap, mchtTaxMap);
        insertMapList.add(newMap);
        logger.info("======>{}",newMap.getString("trxId"));

        capMap.clear();
        mchtTaxMap.clear();

        // 분납 개별건 처리
        for(String trxId : trxIdArray) {
            trxId = trxId.replaceAll("'", "");
            if(!trxId.equals("")){
                capMap = rentDAO.getCapMapByTrxId(trxId);
                mchtTaxMap = rentDAO.getTaxMapByMchtId(capMap.getString("mchtId"));
                insertMapList.add(setInsertMap(transferType, pubDay, pubTime, newMap.getString("trxId"), capMap, mchtTaxMap));
            }
        }

        rentDAO.insertFirmReserve(insertMapList);

        return "OK";
    }

    public SharedMap<String, Object> setInsertNewMap(String trxIdList, String transferType, String pubDay, String pubTime, Long amount, SharedMap<String, Object> capMap, SharedMap<String, Object> mchtTaxMap) {
        SharedMap<String, Object> map = new SharedMap<>();
        RentDAO rentDAO = new RentDAO();
        // reserve
        // trxId, transferType, mchtId, trackId, pubDay, pubTime, status, retry, trxDay, trxTime, amount, fee, feeVat, bankFee, netAmount,
        // balance, resultCd, resultMsg, refId, rootTrxId, account, bankCd, bankName, holder, recordInfo, regId, regDay, regDate
        String trxId = rentDAO.getTrxId();
        map.put("trxId", trxId);
        map.put("transferType"	, transferType);
        map.put("mchtId"	, capMap.getString("mchtId"));
        map.put("trackId"	, "rent-" + CommonUtil.getCurrentDate("yyyyMMddhhmmss"));
        map.put("pubDay"	, pubDay);
        map.put("pubTime"	, pubTime);
        map.put("status"	, "대기");
        map.put("retry"		, 0);
        map.put("trxDay"	, CommonUtil.getCurrentDate("yyyyMMdd"));
        map.put("trxTime"	, CommonUtil.getCurrentDate("hhmmss"));
        map.put("amount"	, Math.abs(amount));
        map.put("fee"		, Math.abs(rentDAO.getFeeSum(trxIdList)));
        map.put("feeVat"	, Math.abs(rentDAO.getFeeVatSum(trxIdList)));
        map.put("bankFee"	, 0);
        map.put("netAmount"	, Math.abs(capMap.getLong("stlAmount")));
        map.put("netAmount"	, Math.abs(rentDAO.getNetAmountSum(trxIdList)));
        map.put("balance"	, rentDAO.getMchtBalance(capMap.getString("mchtId")).getLong("balance")+Math.abs(capMap.getLong("stlAmount")));
        map.put("resultCd"	, "");
        map.put("resultMsg"	, "");
        map.put("refId"		, trxId);
        map.put("rootTrxId"	, "");
        map.put("account"	, rentDAO.getAESEnc(mchtTaxMap.getString("account")));
        map.put("bankCd"	, mchtTaxMap.getString("bankCd"));
        map.put("bankName"	, mchtTaxMap.getString("bankName"));
        map.put("holder"	, rentDAO.getAESEnc(mchtTaxMap.getString("accntHolder")));
        map.put("recordInfo", rentDAO.getSender(capMap.getString("mchtId")));
        map.put("regId"		, capMap.getString("mchtId"));
        map.put("regDay"	, CommonUtil.getCurrentDate("yyyyMMdd"));

        return map;
    }

    public SharedMap<String, Object> setInsertMap(String transferType, String pubDay, String pubTime, String rootTrxId, SharedMap<String, Object> capMap, SharedMap<String, Object> mchtTaxMap) {
        SharedMap<String, Object> map = new SharedMap<>();
        RentDAO rentDAO = new RentDAO();
        // reserve
        // trxId, transferType, mchtId, trackId, pubDay, pubTime, status, retry, trxDay, trxTime, amount, fee, feeVat, bankFee, netAmount,
        // balance, resultCd, resultMsg, refId, rootTrxId, account, bankCd, bankName, holder, recordInfo, regId, regDay, regDate
        map.put("trxId", capMap.getString("trxId"));
        map.put("transferType"	, transferType);
        map.put("mchtId"	, capMap.getString("mchtId"));
        map.put("trackId"	, capMap.getString("trackId"));
        map.put("pubDay"	, pubDay);
        map.put("pubTime"	, pubTime);
        map.put("status"	, "대기");
        map.put("retry"		, 0);
        map.put("trxDay"	, capMap.getString("trxDay"));
        map.put("trxTime"	, capMap.getString("regTime"));
        map.put("amount"	, Math.abs(capMap.getLong("amount")));
        map.put("fee"		, Math.abs(capMap.getLong("stlFee")));
        map.put("feeVat"	, Math.abs(capMap.getLong("stlFeeVat")));
        map.put("bankFee"	, 0);
        map.put("netAmount"	, Math.abs(capMap.getLong("stlAmount")));
        map.put("balance"	, rentDAO.getMchtBalance(capMap.getString("mchtId")).getLong("balance")+Math.abs(capMap.getLong("stlAmount")));
        map.put("resultCd"	, "");
        map.put("resultMsg"	, "");
        map.put("refId"		, capMap.getString("capId"));
        map.put("rootTrxId"	, rootTrxId);
        map.put("account"	, rentDAO.getAESEnc(mchtTaxMap.getString("account")));
        map.put("bankCd"	, mchtTaxMap.getString("bankCd"));
        map.put("bankName"	, mchtTaxMap.getString("bankName"));
        map.put("holder"	, rentDAO.getAESEnc(mchtTaxMap.getString("accntHolder")));
        map.put("recordInfo", rentDAO.getSender(capMap.getString("mchtId")));
        map.put("regId"		, capMap.getString("mchtId"));
        map.put("regDay"	, CommonUtil.getCurrentDate("yyyyMMdd"));

        return map;
    }

    // 가맹점정산(예약이체)
    @RequestMapping(value = {"/rent/settle/form"})
    public ModelAndView settleForm(HttpServletRequest request) {
        return new ModelAndView("/rent/settle/form");
    }

    @RequestMapping(value = "/rent/settle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView settleList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);
        RentDAO rentDAO = new RentDAO();

        request.setAttribute("SUMMAP", rentDAO.getReserveSum(cpRequest.data, null).getRow(0));

        RecordSet rset = rentDAO.getRentSettleList(cpRequest.data,cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,rentDAO).setView(request,"/rent/settle/list","");
    }

    @RequestMapping(value = "/rent/settle/retry/view/{trxId}", method = RequestMethod.GET)
    public ModelAndView settleRetryView(HttpServletRequest request, @PathVariable("trxId") String trxId) {
        RentDAO rentDAO = new RentDAO();

        request.setAttribute("TRXID", trxId);
        request.setAttribute("AMOUNT", rentDAO.getAmount(trxId));
        return new ModelAndView("/rent/settle/transferModal");
    }

    @RequestMapping(value = "/rent/settle/retry/{trxId}", method = RequestMethod.POST)
    public @ResponseBody String retry(HttpServletRequest request, @PathVariable("trxId") String trxId) {
        RentDAO rentDAO = new RentDAO();

        String transferType = CommonUtil.nToB(request.getParameter("transferType"));
        String pubDay = "";
        String pubTime = "";

        String toDay = CommonUtil.getCurrentDate("yyyyMMdd");

        if(transferType.equals("예약")) {
            pubTime = CommonUtil.nToB(request.getParameter("pubTime"));
            pubDay = CommonUtil.nToB(request.getParameter("pubDay"));

            if(Integer.valueOf(pubDay) <= Integer.valueOf(toDay)) {
                return "NOK:이체예정일자는 익일부터 가능합니다.";
            }
        }

        if(rentDAO.updateFirmReserve(trxId, transferType, pubDay, pubTime)) {
            return "OK:재전송 요청 성공하였습니다.";
        } else {
            return "NOK:재전송 요청에 실패하였습니다.";
        }
    }

    // 대행사정산
    @RequestMapping(value = {"/rent/distSettle/form"})
    public ModelAndView distSettleForm(HttpServletRequest request) {
        return new ModelAndView("/rent/distSettle/form");
    }

    @RequestMapping(value = "/rent/distSettle/list", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView distSettleList(HttpServletRequest request,@RequestBody CPRequest cpRequest) {
        SessionUtil.setSearchGrade(request, cpRequest);
        RentDAO rentDAO = new RentDAO();

        cpRequest.setData("grade", "대행사", "eq", "", true);
        cpRequest.setData("stlAmt", "0", "ne", "", true);
        cpRequest.setData("stlDay", "", "", "desc", false);
        cpRequest.setData("memberId", "", "", "asc", false);
        RecordSet rset = rentDAO.distSettlelist(cpRequest.data, cpRequest.page);
        return new CPRUtil(cpRequest).dataList(rset,rentDAO).setView(request,"/rent/distSettle/list","");
    }

    // 정산정보 저장
    @RequestMapping(value = "/rent/distSettle/save", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SharedMap<String, Object> rentDecide(HttpServletRequest request,@RequestBody List<SharedMap<String, String>> requestList) {
        SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
        String regId = SessionUtil.getUserId(request);
        String regDay = CommonUtil.getCurrentDate("yyyyMMdd");
        DAO dao = new DAO();

        for(SharedMap<String, String> eachMap : requestList) {

            logger.debug("SETTLE SAVE = stlId: {}", eachMap.getString("stlId"));
            dao.setDebug(true);
            dao.setTable("PG_RENT_SETTLE");
            dao.setRecord("payOutAmt",eachMap.getLong("payOutAmt"));
            dao.setRecord("summary",eachMap.getString("summary"));
            dao.setRecord("regId",regId);
            dao.setRecord("regDay",regDay);
            dao.addWhere("stlId", eachMap.getString("stlId"));
            if(!dao.update()) {
                resultMap.put("result", "NOK");
                resultMap.put("msg", eachMap.getString("stlId") + " DB 업데이트에 실패했습니다.");
                break;
            }

            dao.initRecord();
        }
        if(!resultMap.getString("result").equals("NOK")) {
            resultMap.put("result", "OK");
        }else {
            resultMap.put("result", "NOK");
            if(resultMap.getString("msg").length() < 1) {
                resultMap.put("msg", "수정에 실패했습니다.");
            }
        }
        return resultMap;
    }

    // 지급 완료 처리
    @RequestMapping(value = "/rent/distSettle/paystatus/{status}", method = RequestMethod.POST)
    public @ResponseBody SharedMap<String, Object> settlePayStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
        SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
        logger.debug("STLID : {}", stlId);
        CPDAO dao = new CPDAO();
        dao.setTable("PG_RENT_SETTLE");
        dao.setColumns("count(1) as cnt");
        dao.addWhere("status", "확정", DAO.ne);
        dao.addWhere("stlId", stlId, DAO.in);
        RentDAO rentDAO = new RentDAO();

        if (dao.search().getRowFirst().getInt("cnt") == 0) {
            if (dao.update("UPDATE PG_RENT_SETTLE SET payStatus = '" + status + "', payOutDay = '" + CommonUtil.getCurrentDate("yyyyMMdd") + "' WHERE stlId IN (" + stlId + ")")) {
                resultMap.put("result", "OK");


                // 정산 완료 노티 전송
                String[] stlIdArray = CommonUtil.split(stlId, ",",true);
                for(String id : stlIdArray) {
                    id = id.replaceAll("'", "");
                    SharedMap<String,Object> stlMap = rentDAO.getStlMapByStlId(id);

                    String hookAddr = rentDAO.getHookAddr(stlMap.getString("memberId"));
                    if(!CommonUtil.isNullOrSpace(hookAddr)) {
                        String payLoad = setPayLoad(stlMap,"완료", "0000", "정상처리");
                        stlMap.put("payLoad", payLoad);
                        new MemSettleHook(hookAddr, stlMap, "0").start();
                    }
                }
            } else {
                resultMap.put("result", "NOK");
            }
        } else {
            resultMap.put("result", "NOK");
            resultMap.put("msg", "확정 또는 지불 상태를 확인해주세요.");
        }
        return resultMap;
    }

    public String setPayLoad(SharedMap<String, Object> sharedMap, String status, String resultCd, String resultMsg){
        SharedMap<String, String> payLoadMap = new SharedMap<String, String>();

        payLoadMap.put("distId",sharedMap.getString("memberId"));
        payLoadMap.put("stlId", sharedMap.getString("stlId"));
        payLoadMap.put("memberName",sharedMap.getString("memberName"));
        payLoadMap.put("status", status);
        payLoadMap.put("resultCd", resultCd);
        payLoadMap.put("resultMsg",resultMsg);
        payLoadMap.put("stlDay",sharedMap.getString("stlDay"));
        payLoadMap.put("payOutDate",CommonUtil.getCurrentDate("yyyyMMdd"));
        payLoadMap.put("payCnt",sharedMap.getString("payCnt"));
        payLoadMap.put("rfdCnt",sharedMap.getString("rfdCnt"));
        payLoadMap.put("payAmt",sharedMap.getString("payAmt"));
        payLoadMap.put("rfdAmt",sharedMap.getString("rfdAmt"));
        payLoadMap.put("stlAmt",sharedMap.getString("stlAmt"));
        payLoadMap.put("bankName",sharedMap.getString("bankName"));
        payLoadMap.put("account",sharedMap.getString("account"));
        payLoadMap.put("accntHolder",sharedMap.getString("accntHolder"));
        String payLoad = CommonUtil.toQueryString(payLoadMap,"UTF-8");
        return payLoad;
    }

    // 정산 상태 변경
    @RequestMapping(value = "/rent/distSettle/status/{status}", method = RequestMethod.POST)
    public @ResponseBody SharedMap<String, Object> rentSettleStatus(HttpServletRequest request, @PathVariable String status, @RequestBody String stlId) {
        SharedMap<String, Object> resultMap = new SharedMap<String, Object>();
        logger.debug("STLID : {}", stlId);
        CPDAO dao = new CPDAO();
        dao.setTable("PG_RENT_SETTLE");
        dao.setColumns("count(1) as cnt");
        dao.addWhere("stlId", stlId, DAO.in);

        if (status.equals("확정") || status.equals("보류")) {
            dao.addWhere("status", "대기", DAO.ne);
        } else if (status.equals("대기")) {
            dao.addWhere("payStatus", "대기", DAO.ne);
        } else {
            logger.error("정산 상태 변경 요청 이상 => {}", status);
            resultMap.put("result", "NOK");
            resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");
            return resultMap;
        }

        if (dao.search().getRowFirst().getInt("cnt") == 0) {
            if (dao.update("UPDATE PG_RENT_SETTLE SET status = '" + status + "' WHERE stlId IN (" + stlId + ")")) {
                resultMap.put("result", "OK");
            } else {
                resultMap.put("result", "NOK");
                resultMap.put("msg", "정산 상태 변경에 실패하였습니다.");

            }
        } else {
            resultMap.put("result", "NOK");
            resultMap.put("msg", "'대기' 상태가 아닌 항목이 포함되어 있습니다. <br>항목을 다시 확인해주세요.");
        }
        return resultMap;
    }

    // 정산 엑셀 데이터
    @RequestMapping(value = "/rent/distSettle/detail", method = RequestMethod.POST)
    public ModelAndView detail(HttpServletRequest request, @RequestParam String grade, @RequestParam String stlId) throws Exception {
        String columns = "capId,trxId,mchtId,name,tmnId,trackId,capType,rfdType,rootTrxId,amount,vat,issuer,authCd,trxDay,regTime,stlType";
        LinkedHashMap<String, String> thead = new LinkedHashMap<String, String>();
        thead.put("capId", "매입번호");
        thead.put("trxId", "거래번호");
        thead.put("mchtId", "가맹점ID");

        thead.put("name", "가맹점");
        if (grade.equals("stlDistId")) {
            columns += ", distName";
            thead.put("distName", "대행사");
        } else if (grade.equals("stlAgencyId")) {
            columns += ", agencyName";
            thead.put("agencyName", "에이전시");
        } else if (grade.equals("stlSalesId")) {
            columns += ", salesName";
            thead.put("salesName", "지사");
        }

        thead.put("tmnId", "터미널ID");
        thead.put("trackId", "거래추적번호");
        thead.put("capType", "매입구분");
        thead.put("rfdType", "취소구분");
        thead.put("rootTrxId", "원거래번호");
        thead.put("amount", "금액");
        thead.put("vat", "VAT");
        thead.put("issuer", "매입사");
        thead.put("authCd", "승인번호");
        thead.put("stlType", "정산일 기준");

        if (grade.equals("stlDistId")) {
            columns += ", stlDistFee, stlDistRate, stlDiffDistFee, stlDiffDistRate,stlDistDay, stlDistId";
            thead.put("stlDistFee", "정산 수수료");
            thead.put("stlDistRate", "정산 기준 수수료율");
            thead.put("stlDiffDistFee", "차액정산 수수료");
            thead.put("stlDiffDistRate", "차액정산 기준 수수료율");
            thead.put("stlDistDay", "정산예정일");
            thead.put("stlDistId", "정산 ID");
        } else if (grade.equals("stlAgencyId")) {
            columns += ", stlAgencyFee, stlAgencyRate, stlDiffAgencyFee, stlDiffAgencyRate, stlAgencyDay, stlAgencyId";
            thead.put("stlAgencyFee", "정산 수수료");
            thead.put("stlAgencyRate", "정산 기준 수수료율");
            thead.put("stlDiffAgencyFee", "차액정산 수수료");
            thead.put("stlDiffAgencyRate", "차액정산 기준 수수료율");
            thead.put("stlAgencyDay", "정산예정일");
            thead.put("stlAgencyId", "정산 ID");
        } else if (grade.equals("stlSalesId")) {
            columns += ", stlSalesFee, stlSalesRate, stlDiffSalesFee, stlDiffSalesRate, stlSalesDay, stlSalesId";
            thead.put("stlSalesFee", "정산 수수료");
            thead.put("stlSalesRate", "정산 기준 수수료율");
            thead.put("stlDiffSalesFee", "차액정산 수수료");
            thead.put("stlDiffSalesRate", "차액정산 기준 수수료율");
            thead.put("stlSalesDay", "정산예정일");
            thead.put("stlSalesId", "정산 ID");
        } else if (grade.equals("stlId")) {
            columns += ",stlAmount, stlRate, stlFee, stlFeeVat, stlDay, stlId";
            thead.put("stlAmount", "정산 금액");
            thead.put("stlFee", "정산 수수료");
            thead.put("stlFeeVat", "정산 수수료 VAT");
            thead.put("stlRate", "정산 기준 수수료율");
            thead.put("stlDay", "정산예정일");
            thead.put("stlId", "정산 ID");
        }

        thead.put("trxDay", "거래일");
        thead.put("regTime", "거래시간");

        CPDAO dao = new CPDAO();
        dao.setTable("VW_TRX_CAP_LIST");
        dao.setColumns(columns);
        dao.addWhere(grade, stlId, CPDAO.eq);

        RecordSet recordSet = dao.search();
        if (recordSet.size() > 0) {
//			String filePath = "webexport";
//			filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.getUploadDir() + File.separator + filePath + File.separator;
//			CPUtil.setTemplateDirectory(filePath);
//			filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

            CPDocument doc = new CPDocument();
            doc.title = "정산 대상 거래";
            XlsExport export = new XlsExport(doc);
            String link = "";

            try {
                link = export.makeExcel(thead, recordSet, true, true);
            } catch (Exception e) {
                link = e.getMessage();
            }

            CPResponse cpResponse = new CPResponse();
            Files file = new Files();
            file.link = link;
            cpResponse.file = file;

            return new ModelAndView("/common/jsonResponse", "message", GsonUtil.toJson(cpResponse));
        }
        return new ModelAndView();
    }
}
