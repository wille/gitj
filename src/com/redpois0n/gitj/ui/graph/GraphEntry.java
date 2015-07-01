package com.redpois0n.gitj.ui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.redpois0n.git.Commit;

public class GraphEntry {
	
	public static final int BALL_DIAMETER = 8;
	public static final int SPACE = 10;
	
	public static final Color[] COLORS = new Color[] { Color.blue, Color.red, Color.green, Color.magenta };

	private String graphData;
	private Commit commit;
	
	public GraphEntry(String graphData, Commit c) {
		this.graphData = graphData;
		this.commit = c;
	}
	
	public BufferedImage render(int height) {
		BufferedImage image = new BufferedImage(100, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		
		int location = SPACE;
		
		for (int i = 0; i < graphData.length(); i++) {
			char c = graphData.charAt(i);
			
			g.setColor(COLORS[i]);
			
			if (c == '*') {
				g.fillOval(location - BALL_DIAMETER / 2, height / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_DIAMETER);
			}
			
			if (c == '*' || c == '|') {
				g.drawLine(location, 0, location, height);
			} else if (c == '/') {
				g.drawLine(location, 0, location + 5, height);
			} else if (c == '\\') {
				g.drawLine(location - 5, 0, location, height);
			}
			
			location += SPACE;
		}
		
		
		return image;
	}
	
	public ImageIcon renderIcon(int height) {
		return new ImageIcon(render(height));
	}
	
	public Commit getCommit() {
		return this.commit;
	}
	
}
