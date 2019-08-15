package th.co.ktb.dsl.model.notification;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NotificationList {
	@JsonProperty("total") int totalRecord = 0; 
	List<NotificationItem> notification;
}