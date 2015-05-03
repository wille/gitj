package com.redpois0n.gitj.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class BookmarksPanel extends JPanel {
	
	public BookmarksPanel() {
		super.setBorder(new EmptyBorder(0, 0, 0, 0));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		setBackground(Color.white);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Component c = BookmarksPanel.this.getComponentAt(e.getX(), e.getY());

				for (Component co : getComponents()) {
					if (co instanceof BookmarkPanel) {
						BookmarkPanel panel = (BookmarkPanel) co;
						panel.setFocused(false);
					}
				}
				
				if (c != null && c instanceof BookmarkPanel) {
					BookmarkPanel panel = (BookmarkPanel) c;
					
					panel.setFocused(true);
				}
				
				repaint();
			}
		});
	}
	
	public void addBookmarkPanel(BookmarkPanel panel) {
		GridBagConstraints grid = new GridBagConstraints();
		grid.gridx = 0;
		grid.gridy = getComponentCount();
		grid.anchor = GridBagConstraints.WEST;
		add(panel, grid);
	}

}
