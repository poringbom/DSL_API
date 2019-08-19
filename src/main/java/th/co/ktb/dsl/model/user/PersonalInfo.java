package th.co.ktb.dsl.model.user;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonalInfo {

	@ApiModelProperty(position = 0, required=true)
	PersonTitle title;

	@ApiModelProperty(position = 1, required=true)
	String firstName;
	
	@ApiModelProperty(position = 2, required=true)
	String lastName;

	@ApiModelProperty(position = 3, required=true)
	Date dob;

	@ApiModelProperty(position = 4, required=true)
	String citizenID;
	
	@ApiModelProperty(position = 5, required=true)
	String email;

	@ApiModelProperty(position = 6, required=true)
	String mobileNo;

	@ApiModelProperty(position = 7, required=true)
	String tel;
}