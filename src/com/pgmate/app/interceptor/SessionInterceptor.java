package com.pgmate.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pgmate.app.session.CPSession;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class SessionInterceptor extends HandlerInterceptorAdapter{
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.interceptor.SessionInterceptor.class );
	
	
	// KBR controller로 보내기 전에 처리하는 인터셉터
	// 반환이 false라면 controller로 요청을 안함
	@Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if(CPUtil.CP_DEBUG){
			logger.info("==========    START    =========");
			logger.info("URI : {} , {}",request.getRequestURI(),request.getMethod());
			request.setAttribute("ServletStartTime", System.currentTimeMillis());
			
		}
		logger.info("{},{},{}",request.getRequestURI(),request.getMethod(),CommonUtil.nToB(request.getHeader("X-Real-IP")));
		
		
		SessionExclude exclude = null;
		
		if (handler instanceof HandlerMethod){
			exclude = ((HandlerMethod) handler).getMethodAnnotation(SessionExclude.class);
		}else{
			//logger.info("HANDLER : {}",handler.getClass());
			//org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler
		}
		
		if(!"/check".equals(request.getRequestURI())) {
			if(exclude == null){
				String contentType = CommonUtil.nToB(request.getContentType()).toLowerCase();
				if(!SessionUtil.isLive(request)){
					logger.debug("SESSION IS NULL : {}",contentType);
					if(CPUtil.CP_DEV_SESSION){
						logger.debug("DEVELOP SESSION ");
						response.sendRedirect("/dev");
						return false;
					}
					
					//AJAX 요청의 경우 
					if(CommonUtil.nToB(request.getHeader("X-Requested-With")).equals("XMLHttpRequest")){
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"사용자 세션이 종료되었습니다.");
						logger.debug("AJAX SESSION EXPIRED : ");
						return false;
					}

					if (contentType.endsWith("html")) {
							response.sendRedirect("/login/form");
					} else {
						response.sendRedirect("/login/expired");
					}
					
					return false;
				}else{
					CPSession cpSession = SessionUtil.get(request);
					if(cpSession.getGrade().equals("가맹점") || cpSession.getGrade().equals("지사")){
						logger.info("{},{},{}",request.getRequestURI(),SessionUtil.getUserId(request),SessionUtil.getParentId(request));
						
					}else{
						
						DAO dao = new DAO();
						dao.setDebug(CPUtil.CP_DEBUG);
						logger.info("{},{},{}",request.getRequestURI(),SessionUtil.getUserId(request),SessionUtil.getParentId(request));
						// KBR : pg_user_todo 테이블 update ( 계정 메뉴 접속 기록 )
						dao.update("INSERT INTO PG_USER_TODO (id,uri,todo,regDay) VALUES ('"+SessionUtil.getUserId(request)+"',"
								+ "'"+CommonUtil.cut(request.getRequestURI(), 200)+"','접속',DATE_FORMAT(now(),'%Y%m%d'))");
					}
				}
			}
		}
		
        return true;
    }
	
	// KBR controller의 handler가 끝나면 처리됨
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	if(CPUtil.CP_DEBUG){
    		logger.info("==========    END SF  =========");
    	}
    }
 
    // KBR view까지 처리가 끝난 후에 처리됨
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	if(CPUtil.CP_DEBUG){
	    	long startTime = CommonUtil.parseLong(request.getAttribute("ServletStartTime"));
	    	logger.info("ElapsedTime : {}msec",(long)(System.currentTimeMillis()-startTime));
	    	if(ex != null){
	    		logger.debug("EXCEPTION STATUS : {}",response.getStatus());
	    	}
	    	logger.info("==========    END      =========");
    	}
    }
	

}
