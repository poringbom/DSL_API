package th.co.ktb.dsl.model.payment;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_EMPTY)
public class Payment extends PaymentRequest {
	@ApiModelProperty(position = 1)
	byte[] payinSlip;

	@ApiModelProperty(position = 2)
	byte[] payQR;

	@ApiModelProperty(position = 3)
	String[] paymentInstruction;
	
	public static Payment getExampleATMResponse() {
		Payment p = new Payment();
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
	public static Payment getExampleTellerResponse() {
		Payment p = new Payment();
		Resource r = new ClassPathResource("PayinSlip.pdf");
		try (InputStream is = r.getInputStream()){
			byte[] data = new byte[(int)r.contentLength()];
			is.read(data);
			p.payinSlip = data;
		} catch(IOException ex) {}
		p.payAmount = 120000d;
		p.paymentInstruction = new String[] {
				"พิมพ์หรือดาวน์โหลดใบแจ้งยอดชำระ",
				"นำใบแจ้งยอดชำระไปที่เคาว์เตอร์ธนาคารกรุงไทย หรือเคาว์เตอร์เซอร์วิสเพื่อชำระเงิน"
		};
		return p;
	}
	public static Payment getExampleQRResponse() {
		Payment p = new Payment();
		Resource r = new ClassPathResource("QR.png");
		try (InputStream is = r.getInputStream()){
			byte[] data = new byte[(int)r.contentLength()];
			is.read(data);
			p.payQR = data;
		} catch(IOException ex) {}
		p.payAmount = 120000d;
		return p;
	}
}