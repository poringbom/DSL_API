package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VerifyOTP extends RequestVerifyOTP{
	@ApiModelProperty(position = 10, required=true)
	String refID;
	@ApiModelProperty(position = 11, required=true)
	String validPeriod = "03:00";
}
