package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContactInfo {
	
	@ApiModelProperty(position = 1, required = true)
	String email;
	
	@ApiModelProperty(position = 2, required = false)
	String mobileNo;
	
	@ApiModelProperty(position = 3, required = false)
	String telNo;
}