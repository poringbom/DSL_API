package th.co.ktb.dsl.model.payment;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DuePayment extends OverduePayment{
	@ApiModelProperty(position = 2, required=true)
	Double accruedPrincipal; // เงินต้นค้างสะสม
	
	@ApiModelProperty(position = 5, example="2019-08-07 22:55:00", required=true)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date dueDate;
}