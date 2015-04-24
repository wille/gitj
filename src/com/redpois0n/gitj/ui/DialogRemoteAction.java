package com.redpois0n.gitj.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.components.BranchComboBox;
import com.redpois0n.gitj.ui.components.RemoteComboBox;

@SuppressWarnings("serial")
public abstract class DialogRemoteAction extends JDialog {

	protected Repository repo;
	protected RemoteComboBox remoteComboBox;
	protected BranchComboBox branchComboBox;
	
	public DialogRemoteAction(Repository repo) throws Exception {
		this.repo = repo;

		setBounds(100, 100, 450, 300);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
				close();
			}
		});
		
		JLabel lblRemote = new JLabel("Remote");
		
		remoteComboBox = new RemoteComboBox(repo);
		
		JLabel lblBranch = new JLabel("Branch");
		
		branchComboBox = new BranchComboBox(repo);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblRemote)
							.addGap(44)
							.addComponent(remoteComboBox, 0, 333, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnOk)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblBranch)
							.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
							.addComponent(branchComboBox, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRemote)
						.addComponent(remoteComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBranch)
						.addComponent(branchComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnOk))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
	}
	
	/**
	 * Called when OK button is pressed
	 */
	public abstract void ok();
	
	/**
	 * Called when cancel button is pressed or action completed
	 */
	public void close() {
		setVisible(false);
		dispose();
	}
}
