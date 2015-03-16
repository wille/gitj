package com.redpois0n.gitj.ui;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class CommitPanel extends AbstractPanel {
	
	private MainFrame parent;
	private PanelUncommited panelList;

	public CommitPanel(MainFrame parent, Repository repo) {
		super(repo);
		this.parent = parent;
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		add(splitPane, BorderLayout.CENTER);
		
		JSplitPane topSplitPane = new JSplitPane();
		topSplitPane.setResizeWeight(0.5);
		panelList = new PanelUncommited(repo);
		try {
			panelList.reload();
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
		topSplitPane.setLeftComponent(panelList);
		
		splitPane.setLeftComponent(topSplitPane);
	}
	
	public PanelUncommited getListPanel() {
		return panelList;
	}
	
	public void cancel() {
		parent.removePanel(this);
	}

}
