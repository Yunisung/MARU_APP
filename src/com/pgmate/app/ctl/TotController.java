package com.pgmate.app.ctl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.app.dao.SettleDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;

@Controller
public class TotController {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.ctl.TotController.class);
	
	
	@RequestMapping(value = "/sales/cap/mcht/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView salesCapList(HttpServletRequest request, @RequestBody CPRequest cpRequest) {
		SessionUtil.setSearchGrade(request, cpRequest);
		SettleDAO dao = new SettleDAO();
		StringBuffer query = new StringBuffer();
		if(cpRequest.getKeyValue("trxDay").indexOf("-") > -1) {
			cpRequest.replaceKeyValue("trxDay", cpRequest.getKeyValue("trxDay").replaceAll("-", ""));
		}

		query.append("(SELECT T1.*,IFNULL(T2.relsAmt,0) AS relsAmt,IFNULL(T2.relsFee,0) AS relsFee,IFNULL(T2.relsVat,0) AS relsVat,IFNULL(T2.relsCnt,0) AS relsCnt ,IFNULL(T3.deductAmount,0) as deductAmt, IFNULL(T4.manualRelsAmt,0) as manualRelsAmt  ");
		query.append(" FROM (SELECT trxDay, mchtId, name as mchtName, ceoName,'지급대기' as `status` ,max(stlDay) stlDay , min(trxDay) startDay ,max(trxDay) endDay, ");
		query.append(" SUM(IF(capType ='매입' and risk ='',amount,0)) as payAmt, SUM(IF(capType ='매입' and risk ='',stlFee,0)) as payFee, SUM(IF(capType ='매입' and risk ='',stlFeeVat,0)) as payVat, SUM(IF(capType ='매입' and risk ='' ,1,0)) as payCnt, ");
		query.append(" SUM(IF(capType ='매입취소',amount,0)) as rfdAmt, SUM(IF(capType ='매입취소',stlFee,0)) as rfdFee, SUM(IF(capType ='매입취소',stlFeeVat,0)) as rfdVat, SUM(IF(capType ='매입취소',1,0)) rfdCnt, ");
		query.append(" SUM(IF(capType ='매입' and risk !='',amount,0)) as holdAmt, SUM(IF(capType ='매입' and risk !='',stlFee,0)) as holdFee, SUM(IF(capType ='매입' and risk !='',stlFeeVat,0)) as holdVat, SUM(IF(capType ='매입' and risk !='',1,0)) as holdCnt, ");
		
		query.append(" SUM(IF(risk ='',stlDistFee,0)) as distFee,");
		query.append(" SUM(IF(risk ='',stlAgencyFee,0)) as agencyFee,");
		query.append(" SUM(IF(risk ='',stlVanFee,0)) as vanFee,");
		query.append(" SUM(IF(risk ='',benefit,0)) as benefit,");
		query.append(" MAX(stlRate) as stlRate,");
		query.append(" MAX(taxId) as taxId");
		query.append(" FROM VW_TRX_CAP WHERE trxDay >='" + cpRequest.getKeyValue("trxStartDay") + "' AND trxDay <='" + cpRequest.getKeyValue("trxEndDay") +"' ");
		query.append(" GROUP BY mchtId");
		query.append(" ) AS T1 LEFT OUTER JOIN ");
		query.append(" ( SELECT B.mchtId ,SUM(B.amount) as relsAmt , SUM(stlFee) as relsFee,  SUM(stlFeeVat) as relsVat , SUM(1) as relsCnt");
		query.append(" FROM PG_SETTLE_HOLD A, VW_TRX_CAP B  WHERE A.capId = B.capId and A.`status` ='반환요청' AND B.trxDay >='" + cpRequest.getKeyValue("trxStartDay") + "' AND B.trxDay <='" + cpRequest.getKeyValue("trxEndDay") +"' ");
		query.append(" group by B.mchtId");
		query.append(" ) AS T2 ON T1.mchtId = T2.mchtId LEFT OUTER JOIN ");
		query.append(" ( SELECT mchtId,SUM(deductAmount) deductAmount FROM VW_COLLECT_SETTLE_DTL_STATUS WHERE status = '확정' and deductAmount < 0 and deductStlId ='' group by mchtId");
		query.append(" ) AS T3 ON T1.mchtId = T3.mchtId LEFT OUTER JOIN ");
		query.append(" ( SELECT mchtId,SUM(amount) manualRelsAmt FROM PG_MCHT_DEPOSIT WHERE `depType` ='반환요청' AND stlId = '' group by mchtId");
		query.append(" ) AS T4 ON T1.mchtId = T4.mchtId ");
		query.append(" ) AS C1 LEFT OUTER JOIN PG_MCHT_TAX C2 ON C1.taxId = C2.taxId");
		
		dao.setTable(query.toString());
		dao.setColumns("concat(DATE_FORMAT(now(),'%y%m%d'), substr(uuid(),1,8)) as idx ,C1.*,(payAmt - payFee - payVat) + (rfdAmt - rfdVat - rfdFee) as stlAmount"
						+ " ,C2.bankCd,C2.bankName,C2.account,C2.accntHolder, stlRate");
		dao.setOrderBy("C1.mchtName asc");
		
		cpRequest.deleteKeyData("trxStartDay");
		cpRequest.deleteKeyData("trxEndDay");
		
		RecordSet rset = dao.search(cpRequest.data);

		return new CPRUtil(cpRequest).dataList(rset, dao).setView(request, "/sales/cap/mcht/list", "");
	}
}
