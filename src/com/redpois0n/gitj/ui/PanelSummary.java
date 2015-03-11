package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.gitj.ui.components.JFileList;
import com.redpois0n.gitj.ui.components.JFileListEntry;
import com.redpois0n.gitj.utils.IconUtils;
import com.redpois0n.gitj.utils.MenuItemUtils;

import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;

@SuppressWarnings("serial")
public class PanelSummary extends JPanel {
	
	private Commit commit;
	private JSplitPane splitPane;
	private JTextPane textPane;
	private JFileList list;
	private DefaultListModel<JFileListEntry> model;
	private JPopupMenu popupMenu;
	
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
		JScrollPane scrollList = new JScrollPane();
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
	
	public void reload(Commit c, List<Diff> diffs) {
		this.commit = c;
		model.clear();
		
		for (Diff diff : diffs) {
			model.addElement(new JFileListEntry(diff.getLocalPath(), new ImageIcon(IconUtils.getIconFromDiffType(diff.getType()))));
		}
		
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

	public void clear() {
		model.clear();
		textPane.setText("");
	}

}
