package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new TabChangeListener());
		splitPane.setRightComponent(tabbedPane);
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
	public void addPanel(String title, MainPanel panel) {
		tabbedPane.addTab(title, panel);
	}
	
	/**
	 * Gets selected repo tab
	 * @return
	 */
	public Repository getSelectedRepo() {
		Component c = tabbedPane.getSelectedComponent();
		
		if (c instanceof MainPanel) {
			MainPanel mp = (MainPanel) c;
			
			return mp.getRepository();
		}
		
		return null;
	}
	
	public class TabChangeListener implements ChangeListener {
		
		@Override
		public void stateChanged(ChangeEvent arg0) {
			Repository repo = MainFrame.this.getSelectedRepo();
			
			if (repo != null) {
				MainFrame.this.setTitle(repo.getName());
			}
		}
	}
	
	
}
