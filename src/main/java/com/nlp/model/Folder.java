package com.nlp.model;

import com.nlp.util.JSONUtils;

public class Folder {
	/**
	 * 文件目录编号
	 */
	private int folderId;
	/**
	 * 硬盘编号
	 */
	private int diskId;
	/**
	 * 文件目录路径
	 */
	private String folderPath;

	public Folder() {
	}
	
	public Folder(int diskId, String folderPath) {
		this.diskId = diskId;
		this.folderPath = folderPath;
	}

	public Folder(int folderId, int diskId, String folderPath) {
		this.folderId = folderId;
		this.diskId = diskId;
		this.folderPath = folderPath;
	}

	public int getFolderId() {
		return folderId;
	}

	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}

	public int getDiskId() {
		return diskId;
	}

	public void setDiskId(int diskId) {
		this.diskId = diskId;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
