package gitj.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.redpois0n.git.Change;
import com.redpois0n.git.Diff;
import com.redpois0n.git.Repository;
import gitj.Main;
import gitj.ui.components.IDiffSelectionListener;
import gitj.ui.components.JFileList;
import gitj.ui.components.JFileListEntry;
import gitj.utils.DialogUtils;
import gitj.utils.GitIconUtils;
import gitj.utils.MenuItemUtils;

@SuppressWarnings("serial")
public class PanelUncommited extends AbstractPanel {
	
	private List<IDiffSelectionListener> listeners = new ArrayList<IDiffSelectionListener>();
	private AbstractPanel parent;
	private DefaultListModel<JFileListEntry> unstagedModel;
	private JFileList unstagedList;

	private DefaultListModel<JFileListEntry> stagedModel;
	private JFileList stagedList;
	
	public PanelUncommited(AbstractPanel parent, final Repository repo) {
		super(repo);
		this.parent = parent;
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);	
		
		JScrollPane stagedScrollList = new JScrollPane();
		stagedScrollList.getVerticalScrollBar().setUnitIncrement(10);
		stagedScrollList.setBorder(null);
		stagedModel = new DefaultListModel<JFileListEntry>();
		stagedList = new JFileList();
		stagedList.setModel(stagedModel);
		stagedList.addMouseListener(new ClickListener());
		stagedScrollList.setViewportView(stagedList);
		splitPane.setLeftComponent(stagedScrollList);
		
		JScrollPane unstagedScrollList = new JScrollPane();
		unstagedScrollList.getVerticalScrollBar().setUnitIncrement(10);
		unstagedScrollList.setBorder(null);
		unstagedModel = new DefaultListModel<JFileListEntry>();
		unstagedList = new JFileList();
		unstagedList.setModel(unstagedModel);
		unstagedList.addMouseListener(new ClickListener());
		unstagedScrollList.setViewportView(unstagedList);
		splitPane.setRightComponent(unstagedScrollList);
		
		JPopupMenu menu = new JPopupMenu();
		MenuItemUtils.addPopup(unstagedList, menu);
		
		JMenuItem btnRemove = new JMenuItem("Remove");
		menu.add(btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> list = getSelectedUnstaged();
				
				if (DialogUtils.confirm("Remove", "Are you sure that you want to remove " + list.size() + " files?")) {
					for (String s : list) {
						File file = repo.getAbsoluteFile(s);
						
						file.delete();
					}
					
					try {
						reload();
					} catch (Exception e1) {
						e1.printStackTrace();
						Main.displayError(e1);
					}
				}
			}
		});
		
		add(splitPane);
	}
	
	public void addListener(IDiffSelectionListener l) {
		listeners.add(l);
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
			
			System.out.println(change.getRepoPath());
			
			model.addElement(new JFileListEntry(change.getRepoPath(), GitIconUtils.getIconFromChangeType(change.getTypes().get(0))));
		}
	}
	
	public List<String> getSelectedUnstaged() {
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < unstagedModel.getSize(); i++) {
			if (unstagedList.isSelectedIndex(i)) {
				list.add(unstagedModel.get(i).getText());
			}
		}
		
		return list;
	}
	
	public List<String> getSelectedStaged() {
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < stagedModel.getSize(); i++) {
			if (stagedList.isSelectedIndex(i)) {
				list.add(stagedModel.get(i).getText());
			}
		}
		
		for (int i = 0; i < unstagedModel.getSize(); i++) {
			if (unstagedList.isSelectedIndex(i)) {
				list.add(unstagedModel.get(i).getText());
			}
		}
		
		return list;
	}
	
	public void loadDiff(List<Diff> diffs) {
		for (IDiffSelectionListener l : listeners) {
			l.onSelect(null, diffs, null);
		}
	}
	
	public class ClickListener extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {
			JFileList list = (JFileList) e.getSource();
			
			JFileListEntry entry = list.getSelectedValue();
			
			if (entry != null && e.getClickCount() == 2 && entry != null) {
				try {
					if (list == stagedList) {
						repo.unstage(entry.getText());
					} else {
						repo.stage(entry.getText());
					}
					
					reload();
				} catch (Exception ex) {
					ex.printStackTrace();
					Main.displayError(ex);
				}
			} else if (entry != null && e.getClickCount() == 1) {
				List<Diff> diffs = repo.getUncommitedDiffs();
				List<Diff> selected = new ArrayList<Diff>();
				
				for (Diff d : diffs) {
					if (d.getLocalPath().equals(entry.getText())) {
						selected.add(d);
					}
				}
				
				loadDiff(selected);
			}
		}
	}
}
