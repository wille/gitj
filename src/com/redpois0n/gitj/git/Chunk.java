package com.redpois0n.gitj.git;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
	
	private Diff parent;
	private String name;
	private List<CodeLine> codelines = new ArrayList<CodeLine>();
	
	public Chunk(Diff parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	public void addLine(CodeLine cl) {
		codelines.add(cl);
	}
	
	public void addLine(CodeLine.Type type, String s) {
		codelines.add(new CodeLine(type, s));
	}
	
	public void addRawLine(String s) {
		CodeLine.Type type;
		
		if (s.startsWith("+ ")) {
			type = CodeLine.Type.ADDED;
		} else if (s.startsWith("- ")) {
			type = CodeLine.Type.REMOVED;
		} else {
			type = CodeLine.Type.NORMAL;
		}
		
		if (s.length() > 2) {
			s = s.substring(2, s.length());
		}
		
		addLine(type, s);
	}
	
	public List<CodeLine> getLines() {
		return codelines;
	}

	public Diff getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}

}
