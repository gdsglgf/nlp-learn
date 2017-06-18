package com.nlp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import com.nlp.model.FileModel;
import com.nlp.model.Folder;

@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)
public interface FileMapper {
	/**
	 * 添加文件夹
	 * @param folder 文件夹
	 */
	public void createFolder(Folder folder);
	/**
	 * 查找文件夹
	 * @param id 目录编号
	 * @return
	 */
	public Folder getFolderById(@Param("id") int id);
	public Folder getFolder(@Param("diskId") int diskId, @Param("path") String path);
	/**
	 * 添加数据文件
	 * @param file 数据文件
	 */
	public void createFile(FileModel file);
	/**
	 * 查找文件
	 * @param id 文件编号
	 * @return
	 */
	public FileModel getFileById(@Param("id") int id);
	/**
	 * 更新文件网页个数
	 * @param fileId 文件编号
	 * @param webcount 网页个数
	 */
	public void updateFileWebcount(@Param("id") int fileId, @Param("webcount") int webcount);
	/**
	 * 更新文件状态
	 * @param fileId 文件编号
	 * @param status 文件状态
	 */
	public void updateFileStatus(@Param("id") int fileId, @Param("status") int status);
	/**
	 * 导出文件统计信息（文件夹编号、目录路径、文件类型、文件个数）
	 * @return
	 */
//	public List<Map<String, Object>> folderReport();
	
	/**
	 * 统计文件个数
	 * @param status 文件状态
	 * @return
	 */
	public int countFile(@Param("status") int status);
	
	/**
	 * 分页查询数据文件
	 * @param status 文件状态
	 * @param offset 跳过记录条数
	 * @param limit 查询条数
	 * @return
	 */
	public List<FileModel> getFileByPage(@Param("status") int status, @Param("offset") int offset, @Param("rows") int rows);
}
