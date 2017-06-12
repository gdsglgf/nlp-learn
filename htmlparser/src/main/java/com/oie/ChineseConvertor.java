package com.oie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import com.hankcs.hanlp.HanLP;

/**
 * 中文繁体转简体
 * @author Administrator
 *
 */
public class ChineseConvertor {

	public static void main(String[] args) {
		String path = Constants.TRADITIONAL_WIKI_PATH;
		String outPath = Constants.SIMPLIFIED_WIKI_PATH;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			FileWriter writer = new FileWriter(outPath);
			String line = null, simp = null;
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				counter++;
				simp = HanLP.convertToSimplifiedChinese(line);
				System.out.println(counter + " ----  " + simp);
				System.out.println("\n-------------\n");
				
				writer.write(simp);
				writer.append('\n');
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
