package com.corenlp;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.machinereading.structure.EntityMention;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * 基于模板的实体识别，只支持英文。
 * http://nlp.stanford.edu/software/regexner.html
 */
public class RegexNERTest {
	public static void ner() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		props.put("file", "data/corenlp/JuliaGillard.txt");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		try {
			pipeline.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void regexner() {
		Properties props = new Properties();
		props.put("annotators", "tokenize,ssplit,regexner");	// tokenize,ssplit,pos,lemma,ner,regexner
		props.put("regexner.mapping", "data/corenlp/jg-regexner.txt");
		props.put("file", "data/corenlp/JuliaGillard.txt");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		try {
			pipeline.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void relation() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, depparse, relation");
//		props.put("regexner.mapping", "data/corenlp/jg-regexner.txt");
//		props.put("file", "data/corenlp/JuliaGillard.txt");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		Annotation document = new Annotation(
				"Barack Obama was born in Hawaii.  He is the president. Obama was elected in 2008.");
		
		pipeline.annotate(document);
		System.out.println("---");
		for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			System.out.println("---");
			System.out.println("entities");
			
			List<EntityMention> entities = sentence.get(MachineReadingAnnotations.EntityMentionsAnnotation.class);
			for (EntityMention m : entities) {
				System.out.println("\t" + m);
			}
			
			// set relations
			System.out.println("---");
			System.out.println("relations");
		    List<RelationMention> relations = sentence.get(MachineReadingAnnotations.RelationMentionsAnnotation.class);
		    for (RelationMention r : relations) {
		    	System.out.println("\t" + r);
		    }
		}
	}

	public static void demo() {
		String text = "Obama was born in Hawaii. He is our president.";
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation doc = new Annotation(text);
		pipeline.annotate(doc);
		for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
			List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
			for(CoreLabel token : tokens) {
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				System.out.println(word + "----" + ner);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		relation();
	}

}
