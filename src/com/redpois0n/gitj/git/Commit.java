package com.redpois0n.gitj.git;

public class Commit {
	
	public Commit(Repository repo, String raw) {
		this(repo, raw.split(";"));
	}
	
	public Commit(Repository repo, String[] args) {
		this(repo, args[0], args[1], args[2], args[3], args[4]);
	}
	
	public Commit(Repository repo, String hash, String authorName, String authorEmail, String when, String comment) {
		
	}

}
