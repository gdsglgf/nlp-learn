package com.nlp.tool;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nlp.job.FileLoader;

public class FileScanner {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		FileLoader fileLoader = ctx.getBean(FileLoader.class);
		fileLoader.scanAndSaveFile();
	}

}
