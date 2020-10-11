package de.iconten.client.rest.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

	public static final SimpleDateFormat simpleISODateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat simpleISODateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static final File USERDIR = new File(System.getProperty("user.dir"));

	public static String formatDate(Date date) {
		return simpleISODateFormat.format(date);
	}

	public static Calendar getCalendar() {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
}
