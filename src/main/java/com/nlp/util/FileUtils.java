package com.nlp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import com.nlp.model.FileModel;

public class FileUtils {
	/**
	 * 扫描特定目录特定类型的文件
	 * @param path 目录路径
	 * @param fileType 文件类型
	 * @return 文件列表
	 */
	public static List<File> listFiles(String path, String fileType) {
		List<File> resultFile = new ArrayList<File>();
		if (path != null && fileType != null && fileType.trim().length() > 0) {
			File f = new File(path);
			if (f.isDirectory()) {
				File[] files = f.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(fileType);
					}
				});
				resultFile = Arrays.asList(files);
			}
		}
		return resultFile;
	}

	/**
	 * 文件数据转换
	 * @param files 文件列表
	 * @return 数据库记录列表
	 */
	private static List<FileModel> convert(List<File> files) {
		List<FileModel> fms = new ArrayList<FileModel>();
		for (File f : files) {
			FileModel m = new FileModel();
			m.setFilename(f.getName());
			m.setBytesize(f.length());
			fms.add(m);
		}
		return fms;
	}

	/**
	 * 扫描特定目录特定类型的文件
	 * @param folders 目录路径数组
	 * @param fileType 文件类型
	 * @return 文件列表
	 */
	public static List<List<FileModel>> scanFile(String[] folders, String fileType) {
		List<List<FileModel>> result = new ArrayList<List<FileModel>>();
		if(folders != null) {
			for (String f : folders) {
				result.add(convert(listFiles(f, fileType)));
			}
		}
		return result;
	}
	
	/**
	 * 扫描特定目录特定类型的文件
	 * @param folders 目录路径数组
	 * @param fileType 文件类型
	 * @return 文件列表
	 */
	public static List<FileModel> scanFile(String filePath, String fileType) {
		return convert(listFiles(filePath, fileType));
	}
	
	/**
	 * 判断指定路径是否是文件夹
	 * @param path 文件路径
	 * @return
	 */
	public static boolean isDirectory(String path) {
		boolean flag = false;
		if (path != null) {
			File f = new File(path);
			flag = f.isDirectory();
		}
		return flag;
	}
	
	/**
	 * 批量判断指定路径是否是文件夹
	 * @param paths 文件路径数组
	 * @return
	 */
	public static boolean[] checkAllDirectory(String[] paths) {
		if (paths == null) {
			return null;
		}
		boolean[] flags = new boolean[paths.length];
		for (int i = 0; i < paths.length; i++) {
			flags[i] = isDirectory(paths[i]);
		}
		return flags;
	}
	
	public static String formatPath(String path) {
		path = path.replace("\\", "\\\\");
		// \b  \t  \n  \f  \r  \"  \'  \\
		path = path.replace("\b", "\\b");
		path = path.replace("\t", "\\t");
		path = path.replace("\n", "\\n");
		path = path.replace("\f", "\\f");
		path = path.replace("\r", "\\r");
		return path;
	}
	
	/**
	 * 读文件到字符串
	 * @param path 文件路径名
	 * @return 文件内容字符串
	 */
	public static String fileToString(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取压缩文件的BufferedReader
	 * @param fileIn  文件路径名
	 * @return
	 */
	public static BufferedReader getBufferedReaderForCompressedFile(String fileIn) {
		BufferedReader br2 = null;
		try {
			FileInputStream fin = new FileInputStream(fileIn);
			BufferedInputStream bis = new BufferedInputStream(fin);
			CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
			br2 = new BufferedReader(new InputStreamReader(input));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return br2;
	}
}
