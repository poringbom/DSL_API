package th.co.ktb.dsl.model.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(value=Include.NON_EMPTY)
public class UserInfo {

	@ApiModelProperty(position = -10, required=true)
	@JsonIgnore
	Boolean resetPIN = false;
	
	@ApiModelProperty(position = -5, required=false, 
		notes="Verify Token Action ใช้สำหรับเพื่อดำเนินการตั้งรหัส PIN หลังจากกรณี Sign-in เรียบร้อยและพบว่ายังไม่มีการกำหนด PIN")
	String verifyActionToken;

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
	UserAddresses addresses;
	
	@ApiModelProperty(position = 10, required=false)
	WorkInfo workInfo; 

	@ApiModelProperty(position = 11, required=false)
	SpouseInfo spouse;
}



