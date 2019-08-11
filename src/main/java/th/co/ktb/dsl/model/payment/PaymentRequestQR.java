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

import th.co.ktb.dsl.model.payment.EmbedResource;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_EMPTY)
public class PaymentRequestQR extends PaymentRequest {
	@ApiModelProperty(position = 2, required=true)
	EmbedResource payQR;

	@ApiModelProperty(position = 3, required=true)
	String[] paymentInstruction;

	public static PaymentRequestQR getExampleQRResponse() {
		PaymentRequestQR p = new PaymentRequestQR();
		Resource r = new ClassPathResource("QR.png");
		try (InputStream is = r.getInputStream()){
			byte[] data = new byte[(int)r.contentLength()];
			is.read(data);
			p.payQR = new EmbedResource(data, "QR.png", "PNG");
		} catch(IOException ex) {}
		p.payAmount = 120000d;
		return p;
	}
}