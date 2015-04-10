package com.redpois0n.gitj.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;

@SuppressWarnings("serial")
public class DialogArchive extends JDialog {
	
	private Repository repo;
	private JTextField txtCommit;
	private JRadioButton rdbtnSpecifiedCommit;
	private JRadioButton rdbtnLatestCommit;
	private JLabel lblName;
	private JTextField txtName;

	public DialogArchive(Commit c, Repository repo) {
		this.repo = repo;
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setTitle("Archive");
		
		JLabel lblCommit = new JLabel("Commit:");
		
		ButtonGroup group = new ButtonGroup();
		
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCommit.setEnabled(rdbtnSpecifiedCommit.isSelected());
			}
		};
		
		rdbtnLatestCommit = new JRadioButton("Latest commit");
		rdbtnLatestCommit.addActionListener(listener);
		group.add(rdbtnLatestCommit);
		
		rdbtnSpecifiedCommit = new JRadioButton("Specified commit:");
		rdbtnSpecifiedCommit.addActionListener(listener);
		rdbtnSpecifiedCommit.setSelected(true);
		group.add(rdbtnSpecifiedCommit);
		
		txtCommit = new JTextField();
		if (c != null) {
			txtCommit.setText(c.getHash());
		}
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
		
		lblName = new JLabel("Prefix:");
		
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
							.addComponent(txtCommit, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
						.addComponent(rdbtnLatestCommit))
					.addContainerGap())
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
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(285, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addGap(9)
					.addComponent(btnCancel)
					.addContainerGap())
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
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCreate)
						.addComponent(btnCancel))
					.addGap(171))
		);
		getContentPane().setLayout(groupLayout);

		setLocationRelativeTo(null);
	}
	
	public void add() {
		setAlwaysOnTop(false);
		
		JFileChooser jfc = new JFileChooser();
		jfc.setSelectedFile(new File("archive.zip"));
		jfc.showSaveDialog(null);
		setAlwaysOnTop(true);

		if (jfc.getSelectedFile() != null) {
			String prefix = txtName.getText();
						
			Commit c;
			
			try {
				if (rdbtnSpecifiedCommit.isSelected()) {
					c = repo.getCommit(txtCommit.getText().trim());
				} else {
					c = repo.getCommits().get(0);
				}
				
				repo.archive(jfc.getSelectedFile(), prefix, "zip", c);
			} catch (Exception e) {
				e.printStackTrace();
				Main.displayError(e);
				return;
			}
		}
				
		cancel();
	}
	
	public void cancel() {
		setVisible(false);
		dispose();
	}
}
