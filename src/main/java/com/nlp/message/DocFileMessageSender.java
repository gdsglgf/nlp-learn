package com.nlp.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.nlp.model.FileModel;
import com.nlp.model.Task;
import com.nlp.model.States.FileState;
import com.nlp.model.States.TaskState;
import com.nlp.service.FileService;
import com.nlp.service.TaskService;
import com.nlp.util.FileUtils;

@Component
public class DocFileMessageSender {
	private static final Logger log = LogManager.getLogger(DocFileMessageSender.class);
	@Resource(name="docFileJmsTemplate")
	private JmsTemplate docFileJmsTemplate;
	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
	
	public void sendMessage(Map<String, Object> mapMessage) {
		docFileJmsTemplate.convertAndSend(mapMessage);
//		jmsTemplate.convertAndSend("message.docFile", textMessage);
	}
	
	public void split(List<FileModel> files) {
		for (FileModel fm : files) {
			Map<String, Object> message = new HashMap<String, Object>();
			int fileId = fm.getFileId();
			fileService.updateFileStatus(fileId, FileState.PROCESSING.ordinal());
			message.put("fileId", fileId);
			
			int cnt = 0;
			String line = null;
			StringBuilder sb = new StringBuilder();
			String path = fm.getAbsolutePath();
			try (BufferedReader reader = FileUtils.getBufferedReaderForCompressedFile(path)) {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					if (line.contains("</doc>")) {
						cnt++;
						message.put("doc", sb.toString());
						docFileJmsTemplate.convertAndSend(message);
						sb = new StringBuilder();
					}
				}
			} catch (IOException e) {
				log.debug(e);
			}
			
			fileService.updateFileWebcount(fileId, cnt);
			fileService.updateFileStatus(fileId, FileState.SOLVED.ordinal());
			log.info(String.format("File:[%s], total %d htmls", path, cnt));
		}
	}
	
	public void execute(int taskId) {
		Thread t = new Thread(() -> {
			List<FileModel> files = taskService.getFileList(taskId, FileState.PUBLISHED.ordinal());
			split(files);
			Task task = new Task(taskId, new Date(), TaskState.SOLVED.ordinal());
			taskService.updateTask(task);
		});
		t.start();
	}
	
	public void execute() {
		Thread t = new Thread(() -> {
			List<FileModel> files = fileService.getAllPendingFile();
			split(files);
		});
		t.start();
	}
}
