package com.htmlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawParser {
	public static final String TEG_REGEX = "<script[\\s\\S]*?</script>|<style[\\s\\S]*?</style>|<[^>]+>";
	/**
	 * 从html中提取纯文本
	 * 去除<html>的标签和多余的空白符
	 * @param html 网页代码
	 * @return 
	 */
	public static String StripHT(String html) {
		Pattern pattern = Pattern.compile(TEG_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(html);
		String text = match.replaceAll(" "); // 剔出<html>的标签
		text = text.replaceAll("\\s{2,}", " ");// 去除字符串中的空格,回车,换行符,制表符
		
		return text.trim();
	}

	/**
	 * 从html中提取纯文本
	 * 去除<html>的标签和多余的空白符
	 * @param html 网页代码
	 * @return 
	 */
	public static String Html2Text(String html) {
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(html);
			html = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(html);
			html = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(html);
			html = m_html.replaceAll(" "); // 过滤html标签
			textStr = html;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		// 剔除多余空白符
		textStr = textStr.replaceAll("\\s{2,}", " ");
		return textStr.trim();// 返回文本字符串
	}
}
