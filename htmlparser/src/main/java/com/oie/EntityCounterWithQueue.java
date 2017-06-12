package com.oie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

class Reader implements Runnable {
	public static double total = 219681195;
	private BlockingQueue<List<String>> sharedQueue;

	public Reader(BlockingQueue<List<String>> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		String path = Constants.ENTITY_PATH;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = null;
			List<String> sentence = new ArrayList<String>();
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				counter++;
				System.out.printf("%d---%.6f%n", counter, counter / total);
				if (line.equals("")) {
					if (!sharedQueue.offer(sentence, 5, TimeUnit.SECONDS)) {
						System.out.println("放入数据失败: " + counter);
					}
					sentence = new ArrayList<String>();
				} else {
					sentence.add(line);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class Counter implements Runnable {
	private BlockingQueue<List<String>> sharedQueue;

	public Counter(BlockingQueue<List<String>> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		String word = null, ner = null, lastword = "", lastner = null;
		String[] tokens = null;
		List<String> sentence = null;
		while (true) {
			try {
				sentence = sharedQueue.poll(5, TimeUnit.SECONDS);
				if (null == sentence) {
					System.out.println("超过5s还没数据，认为所有生产线程都已经退出，自动退出消费线程。");
					break;
				} else {
					word = null;
					ner = null;
					lastword = "";
					lastner = null;
					for (String line : sentence) {
						tokens = line.split("\t");
						word = tokens[0];
						ner = tokens[1];

						if (ner.equals("O")) {
							// save lastword and lastner
							EntityCounterWithQueue.cacheEntity(lastword, lastner);

							lastword = "";
							lastner = null;
						} else {
							if (ner.equals(lastner)) {
								lastword = lastword + word;
							} else {
								// save lastword and lastner
								EntityCounterWithQueue.cacheEntity(lastword, lastner);

								lastword = word;
								lastner = ner;
							}
						}
					}
					EntityCounterWithQueue.cacheEntity(lastword, lastner);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}

public class EntityCounterWithQueue {
	private static Map<String, Map<String, Integer>> cacheCounter = new ConcurrentHashMap<String, Map<String, Integer>>();

	public static void cacheEntity(String word, String ner) {
		if (word == null || ner == null || word.equals("")) {
			return;
		}

		Map<String, Integer> cnt = cacheCounter.get(word);
		if (cnt == null) {
			cnt = new ConcurrentHashMap<String, Integer>();
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
			// BufferedWriter writer = new BufferedWriter(
			// 		new OutputStreamWriter(new FileOutputStream(Constants.COUNTER_PATH), StandardCharsets.UTF_8));

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

	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();

		final BlockingQueue<List<String>> sharedQueue = new LinkedBlockingQueue<List<String>>(10240);

		Thread readerThread = new Thread(new Reader(sharedQueue));
		readerThread.start();

		int nthreads = 32;
		Thread[] threads = new Thread[nthreads];
		for (int i = 0; i < nthreads; i++) {
			threads[i] = new Thread(new Counter(sharedQueue));
			threads[i].start();
		}

		readerThread.join();
		for (int i = 0; i < nthreads; i++) {
			threads[i].join();
		}

		saveCacheCounter();
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.printf("start: %s, end: %s, cost time: %d%n", DateUtils.format(startTime), DateUtils.format(endTime),
				TimeUnit.MILLISECONDS.toSeconds(duration));
	}

}
