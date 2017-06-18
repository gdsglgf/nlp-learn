package com.nlp.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtils {
	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	private static String getEncode(InputStream inputStream) {
		String code = "gb2312";
		try {
			byte[] head = new byte[3];
			inputStream.read(head);
			if (head[0] == -17 && head[1] == -69 && head[2] == -65)
				code = "UTF-8";
			if (head[0] == -1 && head[1] == -2)
				code = "UTF-16";
			if (head[0] == -2 && head[1] == -1)
				code = "Unicode";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 发送http请求 POST和GET请求都可以
	 * 
	 * @param requestUrl 请求地址
	 * @param method 传入的执行的方式是GET还是POST方式
	 * @return String
	 */
	public static String HttpURLConnRequest(String requestUrl, String method) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("contentType", "UTF-8");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoInput(true);
			conn.setRequestMethod(method);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true); // 重定向
			conn.connect();

			String type = conn.getContentType();
			System.out.println("content type: " + type);

			// 将返回的输入流转换成字符串
			InputStream inputStream = conn.getInputStream();
			String encode = getEncode(inputStream);
			System.out.println(encode);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encode);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
				buffer.append(LINE_SEPARATOR);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public static String get(String url) {
		return HttpURLConnRequest(url, "GET");
	}

	public static void main(String[] args) {
		String url = "http://www.jinyongbbs.com/jinyong/yyd/204.html";
		System.out.println(HttpURLConnRequest(url, "GET"));
	}
}