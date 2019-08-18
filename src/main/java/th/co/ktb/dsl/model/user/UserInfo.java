package th.co.ktb.dsl.model.user;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfo {

	@ApiModelProperty(position = 0, required=true)
	PersonTitle title;

	@ApiModelProperty(position = 1, required=true)
	String firstName;
	
	@ApiModelProperty(position = 2, required=true)
	String lastName;

	@ApiModelProperty(position = 3, example="2019-08-07 22:55:00", required=true)
	Date dob;

	@ApiModelProperty(position = 4, required=true)
	String citizenID;

	@ApiModelProperty(position = 5, required=false)
	ContactInfo contact;

	@ApiModelProperty(position = 6, required=false)
	UserAddresses addresses;
	
	@ApiModelProperty(position = 10, required=false)
	WorkingAddressInfo workingAddr; 

	@ApiModelProperty(position = 11, required=false)
	SpouseInfo spouse;
}



