package com.htmlparser;

import com.htmlparser.util.IOUtils;

public class Configs {
	public static final String TEST_FILE = "data/wiki_math.html";
	public static final String TEST_FILE1 = "data/test1.html";
	public static final String TEST_FILE2 = "data/demo.html";
	
	public static final String TEST_DEMO = IOUtils.fileToString(Configs.TEST_FILE2);
	public static final String TEST_HTML = IOUtils.fileToString(Configs.TEST_FILE);
}
