package com.redpois0n.gitj.ui.components;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.gitj.git.Commit;
import com.redpois0n.gitj.git.Repository;

@SuppressWarnings("serial")
public class JCommitPane extends JScrollPane {

	public static final String[] DEFAULT_VALUES = new String[] { "Description", "Date", "Author", "Commit" };
	public static final int INDEX_DESCRIPTION = 0;
	public static final int INDEX_DATE = 1;
	public static final int INDEX_AUTHOR = 2;
	public static final int INDEX_COMMIT = 3;
	
	public JCommitPane(Repository repo) {
		JTable list = new JTable(new CommitTableModel());
		list.setDefaultRenderer(Object.class, new CommitRenderer());
	}
	
	public class CommitRenderer extends DefaultTableCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value instanceof Commit) {
				Commit c = (Commit) value;
				JLabel label = new JLabel();
				
				return label;
			} else {
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		}
	}
	
	public static class CommitTableModel extends DefaultTableModel {
				
		public CommitTableModel() {
			for (String s : DEFAULT_VALUES) {
				super.addColumn(s);
			}
		}
		
	}

}
