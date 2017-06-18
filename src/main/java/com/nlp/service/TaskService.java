package com.nlp.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlp.mapper.TaskMapper;
import com.nlp.model.FileModel;
import com.nlp.model.Task;
import com.nlp.model.TaskItem;

@Service
@Transactional
public class TaskService {
	private static final Logger log = LogManager.getLogger(TaskService.class);
	@Autowired
	private TaskMapper taskMapper;
	
	public int createTask(int[] fileIds) {
		Task task = new Task();
		taskMapper.createTask(task);
		int taskId = task.getTaskId();
		
		if (fileIds != null) {
			taskMapper.createTaskItemBatch(taskId, fileIds);
		}
		
		log.debug(String.format("Create Task: %s", task.toString()));
		
		return taskId;
	}

	public Task getTaskById(int taskId) {
		return taskMapper.getTaskById(taskId);
	}

	public List<TaskItem> getItemListByTaskId(int taskId) {
		return taskMapper.getItemListByTaskId(taskId);
	}
	
	public void updateTask(Task task) {
		taskMapper.updateTask(task);
	}
	
	public List<FileModel> getFileList(int taskId, int status) {
		return taskMapper.getFileList(taskId, status);
	}
}
