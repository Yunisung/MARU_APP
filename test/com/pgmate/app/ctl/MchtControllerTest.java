package com.pgmate.app.ctl;

import com.pgmate.app.dao.VactDtlDAO;
import com.pgmate.lib.dao.DAO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MchtControllerTest {
    private static Logger logger = LoggerFactory.getLogger(MchtControllerTest.class);

    @Test
    public void vactExIssue() {
        // 가상계좌 발행

        //1. 요청건수 <= 남은건수: 이건 DB로 체크

        //2. INSERT 처리
        VactDtlDAO vactDtlDAO = new VactDtlDAO();
        List<String> issueIdList;
        //insert 된 issueId 리스트 반환
        String mchtId = "bktest001";
        String bankCd = "089";
        String userId = "SYSTEM";
        List<String> accountList = new ArrayList<>();
        accountList.add("70022000010003");
        accountList.add("70022000010004");

        issueIdList = vactDtlDAO.insertAppointedVact(mchtId, accountList, bankCd, userId);

        if(issueIdList.size() < 1) {
            logger.error("msg", "DB 작업에 실패했습니다.관리자에게 문의해주세요.");
        } else {
            DAO dao = new DAO();
            logger.debug("INSERT HT_VACT_DTL {} : ", vactDtlDAO.insertHtVactDtl(issueIdList));
            logger.debug("UPDATE PG_MCHT_MNG_VACT {}:", dao.update("UPDATE PG_MCHT_MNG_VACT A LEFT JOIN (SELECT COUNT(*) as cnt, mchtId FROM PG_VACT_DTL GROUP BY mchtId) B ON A.mchtId = B.mchtId SET A.quantity = B.cnt WHERE A.mchtId ='"+mchtId+"' "));
            logger.info("result", "OK");
        }
    }
}
