package com.redpois0n.gitj.utils;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;

import com.redpois0n.git.Commit;
import com.redpois0n.gitj.Main;

public class MenuItemUtils {
	
	public static JMenuItem getOpenSelectedVersion(final Commit c, final String repopath) {
		JMenuItem item = new JMenuItem();
		item.setText("Open selected version");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String contents = c.getRepository().getFileAt(c, repopath);
					File file = FileUtils.writeToTempFile(contents);
					Desktop.getDesktop().open(file);
				} catch (Exception ex) {
					ex.printStackTrace();
					Main.displayError(ex);
				}
			}
		});
		
		return item;
	}

}
