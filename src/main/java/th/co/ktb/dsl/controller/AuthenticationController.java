package th.co.ktb.dsl.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.apidoc.ApiDocAnnotation;
import th.co.ktb.dsl.apidoc.ApiDocHeader;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocHeaderOptionAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocResponseNewAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseNoAuthorized;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.mock.MockService;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.authen.VerifyOTPRq;
import th.co.ktb.dsl.model.authen.SignInRq;
import th.co.ktb.dsl.model.authen.SignInRs;
import th.co.ktb.dsl.model.authen.SignOutRq;
import th.co.ktb.dsl.model.authen.RequestOTPVerifyRq;
import th.co.ktb.dsl.model.authen.UserRegisterInfo;
import th.co.ktb.dsl.model.authen.RequestOTPVerifyRs;

@Api(tags="1.1. DSL-RMS : Authentication API", 
	description="API เกี่ยวกับการยืนยันตัวตนผู้ใช้ และการลงทะเบียนผู้ใช้ ")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthenticationController {
	
	@Autowired JwtUtil jwtUtil;
	@Autowired MockService mockService;
	@Autowired ServiceSQL serviceSQL;
	
	private final String signIn = "signIn";
//	@Testable
	@ApiOperation(value=signIn+Team.GATEWAY_TEAM, 
			notes="API สำหรับการ sign in โดยผลสำเร็จจะแนบ Token กลับมาด้วยใน header response ")
	@ApiDocHeader
	@ApiDocResponseNewAuthorized
	@PostMapping("/signIn")
	@ResponseStatus(HttpStatus.OK)
	public SignInRs signIn(
		@RequestBody SignInRq userLogin,
		HttpServletResponse response
	) throws Exception {
		log.info("signIn()");
		SignInRs rt = mockService.signIn(userLogin);
		response.setHeader("Authorization", "Bearer xxx");
		return rt;
	}
	
	private final String signOut = "signOut";
	@Testable(alwaysMock=false)
	@ApiOperation(value=signOut+Team.GATEWAY_TEAM, 
			notes="API สำหรับการ sign out ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PostMapping("/signOut")
	@ResponseStatus(HttpStatus.OK)
	public void signOut(
		@RequestBody SignOutRq userLogout
	) throws ClientException {
		String token = userLogout.getAccessToken();
		UserToken userToken = jwtUtil.parseToken(token);
		if (!token.equals( ApiDocAnnotation.NO_EXPIRE_TOKEN )) {
			log.info("remove token id: {}",userToken.getTokenID());
			Integer effect = serviceSQL.removeToken(userToken.getTokenID());
			if (effect == null || effect == 0) {
				throw new ClientException("401-000","Invalid token");
			} 
			log.info("Access token was removed from DB");
		} else {
			log.info("Access token is for test, no actually remove from DB");
		}
		return;
	}
	
	private final String refreshTokens = "refreshToken";
	@Testable(alwaysMock=false)
	@ApiOperation(value=refreshTokens+Team.GATEWAY_TEAM, 
			notes="API สำหรับรีเฟรช Token ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseNewAuthorized
	@PutMapping("/token")
	public RefreshTokenRs refreshToken(
		@ApiParam(name="refreshToken",value="Access token และ Refresh token ปัจจุบัน (ที่ยังไม่หมดอายุ)", required=true, type="RefreshToken") 
		@RequestBody RefreshTokenRq refreshToken
	) {
		String token = refreshToken.getAccessToken();
		UserToken userToken = jwtUtil.parseToken(token);
		if (!token.equals( ApiDocAnnotation.NO_EXPIRE_TOKEN )) {
			log.info("remove previous token id: {}",userToken.getTokenID());
			serviceSQL.removeToken(userToken.getTokenID());
		} else {
			log.info("Access token is for test, no actually remove from DB");
		}
		UserToken newToken = userToken.renewToken();
		serviceSQL.addNewToken(newToken);
		log.info("New token -> {}",newToken);
		String retToken = jwtUtil.generateToken(newToken);
		RefreshTokenRs ret = new RefreshTokenRs();
		ret.setAccessToken(retToken);
		ret.setRefreshToken("<RefreshToken>");
		ret.setExpiresIn(jwtUtil.getJwtExpireSec());
		return ret;
	}
	
	private final String verifyUser = "verifyUser";
	@Testable
	@ApiOperation(value=verifyUser, hidden=true,
			notes="API สำหรับขอยืนยันตัวตนด้วย รหัสผู้ใช้+รหัสลับ (user+password) เพื่อขอ Verify Token Action (ตัวอย่างใช้งานกรณีต้องการกำหนดรหัส PIN แต่ลืมรหัส PIN เก่า)​ ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized2Authen
	@PostMapping(path="/verif/user")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUser (
		@RequestBody VerifyUserRq userLogin
	) {}
	
	private final String requestOTPVerify = "requestOTPVerify";
	@Testable
	@ApiOperation(value=requestOTPVerify+Team.DSL_SECURITY_TEAM, 
			notes="API สำหรับขอยืนยันตัวตน ด้วย OTP / สำหรับกรณียืนยันตัวตนก่อนมี Authorization (เช่น การลงทะเบียนผู้ใช้)​ ไม่จำเป็นต้องแนบ Token หากมี Authorization แล้วจำเป็นต้องแนบ Token เสมอ")
	@ApiDocHeaderOptionAuthorized
	@PostMapping(path="/verif/otp/req")
	@ResponseStatus(HttpStatus.OK)
	public RequestOTPVerifyRs requestOTPVerify(
		@ApiParam(name="requestOTP", value="OTP request information", required=true, type="body") 
		@RequestBody RequestOTPVerifyRq requestOTP
	) {
		return new RequestOTPVerifyRs();
	}
	
	private final String verifyOTP = "verifyOTP";
	@Testable
	@ApiOperation(value=verifyOTP+Team.DSL_SECURITY_TEAM, 
			notes="API สำหรับตรวจสอบ OTP")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseAuthorized
	@PostMapping(path="/verif/otp")
	public VerifyOTPRs verifyOTP (
		@ApiParam(name="challengeOTP", value="Submited OTP", required=true, type="body") 
		@RequestBody VerifyOTPRq challengeOTP
	) {
		return null;
	}
	
	private final String requestEmailVerify = "requestEmailVerify";
	@Testable
	@ApiOperation(value=requestEmailVerify, hidden=true,
			notes="API สำหรับขอยืนยันตัวตน ด้วย Email / สำหรับกรณียืนยันตัวตนก่อนมี Authorization (การลงทะเบียนผู้ใช้)​")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseNoAuthorized
	@PostMapping(path="/verif/email")
	@ResponseStatus(HttpStatus.OK)
	public RequestOTPVerifyRs requestEmailVerify(
		@ApiParam(name="userRegister", value="User registration with verify by email", required=true, type="body") 
		@RequestBody UserRegisterInfo userRegister
	) {
		return new RequestOTPVerifyRs();
	}

	private final String verifyToken = "verifyToken";
	@Testable
	@ApiOperation(value=verifyToken+Team.GATEWAY_TEAM, 
			notes="API ตรวจสอบ access token ยังคงมีสิทธิ์อยู่หรือไม่​ ถูกใช้โดย client สำหรับการตรวจสอบเมื่อผู้ใช้มีการ access known url ของ application ตรง")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@RequestMapping(path="/verif/token",method=RequestMethod.HEAD)
	@ResponseStatus(HttpStatus.OK)
	public void verifyToken() {
		return;
	}
}

@Data
class VerifyOTPRs { 
	String verifyActionToken;
}

@Data
class RefreshTokenRq {
	@ApiModelProperty(position = 1, required=true, notes="Access Token")
	String accessToken;
	
	@ApiModelProperty(position = 2, required=true, notes="Refresh Token")
	String refreshToken;
}

@Data
class RefreshTokenRs {
	@ApiModelProperty(position = 1, required=true, notes="Access Token")
	String accessToken;
	
	@ApiModelProperty(position = 2, required=true, notes="Refresh Token")
	String refreshToken;
	
	@ApiModelProperty(position = 3, required=true, notes="Access Token will expires in x seconds.")
	Integer expiresIn = 900;
}

@Data
class VerifyUserRq {
	
	@ApiModelProperty(position = 1, required=true, example="pongchet@orcsoft.co.th")
	String username;

	@ApiModelProperty(position = 2, required=true, example="password")
	String password;
}
