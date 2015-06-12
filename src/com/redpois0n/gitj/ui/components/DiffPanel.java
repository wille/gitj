package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.redpois0n.git.Chunk;
import com.redpois0n.git.CodeLine;
import com.redpois0n.git.Diff;
import com.redpois0n.gitj.utils.GitIconUtils;

@SuppressWarnings("serial")
public class DiffPanel extends JPanel {
	
	public static final Color COLOR_ADDED = new Color(212, 234, 205);
	public static final Color COLOR_REMOVED = new Color(240, 214, 214);
	public static final Color COLOR_PANEL = new Color(237, 237, 237);
	public static final Color COLOR_PANEL_BORDER = new Color(204, 204, 204);
	
	private Diff diff;
	
	private boolean hasChecked;
	private BufferedImage image;
	
	private int prefWidth;
	private int prefHeight;
	
	public DiffPanel(Diff diff) {
		this.diff = diff;
	}

	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		boolean isImage = diff.isImage();
		
		if (isImage && image == null && !hasChecked) {
			try {
				image = ImageIO.read(new ByteArrayInputStream(diff.getData(false)));
				if (image == null) {
					isImage = false;
				}
				
				hasChecked = true;
			} catch (Exception e) {
				e.printStackTrace();
				isImage = false;
			}
		}
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		
		int textHeight = metrics.getHeight();
		
		if (prefWidth == 0 || prefHeight == 0) {
			prefHeight = 25;
			
			if (diff.getChunks().size() == 0) {
				prefHeight += 10;
			}
			
			for (Chunk chunk : diff.getChunks()) {
				prefHeight += 6 + textHeight;
				for (CodeLine line : chunk.getLines()) {
					int sw = metrics.stringWidth(line.getFixedLine()) + 80;
					
					if (sw > prefWidth) {
						prefWidth = sw;
					}
					
					prefHeight += textHeight + 2;
				}
			}
			
			if (metrics.stringWidth(diff.getLocalPath()) + 80 > prefWidth) {
				prefWidth = metrics.stringWidth(diff.getLocalPath()) + 80;
			}
			
			if (isImage) {
				if (image.getWidth() + 80 > prefWidth) {
					prefWidth = image.getWidth() + 80;
				}
				
				prefHeight += image.getHeight();
			}
		}
		
		// Top diff file table
		g.setColor(COLOR_PANEL);
		g.fillRect(0, 0, prefWidth, 25);
		g.setColor(COLOR_PANEL_BORDER);
		g.drawRect(0, 0, prefWidth, 25);
		
		g.setColor(Color.gray);
		g.drawString(diff.getLocalPath(), 20, 4 + textHeight);
		
		g.drawImage(GitIconUtils.getIconFromDiffType(diff.getType()), 2, 5, null);
		
		// Left line number table
		g.setColor(COLOR_PANEL);
		g.fillRect(0, 25, 60, getHeight() - 35);
		g.setColor(COLOR_PANEL_BORDER);
		g.drawRect(0, 25, 60, getHeight() - 35);
						
		int y = 25;
		
		List<Chunk> chunks = diff.getChunks();
		
		for (Chunk chunk : chunks) {			
			g.setColor(Color.black);
			g.drawString(chunk.getName(), 63, y + textHeight);
			g.setColor(COLOR_PANEL_BORDER);
			g.drawRect(60, y, prefWidth - 60, 5 + textHeight);

			y += 6 + textHeight;
			
			int startLine = chunk.getStartLine();
			int startRemovedLine = chunk.getStartRemovedLine();
			
			int startY = y;
			
			for (CodeLine line : chunk.getLines()) {		
				g.setColor(Color.gray);
				
				if (line.getType() == CodeLine.Type.ADDED) {
					g.drawString(startLine++ + "", 60 - metrics.stringWidth(startLine + "") - 5, y + textHeight);
				} else if (line.getType() == CodeLine.Type.REMOVED) {
					g.drawString(startRemovedLine++ + "", 5, y + textHeight);
				} else {					
					g.drawString(startLine++ + "", 60 - metrics.stringWidth(startLine + "") - 5, y + textHeight);
					g.drawString(startRemovedLine++ + "", 5, y + textHeight);
				}
								
				if (line.getType() == CodeLine.Type.ADDED) {
					g.setColor(COLOR_ADDED);
				} else if (line.getType() == CodeLine.Type.REMOVED) {
					g.setColor(COLOR_REMOVED);
				} else {
					g.setColor(Color.white);
				}			
								
				g.fillRect(61, y, prefWidth - 61, textHeight + 2);
								
				y += textHeight + 2;
			}
			
			y = startY;
			
			for (CodeLine line : chunk.getLines()) {		
				if (line.getType() == CodeLine.Type.ADDED) {
					g.setColor(COLOR_ADDED.darker());
					g.drawString("+", 67 - (metrics.stringWidth("+") / 2), y + textHeight - 1);
				} else if (line.getType() == CodeLine.Type.REMOVED) {
					g.setColor(COLOR_REMOVED.darker());
					g.drawString("-", 67 - (metrics.stringWidth("-") / 2), y + textHeight - 1);
				}
				
				g.setColor(Color.black);
				g.drawString(line.getFixedLine(), 80, y + textHeight);						
				
				y += textHeight + 2;
			}
		}
		
		if (isImage) {
			g.drawImage(image, prefWidth / 2, y + 5, null);
		}
		
		g.setColor(COLOR_PANEL_BORDER);
		g.drawRect(0, 0, prefWidth - 1, getHeight() - 10);
		
		g.setColor(Color.white);
		g.fillRect(0, getHeight() - 9, prefWidth, 9);

		if (getWidth() != prefWidth || getHeight() != prefHeight) {
			super.setPreferredSize(getDimension());
		}
	}
	
	public Dimension getDimension() {
		return new Dimension(prefWidth, prefHeight + 10);
	}
	
	public int getPrefWidth() {
		return prefWidth;
	}
	
	public int getPrefHeight() {
		return prefHeight;
	}
}
