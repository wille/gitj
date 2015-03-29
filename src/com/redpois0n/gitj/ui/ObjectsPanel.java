package com.redpois0n.gitj.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class ObjectsPanel extends JScrollPane {

	private MainPanel panel;
	private Repository repo;
	private JTree tree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;

	public ObjectsPanel(MainFrame parent) {
		tree = new JTree();
		tree.setCellRenderer(new Renderer());
		tree.setShowsRootHandles(true);
		tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());

		        if (tp != null && tp.getLastPathComponent() != null) {
		            IconTreeNode node = (IconTreeNode) tp.getLastPathComponent();
		            
		            if (node.getListener() != null) {
		            	node.getListener().actionPerformed(new ActionEvent(node, 0, null));
		            }
		        }
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
		});
		setViewportView(tree);
	}

	public void reload(MainPanel panel, Repository repo) throws Exception {
		this.panel = panel;
		this.repo = repo;
		root = new DefaultMutableTreeNode("root");
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		
		TagTreeNode tagsNode = new TagTreeNode("Tags", null);
		treeModel.insertNodeInto(tagsNode, root, 0);
		
		for (Tag tag : repo.getTags()) {
			TagTreeNode node = new TagTreeNode(tag.getTag(), tag);
			
			treeModel.insertNodeInto(node, tagsNode, 0);
		}
		
		RemoteTreeNode remotesNode = new RemoteTreeNode("Remotes");
		treeModel.insertNodeInto(remotesNode, root, 0);
		
		for (Remote remote : repo.getRemotes()) {
			RemoteTreeNode node = new RemoteTreeNode(remote.getName());
			
			treeModel.insertNodeInto(node, remotesNode, 0);
		}
		
		tree.expandRow(0);
		tree.setRootVisible(false);
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
		private ActionListener listener;
		
		public IconTreeNode(String text, ImageIcon icon, ActionListener listener) {
			super(text);
			this.icon = icon;
			this.listener = listener;
		}
		
		public IconTreeNode(String text, ImageIcon icon) {
			this(text, icon, null);
		}
		
		public ImageIcon getIcon() {
			return this.icon;
		}
		
		public ActionListener getListener() {
			return this.listener;
		}
	}
	
	public class TagTreeNode extends IconTreeNode {
		
		public TagTreeNode(String text, final Tag tag) {
			super(text, IconUtils.getIcon("tag-annotated"), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					panel.getCommitPanel().setSelectedCommit(repo.getCommit(tag.getHash()));
				}	
			});
		}
		
	}
	
	public class RemoteTreeNode extends IconTreeNode {
		
		public RemoteTreeNode(String text) {
			super(text, IconUtils.getIcon("remote"));
		}
	}
}
