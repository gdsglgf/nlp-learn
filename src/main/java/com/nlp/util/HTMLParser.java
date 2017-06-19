package com.nlp.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.nlp.dto.LinkDTO;
import com.nlp.dto.WebDTO;

public class HTMLParser {
	private static final Logger log = LogManager.getLogger(HTMLParser.class);
	public static final Pattern CHARSET_PATTERN = Pattern
			.compile("(?:charset|Charset|CHARSET)\\s*=\\s*\"?\\s*([-\\w]*?)[^-\\w]");
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final Pattern DOCNO_PATTERN = Pattern.compile("<docno>(.*)</docno>");
	public static final Pattern URL_PATTERN = Pattern.compile("<url>(.*)</url>");

	/**
	 * 获取第一个匹配的项, 没有匹配到返回空字符串
	 * 
	 * @param input 输入字符串
	 * @param pattern 正则表达式
	 * @return
	 */
	public static String parseOne(String input, Pattern pattern) {
		String result = "";
		Matcher m = pattern.matcher(input);
		if (m.find()) {
			result = m.group(1);
		}
		return result;
	}

	/**
	 * 获取docno标签的内容, 没有匹配到返回空字符串 <docno>123456</docno>
	 * 
	 * @param input 输入字符串
	 * @return
	 */
	public static String parseDocno(String input) {
		return parseOne(input, DOCNO_PATTERN);
	}

	/**
	 * 获取url标签的内容, 没有匹配到返回空字符串 <url>http://www.test.com/</url>
	 * 
	 * @param input 输入字符串
	 * @return
	 */
	public static String parseUrl(String input) {
		return parseOne(input, URL_PATTERN);
	}

	/**
	 * 获取匹配字符集, 没有匹配到返回默认字符集 <meta charset="UTF-8">
	 * <meta http-equiv="content-type" content="text/html;charset=utf-8">
	 * 
	 * @param input 输入字符串
	 * @return
	 */
	public static String parseCharset(String input) {
		String charset = parseOne(input, CHARSET_PATTERN);
		if (charset.equals("")) {
			charset = DEFAULT_CHARSET;
		}
		return charset;
	}

	public static String parseFromURL(String url) {
		// String html = HttpRequestUtils.get(url);
		// System.out.println(html);
		// String encode = parseCharset(html);
		try {
			Document doc = Jsoup.parse(new URL(url), 3000);
			return doc.getElementsByTag("body").text().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * result.put("docno", docno);<br>
	 * result.put("url", url);<br>
	 * result.put("title", title);<br>
	 * result.put("text", text);<br>
	 * result.put("links", links);<br>
	 * result.put("hasText", hasText);<br>
	 * @param html
	 * @return
	 */
	public static Map<String, Object> parserFromString(String html) {
		String docno = parseDocno(html), url = parseUrl(html), title = "", text = "";
		List<LinkDTO> links = new ArrayList<LinkDTO>();
		boolean hasText = true;
		try {
			Document doc = Jsoup.parse(html, url, Parser.xmlParser());
			title = doc.getElementsByTag("title").text();
			doc.select("style").remove();
			doc.select("script").remove();
			text = doc.getElementsByTag("body").text();
			for (Element link : doc.select("a[href]")) {
				String href = link.attr("abs:href").trim();
				String txt = link.text().trim();
				if (!href.equals("") && !href.equals(url) && !txt.equals("")) {
					links.add(new LinkDTO(href, txt));
				}
			}
		} catch (IllegalArgumentException e) {
			hasText = false;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("docno", docno);
		result.put("url", url);
		result.put("title", title);
		result.put("text", text);
		result.put("links", filterLink(links));
		result.put("hasText", hasText);

		return result;
	}
	
	public static WebDTO parse(String html) {
		String docno = parseDocno(html), url = parseUrl(html), title = "", text = "";
		List<LinkDTO> links = new ArrayList<LinkDTO>();
		boolean hasText = true;
		try {
			Document doc = Jsoup.parse(html, url, Parser.xmlParser());
			title = doc.getElementsByTag("title").text();
			doc.select("style").remove();
			doc.select("script").remove();
			text = doc.getElementsByTag("body").text();
			for (Element link : doc.select("a[href]")) {
				String href = link.attr("abs:href").trim();
				String linkText = link.text().trim();
				if (!href.equals("") && !href.equals(url) && !linkText.equals("")) {
					links.add(new LinkDTO(href, linkText));
				}
			}
		} catch (Exception e) {
			hasText = false;
			log.error(String.format("Error:%s, url:%s", e.getMessage(), url));
		}
		
		return new WebDTO(docno, url, title, text, filterLink(links), hasText);
	}
	
	private static List<LinkDTO> filterLink(List<LinkDTO> links) {
		Set<LinkDTO> set = new HashSet<LinkDTO>(links);
		return new ArrayList<LinkDTO>(set);
	}
}
