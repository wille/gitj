package com.redpois0n.git;

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
	
	public Type getType() {
		return type;
	}

	public String getLine() {
		return line;
	}

	public String getFixedLine() {
		return getLine().replace("\t", "                  ");
	}
}
