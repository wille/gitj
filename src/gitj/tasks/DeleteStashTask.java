package gitj.tasks;

import git.Repository;
import git.Stash;

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
