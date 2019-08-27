package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignInRs {
	@ApiModelProperty(position = 1, required=true, notes="Access Token")
	String accessToken;
	
	@ApiModelProperty(position = 2, required=true, notes="Refresh Token")
	String refreshToken;
	
	@ApiModelProperty(position = 3, required=true, notes="Access Token will expires in x seconds.")
	Integer expiresIn = 900;
}

