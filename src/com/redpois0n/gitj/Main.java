package com.redpois0n.gitj;

import java.io.File;

import javax.swing.UIManager;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.MainFrame;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			MainFrame frame = new MainFrame();
			frame.setVisible(true);

			if (argsContains("--debug", args)) {
				File dir = new File(System.getProperty("user.dir"));
				Repository repository = new Repository(dir);

				frame.loadRepository(repository);
				frame.addBookmark(repository);
			}
			
			Runtime.getRuntime().addShutdownHook(new BookmarksShutdownHook(frame));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean argsContains(String s, String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase(s)) {
				return true;
			}
		}

		return false;
	}

	public static void displayError(Exception e) {

	}

	public static void print(String s) {
		System.out.println(s);
	}
}
