package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PinSetup {
	@ApiModelProperty(position = 1, required=true)
	String newPIN;
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class PinChange extends PinSetup {
		@ApiModelProperty(position = 2, required=true)
		String previousPIN;
	}
}