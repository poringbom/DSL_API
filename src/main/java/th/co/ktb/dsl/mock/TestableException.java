package th.co.ktb.dsl.mock;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.ktb.dsl.model.common.ApiResponseError;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class TestableException extends Exception{
	Integer statusCode;
	ApiResponseError apiResponseError;
	public TestableException (Integer statusCode, ApiResponseError apiResponseError) {
		this.statusCode = statusCode;
		this.apiResponseError = apiResponseError;
	} 

	public TestableException(Throwable t) {
		super(t);
	}
	
}
