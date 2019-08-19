package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WorkingAddressInfo extends AddressInfo {

	@ApiModelProperty(position = 20, required = true)
	String workTel;
	
	@ApiModelProperty(position = 21, required = true)
	String workTelExt;
}
