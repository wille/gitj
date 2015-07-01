package com.redpois0n.git;

public class GraphEntry {

	private String graphData;
	private Commit commit;
	
	public GraphEntry(String graphData, Commit c) {
		this.graphData = graphData;
		this.commit = c;
	}
	
}
