package com.pgmate.app.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String smsKey = request.getParameter("smsKey");

        String usernameDomain = String.format("%s%s%s", username.trim(), String.valueOf(Character.LINE_SEPARATOR), smsKey);
        return new UsernamePasswordAuthenticationToken(usernameDomain, password);
    }

    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        super.setDetails(request, authRequest);
        authRequest.setDetails(customWebAuthenticationDetailsSource.buildDetails(request));
    }

}
