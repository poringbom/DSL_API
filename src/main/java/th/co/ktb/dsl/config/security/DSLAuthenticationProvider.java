package th.co.ktb.dsl.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DSLAuthenticationProvider implements AuthenticationProvider {
	
	@Value("${spring.security.user.name}")
	String SWAGGER_USER;
	@Value("${spring.security.user.password}")
	String SWAGGER_PASSWORD;
	
	public DSLAuthenticationProvider() {}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("{}.authenticate()",DSLAuthenticationProvider.class.getSimpleName());
		
		String login = (String) authentication.getPrincipal();
		String password = (String)authentication.getCredentials();
		log.info("Login DSL-API Spec with u: {} , p: {}",login,password);
		if (!(SWAGGER_USER.equalsIgnoreCase(login) && SWAGGER_PASSWORD.equalsIgnoreCase(password))) {
			log.info("Invalid user or password.");
			throw new UsernameNotFoundException("Invalid user or password.");
		}
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();	
		grantedAuths.add(DSLGrantedAuthority.DUMMY_ICASGrantedAuthority);
		Authentication ret = new UsernamePasswordAuthenticationToken(login, password, grantedAuths);
		log.info("Login success");
		return ret;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		Boolean ret = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
		log.info("{} supports(): {}",DSLAuthenticationProvider.class.getSimpleName(), ret);
		return ret;
	}
	
	
}
