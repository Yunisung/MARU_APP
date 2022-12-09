package com.pgmate.app.security.filter;

import com.pgmate.lib.util.lang.CommonUtil;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String smsKey;
    private String ip;
    private String userAgent;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        // request 파라미터에서 추가로 받을 항목
        smsKey = request.getParameter("smsKey");
        ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty()) ip = CommonUtil.nToB(request.getHeader("X-Real-IP"));
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();

        userAgent = request.getHeader("User-Agent");
    }

    public String getSmsKey() {
        return smsKey;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
