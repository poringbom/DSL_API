package th.co.ktb.dsl.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.annotation.ApiMetadata;
import th.co.ktb.dsl.model.common.ApiResponseError;
import th.co.ktb.dsl.model.user.LoanInfo;
import th.co.ktb.dsl.model.user.UserInfo;

@Api(tags="DSL : User API", description="API หมวดเกี่ยวกับข้อมูลผู้ใช้งานระบบ")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@ApiOperation(hidden=true,
			value="API สำหรับการ Authentication โดยผลสำเร็จจะแนบ Token กลับมาด้วยใน header response ")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK", responseHeaders = {
		        @ResponseHeader(name = "Authorization", description = "JWT Token", response = String.class),
		    }),
		    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
		})
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public void login(
		@RequestParam("userID") String userID,
		@RequestParam("password") String password
	) {
		
	}
	
	@ApiOperation(hidden=true,
			value="API สำหรับการ logout ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PostMapping("/logout")
	public void logout(
		@RequestHeader("Authorization") String token
	) {}
	
	@ApiOperation(hidden=true,
			value="API สำหรับดึงข้อมูล profile, contact ของผู้ใช้ และข้อมูลสิทธิ์ทีจำเป็นในการแสดงผลในบางเมนู " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/info" , produces=MediaType.APPLICATION_JSON_VALUE)
	public UserInfo getUser() {
		return new UserInfo();
	}
	
	@ApiOperation(hidden=true,
			value="API สำหรับดึงข้อมูลรายการบัญขีกู้ยืมทั้งหมดของผู้ใชู้ " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/account" , produces=MediaType.APPLICATION_JSON_VALUE)
	public List<LoanInfo> getAccount() {
		return LoanInfo.getExampleLoanInfo();
	}

	private final String requestOTPVerify = "requestOTPVerify";
	@Testable
	@ApiOperation(value=requestOTPVerify,
			notes="API สำหรับขอยืนยันตัวตน ด้วย OTP ก่อนการลงทะเบียน")
	@ApiImplicitParams({
		@ApiImplicitParam(name = ApiMetadata.HEADER_NAME, value = "Metadata information of service request. syntax: "
				+ "src=<channel>;dest=<service-destination>;service=<api-sevice-name>", required = true, 
				allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
				example = "src=android;dest=dsl-dms;service=payment"),
	})
	@GetMapping(path="/otp")
	@ResponseStatus(HttpStatus.OK)
	public VerifyOTP requestOTPVerify(
		@ApiParam(value="OTP request information", required=true, type="body")@RequestBody RequestVerifyOTP requestOTP
	) {
		return new VerifyOTP();
	}
}

enum VerifyOTPChannel {
	MOBILE, EMAIL;
}

@Data
class RequestVerifyOTP {
	@ApiModelProperty(position = 1, required=true)
	VerifyOTPChannel channel;

	@ApiModelProperty(position = 2, required=true)
	String channelInfo;
}

@Data
@EqualsAndHashCode(callSuper=false)
class VerifyOTP extends RequestVerifyOTP{
//	String verifyToken;
//	String otp;
	@ApiModelProperty(position = 10, required=true)
	String refID;
	@ApiModelProperty(position = 11, required=true)
	String validPeriod = "03:00";
}
