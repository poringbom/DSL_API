package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfo {

	@ApiModelProperty(position = 1, required=true)
	String userID;

	@ApiModelProperty(position = 2, required=true)
	String role;

	@ApiModelProperty(position = 3, required=true)
	PersonalInfo personalInfo;

	@ApiModelProperty(position = 4, required=true)
	ContactInfo contactInfo;
}



