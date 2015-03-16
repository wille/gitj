package com.redpois0n.gitj.ui;

import com.redpois0n.git.Repository;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class CommitPanel extends AbstractPanel {
	
	private PanelUncommited panelList;

	public CommitPanel(Repository repo) {
		super(repo);
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		add(splitPane, BorderLayout.CENTER);
		
		JSplitPane topSplitPane = new JSplitPane();
		topSplitPane.setResizeWeight(0.5);
		panelList = new PanelUncommited(repo);
		topSplitPane.setRightComponent(panelList);
		
		splitPane.setLeftComponent(topSplitPane);
	}
	
	public PanelUncommited getListPanel() {
		return panelList;
	}

}
