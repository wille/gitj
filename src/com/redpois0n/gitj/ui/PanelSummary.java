package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class PanelSummary extends JPanel {
	
	private JSplitPane splitPane;
	private JTextPane textPane;
	private JList<Diff> list;
	private DefaultListModel<Diff> model;
	
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
		
		model = new DefaultListModel<Diff>();
		list = new JList<Diff>();
		list.setModel(model);
		list.setFixedCellHeight(25);
		list.setCellRenderer(new DiffListRenderer());
		JScrollPane scrollList = new JScrollPane();
		scrollList.setBorder(null);
		scrollList.setViewportView(list);
		splitPane.setRightComponent(scrollList);
	}
	
	public void reload(Commit c, List<Diff> diffs) {
		model.clear();
		
		for (Diff diff : diffs) {
			model.addElement(diff);
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
	
	@SuppressWarnings("rawtypes")
	public class DiffListRenderer extends DefaultListCellRenderer {
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Object obj = value;
		
			if (obj instanceof Diff) {
				Diff diff = (Diff) obj;
				
				JLabel label = (JLabel) super.getListCellRendererComponent(list, diff.getLocalPath(), index, isSelected, cellHasFocus);;

				label.setText(diff.getLocalPath());
				label.setIcon(new ImageIcon(IconUtils.getIcon(diff.getType())));
				label.setForeground(Color.black);
				
				if (isSelected) {
					label.setBackground(new Color(191, 205, 219));
				} else {
					label.setBackground(Color.white);
				}
				
				return label;
			}
			
			return null;
		}
	}

}
