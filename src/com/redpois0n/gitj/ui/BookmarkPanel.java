package com.redpois0n.gitj.ui;

import iconlib.IconUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class BookmarkPanel extends JPanel {
	
	private Repository repo;
	private boolean focused;
	
	public BookmarkPanel(Repository repo) {
		this.repo = repo;
		
		setPreferredSize(new Dimension(250, 50));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (focused) {
			g.setColor(new Color(191, 205, 219));
		} else {
			g.setColor(Color.white);
		}
		
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (focused) {
			g.setColor(Color.white);
		} else {
			g.setColor(Color.black);
		}

		g.drawImage(IconUtils.getIcon("icon").getImage(), 5, 5, null);
	}

	public void setFocused(boolean b) {
		this.focused = b;
	}

}
