package com.redpois0n.git;

import java.util.List;

public class Commit {
	
	private Repository repo;
	private String hash;
	private String authorName;
	private String authorEmail;
	private String when;
	private String comment;
	private List<Diff> diffs;
	
	public Commit(Repository repo, String raw) {
		this(repo, raw.split(";"));
	}
	
	public Commit(Repository repo, String[] args) {
		this(repo, args[0], args[1], args[2], args[3], args[4]);
	}
	
	public Commit(Repository repo, String hash, String authorName, String authorEmail, String when, String comment) {
		this.repo = repo;
		this.hash = hash;
		this.authorName = authorName;
		this.authorEmail = authorEmail;
		this.when = when;
		this.comment = comment;
		
		if (this.comment.endsWith("---")) {
			this.comment = comment.substring(0, comment.length() - 3);
		}
	}
	
	public void addDiff(Diff diff) {
		diffs.add(diff);
	}
	
	public List<Diff> getDiffs() {
		if (diffs == null) {
			diffs = repo.getDiffs(this);
		}
		return diffs;
	}

	public String getDisplayAuthor() {
		return getAuthorName() + " <" + getAuthorEmail() + ">";
	}

	public String getDisplayHash() {
		return getHash().substring(0, 6);
	}
	
	public Repository getRepository() {
		return repo;
	}

	public String getHash() {
		return hash;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public String getWhen() {
		return when;
	}

	public String getComment() {
		return comment;
	}

}
