package com.apiejemplo.restapi.model;

public class TaskModel {


	private String title;
	private String userId;
	private String description;
	private boolean ended;
	
	public TaskModel() {
		
	}
		
	public TaskModel(String title, String userId, String description, boolean ended) {
		super();
		this.title = title;
		this.userId = userId;
		this.description = description;
		this.ended = ended;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEnded() {
		return ended;
	}
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	@Override
	public String toString() {
		return "TaskModel [title=" + title + ", userId=" + userId + ", description=" + description + ", ended=" + ended
				+ "]";
	}
	
	
	
	
}
