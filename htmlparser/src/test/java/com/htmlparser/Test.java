package com.htmlparser;

import org.htmlparser.Parser;
import org.htmlparser.visitors.TextExtractingVisitor;

public class Test {
	public static void main(String[] args) throws Exception {
		String sss = "<div class='title'>商品详细说明：</div><p style='word-break: break-all'>ESTEE LAUDER Perfectly Clean Splash Away Foaming Cleanser<br />为中性/混合性肌肤度身订制的清洁产品。 <br />";
		Parser parser = new Parser(sss);
		TextExtractingVisitor visitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(visitor);
		System.out.println(visitor.getExtractedText());
	}
}