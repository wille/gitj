package com.redpois0n.gitj.git;

import java.util.ArrayList;
import java.util.List;

public class Commit {
	
	private Repository repo;
	private String hash;
	private String authorName;
	private String authorEmail;
	private String when;
	private String comment;
	private List<Diff> diffs = new ArrayList<Diff>();
	
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
	}
	
	public void addDiff(Diff diff) {
		diffs.add(diff);
	}
	
	public List<Diff> getDiffs() {
		return diffs;
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
