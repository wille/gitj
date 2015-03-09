package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.components.CommitClickedListener;
import com.redpois0n.gitj.ui.components.DiffHolderPanel;
import com.redpois0n.gitj.ui.components.DiffPanel;
import com.redpois0n.gitj.ui.components.JCommitPane;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private JFrame parent;
	private Repository repository;
	private JSplitPane splitPaneMain;
	private JSplitPane splitPaneLow;
	private JCommitPane jcommitPane;
	private DiffHolderPanel diffHolderPanel;
	private JScrollPane scrollPaneDiffs;
	
	/**
	 * Repository tab
	 * @param repository
	 * @throws Exception
	 */
	public MainPanel(JFrame parent, Repository repository) throws Exception {	
		this.parent = parent;
		this.repository = repository;
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		
		splitPaneMain = new JSplitPane();
		splitPaneMain.setResizeWeight(0.5);
		splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		add(splitPaneMain, BorderLayout.CENTER);
		
		jcommitPane = new JCommitPane(repository.getCommits(true));
		jcommitPane.addListener(new CommitChangeListener());
		
		splitPaneMain.setLeftComponent(jcommitPane);
		
		splitPaneLow = new JSplitPane();
		splitPaneLow.setResizeWeight(0.5);
		
		splitPaneMain.setRightComponent(splitPaneLow);
		
		diffHolderPanel = new DiffHolderPanel();
		scrollPaneDiffs = new JScrollPane();
		scrollPaneDiffs.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneDiffs.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneDiffs.setViewportView(diffHolderPanel);
		
		splitPaneLow.setRightComponent(scrollPaneDiffs);
	}
	
	public void reload() throws Exception {
		jcommitPane.reload(repository.getCommits(true));
	}
	
	public Repository getRepository() {
		return this.repository;
	}
	
	public void loadDiffs(List<Diff> diffs) {
		for (Diff diff : diffs) {		
			diffHolderPanel.addDiffPanel(new DiffPanel(diff));
		}
				
		diffHolderPanel.revalidate();
	}
	
	public class CommitChangeListener implements CommitClickedListener {
		@Override
		public void onClick(Commit c) {
			diffHolderPanel.clear();
			loadDiffs(c.getDiffs());
		}
	}

}
