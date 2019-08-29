package th.co.ktb.dsl.config.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.config.Constants;
import th.co.ktb.dsl.exception.JwtTokenException;
import th.co.ktb.dsl.mock.ServiceSQL;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired ServiceSQL serviceSQL;
	private JwtUtil jwtUtil;
	
	public JwtAuthenticationProvider (JwtUtil jwtUtil) {
		this.jwtUtil =  jwtUtil;
	}

    @Override
    public boolean supports(Class<?> authentication) {
		Boolean ret = JwtAuthenticationToken.class.isAssignableFrom(authentication);
		log.info("{} supports(): {}",JwtAuthenticationToken.class.getSimpleName(), ret);
		return ret;
    }
    
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("JwtAuthenticationProvider.authenticate()");
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();
        log.info("parse token: {}",token);
        	UserToken userToken = jwtUtil.parseToken(token);
        log.info("token ID: {}, with userID: {}, login: {}, expired(from token) : {}", 
        		userToken.getTokenID(),
        		userToken.getUserID(),
        		userToken.getLogin(),
        		userToken.getExpiredTime());
        Date tokenExpire = userToken.getExpiredTime();
        
        UserToken dbUserToken =  serviceSQL.checkToken(userToken.getTokenID());
        if (dbUserToken != null) {
        		log.info("expired(from db) : {}",dbUserToken.getExpiredTime()); 
        		tokenExpire = dbUserToken.getExpiredTime();
        }
        
        Boolean isExpired = tokenExpire.before(DateUtil.currTime());
        if (isExpired) {
	        	log.error("token ID: "+userToken.getTokenID()+" is invalid. "+"(expired:"+isExpired+")");
	        	throw new JwtTokenException("JWT token is expired");
        }
        if (!Constants.TOKEN_ACCESS.equals(dbUserToken.getAction())) {
        		log.info("remove {} token, id: {} from db.",dbUserToken.getAction(),dbUserToken.getTokenID());
        		serviceSQL.removeToken(dbUserToken.getTokenID());
        }
        
        log.info("token ID: "+userToken.getTokenID()+" still valid.");
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        grantedAuths.add(DSLGrantedAuthority.DUMMY_ICASGrantedAuthority);
        Authentication auth = new ApiAuthenticationToken(userToken.getLogin(), null, grantedAuths, userToken);
        return auth;
	}

}

