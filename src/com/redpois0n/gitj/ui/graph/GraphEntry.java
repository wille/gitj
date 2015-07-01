package com.redpois0n.gitj.ui.graph;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.redpois0n.git.Commit;

public class GraphEntry {

	private String graphData;
	private Commit commit;
	
	public GraphEntry(String graphData, Commit c) {
		this.graphData = graphData;
		this.commit = c;
	}
	
	public BufferedImage render(int height) {
		BufferedImage image = new BufferedImage(100, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		
		
		return image;
	}
	
	public ImageIcon renderIcon(int height) {
		return new ImageIcon(render(height));
	}
	
	public Commit getCommit() {
		return this.commit;
	}
	
}
