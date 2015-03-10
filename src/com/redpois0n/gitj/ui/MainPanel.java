package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.components.CommitClickedListener;
import com.redpois0n.gitj.ui.components.DiffHolderPanel;
import com.redpois0n.gitj.ui.components.DiffPanel;
import com.redpois0n.gitj.ui.components.JCommitPane;

@SuppressWarnings("serial")
public class MainPanel extends AbstractPanel {

	private JFrame parent;
	private JSplitPane splitPaneMain;
	private JSplitPane splitPaneLow;
	private JCommitPane jcommitPane;
	private DiffHolderPanel diffHolderPanel;
	private JScrollPane scrollPaneDiffs;
	private PanelSummary panelSummary;
	
	/**
	 * Repository tab
	 * @param repository
	 * @throws Exception
	 */
	public MainPanel(JFrame parent, Repository repository) throws Exception {	
		super(repository);
		this.parent = parent;
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		
		splitPaneMain = new JSplitPane();
		splitPaneMain.setResizeWeight(0.5);
		splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		add(splitPaneMain, BorderLayout.CENTER);
		
		jcommitPane = new JCommitPane(repository);
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
		
		panelSummary = new PanelSummary();
		splitPaneLow.setLeftComponent(panelSummary);
	}
	
	/**
	 * Clears everything visible and reloads commit panel
	 */
	@Override
	public void reload() throws Exception {
		clear();
		jcommitPane.reload(repo.getCommits(true));
	}
	
	/**
	 * Clears Diff and Commit panels
	 */
	public void clear() {
		jcommitPane.clear();
		diffHolderPanel.clear();
		panelSummary.clear();
	}
	
	/**
	 * Loads diffs (On lick
	 * @param diffs
	 */
	public void loadDiffs(Commit c, List<Diff> diffs) {
		splitPaneLow.setLeftComponent(panelSummary);

		diffHolderPanel.clear();
		
		for (Diff diff : diffs) {		
			diffHolderPanel.addDiffPanel(new DiffPanel(diff));
		}
				
		diffHolderPanel.revalidate();
		
		panelSummary.reload(c, diffs);
	}
	
	public void loadUncommited() throws Exception {
		PanelUncommited pu = new PanelUncommited(repo);
		pu.reload();
		splitPaneLow.setLeftComponent(pu);
	}
	
	public class CommitChangeListener implements CommitClickedListener {
		@Override
		public void onClick(Commit c) {
			try {
				if (c == null && repo.hasUnstagedFiles()) {
					loadUncommited();
				} else {
					loadDiffs(c, c.getDiffs());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Main.displayError(ex);
			}
		}
	}

}
