package gitj.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import gitj.ui.components.IDiffSelectionListener;
import gitj.ui.components.JFileList;
import gitj.ui.components.JFileListEntry;
import gitj.utils.MenuItemUtils;

@SuppressWarnings("serial")
public class PanelSummary extends JPanel {
	
	private Commit commit;
	private JSplitPane splitPane;
	private JTextPane textPane;
	private JFileList list;
	private DefaultListModel<JFileListEntry> model;
	private JPopupMenu popupMenu;
	
	private List<IDiffSelectionListener> listeners = new ArrayList<IDiffSelectionListener>();
	
	public PanelSummary() {
		setLayout(new BorderLayout(0, 0));
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		add(splitPane);
		
		textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		JScrollPane scrollTextPane = new JScrollPane();
		scrollTextPane.setBorder(null);
		scrollTextPane.setViewportView(textPane);
		splitPane.setLeftComponent(scrollTextPane);
		
		model = new DefaultListModel<JFileListEntry>();
		list = new JFileList();
		list.setModel(model);
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		      
		        if (!lsm.isSelectionEmpty() && !e.getValueIsAdjusting()) {
		            int min = lsm.getMinSelectionIndex();
		            int max = lsm.getMaxSelectionIndex();
		            
		            List<Diff> diffs = new ArrayList<Diff>();
		            List<Diff> allDiffs = commit.getDiffs(false);
		            
		            for (int i = min; i <= max; i++) {
		                if (lsm.isSelectedIndex(i)) {
		                    JFileListEntry entry = model.get(i);
		                    
		                    for (Diff d : allDiffs) {
		                    	if (d.getLocalPath().equals(entry.getText())) {
		                    		diffs.add(d);
		                    	}
		                    }
		                }
		            }
		            
		            for (IDiffSelectionListener l : listeners) {
                    	l.onSelect(commit, diffs, allDiffs);
                    }
		        }
			}		
		});
		JScrollPane scrollList = new JScrollPane();
		scrollList.getVerticalScrollBar().setUnitIncrement(10);
		scrollList.setBorder(null);
		scrollList.setViewportView(list);
		splitPane.setRightComponent(scrollList);
		
		popupMenu = new JPopupMenu();
		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				popupMenu.removeAll();
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				Object value = list.getSelectedValue();
				
				JMenuItem item = MenuItemUtils.getOpenSelectedVersion(commit, value == null ? "" : list.getSelectedValue().getText());
				item.setEnabled(value != null);
				popupMenu.add(item);
			}
		});
		MenuItemUtils.addPopup(list, popupMenu);
	}
	
	public void addListener(IDiffSelectionListener l) {
		listeners.add(l);
	}
	
	public void removeListener(IDiffSelectionListener l) {
		listeners.remove(l);
	}
	
	public void reload(Commit c) {
		this.commit = c;
		model.clear();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("<b>Commit:</b> " + c.getHash() + " [" + c.getDisplayHash() + "]<br>");
		sb.append("<b>Author:</b> " + c.getDisplayAuthor().replace(">", "&#62").replace("<", "&#60") + "<br>");
		sb.append("<b>When:</b> " + c.getWhen() + "<br>");
		sb.append("<br>");
		sb.append(c.getComment());
		sb.append("</html>");
		
		textPane.setText(sb.toString());
	}
	
	public DefaultListModel<JFileListEntry> getListModel() {
		return model;
	}

	public void clear() {
		model.clear();
		textPane.setText("");
	}

	public void reloadDividers() {
		splitPane.setDividerLocation(splitPane.getSize().height / 2);		
	}

}
