package th.co.ktb.dsl.config.security;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.config.Constants;

@Data
@JsonInclude(value=Include.NON_NULL)
public class UserToken {
	Integer tokenID;
	String login;
	Integer userID;
	String tokenValue;
	String action;
	Date expiredTime;
	
	public UserToken renewToken() {
		UserToken ret = new UserToken();
		ret.login = login;
		ret.userID = userID;
		ret.tokenID = null;
		ret.action = "Login";
		ret.expiredTime = DateUtil.currDatePlusSec(JwtUtil.JWT_EXPIRE_SEC); 
		try {
			ret.tokenValue = Utilities.getObjectMapper().writeValueAsString(ret);
		} catch(Exception ex) {}
		return ret;
	}
	
	public static UserToken createOneTimeToken() {
		UserToken ret = new UserToken();
		ret.login = "any";
		ret.userID = 0;
		ret.tokenID = null;
		ret.action = Constants.TOKEN_ONE_TIME;
		ret.expiredTime = DateUtil.currDatePlusSec(JwtUtil.JWT_EXPIRE_SEC); 
		try {
			ret.tokenValue = Utilities.getObjectMapper().writeValueAsString(ret);
		} catch(Exception ex) {}
		return ret;
	}
}
