package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DialogCreateTag extends JDialog {
	
	private DialogTags parent;
	private JTextField txtCommit;

	public DialogCreateTag(DialogTags parnet) {
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setTitle("New Tag...");
		
		JLabel lblCommit = new JLabel("Commit:");
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton rdbtnLatestCommit = new JRadioButton("Latest commit");
		rdbtnLatestCommit.setSelected(true);
		group.add(rdbtnLatestCommit);
		
		JRadioButton rdbtnSpecifiedCommit = new JRadioButton("Specified commit:");
		group.add(rdbtnSpecifiedCommit);
		
		txtCommit = new JTextField();
		txtCommit.setEnabled(false);
		txtCommit.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblCommit))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(35)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(rdbtnSpecifiedCommit)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtCommit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(rdbtnLatestCommit))))
					.addContainerGap(202, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(16, Short.MAX_VALUE)
					.addComponent(lblCommit)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnLatestCommit)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnSpecifiedCommit)
						.addComponent(txtCommit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(183))
		);
		getContentPane().setLayout(groupLayout);
		
		pack();
		
		setLocationRelativeTo(null);
	}
	
	public void add() {
		
		
		if (parent != null) {
			parent.reload();
		}
	}
	
	public void cancel() {
		
	}
}
