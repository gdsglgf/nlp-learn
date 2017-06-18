package com.nlp.model;

public class States {
	/**
	 * PENDING 待处理<br>
	 * PUBLISHED 已发布<br>
	 * PROCESSING 正在处理<br>
	 * SOLVED 已处理
	 */
	public enum FileState {PENDING, PUBLISHED, PROCESSING, SOLVED};
	
	/**
	 * PENDING 待处理<br>
	 * PROCESSING 正在处理<br>
	 * SOLVED 已处理
	 */
	public enum TaskState {PENDING, PROCESSING, SOLVED};
}
