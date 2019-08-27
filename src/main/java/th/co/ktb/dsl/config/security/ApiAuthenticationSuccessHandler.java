package th.co.ktb.dsl.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.model.authen.SignInRs;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Slf4j
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired JwtUtil jwtUtil;

	public ApiAuthenticationSuccessHandler() {
		log.info(">> new " + ApiAuthenticationSuccessHandler.class.getSimpleName());
	}
	
	public ApiAuthenticationSuccessHandler (JwtUtil jwtUtil) {
		this();
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
			throws IOException, ServletException  {
    	
	    	UserToken userTokenVo = ((ApiAuthenticationToken) authentication).getUserInfo();
//	    	String token = jwtUtil.generateToken(userTokenVo);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        SignInRs loginRsp = new SignInRs();
        loginRsp.setAccessToken(userTokenVo.getTokenValue());
        loginRsp.setExpiresIn(900);
        loginRsp.setRefreshToken("<RefreshToken>");
        Utilities.getObjectMapper().writeValue(response.getWriter(), loginRsp);
        
//        response.setHeader(JwtUtil.JWT_AUTHORIZATION_HEADER, JwtUtil.JWT_HEADER_PREFIX+token);
        clearAuthenticationAttributes(request);
    }
	
	/**
	 * Removes temporary authentication-related data which may have been stored in
	 * the session during the authentication process..
	 * 
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
