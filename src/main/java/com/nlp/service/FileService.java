package com.nlp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlp.dto.DatatablesViewPage;
import com.nlp.mapper.FileMapper;
import com.nlp.model.FileModel;
import com.nlp.model.Folder;
import com.nlp.model.States.FileState;
import com.nlp.util.FileUtils;

@Service
@Transactional
public class FileService {
	private static final Logger log = LogManager.getLogger(FileService.class);
	@Autowired
	private FileMapper fileMapper;
	
	private boolean isAllTrue(boolean[] flags) {
		boolean result = false;
		if (flags != null) {
			result = true;
			for (boolean b : flags) {
				if (b == false) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	public Map<String, Object> scanFile(String[] filePath, String fileType) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean[] isDir = FileUtils.checkAllDirectory(filePath);
		result.put("isSuccessful", isAllTrue(isDir));
		result.put("status", isDir);
		return result;
	}
	
	public Map<String, Object> folderDetails(String[] filePath, String fileType) {
		Map<String, Object> result = new HashMap<String, Object>();;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		int[] counts = new int[filePath.length];
		for (int i = 0; i < filePath.length; i++) {
			String path = filePath[i];
			List<File> files = FileUtils.listFiles(path, fileType);
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("path", FileUtils.formatPath(path));
			counts[i] = files.size();
			item.put("filecount", counts[i]);
			data.add(item);
			
		}
		result.put("details", data);
		result.put("cnts", Arrays.toString(counts));
		
		return result;
	}
	
	public void saveFile(int diskId, String[] filePath, String fileType) {
		Thread t = new Thread(() -> {
			for (String path : filePath) {
				Folder folder = fileMapper.getFolder(diskId, path);
				if (folder == null) {
					folder = new Folder(diskId, path);
					List<FileModel> fms = FileUtils.scanFile(path, fileType);
					if (fms.size() > 0) {
						fileMapper.createFolder(folder);
						for (FileModel fm : fms) {
							fm.setFolder(folder);
							fileMapper.createFile(fm);
						}
					}
					log.info(String.format("Save Folder: %s, files: %d", folder, fms.size()));
				}
			}
		});
		t.start();
	}
	
	public void saveFileAtOnce(int diskId, String[] filePath, String fileType) {
		for (String path : filePath) {
			Folder folder = fileMapper.getFolder(diskId, path);
			if (folder == null) {
				folder = new Folder(diskId, path);
				List<FileModel> fms = FileUtils.scanFile(path, fileType);
				if (fms.size() > 0) {
					fileMapper.createFolder(folder);
					for (FileModel fm : fms) {
						fm.setFolder(folder);
						fileMapper.createFile(fm);
					}
				}
				log.info(String.format("Save Folder: %s, files: %d", folder, fms.size()));
			}
		}
	}
	
	public DatatablesViewPage<FileModel> getFileByPage(int offset, int rows) {
		DatatablesViewPage<FileModel> data = new DatatablesViewPage<FileModel>();
		int status = FileState.PENDING.ordinal();
		data.setAaData(fileMapper.getFileByPage(status, offset, rows));
		int total = fileMapper.countFile(status);
		data.setRecordsTotal(total);
		data.setRecordsFiltered(total);
		return data;
	}
	
	public List<FileModel> getAllPendingFile() {
		int status = FileState.PENDING.ordinal();
		int total = fileMapper.countFile(status);
		return fileMapper.getFileByPage(status, 0, total);
	}

	public void updateFileWebcount(int fileId, int webcount) {
		fileMapper.updateFileWebcount(fileId, webcount);
	}

	public void updateFileStatus(int fileId, int status) {
		fileMapper.updateFileStatus(fileId, status);
	}
}
