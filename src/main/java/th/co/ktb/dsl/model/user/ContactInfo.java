package th.co.ktb.dsl.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(value=Include.NON_EMPTY)
public class ContactInfo {
	
	@ApiModelProperty(position = 1, required = true)
	String email;
	
	@ApiModelProperty(position = 2, required = false)
	String mobileNo;
	
	@ApiModelProperty(position = 3, required = false)
	String telNo;
}