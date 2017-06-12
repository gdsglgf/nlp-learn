package com.corenlp;

import java.util.List;

import edu.stanford.nlp.ie.machinereading.structure.EntityMention;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class ChineseRelation {

	public static void main(String[] args) {
		String text = "泰山坐落于山东省中部。马云创建了阿里巴巴。李先生在百度公司工作。王先生住在北京.";
		StanfordCoreNLP pipeline = new StanfordCoreNLP("zh-relation.properties");
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		System.out.println("---");
		for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			System.out.println("---");
			System.out.println("entities");
			// set entities
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

}
