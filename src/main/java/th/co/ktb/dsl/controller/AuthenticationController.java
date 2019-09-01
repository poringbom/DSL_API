package th.co.ktb.dsl.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.apidoc.ApiDocAnnotation;
import th.co.ktb.dsl.apidoc.ApiDocHeader;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocHeaderOptionAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocResponseNewAuthorized;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.mock.MockService;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.mock.ServiceSQL.LoginUser;
import th.co.ktb.dsl.model.authen.VerifyOTPRq;
import th.co.ktb.dsl.model.authen.SignInRq;
import th.co.ktb.dsl.model.authen.SignInRs;
import th.co.ktb.dsl.model.authen.SignOutRq;
import th.co.ktb.dsl.model.authen.MockOTP;
import th.co.ktb.dsl.model.authen.RequestOTPVerifyRq;
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
	@Testable(alwaysMock=false)
	@ApiOperation(value=verifyUser, 
			notes="API สำหรับขอยืนยันมีตัวตนผู้ใช้ในระบบด้วย Email และ Citizend ID สำหรับกรณียืนยันตัวตนก่อนมี Authorization (เช่นการขอ OTP เพื่อกรณีลืม password)​")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized2Authen
	@GetMapping(path="/verif/user")
	@ResponseStatus(HttpStatus.OK)
	public VerifyUserRs verifyUser (
		@ApiParam(name="cid", value="หมายเลขบัตรประชาชนของผู้ใช้เพื่อยืนตัวตนมีอยู่ในระบ", required=true, type="query") 
		@RequestParam("cid") String citizenID,
		@ApiParam(name="email", value="email ของผู้ใช้เพื่อยืนยันตัวตนมีอยู่ในระบบ", required=true, type="body") 
		@RequestParam("email") String email
//		@RequestBody VerifyUserRq verifUser
	) throws ClientException {
		LoginUser user = new LoginUser();
		user.setCitizenID(citizenID);
		user.setLogin(email);
		user = serviceSQL.getUserByCifAndLogin(user);
		if (user == null) {
			throw new ClientException("401-001", "Invalid email or citizenID");
		} else {
			VerifyUserRs ret = new VerifyUserRs();
			ret.setEmail(user.getLogin());
			ret.setMobileNo(user.getMobileNo());
			ret.setVerifyUserRefID("<Verify User RefID>");
			return ret;
		}
	}
	
	private final String requestOTPVerify = "requestOTPVerify";
	@Testable(alwaysMock=false)
	@ApiOperation(value=requestOTPVerify+Team.DSL_SECURITY_TEAM, 
			notes="API สำหรับขอยืนยันตัวตน ด้วย OTP / สำหรับกรณียืนยันตัวตนก่อนมี Authorization (เช่น การลงทะเบียนผู้ใช้)​ ไม่จำเป็นต้องแนบ Token หากมี Authorization แล้วจำเป็นต้องแนบ Token เสมอ")
	@ApiDocHeaderOptionAuthorized
	@PostMapping(path="/verif/otp/req")
	@ResponseStatus(HttpStatus.OK)
	public RequestOTPVerifyRs requestOTPVerify(
		@ApiParam(name="requestOTP", value="OTP request information", required=true, type="body") 
		@RequestBody RequestOTPVerifyRq requestOTP
	) {
		RequestOTPVerifyRs ret = mockService.generateOTP(
				requestOTP.getObjective(), 
				requestOTP.getChannel(), 
				requestOTP.getChannelInfo());
		return ret;
	}
	
	private final String verifyOTP = "verifyOTP";
	@Testable(alwaysMock=false)
	@ApiOperation(value=verifyOTP+Team.DSL_SECURITY_TEAM, 
			notes="API สำหรับตรวจสอบ OTP")
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseAuthorized
	@PostMapping(path="/verif/otp")
	public VerifyOTPRs verifyOTP (
		@ApiParam(name="challengeOTP", value="Submited OTP", required=true, type="body") 
		@RequestBody VerifyOTPRq challengeOTP
	) throws ClientException {
		MockOTP mockOTP = serviceSQL.checkOTP(challengeOTP.getRefID());
		if (mockOTP == null) {
			throw new ClientException("400-000","Not found OTP ref");
		} else {
			if (mockOTP.getExpireDTM().before(DateUtil.currTime())) {
				throw new ClientException("400-001","OTP expired");
			}
			if (!mockOTP.getOtp().equals(challengeOTP.getOtp())) {
				throw new ClientException("400-002","Invalid otp");
			}
		}
		
		log.info("Generate one-time token.");
		UserToken userToken = UserToken.createOneTimeToken();
		serviceSQL.addNewToken(userToken);
		String token = jwtUtil.generateToken(userToken);
		log.info("One-time token with value: {}",userToken);
		VerifyOTPRs ret = new VerifyOTPRs(token);
		log.info("Remove token refID: {}",challengeOTP.getRefID());
		serviceSQL.removeOTP(challengeOTP.getRefID());
		return ret;
	}
/*	
	private final String verifyUser = "verifyUser";
	@Testable
	@ApiOperation(value=verifyUser, //hidden=true,
			notes="API สำหรับขอยืนยันมีตัวตนผู้ใช้ในระบบด้วย Email และ Citizend ID สำหรับกรณียืนยันตัวตนก่อนมี Authorization (เช่นการลืม password)​")
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
*/
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
@NoArgsConstructor
@AllArgsConstructor
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
	
	@ApiModelProperty(position = 1, required=true)
	String citizenID;

	@ApiModelProperty(position = 2, required=true, example="pongchet@orcsoft.co.th")
	String email;
}

@Data
@JsonInclude(value=Include.NON_EMPTY)
class VerifyUserRs {
	
	@ApiModelProperty(position = 1, required=true)
	String verifyUserRefID;
	
	@ApiModelProperty(position = 2, required=true, example="pongchet@orcsoft.co.th")
	String email;
	
	@ApiModelProperty(position = 3, required=false)
	String mobileNo;

}
