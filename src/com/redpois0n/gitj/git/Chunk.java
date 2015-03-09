package com.redpois0n.gitj.git;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
	
	private Diff parent;
	private List<CodeLine> codelines = new ArrayList<CodeLine>();
	
	public Chunk(Diff parent) {
		this.parent = parent;
	}
	
	public void addLine(CodeLine cl) {
		codelines.add(cl);
	}
	
	public void addLine(CodeLine.Type type, String s) {
		codelines.add(new CodeLine(type, s));
	}
	
	public List<CodeLine> getLines() {
		return codelines;
	}

	public Diff getParent() {
		return parent;
	}

}
