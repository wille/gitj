package com.redpois0n.gitj.ui.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redpois0n.git.Commit;

public class GitGraph {
	
	public static final Color DEFAULT_COLOR = Color.black;
	
	public static final Map<String, Color> COLORS = new HashMap<String, Color>();
	
	static {
		COLORS.put("[30m", Color.black);
		COLORS.put("[31m", Color.red.darker());
		COLORS.put("[32m", Color.green.darker());
		COLORS.put("[33m", Color.yellow.darker());
		COLORS.put("[34m", Color.blue);
		COLORS.put("[35m", Color.magenta.darker());
		COLORS.put("[36m", Color.cyan.darker());
		COLORS.put("[37m", Color.white);
	}
	
	public static final String ANSI_RESET = "\u001B[0m";

	private int depth;
	private List<GraphEntry> list = new ArrayList<GraphEntry>();
	private Color[] latestColor;
	
	public void add(GraphEntry entry) {
		this.list.add(entry);
	}
	
	public GraphEntry get(Commit c) {
		for (GraphEntry entry : list) {
			if (entry.getCommit() != null && entry.getCommit().equals(c)) {
				return entry;
			}
		}
		
		return null;
	}
	
	/**
	 * Updates maximum branch depth for this graph
	 * @param depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
		this.latestColor = new Color[depth];
	}
	
	public Color[] getLatestColors() {
		return this.latestColor;
	}
	
	public GraphEntry getPrevious(GraphEntry entry) {
		int index = list.indexOf(entry);
		
		if (index < 1) {
			return null;
		}
		
		return list.get(index - 1);
	}
	
	public GraphEntry getNext(GraphEntry entry) {
		int index = list.indexOf(entry);
		
		if (index < 0 || index + 1 >= list.size()) {
			return null;
		}
		
		return list.get(index + 1);
	}
}
