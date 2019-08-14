package th.co.ktb.dsl.model.suspend;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data 
public class SuspendRequestCreate {

	@ApiModelProperty(position = 1, required=false)
	String requestNo;

	@ApiModelProperty(position = 2, required=true)
	SuspendReason requestReason;

	@ApiModelProperty(position = 3, required=true)
	SuspendStatus requestStatus;

	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00", required=false)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date requestDate;
	
	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00", required=false)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date docSubmitDue;

	@ApiModelProperty(position = 15, notes="Reason - (Free text, up to 1,000 character)", required=false)
	String reason; // example of additional info
}
