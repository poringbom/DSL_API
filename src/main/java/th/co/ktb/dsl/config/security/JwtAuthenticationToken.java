package th.co.ktb.dsl.config.security;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token ;
	private String principal;
	private String credential;

	public JwtAuthenticationToken(String aPrincipal, String aCredentials, List<GrantedAuthority> authors) {
		this(aPrincipal,aCredentials,null,authors);
	}

	public JwtAuthenticationToken(String aPrincipal, String aCredentials, String token, List<GrantedAuthority> authors) {
		super(authors);
		principal = aPrincipal;
		credential = aCredentials;
		this.token = token;
	}
	
	public JwtAuthenticationToken(String token) {
		this(null,null,token,null);
	}
	
	public String getToken() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return credential;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

}
