package gitj.ui;

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

import com.redpois0n.git.Branch;
import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import gitj.Main;
import gitj.ui.components.BranchComboBox;
import gitj.ui.components.ICommitClickListener;
import gitj.ui.dialogs.DialogArchive;
import gitj.ui.dialogs.DialogReset;
import gitj.utils.DialogUtils;
import gitj.utils.IOUtils;
import gitj.utils.IconGenerator;
import gitj.utils.MenuItemUtils;

@SuppressWarnings("serial")
public class CommitListPanel extends JScrollPane {
	
	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);
	
	public static final String COLUMN_GRAPH = "Graph";
	public static final String COLUMN_DESCRIPTION = "Description";
	public static final String COLUMN_DATE = "Date";
	public static final String COLUMN_AUTHOR = "Author";
	public static final String COLUMN_COMMIT = "Commit";
	
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

        super.getVerticalScrollBar().setUnitIncrement(10);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		branchBox = new BranchComboBox(this, repo);
		branchBox.reload();
		toolBar.add(branchBox);
				
		table.setDefaultRenderer(Object.class, new CommitRenderer());
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setFillsViewportHeight(true);
		table.setIntercellSpacing(new Dimension(0, 0));
						
		panel = new JPanel();

		panel.setLayout(new BorderLayout(0, 0));	
		panel.add(table, BorderLayout.SOUTH);
		panel.add(toolBar, BorderLayout.NORTH);

		super.setViewportView(panel);
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent evt) {
		        int row = table.getSelectedRow();
		        
		        if (row != -1) {
		        	TableEntry entry = (TableEntry) table.getValueAt(row, 0);
		        	
					//System.out.println(entry.getCommit().getRepository().getGraph().get(entry.getCommit()).getData().get(entry.getGraphIndex()));

		        	if (entry.getGraphIndex() == 0) {
		        		for (ICommitClickListener l : listeners) {
			        		l.onClick(entry.getCommit());
			        	}
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
							reload();
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
		
		reload();
	}
	
	public Commit getSelectedCommit() {
		int row = table.getSelectedRow();
		
		if (row != -1) {
			return ((CommitListPanel.TableEntry) table.getValueAt(row, 0)).getCommit();
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
	 * @throws Exception
	 */
	public void reload() throws Exception {
		Branch selectedBranch = branchBox.getSelectedBranch();
		
		List<Commit> commits = repository.getCommits(true, selectedBranch == null, selectedBranch);
		this.commits = commits;
		
		clear();
		
		if (repository.hasUnstagedFiles()) {
			model.addRow(new Object[] { null });
		}
		
		for (Commit c : commits) {
			List<String> data = c.getGraphData();
			
			for (int i = 0; i < data.size(); i++) {
				model.addRow(new Object[] { new TableEntry(c, i) });
			}
		}
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
	
	public class TableEntry {
		
		private Commit commit;
		private int graphIndex;
		
		public TableEntry(Commit commit, int graphIndex) {
			this.commit = commit;
			this.graphIndex = graphIndex;
		}
		
		public Commit getCommit() {
			return this.commit;
		}
		
		public int getGraphIndex() {
			return this.graphIndex;
		}
	}
	
	public class CommitRenderer extends DefaultTableCellRenderer {
		
		private final Map<Commit, ImageIcon> cache = new HashMap<Commit, ImageIcon>();
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			Object obj = table.getValueAt(row, 0);
			
			if (obj instanceof TableEntry) {
				TableEntry entry = (TableEntry) obj;
				Commit c = entry.getCommit();
				
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

				if (table.getColumnName(column).equals(COLUMN_GRAPH)) {
					label.setIcon(c.getRepository().getGraph().getIcon(row * table.getRowHeight()));
					label.setText("");
				} else if (entry.getGraphIndex() != 0) {
					return label;
				}

				if (table.getColumnName(column).equals(COLUMN_DESCRIPTION)) {										
					label.setIcon(icon);
					label.setText(c.getComment());
				} else if (table.getColumnName(column).equals(COLUMN_DATE)) {
					label.setText(c.getWhen());
				} else if (table.getColumnName(column).equals(COLUMN_AUTHOR)) {
					label.setText(c.getDisplayAuthor());
				} else if (table.getColumnName(column).equals(COLUMN_COMMIT)) {
					label.setText(c.getDisplayHash());
				}
			} else if (obj == null) {
				if (table.getColumnName(column).equals(COLUMN_DESCRIPTION)) {										
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
			super.addColumn(COLUMN_GRAPH);
			super.addColumn(COLUMN_DESCRIPTION);
			super.addColumn(COLUMN_DATE);
			super.addColumn(COLUMN_AUTHOR);
			super.addColumn(COLUMN_COMMIT);
		}
		
		@Override
		public boolean isCellEditable(int i, int i1) {
			return false;
		}
		
	}

}
