package com.redpois0n.gitj.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.gitj.Main;

@SuppressWarnings("serial")
public class CommitButtonPanel extends JPanel {
	
	private CommitPanel parent;
	private JLabel lblAuthor;
	private JComboBox<String> comboBox;
	private JTextPane textPane;
	private JButton btnCommit;

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
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		
		btnCommit = new JButton("Commit");
		btnCommit.setEnabled(false);
		btnCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				commit();
			}
		});
		
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
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				update();
			}
		});
		scrollPane.setViewportView(textPane);
		setLayout(groupLayout);
		
		textPane.requestFocusInWindow();

	}
	
	public void commit() {
		try {
			parent.getRepository().commit(textPane.getText().trim());
			parent.cancel();
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
	}
	
	public void cancel() {
		parent.cancel();
	}
	
	public void update() {
		btnCommit.setEnabled(textPane.getText().trim().length() > 0);
	}
}
