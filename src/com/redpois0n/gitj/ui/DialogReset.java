package com.redpois0n.gitj.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.git.Commit;
import com.redpois0n.git.ResetMode;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.utils.DialogUtils;

@SuppressWarnings("serial")
public class DialogReset extends JDialog {
	
	private Commit commit;
	private JComboBox<String> comboBox;
	private JLabel lblCommit;
	private JLabel lblBranch;

	public DialogReset(Commit c) {
		this.commit = c;
		setTitle("Reset to Commit");
		setModal(true);
		setResizable(false);
		setAlwaysOnTop(true);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ok();
			}
		});
		
		JLabel lblMode = new JLabel("Mode");
		
		JLabel lblResetBranch = new JLabel("Reset branch");
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		
		for (ResetMode mode : ResetMode.values()) {
			model.addElement(mode.getTextual() + " - " + mode.getDescription());
		}
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(model);
		
		lblBranch = new JLabel("Branch");
		
		JLabel lblToCommit = new JLabel("To commit");
		
		lblCommit = new JLabel(c.getHash() + " - " + c.getComment());
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnOk)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblMode)
								.addComponent(lblResetBranch)
								.addComponent(lblToCommit))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCommit)
								.addComponent(lblBranch)
								.addComponent(comboBox, 0, 350, Short.MAX_VALUE))))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblResetBranch)
						.addComponent(lblBranch))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMode)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblToCommit)
						.addComponent(lblCommit))
					.addPreferredGap(ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnOk))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	public void cancel() {
		setVisible(false);
		dispose();
	}
	
	public void ok() {
		setAlwaysOnTop(false);
		ResetMode mode = null;
		
		String selected = comboBox.getSelectedItem().toString().split(" - ")[0];
		for (ResetMode m : ResetMode.values()) {
			if (selected.equals(m.getTextual())) {
				mode = m;
				break;
			}
		}
		
		if (DialogUtils.confirm("Are you sure that you want to reset current branch to commit " + commit.getHash(), "Reset to Commit")) {
			try {
				commit.reset(mode);
			} catch (Exception e) {
				e.printStackTrace();
				Main.displayError(e);
			}
		}
		
		setAlwaysOnTop(true);
	}
}
