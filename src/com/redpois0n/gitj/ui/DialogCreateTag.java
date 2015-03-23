package com.redpois0n.gitj.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.git.Tag;
import com.redpois0n.gitj.Main;

@SuppressWarnings("serial")
public class DialogCreateTag extends JDialog {
	
	private DialogTags parent;
	private Repository repo;
	private JTextField txtCommit;
	private JTextPane txtMessage;
	private JCheckBox chckbxMessage;
	private JRadioButton rdbtnSpecifiedCommit;
	private JRadioButton rdbtnLatestCommit;
	private JLabel lblName;
	private JTextField txtName;

	public DialogCreateTag(DialogTags parent, Repository repo) {
		this.parent = parent;
		this.repo = repo;
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setTitle("New Tag...");
		
		JLabel lblCommit = new JLabel("Commit:");
		
		ButtonGroup group = new ButtonGroup();
		
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCommit.setEnabled(rdbtnSpecifiedCommit.isSelected());
			}
		};
		
		rdbtnLatestCommit = new JRadioButton("Latest commit");
		rdbtnLatestCommit.setSelected(true);
		rdbtnLatestCommit.addActionListener(listener);
		group.add(rdbtnLatestCommit);
		
		rdbtnSpecifiedCommit = new JRadioButton("Specified commit:");
		rdbtnSpecifiedCommit.addActionListener(listener);
		group.add(rdbtnSpecifiedCommit);
		
		txtCommit = new JTextField();
		txtCommit.setEnabled(false);
		txtCommit.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		chckbxMessage = new JCheckBox("Message");
		chckbxMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtMessage.setEnabled(chckbxMessage.isSelected());
			}
		});
		chckbxMessage.setSelected(true);
		
		lblName = new JLabel("Name:");
		
		txtName = new JTextField();
		txtName.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(rdbtnSpecifiedCommit)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtCommit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(rdbtnLatestCommit))
					.addContainerGap(202, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(288, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(37, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 387, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(chckbxMessage)
					.addContainerGap(361, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(lblName)
					.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
					.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
					.addGap(11))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCommit)
					.addContainerGap(385, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblCommit))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(9)
							.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnLatestCommit)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnSpecifiedCommit)
						.addComponent(txtCommit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxMessage)
					.addGap(6)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnCreate))
					.addContainerGap())
		);
		
		txtMessage = new JTextPane();
		scrollPane.setViewportView(txtMessage);
		getContentPane().setLayout(groupLayout);

		pack();

		setLocationRelativeTo(null);
	}
	
	public void add() {
		Tag.Type type;
		String message;
		
		if (chckbxMessage.isSelected()) {
			type = Tag.Type.ANNOTATED;
			message = txtMessage.getText();
		} else {
			type = Tag.Type.LIGHTWEIGHT;
			message = null;
		}
		
		String name = txtName.getText().trim();
		
		Commit c;
		
		try {
			if (rdbtnSpecifiedCommit.isSelected()) {
				c = repo.getCommit(txtCommit.getText().trim());
			} else {
				c = repo.getCommits().get(0);
			}
			
			repo.createTag(type, name, c, message);
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
		
		if (parent != null) {
			parent.reload();
		}
		
		cancel();
	}
	
	public void cancel() {
		setVisible(false);
		dispose();
	}
}
