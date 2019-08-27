package th.co.ktb.dsl.filter;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.config.security.ApiAuthenticationToken;
import th.co.ktb.dsl.model.authen.SignInRq;

@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {
	
	public ApiAuthenticationFilter(String defaultUrl) {
		super(defaultUrl);
		logger.info(">> new " + ApiAuthenticationFilter.class.getSimpleName());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		log.info("*** attempAuthen: api authen for request url: " + request.getRequestURL().toString());
		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			logger.info("Authentication method not supported. Request method: " + request.getMethod());
			throw new AuthenticationServiceException("Authentication method not supported");
		}
		
		// ===== check login, password =====
		String body = "", temp; 
		try (BufferedReader br = request.getReader()){
			while ((temp = br.readLine()) != null) {
				body += temp;
			};
		}
		log.info("request body: {}",body);
		
		SignInRq loginRq = Utilities.getObjectMapper().readValue(body, SignInRq.class);
		String login = loginRq.getUsername();
		String password = loginRq.getPassword();
		log.info("login user: [{}] , pwd: [{}]",login, password);
		if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
			throw new AuthenticationServiceException("Username or Password not provided");
		}
		
		
		// ===== set token =====
		ApiAuthenticationToken token = new ApiAuthenticationToken(login, password);
		logger.info("start authenticate user: " + login);
		Authentication authen = this.getAuthenticationManager().authenticate(token);
		return authen;
	}




}
