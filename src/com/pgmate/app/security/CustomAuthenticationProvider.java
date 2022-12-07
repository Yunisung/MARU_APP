package com.pgmate.app.security;

import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.security.CustomAuthenticationProvider.class );

    UserDetails isValidUser(String memberId, String memberPw) {

        logger.info("----- login/in START -----");
        UserDAO userDAO = new UserDAO();


        //입력받은 패스워드 암호화 값
        String inputPw = userDAO.getPassword(memberPw);
        //대소문자 구분안함.

        RecordSet rset = userDAO.getById(memberId);
        //존재하지 않는 아이디
        if (rset.size() < 1) {
            logger.info("----- login/in notFind ID -----");
            // 로그인 아이디가 아닌 터미널로 접속했는지 확인
            RecordSet rset2 = new MchtTmnDAO().getById(memberId);
            if(rset2.size() == 0) {
                // 터미널 인증 필요
            } else {
                throw new BadCredentialsException("NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
            }
        }

        logger.info("----- login/in find ID -----");
        SharedMap<String, Object> memberMap = rset.getRow(0);

        if(!memberMap.isEquals("pw", inputPw) && memberMap.isEquals("status", "사용")){
            int retry = memberMap.getInt("pwRetry")+1;
            if(retry > 4){
                //5회 이상 만료 처리
                userDAO.updateUserPwRetry(memberMap.getString("id"), retry, "만료");
            }else{
                //5회 미만에 대해서는 횟수만 +
                userDAO.updateUserPwRetry(memberMap.getString("id"), retry);
            }
            throw new BadCredentialsException("Invalid Username or Password");
        }

        if(memberMap.isEquals("pwStatus", "만료")){
            throw new LockedException("Invalid Username or Password");
        }

        if(!memberMap.isEquals("status", "사용")){
            throw new BadCredentialsException("Invalid Username or Password");
        }

        if(memberMap.isNullOrSpace("parentId")){
            throw new BadCredentialsException("Invalid Username or Password");
        }

        logger.info("----- login/in pw Check End -----");


        CustomUserDetail user = new CustomUserDetail();
        user.setUserId(memberId);
        user.setUserPw(memberPw);
        return user;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        System.out.println("############### " + request.getRequestURI());

        UserDetails userDetails = isValidUser(username, password);

        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        if (userDetails != null) {
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            // roles.add(new SimpleGrantedAuthority("IS_AUTHENTICATED_ANONYMOUSLY"));
            throw new BadCredentialsException("Incorrect user credentials !!");
        }

        //아이디, 비밀번호, 권한, customUserDetail 값 세팅
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username, password, roles);
        result.setDetails(userDetails);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}