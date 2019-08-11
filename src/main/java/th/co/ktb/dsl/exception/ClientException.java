package th.co.ktb.dsl.exception;

import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
public class ClientException extends ApiException {
	String code = "409-000";
	public ClientException(String message) {
		this.message = message;
	}
}
