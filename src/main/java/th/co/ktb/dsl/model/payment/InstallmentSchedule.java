package th.co.ktb.dsl.model.payment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import th.co.ktb.dsl.model.common.PaymentStatus;

@Data
public class InstallmentSchedule {
	@ApiModelProperty(position = 1, required=true)
	Double interest;
	
	@ApiModelProperty(position = 2, required=true)
	Double principal;
	
	@ApiModelProperty(position = 3, required=true)
	Double total;

	@ApiModelProperty(position = 4, example="2019-08-07 22:55:33", required=true)
	Date lastUpdate;
	
	@ApiModelProperty(position = 9, required=true)
	List<PaySchedule> schedule;
	
	public static InstallmentSchedule getExampleInstallmentSchedule() {
		List<PaySchedule> list = new ArrayList<PaySchedule>();
		list.add(new PaySchedule(1, 7, 2004, 1.5, 1916.0, 0.0, 1916.0, PaymentStatus.PAID));
		list.add(new PaySchedule(2, 7, 2005, 2.5, 3192.0, 1227.37, 4449.64, PaymentStatus.PAID));
		list.add(new PaySchedule(3, 7, 2006, 3.0, 3831.0, 1227.37, 5058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(4, 7, 2007, 4.0, 3831.0, 1227.37, 10058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(5, 7, 2008, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(6, 7, 2009, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(7, 7, 2010, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(8, 7, 2011, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(9, 7, 2012, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(10, 7, 2013, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(11, 7, 2014, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(12, 7, 2015, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(13, 7, 2016, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(14, 7, 2017, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		list.add(new PaySchedule(15, 7, 2018, 5.0, 3831.0, 1227.37, 12058.37, PaymentStatus.PAID));
		InstallmentSchedule i = new InstallmentSchedule();
		i.interest = 29583.98;
		i.principal = 118231.00;
		i.total = 151383.83;
		i.setLastUpdate(Calendar.getInstance().getTime());
		i.schedule = list;
		return i;
	}
}