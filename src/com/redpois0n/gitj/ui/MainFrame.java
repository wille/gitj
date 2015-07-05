package com.redpois0n.gitj.ui;

import iconlib.IconUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.redpois0n.git.Commit;
import com.redpois0n.git.InvalidRepositoryException;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Bookmarks;
import com.redpois0n.gitj.Language;
import com.redpois0n.gitj.LanguageScanner;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.Version;
import com.redpois0n.gitj.tasks.Task;
import com.redpois0n.gitj.ui.dialogs.DialogCreateTag;
import com.redpois0n.gitj.ui.dialogs.DialogLineCount;
import com.redpois0n.gitj.ui.dialogs.DialogNew;
import com.redpois0n.gitj.ui.dialogs.DialogPull;
import com.redpois0n.gitj.ui.dialogs.DialogPush;
import com.redpois0n.gitj.ui.dialogs.DialogRemotes;
import com.redpois0n.gitj.ui.dialogs.DialogStash;
import com.redpois0n.gitj.ui.dialogs.DialogTags;
import com.redpois0n.gitj.utils.GitIconUtils;
import com.redpois0n.pathtree.FileFilter;
import com.redpois0n.pathtree.FileJTree;
import com.redpois0n.pathtree.NodeClickListener;
import com.redpois0n.pathtree.PathTreeNode;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);

	private File latestDir;
	
	private Bookmarks bookmarks;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JTabbedPane leftTabbedPane;
	private JSplitPane splitPane;
	private FileJTree tree;
	private ObjectsPanel objectPane;
	private BookmarksPanel bookmarksPanel;
	private StatusBar statusBar;

	public MainFrame() {
		setIconImages(GitIconUtils.getIcons());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnRepository = new JMenu("Repository");
		menuBar.add(mnRepository);
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.setIcon(IconUtils.getIcon("update-small"));
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadCurrentRepo();
			}
		});
		
		JMenuItem mntmRemotes = new JMenuItem("Remotes");
		mntmRemotes.setIcon(IconUtils.getIcon("remote"));
		mntmRemotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remotes();
			}
		});
		mnRepository.add(mntmRemotes);
		mnRepository.add(mntmRefresh);
		
		JMenuItem mntmTags = new JMenuItem("Tags");
		mntmTags.setIcon(IconUtils.getIcon("tag-annotated"));
		mntmTags.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTags();
			}
		});
		mnRepository.add(mntmTags);
		
		JMenuItem mntmLineCount = new JMenuItem("Line Count");
		mntmLineCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lineCount();
			}
		});
		mnRepository.add(mntmLineCount);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JCheckBoxMenuItem chckbxmntmViewLanguageBar = new JCheckBoxMenuItem("View Language Bar");
		chckbxmntmViewLanguageBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				
				AbstractPanel panel = getSelectedPanel();
				
				if (panel instanceof MainPanel) {
					// TODO
				}
			}
		});
		chckbxmntmViewLanguageBar.setSelected(true);
		mnView.add(chckbxmntmViewLanguageBar);
		
		mnView.addSeparator();
		
		JMenuItem mntmAddBookmark = new JMenuItem("Add bookmark");
		mntmAddBookmark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				Repository repo = getSelectedRepo();
				
				if (repo != null) {
					addBookmark(repo);
				}
			}
		});
		mnView.add(mntmAddBookmark);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnCloneNew = new JButton("Clone/New");
		btnCloneNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				newRepo();
			}
		});
		btnCloneNew.setIcon(IconUtils.getIcon("database-add"));
		toolBar.add(btnCloneNew);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				open();
			}
		});
		btnOpen.setIcon(IconUtils.getIcon("folder-open"));
		toolBar.add(btnOpen);
		
		toolBar.addSeparator();
		
		JButton btnCommit = new JButton("Commit");
		btnCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				commit();
			}
		});
		btnCommit.setIcon(IconUtils.getIcon("commit"));
		toolBar.add(btnCommit);
		
		JButton btnStash = new JButton("Stash");
		btnStash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stash();
			}
		});
		btnStash.setIcon(IconUtils.getIcon("stash-big"));
		toolBar.add(btnStash);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stageSelected();
			}
		});
		
		JButton btnCheckout = new JButton("Checkout");
		btnCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkout();
			}
		});
		toolBar.add(btnCheckout);
		btnCheckout.setIcon(IconUtils.getIcon("checkout"));
		
		btnAdd.setIcon(IconUtils.getIcon("add-big"));
		toolBar.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				unstageSelected();
			}
		});
		btnRemove.setIcon(IconUtils.getIcon("remove-big"));
		toolBar.add(btnRemove);
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadCurrentRepo();
			}
		});
		
		JButton btnTag = new JButton("Tag");
		btnTag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tag();
			}
		});
		btnTag.setIcon(IconUtils.getIcon("tag-big"));
		toolBar.add(btnTag);
		btnReload.setIcon(IconUtils.getIcon("update"));
		toolBar.add(btnReload);
		
		JButton btnFetch = new JButton("Fetch");
		btnFetch.setIcon(IconUtils.getIcon("fetch"));
		toolBar.add(btnFetch);
		
		JButton btnPull = new JButton("Pull");
		btnPull.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pull();
			}
		});
		btnPull.setIcon(IconUtils.getIcon("pull"));
		toolBar.add(btnPull);
		
		JButton btnPush = new JButton("Push");
		btnPush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				push();
			}
		});
		btnPush.setIcon(IconUtils.getIcon("push"));
		toolBar.add(btnPush);
		
		splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new TabChangeListener());
		splitPane.setRightComponent(tabbedPane);
		
		JScrollPane scrollPaneTree = new JScrollPane();
		tree = new FileJTree(false);
		tree.addFileClickListener(new NodeClickListener() {
			@Override
			public void itemSelected(PathTreeNode node, String path) {
				File file = new File(path);

				openFile(node, file);
			}
		});
		
		bookmarksPanel = new BookmarksPanel(this);
		
		for (String bookmark : Bookmarks.load()) {
			File folder = new File(bookmark);
			
			if (folder.exists() && folder.isDirectory()) {
				try {
					bookmarksPanel.addBookmarkPanel(new BookmarkPanel(bookmarksPanel, new Repository(folder)));
				} catch (InvalidRepositoryException e) {
					e.printStackTrace();
				}
			}
		}
		
		JScrollPane scrollPaneBookmarks = new JScrollPane();
		scrollPaneBookmarks.setViewportView(bookmarksPanel);
		
		scrollPaneTree.setViewportView(tree);
		
		objectPane = new ObjectsPanel(this);
		
		leftTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		leftTabbedPane.addTab("Bookmarks", IconUtils.getIcon("bookmark-folder"), scrollPaneBookmarks);
		leftTabbedPane.addTab("Files", IconUtils.getIcon("folder-tree"), scrollPaneTree);
		leftTabbedPane.addTab("Repository", IconUtils.getIcon("tag-annotated"), objectPane);

		splitPane.setLeftComponent(leftTabbedPane);
		
		statusBar = new StatusBar();
		contentPane.add(statusBar, BorderLayout.SOUTH);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				splitPane.setDividerLocation(splitPane.getSize().width / 4);
			}
		});
	}

	public void addRepoToTree(final Repository repo, String dir) {
		tree.setFilter(new FileFilter() {
			@Override
			public boolean allow(File file) {
				try {
					return !file.getName().startsWith(".") || repo.isTracked(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return true;
			}
		});
		tree.add(dir, IconUtils.getIcon("repo"));
	}
	
	public void openFile(PathTreeNode node, File file) {

		File rootDir = new File(node.getText());

		Repository repo = getFromName(rootDir.getName());

		if (repo != null) {
			try {
				List<Language> langs = new LanguageScanner(repo, false).scan(true);

				for (Language lang : langs) {
					for (String ext : lang.getExtensions()) {
						if (node.getText().toLowerCase().endsWith(ext)) {
							Desktop.getDesktop().open(file); // TODO
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public Repository getFromName(String name) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			AbstractPanel panel = (AbstractPanel) tabbedPane.getComponentAt(i);
			String tabName = tabbedPane.getTitleAt(i);
			
			if (tabName.equals(name)) {
				return panel.getRepository();
			}
		}
		
		return null;
	}
	
	/**
	 * Loads repository in new panel
	 * @param repository
	 */
	public void loadRepository(final Repository repository) {
		if (!isOpen(repository)) {
			new Thread() {
				@Override
				public void run() {
					try {			
						statusBar.setUpdating();
						statusBar.setText("Loading...");
						
						MainPanel pane = new MainPanel(MainFrame.this, repository);
						
						addPanel(repository.getFolder().getName(), pane, IconUtils.getIcon("repo"));
						
						addRepoToTree(repository, repository.getFolder().getAbsolutePath());
					
						statusBar.update(repository);
						bookmarksPanel.reload(repository);
						statusBar.setText("");
					} catch (Exception ex) {
						ex.printStackTrace();
						statusBar.error(ex);
					}
				}
			}.start();
		} else {
			select(repository);
		}
	}
	
	/**
	 * Add repository into bookmarks bar
	 * @param repo
	 */
	public void addBookmark(Repository repo) {
		bookmarksPanel.addBookmarkPanel(new BookmarkPanel(bookmarksPanel, repo));
	}
	
	/**
	 * Add tab
	 * @param title Tab title
	 * @param panel
	 * @param icon 
	 */
	public void addPanel(String title, AbstractPanel panel, ImageIcon icon) {
		tabbedPane.addTab(title, icon, panel);
		tabbedPane.setSelectedComponent(panel);
	}
	
	/**
	 * Remove tab
	 * @param panel
	 */
	public void removePanel(AbstractPanel panel) {
		tabbedPane.remove(panel);
	}
	
	/**
	 * Gets selected repo tab
	 * @return
	 */
	public Repository getSelectedRepo() {
		Component c = tabbedPane.getSelectedComponent();
		
		if (c instanceof AbstractPanel) {
			AbstractPanel mp = (AbstractPanel) c;
			
			return mp.getRepository();
		}
		
		return null;
	}
	
	/**
	 * Checks if repository has a panel
	 * @param repo
	 * @return
	 */
	public boolean isOpen(Repository repo) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			AbstractPanel panel = (AbstractPanel) tabbedPane.getComponentAt(i);
			
			if (panel instanceof MainPanel && ((MainPanel) panel).getRepository().equals(repo)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Selects the repository panel
	 * @param repo
	 */
	public void select(Repository repo) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			AbstractPanel panel = (AbstractPanel) tabbedPane.getComponentAt(i);
			
			if (panel instanceof MainPanel && ((MainPanel) panel).getRepository().equals(repo)) {
				tabbedPane.setSelectedIndex(i);
			}
		}
	}

	/**
	 * Gets selected tab
	 * @return
	 */
	public AbstractPanel getSelectedPanel() {
		Component c = tabbedPane.getSelectedComponent();
		
		if (c instanceof AbstractPanel) {
			return (AbstractPanel) c;
		}
		
		return null;
	}
	
	public Commit getSelectedCommit() {
		Component c = tabbedPane.getSelectedComponent();
		
		if (c instanceof MainPanel) {
			MainPanel panel = (MainPanel) c;
			
			Commit commit = panel.getCommitPanel().getSelectedCommit();
			
			return commit;
		}
		
		return null;
	}
	
	public void reloadCurrentRepo() {
		final AbstractPanel mp = getSelectedPanel();
		
		if (mp != null) {
			try {
				mp.reload();
				
				reloadRepo(mp.repo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Reloads current repo with default status string "Reloading"
	 * @param repo
	 */
	public void reloadRepo(Repository repo) {
		reloadRepo("Reloading", repo);
	}
	
	/**
	 * Reloads selected repo
	 */
	public void reloadRepo(final String status, final Repository repo) {
		new Thread() {
			@Override
			public void run() {
				try {
					statusBar.setUpdating();
					statusBar.setText(status);
					objectPane.reload(null, repo);
					bookmarksPanel.reload(repo);
					statusBar.update(repo);
					statusBar.setText("");
				} catch (Exception e) {
					e.printStackTrace();
					statusBar.error(e);
				}
			}
		}.start();

	}
	
	/**
	 * Returns tabs
	 * @return
	 */
	public Component[] getTabs() {
		return tabbedPane.getComponents();
	}
	
	/**
	 * Sets title beginning with "gitj version - "
	 */
	@Override
	public void setTitle(String title) {
		if (title == null || title.length() == 0) {
			super.setTitle("gitj " + Version.getVersion());
		} else {
			super.setTitle("gitj " + Version.getVersion() + " - " + title);
		}
	}
	
	/**
	 * Opens commit panel for selected repository
	 */
	public void commit() {
		Repository repo = getSelectedRepo();
		AbstractPanel panel = getSelectedPanel();
		
		if (repo != null && panel instanceof MainPanel) {
			addPanel("Commit", new CommitPanel(this, (MainPanel) panel, repo), IconUtils.getIcon("commit-small"));
		}
	}
	
	/**
	 * Opens stash dialog
	 */
	public void stash() {
		Repository repo = getSelectedRepo();

		if (repo != null) {
			new DialogStash(this, repo).setVisible(true);
		}
	}
	
	/**
	 * Stages selected files in current repository
	 */
	public void stageSelected() {
		toggle(true);
	}
	
	/**
	 * Unstages selected files in current repository
	 */
	public void unstageSelected() {
		toggle(false);
	}
	
	/**
	 * Stages or unstages selected files in current repository
	 * @param stage
	 */
	public void toggle(boolean stage) {
		Repository repo = getSelectedRepo();
		AbstractPanel apanel = getSelectedPanel();
		
		if (apanel instanceof AbstractPanel) {
			JPanel panel = null;
			
			if (apanel instanceof MainPanel) {
				panel = ((MainPanel) apanel).getListPanel();
			} else if (apanel instanceof CommitPanel) {
				panel = ((CommitPanel) apanel).getListPanel();
			}
			
			if (panel instanceof PanelUncommited) {
				PanelUncommited pu = (PanelUncommited) panel;
				
				List<String> list;
				
				if (stage) {
					list = pu.getSelectedUnstaged();
				} else {
					list = pu.getSelectedStaged();
				}
				
				for (String path : list) {
					try {
						if (stage) {
							repo.stage(path);
						} else {
							repo.unstage(path);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				try {
					pu.reload();
				} catch (Exception e) {
					e.printStackTrace();
					Main.displayError(e);
				}
			}
		}
	}
	
	public void open() {
		JFileChooser c = new JFileChooser();
		c.setSelectedFile(latestDir);
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		c.showOpenDialog(null);
		
		File file = c.getSelectedFile();
		latestDir = file;
		
		if (file != null) {
			try {
				Repository repository = new Repository(file);
				loadRepository(repository);
			} catch (InvalidRepositoryException e) {
				e.printStackTrace();
				// TODO
			}
		}
	}
	
	public void remotes() {
		Repository repo = getSelectedRepo();
		
		if (repo != null) {
			new DialogRemotes(repo).setVisible(true);
		}
	}
	
	public void showTags() {
		Repository repo = getSelectedRepo();
		
		if (repo != null) {
			new DialogTags(repo).setVisible(true);
		}
	}
	
	public void checkout() {
		
	}
	
	public void tag() {
		Commit commit = getSelectedCommit();
		Repository repo = getSelectedRepo();
		
		if (commit != null && repo != null) {
			new DialogCreateTag(null, repo, commit).setVisible(true);
		}
	}
	
	public void push() {
		Repository repo = getSelectedRepo();

		if (repo != null) {
			try {
				new DialogPush(this, repo).setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pull() {
		Repository repo = getSelectedRepo();

		if (repo != null) {
			try {
				new DialogPull(this, repo).setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void newRepo() {
		new DialogNew().setVisible(true);
	}

	public void lineCount() {
		Repository repo = getSelectedRepo();
		
		if (repo != null) {
			new DialogLineCount(repo).setVisible(true);
		}
	}
	
	public StatusBar getStatusBar() {
		return statusBar;
	}
	
	public BookmarksPanel getBookmarksPanel() {
		return bookmarksPanel;
	}
	
	public void runTask(final Task t) {
		new Thread() {
			@Override
			public void run() {
				statusBar.getProgressBar().setVisible(true);
				statusBar.setText(t.getText());
				
				try {
					t.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Main.displayError(e);
					statusBar.error(e);
					return;
				}

				statusBar.getProgressBar().setVisible(false);
				statusBar.setText("");
				
				reloadCurrentRepo();
			}
		}.start();
	}
	
	public class TabChangeListener implements ChangeListener {
		
		/**
		 * Called when tab changed
		 */
		@Override
		public void stateChanged(ChangeEvent arg0) {
			AbstractPanel panel = MainFrame.this.getSelectedPanel();
			Repository repo = MainFrame.this.getSelectedRepo();
			
			if (repo != null) {
				MainFrame.this.setTitle(repo.getName());
				
				if (panel instanceof MainPanel) {
					try {
						objectPane.reload((MainPanel)panel, repo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				MainFrame.this.setTitle("");
			}			
		}
	}
	
}
