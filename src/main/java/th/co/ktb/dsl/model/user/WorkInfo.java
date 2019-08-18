package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkInfo {
	@ApiModelProperty(position = 1, required=false)
	String occupation;
	
	@ApiModelProperty(position = 2, required=false)
	String annualIncome;
	
	@ApiModelProperty(position = 3, required=false)
	String company;
}