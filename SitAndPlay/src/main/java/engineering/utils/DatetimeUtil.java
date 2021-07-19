package main.java.engineering.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import main.java.engineering.exceptions.DateParsingException;

public class DatetimeUtil {
	public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";
	public static final String MYSQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_STRING_FORMAT = "dd/MM/yyyy";
	public static final String TIME_STRING_FORMAT = "HH:mm";
	
	// private constructor to avoid instances; only static methods
	private DatetimeUtil() {}
	
	public static Date stringToDate(String date, String time) throws DateParsingException {
		try {
			var format = new SimpleDateFormat(DATETIME_FORMAT);

			return format.parse(date + " " + time);
		} catch (ParseException e) {
			throw new DateParsingException("Impossible to parse date and time, please review your inputs");
		}
	}
	
	public static Date getCurrentDatetime() throws DateParsingException {
		var formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
		var format = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
		var currentDateTime = LocalDateTime.now();
		String now = formatter.format(currentDateTime);
		try {
			return format.parse(now);
		} catch (ParseException e) {
			throw new DateParsingException("Unable to build current timestamp. Please retry later");
		}
	}
	
	public static Boolean isFutureDatetime(Date date) throws DateParsingException {
		var current = getCurrentDatetime();
		return (date != null && date.after(current));
	}
	
	public static Boolean isFutureDatetime(String date, String time) throws DateParsingException {
		var builtDate = stringToDate(date, time);
		var current = getCurrentDatetime();
		return (builtDate != null && builtDate.after(current));
	}
	
	
	public static Boolean isValidDateWithMargin(String date, String time, int marginHours) throws DateParsingException {
		var builtDate = stringToDate(date, time);
		var calendar = Calendar.getInstance();
		calendar.setTime(builtDate);
		calendar.add(Calendar.HOUR_OF_DAY, - marginHours);
		var limitDatetime = calendar.getTime();
		var current = getCurrentDatetime();
		// return true if the current time is before the built date - the hours of margin
		if (builtDate == null || current == null) {
			throw new DateParsingException("Something went wrong parsing timestamps, please retry later.");
		}
		return  (current.before(limitDatetime));
	}
	public static String getDate(Date date) {
		var format = new SimpleDateFormat(DATE_STRING_FORMAT, Locale.getDefault());
		return format.format(date);
	}
	
	public static String getTime(Date date) {
		var format = new SimpleDateFormat(TIME_STRING_FORMAT, Locale.getDefault());
		return format.format(date);
	}
	
	public static java.sql.Timestamp fromDateToMysqlTimestamp(Date date){
		return new java.sql.Timestamp(date.getTime());
	}
	
	public static Date fromMysqlTimestampToDate(java.sql.Timestamp mysql) throws DateParsingException {
		var format = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
		var string = format.format(mysql);
		try {
			return format.parse(string);
		} catch (ParseException e) {
			throw new DateParsingException("Unable to parse date retrieved from database. Please retry later.");
		}
	}
}
