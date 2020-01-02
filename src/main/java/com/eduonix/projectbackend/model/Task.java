package com.eduonix.projectbackend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasks")
public class Task {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="task")
	private String task;
	
	@Column(name="holderName")
	private String holderName;

	public Long getId() {
		return id;
	}

	public Task() {
		super();
	}

	public Task(String task, String holderName) {
		super();
		this.task = task;
		this.holderName = holderName;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	
	
	
	

}
