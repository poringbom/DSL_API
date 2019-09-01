package th.co.ktb.dsl.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(value=Include.NON_EMPTY)
public class UserAddresses {

	@ApiModelProperty(position = 1, required=true)
	AddressInfo houseRegisterAddr;

	@ApiModelProperty(position = 2, required=false)
	AddressInfo residenceAddr;

	@ApiModelProperty(position = 3, required=false)
	AddressInfo deliveryAddr;

	@ApiModelProperty(position = 4, required=true)
	Boolean residenceAsHouseRegister = false;

	@ApiModelProperty(position = 5, required=true)
	Boolean deliveryAsHouseRegister = false;

	@ApiModelProperty(position = 6, required=true)
	Boolean deliveryAsResidence = false;
}

