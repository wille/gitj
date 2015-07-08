package com.redpois0n.gitj.tasks;

import com.redpois0n.git.Repository;
import com.redpois0n.git.Stash;

public class DeleteStashTask extends Task {

	private Stash stash;
	
	public DeleteStashTask(Repository repo, Stash stash) {
		super("Deleting stash...", repo);
		this.stash = stash;
	}

	@Override
	public void execute() throws Exception {
		repo.deleteStash(stash);
	}

}
