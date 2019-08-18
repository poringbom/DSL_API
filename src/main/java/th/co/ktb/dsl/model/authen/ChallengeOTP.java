package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChallengeOTP {
	@ApiModelProperty(position = 1, required=true)
	String otp;
	
	@ApiModelProperty(position = 2, required=true)
	String refID;
}