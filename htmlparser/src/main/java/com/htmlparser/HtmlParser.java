package com.htmlparser;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.TextExtractingVisitor;

public class HtmlParser {
	/**
	 * 去除html代码的script和style标签
	 * @param html
	 * @return
	 */
	public static String trimTags(String html) {
		NodeFilter scriptFilter = new NodeClassFilter(ScriptTag.class);
		NodeFilter styleFilter = new NodeClassFilter(StyleTag.class);
		NodeFilter[] filter = { scriptFilter, styleFilter };
		OrFilter orFilter = new OrFilter(filter);
		String text = "";
		try {
			text = ParserUtils.trimTags(html, orFilter, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * 根据提供的URL，获取此URL对应网页的纯文本信息
	 * 
	 * @param url 提供的URL链接
	 * @return RL对应网页的纯文本信息
	 */
	public static String getText(String url) {
		StringBean sb = new StringBean();
		try { // 设置不需要得到页面所包含的链接信息
			sb.setLinks(false); // 设置将不间断空格由正规空格所替代
			sb.setReplaceNonBreakingSpaces(true); // 设置将一序列空格由一个单一空格所代替
			sb.setCollapse(true); // 传入要解析的URL
			sb.setURL(url);
		} catch (Exception e) {
		}
		return sb.getStrings();
	}
	
	/**
	 * 获取纯文本信息
	 * 
	 * @param str
	 * @return
	 */
	public static String getPlainText(String str) {
		try {
			Parser parser = new Parser();
			parser.setInputHTML(str);
			StringBean sb = new StringBean(); // 设置不需要得到页面所包含的链接信息
			sb.setLinks(false); // 设置将不间断空格由正规空格所替代
			sb.setReplaceNonBreakingSpaces(true); // 设置将一序列空格由一个单一空格所代替
			sb.setCollapse(true);
			parser.visitAllNodesWith(sb);
			str = sb.getStrings();
		} catch (ParserException e) {
		}
		return str;
	}

	/**
	 * 获取html所有标签内(包括style和script标签)的文本
	 * @param html
	 * @return
	 */
	public static String getPlainText2(String html) {
		String text = "";
		try {
			Parser parser = new Parser();
			parser.setInputHTML(html);
			TextExtractingVisitor visitor = new TextExtractingVisitor();
			parser.visitAllNodesWith(visitor);
			text = visitor.getExtractedText();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return text;
	}

	// 提取Hub类网页文本内容,如yahoo,sina等门户网
	public static String getHubEntries(String html) {
		StringBean bean = new StringBean();
		bean.setLinks(false);
		bean.setReplaceNonBreakingSpaces(true);
		bean.setCollapse(true);
		
		try {
			Parser parser = new Parser(html);
			parser.visitAllNodesWith(bean);
			parser.reset();
		} catch (ParserException e) {
			System.err.println("getHubEntries()-->" + e);
		}
		
		return bean.getStrings();
	}

	// 获取主题性(Topical)网页文本内容：对于博客等以文字为主体的网页效果较好
	public static String getTopicBlock(String html) {
		HasParentFilter acceptedFilter = new HasParentFilter(new TagNameFilter("p"));
		NodeList nodes = null;
		
		try {
			Parser parser = new Parser(html);
			nodes = parser.extractAllNodesThatMatch(acceptedFilter);
			parser.reset();
		} catch (ParserException e) {
			System.err.println("getTopicBlock" + e);
		}

		StringBuffer sb = new StringBuffer();
		SimpleNodeIterator iter = nodes.elements();
		while (iter.hasMoreNodes()) {
			Node node = iter.nextNode();
			try {
				sb.append(node.getText() + "\n");
			} catch(Exception e) {
				System.err.println("getText:" + e);
			}
		}
		
		return sb.toString();
	}
}
