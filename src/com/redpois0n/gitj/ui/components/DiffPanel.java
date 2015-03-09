package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
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
				if (line.getType() == CodeLine.Type.ADDED) {
					g.setColor(Color.green);
				} else if (line.getType() == CodeLine.Type.REMOVED) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.white);
				}
								
				g.fillRect(0, y, prefWidth, metrics.getHeight() + 2);
				
				g.setColor(Color.black);
				g.drawString(line.getLine(), 0, y + metrics.getHeight());
				
				y += metrics.getHeight() + 2;
			}
		}
		
		g.drawRect(0, 0, getWidth(), getHeight());
		
		Dimension d = new Dimension(prefWidth + 1, prefHeight + 1);
		super.setSize(d);
		super.setPreferredSize(d);
	}
}
