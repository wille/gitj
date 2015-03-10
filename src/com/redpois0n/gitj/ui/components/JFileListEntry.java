package com.redpois0n.gitj.ui.components;

import javax.swing.ImageIcon;

public class JFileListEntry {
	
	private String text;
	private ImageIcon icon;
	
	public JFileListEntry(String text, ImageIcon icon) {
		this.text = text;
		this.icon = icon;
	}
	
	public String getText() {
		return this.text;
	}
	
	public ImageIcon getIcon() {
		return this.icon;
	}

}
