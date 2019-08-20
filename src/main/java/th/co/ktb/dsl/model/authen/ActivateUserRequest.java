package th.co.ktb.dsl.model.authen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivateUserRequest {

	@ApiModelProperty(position = 1, required=true)
	String registerRefID;
}