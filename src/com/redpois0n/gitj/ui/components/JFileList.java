package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class JFileList extends JList<JFileListEntry> {
	
	public JFileList() {
		setCellRenderer(new JFileListRenderer());
		setFixedCellHeight(25);
	}
	
	@SuppressWarnings("rawtypes")
	public class JFileListRenderer extends DefaultListCellRenderer {
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Object obj = value;
		
			if (obj instanceof JFileListEntry) {
				JFileListEntry entry = (JFileListEntry) obj;
				
				JLabel label = (JLabel) super.getListCellRendererComponent(list, entry.getText(), index, isSelected, cellHasFocus);;

				label.setIcon(entry.getIcon());
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
