package com.redpois0n.gitj;

import java.awt.Color;
import java.util.List;

public class Language {

	private String language;
	private Color color;
	private List<String> extensions;
	private int files;
	private int linecount;
	
	public Language(String language, Color color, List<String> extensions, int files, int linecount) {
		this.language = language;
		this.color = color;
		this.extensions = extensions;
		this.files = files;
		this.linecount = linecount;
	}
	
	public Language(String language, Color color, List<String> extensions) {
		this.language = language;
		this.color = color;
		this.extensions = extensions;
	}
	
	public Language(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public List<String> getExtensions() {
		return extensions;
	}
	
	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}

	public int getFiles() {
		return files;
	}

	public int getLineCount() {
		return linecount;
	}

	public void addLineCount(int i) {
		linecount += i;
	}

	public void incrementFiles() {
		files++;
	}

}
