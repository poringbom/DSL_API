package th.co.ktb.dsl.config.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.exception.JwtTokenException;
import th.co.ktb.dsl.model.common.ApiResponseError;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Slf4j
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	public JwtAuthenticationFailureHandler() {
		log.info(">> new "+JwtAuthenticationFailureHandler.class.getSimpleName());
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {
		log.debug("Pre-authenticated with JWT Rejecting access");
		if (ex instanceof JwtTokenException) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
        } else {
        		response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
		Utilities.filterStackTrace(ex);
        ApiResponseError err = new ApiResponseError("401-000", ex.getMessage(), ex.getStackTrace());
		log.info("write login error response.");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Utilities.getObjectMapper().writeValue(response.getWriter(), err);
	}
}
