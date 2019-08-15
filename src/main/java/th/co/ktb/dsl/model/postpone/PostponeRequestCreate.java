package th.co.ktb.dsl.model.postpone;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data 
public class PostponeRequestCreate {
	@ApiModelProperty(position = 1, required=false)
	String requestNo;

	@ApiModelProperty(position = 2, required=true)
	PostponeReason requestReason;

	@ApiModelProperty(position = 3, required=true)
	PostponeStatus requestStatus;

	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00", required=false)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date requestDate;
	
	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00", required=false)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date docSubmitDue;

	@ApiModelProperty(position = 12, required=false)
	AdditionalRequestInfo additionInfo;
	
}

@Data
class AdditionalRequestInfo {
	@ApiModelProperty(position = 1, required=false)
	Double income; // example of additional info
	
	@ApiModelProperty(position = 2, required=false)
	List<FamilyMember> lookAfterMember;

	@ApiModelProperty(position = 10, notes="Reason - (Free text, up to 1,000 character)", required=false)
	String reason; // example of additional info
}

@Data
class FamilyMember {
	@ApiModelProperty(position = 1, required=true)
	FamilyMemberStatus type; // Senility, 
	
	@ApiModelProperty(position = 2, required=true)
	Integer number;
}

enum FamilyMemberStatus {
	SENILITY,
	DISABILITY,
	SICKNESS
}