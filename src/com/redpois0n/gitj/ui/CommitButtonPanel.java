package com.redpois0n.gitj.ui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.redpois0n.gitj.Main;

@SuppressWarnings("serial")
public class CommitButtonPanel extends JPanel {
	
	private CommitPanel parent;
	private JLabel lblAuthor;
	private JComboBox<String> comboBox;
	private JTextPane textPane;

	public CommitButtonPanel(CommitPanel parent) {
		this.parent = parent;
		
		try {
			lblAuthor = new JLabel(parent.getRepository().getAuthorString());
		} catch (Exception e) {
			e.printStackTrace();
			lblAuthor = new JLabel("Failed to load author: " + e.getClass().getSimpleName() + ", " + e.getMessage());
			Main.displayError(e);
		}
		
		comboBox = new JComboBox<String>();
		
		JButton btnCancel = new JButton("Cancel");
		
		JButton btnCommit = new JButton("Commit");
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(lblAuthor)
							.addPreferredGap(ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCommit)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAuthor)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnCommit))
					.addContainerGap())
		);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		setLayout(groupLayout);

	}
}
