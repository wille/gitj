package com.redpois0n.gitj.ui;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class ObjectsPanel extends JScrollPane {

	private JTree tagTree;
	private DefaultTreeModel tagTreeModel;
	private DefaultMutableTreeNode root;

	public ObjectsPanel(MainFrame parent) {
		root = new DefaultMutableTreeNode("root");
		tagTreeModel = new DefaultTreeModel(root);
		tagTree = new JTree(tagTreeModel);
		setViewportView(tagTree);
	}
	
	public void reload(Repository repo) {

	}
}
