package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.components.JCommitPane;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private JFrame parent;
	private Repository repository;
	private JSplitPane splitPaneMain;
	private JCommitPane jcommitPane;
	
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
		
		add(splitPaneMain, BorderLayout.CENTER);
		
		jcommitPane = new JCommitPane(repository.getCommits(true));
		
		splitPaneMain.setRightComponent(jcommitPane);
	}
	
	public Repository getRepository() {
		return this.repository;
	}

}
