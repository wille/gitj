package com.redpois0n.gitj.ui;

import iconlib.IconUtils;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.LanguageScanner;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.components.DiffHolderPanel;
import com.redpois0n.gitj.ui.components.DiffPanel;
import com.redpois0n.gitj.ui.components.ICommitClickListener;
import com.redpois0n.gitj.ui.components.IDiffSelectionListener;
import com.redpois0n.gitj.ui.components.JFileListEntry;
import com.redpois0n.gitj.ui.components.LanguageBar;
import com.redpois0n.gitj.utils.GitIconUtils;

@SuppressWarnings("serial")
public class MainPanel extends AbstractPanel {

	private JFrame parent;
	private JSplitPane splitPaneMain;
	private JSplitPane splitPaneLow;
	private CommitListPanel jcommitPane;
	private DiffHolderPanel diffHolderPanel;
	private JScrollPane scrollPaneDiffs;
	private PanelSummary panelSummary;
	private LanguageBar langBar;
	private JPanel panelList;

	/**
	 * Repository tab
	 * 
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
		
		langBar = new LanguageBar();
		add(langBar, BorderLayout.NORTH);

		jcommitPane = new CommitListPanel(repository);
		jcommitPane.addListener(new CommitClickListener());

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
		panelSummary.addListener(new DiffSelectionListener());
		
		reloadLangBar();
	}
	
	public void reloadDividers() {
		splitPaneMain.setDividerLocation(splitPaneMain.getSize().height / 2);
		splitPaneLow.setDividerLocation(splitPaneLow.getSize().width / 2);
		panelSummary.reloadDividers();
	}

	/**
	 * Clears everything visible and reloads commit panel
	 * 
	 * @throws Exception
	 */
	@Override
	public void reload() throws Exception {
		clear();
		reloadCommits();
		reloadUncommited();	
		
		reloadDividers();
		
		reloadLangBar();
	}
	
	public void reloadLangBar() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					langBar.setLanguages(new LanguageScanner(repo).scan(false));
					langBar.repaint();
				} catch (Exception e) {
					e.printStackTrace();
					Main.displayError(e);
				}
			}
		}).start();
	}

	/**
	 * Reloads commit list
	 * 
	 * @throws Exception
	 */
	public void reloadCommits() throws Exception {
		List<Commit> commits = repo.getCommits(true);
		jcommitPane.clear();
		jcommitPane.reload(commits);
		
		reloadDividers();
	}

	/**
	 * Reloads staged and unstaged files
	 * 
	 * @throws Exception
	 */
	public void reloadUncommited() throws Exception {
		if (panelList instanceof PanelUncommited) {
			PanelUncommited panel = (PanelUncommited) panelList;

			panel.reload();
			
			reloadDividers();
		}
	}

	/**
	 * Clears Diff and Commit panels
	 */
	public void clear() {
		diffHolderPanel.clear();
		panelSummary.clear();
	}

	/**
	 * Loads diffs (On click on commit)
	 * 
	 * @param diffs
	 *            Diffs to load
	 * @param allDiffs
	 *            All diffs for current commit
	 * @param reloadDiffList
	 *            if we should clear and fill the JList containing the diffs
	 *            with data from diffs list
	 */
	public void loadDiffs(Commit c, final List<Diff> diffs, List<Diff> allDiffs, boolean reloadDiffList) {
		if (allDiffs != null) {
			splitPaneLow.setLeftComponent(panelSummary);
			panelList = panelSummary;
		} else {
			PanelUncommited pu = new PanelUncommited(this, repo);
			pu.addListener(new DiffSelectionListener());
			try {
				pu.reload();
			} catch (Exception e) {
				e.printStackTrace();
			}
			splitPaneLow.setLeftComponent(pu);
			panelList = pu;
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				diffHolderPanel.clear();
				diffHolderPanel.revalidate();

				for (Diff diff : diffs) {
					diffHolderPanel.addDiffPanel(new DiffPanel(diff));
				}

				diffHolderPanel.revalidate();
			}
		});

		if (reloadDiffList) {
			panelSummary.reload(c);

			for (Diff diff : allDiffs) {
				panelSummary.getListModel().addElement(new JFileListEntry(diff.getLocalPath(), new ImageIcon(GitIconUtils.getIconFromDiffType(diff.getType()))));
			}
		}
		
		reloadDividers();
	}

	/**
	 * Loads stage and unstaging panel
	 * 
	 * @throws Exception
	 */
	public void loadUncommited() throws Exception {
		List<Diff> diffs = repo.getUncommitedDiffs();
		loadDiffs(null, diffs, null, false);
	}

	public JFrame getParentFrame() {
		return this.parent;
	}

	public CommitListPanel getCommitPanel() {
		return this.jcommitPane;
	}

	public class CommitClickListener implements ICommitClickListener {
		@Override
		public void onClick(Commit c) {
			try {
				if (c == null && repo.hasUnstagedFiles()) {
					loadUncommited();
				} else if (c != null) {
					List<Diff> d = c.getDiffs(false);
					loadDiffs(c, d, d, true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Main.displayError(ex);
			}
		}
	}

	public class DiffSelectionListener implements IDiffSelectionListener {
		@Override
		public void onSelect(Commit c, List<Diff> d, List<Diff> all) {
			try {
				loadDiffs(c, d, all, false);
			} catch (Exception ex) {
				ex.printStackTrace();
				Main.displayError(ex);
			}
		}
	}

	public JPanel getListPanel() {
		return panelList;
	}
}
