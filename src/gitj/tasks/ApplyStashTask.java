package gitj.tasks;

import com.redpois0n.git.Repository;
import com.redpois0n.git.Stash;

public class ApplyStashTask extends Task {

	private Stash stash;
	
	public ApplyStashTask(Repository repo, Stash stash) {
		super("Applying stash...", repo);
		this.stash = stash;
	}

	@Override
	public void execute() throws Exception {
		repo.applyStash(stash);
	}

}
