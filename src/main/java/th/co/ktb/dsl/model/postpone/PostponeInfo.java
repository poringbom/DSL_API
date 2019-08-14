package th.co.ktb.dsl.model.postpone;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data 
public class PostponeInfo {
	@ApiModelProperty(position = 1, required=true)
	String requestNo;

	@ApiModelProperty(position = 2, required=true)
	PostponeReason requestReason;

	@ApiModelProperty(position = 3, required=true)
	PostponeStatus requestStatus;

	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00", required=true)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date requestDate;

	@ApiModelProperty(position = 5, example="2019-08-07")
	@JsonFormat(pattern = "yyyy-MM-dd")  Date docSubmitDue;

	@ApiModelProperty(position = 6, example="2019-08-07")
	@JsonFormat(pattern = "yyyy-MM-dd") Date effectiveStart;

	@ApiModelProperty(position = 7, example="2019-08-07")
	@JsonFormat(pattern = "yyyy-MM-dd") Date effectiveEnd;

}