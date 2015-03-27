package com.redpois0n.gitj.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.utils.IconUtils;

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
		
		TagTreeNode tagsNode = new TagTreeNode("Tags");
		treeModel.insertNodeInto(tagsNode, root, 0);
		
		for (Tag tag : repo.getTags()) {
			TagTreeNode node = new TagTreeNode(tag.getTag());
			
			treeModel.insertNodeInto(node, tagsNode, 0);
		}
		
		RemoteTreeNode remotesNode = new RemoteTreeNode("Remotes");
		treeModel.insertNodeInto(remotesNode, root, 1);
		
		for (Remote remote : repo.getRemotes()) {
			RemoteTreeNode node = new RemoteTreeNode(remote.getName());
			
			treeModel.insertNodeInto(node, remotesNode, 0);
		}
		
		tree.expandRow(0);
	}

	public class Renderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (value instanceof IconTreeNode) {
				label.setIcon(((IconTreeNode) value).getIcon());
			}
			
			return label;
		}
	}
	
	public abstract class IconTreeNode extends DefaultMutableTreeNode {
		
		private ImageIcon icon;
		
		public IconTreeNode(String text, ImageIcon icon) {
			super(text);
			this.icon = icon;
		}
		
		public ImageIcon getIcon() {
			return this.icon;
		}
	}
	
	public class TagTreeNode extends IconTreeNode {
		
		public TagTreeNode(String text) {
			super(text, IconUtils.getIcon("tag-annotated"));
		}
		
	}
	
	public class RemoteTreeNode extends IconTreeNode {
		
		public RemoteTreeNode(String text) {
			super(text, IconUtils.getIcon("remote"));
		}
	}
}
