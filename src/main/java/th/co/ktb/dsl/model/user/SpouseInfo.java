package th.co.ktb.dsl.model.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(value=Include.NON_EMPTY)
public class SpouseInfo {

	@ApiModelProperty(position = -1, required=true)
	String materialStatus;
	
	@ApiModelProperty(position = 0, required=true)
	PersonTitle title;

	@ApiModelProperty(position = 1, required=true)
	String firstName;
	
	@ApiModelProperty(position = 2, required=true)
	String lastName;

	@ApiModelProperty(position = 3, example="2019-08-07", required=true)
	@JsonFormat(pattern = "yyyy-MM-dd") 
	Date dob;

	@ApiModelProperty(position = 4, required=true)
	String citizenID;

	@ApiModelProperty(position = 5, required=false)
	ContactInfo contact;
	
	@ApiModelProperty(position = 6, required=false)
	WorkInfo workInfo;
}


