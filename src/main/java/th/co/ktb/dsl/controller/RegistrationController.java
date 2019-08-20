package th.co.ktb.dsl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.apidoc.ApiDocHeader;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocHeaderNoAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocResponseNoAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.authen.ActivateUserRequest;
import th.co.ktb.dsl.model.authen.ActivateUserResponse;
import th.co.ktb.dsl.model.authen.UserRegisterInfo;
import th.co.ktb.dsl.model.authen.UserRegisterResponse;
import th.co.ktb.dsl.model.user.ContactInfo;
import th.co.ktb.dsl.model.user.PasswordReset;
import th.co.ktb.dsl.model.user.PasswordReset.PasswordChange;
import th.co.ktb.dsl.model.user.PinSetup;
import th.co.ktb.dsl.model.user.PinSetup.PinChange;

@Api(tags="1.2. DSL-RMS : Registration API", 
	description="API เกี่ยวกับการลงทะเบียนใช้งาน")
@RestController
@RequestMapping("/api/v1/rms")
public class RegistrationController {
	
	private final String resetPIN = "resetPIN";
	@Testable
	@ApiOperation(value=resetPIN,
			notes="API สำหรับกำหนด PIN ใหม่ (กรณีไม่เคยมีมาก่อน หรือ ผู้ใช้ลืม PIN)")
	@ApiDocHeaderAuthorized2Authen
	@PostMapping("/pin")
	@ResponseStatus(HttpStatus.CREATED)
	public void resetPIN(
		@ApiParam(name="newPIN",type="body",value="New setup PIN request", required=true) @RequestBody PinSetup pinSetup
	) {}
	
	private final String changePIN = "changePIN";
	@Testable
	@ApiOperation(value=changePIN,
			notes="API สำหรับกำหนด PIN ใหม่ (ผุ้ใช้จำเป็นต้องทราบ PIN ปัจจุบัน)")
	@ApiDocHeaderAuthorized
	@PutMapping("/pin")
	@ResponseStatus(HttpStatus.OK)
	public void changePin(
		@ApiParam(name="changePIN",type="body",value="Change PIN request",required=true) @RequestBody PinChange pinChange
	) {}
	
	private final String resetPassword = "resetPassword";
	@Testable
	@ApiOperation(value=resetPassword,
			notes="API สำหรับกำหนดรหัสผ่านเข้าใช้งานระบบใหม่ (กรณีผู้ใช้ลืม PIN)")
	@ApiDocHeaderAuthorized2Authen
	@PostMapping("/password")
	@ResponseStatus(HttpStatus.CREATED)
	public void resetPassword(
		@ApiParam(name="passwordReset",type="body",value="New password", required=true) @RequestBody PasswordReset passwordReset
	) {}
	
	private final String changePassword = "changePassword";
	@Testable
	@ApiOperation(value=changePassword,
			notes="API สำหรับเปลี่ยนรหัสผ่านเข้าใช้งานระบบ (ผู้ใช้จำเป็นต้องทราบรหัสผ่านปัจจุบัน)")
	@ApiDocHeaderAuthorized
	@PutMapping("/password")
	@ResponseStatus(HttpStatus.OK)
	public void changePassword(
		@ApiParam(name="passwordChange",type="body",value="Change passowrd",required=true) @RequestBody PasswordChange passwordChange
	) {}

	private final String registerUser = "registerUser";
	@Testable
	@ApiOperation(value=registerUser,
			notes="API สำหรับลงทะเบียนการใช้งานระบบด้วยบัตรประชาชน")
	@ApiDocHeaderNoAuthorized2Authen
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.OK)
	public UserRegisterResponse registerUser(
		@ApiParam(name="userInfo",type="body",required=true, value="ข้อมูลผู้ใช้สำหรับการลงทะเบียนใช้งาน") 
		@RequestBody UserRegisterInfo userInfo
	) {
		return null;
	}

	private final String resendVerifyEmail = "resendVerifyEmail";
	@Testable
	@ApiOperation(value=resendVerifyEmail,
			notes="API สำหรับส่ง Email เพื่อยืนยันตัวตนเปิดการใช้งานผู้ใช้ในระบบ")
	@ApiDocHeader
	@ApiDocResponseNoAuthorized
	@PostMapping("/email")
	@ResponseStatus(HttpStatus.OK)
	public void resendVerifyEmail(
		@ApiParam(name="registerRef",type="body",required=true, value="Register reference ID") 
		@RequestBody ActivateUserRequest registerRef
	) {
		return;
	}
	
	private final String activateUser = "activateUser";
	@Testable
	@ApiOperation(value=activateUser,
			notes="API สำหรับเปิดการใช้งานผู้ใช้สำหรับกรณี ผู้ใช้ทำการเลือกช่องทางยืนยันตัวตนผ่านทาง Email.")
	@ApiDocHeaderNoAuthorized2Authen
	@ApiDocResponseNoAuthorized
	@PostMapping("/activation")
	@ResponseStatus(HttpStatus.OK)
	public ActivateUserResponse activateUser(
		@ApiParam(name="registerRef",type="body",required=true, value="Register reference ID") 
		@RequestBody ActivateUserRequest registerRef
	) {
		return null;
	}
	
/*
	private final String registerUserByOpenID = "registerUserByOpenID";
	@Testable
	@ApiOperation(value=registerUserByOpenID,hidden=true,
			notes="API สำหรับลงทะเบียนการใช้งานผ่านช่องทาง OpenID")
	@PostMapping("/register/openID")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void registerUserByOpenID(
		
	) {
		return;
	}
*/
}
