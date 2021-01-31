package com.apiejemplo.restapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TASK")
public class Task {
	
	@Id
	@Column(name="title", unique=true, nullable = false, length=45)
	private String title;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="username", nullable = false)
	private UserDB user;
	
	@Column(name = "description", nullable = false, length = 120)
	private String description;
	
	@Column(name = "ended", nullable = false)
	private boolean ended;

	public Task() {
		
	}
	
	public Task(String title, UserDB user, String description, boolean ended) {
		super();
		this.title = title;
		this.user = user;
		this.description = description;
		this.ended = ended;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UserDB getUser() {
		return user;
	}

	public void setUserName(UserDB user) {
		this.user = user;
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
	
	

}
