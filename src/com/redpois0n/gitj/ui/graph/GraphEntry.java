package com.redpois0n.gitj.ui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.redpois0n.git.Commit;

public class GraphEntry {
	
	public static final Color[] COLORS = new Color[] { Color.blue, Color.red, Color.green, Color.magenta };

	private String graphData;
	private Commit commit;
	
	public GraphEntry(String graphData, Commit c) {
		this.graphData = graphData;
		this.commit = c;
	}
	
	public BufferedImage render(int height) {
		BufferedImage image = new BufferedImage(100, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 100, height);
		
		int location = 5;
		
		for (int i = 0; i < graphData.length(); i++) {
			char c = graphData.charAt(i);
			
			g.setColor(COLORS[i]);
			
			if (c == '*' || c == '|') {
				g.drawLine(location, 0, location, height);
			} else if (c == '/') {
				
			} else if (c == '\\') {
					
			}
			
			location += 5;
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
