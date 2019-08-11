package th.co.ktb.dsl.model.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OverduePayment {
	@ApiModelProperty(position = 1, required=true)
	String caption; // caption สำหรับแสดงการชำระเงิน
	
	@ApiModelProperty(position = 2, required=true)
	Double accumulateInterest; // ดอกเบี้ยสะสม
	
	@ApiModelProperty(position = 3, required=true)
	Double principal; // เงินต้น
	
	@ApiModelProperty(position = 4, required=true)
	Double penalty; // ค่าปรับ
	
	@ApiModelProperty(position = 9, required=true)
	Double totalAmount; // รวม
}

