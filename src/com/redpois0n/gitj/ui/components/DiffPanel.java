package com.redpois0n.gitj.ui.components;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.redpois0n.git.Chunk;
import com.redpois0n.git.CodeLine;
import com.redpois0n.git.Diff;

@SuppressWarnings("serial")
public class DiffPanel extends JPanel {
	
	private Diff diff;
	
	public DiffPanel(Diff diff) {
		this.diff = diff;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int prefWidth = 0;
		int prefHeight = 0;
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		
		for (Chunk chunk : diff.getChunks()) {
			prefHeight += 10;
			for (CodeLine line : chunk.getLines()) {
				int sw = metrics.stringWidth(line.getLine());
				
				if (sw > prefWidth) {
					prefWidth = sw;
				}
				
				prefHeight += metrics.getHeight() + 2;
			}
		}
		
		int y = 0;
		
		for (Chunk chunk : diff.getChunks()) {
			y += 10;
			for (CodeLine line : chunk.getLines()) {		
				y += metrics.getHeight() + 2;
				
				g.drawString(line.getLine(), 5, y);
			}
		}
		
		Dimension d = new Dimension(prefWidth, prefHeight);
		super.setSize(d);
		super.setPreferredSize(d);
		
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

	}
}
