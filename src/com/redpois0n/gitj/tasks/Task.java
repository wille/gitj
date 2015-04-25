package com.redpois0n.gitj.tasks;

import com.redpois0n.git.Repository;

public abstract class Task {
	
	protected Repository repo;
	
	public Task(Repository repo) {
		this.repo = repo;
	}
	
	public abstract void execute() throws Exception;

}
