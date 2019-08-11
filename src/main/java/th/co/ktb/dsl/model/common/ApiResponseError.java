package th.co.ktb.dsl.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic=false)
@JsonInclude(Include.NON_NULL)
public class ApiResponseError {
	@ApiModelProperty(position = 1, required=true)
	@NonNull String code;
	
	@ApiModelProperty(position = 2, required=true)
	@NonNull String message;
	
	@ApiModelProperty(position = 3)
	String description;
	
	@ApiModelProperty(position = 4)
	StackTraceElement[] trace;
	
	@ApiModelProperty(position = 11)
	List<Error> errors;
	
	public ApiResponseError(String code, String message, StackTraceElement[] trace) {
		this(code, message);
		this.trace = trace;
	}
}

@Data
class Error {
	@ApiModelProperty(position = 1, required=true)
	String code;
	@ApiModelProperty(position = 2)
	String field;
	@ApiModelProperty(position = 3, required=true)
	String message;
}