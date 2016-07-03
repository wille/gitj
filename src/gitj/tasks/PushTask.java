package gitj.tasks;

import com.redpois0n.git.Branch;
import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;

public class PushTask extends Task {

	private Branch branch;
	private Remote remote;
	
	public PushTask(Repository repo, Branch branch, Remote remote) {
		super("Pushing...", repo);
		this.branch = branch;
		this.remote = remote;
	}

	@Override
	public void execute() throws Exception {
		repo.push(remote, branch);
	}

}
