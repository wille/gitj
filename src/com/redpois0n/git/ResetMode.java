package com.redpois0n.git;

public enum ResetMode {
	
	SOFT("soft", "keep all local changes"),
	MIXED("mixed", "keep working copy but reset index"),
	HARD("hard", "discard all working copy changes");
	
	private String textual;
	private String desc;
	
	private ResetMode(String textual, String desc) {
		this.textual = textual;
		this.desc = desc;
	}

	public String getTextual() {
		return textual;
	}
	
	public String getDescription() {
		return desc;
	}

}
