package th.co.ktb.dsl.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Slf4j
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	public JwtAuthenticationSuccessHandler() {
		log.info(">> new "+JwtAuthenticationSuccessHandler.class.getSimpleName());
	}

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
    	// We do not need to do anything extra on REST authentication success, 
    	// because there is no page to redirect to
    }
}
