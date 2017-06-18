package com.nlp.model;

import com.nlp.model.States.FileState;
import com.nlp.util.JSONUtils;

public class FileModel {
	/**
	 * 文件唯一编号
	 */
	private int fileId;
	/**
	 * 文件目录
	 */
	private Folder folder;
	/**
	 * 文件名
	 */
	private String filename;
	/**
	 * 文件字节数
	 */
	private long bytesize;
	/**
	 * 网页个数
	 */
	private int webcount;
	/**
	 * 文件状态
	 */
	private int status;
	
	public FileModel() {
		this.status = FileState.PENDING.ordinal();
	}
	public FileModel(int fileId, Folder folder, String filename, long bytesize, int webCount, int status) {
		this.fileId = fileId;
		this.folder = folder;
		this.filename = filename;
		this.bytesize = bytesize;
		this.webcount = webCount;
		this.status = status;
	}
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public Folder getFolder() {
		return folder;
	}
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getBytesize() {
		return bytesize;
	}
	public void setBytesize(long bytesize) {
		this.bytesize = bytesize;
	}
	public int getWebcount() {
		return webcount;
	}
	public void setWebcount(int webcount) {
		this.webcount = webcount;
	}
	public int getStatus() {
		return status;
	}
	public String getStatusString() {
		return FileState.values()[status].name();
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setStatus(FileState status) {
		this.status = status.ordinal();
	}
	public String getAbsolutePath() {
		return String.format("%s/%s", folder.getFolderPath(), filename);
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
