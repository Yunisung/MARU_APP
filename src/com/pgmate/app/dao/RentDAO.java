package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RentDAO extends DAO {
    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.RentDAO.class );
    private static final String TABLE = "VW_MCHT";
    private static final String COLUMNS = "mchtId,name,nick,status,bizType,bizCategory,distId,agencyId,salesId,idType,FN_MASK_IDENTIFY(identity) as identity,tel1,tel2,fax,zip,addr1,addr2,lat,lng,ceoName,FN_MASK_IDENTIFY(ceoIdentity) as ceoIdentity,ceoPhone,ceoTel,ceoZip,ceoAddr1,ceoAddr2,managerName,managerPhone,deposit,regId,regDay,regDate,salesName,agencyName,distName,aggregator,FN_AES_DEC(identity) AS decIdentity, mchtActiveDate";

    public RentDAO() {
        super(TABLE,CPUtil.CP_DEBUG);
        super.setColumns(RentDAO.COLUMNS);
    }

    public String getAESEnc(String value){
        String query = "SELECT FN_AES_ENC('"+value+"') pw";
        RecordSet rset = query(query);
        if(rset.size() ==0) {
            return "";
        } else {
            rset.next();
            return rset.getString("pw");
        }
    }

    public RecordSet exList(List<Data> datas, Page page){
		super.setDebug(true);
        super.setTable("( SELECT A.*, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate,B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
                "D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval,E.contractType, E.contractStatus " +
                "FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId LEFT JOIN PG_MCHT_RENT E ON A.mchtId = E.mchtId LEFT JOIN PG_MCHT_SVC F ON A.mchtId = F.mchtId WHERE F.rent = '사용') T");
        super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as sumRate");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
    }
}
