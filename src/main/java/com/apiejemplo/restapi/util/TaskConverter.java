package com.apiejemplo.restapi.util;

import org.springframework.stereotype.Component;

import com.apiejemplo.restapi.entity.Task;
import com.apiejemplo.restapi.entity.UserDB;
import com.apiejemplo.restapi.model.TaskModel;


@Component("taskConverter")
public class TaskConverter {

	public TaskModel convertTask2TaskModel(Task task) {
		return new TaskModel(task.getTitle(), task.getUser().getUsername(), task.getDescription(),
				task.isEnded());
	}

	public Task convertTaskModel2Task(TaskModel taskModel) {
		return new Task(taskModel.getTitle(), new UserDB(taskModel.getUserId()), taskModel.getDescription(),
				taskModel.isEnded());
	}

}
