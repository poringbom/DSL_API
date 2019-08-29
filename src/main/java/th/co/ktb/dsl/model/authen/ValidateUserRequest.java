package th.co.ktb.dsl.model.authen;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(content=Include.NON_NULL)
public class ValidateUserRequest {

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
	
	@ApiModelProperty(position = 6, example="2019-08-07", required=true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date dob;

	@ApiModelProperty(position = 7, required=true)
	String email;

	@ApiModelProperty(position = 8, required=false)
	String mobileNo;
	
}