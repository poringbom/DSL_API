package th.co.ktb.dsl.exception;

@SuppressWarnings("serial")
public class ServerException extends ApiException {
	String code = "500-000";
	public ServerException(String message) {
		super.code = code;
		this.message = message;
	}
}
