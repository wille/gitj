package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.utils.IconGenerator;

@SuppressWarnings("serial")
public class JCommitPane extends JScrollPane {
	
	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);

	public static final String[] DEFAULT_VALUES = new String[] { "Description", "Date", "Author", "Commit" };
	public static final int INDEX_DESCRIPTION = 0;
	public static final int INDEX_DATE = 1;
	public static final int INDEX_AUTHOR = 2;
	public static final int INDEX_COMMIT = 3;
	
	private List<ICommitClickListener> listeners = new ArrayList<ICommitClickListener>();
	
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
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		
		super.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent evt) {
		        int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	Commit c = (Commit) table.getValueAt(row, 0);
		        	
		        	for (ICommitClickListener l : listeners) {
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
	
	public void addListener(ICommitClickListener l) {
		listeners.add(l);
	}
	
	public void removeListener(ICommitClickListener l) {
		listeners.remove(l);
	}
	
	public class CommitRenderer extends DefaultTableCellRenderer {
		
		private Map<Tag, ImageIcon> cache = new HashMap<Tag, ImageIcon>();
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			Object obj = table.getValueAt(row, 0);
			
			if (obj instanceof Commit) {
				Commit c = (Commit) obj;
				
				ImageIcon icon = null;
				
				if (c.getTags() != null) {
					for (Tag tag : c.getTags()) {
						if (tag.getHash().equals(c.getHash())) {
							if (cache.containsKey(tag)) {
								icon = cache.get(tag);
								break;
							} else {
								icon = IconGenerator.getTagIcon(tag);
								cache.put(tag, icon);
								break;
							}
						}
					}
				}
				
				label.setIcon(null);
				if (column == INDEX_DESCRIPTION) {										
					label.setIcon(icon);
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
				
			} else if (obj == null) {
				if (column == INDEX_DESCRIPTION) {
					label.setText("Uncommitted changes");
					label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
				} else {
					label.setText("");
				}
			}
			
			if (isSelected) {
				label.setBackground(TABLE_SELECTED);
			} else if (row % 2 == 0) {
				label.setBackground(TABLE_GRAY);
			} else {
				label.setBackground(Color.white);
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
