package th.co.ktb.dsl.model.authen;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRegisterInfo {

	@ApiModelProperty(position = 1, required=true)
	String citizenID;

	@ApiModelProperty(position = 2, required=false)
	String citizenLaserCode;

	@ApiModelProperty(position = 3, required=true)
	String title;

	@ApiModelProperty(position = 4, required=true)
	String firstName;

	@ApiModelProperty(position = 5, required=true)
	String lastName;
	
	@ApiModelProperty(position = 6, example="2019-08-07 22:55:00", required=true)
	Date dob;

	@ApiModelProperty(position = 7, required=true)
	String email;

	@ApiModelProperty(position = 8, required=false)
	String mobileNo;
	
//	@ApiModelProperty(position = 9, required=true)
//	String password;

	@ApiModelProperty(position = 20, required=true)
	VerifyActionChannel verifyChannel;
}