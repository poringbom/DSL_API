package th.co.ktb.dsl;

import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.text.ParseException;

public class DateUtil {
	
	private static Integer offsetMs = 0;

	public static void setOffsetMs(Integer ms) {
		offsetMs = ms;
	}

	public static final Locale DEFAULT_LOCALE = new Locale("en_US");

	public static Date currDate() {
		Calendar cal = Calendar.getInstance(DEFAULT_LOCALE);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.MILLISECOND, -offsetMs);
		return cal.getTime();
	}

	public static Date currTime() {
		Calendar cal = Calendar.getInstance(DEFAULT_LOCALE);
		cal.add(Calendar.MILLISECOND, -offsetMs);
		return cal.getTime();
	}
	
	public static Date timePlusSec(Date d, int sec) {
		Calendar cal = Calendar.getInstance(DEFAULT_LOCALE);
		cal.setTime(d);
		cal.add(Calendar.SECOND, sec);
		return cal.getTime();
	}

	public static Date currDatePlusSec(int sec) {
		Calendar cal = Calendar.getInstance(DEFAULT_LOCALE);
		cal.add(Calendar.SECOND, sec);
		cal.add(Calendar.MILLISECOND, -offsetMs);
		return cal.getTime();
	}

	public static Date currDatePlusDay(int day) {
		final Calendar cal = Calendar.getInstance(DEFAULT_LOCALE);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static Date datePlusSec(final Date date, int sec) {
		Calendar cal = Calendar.getInstance(DEFAULT_LOCALE);
		cal.setTime(date);
		cal.add(Calendar.SECOND, sec);
		return cal.getTime();
	}

	public static String dateToDateStr(final Date date, String format) {
		DateFormat df = new SimpleDateFormat(format, DEFAULT_LOCALE);
		return df.format(date);
	}

	public static Date dateStrToDate(final String dateStr, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format, DEFAULT_LOCALE);
		return df.parse(dateStr);
	}

	public static Date BEtoAD(final Date be) {
		return convertYear(be, -543);
	}

	public static Date ADtoBE(final Date ad) {
		return convertYear(ad, 543);
	}

	public static Date convertYear(Date date, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return new Date(cal.getTimeInMillis());
	}
	
	public static Timestamp currentSQLDatetime() {
		Calendar cal = Calendar.getInstance(); 
		Timestamp dateSql = new Timestamp(cal.getTimeInMillis());
		return dateSql;
	}
}
