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
public class PaymentRequestTeller extends PaymentRequest {
	@ApiModelProperty(position = 2, required=true)
	EmbedResource payinSlip;

	@ApiModelProperty(position = 3, required=true)
	String[] paymentInstruction;
	
	public static PaymentRequestTeller getExampleTellerResponse() {
		PaymentRequestTeller p = new PaymentRequestTeller();
		Resource r = new ClassPathResource("PayinSlip.pdf");
		try (InputStream is = r.getInputStream()){
			byte[] data = new byte[(int)r.contentLength()];
			is.read(data);
			p.payinSlip = new EmbedResource(data, "PayinSlip.pdf", "PDF");
		} catch(IOException ex) {}
		p.payAmount = 120000d;
		p.paymentInstruction = new String[] {
				"พิมพ์หรือดาวน์โหลดใบแจ้งยอดชำระ",
				"นำใบแจ้งยอดชำระไปที่เคาว์เตอร์ธนาคารกรุงไทย หรือเคาว์เตอร์เซอร์วิสเพื่อชำระเงิน"
		};
		return p;
	}
}