package com.pgmate.app.dao;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrameworkTest {

    private static Logger logger = LoggerFactory.getLogger(FrameworkTest.class);

    @Test
    public void configXssChangeForInsert() {
        MyDAO myDAO = new MyDAO();

        SharedMap<String, Object> ioMap = new SharedMap<>();
        ioMap.put("trxId", "T230214040904");
        ioMap.put("widgetKey", "key_16763574214833f67e24");
        ioMap.put("mchtId", "bktest001");
        ioMap.put("tmnId", "TMN000003");
        String reqJson = "{\"redirecturl\":\"http://www.bkwinners.com/redirect/redirect.html\",\"trxtype\":\"lpay\",\"publickey\":\"pk_94b7-c3e72c-813-30a0b\",\"products\":[{\"name\":\"t-shirts\",\"price\":\"1004\",\"qty\":1.0,\"desc\":\"deq-scription\"}],\"mode\":\"layer\",\"authorization\":\"pk_94b7-c3e72c-813-30a0b\",\"authform\":\"{\\\"lpay_eci\\\":\\\"\\\",\\\"amount\\\":\\\"1004\\\",\\\"lpay_f_co_cd\\\":\\\"\\\",\\\"goodname\\\":\\\"t-shirts\\\",\\\"p_req_id\\\":\\\"\\\",\\\"lpay_card_yymm\\\":\\\"\\\",\\\"storeid\\\":\\\"2010000002\\\",\\\"lpay_pg_id\\\":\\\"\\\",\\\"ordername\\\":\\\"박윤성\\\",\\\"phoneno\\\":\\\"01091697725\\\",\\\"lpay_p_m_num\\\":\\\"\\\",\\\"currencytype\\\":\\\"0\\\",\\\"lpay_mem_m_num\\\":\\\"\\\",\\\"lpay_tr_id\\\":\\\"\\\",\\\"lpay_otc_num\\\":\\\"\\\",\\\"installment\\\":\\\"\\\",\\\"lpay_req_amt\\\":\\\"\\\",\\\"ordernumber\\\":\\\"test-20220803171124\\\",\\\"lpay_xid\\\":\\\"\\\",\\\"lpay_imonth_num\\\":\\\"\\\",\\\"lpay_cavv\\\":\\\"\\\",\\\"email\\\":\\\"test@test.com\\\"}\",\"payername\":\"박윤성\",\"payeremail\":\"test@test.com\",\"key\":\"key_1659514282701c768635\",\"height\":1000,\"amount\":\"1004\",\"targetmethod\":\"popup\",\"trackid\":\"test-20220803171124\",\"udf1\":\"\",\"udf2\":\"\",\"webhookurl\":\"\",\"target\":\"kspay\",\"widgetlogourl\":\"\",\"apimaxinstall\":\"0\",\"payroute\":\"simple\",\"form\":\"{\\\"sndstoreid\\\":\\\"2010000002\\\",\\\"sndcharset\\\":\\\"utf-8\\\",\\\"sndordernumber\\\":\\\"test-20220803171124\\\",\\\"sndmobile\\\":\\\"01091697725\\\",\\\"sndordername\\\":\\\"박윤성\\\",\\\"sndgoodname\\\":\\\"t-shirts\\\",\\\"sndemail\\\":\\\"test@test.com\\\",\\\"sndamount\\\":\\\"1004\\\",\\\"sndreply\\\":\\\"https://devapi.bkwinners.kr/api/lpay/mobile/return/t220803037374\\\",\\\"sndprocesstype\\\":\\\"3\\\"}\",\"payertel\":\"01091697725\",\"width\":1000,\"debugmode\":\"sandbox\",\"device\":\"mobile\",\"targeturl\":\"https://kspay.ksnet.to/store/pay_proxy/kakao/kakao_rs_o1.jsp\"}";
        ioMap.put("reqJson", reqJson);

        myDAO.insertTrxIO3D(ioMap);
    }

    @Test
    public void configXssChangeForUpdate() {
        MyDAO myDAO = new MyDAO();

        SharedMap<String, Object> ioMap = new SharedMap<>();
        ioMap.put("trxId", "T230214040904");
        ioMap.put("widgetKey", "key_16763574214833f67e24");
        ioMap.put("mchtId", "bktest001");
        ioMap.put("tmnId", "TMN000003");
        String resData = "{\n" +
                "  \"result\": {\n" +
                "    \"resultCd\": \"0000\",\n" +
                "    \"resultMsg\": \"정상\",\n" +
                "    \"advanceMsg\": \"정상승인\",\n" +
                "    \"create\": \"20220802110115\"\n" +
                "  },\n" +
                "  \"pay\": {\n" +
                "    \"authCd\": \"02110122    \",\n" +
                "    \"card\": {\n" +
                "      \"cardId\": \"card_6c08-a47dbc-5c9-63085\",\n" +
                "      \"installment\": 0,\n" +
                "      \"bin\": \"KAKAO \",\n" +
                "      \"last4\": \"    \",\n" +
                "      \"issuer\": \"기타\",\n" +
                "      \"cardType\": \"신용\",\n" +
                "      \"acquirer\": \"\",\n" +
                "      \"issuerCode\": \"\",\n" +
                "      \"acquirerCode\": \"\"\n" +
                "    },\n" +
                "    \"webhookUrl\": \"\",\n" +
                "    \"products\": [\n" +
                "      {\n" +
                "        \"prodId\": \"pdt_e702-13d7d7-5a2-50be7\",\n" +
                "        \"name\": \"T-Shirts\",\n" +
                "        \"qty\": 1,\n" +
                "        \"price\": 1004,\n" +
                "        \"desc\": \"간편결제\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"trxId\": \"T220802037341\",\n" +
                "    \"trxType\": \"KAKAO\",\n" +
                "    \"tmnId\": \"TMN000032\",\n" +
                "    \"trackId\": \"test-20220802110035\",\n" +
                "    \"amount\": 1004,\n" +
                "    \"udf1\": \"\",\n" +
                "    \"udf2\": \"\"\n" +
                "  }\n" +
                "}";

        myDAO.updateTrxIO3D(ioMap, resData);
    }

    @Test
    public void configXssChangeForDelete() {
        MyDAO myDAO = new MyDAO();

        String trxId = "T230214040903";
        myDAO.deleteTrxIO3D(trxId);
    }


    class MyDAO extends DAO {
        public void insertTrxIO3D(SharedMap<String, Object> ioMap) {

            super.setTable("PG_TRX_IO_3D");
            super.setXssChange(false);
            super.setDebug(true);

            super.setRecord("trxId"		, ioMap.getString("trxId"));
            super.setRecord("widgetKey"	, ioMap.getString("widgetKey"));
            super.setRecord("mchtId"	, ioMap.getString("mchtId"));
            super.setRecord("tmnId"		, ioMap.getString("tmnId"));
            super.setRecord("trackId"	, ioMap.getString("trackId"));
            super.setRecord("device"	, ioMap.getString("device"));
            super.setRecord("van"		, ioMap.getString("van"));
            super.setRecord("vanId"		, ioMap.getString("vanId"));
            super.setRecord("reqJson"	, ioMap.getString("reqJson"));
            super.setRecord("resJson"	, ioMap.getString("resJson"));
            super.setRecord("resultCd"	, ioMap.getString("resultCd"));
            super.setRecord("resultMsg"	, ioMap.getString("resultMsg"));
            super.setRecord("regDay"	, ioMap.getString("regDay"));
            super.setRecord("regTime"	, ioMap.getString("regTime"));
            logger.info("set PG_TRX_IO_3D : {}", super.insert());

            super.initRecord();
        }

        public void updateTrxIO3D(SharedMap<String,Object> ioMap,String resData){
            super.setXssChange(false);
            super.setDebug(true);
            super.setTable("PG_TRX_IO_3D");
            super.setRecord("vanTrxId", 	ioMap.getString("vanTrxId"));
            super.setRecord("vanResultCd", 	ioMap.getString("vanResultCd"));
            super.setRecord("vanResultMsg", ioMap.getString("vanResultMsg"));
            super.setRecord("vanResultDate", ioMap.getString("vanResultDate"));
            super.setRecord("resData", resData);

            super.addWhere("trxId", 		ioMap.getString("trxId"));
            boolean update = super.update();
            logger.info("set PG_TRX_IO_3D update : {}",update );

            super.initRecord();
        }

        public boolean deleteTrxIO3D(String trxId) {
            super.setTable("PG_TRX_IO_3D");
            super.setXssChange(false);
            super.setDebug(true);

            super.addWhere("trxId", trxId);
            boolean deleted = super.delete();
            super.initRecord();
            return deleted;
        }
    }

}
