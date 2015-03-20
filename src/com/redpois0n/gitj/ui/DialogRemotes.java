package com.redpois0n.gitj.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.utils.IconGenerator;

@SuppressWarnings("serial")
public class DialogRemotes extends JDialog {

	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);

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
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);

	}

	public class RemotesRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

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
