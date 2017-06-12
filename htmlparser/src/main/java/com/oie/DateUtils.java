package com.oie;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	public static String now() {
		return sdf.format(new Date());
	}
	
	public static String format(long date) {
		return sdf.format(new Date(date));
	}
	
	public static void main(String[] args) {
		System.out.println(now());
	}
}
