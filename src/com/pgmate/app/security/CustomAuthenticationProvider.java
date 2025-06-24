package com.pgmate.app.security;

import com.pgmate.app.dao.MchtDAO;
import com.pgmate.app.dao.MchtTmnDAO;
import com.pgmate.app.dao.UserDAO;
import com.pgmate.app.dao.UserIpDAO;
import com.pgmate.app.security.filter.CustomWebAuthenticationDetails;
import com.pgmate.app.util.WebCache;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.security.CustomAuthenticationProvider.class );
    private SharedMap<String, Long> loginMap = new SharedMap<String, Long>();

    CustomUserDetail isValidUser(String memberId, String memberPw, String smsKey, String ip, String userAgent) {

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
            if(rset2.size() > 0) {
                SharedMap<String,Object> tmnMap = rset2.getRow(0);
                SharedMap<String,Object> tmnMchtMap = new MchtDAO().getById(tmnMap.getString("mchtId")).getRow(0);

                // 터미널 인증 검사
                if(tmnMchtMap.getString("aggregator").equalsIgnoreCase("Y") && tmnMap.isEquals("serial", memberPw) && tmnMap.isEquals("status", "사용")){
                    CustomUserDetail user = new CustomUserDetail();
                    user.setUserId(memberId);
                    user.setUserPw(memberPw);
                    user.setUserType("터미널");
                    user.setInitMap(tmnMap);
                    return user;
                } else {
                    throw new BadCredentialsException("NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
                }
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
            throw new BadCredentialsException("NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }

        if(memberMap.isEquals("pwStatus", "만료")){
            throw new BadCredentialsException("NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }

        if(!memberMap.isEquals("status", "사용")){
            throw new BadCredentialsException("NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }

        if(memberMap.isNullOrSpace("parentId")){
            throw new BadCredentialsException("NOK||등록되지 않은 아이디이거나, 아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }

        logger.info("----- login/in pw Check End -----");

        if(!smsKey.isEmpty()) {
            /* 현재 IP를 인증된 IP로 등록 */
            WebCache wc = new WebCache();
            String cSMSKey = wc.getSMSKey(memberId);
            logger.info("----- loing/in IP Check -----");
            if(smsKey.equals(cSMSKey)) {
                new UserIpDAO().insertIp(memberId, ip, userAgent);
            } else {
                throw new BadCredentialsException("INVALIDKEY||인증번호가 올바르지 않습니다.||MEMBER");
            }
        } else {
            //여기서 SMS문자 세팅
            WebCache wc = new WebCache();
            String number = String.format("%1$" + 6 + "s", ((int) (Math.random() * 999999) + 1)).replace(' ', '0');
            wc.setSMSKey(memberId, number);

            //PYS : 중복로그인금지
            if(!loginMap.containsKey(memberId)) {
                Date date = new Date();
                loginMap.put(memberId, date.getTime());

                throw new BadCredentialsException("UNAUTHORIZED||SMS 인증이 필요합니다.||MEMBER");
            } else {

                long prevtime = loginMap.get(memberId);
                Date date = new Date();
                long curtime = date.getTime();

                long diff = curtime - prevtime;
                logger.debug("loginMap diff : {}", diff);

                //PYS : 1초안에 같은 요청이 들어오면 무시
                if(diff < 1000) {
                    throw new BadCredentialsException("");
                } else {
                    throw new BadCredentialsException("UNAUTHORIZED||SMS 인증이 필요합니다.||MEMBER");
                }

            }

            //PYS_0421 : 로그인마다 sms인증 하도록 변경
//            UserIpDAO userIpDAO = new UserIpDAO();
//            /* 인증된 아이피인지 확인 */
//            rset = userIpDAO.getByActiveIp(memberId);
//            if (rset.size() < 1) {
//                logger.info("----- loing/in IP ADD -----");
//                throw new BadCredentialsException("UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||MEMBER");
//            }
//            boolean authorized = false;
//            List<SharedMap<String, Object>> ipList = rset.getRows();
//            for (SharedMap<String, Object> eachMap : ipList) {
//                if (eachMap.getString("ipAddr").equals(ip)) {
//                    authorized = true;
//                    logger.debug("EXPIREDAY UPDATE : {}", userIpDAO.updateExpireDay(memberId, ip));
//                    break;
//                }
//            }
//
//            if (!authorized) {
//                throw new BadCredentialsException("UNAUTHORIZED||등록되지 않은 IP로 접속요청.<br>SMS 인증이 필요합니다.||MEMBER");
//            }
        }

        if(loginMap.containsKey(memberId)) {
            loginMap.remove(memberId);
        }

        logger.debug("loginMap : {}", loginMap.toJson());

        logger.info("----- login/in smsKey Check End -----");


        CustomUserDetail user = new CustomUserDetail();
        user.setUserId(memberId);
        user.setUserPw(memberPw);
        user.setUserGrade(memberMap.getString("grade"));
        user.setUserRole(memberMap.getString("role"));
        user.setUserType("일반");
        return user;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        CustomWebAuthenticationDetails details = ((CustomWebAuthenticationDetails)authentication.getDetails());
        String smsKey = details.getSmsKey();
        String ip = details.getIp();
        String userAgent = details.getUserAgent();

        CustomUserDetail userDetails = isValidUser(username, password, smsKey, ip, userAgent);

        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        if (userDetails != null) {
            // role 부여
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));

            if(userDetails.getUserType().equals("일반")) {
                String grantedRole = "";
                // grade에 따른 role 부여
                if (userDetails.getUserGrade().equals("본사")) {
                    grantedRole = "ROLE_COMP";
                } else if (userDetails.getUserGrade().equals("대행사")) {
                    grantedRole = "ROLE_DIST";
                } else if (userDetails.getUserGrade().equals("에이전시")) {
                    grantedRole = "ROLE_AGENCY";
                } else if (userDetails.getUserGrade().equals("지사")) {
                    grantedRole = "ROLE_SALES";
                } else if (userDetails.getUserGrade().equals("가맹점")) {
                    grantedRole = "ROLE_MCHT";
                }
                // 관리자라면 _ADMIN 부여
                if (userDetails.getUserRole().equals("관리자") || userDetails.getUserRole().equals("마스터")) {
                    grantedRole = grantedRole + "_ADMIN";
                }

                // grade에 따른 role 부여
                roles.add(new SimpleGrantedAuthority(grantedRole));

            } else if(userDetails.getUserType().equals("터미널")) {
                roles.add(new SimpleGrantedAuthority("ROLE_TMN"));
            }

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