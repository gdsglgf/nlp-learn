package com.nlp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.summary.TextRankKeyword;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;

public class NLPUtils {
	public static final String WORD_NOTATIONS = "nvpkrRmMcuehqdyoxatsbfzw";
	public static final String DEFAULT_NOTATION = "other";

	public static String getNotation(char type) {
		int index = WORD_NOTATIONS.indexOf(type);
		if (index == -1) {
			return DEFAULT_NOTATION;
		} else {
			return String.valueOf(type);
		}
	}
	
	public static String createHtml(String text) {
		Segment segment = HanLP.newSegment();
		List<Term> terms = segment.seg(text);
		StringBuffer buf = new StringBuffer();
		for (Term t : terms) {
			buf.append(String.format("<span class=\"%s\">%s</span>", 
					getNotation(t.nature.firstChar()), t.toString()));
		}
		return buf.toString();
	}

	private static List<Term> filter(List<Term> terms, String prefix) {
		Set<String> wordSet = new HashSet<String>();
		List<Term> result = new ArrayList<Term>();
		for (Term t : terms) {
			if (t.nature.startsWith(prefix)) {
				if (wordSet.add(t.word)) {
					result.add(t);
				}
			}
		}
		return result;
	}
	
	public static String summary(String text) {
		List<String> sentenceList = HanLP.extractSummary(text, 3);
		StringBuffer buf = new StringBuffer();
		for (String s : sentenceList) {
			buf.append(s);
		}
		return buf.toString();
	}

	public static String keyword(String text) {
		List<String> keywordList = HanLP.extractKeyword(text, 5);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			buf.append(keywordList.get(i));
			buf.append(", ");
		}
		buf.append(keywordList.get(4));
		return buf.toString();
	}
	
	public static Map<String, Float> textRank(String text) {
		TextRankKeyword textRankKeyword = new TextRankKeyword();
		Map<String, Float> rank = textRankKeyword.getTermAndRank(text);
		return rank;
	}
	
	/**
	 * 自动去除停用词
	 * @param text
	 * @return
	 */
	public static List<String> notionalTokenizer(String text) {
		List<Term> terms = NotionalTokenizer.segment(text);
		List<String> words = new ArrayList<String>(terms.size());
		for (Term t : terms) {
			words.add(t.word);
		}
		return words;
	}
	
	public static Map<String, Integer> TDIDF(String text) {
		Map<String, Integer> counter = new HashMap<String, Integer>();
		List<String> words = notionalTokenizer(text);
		for (String w : words) {
			if (counter.containsKey(w)) {
				counter.put(w, counter.get(w) + 1);
			} else {
				counter.put(w, 1);
			}
		}
		return counter;
	}
}
