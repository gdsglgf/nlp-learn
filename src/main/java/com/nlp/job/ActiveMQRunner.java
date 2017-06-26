package com.nlp.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nlp.message.DocFileMessageSender;

public class ActiveMQRunner {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("dispatcher-servlet.xml");
		FileLoader fileLoader = ctx.getBean(FileLoader.class);
		DocFileMessageSender sender = ctx.getBean(DocFileMessageSender.class);
		sender.split(fileLoader.loadFile());
		ctx.registerShutdownHook();
	}

}
