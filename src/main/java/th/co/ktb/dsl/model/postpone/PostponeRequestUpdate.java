package th.co.ktb.dsl.model.postpone;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data 
public class PostponeRequestUpdate {
	@ApiModelProperty(position = 1, required=true)
	String requestID;

	@ApiModelProperty(position = 2, required=true)
	PostponeReason requestReason;

	@ApiModelProperty(position = 3, required=true)
	PostponeStatus requestStatus = PostponeStatus.WAIT_APPROVAL;
}