package th.co.ktb.dsl.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.mock.MockService;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.authen.ChallengeOTP;
import th.co.ktb.dsl.model.authen.LoginRequest;
import th.co.ktb.dsl.model.authen.LoginResponse;
import th.co.ktb.dsl.model.authen.LogoutRequest;
import th.co.ktb.dsl.model.authen.RequestVerifyOTP;
import th.co.ktb.dsl.model.authen.UserRegisterInfo;
import th.co.ktb.dsl.model.authen.VerifyOTP;

@Api(tags="1.1. DSL-RMS : Authentication API", 
	description="API เกี่ยวกับการยืนยันตัวตนผู้ใช้ และการลงทะเบียนผู้ใช้ ")
@RestController
@RequestMapping("/api/v1")

public class AuthenticationController {
	
	@Autowired MockService mockService;
	
	private final String signIn = "signIn";
//	@Testable
	@ApiOperation(value=signIn+Team.GATEWAY_TEAM, 
			notes="API สำหรับการ sign in โดยผลสำเร็จจะแนบ Token กลับมาด้วยใน header response ")
	@ApiDocHeader
	@ApiDocResponseNewAuthorized
	@PostMapping("/signIn")
	@ResponseStatus(HttpStatus.OK)
	public LoginResponse signIn(
		@RequestBody LoginRequest userLogin,
		HttpServletResponse response
	) throws Exception {
		LoginResponse rt = mockService.signIn(userLogin);
		response.setHeader("Authorization", "Bearer xxx");
		return rt;
	}
	
	private final String signOut = "signOut";
	@Testable
	@ApiOperation(value=signOut+Team.GATEWAY_TEAM, 
			notes="API สำหรับการ sign out ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PostMapping("/signOut")
	public void signOut(
		@RequestBody LogoutRequest userLogout
	) {}
	
	private final String refreshTokens = "refreshToken";
	@Testable
	@ApiOperation(value=refreshTokens+Team.GATEWAY_TEAM, 
			notes="API สำหรับรีเฟรช Token ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseNewAuthorized
	@PutMapping("/token")
	public LoginResponse refreshToken(
		@ApiParam(name="refreshToken",value="Access token และ Refresh token ปัจจุบัน (ที่ยังไม่หมดอายุ)", required=true, type="RefreshToken") 
		@RequestBody LogoutRequest refreshToken
	) {
		return null;
	}
	
	private final String verifyUser = "verifyUser";
	@Testable
	@ApiOperation(value=verifyUser, 
			notes="API สำหรับขอยืนยันตัวตนด้วย รหัสผู้ใช้+รหัสลับ (user+password) เพื่อขอ Verify Token Action (ตัวอย่างใช้งานกรณีลืมรหัส PIN)​ ")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseAuthorized2Authen
	@GetMapping(path="/verifyUser")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUser (
		@RequestBody LoginRequest userLogin
	) {}
	
	private final String requestOTPVerify = "requestOTPVerify";
	@Testable
	@ApiOperation(value=requestOTPVerify+Team.DSL_SECURITY_TEAM, 
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
	@ApiOperation(value=verifyOTP+Team.DSL_SECURITY_TEAM, 
			notes="API สำหรับตรวจสอบ OTP")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseAuthorized2Authen
	@GetMapping(path="/otp/verify")
	public void verifyOTP (
		@ApiParam(name="challengeOTP", value="Submited OTP", required=true, type="body") @RequestBody ChallengeOTP challengeOTP
	) {
		
	}
	
	private final String requestEmailVerify = "requestEmailVerify";
	@Testable
	@ApiOperation(value=requestEmailVerify, 
			notes="API สำหรับขอยืนยันตัวตน ด้วย Email / สำหรับกรณียืนยันตัวตนก่อนมี Authorization (การลงทะเบียนผู้ใช้)​")
	@ApiDocHeaderOptionAuthorized
	@GetMapping(path="/email/verify")
	@ResponseStatus(HttpStatus.OK)
	public VerifyOTP requestEmailVerify(
		@ApiParam(name="userRegister", value="User registration with verify by email", required=true, type="body") @RequestBody UserRegisterInfo userRegister
	) {
		return new VerifyOTP();
	}

}
