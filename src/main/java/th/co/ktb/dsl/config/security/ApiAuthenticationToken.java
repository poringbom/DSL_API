package th.co.ktb.dsl.config.security;

import java.util.List;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiAuthenticationToken extends TestingAuthenticationToken {

	private static final long serialVersionUID = 1L;
	private final UserToken userInfo;
	private String channel = null;

	public ApiAuthenticationToken(Object principal, Object credentials) {
		this(principal, credentials,null);
	}

	public ApiAuthenticationToken(Object principal, Object credentials, UserToken userInfo) {
		this(principal, credentials,null,userInfo);
	}

	public ApiAuthenticationToken(Object principal, Object credentials, List<GrantedAuthority> authorities, UserToken userInfo) {
		super(principal, credentials, authorities);
		this.userInfo = userInfo;
	}

	public UserToken getUserInfo() {
		return userInfo;
	}
}
