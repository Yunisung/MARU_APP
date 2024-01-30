package com.pgmate.app.dao;

import com.pgmate.app.model.ajax.Data;
import com.pgmate.app.model.ajax.Page;
import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class RentDAO extends DAO {
    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.dao.RentDAO.class );
    private static final String TABLE = "VW_MCHT";
    private static final String COLUMNS = "*";

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
        super.setTable("( SELECT A.*, B.diffType, B.loanSettleStatus, B.settleType, B.rate, B.loanRate, B.payOutFee, B.distRate,B.agencyRate,B.salesRate, B.diff0DistRate, B.diff1DistRate, B.diff2DistRate, B.diff3DistRate, B.diff0CheckDistRate, B.diff1CheckDistRate, B.diff2CheckDistRate, B.diff3CheckDistRate, B.diff0AgencyRate, B.diff1AgencyRate, B.diff2AgencyRate, B.diff3AgencyRate, B.diff0CheckAgencyRate, B.diff1CheckAgencyRate, B.diff2CheckAgencyRate, B.diff3CheckAgencyRate, B.diff0SalesRate, B.diff1SalesRate, B.diff2SalesRate, B.diff3SalesRate, B.diff0CheckSalesRate, B.diff1CheckSalesRate, B.diff2CheckSalesRate, B.diff3CheckSalesRate,B.limitOnce, C.bankName, C.account, C.accntHolder, C.email, " +
                "D.holderName, D.status as vactStatus,D.issueType,D.expireSet,D.startDay,IF(D.settleTarget = 'Y', '사용', IF(D.settleTarget = 'N', '중지', '')) AS settleTarget, IF(D.feeType = '0', '정액', IF(D.feeType = '1', '정률', ''))as feeType, D.settleType as vactSettleType,D.fee,D.rate as vactRate, D.distSettleType,D.distFee,D.distRate as vactDistRate,D.agencySettleType,D.agencyFee,D.agencyRate as vactAgencyRate,D.salesSettleType,D.salesFee,D.salesRate as vactSalesRate,D.hookType,D.hookAddr,D.payOutFee as vactPayOutFee,D.transferInterval,E.contractType, E.contractStatus, E.rentRate, E.depositRate, E.chargeTarget " +
                "FROM VW_MCHT A LEFT JOIN PG_MCHT_MNG B ON A.mchtId = B.mchtId LEFT JOIN PG_MCHT_TAX C ON A.mchtId = C.mchtId AND C.taxStatus = '사용' LEFT JOIN PG_MCHT_MNG_VACT D ON A.mchtId = D.mchtId LEFT JOIN PG_MCHT_RENT E ON A.mchtId = E.mchtId LEFT JOIN PG_MCHT_SVC F ON A.mchtId = F.mchtId WHERE F.rent = '사용') T");
        super.setColumns("T.*, FN_MASK_IDENTIFY(T.identity) as maskidentity, FN_AES_DEC(T.identity) as decidentity,(T.rate + T.loanRate) as sumRate");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
    }


    public RecordSet trxSum(List<Data> datas,Page page) {
        super.setDebug(true);
        super.setTable("VW_TRX_CAP_LIST");
        super.setColumns("SUM(amount) AS amount");
        super.setWhere("serviceType='월세앱'");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        RecordSet rset = super.search();
        super.initRecord();
        return rset;	//LIST PAGING 검색
    }

    public RecordSet list(List<Data> datas,Page page){
        logger.info("=================================");
        super.setDebug(true);
        super.setTable("VW_TRX_CAP_LIST");
        super.setColumns("*");
        super.setWhere("serviceType='월세앱'");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
    }

    public RecordSet depositSum(List<Data> datas) {
        super.setDebug(true);
//        super.setTable("(SELECT A.*, B.billingMethod FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN VW_TRX_CAP B ON A.trxId=B.trxId) C");
        super.setTable("(SELECT A.*, B.trxId as csTrxId " +
                "FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN PG_CHARGE_SETTLE_FIRM_RESERVE B ON A.trxId=B.trxId LEFT OUTER JOIN VW_TRX_CAP C ON A.trxId=C.trxId "
                + "WHERE (C.serviceType = '월세앱' OR B.trxId IS NOT NULL)) D");
        super.setColumns("SUM(if(trxType='출금',amount,0)) AS depositAmt, SUM(if(trxType='입금',amount,0)) AS withdrawAmt");
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        RecordSet rset = super.search();
        super.initRecord();
        return rset;
    }

    public RecordSet listWithDecAccount(List<Data> datas, Page page){
        page = CPUtil.correctPage(page);

        super.setDebug(true);
//        super.setTable("(SELECT A.*, B.billingMethod, C.trxId as firmTrxId " +
//                "FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN VW_TRX_CAP B ON A.trxId=B.trxId LEFT OUTER JOIN PG_CHARGE_SETTLE_FIRM_RESERVE C ON A.trxId=C.trxId " +
//                "WHERE B.serviceType='월세앱') D");
        super.setTable("(SELECT A.*, B.trxId as csTrxId, C.billingMethod " +
                "FROM VW_CHARGE_SETTLE A LEFT OUTER JOIN PG_CHARGE_SETTLE_FIRM_RESERVE B ON A.trxId=B.trxId LEFT OUTER JOIN VW_TRX_CAP C ON A.trxId=C.trxId " +
                " WHERE (C.serviceType = '월세앱' OR B.trxId IS NOT NULL)) D");
        super.setColumns("D.*");
        super.setOrderBy("regDate desc");

        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);	//LIST PAGING 검색
    }

    public int getSumAmt(String trxIdList) {
        super.setTable("VW_CHARGE_SETTLE");
        super.setColumns("SUM(amount) as amount");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0).getInt("amount");
    }

    public List<SharedMap<String,Object>> isCSReserve(String trxIdList) {
        super.setDebug(true);
        super.setTable("PG_CHARGE_SETTLE_FIRM_RESERVE");
        super.setColumns("trxId");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRows();
    }

    public List<SharedMap<String,Object>> isSingleMchtId(String trxIdList) {
        super.setDebug(true);
        super.setTable("VW_CHARGE_SETTLE");
        super.setColumns("mchtId");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRows();
    }

    public List<SharedMap<String,Object>> isSameAccount(String trxIdList) {
        super.setDebug(true);
        super.setTable("VW_CHARGE_SETTLE");
        super.setColumns("account");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRows();
    }

    public List<SharedMap<String,Object>> isRiskTrx(String trxIdList) {
        super.setDebug(true);
        super.setTable("VW_TRX_CAP");
        super.setColumns("risk");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRows();
    }

    public SharedMap<String,Object> getCapMapByTrxId(String trxId) {
        super.setDebug(true);
        super.setTable("VW_TRX_CAP");
        super.setColumns("*");
        super.addWhere("trxId", trxId);

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0);
    }

    public SharedMap<String, Object> getTaxMapByMchtId(String mchtId) {
        super.setDebug(true);
        super.setTable("PG_MCHT_TAX");
        super.setColumns("*");
        super.addWhere("mchtId", mchtId);

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0);
    }

    public static String getFunction(String function, String value) {
        String returnVal = "";
        String query = "SELECT " + function + "(?) as val";

        DBManager db = null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rset = null;

        try {

            db = DBFactory.getInstance();
            conn = db.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, value);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                returnVal = rset.getString(1);
            }
            conn.commit();
        } catch (Exception t) {
            logger.debug("sql error : {}, query : {}", t.getMessage(), query);
        } finally {
            db.close(conn, pstmt, rset);
        }
        return returnVal;
    }

    public synchronized static String getTrxId() {
        return "T" + getFunction("FN_NEXTVAL2", "TRN");
    }


    /*ublic static String getTrackId() {
        return
    }*/
    public long getFeeSum(String trxIdList) {
        super.setTable("VW_TRX_CAP");
        super.setColumns("SUM(stlFee) as feeSum");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0).getLong("feeSum");
    }

    public long getFeeVatSum(String trxIdList) {
        super.setTable("VW_TRX_CAP");
        super.setColumns("SUM(stlFeeVat) as feeVatSum");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0).getLong("feeVatSum");
    }

    public long getNetAmountSum(String trxIdList) {
        super.setTable("VW_TRX_CAP");
        super.setColumns("SUM(stlAmount) as netAmountSum");
        super.setWhere("trxId IN (" + trxIdList + ")");

        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0).getLong("netAmountSum");
    }

    /**
     * 충전정산 잔액조회
     * @param mchtId
     * @return
     */
    public SharedMap<String, Object> getMchtBalance(String mchtId){
        super.setTable("PG_MCHT_BALANCE");
        super.setColumns("*");
        super.addWhere("mchtId",mchtId,eq);
        super.setOrderBy("");
        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRowFirst();
    }

    public String getSender(String mchtId) {
        super.setTable("PG_MCHT_RENT");
        super.setColumns("sender");
        super.addWhere("mchtId", mchtId, eq);
        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0).getString("sender");
    }


    public int insertFirmReserve(List<SharedMap<String, Object>> newMapList) {
        int inserted = 0;
        int count = 0;
        logger.info("insert chargeSettleFirmReserve batch : {}", newMapList.size());
        String query = "INSERT INTO PG_CHARGE_SETTLE_FIRM_RESERVE(trxId,transferType,mchtId,trackId,pubDay,pubTime,status,retry,trxDay,trxTime,amount,fee,feeVat,bankFee,netAmount,balance,resultCd,resultMsg,refId,rootTrxId,account,bankCd,bankName,holder,regId,regDay) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        DBManager db = null;
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {

            db = DBFactory.getInstance();
            conn = db.getConnection();
            pstmt = conn.prepareStatement(query);

            int batchSize = 100;

            for(SharedMap<String,Object> map : newMapList){
                int i=1;
                pstmt.setString(i++, map.getString("trxId"));
                pstmt.setString(i++, map.getString("transferType"));
                pstmt.setString(i++, map.getString("mchtId"));
                pstmt.setString(i++, map.getString("trackId"));
                pstmt.setString(i++, map.getString("pubDay"));
                pstmt.setString(i++, map.getString("pubTime"));
                pstmt.setString(i++, map.getString("status"));
                pstmt.setString(i++, map.getString("retry"));
                pstmt.setString(i++, map.getString("trxDay"));
                pstmt.setString(i++, map.getString("trxTime"));
                pstmt.setString(i++, map.getString("amount"));
                pstmt.setString(i++, map.getString("fee"));
                pstmt.setString(i++, map.getString("feeVat"));
                pstmt.setString(i++, map.getString("bankFee"));
                pstmt.setString(i++, map.getString("netAmount"));
                pstmt.setString(i++, map.getString("balance"));
                pstmt.setString(i++, map.getString("resultCd"));
                pstmt.setString(i++, map.getString("resultMsg"));
                pstmt.setString(i++, map.getString("refId"));
                pstmt.setString(i++, map.getString("rootTrxId"));
                pstmt.setString(i++, map.getString("account"));
                pstmt.setString(i++, map.getString("bankCd"));
                pstmt.setString(i++, map.getString("bankName"));
                pstmt.setString(i++, map.getString("holder"));
                pstmt.setString(i++, map.getString("regId"));
                pstmt.setString(i++, map.getString("regDay"));

                pstmt.addBatch();
                if (++count % batchSize == 0) {
                    inserted += pstmt.executeBatch().length;
                }
            }
            inserted += pstmt.executeBatch().length;
            conn.commit();

        } catch (Exception e) {
            logger.debug("insert batch chargeSettleFirmReserve error : {}", CommonUtil.getExceptionMessage(e));
        } finally {
            db.close(pstmt);
            db.close(conn);
        }
        return inserted;

    }

    public RecordSet getReserveSum(List<Data> datas,Page page) {
        super.setDebug(true);
//        super.setTable("PG_CHARGE_SETTLE_FIRM_RESERVE");
        super.setTable("(SELECT A.*, B.regDay as payDay FROM PG_CHARGE_SETTLE_FIRM_RESERVE A LEFT OUTER JOIN VW_TRX_CAP B ON A.refTrxId=B.trxId) AS C");
        super.setColumns("SUM(amount) AS amount");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, datas);				//DATA to CONDITION
        RecordSet rset = super.search();
        super.initRecord();
        return rset;	//LIST PAGING 검색
    }

    public RecordSet getRentSettleList(List<Data> data, Page page) {
        super.setDebug(true);
        super.setTable("(SELECT A.*, FN_AES_DEC(A.account) as decAccount, FN_AES_DEC(A.holder) as decHolder, C.name, " +
                "B.billingType, B.billingMethod, B.regDay as payDay, B.regTime as payTime FROM PG_CHARGE_SETTLE_FIRM_RESERVE A " +
                "LEFT OUTER JOIN VW_TRX_CAP B ON A.refTrxId=B.trxId LEFT OUTER JOIN PG_MCHT C ON A.mchtId=C.mchtId) AS D");
        super.setColumns("*");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, data);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);
    }

    public int getAmount(String trxId) {
        super.setDebug(true);
        super.setTable("PG_CHARGE_SETTLE_FIRM_RESERVE");
        super.setColumns("netAmount as amount");
        super.addWhere("trxId", trxId, eq);
        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRow(0).getInt("amount");
    }

    public boolean updateFirmReserve(String trxId, String transferType, String pubDay, String pubTime) {
        super.setTable("PG_CHARGE_SETTLE_FIRM_RESERVE");
        super.setRecord("status", "대기");
        super.setRecord("retry", 0);
        super.setRecord("transferType", transferType);
        super.setRecord("pubDay", pubDay);
        super.setRecord("pubTime", pubTime);
        super.addWhere("trxId", trxId);

        boolean result = super.update();
        super.initRecord();
        return result;
    }

    public RecordSet distSettlelist(List<Data> data, Page page) {
        super.setTable("PG_RENT_SETTLE A LEFT OUTER JOIN PG_MAM_DIST B ON A.memberId=B.distId");
        super.setColumns("(A.payCnt+A.rfdCnt) AS totalCnt,(A.payAmt+A.rfdAmt) AS totalAmt, B.name as memberName, A.*");
        page = CPUtil.correctPage(page);
        CPUtil.setDAO(this, data);				//DATA to CONDITION
        return super.searchList(page.current, page.size,page.hash);
    }

    public SharedMap<String, Object> getStlMapByStlId(String id) {
        super.setTable("PG_RENT_SETTLE A LEFT OUTER JOIN PG_MAM_DIST B ON A.memberId=B.distId");
        super.setColumns("(A.payCnt+A.rfdCnt) AS totalCnt,(A.payAmt+A.rfdAmt) AS totalAmt, B.name as memberName, A.*");
        super.addWhere("A.stlId", id);
        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRowFirst();
    }

    public String getHookAddr(String memberId) {
        super.setTable("PG_MCHT_WEBHOOK");
        super.setColumns("hookUrl");
        super.addWhere("id", memberId);
        super.addWhere("trxType", "SETTLE");
        super.addWhere("status", "Y");
        RecordSet rset = super.search();
        super.initRecord();
        return rset.getRowFirst().getString("hookUrl");
    }

    public boolean insertMemSettleNoti(SharedMap<String, Object> ntsMap) {
        int result = 0;
        logger.info("insertRentSettleNoti batch : {}", ntsMap.size());
        String query = "INSERT INTO `PG_RENT_SETTLE_NOTI` (`stlId`, `memberId`, `hookAddr`, `retry`, `status`, `code`, `payLoad`, `resData`, `regDay`, `regTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        DBManager db 	= null;
        PreparedStatement pstmt	= null;
        Connection conn			= null;
        ResultSet rset			= null;
        int i = 1;
        try{
            db 		= DBFactory.getInstance();
            conn	= db.getConnection();
            pstmt	= conn.prepareStatement(query);
            pstmt.setString(i++,ntsMap.getString("stlId"));
            pstmt.setString(i++,ntsMap.getString("memberId"));
            pstmt.setString(i++,ntsMap.getString("hookAddr"));
            pstmt.setInt(i++,ntsMap.getInt("retry"));
            pstmt.setString(i++,ntsMap.getString("status"));
            pstmt.setInt(i++,ntsMap.getInt("code"));
            pstmt.setString(i++,ntsMap.getString("payLoad"));
            pstmt.setString(i++,ntsMap.getString("resData"));
            pstmt.setString(i++,ntsMap.getString("regDay"));
            pstmt.setString(i++,ntsMap.getString("regTime"));

            result = pstmt.executeUpdate();
            conn.commit();
        }catch(Exception e){
            e.printStackTrace();
            logger.error("insertRiskChangeNoti ERROR : {}, query : {}", e.getMessage(), query);
        }finally{
            db.close(conn,pstmt,rset);
        }

        if(result > 0) {
            return true;
        }else {
            return false;
        }
    }
}
