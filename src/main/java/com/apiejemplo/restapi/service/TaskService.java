package com.apiejemplo.restapi.service;

import java.util.List;

import com.apiejemplo.restapi.model.TaskModel;

public interface TaskService {
	
	public abstract TaskModel createTask(TaskModel taskModel);
	
	public abstract List<TaskModel> showAllTask(String username);
	
	public abstract boolean updateTask(TaskModel taskModel);
	
	public abstract boolean deleteTask(TaskModel taskModel);

}
