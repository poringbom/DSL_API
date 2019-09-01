package th.co.ktb.dsl.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.apidoc.ApiDocHeader;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocHeaderNoAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocHeaderOptionAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseNewAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseNoAuthorized;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.config.Constants;
import th.co.ktb.dsl.config.security.ApiAuthenticationToken;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.mock.MockService;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.mock.ServiceSQL.LoginUser;
import th.co.ktb.dsl.model.authen.ValidateUserRequest;
import th.co.ktb.dsl.model.authen.VerifyActionChannel;
import th.co.ktb.dsl.model.user.OpenIDFormData;
import th.co.ktb.dsl.model.user.OpenIDFormData.TempUser;
import th.co.ktb.dsl.model.user.PasswordReset;
import th.co.ktb.dsl.model.user.PersonTitle;
import th.co.ktb.dsl.model.user.PasswordReset.PasswordChange;
import th.co.ktb.dsl.model.user.PinSetup;
import th.co.ktb.dsl.model.user.PinSetup.PinChange;

@Api(tags = "1.2. DSL-RMS : Registration API", description = "API เกี่ยวกับการลงทะเบียนใช้งาน")
@RestController
@RequestMapping("/api/v1/rms")
@Transactional
@Slf4j
public class RegistrationController {

	@Autowired
	ServiceSQL sql;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	MockService mockService;

	private final String resetPIN = "resetPIN";

	@Testable(alwaysMock = false)
	@ApiOperation(value = resetPIN
			+ Team.RMS_TEAM, hidden = true, notes = "API สำหรับกำหนด PIN ใหม่ (กรณีไม่เคยมีมาก่อน หรือ ผู้ใช้ลืม PIN)")
	@ApiDocHeaderNoAuthorized2Authen
	@ApiDocResponseNewAuthorized
	@PostMapping("/pin")
	@ResponseStatus(HttpStatus.CREATED)
	public void resetPIN(
			@ApiParam(name = "newPIN", type = "body", value = "New setup PIN request", required = true) @RequestBody PinSetup pinSetup,
			@ApiIgnore Authentication auth) {
		log.info("do {}()", resetPIN);
		log.info("Auth Class: {}", auth.getClass().getSimpleName());
		if (auth instanceof ApiAuthenticationToken) {
			UserToken user = ((ApiAuthenticationToken) auth).getUserInfo();
			log.info("Set new pin to userID: {}, login: ", user.getUserID(), user.getLogin());
			sql.resetPIN(user.getUserID(), pinSetup.getNewPIN(), null);
		}
	}

	private final String changePIN = "changePIN";

	@Testable(alwaysMock = false)
	@ApiOperation(value = changePIN
			+ Team.RMS_TEAM, hidden = true, notes = "API สำหรับกำหนด PIN ใหม่ (ผุ้ใช้จำเป็นต้องทราบ PIN ปัจจุบัน)")
	@ApiDocHeaderAuthorized
	@PutMapping("/pin")
	@ResponseStatus(HttpStatus.OK)
	public void changePin(
			@ApiParam(name = "changePIN", type = "body", value = "Change PIN request", required = true) @RequestBody PinChange pinChange,
			@ApiIgnore Authentication auth) throws ClientException {
		log.info("do {}()", changePIN);
		log.info("Auth Class: {}", auth.getClass().getSimpleName());
		if (auth instanceof ApiAuthenticationToken) {
			UserToken user = ((ApiAuthenticationToken) auth).getUserInfo();
			log.info("Update pin to userID: {}, login: ", user.getUserID(), user.getLogin());
			Integer rowEffect = sql.resetPIN(user.getUserID(), pinChange.getNewPIN(), pinChange.getPreviousPIN());
			if (rowEffect < 1) {
				log.info("row effect: {}", rowEffect);
				throw new ClientException("Invalid old pin");
			}
		}
	}

	private final String resetPassword = "resetPassword";

	@Testable(alwaysMock = false)
	@ApiOperation(value = resetPassword
			+ Team.RMS_TEAM, notes = "API สำหรับกำหนดรหัสผ่านเข้าใช้งานระบบใหม่ (กรณีผู้ใช้ลืม PIN)")
	@ApiDocHeaderNoAuthorized2Authen
	@ApiDocResponseNewAuthorized
	@PostMapping("/password")
	@ResponseStatus(HttpStatus.CREATED)
	public void resetPassword(
			@ApiParam(name = "passwordReset", type = "body", value = "New password", required = true) @RequestBody PasswordReset passwordReset,
			@ApiIgnore Authentication auth) {
		log.info("do {}()", resetPassword);
		log.info("Auth Class: {}", auth.getClass().getSimpleName());
		if (auth instanceof ApiAuthenticationToken) {
			UserToken user = ((ApiAuthenticationToken) auth).getUserInfo();
			log.info("Set new password to userID: {}, login: ", user.getUserID(), user.getLogin());
			sql.resetPassword(user.getUserID(), passwordReset.getNewPassword(), null);
		}
	}

	private final String changePassword = "changePassword";

	@Testable(alwaysMock = false)
	@ApiOperation(value = changePassword
			+ Team.RMS_TEAM, notes = "API สำหรับเปลี่ยนรหัสผ่านเข้าใช้งานระบบ (ผู้ใช้จำเป็นต้องทราบรหัสผ่านปัจจุบัน)")
	@ApiDocHeaderAuthorized
	@PutMapping("/password")
	@ResponseStatus(HttpStatus.OK)
	public void changePassword(
			@ApiParam(name = "passwordChange", type = "body", value = "Change passowrd", required = true) @RequestBody PasswordChange passwordChange,
			@ApiIgnore Authentication auth) throws ClientException {
		log.info("do {}()", changePassword);
		log.info("Auth Class: {}", auth.getClass().getSimpleName());
		if (auth instanceof ApiAuthenticationToken) {
			UserToken user = ((ApiAuthenticationToken) auth).getUserInfo();
			log.info("Update password to userID: {}, login: ", user.getUserID(), user.getLogin());
			log.info("old password: {}, new password: {}", passwordChange.getPreviousPassword(),
					passwordChange.getNewPassword());
			Integer rowEffect = sql.resetPassword(user.getUserID(), passwordChange.getNewPassword(),
					passwordChange.getPreviousPassword());
			if (rowEffect < 1) {
				log.info("row effect: {}", rowEffect);
				throw new ClientException("Invalid old password");
			}
		}
	}

	private final String registerUser = "registerUser";

	@Testable(alwaysMock = false)
	@ApiOperation(value = registerUser
			+ Team.RMS_TEAM, notes = "API สำหรับลงทะเบียนการใช้งานผู้ใช้ / โดยอ้างอิงจาก Register Ref ID ที่ผ่านการ validation เรียบร้อย และได้รับ Verification Token เพื่อดำเนินการแล้ว")
	@ApiDocHeaderNoAuthorized2Authen
	@ApiDocResponseNewAuthorized
	@PutMapping("/user")
	@ResponseStatus(HttpStatus.OK)
	public ReqisterUserRs registerUser(
			@ApiParam(name = "userInfo", type = "body", required = true, value = "ข้อมูลผู้ใช้สำหรับการลงทะเบียนใช้งาน") @RequestBody ReqisterUserRq userInfo)
			throws ClientException, JsonParseException, JsonMappingException, IOException {
		log.info("do {}()", registerUser);
		String refID = userInfo.getRegisterRefID();
		String tempUser = sql.getTempUser(refID, TempUser.TEMP_VALIDATED);
		if (tempUser != null) {
			ValidateUserRequest valdatedUser = Utilities.getObjectMapper().readValue(tempUser,
					ValidateUserRequest.class);
			LoginUser loginUser = new LoginUser(valdatedUser.getEmail(), "");
			loginUser.setCitizenID(valdatedUser.getCitizenID());
			loginUser.setTitle(valdatedUser.getTitle());
			loginUser.setFirstName(valdatedUser.getFirstName());
			loginUser.setLastName(valdatedUser.getLastName());
			loginUser.setMobileNo(valdatedUser.getMobileNo());
			loginUser.setDob(valdatedUser.getDob());
			log.info("add new user to db: {}", loginUser);
			sql.addNewUser(loginUser);
			sql.addNewUserInfo(loginUser.getUserID());
			sql.removeTempUser(refID);

			String verifyActionToken = jwtUtil.generateOneTimeToken(loginUser.getLogin(), loginUser.getUserID(),
					"ResetPassword");

			ReqisterUserRs ret = new ReqisterUserRs();
			ret.setRegisterRefID(refID);
			ret.setVerifyActionToken(verifyActionToken);
			ret.setVerifyChannel(userInfo.getVerifyChannel());
			return ret;
		} else {
			throw new ClientException("401-001", "Invalid registerRefID");
		}
	}

	private final String resendVerifyEmail = "resendVerifyEmail";

	@Testable(alwaysMock = false)
	@ApiOperation(value = resendVerifyEmail
			+ Team.RMS_TEAM, notes = "API สำหรับส่ง Email เพื่อยืนยันตัวตนลงทะเบียนผู้ใช้ในระบบ")
	@ApiDocHeader
	@ApiDocResponseNoAuthorized
	@PostMapping("/email")
	@ResponseStatus(HttpStatus.OK)
	public ResendVerifyEmailRs resendVerifyEmail(
			@ApiParam(name = "registerRef", type = "body", required = true, value = "Register reference ID") @RequestBody ResendVerifyEmailRq registerRef)
			throws ClientException, JsonParseException, JsonMappingException, IOException {
		log.info("do {}()", resendVerifyEmail);
		ResendVerifyEmailRs ret = new ResendVerifyEmailRs();
		String regRefID = registerRef.getRegisterRefID();
		String tempUser = sql.getTempUser(regRefID, TempUser.TEMP_VALIDATED);
		if (tempUser != null) {
			ValidateUserRequest user = Utilities.getObjectMapper().readValue(tempUser, ValidateUserRequest.class);
			String email = user.getEmail();
			ret.setRegisterRefID(registerRef.getRegisterRefID());
			ret.setVerifyChannel(VerifyActionChannel.EMAIL);
			ret.setEmail(email);

			String verifyActionToken = jwtUtil.generateOneTimeToken(email, 0, Constants.TOKEN_REGISTER_USER);
			String linkURL = sql.getConfig("activation.url");
			linkURL = linkURL == null ? "http://pornhub.com/" : linkURL;

			MessageFormat mf = new MessageFormat("Link to register user (activation): {0}?refID={1}&token={2}");
			String text = mf.format(new Object[] { linkURL, registerRef.getRegisterRefID(), verifyActionToken });
			log.info("activation url: {}", linkURL);
			log.info("send email to {}:", email);
			log.info("email body: {}", text);
			mockService.sendEmail(email, "[Mock-DSL-API] Activate User", text);
			return ret;
		} else {
			throw new ClientException("401-001", "Invalid registerRefID");
		}
	}

	/*
	 * private final String activateUser = "activateUser";
	 * 
	 * @Testable
	 * 
	 * @ApiOperation(value=activateUser+Team.RMS_TEAM,
	 * notes="API สำหรับเปิดการใช้งานผู้ใช้สำหรับกรณี ผู้ใช้ทำการเลือกช่องทางยืนยันตัวตนผ่านทาง Email."
	 * )
	 * 
	 * @ApiDocHeaderNoAuthorized2Authen
	 * 
	 * @ApiDocResponseNoAuthorized
	 * 
	 * @PostMapping("/activation")
	 * 
	 * @ResponseStatus(HttpStatus.OK) public ActivateUserResponse activateUser(
	 * 
	 * @ApiParam(name="registerRef",type="body",required=true,
	 * value="Register reference ID")
	 * 
	 * @RequestBody ActivateUserRequest registerRef ) { return null; }
	 */
	private final String validateUser = "validateUser";

	@Testable(alwaysMock = false)
	@ApiOperation(value = validateUser
			+ Team.RMS_TEAM, notes = "API สำหรับตรวจสอบข้อมูลผู้ลงทะเบียนถูกต้องตรงตามในข้อมูลกรมการปกครอง และไม่ซ้ำซ้อนกับข้อมูลในระบบ "
					+ "/ ใช้เรียกในขั้นตอนผู้ใช้กดปุ่ม 'ถัดไป' ในขั้นตอนสมัครทั้งการลงทะเบียนแบบใช้ บัตรประชาชน และ OpenID "
					+ "/ ผลลัพธ์ระบบจะคืน Reference ID ที่เป็นตัวแทนข้อมูลของผู้ใช้ที่จะสมัครในขั้นตอนถัดๆไป ")
	// @ApiDocHeaderNoAuthorized2Authen
	@ApiDocHeaderOptionAuthorized
	@ApiDocResponseNoAuthorized
	@PostMapping("/user")
	@ResponseStatus(HttpStatus.OK)
	public ValidateUserRs validateUser(
			@ApiParam(name = "userInfo", type = "body", required = true, value = "ข้อมูลผู้ใช้สำหรับการลงทะเบียนใช้งาน") @RequestBody ValidateUserRequest userInfo)
			throws JsonProcessingException {
		log.info("do {}()", validateUser);
		TempUser tmpUser = new TempUser();
		tmpUser.setData(Utilities.getObjectMapper().writeValueAsString(userInfo));
		tmpUser.setType(TempUser.TEMP_VALIDATED);
		sql.addTempUser(tmpUser);
		ValidateUserRs ret = new ValidateUserRs();
		ret.setRegisterRefID(Integer.toString(tmpUser.getRefID()));
		return ret;
	}

	private final String registerUserByOpenID = "registerUserByOpenID";

	@ApiDocHeader
	@ApiOperation(value = registerUserByOpenID
			+ Team.RMS_TEAM, notes = "API (Link) สำหรับเริ่มต้นกระบวนการลงทะเบียนการใช้งานผ่านช่องทาง OpenID")
	@GetMapping("/openID")
	public void registerUserByOpenID(
		@ApiIgnore HttpServletRequest request, 
		@ApiIgnore HttpServletResponse response,
		@ApiParam("platform") String platform)
			throws IOException, ServletException {
		request.getRequestDispatcher("/openID").forward(request, response);
		// response.sendRedirect("/openID");
	}

	private final String getOpenIDUserInfo = "getOpenIDUserInfo";

	@Testable(alwaysMock = false)
	@ApiDocHeader
	@ApiOperation(value = getOpenIDUserInfo
			+ Team.RMS_TEAM, notes = "API - สำหรับเรียกดูข้อมูลผู้ใช้เริ่มต้น ที่ได้รับจาก OpenID เพื่อตั้งต้นกระบวนการลงทะเบียนผู้ใช้ต่อไป")
	@GetMapping("/openID/user")
	@ResponseStatus(HttpStatus.OK)
	public GetOpenIDUserInfoRs getOpenIDUserInfo(
			@ApiParam(name = "registerRef", type = "query", required = true, value = "Register reference ID") @RequestParam("registerRef") String registerRef)
			throws JsonParseException, JsonMappingException, IOException {
		log.info("do {}()", getOpenIDUserInfo);
		String tempUserInfo = sql.getTempUser(registerRef, TempUser.TEMP_OPEN_ID);
		OpenIDFormData fromData = Utilities.getObjectMapper().readValue(tempUserInfo, OpenIDFormData.class);
		GetOpenIDUserInfoRs ret = new GetOpenIDUserInfoRs();
		ret.setCitizenID(fromData.getCitizenID());
		ret.setTitle(fromData.getTitle());
		ret.setFirstName(fromData.getFirstName());
		ret.setLastName(fromData.getLastName());
		ret.setDob(fromData.getDob());
		return ret;
	}
}

@Data
class GetOpenIDUserInfoRq {
	String registerRefID;
}

@Data
class GetOpenIDUserInfoRs {
	@ApiModelProperty(position = 1, required = true)
	String citizenID;

	@ApiModelProperty(position = 2, required = true)
	PersonTitle title;

	@ApiModelProperty(position = 3, required = true)
	String firstName;

	@ApiModelProperty(position = 4, required = true)
	String lastName;

	@ApiModelProperty(position = 5, example = "2019-08-07", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date dob;
}

@Data
class ValidateUserRs {
	String registerRefID;
}

@Data
class ResendVerifyEmailRq {

	@ApiModelProperty(position = 1, required = true)
	String registerRefID;
}

@Data
class ResendVerifyEmailRs {

	@ApiModelProperty(position = 1, required = true)
	String registerRefID;

	@ApiModelProperty(position = 2, required = true)
	VerifyActionChannel verifyChannel;

	@ApiModelProperty(position = 3, required = false, value = "มีค่าเมื่อทำการลงทะเบียนและยืนยันผ่านช่องทาง Email")
	String email;
}

@Data
class ReqisterUserRq {

	@ApiModelProperty(position = 1, required = true)
	String registerRefID;

	@ApiModelProperty(position = 2, required = true)
	VerifyActionChannel verifyChannel;
}

@Data
class ReqisterUserRs {

	@ApiModelProperty(position = 1, required = true)
	String registerRefID;

	@ApiModelProperty(position = 2, required = true)
	VerifyActionChannel verifyChannel;

	@ApiModelProperty(position = 4, required = false, value = "มีค่าเมื่อทำการลงทะเบียนและยืนยันผ่านช่องทาง Mobile")
	String verifyActionToken;
}