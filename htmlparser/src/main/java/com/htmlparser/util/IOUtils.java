package com.htmlparser.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {
	/**
	 * 读文件到字符串
	 * @param path 文件路径名
	 * @return 文件内容字符串
	 */
	public static String fileToString(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
