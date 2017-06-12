package com.corenlp;

import java.util.Properties;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.HybridCorefAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

public class ChineseCoref {

	public static void main(String[] args) {
		String text = "全国两会接近尾声，习近平总书记去了六个团组，对一些重要工作做出新部署。对扶贫干部，他要求“坚守岗位”。";
		args = new String[] { 
				"-props", "edu/stanford/nlp/coref/hybrid/properties/zh-coref-default.properties" // StanfordCoreNLP-chinese.properties
				};

		Annotation document = new Annotation(text);
		Properties props = StringUtils.argsToProperties(args);
		StanfordCoreNLP corenlp = new StanfordCoreNLP(props);
		corenlp.annotate(document);
		HybridCorefAnnotator hcoref = new HybridCorefAnnotator(props);
		hcoref.annotate(document);
		
		System.out.println("---");
		System.out.println("coref chains");
		for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
			System.out.println("\t" + cc);
		}
		for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			System.out.println("---");
			System.out.println("mentions");
			for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
				System.out.println("\t" + m);
			}
		}
	}

}
