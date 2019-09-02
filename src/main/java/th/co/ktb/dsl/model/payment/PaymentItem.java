package th.co.ktb.dsl.model.payment;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import th.co.ktb.dsl.model.common.PaymentStatus;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentItem {
	@ApiModelProperty(position = 1)
	String refID;
	
	@ApiModelProperty(position = 2)
	String payChannel;
	
	@ApiModelProperty(position = 3)
	Double payAmount;
	
	@ApiModelProperty(position = 4, example="2019-08-07 22:55:00")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date paidDate;
	
	@ApiModelProperty(position = 5)
	PaymentStatus payStatus;
	
	@ApiModelProperty(position = 6)
	String receipt; // link, id, ... 

	@ApiModelProperty(position = 7)
	PayType type = PayType.PAYMENT;
}