package th.co.ktb.dsl.model.postpone;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PostponeSummary {
	@ApiModelProperty(position = 1, required=true)
	boolean gracePeriod = false;
	
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
