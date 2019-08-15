package th.co.ktb.dsl.model.suspend;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SuspendSummary {
	@ApiModelProperty(position = 1, required=true)
	boolean gracePeriod = false;

//	@ApiModelProperty(position = 2, required=false)
//	boolean overdueFlag = false;
	
//	@ApiModelProperty(position = 2, required=true)
//	boolean onSuspendPeriod = false;

	@ApiModelProperty(position = 8)
	SuspendInfo activeRequest;

	@ApiModelProperty(position = 9)
	List<SuspendInfo> history;
	
}
