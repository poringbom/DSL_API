package th.co.ktb.dsl.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(value=Include.NON_EMPTY)
public class WorkInfo {
	@ApiModelProperty(position = 1, required=false)
	Occupation occupation;
	
	@ApiModelProperty(position = 2, required=false)
	String other;
	
	@ApiModelProperty(position = 3, required=false)
	Double annualIncome;
	
	@ApiModelProperty(position = 4, required=false)
	String company;

	@ApiModelProperty(position = 5, required=false)
	WorkingAddressInfo workingAddress;
}

enum Occupation {
	TEACHER, IT_OFFICER, ENGINEER, ARCHIETECT, OTHER
}

