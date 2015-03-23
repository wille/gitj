package com.redpois0n.gitj.ui.pathtree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class PathTreeModel extends DefaultTreeModel {
	
	private TreeNode rootNode;
	
	public PathTreeModel(TreeNode root) {
		super(root);
		this.rootNode = root;
	}

	public void addRoot(DefaultMutableTreeNode root) {
		super.insertNodeInto(root, (DefaultMutableTreeNode) rootNode, 0);
	}
	
	public void removeRoot(DefaultMutableTreeNode root) {
		super.removeNodeFromParent(root);
	}
	
	public TreeNode getRootNode() {
		return rootNode;
	}

}
