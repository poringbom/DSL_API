package th.co.ktb.dsl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {
	public static Date parseDate(String d) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(d);
		} catch (ParseException pe) {
			return Calendar.getInstance().getTime();
		}
	}
}
