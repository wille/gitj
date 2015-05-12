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

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.utils.StatusUtils;

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
		statusLabel.setIcon(null);
		for (JLabel label : statusLabels) {
			toolBar.remove(label);
		}
		
		statusLabels = StatusUtils.getIcons(repo);
		
		for (JLabel label : statusLabels) {
			toolBar.add(label, BorderLayout.EAST);
		}
		
		toolBar.revalidate();
	}

	public void setUpdating() {
		statusLabel.setIcon(IconUtils.getIcon("reload-gray"));
	}

}
