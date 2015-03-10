package com.redpois0n.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.redpois0n.gitj.utils.FileUtils;

public class Diff {
	
	public static enum Type {
		NEW, DELETED, EDITED;
	}
	
	private Commit parent;
	private File file;
	private List<Chunk> chunks = new ArrayList<Chunk>();
	private Type type;
	
	public Diff(Commit parent, File file, Type type) {
		this.parent = parent;
		this.file = file;
		this.type = type;
	}
	
	public Diff(Commit parent, String file, Type type) {
		this(parent, new File(file), type);
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
	
	public Type getType() {
		return type;
	}

	public String getLocalPath() {
		return FileUtils.getRepoPath(file, parent.getRepository());
	}
	
}
