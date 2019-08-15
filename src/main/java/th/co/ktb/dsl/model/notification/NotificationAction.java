package th.co.ktb.dsl.model.notification;

import lombok.Data;

@Data 
public class NotificationAction {
	NotificationActionType actionType;
	String actionInfo;
}
