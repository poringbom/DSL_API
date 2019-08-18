package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WorkingAddressInfo extends AddressInfo {

	@ApiModelProperty(position = 0, required = true)
	WorkInfo workInfo;
	
	@ApiModelProperty(position = 20, required = true)
	String workTel;
}
