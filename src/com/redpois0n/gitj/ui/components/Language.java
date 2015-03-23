package com.redpois0n.gitj.ui.components;

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

	public String getLanguage() {
		return language;
	}

	public Color getColor() {
		return color;
	}
	
	public List<String> getExtensions() {
		return extensions;
	}

	public int getFiles() {
		return files;
	}

	public int getLineCount() {
		return linecount;
	}

}
