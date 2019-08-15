package th.co.ktb.dsl.model.notification;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotificationItem {
	@ApiModelProperty(position = 1, required=true)
	String notfID;
	
	@ApiModelProperty(position = 2, required=true)
	String notfHeader;
	
	@ApiModelProperty(position = 3, required=true)
	String notfBody;
	
	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00", required=true)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") 
	String notfDTM;
	
	@ApiModelProperty(position = 5, required=false)
	NotificationAction action;
	
	@ApiModelProperty(position = 6, required=true)
	Boolean isRead = false;
}
