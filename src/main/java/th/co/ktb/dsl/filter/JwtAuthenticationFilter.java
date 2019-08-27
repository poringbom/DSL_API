package th.co.ktb.dsl.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.SkipPathRequestMatcher;
import th.co.ktb.dsl.config.security.JwtAuthenticationToken;
import th.co.ktb.dsl.exception.JwtTokenException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired 
	public JwtAuthenticationFilter(SkipPathRequestMatcher matcher) {
		super(matcher);
		logger.info(">> new "+JwtAuthenticationFilter.class.getSimpleName());
	}
	
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
    		throws AuthenticationException {
    		logger.info("*** attempAuthen: jwttoken for request url: "+request.getRequestURL().toString());
        String hToken = request.getHeader(JwtUtil.JWT_AUTHORIZATION_HEADER);
        String qsToken = request.getParameter(JwtUtil.JWT_QUERY_STRING_PARAM);
        JwtAuthenticationToken authRequest = null;
        if (qsToken != null) {
        	logger.info("token come along with request via query string.");
        	authRequest = new JwtAuthenticationToken(qsToken);
        } else if (hToken != null && hToken.startsWith(JwtUtil.JWT_HEADER_PREFIX)) {
        		logger.info("token come along with request via header.");
	        String authToken = hToken.substring(7);
	        authRequest = new JwtAuthenticationToken(authToken);
        } else {
        		logger.debug("not found token.");
            throw new JwtTokenException("No JWT token found in request headers");
        }
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
    		FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}
