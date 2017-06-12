package com.hanlp;

import com.hankcs.hanlp.HanLP;
import com.htmlparser.util.IOUtils;

public class TraditionalChinese2SimplifiedChineseTest {
	public static void main(String[] args) {
		System.out.println(HanLP.convertToTraditionalChinese("用笔记本电脑写程序"));
		System.out.println(HanLP.convertToSimplifiedChinese("「以後等妳當上皇后，就能買士多啤梨慶祝了」"));
		
		String path = "E:\\NLP\\datasets\\wikimedia\\demo\\output.txt";
		String article = IOUtils.fileToString(path);
		System.out.println(article);
		System.out.println("\n\n-------------\n\n");
		System.out.println(HanLP.convertToSimplifiedChinese(article));
	}
}
