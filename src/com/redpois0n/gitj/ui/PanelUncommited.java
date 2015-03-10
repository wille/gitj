package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class PanelUncommited extends AbstractPanel {

	public PanelUncommited(Repository repo) {
		super(repo);
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		
		add(splitPane);
	}

}
