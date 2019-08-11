package th.co.ktb.dsl.model.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_EMPTY)
public class PaymentRequestATM extends PaymentRequest {
	@ApiModelProperty(position = 3, required=true)
	String[] paymentInstruction;
	
	public static PaymentRequestATM getExampleATMResponse() {
		PaymentRequestATM p = new PaymentRequestATM();
		p.payAmount = 120000d;
		p.paymentInstruction = new String[] {
				"ใส่บัตร ATM และกดรหัสบัตร",
				"เลือก \"บริการอื่นๆ\"",
				"เลือก \"บริการการศึกษา\"",
				"เลือก \"ชำระหนี้ กยศ./กรอ.\"",
				"ใส่ \"ใส่เลขบัตรประชาชน\" ของ \"ผู้กู้\" และกดยืนยัน",
				"ระบุจำนวนเงินที่ต้องการชำระจากนั้นกดยืนยัน",
				"รับใบสลิปและเก็บไว้เพื่อใช้เป็นหลักฐาน"
		};
		return p;
	}
}