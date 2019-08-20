package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivateUserResponse {

	@ApiModelProperty(position = 1, required=true, value="Token สำหรับตรวจสอบเพื่อดำเนินการตั้งรหัสผ่านในขั้นตอนถัดไป")
	String verifyActionToken;
	
}