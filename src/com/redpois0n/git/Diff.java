package com.redpois0n.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gitj.utils.FileUtils;

public class Diff {
	
	public static enum Type {
		NEW, DELETED, EDITED;
	}
	
	public static enum DataType {
		BINARY, TEXT;
	}
	
	private Commit parent;
	private File file;
	private List<Chunk> chunks = new ArrayList<Chunk>();
	private Type type;
	private DataType datatype = DataType.TEXT;
	private byte[] data;
	
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
	
	public void setDataType(DataType type) {
		this.datatype = type;
	}
	
	public DataType getDataType() {
		return datatype;
	}
	
	public byte[] getData(boolean cache) throws Exception {
		String[] cmd = new String[] { "git", "show", parent.getHash() + ":" + getLocalPath() };
		
		if (cache && data == null) {
			data = parent.getRepository().readBinary(cmd);
		} else if (!cache) {
			return parent.getRepository().readBinary(cmd);
		}
		
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean isBinary() {
		return datatype == DataType.BINARY;
	}
	
	public boolean isImage() {
		String path = getLocalPath();
		
		if (path == null) {
			return false;
		}
		
		path = path.toLowerCase();
		
		return path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif");
	}
}
