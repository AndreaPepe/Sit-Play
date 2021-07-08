package main.java.engineering.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DatetimeUtil {
	public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";
	public static final String MYSQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_STRING_FORMAT = "dd/MM/yyyy";
	public static final String TIME_STRING_FORMAT = "HH:mm";
	
	// private constructor to avoid instances; only static methods
	private DatetimeUtil() {}
	
	public static Date stringToDate(String date, String time) {
		try {
			var format = new SimpleDateFormat(DATETIME_FORMAT);

			return format.parse(date + " " + time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getCurrentDatetime() {
		var formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
		var format = new SimpleDateFormat(DATETIME_FORMAT);
		var currentDateTime = LocalDateTime.now();
		String now = formatter.format(currentDateTime);
		try {
			return format.parse(now);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Boolean isFutureDatetime(Date date) {
		var current = getCurrentDatetime();
		return (date != null && date.after(current));
	}
	
	public static Boolean isFutureDatetime(String date, String time) {
		var builtDate = stringToDate(date, time);
		var current = getCurrentDatetime();
		return (builtDate != null && builtDate.after(current));
	}
	
	public static String getDate(Date date) {
		var format = new SimpleDateFormat(DATE_STRING_FORMAT);
		return format.format(date);
	}
	
	public static String getTime(Date date) {
		var format = new SimpleDateFormat(TIME_STRING_FORMAT);
		return format.format(date);
	}
	
	public static java.sql.Timestamp fromDateToMysqlTimestamp(Date date){
		return new java.sql.Timestamp(date.getTime());
	}
	
	public static Date fromMysqlTimestampToDate(java.sql.Timestamp mysql) {
		var format = new SimpleDateFormat(DATETIME_FORMAT);
		var string = format.format(mysql);
		try {
			return format.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
