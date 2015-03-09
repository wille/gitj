package com.redpois0n.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Diff {
	
	private Commit parent;
	private File file;
	private List<Chunk> chunks = new ArrayList<Chunk>();
	
	public Diff(Commit parent, File file) {
		this.parent = parent;
		this.file = file;
	}
	
	public Diff(Commit parent, String file) {
		this(parent, new File(file));
	}
	
	public void addChunk(Chunk c) {
		chunks.add(c);
	}

	public List<Chunk> getChunks() {
		return chunks;
	}

	public Commit getParent() {
		return parent;
	}
	
	public File getFile() {
		return file;
	}
	
}
