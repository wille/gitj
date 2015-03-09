package com.redpois0n.gitj.ui.components;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.redpois0n.gitj.git.Diff;

@SuppressWarnings("serial")
public class DiffPanel extends JPanel {
	
	private Diff diff;
	
	private DiffPanel(Diff diff) {
		this.diff = diff;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawString(diff.getFile().getAbsolutePath(), 0, 0);
		
		
	}
}
