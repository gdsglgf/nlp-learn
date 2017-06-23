package com.nlp.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.nlp.model.FileModel;
import com.nlp.model.States.FileState;
import com.nlp.service.FileService;
import com.nlp.util.FileUtils;
import com.nlp.util.HTMLParser;

class Product {
	public int cnt;
	public String doc;
	
	public Product(int cnt, String doc) {
		this.cnt = cnt;
		this.doc = doc;
	}
}

class Producer implements Runnable {
	private static final Logger log = LogManager.getLogger(Producer.class);
	private long timeout;
	private List<FileModel> files;
	private BlockingQueue<Product> queues;
	private FileService fileService;

	public Producer(long timeout, List<FileModel> files, BlockingQueue<Product> queues, FileService fileService) {
		this.timeout = timeout;
		this.files = files;
		this.queues = queues;
		this.fileService = fileService;
	}

	@Override
	public void run() {
		for (FileModel fm : files) {
			int fileId = fm.getFileId();
			fileService.updateFileStatus(fileId, FileState.PROCESSING.ordinal());
			int cnt = 0;
			String line = null;
			StringBuilder sb = new StringBuilder();
			String path = fm.getAbsolutePath();
			log.info(String.format("File start:[%s]", path));
			try (BufferedReader reader = FileUtils.getBufferedReaderForCompressedFile(path)) {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					if (line.contains("</doc>")) {
						cnt++;
						try {
							if (!queues.offer(new Product(cnt, sb.toString()), timeout, TimeUnit.SECONDS)) {
								log.error(String.format("Put data time out(%d seconds): cnt=%d, url=%s", 
										timeout, cnt, HTMLParser.parseUrl(sb.toString())));
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sb = new StringBuilder();
					}
				}
			} catch (IOException e) {
				log.error(e);
			}
			log.info(String.format("File end:[%s], total %d htmls", path, cnt));
			fileService.updateFileWebcount(fileId, cnt);
			fileService.updateFileStatus(fileId, FileState.SOLVED.ordinal());
		}
	}
	
}

class Consumer implements Runnable {
	private static final Logger log = LogManager.getLogger(Consumer.class);
	private long timeout;
	private BlockingQueue<Product> queues;
	private Standalone standalone;
	
	public Consumer(long timeout, BlockingQueue<Product> queues, Standalone standalone) {
		this.timeout = timeout;
		this.queues = queues;
		this.standalone = standalone;
	}

	@Override
	public void run() {
		Product product = null;
		while (true) {
			try {
				product = queues.poll(timeout, TimeUnit.SECONDS);
				if (product == null) {
					log.debug(String.format("Get data time out(%d seconds). Stop thread.", timeout));
					break;
				} else {
					standalone.parse(product.cnt, product.doc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
}

@Component
public class ConcurrentJob {
	@Value("${job.queue.capacity}")
	private int capacity;
	@Value("${job.thread.size}")
	private int nthread;
	@Value("${job.queue.timeout}")
	private long timeout;

	@Autowired
	FileLoader fileLoader;
	@Autowired
	private Standalone standalone;
	@Autowired
	private FileService fileService;
	
	public void exec() {
		List<FileModel> files = fileLoader.loadFile();
		BlockingQueue<Product> queues = new LinkedBlockingQueue<Product>(capacity);
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new Producer(timeout, files, queues, fileService));
		for (int i = 0; i < nthread; i++) {
			service.execute(new Consumer(timeout, queues, standalone));
		}
	}
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ConcurrentJob runner = ctx.getBean(ConcurrentJob.class);
		runner.exec();
		ctx.registerShutdownHook();
	}
}
