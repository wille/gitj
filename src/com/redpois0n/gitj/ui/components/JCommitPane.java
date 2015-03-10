package com.redpois0n.gitj.ui.components;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class JCommitPane extends JScrollPane {

	public static final String[] DEFAULT_VALUES = new String[] { "Description", "Date", "Author", "Commit" };
	public static final int INDEX_DESCRIPTION = 0;
	public static final int INDEX_DATE = 1;
	public static final int INDEX_AUTHOR = 2;
	public static final int INDEX_COMMIT = 3;
	
	private List<CommitClickedListener> listeners = new ArrayList<CommitClickedListener>();
	
	private Repository repo;
	private List<Commit> commits;
	private JTable table;
	private CommitTableModel model;
	
	public JCommitPane(Repository repo) throws Exception {
		this.repo = repo;
		this.commits = repo.getCommits();
		this.model = new CommitTableModel();
		this.table = new JTable(model);
		
		table.setDefaultRenderer(Object.class, new CommitRenderer());
		
		super.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent evt) {
		        int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	Commit c = (Commit) table.getValueAt(row, 0);
		        	
		        	for (CommitClickedListener l : listeners) {
		        		l.onClick(c);
		        	}
		        }
		    }
		});
		
		reload(commits);
	}
	
	public void reload(List<Commit> commits) throws Exception {
		this.commits = commits;
		
		clear();
		
		if (repo.hasUnstagedFiles()) {
			model.addRow(new Object[] { null });
		}
		
		for (Commit c : commits) {
			model.addRow(new Object[] { c });
		}
	}
	
	public void clear() {
		while (table.getRowCount() > 0) {
			model.removeRow(0);
		}
	}
	
	public void addListener(CommitClickedListener l) {
		listeners.add(l);
	}
	
	public void removeListener(CommitClickedListener l) {
		listeners.remove(l);
	}
	
	public class CommitRenderer extends DefaultTableCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if (table.getValueAt(row, 0) instanceof Commit) {
				Commit c = (Commit) table.getValueAt(row, 0);
				
				if (column == INDEX_DESCRIPTION) {
					label.setText(c.getComment());
				} else if (column == INDEX_DATE) {
					label.setText(c.getWhen());
				} else if (column == INDEX_AUTHOR) {
					label.setText(c.getDisplayAuthor());
				} else if (column == INDEX_COMMIT) {
					label.setText(c.getDisplayHash());
				} else {
					throw new IndexOutOfBoundsException();
				}
				
			}
			
			return label;
		}
	}
	
	public static class CommitTableModel extends DefaultTableModel {
				
		public CommitTableModel() {
			for (String s : DEFAULT_VALUES) {
				super.addColumn(s);
			}
		}
		
		@Override
		public boolean isCellEditable(int i, int i1) {
			return false;
		}
		
	}

}
