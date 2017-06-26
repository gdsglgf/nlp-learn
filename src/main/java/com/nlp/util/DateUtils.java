package com.nlp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	// yyyy-MM-dd HH:mm:ss:SSS
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private DateUtils() {}

	public static String now() {
		return sdf.format(new Date());
	}
	
	public static String format(long date) {
		return sdf.format(new Date(date));
	}

	public static String format(Date date) {
		if (date == null) {
			return null;
		}
		return sdf.format(date);
	}
	
	public static Date parse(String d) {
		try {
			return sdf.parse(d);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(now());
	}
}
