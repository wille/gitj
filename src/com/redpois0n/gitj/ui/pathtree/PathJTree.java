package com.redpois0n.gitj.ui.pathtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class PathJTree extends JTree {
	
	private static final List<PathListener> listeners = new ArrayList<PathListener>();
	
	private String delimiter;

	public PathJTree() {
		this(new PathTreeModel(new PathTreeNode("root", null)), "/");
	}
	
	public PathJTree(TreeModel model, String delimiter) {
		super(model);
		super.setShowsRootHandles(true);
		super.setCellRenderer(new PathTreeRenderer());
		super.addMouseListener(new ClickListener());
		this.delimiter = delimiter;
	}
	
	public PathTreeModel getRealModel() {
		return (PathTreeModel) super.getModel();
	}
	
	public PathTreeNode getSelectedNode() {
		return getSelectionPath().getLastPathComponent() == null ? null : (PathTreeNode) getSelectionPath().getLastPathComponent();
	}
	
	public void addPathListener(PathListener l) {
		listeners.add(l);
	}
	
	public void remotePathListener(PathListener l) {
		listeners.remove(l);
	}
	
	public String getDelimiter() {
		return this.delimiter;
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public class ClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		    TreePath tp = PathJTree.this.getPathForLocation(e.getX(), e.getY());

		    if (tp != null) {
		    	String path = PathJTree.this.makePath(tp);
		    	
		    	for (PathListener l : listeners) {
		    		l.pathSelected(path);
		    	}
		    }
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	public String makePath(TreePath p) {
		String path = "";
		
		for (Object obj : p.getPath()) {
			if (obj instanceof PathTreeNode && obj != getRealModel().getRootNode()) {
				PathTreeNode node = (PathTreeNode) obj;
				path += node.toString() + delimiter;
			}
		}
		
		if (path.length() > 0) {
			path = path.substring(0, path.length() - 1);
		}
		
		return path;
	}
	
	public TreeNode getNodeFromPath(String path) {		
		for (int i = 0; i < super.getRowCount(); i++) {
			TreePath treePath = super.getPathForRow(i);
			String mpath = makePath(treePath);

			if (mpath.equalsIgnoreCase(path)) {
				return (TreeNode) treePath.getLastPathComponent();
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean exists(String s) {
	    Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode) getRealModel().getRootNode()).depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = e.nextElement();
	        String path = makePath(new TreePath(node.getPath()));
	        if (s.equalsIgnoreCase(path)) {
	            return true;
	        }
	    }
	    return false;
	}
}
