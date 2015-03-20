package com.redpois0n.gitj.ui;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.CommitListPanel.CommitRenderer;

@SuppressWarnings("serial")
public class DialogRemotes extends JDialog {

	private Repository repo;
	private JScrollPane scrollPane;
	private JTable table;
	private RemotesTableModel model;

	public DialogRemotes(Repository repo) {
		this.repo = repo;
		setBounds(100, 100, 450, 300);

		JButton btnCancel = new JButton("Cancel");

		JButton btnOk = new JButton("OK");

		scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap(316, Short.MAX_VALUE).addComponent(btnOk).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel).addContainerGap()).addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnCancel).addComponent(btnOk)).addContainerGap()));

		model = new RemotesTableModel();
		table = new JTable(model);
		table.setDefaultRenderer(Object.class, new DefaultRenderer());
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setFillsViewportHeight(true);
		table.setIntercellSpacing(new Dimension(0, 0));
		
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);

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
