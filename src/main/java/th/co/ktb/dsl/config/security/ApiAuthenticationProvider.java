package th.co.ktb.dsl.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.ServiceSQL.LoginUser;;

@Slf4j
public class ApiAuthenticationProvider implements AuthenticationProvider {

	@Autowired JwtUtil jwtUtil;
	@Autowired ServiceSQL serviceSQL;
	
	public ApiAuthenticationProvider() {}
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		log.info("{}.authenticate()",ApiAuthenticationProvider.class.getSimpleName());
		
		LoginUser loginUser = new LoginUser((String)auth.getPrincipal(),(String)auth.getCredentials());
		loginUser = serviceSQL.getUserByLogin(loginUser);
		if (loginUser == null) {
			log.info("Invalid user or password.");
			throw new UsernameNotFoundException("Invalid user or password.");
		}
		UserToken userTokenVo = new UserToken();
		final Date currTime = DateUtil.currTime();
		userTokenVo.setLogin(loginUser.getLogin());
		userTokenVo.setUserID(loginUser.getUserID());
		userTokenVo.setAction("Login");
		Date expired = DateUtil.datePlusSec(currTime,(jwtUtil.getJwtExpireSec()));
		log.info("create token with expired time: "+expired);
		userTokenVo.setExpiredTime(expired);
		try {
			userTokenVo.setTokenValue(Utilities.getObjectMapper().writeValueAsString(userTokenVo));
		} catch (JsonProcessingException e) {
			throw new AuthenticationServiceException("Invalid token.");
		}
		serviceSQL.addNewToken(userTokenVo);
		log.info("userTokenVo -> {}",userTokenVo);
		String token = jwtUtil.generateToken(userTokenVo);
		userTokenVo.setTokenValue(token);
		return new ApiAuthenticationToken(auth.getName(), auth.getCredentials(), userTokenVo);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		Boolean ret = ApiAuthenticationToken.class.isAssignableFrom(authentication);
		log.info("{} supports(): {}",ApiAuthenticationProvider.class.getSimpleName(), ret);
		return ret;
	}
	
	
}
