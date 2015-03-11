package com.redpois0n.git;

public class Tag {
	
	private String tag;
	private String message;
	
	public Tag(String tag) {
		this.tag = tag;
		this.message = null;
	}
	
	public Tag(String tag, String message) {
		this.tag = tag;
		this.message = message;
	}
	
	public boolean isAnnotated() {
		return this.message != null;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public String getMessage() {
		return this.message;
	}

}
