package com.redpois0n.git;

public class Tag {
	
	public static enum Type {
		ANNOTATED, LIGHTWEIGHT;
	}
	
	private String hash;
	private String tag;
	private String message;
	private String tagger;
	private String date;
	private Type type;
	
	public Tag(String hash, String tag) {
		this.hash = hash;
		this.tag = tag;
		this.type = Type.LIGHTWEIGHT;
	}
	
	public Tag(String hash, String tag, String message, String tagger, String date) {
		this.hash = hash;
		this.tag = tag;
		this.message = message;
		this.tagger = tagger;
		this.date = date;
		this.type = Type.ANNOTATED;
	}
	
	public boolean isAnnotated() {
		return this.type == Type.ANNOTATED;
	}
	
	public String getHash() {
		return this.hash;
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
	
	public Type getType() {
		return this.type;
	}

}
