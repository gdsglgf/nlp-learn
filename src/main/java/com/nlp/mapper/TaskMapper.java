package com.nlp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import com.nlp.model.FileModel;
import com.nlp.model.Task;
import com.nlp.model.TaskItem;

@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)
public interface TaskMapper {
	/**
	 * 添加抽取任务记录
	 * @param task 抽取任务
	 */
	public void createTask(Task task);
	/**
	 * 更新抽取任务记录
	 * @param task 抽取任务
	 */
	public void updateTask(Task task);
	/**
	 * 添加任务项记录
	 * @param item 任务项
	 */
	public void createTaskItem(TaskItem item);
	/**
	 * 批量添加任务项
	 * @param taskId 任务编号
	 * @param fileIds 文件编号列表
	 */
	public void createTaskItemBatch(@Param("taskId")int taskId, @Param("fileIds")int[] fileIds);
	/**
	 * 按照任务编号查找任务项列表
	 * @param taskId 任务编号
	 * @return
	 */
	public List<TaskItem> getItemListByTaskId(@Param("taskId") int taskId);
	
	public List<FileModel> getFileList(@Param("taskId") int taskId, @Param("status") int status);
	/**
	 * 按照任务编号查找
	 * @param taskId 任务编号
	 * @return
	 */
	public Task getTaskById(@Param("taskId") int taskId);
}
