package com.redpois0n.gitj.git;

public class CodeLine {
	
	public static enum Type {
		NORMAL, ADDED, REMOVED;
	}
	
	private Type type;
	private String line;
	
	public CodeLine(Type type, String line) {
		this.type = type;
		this.line = line;
	}

}
