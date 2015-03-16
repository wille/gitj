package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.redpois0n.git.Change;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.components.JFileList;
import com.redpois0n.gitj.ui.components.JFileListEntry;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class PanelUncommited extends AbstractPanel {
	
	private DefaultListModel<JFileListEntry> unstagedModel;
	private JFileList unstagedList;

	private DefaultListModel<JFileListEntry> stagedModel;
	private JFileList stagedList;
	
	public PanelUncommited(Repository repo) {
		super(repo);
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		
		JScrollPane stagedScrollList = new JScrollPane();
		stagedScrollList.setBorder(null);
		stagedModel = new DefaultListModel<JFileListEntry>();
		stagedList = new JFileList();
		stagedList.setModel(stagedModel);
		stagedScrollList.setViewportView(stagedList);
		splitPane.setLeftComponent(stagedScrollList);
		
		JScrollPane unstagedScrollList = new JScrollPane();
		unstagedScrollList.setBorder(null);
		unstagedModel = new DefaultListModel<JFileListEntry>();
		unstagedList = new JFileList();
		unstagedList.setModel(unstagedModel);
		unstagedScrollList.setViewportView(unstagedList);
		splitPane.setRightComponent(unstagedScrollList);
		
		add(splitPane);
	}
	
	public void reload() throws Exception {
		unstagedModel.clear();
		stagedModel.clear();
		
		List<Change> changes = repo.parseStatus();
		
		for (Change change : changes) {
			DefaultListModel<JFileListEntry> model;
			
			if (change.isStaged()) {
				model = stagedModel;
			} else {
				model = unstagedModel;
			}
			
			model.addElement(new JFileListEntry(change.getRepoPath(), IconUtils.getIconFromChangeType(change.getTypes().get(0))));
		}
	}
	
	public List<String> getSelectedUnstaged() {
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < unstagedModel.getSize(); i++) {
			list.add(unstagedModel.get(i).getText());
		}
		
		return list;
	}
	
	public List<String> getSelectedStaged() {
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < stagedModel.getSize(); i++) {
			list.add(stagedModel.get(i).getText());
		}
		
		return list;
	}
}
