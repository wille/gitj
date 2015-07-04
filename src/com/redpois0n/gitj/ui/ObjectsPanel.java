package com.redpois0n.gitj.ui;

import iconlib.IconUtils;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.redpois0n.git.Branch;
import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Stash;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.Main;

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
		root = new DefaultMutableTreeNode("root");
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		tree.addMouseListener(new MouseAdapter() {

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
			
		});
		setViewportView(tree);
	}

	public void reload(MainPanel panel, Repository repo) throws Exception {
		this.panel = panel;
		this.repo = repo;	
		root.removeAllChildren();
		
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
		
		BranchTreeNode branchNode = new BranchTreeNode("Branches", null);
		treeModel.insertNodeInto(branchNode, root, 0);
		
		for (Branch branch : repo.getBranches()) {
			BranchTreeNode node = new BranchTreeNode(branch.getName(), branch);
			
			treeModel.insertNodeInto(node, branchNode, 0);
		}
		
		StashTreeNode stashNode = new StashTreeNode("Stashes", null);
		treeModel.insertNodeInto(stashNode, root, 0);
		
		for (Stash stash : repo.getStashes()) {
			StashTreeNode node = new StashTreeNode(stash.getName(), stash);
			
			treeModel.insertNodeInto(node, stashNode, 0);
		}
		
		treeModel.reload(root);

		tree.setRootVisible(false);
	}

	public class Renderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (value instanceof IconTreeNode) {
				label.setIcon(((IconTreeNode) value).getIcon());
			}
			
			label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
			
			try {
				if (value instanceof BranchTreeNode) {	
					BranchTreeNode node = ((BranchTreeNode) value);
					if (node.getBranch() != null && node.getBranch().equals(repo.getCurrentBranch())) {
						label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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
					if (tag != null) {
						panel.getCommitPanel().setSelectedCommit(repo.getCommit(tag.getHash()));
					}
				}	
			});
		}
		
	}
	
	public class BranchTreeNode extends IconTreeNode {
		
		private Branch branch;
		
		public BranchTreeNode(String text, final Branch branch) {
			super(text, IconUtils.getIcon("branch"), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (branch != null) {
						try {
							repo.checkout(branch);
						} catch (Exception e1) {
							e1.printStackTrace();
							Main.displayError(e1);
						}
					}
				}
			});
			
			this.branch = branch;
		}
		
		public Branch getBranch() {
			return this.branch;
		}
	}
	
	public class RemoteTreeNode extends IconTreeNode {
		
		public RemoteTreeNode(String text) {
			super(text, IconUtils.getIcon("remote"));
		}
	}
	
	public class StashTreeNode extends IconTreeNode {
		
		private Stash stash;
		
		public StashTreeNode(String text, final Stash stash) {
			super(text, IconUtils.getIcon("stash"), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (stash != null) {
						try {

						} catch (Exception ex) {
							ex.printStackTrace();
							Main.displayError(ex);
						}
					}
				}
			});
			
			this.stash = stash;
		}
		
		public Stash getStash() {
			return this.stash;
		}
	}
}
