package com.redpois0n.gitj.ui.pathtree;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class PathTreeNode extends DefaultMutableTreeNode {
	
	private String text;
	private Icon icon;
	
	public PathTreeNode(String s, Icon icon) {
		super(s);
		this.text = s;
		this.icon = icon;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Icon getIcon() {
		return this.icon;
	}
}
