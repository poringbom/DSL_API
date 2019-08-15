package th.co.ktb.dsl.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.annotation.ApiMetadata;

@Api(tags="DSL-RMS : Registration API", description="API เกี่ยวกับการลงทะเบียนใช้งาน")
@RestController
@RequestMapping("/api/v1/rms")
public class RegistrationController {
	
	
	private final String registerUserByCitizenID = "registerUserByCitizenID";
	@Testable
	@ApiOperation(value=registerUserByCitizenID,
			notes="API สำหรับลงทะเบียนการใช้งานระบบด้วยบัตรประชาชน")
	@ApiImplicitParams({
		@ApiImplicitParam(name = ApiMetadata.HEADER_NAME, value = "Metadata information of service request. syntax: "
				+ "src=<channel>;dest=<service-destination>;service=<api-sevice-name>", required = true, 
				allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
				example = "src=android;dest=dsl-dms;service=payment"),
		@ApiImplicitParam(name = "X-Verify-Token", value = "Valid token for user registration, receive from OTP verification process.", 
				required = true, paramType = "header", dataTypeClass = String.class)
	})
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void registerUserByCitizenID(
		@ApiParam(name="userInfo",type="body",required=true) @RequestBody UserRegisterInfo userInfo
	) {
		return;
	}
	

	private final String registerUserByOpenID = "registerUserByOpenID";
	@Testable
	@ApiOperation(value=registerUserByOpenID,
			notes="API สำหรับลงทะเบียนการใช้งานผ่านช่องทาง OpenID")
	@PostMapping("/register/openID")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void registerUserByOpenID(
		
	) {
		return;
	}
}


@Data
class UserRegisterInfo {

	@ApiModelProperty(position = 1, required=true)
	String citizenID;

	@ApiModelProperty(position = 2, required=true)
	String citizenLaserCode;

	@ApiModelProperty(position = 3, required=true)
	String title;

	@ApiModelProperty(position = 4, required=true)
	String firstName;

	@ApiModelProperty(position = 5, required=true)
	String lastName;

	@ApiModelProperty(position = 6, required=true)
	String email;

	@ApiModelProperty(position = 7, required=true)
	String mobileNo;
	
	@ApiModelProperty(position = 8, required=true)
	Date dob;
	
	@ApiModelProperty(position = 9, required=true)
	String registerPassword;

	@ApiModelProperty(position = 20, required=true)
	RegisterChannel registerChannel;
}

enum RegisterChannel {
	WEB,
	MOBILE
}
