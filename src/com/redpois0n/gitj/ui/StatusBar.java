package com.redpois0n.gitj.ui;

import iconlib.IconUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

import com.redpois0n.git.Change;
import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class StatusBar extends JPanel {
	
	private List<JLabel> statusLabels = new ArrayList<JLabel>();
	private JLabel statusLabel;
	private JProgressBar progressBar;
	private JToolBar toolBar;
	
	public StatusBar() {
		setLayout(new BorderLayout(0, 0));
		
		statusLabel = new JLabel("Status");
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		progressBar.setIndeterminate(true);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.CENTER);
		toolBar.add(statusLabel);
		toolBar.add(progressBar);
	}
	
	public void setError(String s) {
		statusLabel.setForeground(Color.red);
		statusLabel.setText(s);
	}
	
	public void setText(String s) {
		statusLabel.setForeground(Color.black);
		statusLabel.setText(s);
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void update(Repository repo) {
		for (JLabel label : statusLabels) {
			toolBar.remove(label);
		}
		
		statusLabels.clear();
		
		try {
			List<Change> changes = repo.parseStatus();

			int added = 0;
			int removed = 0;
			int modified = 0;
			int renamed = 0;
			int unknown = 0;
			
			for (Change change : changes) {
				Change.Type type = change.getFirstType();
					
				if (type == Change.Type.ADDED) {
					added++;
				} else if (type == Change.Type.UNSTAGED_DELETE) {
					removed++;
				} else if (type == Change.Type.STAGED_DELETE) {
					removed++;
				} else if (type == Change.Type.UNSTAGED_RENAME || type == Change.Type.STAGED_RENAME) {
					renamed++;
				} else if (type == Change.Type.UNSTAGED_COPY) {
					modified++;
				} else if (type == Change.Type.STAGED_COPY) {
					modified++;
				} else if (type == Change.Type.UNSTAGED_MODIFIED || type == Change.Type.STAGED_MODIFIED) {
					modified++;
				} else if (type == Change.Type.UNSTAGED) {
					unknown++;
				} else {
					removed++;
				}
			}
			
			if (added > 0) {
				statusLabels.add(new JLabel(added + "", IconUtils.getIcon("added"), 10));
			}
			
			if (removed > 0) {
				statusLabels.add(new JLabel(removed + "", IconUtils.getIcon("removed"), 10));
			}
			
			if (modified > 0) {
				statusLabels.add(new JLabel(modified + "", IconUtils.getIcon("modified"), 10));
			}
			
			if (renamed > 0) {
				statusLabels.add(new JLabel(renamed + "", IconUtils.getIcon("renamed"), 10));
			}
			
			if (unknown > 0) {
				statusLabels.add(new JLabel(unknown + "", IconUtils.getIcon("unknown"), 10));
			}
			
			for (JLabel label : statusLabels) {
				toolBar.add(label, BorderLayout.EAST);
			}
			
			toolBar.revalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
