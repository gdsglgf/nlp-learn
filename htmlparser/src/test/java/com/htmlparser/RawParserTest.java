package com.htmlparser;

public class RawParserTest {

	public static void main(String[] args) {
		String html = Configs.TEST_HTML;
//		System.out.println(html);
		
		System.out.println(RawParser.StripHT(html));
//		System.out.println(RawParser.Html2Text(html));
	}

}
