package com.htmlparser;

import java.util.Set;

public class HtmlLinkParserTest {

	public static void main(String[] args) {
		String url = "http://www.tuicool.com/articles/UJJvim";
		Set<String> links = HtmlLinkParser.extracLinks(url);
		for (String s : links) {
			System.out.println(s);
		}
	}

}
