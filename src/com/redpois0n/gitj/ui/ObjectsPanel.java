package com.redpois0n.gitj.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;

@SuppressWarnings("serial")
public class ObjectsPanel extends JScrollPane {

	private JTree tree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;

	public ObjectsPanel(MainFrame parent) {
		root = new DefaultMutableTreeNode("root");
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		tree.setCellRenderer(new Renderer());
		setViewportView(tree);
	}

	public void reload(Repository repo) throws Exception {
		root.removeAllChildren();
		
		DefaultMutableTreeNode tagsNode = new DefaultMutableTreeNode("Tags");
		treeModel.insertNodeInto(tagsNode, root, 0);
		
		for (Tag tag : repo.getTags()) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(tag.getTag());
			
			treeModel.insertNodeInto(node, tagsNode, 0);
		}
		
		tree.expandRow(0);
	}

	public class Renderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			return label;
		}

	}
}
