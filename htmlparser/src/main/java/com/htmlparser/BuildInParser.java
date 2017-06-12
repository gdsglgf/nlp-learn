package com.htmlparser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class BuildInParser extends HTMLEditorKit.ParserCallback {
	StringBuffer s;

	public BuildInParser() {
	}

	public void parse(Reader in) throws IOException {
		s = new StringBuffer();
		ParserDelegator delegator = new ParserDelegator();
		// the third parameter is TRUE to ignore charset directive
		delegator.parse(in, this, Boolean.TRUE);
	}

	public void handleText(char[] text, int pos) {
		s.append(text); // style 标签内容也加进去了
	}

	public String getText() {
		return s.toString();
	}

	public static String parse(String html) {
		String text = "";
		try {
			// the HTML to convert
			Reader in = new StringReader(html);
			// FileReader in = new FileReader("text.html");
			BuildInParser parser = new BuildInParser();
			parser.parse(in);
			in.close();
			text = parser.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}