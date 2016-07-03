package gitj.ui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.redpois0n.git.Commit;
import com.redpois0n.git.ResetMode;
import gitj.Main;
import gitj.utils.DialogUtils;

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
		setBounds(0, 0, 450, 200);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(369, 133, 65, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(316, 133, 47, 23);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}
		});
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(48, 65, 26, 14);
		
		JLabel lblResetBranch = new JLabel("Reset branch");
		lblResetBranch.setBounds(10, 30, 64, 14);
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		
		for (ResetMode mode : ResetMode.values()) {
			model.addElement(mode.getTextual() + " - " + mode.getDescription());
		}
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(84, 62, 350, 20);
		comboBox.setModel(model);
		
		lblBranch = new JLabel("Branch");
		lblBranch.setBounds(84, 30, 33, 14);
		
		JLabel lblToCommit = new JLabel("To commit");
		lblToCommit.setBounds(26, 100, 48, 14);
		
		lblCommit = new JLabel(c.getHash() + " - " + c.getComment());
		lblCommit.setBounds(84, 100, 120, 14);
		getContentPane().setLayout(null);
		getContentPane().add(btnOk);
		getContentPane().add(btnCancel);
		getContentPane().add(lblMode);
		getContentPane().add(lblResetBranch);
		getContentPane().add(lblToCommit);
		getContentPane().add(lblCommit);
		getContentPane().add(lblBranch);
		getContentPane().add(comboBox);
		
		setLocationRelativeTo(null);
	}
	
	public void close() {
		setVisible(false);
		dispose();
	}
	
	public void reset() {
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
				close();
			} catch (Exception e) {
				e.printStackTrace();
				Main.displayError(e);
			}
		}
	
		setAlwaysOnTop(true);
	}
}
