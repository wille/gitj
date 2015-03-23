package com.redpois0n.gitj.ui.pathtree;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class PathTreeRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		if (value instanceof PathTreeNode) {
			PathTreeNode node = (PathTreeNode) value;
			
			label.setIcon(node.getIcon());
			
			String text = node.getText();
			
			if (text.contains(File.separator)) {
				String name = text;
				
				if (name.endsWith(".")) {
					name = name.substring(0, name.length() - 1);
				}
				
				if (name.endsWith(File.separator)) {
					name = name.substring(0, name.length() - 1);
				}
				
				name = name.substring(name.lastIndexOf(File.separator) + 1, name.length());
				
				label.setText(name);
			}
		}
		
		return label;
	}
}
