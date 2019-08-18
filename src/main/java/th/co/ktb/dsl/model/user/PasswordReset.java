package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PasswordReset {
	@ApiModelProperty(position = 1, required=true)
	String newPassword;
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class PasswordChange extends PasswordReset {
		@ApiModelProperty(position = 2, required=true)
		String previousPassword;
	}
}
