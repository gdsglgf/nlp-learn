package com.nlp.model;

import com.nlp.util.JSONUtils;

public class TaskItem {
	private int itemId;
	private int taskId;
	private FileModel file;
	public TaskItem() {
	}
	public TaskItem(int itemId, int taskId, FileModel file) {
		this.itemId = itemId;
		this.taskId = taskId;
		this.file = file;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public FileModel getFile() {
		return file;
	}
	public void setFile(FileModel file) {
		this.file = file;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
