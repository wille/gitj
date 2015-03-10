package com.redpois0n.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
		String path = file.getAbsolutePath();
		
		path.replace(parent.getRepository().getFolder().getAbsolutePath(), "").replace("\\", "/");
		
		if (path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		
		return path;
	}
	
}
