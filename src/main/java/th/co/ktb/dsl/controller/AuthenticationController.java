package th.co.ktb.dsl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
import th.co.ktb.dsl.apidoc.ApiDocHeaderOptionAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocResponseNewAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.authen.ChallengeOTP;
import th.co.ktb.dsl.model.authen.LoginRequest;
import th.co.ktb.dsl.model.authen.LoginResponse;
import th.co.ktb.dsl.model.authen.RequestVerifyOTP;
import th.co.ktb.dsl.model.authen.VerifyOTP;

//@Api(tags="1.1. DSL-RMS : Authentication API", 
//	description="API เกี่ยวกับการยืนยันตัวตนผู้ใช้ และการลงทะเบียนผู้ใช้ ")
@RestController
@RequestMapping("/api/v1")

public class AuthenticationController {
	
	private final String login = "login";
	@Testable
	@ApiOperation(value=login,
			notes="API สำหรับการ Authentication โดยผลสำเร็จจะแนบ Token กลับมาด้วยใน header response ")
	@ApiDocHeader
	@ApiDocResponseNewAuthorized
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public LoginResponse login(
		@RequestBody LoginRequest login
	) {
		return new LoginResponse();
	}
	
	private final String logout = "logout";
	@Testable
	@ApiOperation(value=logout,
			notes="API สำหรับการ logout ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PostMapping("/logout")
	public void logout(
	) {}
	
	private final String refreshToken = "refreshToken";
	@Testable
	@ApiOperation(value=refreshToken,
			notes="API สำหรับรีเฟรช Token กรณี Token ใกล้หมดอายุ "
				+ "/ ในส่วน Backend ควร implement ตรวจสอบ Token หากใกล้หมดอายุ ควรบังคับให้ Frontend request service นี้เป็นลำดับแรกก่อนจะเรียก service ใดๆ ต่อไป")
	@ApiDocHeaderAuthorized
	@ApiDocResponseNewAuthorized
	@PutMapping("/token")
	public void refreshToken(
	) {}
	
	private final String verifyUser = "verifyUser";
	@Testable
	@ApiOperation(value=verifyUser,
			notes="API สำหรับขอยืนยันตัวตนด้วย รหัสผู้ใช้+รหัสลับ (user+password) เพื่อขอ Verify Token Action (ตัวอย่างใช้งานกรณีลืมรหัส PIN)​ ")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseAuthorized2Authen
	@GetMapping(path="/verifyUser")
	@ResponseStatus(HttpStatus.OK)
	public VerifyOTP verifyUser (
		@ApiParam(name="requestOTP", value="OTP request information", required=true, type="body") @RequestBody RequestVerifyOTP requestOTP
	) {
		return new VerifyOTP();
	}
	
	private final String requestOTPVerify = "requestOTPVerify";
	@Testable
	@ApiOperation(value=requestOTPVerify,
			notes="API สำหรับขอยืนยันตัวตน ด้วย OTP / สำหรับกรณียืนยันตัวตนก่อนมี Authorization (เช่น การลงทะเบียนผู้ใช้)​ ไม่จำเป็นต้องแนบ Token หากมี Authorization แล้วจำเป็นต้องแนบ Token เสมอ")
	@ApiDocHeaderOptionAuthorized
	@GetMapping(path="/otp")
	@ResponseStatus(HttpStatus.OK)
	public VerifyOTP requestOTPVerify(
		@ApiParam(name="requestOTP", value="OTP request information", required=true, type="body") @RequestBody RequestVerifyOTP requestOTP
	) {
		return new VerifyOTP();
	}
	
	private final String verifyOTP = "verifyOTP";
	@Testable
	@ApiOperation(value=verifyOTP,
			notes="API สำหรับตรวจสอบ OTP")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseAuthorized2Authen
	@GetMapping(path="/otp/verify")
	public void verifyOTP (
		@ApiParam(name="challengeOTP", value="Submited OTP", required=true, type="body") @RequestBody ChallengeOTP challengeOTP
	) {
		
	}

}
