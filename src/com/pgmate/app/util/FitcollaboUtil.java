package com.pgmate.app.util;

import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.TrxRfdDAO;
import com.pgmate.app.dao.VanDAO;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.key.CPKEY;
import com.pgmate.lib.key.GenKey;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.pay.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class FitcollaboUtil {
    private static Logger logger = LoggerFactory.getLogger(FitcollaboUtil.class);
    private static Map<String, Object> storeIdMap;
    /**
     *
     */
    public FitcollaboUtil() {
        storeIdMap = new HashMap<>();
        storeIdMap.put("2055800001", "MBPAYEJS01"); // 개발
        storeIdMap.put("2055800002", "MBPAYEJS01"); // 운영
        storeIdMap.put("2055800003", "MBPAYEJS02"); // 운영
    }

    public String refund(SharedMap<String, Object> trxMap, SharedMap<String, Object> loadMap, String amount, String regId, String grade) {
        String res = "";
        String type = "";

        if(Integer.parseInt(trxMap.getString("amount")) == Integer.parseInt(amount)) {
            type = "0";
        } else {
            type = "1";
        }

        SharedMap<String, Object> parameters = new SharedMap<>();
        parameters.put("tid", loadMap.getString("tid"));
        parameters.put("storeId", storeIdMap.get(trxMap.getString("vanId")));
//        parameters.put("storeAuthKey", "");
        parameters.put("orderNo", loadMap.getString("orderNumber"));
        parameters.put("approvalNo", trxMap.getString("authCd"));
        parameters.put("amount", amount);
        parameters.put("type", type);
//		parameters.put("supplyValue", "");
//		parameters.put("tax", "");
//		parameters.put("vat", "");
//		parameters.put("taxFree", "");

        SharedMap<String,Object> tmnMap = new MchtTmnDAO().getById(trxMap.getString("tmnId")).getRow(0);
        if(!tmnMap.getString("status").equals("사용")) {
            Response response = new Response();
            Result result = new Result();
            result.resultCd = "XXXX";
            result.resultMsg= "터미널 상태 ["+tmnMap.getString("status")+"] 로 인한 취소 불가";
            response.result = result;
            res = GsonUtil.toJson(response);
            return res;
        }

        res = comm(parameters);
        if(res.startsWith("CONNECT")){
            Response response = new Response();
            Result result = new Result();
            result.resultCd = "XXXX";
            result.resultMsg= "통신장애로 인한 취소 실패 ";
            response.result = result;
            res = GsonUtil.toJson(response);
        }else{
            FitcollaboResult resp = (FitcollaboResult)GsonUtil.fromJson(res, FitcollaboResult.class);
            Response response = new Response();
            Result result = new Result();
            try{
                if(resp.resultCode == "0000") {
                    result.resultCd = "0000";
                    result.resultMsg= "취소 성공";
                    response.result = result;
                } else {
                    result.resultCd = resp.resultCode;
                    result.resultMsg= resp.resultMessage;
                    response.result = result;
                }

                if(resp.resultData != null) {
                    TrxRfdDAO trxRfdDAO = new TrxRfdDAO();
                    if(!trxRfdDAO.insertTrxAdminRfd(trxMap,resp,regId)) {
                        logger.info("===== PG_TRX_ADMIN_RFD INSERT FAILD =====");
                    }
                }
                res = GsonUtil.toJson(response);
            }catch(Exception e){}
        }

        return res;
    }

    public String comm(SharedMap<String, Object> parameters) {
//        String refundUrl = "https://payment.tvhub.co.kr:10488/APP-PAY-RP-INTEG/ext/appcardCancel.mv";    // dev
		String refundUrl = "https://payment.tvhub.co.kr:488/APP-PAY-RP-INTEG/ext/appcardCancel.mv";	// live
        StringBuilder result = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection conn = null;
        long time = System.currentTimeMillis();

        try {
            for (Map.Entry<String, Object> param : parameters.entrySet()) {
                if (buffer.length() != 0) buffer.append('&');
                buffer.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                buffer.append('=');
                buffer.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            String parameter = buffer.toString();


            logger.info("LOCAL >> FITCOLLABO [" + parameter + "]");
            URL url = new URL(refundUrl);
            conn = (HttpURLConnection) url.openConnection();

            System.setProperty("https.protocols", "TLSv1.2");

            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "close");

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(parameter);
            wr.flush();
            wr.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = br.readLine()) != null)
                result.append(line + "\n");

            br.close();

        } catch (Exception e) {
            result.append("CONNECT ERROR [" + e.getMessage() + "] " + refundUrl);
            logger.error("FITCOLLABO URL REQUEST ERROR =[" + e.getMessage() + "]");

        } finally {
            logger.info("ElapsedTime : " + (long) (System.currentTimeMillis() - time) + "msec");
            logger.info("LOCAL << FITCOLLABO [" + result.toString() + "]");
            if(conn != null) conn.disconnect();
        }
        return result.toString();
    }
}
