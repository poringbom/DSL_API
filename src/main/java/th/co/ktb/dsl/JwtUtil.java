package th.co.ktb.dsl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.mock.ServiceSQL;

@Component
@Slf4j
public class JwtUtil {
	
	@Autowired ServiceSQL sql;

	public static final String JWT_AUTHORIZATION_HEADER = "Authorization";
	public static final String JWT_HEADER_PREFIX = "Bearer ";
	public static final String JWT_HEADER_OTT_PREFIX = "Token ";
	public static final String JWT_QUERY_STRING_PARAM = "T";
	public static final String JWT_VERIFY_ACTION_TOKEN = "Verify-Token-Action";
	public static Integer JWT_EXPIRE_SEC = 900;
	
	@Getter private Integer jwtExpireSec = 900; //31536000
	@Getter private String secret = "576a4f0e-2aec-4870-bb35-93b63b9ebc3a";
    
    public JwtUtil() {
	    	log.info(">> new "+JwtUtil.class.getSimpleName());
	    	log.info(">>     svs.web-jwt.secret: "+secret);
	    	JWT_EXPIRE_SEC = jwtExpireSec;
    }

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     * 
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public UserToken parseToken(String token) throws JwtException, ClassCastException{
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            UserToken u = new UserToken();
            u.setLogin(body.getSubject());
            u.setUserID(Integer.parseInt((String) body.get("userID")));
            u.setTokenID((Integer) body.get("tokenID"));
            Long exprTime = Long.parseLong((String) body.get("expired"));
            u.setExpiredTime(new Date(exprTime));
            return u;

        } catch (JwtException | ClassCastException e) {
            throw e;
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     * 
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(UserToken u) {
        Claims claims = Jwts.claims().setSubject(u.getLogin());
        claims.put("userID", u.getUserID() + "");
        claims.put("action", u.getAction() + "");
        claims.put("tokenID", u.getTokenID());
        if (u.getExpiredTime() != null) {
        		claims.put("expired", u.getExpiredTime().getTime()  + "");
        } 
        
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    public String generateOneTimeToken(String login, Integer userID, String action) throws JsonProcessingException {
    		UserToken userToken = UserToken.createOneTimeToken();
    		userToken.setLogin(login);
		userToken.setUserID(userID);
		userToken.setAction(action);
//		userToken.setTokenValue(Utilities.getObjectMapper().writeValueAsString(userToken));
		sql.addNewToken(userToken);
		userToken.getTokenID();
		String verifyActionToken = generateToken(userToken);
		log.info("create verify-action-token for {}, tokenID: {}\n{}", action, userToken.getTokenID(), verifyActionToken);
    		return verifyActionToken;
    }
}
