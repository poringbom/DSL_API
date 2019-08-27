package th.co.ktb.dsl.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.model.common.ApiResponseError;

@Slf4j
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler{
	
	public ApiAuthenticationFailureHandler() {
		log.info(">> new " + ApiAuthenticationFailureHandler.class.getSimpleName());
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) 
			throws IOException, ServletException 
	{
		log.info("set response status: {}",HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
		Utilities.filterStackTrace(exception);
        ApiResponseError err = new ApiResponseError("401-000", exception.getMessage(), exception.getStackTrace());
		log.info("write login error response.");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Utilities.getObjectMapper().writeValue(response.getWriter(), err);
		
	}
}
