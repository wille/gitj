package com.redpois0n.gitj;

import java.awt.Component;

import com.redpois0n.gitj.ui.MainFrame;
import com.redpois0n.gitj.ui.MainPanel;

public class RunnableReload implements Runnable {
	
	private MainFrame instance;
	
	public RunnableReload(MainFrame instance) {
		this.instance = instance;
	}

	@Override
	public void run() {
		while (true) {
			try {
				for (Component c : instance.getTabs()) {
					if (c instanceof MainPanel) {
						MainPanel panel = (MainPanel) c;
						
						panel.reloadCommits();
						panel.reloadUncommited();
					}
				}
				
				Thread.sleep(10000L);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
