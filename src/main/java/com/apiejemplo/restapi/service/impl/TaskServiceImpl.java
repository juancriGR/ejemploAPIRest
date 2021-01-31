package com.apiejemplo.restapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.apiejemplo.restapi.entity.Task;
import com.apiejemplo.restapi.model.TaskModel;
import com.apiejemplo.restapi.repository.TaskRepository;
import com.apiejemplo.restapi.service.TaskService;
import com.apiejemplo.restapi.util.TaskConverter;


@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService{
	
	@Autowired
	@Qualifier("taskRepository")
	TaskRepository taskRepository;
	
	@Autowired
	@Qualifier("taskConverter")
	TaskConverter converter;

	/**
	 * Creates the task.
	 *
	 * @param taskModel the task model
	 * @return the task model
	 */
	@Override
	public TaskModel createTask(TaskModel taskModel) {
		taskModel.setEnded(false);
		Task task = taskRepository.save(converter.convertTaskModel2Task(taskModel));
		return converter.convertTask2TaskModel(task);
	}

	/**
	 * Show all task.
	 *
	 * @param username the username
	 * @return the list
	 */
	@Override
	public List<TaskModel> showAllTask(String username) {
		List<Task> listeTask = taskRepository.findAllByUsername(username);
		return convertList2TaskModel(listeTask);
	}

	/**
	 * Update task.
	 *
	 * @param taskModel the task model
	 * @return true, if successful
	 */
	@Override
	public boolean updateTask(TaskModel taskModel) {
		Optional<Task> task = taskRepository.findById(taskModel.getTitle());
		if(task.isPresent()) {
			taskModel.setUserId(task.get().getUser().getUsername());
			taskModel.setDescription(task.get().getDescription());
			taskRepository.save(converter.convertTaskModel2Task(taskModel));
		}
		return task.isPresent();
	}

	/**
	 * Delete task.
	 *
	 * @param taskModel the task model
	 * @return true, if successful
	 */
	@Override
	public boolean deleteTask(TaskModel taskModel) {
		Optional<Task> task = taskRepository.findById(taskModel.getTitle());
		if(task.isPresent()) {
			taskRepository.deleteById(converter.convertTaskModel2Task(taskModel).getTitle());
		}
		
		return task.isPresent();
		
	}
	
	/**
	 * Convert list 2 task model.
	 *
	 * @param listTask the list task
	 * @return the list
	 */
	private List<TaskModel> convertList2TaskModel(List<Task> listTask){
		List<TaskModel> listeTaskModel = new ArrayList<TaskModel>();
		for (Task task : listTask) {
			listeTaskModel.add(converter.convertTask2TaskModel(task));
		}
		
		return listeTaskModel;
	}

}
