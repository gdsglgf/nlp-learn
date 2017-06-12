package com.corenlp;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.htmlparser.Configs;
import com.htmlparser.RawParser;
import com.htmlparser.util.IOUtils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.ClassifierCombiner;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.ObjectBank;
import edu.stanford.nlp.util.StringUtils;

/**
 * 命名实体识别
 * http://nlp.stanford.edu/software/CRF-NER.html
 *
 */
public class CRFClassifierTest {
	static String news = "President Xi Jinping of China, on his first state visit to the United States, showd off his familiarity with American history and pop culture on Tuesday night.";

	public static void fullClassify(String text) {
		Properties props = new Properties();
		props.put("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz,edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz,edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
		try {
			ClassifierCombiner<CoreLabel> ec = new ClassifierCombiner<CoreLabel>(props);
			classify(ec, text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void classify(AbstractSequenceClassifier<CoreLabel> classifier, String text) {
		try {
			ObjectBank<List<CoreLabel>> documents = classifier.makeObjectBankFromString(text,
					classifier.plainTextReaderAndWriter());

			for (List<CoreLabel> doc : documents) {
				List<CoreLabel> docOutput = classifier.classify(doc);
				for (CoreLabel wi : docOutput) {
					String token = StringUtils.getNotNullString(wi.get(CoreAnnotations.TextAnnotation.class));
					String ans = StringUtils.getNotNullString(wi.get(CoreAnnotations.AnswerAnnotation.class));
					if (!ans.equals("O")) {
						System.out.println(token + "/" + ans);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void classifyForEnglish(String text) {
		String serializedClassifier = "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
		try {
			AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
			classify(classifier, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void classifyForChinese(String text) {
		String serializedClassifier = "edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz";
		List<String> words = SegDemo.seg(text);
//		text = words.stream().collect(Collectors.joining(" "));
		text = String.join(" ", words);
		System.out.println(text);
		try {
			AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
			System.out.println(classifier.classifyToString(text));
			classify(classifier, text);
			System.out.println(classifier.classifyWithInlineXML(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// String text = "Good afternoon Rajat Raina, how are you today? I go to
		// school at Stanford University, which is located in California.";
		
		fullClassify(news);

		
//		String text = "奥巴马出生在夏威夷 。他是美国总统 。";
//		 System.out.println(text);
//		 List<String> words = SegDemo.seg(text);
//		 System.out.println(words);
//		 text = words.stream().reduce((a, b) -> a + " " + b).get();
//		 System.out.println(text);
		 
		String text = RawParser.StripHT(Configs.TEST_HTML);
		
		
		classifyForChinese(IOUtils.fileToString("data/other.txt"));

	}
}