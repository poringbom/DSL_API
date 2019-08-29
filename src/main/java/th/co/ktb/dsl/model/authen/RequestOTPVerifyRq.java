package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RequestOTPVerifyRq {
	@ApiModelProperty(position = 1, required=true, example="MOBILE")
	VerifyOTPChannel channel;

	@ApiModelProperty(position = 2, required=true, example="0819248388")
	String channelInfo;
	
	@ApiModelProperty(position = 3, required=false, example="TEST")
	String objective;
}
