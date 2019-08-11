package th.co.ktb.dsl.model.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.ktb.dsl.model.common.PaymentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaySchedule {
	@ApiModelProperty(position = 1, required=true)
	Integer order;
	
	@ApiModelProperty(position = 2, required=true)
	Integer month;
	
	@ApiModelProperty(position = 3, required=true)
	Integer year;
	
	@ApiModelProperty(position = 4, required=true)
	Double principalRatio; //สัดส่วนเงินต้น
	
	@ApiModelProperty(position = 5, required=true)
	Double principal; //เงินต้น
	
	@ApiModelProperty(position = 6, required=true)
	Double interest; // ยอดดอกเบี้ย
	
	@ApiModelProperty(position = 7, required=true)
	Double total; //ดอกเบี้ย+เงินต้น
	
	@ApiModelProperty(position = 8, required=true)
	PaymentStatus payStatus;
}