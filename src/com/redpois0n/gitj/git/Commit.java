package com.redpois0n.gitj.git;

public class Commit {
	
	public Commit(String raw) {
		this(raw.split(";"));
	}
	
	public Commit(String[] args) {
		this(args[0], args[1], args[2], args[3], args[4]);
	}
	
	public Commit(String hash, String authorName, String authorEmail, String when, String comment) {
		
	}

}
