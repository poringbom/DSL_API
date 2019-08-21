package th.co.ktb.dsl.model.common;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import th.co.ktb.dsl.model.postpone.PostponeInfo;
import th.co.ktb.dsl.model.suspend.SuspendInfo;

@Data
public class RequestSummary {
	@ApiModelProperty(position = 1, required=true)
	boolean gracePeriod = false;
	PostponeSummary postponeSummary;
	SuspendSummary suspendSummary;
}

@Data
class PostponeSummary {
	
	@ApiModelProperty(position = 2, required=true)
	boolean onSuspendPeriod = false;

	@ApiModelProperty(position = 3, required=false)
	boolean overdueFlag = false;

	@ApiModelProperty(position = 4, required=false)
	Integer numApprovedRequest = 0;

	@ApiModelProperty(position = 8)
	PostponeInfo activeRequest;

	@ApiModelProperty(position = 9)
	List<PostponeInfo> history;
	
}

@Data
class SuspendSummary {
	@ApiModelProperty(position = 1, required=true)
	boolean gracePeriod = false;

	@ApiModelProperty(position = 8)
	SuspendInfo activeRequest;

	@ApiModelProperty(position = 9)
	List<SuspendInfo> history;
	
}