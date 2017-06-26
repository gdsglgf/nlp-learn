package com.nlp.job;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nlp.model.FileModel;
import com.nlp.service.FileService;

@Service
public class FileLoader {
	private static final Logger log = LogManager.getLogger(FileLoader.class);
	@Value("${src.disk.id}")
	private int diskId;
	@Value("${src.file.type}")
	private String fileType;
	@Value("${src.file.paths}")
	private String[] filePath;
	
	@Autowired
	private FileService fileService;
	
	public List<FileModel> loadFile() {
		scanAndSaveFile();
		List<FileModel> files = fileService.getAllPendingFile();
		log.info(String.format("load %d files", files.size()));
		return files;
	}
	
	public void scanAndSaveFile() {
		log.info(String.format("diskId:%d, fileType:%s, paths:%s", diskId, fileType, Arrays.toString(filePath)));
		fileService.saveFileAtOnce(diskId, filePath, fileType);
	}
}
