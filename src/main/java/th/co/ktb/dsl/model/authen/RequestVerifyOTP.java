package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RequestVerifyOTP {
	@ApiModelProperty(position = 1, required=true)
	VerifyOTPChannel channel;

	@ApiModelProperty(position = 2, required=true)
	String channelInfo;
}
