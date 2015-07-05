package com.redpois0n.gitj.ui.dialogs;

import iconlib.IconUtils;

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

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.DefaultRenderer;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class DialogStash extends JDialog {

	private Repository repo;
	private JTextField textField;
	private JCheckBox chckbxKeepStagedChanges;

	public DialogStash(Repository repo) {
		setIconImage(IconUtils.getIcon("stash").getImage());
		setTitle("Stash");
		setAlwaysOnTop(true);
		setModal(true);
		this.repo = repo;
		setBounds(100, 100, 450, 137);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancel();
			}
		});
		
		JButton btnStash = new JButton("Stash");
		btnStash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stash();
			}
		});
		
		JLabel lblMessage = new JLabel("Message");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		chckbxKeepStagedChanges = new JCheckBox("Keep staged changes");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxKeepStagedChanges)
							.addPreferredGap(ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
							.addComponent(btnStash)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblMessage)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMessage))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxKeepStagedChanges)
						.addComponent(btnCancel)
						.addComponent(btnStash))
					.addContainerGap())
		);

		getContentPane().setLayout(groupLayout);

		setLocationRelativeTo(null);
	}
	
	public void stash() {
		try {
			repo.stash(textField.getText(), chckbxKeepStagedChanges.isSelected());
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
	}
	
	public void cancel() {
		setVisible(false);
		dispose();
	}
}
