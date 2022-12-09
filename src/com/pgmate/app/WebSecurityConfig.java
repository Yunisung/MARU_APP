package com.pgmate.app;

import com.pgmate.app.security.AjaxAuthenticationFailureHandler;
import com.pgmate.app.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/assets/**")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/**").permitAll() //전체 접근 허용
                .antMatchers("/login/form").permitAll()
                .antMatchers("/loginProcess").permitAll()
                .antMatchers("/login/invalidCheck").permitAll()
                .antMatchers("/logout/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login/form")
                .usernameParameter("memberId")
                .passwordParameter("memberPw")
                .loginProcessingUrl("/loginProcess")
                .defaultSuccessUrl("/login/invalidCheck", true)
                //.failureUrl("/login.html?error=true")
                .failureHandler(authenticationFailureHandler());
                //.and()
                //.logout()
                //.logoutUrl("/perform_logout")
                //.deleteCookies("JSESSIONID")
                //.logoutSuccessHandler(logoutSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.customAuthenticationProvider);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return ajaxAuthenticationFailureHandler;
    }
}