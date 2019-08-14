package th.co.ktb.dsl.model.suspend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data 
public class SuspendRequestUpdate {

	@ApiModelProperty(position = 1, required=true)
	String requestNo;

	@ApiModelProperty(position = 2, required=true)
	SuspendReason requestReason;

	@ApiModelProperty(position = 3, required=true)
	SuspendStatus requestStatus;
}
