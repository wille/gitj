package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class DiffHolderPanel extends JPanel {
	
	public DiffHolderPanel() {
		super.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		setBackground(Color.white);
	}
	
	public void addDiffPanel(final DiffPanel panel) {
		GridBagConstraints grid = new GridBagConstraints();
		grid.gridx = 0;
		grid.gridy = getComponentCount();
		grid.anchor = GridBagConstraints.WEST;
		add(panel, grid);
		
		Runnable r = new Runnable() {
			public void run() {
				int width = 0;
				int height = 0;

				for (Component c : getComponents()) {
					if (c instanceof DiffPanel) {
						DiffPanel dp = (DiffPanel) c;
													
						height += dp.getPrefHeight();
													
						if (dp.getPrefWidth() > width) {
							width = (int) dp.getPrefWidth();
						}
					}
				}
				Dimension d = new Dimension(width, height);
				
				panel.setSize(d);
			}
		};
		SwingUtilities.invokeLater(r);

		repaint();		
	}
	
	public void clear() {
		for (Component c : super.getComponents()) {
			remove(c);
		}
	}

}
