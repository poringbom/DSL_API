package th.co.ktb.dsl.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JwtTokenException(String msg) {
		super(msg);
	}
}
