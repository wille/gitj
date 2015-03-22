package com.redpois0n.git;

public enum CommitOption {
	
	NORMAL("Commit", "commit changes"),
	AMEND("Amend", "Amend latest commit");
	
	private String textual;
	private String desc;
	
	private CommitOption(String textual, String desc) {
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
