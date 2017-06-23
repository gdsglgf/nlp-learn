package com.nlp.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StandaloneRunner {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Standalone runner = ctx.getBean(Standalone.class);
		runner.exec();
		ctx.registerShutdownHook();
	}

}
