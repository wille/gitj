package com.redpois0n.gitj.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class BookmarksPanel extends JPanel {
	
	private List<BookmarkPanel> panels = new ArrayList<BookmarkPanel>();
		
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
						//panel.setFocused(false); // TODO
					}
				}
				
				if (c != null && c instanceof BookmarkPanel) {
					BookmarkPanel panel = (BookmarkPanel) c;
					
					//panel.setFocused(true); // TODO
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
		panels.add(panel);
	}
	
	public BookmarkPanel getFromRepo(Repository repo) {
		for (BookmarkPanel panel : panels) {
			if (panel.getRepository().equals(repo)) {
				return panel;
			}
		}
		
		return null;
	}

	/**
	 * Tries to reload bookmark panel if it exists for a repo
	 * @param repo
	 */
	public void reload(Repository repo) {
		BookmarkPanel panel = getFromRepo(repo);
		
		if (panel != null) {
			panel.reload();
		}
	}

}
