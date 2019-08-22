package th.co.ktb.dsl.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.notification.NotificationList;

@Api(tags="0.2. DSL : Notification API", description="API เกี่ยวกับการแจ้งเตือน Notification")
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
	
	private final String listNotification = "listNotification";
	@Testable
	@ApiOperation(value=listNotification, tags=listNotification,
			notes="API สำหรับเรียกดูรายการแจ้งเตือน ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public NotificationList listNotification(
		@ApiParam(type="query", value="Data offset (order by last recently received)", required=false, defaultValue="1") @RequestParam(name="offset", required=false) String offset,
		@ApiParam(type="query", value="Limit data size (default = 5, All = -1)", required=false, defaultValue="-1") @RequestParam(name="size", required=false) String size
	) {
		return new NotificationList();
	}
	
	private final String acknowledgeNotification = "acknowledgeNotification";
	@Testable
	@ApiOperation(value=acknowledgeNotification, tags=acknowledgeNotification,
			notes="API สำหรับที่รับทราบรายการแจ้งเตือน ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PatchMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public void acknowledgeNotification(
		@ApiParam(name="List of accept notification.", type="body", required=true) @RequestBody(required=true) List<String> listNotfID
	) {
		return;
	}
}



