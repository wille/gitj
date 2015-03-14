package com.redpois0n.gitj.ui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DiffHolderPanel extends JPanel {
	
	public DiffHolderPanel() {		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
	}
	
	public void addDiffPanel(final DiffPanel panel) {
		GridBagConstraints grid = new GridBagConstraints();
		grid.gridx = 0;
		grid.gridy = getComponentCount();
		grid.anchor = GridBagConstraints.WEST;
		add(panel, grid);
		
		repaint();
		
		Runnable r = new Runnable() {
			public void run() {
				try {
					Thread.sleep(100L);
				} catch (Exception ex) { }
				
				int width = 0;
				int height = 0;
				
				do {
					for (Component c : getComponents()) {
						if (c instanceof DiffPanel) {
							DiffPanel dp = (DiffPanel) c;
														
							height += dp.getPrefHeight();
														
							if (dp.getPrefWidth() > width) {
								width = (int) dp.getPrefWidth();
							}
						}
					}
					
					try {
						Thread.sleep(100L);
					} catch (Exception ex) { }
				} while (width == 0 && height == 0);
	
				Dimension d = new Dimension(width, height);
				
				panel.setSize(d);
				//panel.setPreferredSize(d);
			}
		};
		
		new Thread(r).start();
	}
	
	public void clear() {
		for (Component c : super.getComponents()) {
			remove(c);
		}
	}

}
