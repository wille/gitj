package com.redpois0n.git;

public class Stash {

	private String stashName;
	private String name;

	public Stash(String stashName, String name) {
		this.stashName = stashName;
		this.name = name;
	}
	
	public String getStashName() {
		return this.stashName;
	}
	
	public String getName() {
		return this.name;
	}

}
