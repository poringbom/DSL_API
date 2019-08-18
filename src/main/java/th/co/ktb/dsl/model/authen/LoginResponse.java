package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginResponse {
	@ApiModelProperty(position = 1, required=false)
	Boolean requireSetupPin = false;
	
	@ApiModelProperty(position = 2, required=false)
	String verifyTokenAction = null;
}

