package gitj.tasks;

import git.Branch;
import git.Remote;
import git.Repository;

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
