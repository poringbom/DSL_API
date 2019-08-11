package th.co.ktb.dsl.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonPropertyOrder(alphabetic=false)
public class ApiResponseError {
	@ApiModelProperty(position = 1, required=true)
	@JsonProperty("code") String code;
	@ApiModelProperty(position = 2, required=true)
	@JsonProperty("message") String message;
	@ApiModelProperty(position = 3)
	@JsonProperty("description") String description;
	@ApiModelProperty(position = 4)
	@JsonProperty("errors") List<Error> errors;
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