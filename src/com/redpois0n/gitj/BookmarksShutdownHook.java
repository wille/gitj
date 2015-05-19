package com.redpois0n.gitj;

import com.redpois0n.gitj.ui.MainFrame;

public class BookmarksShutdownHook extends Thread {

	private MainFrame frame;
	
	public BookmarksShutdownHook(MainFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void run() {
		try {
			Bookmarks.save(frame.getBookmarksPanel().getBookmarks());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
