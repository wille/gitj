package com.redpois0n.gitj.tasks;

import com.redpois0n.git.Repository;

public class StashTask extends Task {

	private String message;
	boolean keepstaged;
	
	public StashTask(Repository repo, String message, boolean keepstaged) {
		super("Saving stash...", repo);
		this.message = message;
		this.keepstaged = keepstaged;
	}

	@Override
	public void execute() throws Exception {
		repo.stash(message, keepstaged);
	}

}
