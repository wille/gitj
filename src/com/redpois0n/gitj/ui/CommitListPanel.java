package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.components.BranchComboBox;
import com.redpois0n.gitj.ui.components.ICommitClickListener;
import com.redpois0n.gitj.ui.dialogs.DialogArchive;
import com.redpois0n.gitj.ui.dialogs.DialogReset;
import com.redpois0n.gitj.utils.DialogUtils;
import com.redpois0n.gitj.utils.IOUtils;
import com.redpois0n.gitj.utils.IconGenerator;
import com.redpois0n.gitj.utils.MenuItemUtils;

@SuppressWarnings("serial")
public class CommitListPanel extends JScrollPane {
	
	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);

	public static final String[] DEFAULT_VALUES = new String[] { "Description", "Date", "Author", "Commit" };
	public static final int INDEX_DESCRIPTION = 0;
	public static final int INDEX_DATE = 1;
	public static final int INDEX_AUTHOR = 2;
	public static final int INDEX_COMMIT = 3;
	
	private List<ICommitClickListener> listeners = new ArrayList<ICommitClickListener>();
	
	private Repository repository;
	private List<Commit> commits;
	private JTable table;
	private CommitTableModel model;
	private BranchComboBox branchBox;
	private JToolBar toolBar;
	private JPanel panel;
	
	public CommitListPanel(Repository repo) throws Exception {
		this.repository = repo;
		this.commits = repo.getCommits();
		this.model = new CommitTableModel();
		this.table = new JTable(model);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		branchBox = new BranchComboBox(repo);
		toolBar.add(branchBox);
				
		table.setDefaultRenderer(Object.class, new CommitRenderer());
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setFillsViewportHeight(true);
		table.setIntercellSpacing(new Dimension(0, 0));
				
		panel = new JPanel();

		panel.setLayout(new BorderLayout(0, 0));	
		panel.add(table, BorderLayout.CENTER);
		panel.add(toolBar, BorderLayout.NORTH);

		super.setViewportView(panel);
		
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
		
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem imRevert = new JMenuItem("Revert commit");
		imRevert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	Commit c = (Commit) table.getValueAt(row, 0);
		        	
		        	if (DialogUtils.confirm("Are you sure that you want to create a new commit,\nreversing all the changes in " + c.getHash(), "Revert commit")) {
		        		try {
							c.revert();
							reload(repository.getCommits(true));
						} catch (Exception e) {
							e.printStackTrace();
							Main.displayError(e);
						}
		        	}
		        }
			}		
		});
		menu.add(imRevert);
		
		JMenuItem imReset = new JMenuItem("Reset current branch to this commit");
		imReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	Commit c = (Commit) table.getValueAt(row, 0);
		        	
		        	new DialogReset(c).setVisible(true);
		        }
			}		
		});
		menu.add(imReset);
		
		JMenuItem imArchive = new JMenuItem("Archive");
		imArchive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	Commit c = (Commit) table.getValueAt(row, 0);
		        	
		        	new DialogArchive(c, repository).setVisible(true);
		        }
			}		
		});
		menu.add(imArchive);
		
		JMenuItem imCopy = new JMenuItem("Copy SHA1 to clipboard");
		imCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	Commit c = (Commit) table.getValueAt(row, 0);
		        	
		        	IOUtils.setClipboard(c.getHash());
		        }
			}		
		});
		menu.add(imCopy);
		
		MenuItemUtils.addPopup(table, menu);
		
		reload(commits);
	}
	
	public Commit getSelectedCommit() {
		int row = table.getSelectedRow();
		
		if (row != -1) {
			return (Commit) table.getValueAt(row, 0);
		}
		
		return null;
	}
	
	public void setSelectedCommit(Commit commit) {
		for (int i = 0; i < model.getRowCount(); i++) {
			Commit c = (Commit) table.getValueAt(i, 0);
			
			if (c == commit) {
				table.setRowSelectionInterval(i, i);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
				break;
			}
		}
	}
	
	/**
	 * Reloads the commit list. If repository has unstaged changes, will add "null" to the top which is the row "Uncommited changes"
	 * @param commits
	 * @throws Exception
	 */
	public void reload(List<Commit> commits) throws Exception {
		this.commits = commits;
		
		clear();
		
		if (repository.hasUnstagedFiles()) {
			model.addRow(new Object[] { null });
		}
		
		for (Commit c : commits) {
			model.addRow(new Object[] { c });
		}
		
		branchBox.reload(repository.getBranches());
	}
	
	public void clear() {
		while (table.getRowCount() > 0) {
			model.removeRow(0);
		}
	}
	
	/**
	 * Adds commit click listener
	 * @param l
	 */
	public void addListener(ICommitClickListener l) {
		listeners.add(l);
	}
	
	/**
	 * Removes commit click listener
	 * @param l
	 */
	public void removeListener(ICommitClickListener l) {
		listeners.remove(l);
	}
	
	public class CommitRenderer extends DefaultTableCellRenderer {
		
		private final Map<Commit, ImageIcon> cache = new HashMap<Commit, ImageIcon>();
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			Object obj = table.getValueAt(row, 0);
			
			if (obj instanceof Commit) {
				Commit c = (Commit) obj;
				
				ImageIcon icon = null;
				
				if (c.getTags() != null) {
					List<Tag> tags = new ArrayList<Tag>();
					
					for (Tag tag : c.getTags()) {
						if (tag.getHash().equals(c.getHash())) {
							tags.add(tag);
						}
					}
					
					if (cache.containsKey(c)) {
						icon = cache.get(c);
					} else {
						icon = IconGenerator.getTagIcons(tags);
						cache.put(c, icon);
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
			} else if (row % 2 == 1) {
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
