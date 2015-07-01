package com.redpois0n.gitj.ui.graph;

import com.redpois0n.git.Commit;

public class GraphEntry {

	private String graphData;
	private Commit commit;
	
	public GraphEntry(String graphData, Commit c) {
		this.graphData = graphData;
		this.commit = c;
	}
	
}
