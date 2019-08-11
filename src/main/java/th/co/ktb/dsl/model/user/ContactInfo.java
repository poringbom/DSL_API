package th.co.ktb.dsl.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContactInfo {

	@ApiModelProperty(position = 1)
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
	
	@ApiModelProperty(position = 7)
	String subDistrict;
	
	@ApiModelProperty(position = 8)
	String district;
	
	@ApiModelProperty(position = 9)
	String province;
	
	@ApiModelProperty(position = 10)
	String postCode;
	
	@ApiModelProperty(position = 21)
	String mobileNo;
	
	@ApiModelProperty(position = 22, required = true)
	String email;
	
	@ApiModelProperty(position = 23)
	String telNo;
}