package com.redpois0n.gitj;

import java.io.File;

import javax.swing.UIManager;

import com.redpois0n.gitj.git.Repository;
import com.redpois0n.gitj.ui.MainFrame;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
			
			File dir = new File(".");
			Repository repository = new Repository(dir);
			repository.getCommits();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
