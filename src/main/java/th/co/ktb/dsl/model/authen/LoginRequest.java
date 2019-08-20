package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {
	
	@ApiModelProperty(position = 1, required=true)
	String username;

	@ApiModelProperty(position = 2, required=true)
	String password;
}
