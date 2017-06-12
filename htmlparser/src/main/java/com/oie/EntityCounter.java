package com.oie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

public class EntityCounter {
	// 欧洲={LOCATION=10, DEMONYM=1}
	private static Map<String, Map<String, Integer>> cacheCounter = new HashMap<String, Map<String, Integer>>();
	
	public static void cacheEntity(String word, String ner) {
		if (word == null || ner == null || word.equals("")) {
			return;
		}
		
		Map<String, Integer> cnt = cacheCounter.get(word);
		if (cnt == null) {
			cnt = new HashMap<String, Integer>();
			cnt.put(ner, 1);
			cacheCounter.put(word, cnt);
		} else {
			Integer num = cnt.get(ner);
			if (num == null) {
				num = 1;
			} else {
				num = num + 1;
			}
			cnt.put(ner, num);
		}
	}
	
	public static void saveCacheCounter() {
		try {
			FileWriter writer = new FileWriter(Constants.COUNTER_PATH);
			for (Entry<String, Map<String, Integer>> ent : cacheCounter.entrySet()) {
				writer.write(JSON.toJSONString(ent));
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String path = Constants.ENTITY_PATH;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = null, word = null, ner = null, lastword = "", lastner = null;
			String[] tokens = null;
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				counter++;
				System.out.printf("%d---[%s]%n", counter, line);
				if (line.length() > 0) {
					tokens = line.split("\t");
					word = tokens[0];
					ner = tokens[1];
//					System.out.printf("%s---%s%n", tokens[0], tokens[1]);
					if (ner.equals("O")) {
						// save lastword and lastner
						cacheEntity(lastword, lastner);
						
						lastword = "";
						lastner = null;
					} else {
						if (ner.equals(lastner)) {
							lastword = lastword + word;
						} else {
							// save lastword and lastner
							cacheEntity(lastword, lastner);
							
							lastword = word;
							lastner = ner;
						}
					}
				} else {
					cacheEntity(lastword, lastner);
					lastword = "";
					lastner = null;
				}
			}
			reader.close();
			saveCacheCounter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
