package com.htmlparser;

import org.junit.Test;

public class HtmlParserTest {
	@Test
	public void testTrimTags() {
		System.out.println("-----------testTrimTags--------------");
		System.out.println(HtmlParser.trimTags(Configs.TEST_DEMO));
	}
	
	@Test
	public void testGetPlainText() {
		System.out.println("------------testGetPlainText-------------");
		System.out.println(HtmlParser.getPlainText(Configs.TEST_DEMO));
	}
	
	@Test
	public void testGetPlainText2() {
		System.out.println("------------testGetPlainText2-------------");
		System.out.println(HtmlParser.getPlainText2(Configs.TEST_DEMO));
	}
	
	@Test
	public void testGetHubEntries() {
		System.out.println("------------testGetHubEntries-------------");
		System.out.println(HtmlParser.getHubEntries(Configs.TEST_DEMO));
	}
	
	@Test
	public void testGetTopicBlock() {
		System.out.println("------------testGetTopicBlock-------------");
		System.out.println(HtmlParser.getTopicBlock(Configs.TEST_DEMO));
	}
	
	@Test
	public void testGetText() {
		System.out.println("------------testGetText-------------");
		System.out.println(HtmlParser.getText("http://www.tuicool.com/articles/UJJvim"));
	}
}
