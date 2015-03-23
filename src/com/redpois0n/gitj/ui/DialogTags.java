package com.redpois0n.gitj.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.utils.IconUtils;

@SuppressWarnings("serial")
public class DialogTags extends JDialog {

	private Repository repo;
	private JScrollPane scrollPane;
	private JTable table;
	private RemotesTableModel model;

	public DialogTags(Repository repo) {
		setIconImage(IconUtils.getIcon("tag-annotated").getImage());
		setTitle("Tags");
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
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add();
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
					.addComponent(btnRemove)
					.addPreferredGap(ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
					.addComponent(btnCancel)
					.addContainerGap())
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnAdd)
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
			for (Tag tag : repo.getTags(true)) {
				model.addRow(new Object[] { tag.getTag(), tag.getHash() } );
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
		new DialogCreateTag(this, repo, null).setVisible(true);
	}
	
	public void remove() {
		int row = table.getSelectedRow();
		
		if (row != -1) {
			String name = table.getValueAt(row, 0).toString();
			
			try {
				repo.deleteTag(name);
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
			super.addColumn("Commit");
			super.addColumn("Message");
		}

		@Override
		public boolean isCellEditable(int i, int i1) {
			return false;
		}

	}
}
