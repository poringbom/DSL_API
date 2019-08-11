package th.co.ktb.dsl.model.payment;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import th.co.ktb.dsl.Utilities;

@Data
@JsonInclude(Include.NON_EMPTY)
@ApiModel
public class PaymentInfo {
	@ApiModelProperty(position = 1, required=true)
	Double debtBalance;
	
	@ApiModelProperty(position = 2, example="2019-08-07 22:55:00") 
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date lastPayment;
	
	@ApiModelProperty(position = 3, example="2019-08-07 22:55:00")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date lastDuePayment;
	
	@ApiModelProperty(position = 4, required=false)
	DuePayment duePayment;
	
	@ApiModelProperty(position = 5, required=false)
	OverduePayment overduePayment;
	

	public static PaymentInfo getExamplePayment() {
		PaymentInfo p = new PaymentInfo();
		p.debtBalance = 163983.92d;
		p.lastDuePayment = Utilities.parseDate("2032-07-05 22:55");
		return p;
	}
	
	public static PaymentInfo getExamplePaymentWithDue() {
		PaymentInfo p = new PaymentInfo();
		p.debtBalance = 163983.92d;
		p.lastDuePayment = Utilities.parseDate("2032-07-05 22:55");
		
		DuePayment d = new DuePayment();
		d.caption = "ยอดค้างชำระ 2562";
		d.accruedPrincipal = 4277.30d;
		d.accumulateInterest = 309.91d;
		d.dueDate = Utilities.parseDate("2019-07-05 22:55");
		p.duePayment = d;
		d.totalAmount = 4528.28d;
		return p;
	}
	
	public static PaymentInfo getExamplePaymentWithOverDue() {
		PaymentInfo p = new PaymentInfo();
		p.debtBalance = 163983.92d;
		p.lastDuePayment = Utilities.parseDate("2032-07-05 22:55");
		
		DuePayment d = new DuePayment();
		d.caption = "ยอดค้างชำระ 25xx-2561 และยอดค้างชำระ 2562";
		d.accruedPrincipal = 6290.14d;
		d.accumulateInterest = 309.91d;
		d.principal = 4277.30d;
		d.penalty = 1024.41d;
		d.dueDate = Utilities.parseDate("2019-07-05 22:55");
		d.totalAmount = 11967.86d;
		p.duePayment = d;
		
		OverduePayment o = new OverduePayment();
		o.caption = "ยอดค้างชำระ 25xx-2561";
		o.principal = 6837.12d;
		o.accumulateInterest = 253.31d;
		o.penalty = 1024.41d;
		o.totalAmount = 7577.86d;
		p.overduePayment = o;
		return p;
	}
}