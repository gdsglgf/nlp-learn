package com.oie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * JVM参数：-Xms2g -Xmx3g
 * 
 * @author Administrator
 * 
 *  Eclipse下运行ok, maven命令行下抛异常。。。 
 *  mvn exec:java -Dexec.mainClass="com.oie.EntityExtractor" 抛异常
 *  com.alibaba.fastjson.JSONException: syntax error, unexpect token error
 *
 */
public class EntityExtractor {
	private static FileWriter writer = null;
	private static StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");

	public static void loadModel() {
		if (pipeline == null) {
			System.out.println("start load model....");
			long start = System.currentTimeMillis();
			pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");
			long duration = System.currentTimeMillis() - start;
			System.out.printf("model is loaded: using %d seconds", TimeUnit.MILLISECONDS.toSeconds(duration));
		}
	}

	public static void extractEntity(String text) throws IOException {
		if (text == null || text.trim().equals("")) {
			System.out.println("Empty text");
			return;
		}
		text = text.trim();

		loadModel();

		// 用一些文本来初始化一个注释。文本是构造函数的参数。
		Annotation annotation = new Annotation(text);

		// 运行所有选定的代码在本文
		pipeline.annotate(annotation);

		// 从注释中获取CoreMap List
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			// 从CoreMap中取出CoreLabel List，逐一打印出来
			List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
			for (CoreLabel token : tokens) {
				String word = token.getString(TextAnnotation.class);
				String ner = token.getString(NamedEntityTagAnnotation.class);
				writer.write(word + "\t" + ner + "\n");
			}
			writer.append('\n');
		}
		writer.flush();
	}

	public static void main(String[] args) {
		String path = Constants.SIMPLIFIED_WIKI_PATH;
		String outPath = Constants.ENTITY_PATH;
		int skip = 43;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			writer = new FileWriter(outPath, true);
			String line = null;
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				counter++;
				System.out.printf("%d: %s--%s%n", counter, DateUtils.now(), line);
				if (counter < skip) {
					continue;
				}
				Wiki wiki = JSON.parseObject(line, Wiki.class);
				// System.out.println(counter + "----" + wiki.getText());
				extractEntity(wiki.getText());

				// if (counter == 43) {
				// break;
				// }
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
