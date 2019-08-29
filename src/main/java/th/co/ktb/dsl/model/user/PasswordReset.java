package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordReset {
	@ApiModelProperty(position = 1, required=true)
	String newPassword;
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	@NoArgsConstructor
	public static class PasswordChange extends PasswordReset {
		@ApiModelProperty(position = 2, required=true)
		String previousPassword;
	}
}
