package th.co.ktb.dsl.model.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class PaymentRequest {
	@ApiModelProperty(position = 1, required=true)
	Double payAmount; // ยอดรวมชำระทั้งหมด 
}
