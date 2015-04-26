package com.redpois0n.gitj.tasks;

import com.redpois0n.git.Branch;
import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;

public class PullTask extends Task {

	private Branch branch;
	private Remote remote;
	
	public PullTask(Repository repo, Branch branch, Remote remote) {
		super("Pulling...", repo);
		this.branch = branch;
		this.remote = remote;
	}

	@Override
	public void execute() throws Exception {
		repo.pull(remote, branch);
	}

}
