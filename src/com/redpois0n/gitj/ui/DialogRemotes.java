package com.redpois0n.gitj.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class DialogRemotes extends JDialog {

	private Repository repo;
	private JScrollPane scrollPane;
	private JTable table;
	private RemotesTableModel model;

	public DialogRemotes(Repository repo) {
		setIconImage(IconUtils.getIcon("remote").getImage());
		setTitle("Remotes");
		setAlwaysOnTop(true);
		setModal(true);
		this.repo = repo;
		setBounds(100, 100, 450, 300);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancel();
			}
		});

		scrollPane = new JScrollPane();
		
		JButton btnAdd = new JButton("New");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit();
			}
		});
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAdd)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEdit)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRemove)
					.addPreferredGap(ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
					.addComponent(btnCancel)
					.addContainerGap())
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnAdd)
						.addComponent(btnEdit)
						.addComponent(btnRemove))
					.addContainerGap())
		);

		model = new RemotesTableModel();
		table = new JTable(model);
		table.setDefaultRenderer(Object.class, new DefaultRenderer());
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setFillsViewportHeight(true);
		table.setIntercellSpacing(new Dimension(0, 0));
		
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);

		setLocationRelativeTo(null);
		
		reload();
	}
	
	public void reload() {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		
		try {
			for (Remote remote : repo.getRemotes()) {
				model.addRow(new Object[] { remote.getName(), remote.getPath() } );
			}
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
	}
	
	public void cancel() {
		setVisible(false);
		dispose();
	}

	public void add() {
		setAlwaysOnTop(false);
		
		String name = JOptionPane.showInputDialog(null, "Input remote name", "Create new Remote", JOptionPane.QUESTION_MESSAGE);
		
		if (name == null || name != null && name.trim().length() == 0) {
			return;
		}
		
		String path = JOptionPane.showInputDialog(null, "Input remote URL", "Create new Remote", JOptionPane.QUESTION_MESSAGE);
		
		if (path == null || path != null && path.trim().length() == 0) {
			return;
		}
		
		try {
			repo.addRemote(name, path);
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
		
		setAlwaysOnTop(true);
		
		reload();
	}
	
	public void edit() {
		setAlwaysOnTop(false);

		int row = table.getSelectedRow();
		
		if (row != -1) {
			String name = table.getValueAt(row, 0).toString();

			String path = JOptionPane.showInputDialog(null, "Input new remote URL", "Edit Remote", JOptionPane.QUESTION_MESSAGE);
			
			if (path == null || path != null && path.trim().length() == 0) {
				return;
			}
			
			try {
				repo.editRemote(name, path);
			} catch (Exception e) {
				e.printStackTrace();
				Main.displayError(e);
			}
		}
		
		setAlwaysOnTop(true);
		
		reload();
	}
	
	public void remove() {
		int row = table.getSelectedRow();
		
		if (row != -1) {
			String name = table.getValueAt(row, 0).toString();
			
			try {
				repo.removeRemote(name);
			} catch (Exception e) {
				e.printStackTrace();
				Main.displayError(e);
			}
		}
		
		reload();
	}

	public class RemotesTableModel extends DefaultTableModel {

		public RemotesTableModel() {
			super.addColumn("Name");
			super.addColumn("Path");
		}

		@Override
		public boolean isCellEditable(int i, int i1) {
			return false;
		}

	}
}
