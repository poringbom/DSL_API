package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRegisterResponse {

	@ApiModelProperty(position = 1, required=true)
	String registerRefID;

	@ApiModelProperty(position = 2, required=true)
	VerifyActionChannel verifyChannel;
	
	@ApiModelProperty(position = 3, required=false, value="มีค่าเมื่อทำการลงทะเบียนและยืนยันผ่านช่องทาง Email")
	String email;
	
	@ApiModelProperty(position = 4, required=false, value="มีค่าเมื่อทำการลงทะเบียนและยืนยันผ่านช่องทาง Mobile")
	String verifyActionToken;
}