package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.components.JCommitPane;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JSplitPane splitPaneMain;
	private JCommitPane jcommitPane;
	
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		splitPaneMain = new JSplitPane();
		contentPane.add(splitPaneMain, BorderLayout.CENTER);
		
		
	}

	public void loadRepository(Repository repository) {
		try {
			List<Commit> commits = repository.getCommits(true);
			
			jcommitPane = new JCommitPane(commits);
			
			splitPaneMain.setRightComponent(jcommitPane);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
