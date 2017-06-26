package com.nlp.tool;

import java.util.Arrays;
import java.util.Date;

import com.nlp.util.DateUtils;

public class Statistics {
	public static void main(String[] args) {
		System.out.println(String.format("args:%s", Arrays.toString(args)));
		if (args == null || args.length != 3) {
			System.out.println("Usage: Statistics <startTime> <endTime> <count>\n\t(time format is yyyy-MM-dd HH:mm:ss)");
			return;
		}
		try {
			Date startTime = DateUtils.parse(args[0]);
			Date endTime = DateUtils.parse(args[1]);
			double count = Integer.parseInt(args[2]);
			
			long total = endTime.getTime() - startTime.getTime();
			StringBuffer buf = new StringBuffer();
			
			long seconds = total / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			buf.append(String.format("Cost %dms = %ds = %dm = %dh%n", 
					total, seconds, minutes, hours)
			);
			
			buf.append("Average:\n");
			buf.append(String.format("\t%.2f items / second%n", count / seconds));
			buf.append(String.format("\t%.2f items / minutes%n", count / minutes));
			buf.append(String.format("\t%.2f items / hours%n", count / hours));
			
			System.out.println(buf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
