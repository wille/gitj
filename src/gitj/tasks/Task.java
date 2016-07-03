package gitj.tasks;

import com.redpois0n.git.Repository;

public abstract class Task {
	
	protected String text;
	protected Repository repo;
	
	public Task(String text, Repository repo) {
		this.text = text;
		this.repo = repo;
	}
	
	public abstract void execute() throws Exception;

	public String getText() {
		return text;
	}

}
