package com.redpois0n.gitj.ui.graph;

import java.util.ArrayList;
import java.util.List;

import com.redpois0n.git.Commit;

public class GitGraph {
	
	private List<GraphEntry> list = new ArrayList<GraphEntry>();
	
	public void add(GraphEntry entry) {
		this.list.add(entry);
	}
	
	public GraphEntry get(Commit c) {
		for (GraphEntry entry : list) {
			if (entry.getCommit().equals(c)) {
				return entry;
			}
		}
		
		return null;
	}

}
