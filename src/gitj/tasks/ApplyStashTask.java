package gitj.tasks;

import git.Repository;
import git.Stash;

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
