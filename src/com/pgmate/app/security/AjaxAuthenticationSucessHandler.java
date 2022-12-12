package com.pgmate.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationSucessHandler implements AuthenticationSuccessHandler {

    private static Logger logger = LoggerFactory.getLogger( AjaxAuthenticationSucessHandler.class );
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Authentication ROLE => {}", authentication.getAuthorities());
        //logger.debug("xxxxxxxxxxxx => {}", authentication.getDetails());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        logger.info("----- login/in SUCCESS -----");
        UserDAO userDAO = new UserDAO();

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty()) ip = CommonUtil.nToB(request.getHeader("X-Real-IP"));
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();

        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String memberId = userDetails.getUserId();
//		String memberPw = userDetails.getUserPw();

        // 터미널 인가 처리
        if(userDetails.getUserType().equals("터미널")) {
            CPSession cpSession = new CPSession();
            SharedMap<String,Object> tmnMap = userDetails.getInitMap();
            SessionUtil.initTmnSessionData(cpSession, tmnMap);
            cpSession.setTargetURL("/");
            //Session  생성
            SessionUtil.create(request, cpSession);
            SessionUtil.setAttribute(request,"endDate",CommonUtil.getCurrentDate("yyyyMMdd"));
            objectMapper.writeValue(response.getWriter(), "OK||"+cpSession.getTargetURL());
            return;
        }

        RecordSet rset = userDAO.getById(memberId);

        if (rset.size() < 1) {
            objectMapper.writeValue(response.getWriter(), "NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
            return;
        }

        SharedMap<String, Object> memberMap = rset.getRow(0);

        //접속 패스워드에 대한 재시도횟수 초기화
        userDAO.updateUserPwRetry(memberMap.getString("id"), 0);
        if(memberMap.getString("grade").equals("본사") && memberMap.getString("role").equals("관리자")) {
            SessionUtil.setAttribute(request, "CP_DEBUG", CPUtil.CP_DEBUG);
        }
        logger.debug("grade [{}]",memberMap.getString("grade"));
        logger.debug("role [{}]",memberMap.getString("role"));

        //USERSESSION
        CPSession cpSession = new CPSession();
        cpSession.setAccessDate(CommonUtil.getCurrentTimestamp());

        cpSession.setLastAccessDate(userDAO.getAccessById(memberMap.getString("id")));

        SessionUtil.initSessionData(cpSession, memberMap);

        cpSession.setTargetURL("/");

        // 모든 유저 대행사 아이디 세팅
        SharedMap<String, Object> userInfo = userDAO.getParentsById(memberId).getRowFirst();
        if(!userInfo.isEmpty()) {
            cpSession.setDistId(userInfo.getString("distId"));
        }

        //ACCESS 기록 추가
        logger.info("----- loing/in ID Access -----");
        userDAO.insertUserAcess(memberMap.getString("id"),ip,request.getHeader("User-Agent"));

        //Session  생성
        SessionUtil.create(request, cpSession, memberMap);
        SessionUtil.setAttribute(request,"endDate",CommonUtil.getCurrentDate("yyyyMMdd"));
        //return "OK||"+cpSession.getTargetURL();

        objectMapper.writeValue(response.getWriter(), "OK||"+cpSession.getTargetURL());
    }
}
