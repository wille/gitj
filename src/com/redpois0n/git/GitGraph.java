package com.redpois0n.git;

import java.util.ArrayList;
import java.util.List;

public class GitGraph {
	
	private List<GraphEntry> list = new ArrayList<GraphEntry>();
	
	public void add(GraphEntry entry) {
		this.list.add(entry);
	}

}
