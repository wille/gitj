package com.redpois0n.git;

public class Tag {
	
	public static enum Type {
		ANNOTATED, LIGHTWEIGHT;
	}
	
	private String tag;
	private String message;
	private String tagger;
	private String date;
	private Type type;
	
	public Tag(String tag) {
		this.tag = tag;
		this.type = Type.LIGHTWEIGHT;
	}
	
	public Tag(String tag, String message, String tagger, String date) {
		this.tag = tag;
		this.message = message;
		this.tagger = tagger;
		this.date = date;
		this.type = Type.ANNOTATED;
	}
	
	public boolean isAnnotated() {
		return this.type == Type.ANNOTATED;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getTagger() {
		return this.tagger;
	}
	
	public String getDate() {
		return this.date;
	}

}
