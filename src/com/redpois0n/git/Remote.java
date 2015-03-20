package com.redpois0n.git;

public class Remote {
	
	private Repository repo;
	private String name;
	private String path;
	
	public Remote(Repository repo, String name, String path) {
		this.repo = repo;
		this.name = name;
		this.path = path;
	}
	
	public Repository getRepository() {
		return this.repo;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPath() {
		return this.path;
	}

}
