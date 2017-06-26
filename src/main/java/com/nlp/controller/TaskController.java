package com.nlp.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nlp.dto.DatatablesViewPage;
import com.nlp.dto.PageDTO;
import com.nlp.dto.WebLinkDTO;
import com.nlp.message.DocFileMessageSender;
import com.nlp.message.QueueReporter;
import com.nlp.model.FileModel;
import com.nlp.model.States.FileState;
import com.nlp.service.FileService;
import com.nlp.service.TaskService;
import com.nlp.service.WebService;

@Controller
@RequestMapping(value = "/task")
public class TaskController {
	private static final Logger log = LogManager.getLogger(TaskController.class);
	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
	@Resource(name="docFileMessageSender")
	DocFileMessageSender sender;
	@Autowired
	private WebService webService;
	@Autowired
	private QueueReporter queueReporter;

	/**
	 * 显示上传文件目录页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView uploadView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("task/upload");
		return view;
	}
	
	/**
	 * 处理上传文件目录请求
	 * @param diskId
	 * @param fileType
	 * @param filePath
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upload.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> uploadAction(
			@RequestParam(value = "diskId", required = true) int diskId,
            @RequestParam(value = "fileType", required = true) String fileType,
            @RequestParam(value = "filePath[]", required = true) String[] filePath,
            HttpServletRequest request) {
		
		log.debug(String.format("diskId:%d, type:%s, paths:%s", 
				diskId, fileType, Arrays.toString(filePath)));
		
		Map<String, Object> result = fileService.scanFile(filePath, fileType);
		if ((boolean) result.get("isSuccessful")) {
			HttpSession session = request.getSession();
			session.setAttribute("isUpload", true);
			session.setAttribute("diskId", diskId);
			session.setAttribute("fileType", fileType);
			session.setAttribute("filePath", filePath);
			
			fileService.saveFile(diskId, filePath, fileType);
		}
		
		return result;
	}
	
	/**
	 * 显示目录上传结果页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload-details", method = RequestMethod.GET)
	public ModelAndView uploadDetailsView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = null;
		HttpSession session = request.getSession();
		Boolean isUpload = (Boolean) session.getAttribute("isUpload");
		if (isUpload != null && isUpload == true) {
			view = new ModelAndView("task/upload-details");
			String fileType = (String) session.getAttribute("fileType");
			String[] filePath = (String[]) session.getAttribute("filePath");
			
			Map<String, Object> result = fileService.folderDetails(filePath, fileType);
			view.addObject("details", result.get("details"));
			view.addObject("length", filePath.length);
			view.addObject("cnts", result.get("cnts"));
		} else {
			view = new ModelAndView("redirect:/task/upload");
		}
		
		return view;
	}
	
	/**
	 * 显示任务发布页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView publishView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("task/publish");
		view.addObject("records", "upload-details");
		view.addObject("forwardUrl", "task-details");
		return view;
	}
	
	/**
	 * 处理文件数据请求
	 * @param request
	 * @param draw
	 * @param offset
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/fileData.action", method = RequestMethod.POST)
	public @ResponseBody DatatablesViewPage<FileModel> fileDataAction(
			HttpServletRequest request,
			@RequestParam(value = "draw", defaultValue = "1", required = false) Integer draw,
			@RequestParam(value = "start", defaultValue = "0", required = false) Integer offset,
			@RequestParam(value = "length", defaultValue = "10", required = false) Integer rows) {
		
		DatatablesViewPage<FileModel> data = fileService.getFileByPage(offset, rows);
		data.setDraw(draw);
		
		return data;
	}
	
	/**
	 * 处理任务发布请求
	 * @param request
	 * @param fileIds
	 * @return
	 */
	@RequestMapping(value = "/publish.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> publishAction(
			HttpServletRequest request,
			@RequestParam(value = "fileIds[]", required = true) int[] fileIds) {
		
		log.debug(String.format("Task files: %s", Arrays.toString(fileIds)));
		
		Map<String, Object> result = new HashMap<String, Object>();
		for (int fileId : fileIds) {
			fileService.updateFileStatus(fileId, FileState.PUBLISHED.ordinal());
		}
		int taskId = taskService.createTask(fileIds);
		result.put("isSuccessful", taskId != 0);
		result.put("forwardUrl", String.format("task-details/%d", taskId));

		return result;
	}
	
	/**
	 * 显示已发布任务页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/task-details/{taskId}", method = RequestMethod.GET)
	public ModelAndView taskDetailsView(HttpServletRequest request,
			@PathVariable(value="taskId") Integer taskId) {
		ModelAndView view = null;
		if (taskId == null) {
			view = new ModelAndView("task/publish");
		} else {
			view = new ModelAndView("task/task-details");
			view.addObject("task", taskService.getTaskById(taskId));
		}
		
		return view;
	}
	
	@RequestMapping(value = "/running/{taskId}", method = RequestMethod.GET)
	public ModelAndView runningTaskView(@PathVariable(value="taskId") int taskId) {
		ModelAndView view = new ModelAndView("task/running");
		sender.execute(taskId);
		return view;
	}
	
	@RequestMapping(value = "/run-all", method = RequestMethod.GET)
	public ModelAndView runningAllView() {
		ModelAndView view = new ModelAndView("task/running");
		sender.execute();
		return view;
	}
	
	@RequestMapping(value = "/queue-report", method = RequestMethod.GET)
	public ModelAndView queueReportView() {
		ModelAndView view = new ModelAndView("task/running");
		return view;
	}
	
	@RequestMapping(value = "/queueData.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queueDataAction() {
		return queueReporter.getQueueData();
	}
	
	@RequestMapping(value = "/link-list", method = RequestMethod.GET)
	public ModelAndView linkDetailsView() {
		ModelAndView view = new ModelAndView("task/link-list");
		return view;
	}
	
	@RequestMapping(value = "/linkData.action", method = RequestMethod.POST)
	public @ResponseBody DatatablesViewPage<WebLinkDTO> linkDataAction(
			HttpServletRequest request, PageDTO dto) {
		log.debug(dto);
		DatatablesViewPage<WebLinkDTO> data = webService.getWebURLByPage(dto);
		return data;
	}
}
