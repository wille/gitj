package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class StatusBar extends JPanel {
	
	private JLabel statusLabel;
	private JProgressBar progressBar;
	
	public StatusBar() {
		setLayout(new BorderLayout(0, 0));
		
		statusLabel = new JLabel("Status");
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		progressBar.setIndeterminate(true);
		
		add(statusLabel, BorderLayout.WEST);
		add(progressBar, BorderLayout.EAST);
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

}
