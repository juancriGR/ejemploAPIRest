package com.apiejemplo.restapi.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiejemplo.restapi.model.TaskModel;
import com.apiejemplo.restapi.service.TaskService;
import com.apiejemplo.restapi.util.ConfigMessageProperties;
import com.apiejemplo.restapi.util.JsonUtils;


@RestController
@RequestMapping("/apiejemplo")
public class TaskController {
	
	
	private static final Log LOG = LogFactory.getLog(TaskController.class);
	
	
	@Autowired
	TaskService taskService;
	
	
	@Autowired
	private ConfigMessageProperties configMessageProperties;
		
	
	/**
	 * Save task.
	 *
	 * @param listTask the list task
	 * @return the response entity
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PostMapping("/savetask")
	public ResponseEntity<Map<String, Object>> saveTask(@RequestBody List<TaskModel> listTask) {
		LOG.info("METHOD: saveTask()"+ " --PARAMS : "+listTask.toString());
		Map<String, Object> jsonBody = new LinkedHashMap<>();
		ResponseEntity<Map<String, Object>> response;
		
		if(!listTask.isEmpty() && validateDataCreate(listTask)) {
			for (TaskModel taskModel : listTask) {
				taskModel.setUserId(getUserFromAuth());
				taskService.createTask(taskModel);
			}
			JsonUtils.generateResponseBody(jsonBody,configMessageProperties.getConfigValue("task.generated.ok"), null,
					"/apiejemplo/savetask", null);
			response = ResponseEntity.ok(jsonBody);
		}else {
			LOG.warn("METHOD: saveTask() ->" + configMessageProperties.getConfigValue("error.field.required"));
			JsonUtils.generateResponseBody(jsonBody, null, configMessageProperties.getConfigValue("error.field.required"),
					"/apiejemplo/savetask", null);
			response = ResponseEntity.badRequest().body(jsonBody);
		}
		return response;
	}
	
	/**
	 * Finish task.
	 *
	 * @param taskModel the task model
	 * @return the response entity
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PutMapping("/finishtask")
	public ResponseEntity<Map<String, Object>> finisTask(@RequestBody TaskModel taskModel) {
		LOG.info("METHOD: finisTask()"+ " --PARAMS : "+taskModel.toString());
		Map<String, Object> jsonBody = new LinkedHashMap<>();
		ResponseEntity<Map<String, Object>> response;
		if(validateFinishTask(taskModel)) {
			boolean updated = taskService.updateTask(taskModel);
			if(updated) {
				JsonUtils.generateResponseBody(jsonBody,configMessageProperties.getConfigValue("task.updated.ok"), null,
						"/apiejemplo/finishtask", null);
				response = ResponseEntity.ok(jsonBody);
			}else {
				LOG.warn("METHOD: finisTask() ->" + configMessageProperties.getConfigValue("task.updated.ko"));
				JsonUtils.generateResponseBody(jsonBody, null, configMessageProperties.getConfigValue("task.updated.ko"),
						"/apiejemplo/finishtask", null);
				response = ResponseEntity.badRequest().body(jsonBody);
			}
		}else {
			LOG.warn("METHOD: finisTask() ->" + configMessageProperties.getConfigValue("error.field.required"));
			JsonUtils.generateResponseBody(jsonBody, null, configMessageProperties.getConfigValue("error.field.required"),
					"/apiejemplo/finishtask", null);
			response = ResponseEntity.badRequest().body(jsonBody);
		}
		return response;
		
	}
	
	/**
	 * Delete task.
	 *
	 * @param taskModel the task model
	 * @return the response entity
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DeleteMapping("/deletetask")
	public ResponseEntity<Map<String, Object>> deleteTask(@RequestBody TaskModel taskModel) {

		LOG.info("METHOD: deleteTask()" + " --PARAMS : " + taskModel.getTitle());
		Map<String, Object> jsonBody = new LinkedHashMap<>();
		ResponseEntity<Map<String, Object>> response;

		if (!taskModel.getTitle().isEmpty() && taskModel.getTitle() != null) {
			boolean deleted = taskService.deleteTask(taskModel);
			if(deleted) {
				JsonUtils.generateResponseBody(jsonBody, configMessageProperties.getConfigValue("task.deleted.ok"), null,
						"/apiejemplo/deletetask", null);
				response = ResponseEntity.ok(jsonBody);
			}else {
				JsonUtils.generateResponseBody(jsonBody, taskModel.getTitle(), configMessageProperties.getConfigValue("task.deleted.ko"),
						"/apiejemplo/deletetask", null);
				response = ResponseEntity.badRequest().body(jsonBody);
			}
			
		} else {
			LOG.warn("METHOD: deleteTask() ->" + configMessageProperties.getConfigValue("error.field.required"));
			JsonUtils.generateResponseBody(jsonBody, taskModel, configMessageProperties.getConfigValue("error.field.required"),
					"/apiejemplo/deletetask", null);
			response = ResponseEntity.badRequest().body(jsonBody);
		}
		return response;
	}
	
	/**
	 * Show task.
	 *
	 * @return the response entity
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/showtask")
	public ResponseEntity<Map<String, Object>> showTask() {
		
		LOG.info("METHOD: showTask()");
		Map<String, Object> jsonBody = new LinkedHashMap<>();
		ResponseEntity<Map<String, Object>> response;
		
		
		List<TaskModel> listeTask = taskService.showAllTask(getUserFromAuth());
		if(!listeTask.isEmpty()) {
			JsonUtils.generateResponseBody(jsonBody, listeTask, null,
					"/mimamcom/showtask", null);
			response = ResponseEntity.ok(jsonBody);
		}else {
			LOG.warn("METHOD: showTask() ->" + configMessageProperties.getConfigValue("task.show.empty"));
			JsonUtils.generateResponseBody(jsonBody, null, configMessageProperties.getConfigValue("task.show.empty"),
					"/mimamcom/showtask", null);
			response = ResponseEntity.badRequest().body(jsonBody);
		}
		return  response;
	}
	
	
	
	/**
	 * Validate data create.
	 *
	 * @param listTask the list task
	 * @return true, if successful
	 */
	private boolean validateDataCreate(List<TaskModel> listTask) {
		boolean isValide = true;
		for(TaskModel taskModel: listTask) {
			if(taskModel.getTitle() == null || taskModel.getDescription() == null) {
				isValide = false;
			}
		}
		
		return isValide;
	}
	
	/**
	 * Validate finish task.
	 *
	 * @param taskModel the task model
	 * @return true, if successful
	 */
	private boolean validateFinishTask(TaskModel taskModel) {
		boolean isValide = true;
		if(taskModel.getTitle() == null || !taskModel.isEnded()) {
				isValide = false;
			}
		
		return isValide;
	}
	
	
	/**
	 * Gets the user from auth.
	 *
	 * @return the user from auth
	 */
	private String getUserFromAuth() {
		return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	

}
