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
public class PaymentReceipt {
	@ApiModelProperty(position = 1, required=true)
	EmbedResource receipt;

	public static PaymentReceipt getExamplePaymentReceipt() {
		PaymentReceipt p = new PaymentReceipt();
		Resource r = new ClassPathResource("QR.png");
		try (InputStream is = r.getInputStream()){
			byte[] data = new byte[(int)r.contentLength()];
			is.read(data);
			p.receipt = new EmbedResource(data, "QR.png", "PNG");
		} catch(IOException ex) {}
		return p;
	}
}