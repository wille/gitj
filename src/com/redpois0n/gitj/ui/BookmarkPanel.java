package com.redpois0n.gitj.ui;

import iconlib.IconUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class BookmarkPanel extends JPanel {
	
	private Repository repo;
	
	public BookmarkPanel(Repository repo) {
		this.repo = repo;
		
		setPreferredSize(new Dimension(250, 50));
		
		setLayout(new BorderLayout(0, 0));
		
		JToolBar b1 = new JToolBar();
		b1.setFloatable(false);
		add(b1, BorderLayout.NORTH);

		JToolBar b2 = new JToolBar();
		b2.setFloatable(false);
		add(b2, BorderLayout.SOUTH);
		
		JLabel lblName = new JLabel(repo.getName() + "  ");
		lblName.setIcon(IconUtils.getIcon("icon"));
		b1.add(lblName);
		
		JLabel lblPath = new JLabel(repo.getFolder().getAbsolutePath());
		lblPath.setForeground(Color.gray);
		b1.add(lblPath);
	}
}
