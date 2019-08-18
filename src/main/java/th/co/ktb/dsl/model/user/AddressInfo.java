package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressInfo {

	@ApiModelProperty(position = 1, required = true)
	String addressNo;

	@ApiModelProperty(position = 2)
	String building;
	
	@ApiModelProperty(position = 3)
	String moo;
	
	@ApiModelProperty(position = 4)
	String soi;
	
	@ApiModelProperty(position = 5)
	String alley;
	
	@ApiModelProperty(position = 6)
	String street;
	
	@ApiModelProperty(position = 7, required = true)
	String subDistrict;
	
	@ApiModelProperty(position = 8, required = true)
	String district;
	
	@ApiModelProperty(position = 9, required = true)
	String province;
	
	@ApiModelProperty(position = 10, required = true)
	String postCode;
}
