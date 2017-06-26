package com.nlp.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nlp.util.HTMLParser;
import com.nlp.util.NLPUtils;

@Controller
@RequestMapping("/")
public class DefaultController {
	private static final Logger logger = LogManager.getLogger(DefaultController.class);
	@ResponseBody
	@RequestMapping("test")
	public String test() {
		logger.debug("log test");
		return "helloworld";
	}
	
	@RequestMapping("/")
    public String index(HttpServletRequest request) {
		return "task/upload";
	}
	
	@RequestMapping("/index")
	public String indexView() {
		return "index";
	}
	
	@ResponseBody
	@RequestMapping("/loadPage.action")
	public String loadPageAction(@RequestParam(value = "url", required = true) String url) {
		logger.debug("url is " + url);
		System.out.println("start load page...");
		String content = HTMLParser.parseFromURL(url);
		System.out.println("load page done...");
		return content;
	}
	
	@RequestMapping("/segment")
	public ModelAndView segmentView(@RequestParam(value = "text", required = true) String text) {
		ModelAndView view = new ModelAndView("segment");
		view.addObject("content", NLPUtils.createHtml(text));
		return view;
	}
	
	@ResponseBody
	@RequestMapping("/removeStopWord.action")
	public List<String> removeStopWordAction(@RequestParam(value = "text", required = true) String text) {
		return NLPUtils.notionalTokenizer(text);
	}
	
	@RequestMapping("/wordcloud")
	public String wordcloudView() {
		return "wordcloud";
	}
	
	@ResponseBody
	@RequestMapping("/textRank.action")
	public Map<String, Float> textRankAction(@RequestParam(value = "text", required = true) String text) {
		return NLPUtils.textRank(text);
	}
	
	@ResponseBody
	@RequestMapping("/tdidf.action")
	public Map<String, Integer> tdidfAction(@RequestParam(value = "text", required = true) String text) {
		return NLPUtils.TDIDF(text);
	}
}
