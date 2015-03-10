package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import com.redpois0n.git.Chunk;
import com.redpois0n.git.CodeLine;
import com.redpois0n.git.Diff;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class DiffPanel extends JPanel {
	
	public static final Color COLOR_ADDED = new Color(212, 234, 205);
	public static final Color COLOR_REMOVED = new Color(240, 214, 214);
	public static final Color COLOR_PANEL = new Color(237, 237, 237);
	public static final Color COLOR_PANEL_BORDER = new Color(204, 204, 204);
	
	private Diff diff;
	
	private int prefWidth;
	private int prefHeight;
	
	public DiffPanel(Diff diff) {
		this.diff = diff;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		
		if (prefWidth == 0 || prefHeight == 0) {
			prefHeight += 16;
			
			if (diff.getChunks().size() == 0) {
				prefHeight += 10;
			}
			
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
			
			if (metrics.stringWidth(diff.getFile().getAbsolutePath()) + 25 > prefWidth) {
				prefWidth = metrics.stringWidth(diff.getFile().getAbsolutePath()) + 25;
			}
		}
		
		// Top diff file table
		g.setColor(COLOR_PANEL);
		g.fillRect(0, 0, prefWidth, 25);
		g.setColor(COLOR_PANEL_BORDER);
		g.drawRect(0, 0, prefWidth, 25);
		
		g.setColor(Color.gray);
		g.drawString(diff.getFile().getAbsolutePath(), 20, 4 + metrics.getHeight());
		
		g.drawImage(IconUtils.getIcon(diff.getType()), 2, 5, null);
		
		// Left line number table
		g.setColor(COLOR_PANEL);
		g.fillRect(0, 25, 20, prefHeight);
		g.setColor(COLOR_PANEL_BORDER);
		g.drawRect(0, 25, 20, prefHeight);
						
		int y = 20;
		
		List<Chunk> chunks = diff.getChunks();
		
		for (Chunk chunk : chunks) {
			y += 5;
			g.setColor(Color.black);
			g.drawString(chunk.getName(), 23, y + metrics.getHeight());
			g.setColor(COLOR_PANEL_BORDER);
			g.drawRect(20, y, prefWidth - 20, 5 + metrics.getHeight());

			y += 6 + metrics.getHeight();
			
			for (CodeLine line : chunk.getLines()) {		
				if (line.getType() == CodeLine.Type.ADDED) {
					g.setColor(COLOR_ADDED);
				} else if (line.getType() == CodeLine.Type.REMOVED) {
					g.setColor(COLOR_REMOVED);
				} else {
					g.setColor(Color.white);
				}
								
				g.fillRect(21, y, prefWidth, metrics.getHeight() + 2);
				
				g.setColor(Color.black);
				g.drawString(line.getFixedLine(), 23, y + metrics.getHeight());
				
				y += metrics.getHeight() + 2;
			}
		}
		
		g.setColor(COLOR_PANEL_BORDER);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
							
		super.setSize(getDimension());
		super.setPreferredSize(getDimension());
	}
	
	public Dimension getDimension() {
		return new Dimension(prefWidth, prefHeight);
	}
	
	public int getPrefWidth() {
		return prefWidth;
	}
	
	public int getPrefHeight() {
		return prefHeight;
	}
}
