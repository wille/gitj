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

import com.redpois0n.git.CommitOption;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.tasks.CommitTask;
import com.redpois0n.gitj.tasks.Task;
import com.redpois0n.gitj.utils.DialogUtils;

@SuppressWarnings("serial")
public class CommitButtonPanel extends JPanel {
	
	private CommitPanel parent;
	private JLabel lblAuthor;
	private JComboBox<String> comboBox;
	private JTextPane textPane;
	private JButton btnCommit;

	public CommitButtonPanel(CommitPanel p) {
		this.parent = p;
		
		try {
			lblAuthor = new JLabel(parent.getRepository().getAuthorString());
		} catch (Exception e) {
			e.printStackTrace();
			lblAuthor = new JLabel("Failed to load author: " + e.getClass().getSimpleName() + ", " + e.getMessage());
			Main.displayError(e);
		}
		
		comboBox = new JComboBox<String>();
		
		for (CommitOption o : CommitOption.values()) {
			comboBox.addItem(o.getTextual() + " - " + o.getDescription());
		}
		
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String selected = comboBox.getSelectedItem().toString();
				
				if (selected.startsWith(CommitOption.AMEND.getTextual())) {
					if (DialogUtils.confirm("Do you want to replace the commit message with the message from your latest commit?", "Amend Commit")) {
						try {
							textPane.setText(parent.getRepository().getCommits().get(0).getComment());
							btnCommit.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace();
							Main.displayError(e);
						}
					}
				}
			}
		});
		
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
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAuthor)
							.addPreferredGap(ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE))
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
		String item = comboBox.getSelectedItem().toString();
		
		CommitOption mode = null;
		
		for (CommitOption o : CommitOption.values()) {
			if (o.getTextual().equals(item.split(" - ")[0])) {
				mode = o;
				break;
			}
		}
		
		final CommitOption m = mode;
		
		parent.getParentFrame().runTask(new CommitTask(parent.getRepository(), textPane.getText(), m));
		
		parent.cancel();
	}
	
	public void cancel() {
		parent.cancel();
	}
	
	public void update() {
		btnCommit.setEnabled(textPane.getText().trim().length() > 0);
	}
}
