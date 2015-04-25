package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.components.DiffHolderPanel;
import com.redpois0n.gitj.ui.components.DiffPanel;
import com.redpois0n.gitj.ui.components.IDiffSelectionListener;

@SuppressWarnings("serial")
public class CommitPanel extends AbstractPanel {

	private MainFrame parent;
	private PanelUncommited panelList;
	private CommitButtonPanel buttonPanel;
	private MainPanel parentPanel;
	private DiffHolderPanel diffHolderPanel;
	private JSplitPane topSplitPane;
	private JSplitPane splitPane;

	public CommitPanel(MainFrame parent, MainPanel parentPanel, Repository repo) {
		super(repo);
		this.parent = parent;
		this.parentPanel = parentPanel;
		setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.75);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		add(splitPane, BorderLayout.CENTER);

		topSplitPane = new JSplitPane();
		topSplitPane.setResizeWeight(0.5);
		panelList = new PanelUncommited(this, repo);
		panelList.addListener(new DiffSelectionListener());
		reload();

		diffHolderPanel = new DiffHolderPanel();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(diffHolderPanel);
		topSplitPane.setRightComponent(scrollPane);

		topSplitPane.setLeftComponent(panelList);

		splitPane.setLeftComponent(topSplitPane);

		buttonPanel = new CommitButtonPanel(this);
		splitPane.setRightComponent(buttonPanel);
		
		loadDiffs(repo.getUncommitedDiffs());
	}

	public PanelUncommited getListPanel() {
		return panelList;
	}
	
	public void reloadDividers() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				topSplitPane.setDividerLocation(topSplitPane.getSize().width / 2);	
			}
		});
	}
	

	/**
	 * Closes current tab
	 */
	public void cancel() {
		parent.removePanel(this);
		reload();
	}

	@Override
	public void reload() {
		try {
			panelList.reload();
			parentPanel.reload();
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
		
		reloadDividers();
	}
	
	public MainFrame getParentFrame() {
		return parent;
	}

	public void loadDiffs(final List<Diff> diffs) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				diffHolderPanel.clear();

				for (Diff diff : diffs) {
					diffHolderPanel.addDiffPanel(new DiffPanel(diff));
				}
				
				diffHolderPanel.revalidate();
			}
		});
	}
	
	public class DiffSelectionListener implements IDiffSelectionListener {
		@Override
		public void onSelect(Commit c, List<Diff> d, List<Diff> all) {
			try {
				loadDiffs(d);
			} catch (Exception ex) {
				ex.printStackTrace();
				Main.displayError(ex);
			}
		}
	}

}
