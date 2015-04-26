package com.redpois0n.gitj.tasks;

import com.redpois0n.git.CommitOption;
import com.redpois0n.git.Repository;

public class CommitTask extends Task {

	private String message;
	private CommitOption mode;
	
	public CommitTask(Repository repo, String message, CommitOption mode) {
		super("Committing...", repo);
		this.message = message;
		this.mode = mode;
	}

	@Override
	public void execute() throws Exception {
		repo.commit(message, mode);
	}

}
