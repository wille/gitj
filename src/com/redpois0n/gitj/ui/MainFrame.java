package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.redpois0n.git.InvalidRepositoryException;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.RunnableReload;
import com.redpois0n.gitj.Version;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);

	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	public MainFrame() {
		setIconImage(IconUtils.getIcon("icon").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnRepository = new JMenu("Repository");
		menuBar.add(mnRepository);
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadCurrentRepo();
			}
		});
		
		JMenuItem mntmRemotes = new JMenuItem("Remotes");
		mntmRemotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remotes();
			}
		});
		mnRepository.add(mntmRemotes);
		mnRepository.add(mntmRefresh);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnCloneNew = new JButton("Clone/New");
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
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stageSelected();
			}
		});
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
		
		JButton btnFetch = new JButton("Fetch");
		btnFetch.setIcon(IconUtils.getIcon("fetch"));
		toolBar.add(btnFetch);
		
		JButton btnPull = new JButton("Pull");
		btnPull.setIcon(IconUtils.getIcon("pull"));
		toolBar.add(btnPull);
		
		JButton btnPush = new JButton("Push");
		btnPush.setIcon(IconUtils.getIcon("push"));
		toolBar.add(btnPush);
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new TabChangeListener());
		splitPane.setRightComponent(tabbedPane);
		
		new Thread(new RunnableReload(this)).start();
	}

	/**
	 * Loads repository in new panel
	 * @param repository
	 */
	public void loadRepository(Repository repository) {
		try {			
			MainPanel pane = new MainPanel(this, repository);
			
			addPanel(repository.getFolder().getName(), pane);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Add tab
	 * @param title Tab title
	 * @param panel
	 */
	public void addPanel(String title, AbstractPanel panel) {
		tabbedPane.addTab(title + "                   ", panel);
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
	
	/**
	 * Reloads selected repo
	 */
	public void reloadCurrentRepo() {
		AbstractPanel mp = getSelectedPanel();
		
		if (mp != null) {
			try {
				mp.reload();
			} catch (Exception e) {
				e.printStackTrace();
				Main.displayError(e);
			}
		}
	}
	
	/**
	 * Returns tabs
	 * @return
	 */
	public Component[] getTabs() {
		return tabbedPane.getComponents();
	}
	
	/**
	 * Sets title beginning with "gitj - "
	 */
	@Override
	public void setTitle(String title) {
		super.setTitle("gitj " + Version.getVersion() + " - " + title);
	}
	
	/**
	 * Opens commit panel for selected repository
	 */
	public void commit() {
		Repository repo = getSelectedRepo();
		AbstractPanel panel = getSelectedPanel();
		
		if (repo != null && panel instanceof MainPanel) {
			addPanel("Commit", new CommitPanel(this, (MainPanel) panel, repo));
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
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		c.showOpenDialog(null);
		
		File file = c.getSelectedFile();
		
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
	
	public class TabChangeListener implements ChangeListener {
		
		/**
		 * Called when tab changed
		 */
		@Override
		public void stateChanged(ChangeEvent arg0) {
			Repository repo = MainFrame.this.getSelectedRepo();
			
			if (repo != null) {
				MainFrame.this.setTitle(repo.getName());
			}
		}
	}
	
}
