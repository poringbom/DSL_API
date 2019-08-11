package th.co.ktb.dsl.model.payment;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.model.common.PaymentStatus;

@Data
@EqualsAndHashCode(callSuper=false)
public class PaymentHistory {
	@JsonProperty("total") int totalRecord = 0; 
	List<PaymentItem> history;
	
	public static PaymentHistory getExamplePaymentHistory(int num) {
		num = num>15?15:num;
		List<PaymentItem> list = new ArrayList<PaymentItem>();
		for(int i=0; i<num; i++) {
			list.add(new PaymentItem("927USH2837492","ATM",11957.86d,Utilities.parseDate("20"+(19-i)+"-06-12 12:32:00"),PaymentStatus.PAID,null));
		}
//		list.add(new PaymentItem("927USH2837492","Int Transfer",11957.86d,Utilities.parseDate("2018-06-12 12:32:00"),"PAID",null));
//		list.add(new PaymentItem("927USH2837492","Counter Service",11957.86d,Utilities.parseDate("2017-06-12 12:32:00"),"PAID",null));
		PaymentHistory ph = new PaymentHistory();
		ph.history = list;
		return ph;
	}
}