package com.nlp.model;

import java.util.Date;
import java.util.List;

import com.nlp.model.States.TaskState;
import com.nlp.util.JSONUtils;

public class Task {
	private int taskId;
	private Date pubTime;
	private Date lastModified;
	private Date endTime;
	private int status;
	private List<TaskItem> items;
	public Task() {
		pubTime = new Date();
		lastModified = pubTime;
		status = TaskState.PENDING.ordinal();
	}
	public Task(int taskId, Date endTime, int status) {
		this.taskId = taskId;
		this.endTime = endTime;
		this.status = status;
	}
	public Task(int taskId, Date pubTime, Date lastModified, Date endTime, int status, List<TaskItem> items) {
		this.taskId = taskId;
		this.pubTime = pubTime;
		this.lastModified = lastModified;
		this.endTime = endTime;
		this.status = status;
		this.items = items;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<TaskItem> getItems() {
		return items;
	}
	public void setItems(List<TaskItem> items) {
		this.items = items;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
