package gitj.ui.dialogs;

import iconlib.IconUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.git.Repository;
import com.redpois0n.git.Stash;
import gitj.tasks.ApplyStashTask;
import gitj.tasks.DeleteStashTask;
import gitj.ui.MainFrame;

@SuppressWarnings("serial")
public class DialogStash extends JDialog {

	private MainFrame parent;
	private Repository repo;
	private Stash stash;

	public DialogStash(MainFrame parent, Repository repo, Stash stash) {
		setIconImage(IconUtils.getIcon("stash").getImage());
		setTitle("Stash " + stash.getStashName());
		setAlwaysOnTop(true);
		setModal(true);
		this.parent = parent;
		this.repo = repo;
		this.stash = stash;
		setBounds(100, 100, 450, 137);

		JButton btnCancel = new JButton("Delete");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delete();
				cancel();
			}
		});
		
		JButton btnStash = new JButton("Apply");
		btnStash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apply();
				cancel();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(294, Short.MAX_VALUE)
					.addComponent(btnStash)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(64, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnStash))
					.addContainerGap())
		);

		getContentPane().setLayout(groupLayout);

		setLocationRelativeTo(null);
	}
	
	public void apply() {
		parent.runTask(new ApplyStashTask(repo, stash));
	}
	
	public void delete() {
		parent.runTask(new DeleteStashTask(repo, stash));
	}
	
	public void cancel() {
		setVisible(false);
		dispose();
	}
}
