package gitj.ui.dialogs;

import com.redpois0n.git.Repository;
import gitj.tasks.PullTask;
import gitj.ui.MainFrame;

@SuppressWarnings("serial")
public class DialogPull extends DialogRemoteAction {

	public DialogPull(MainFrame parent, Repository repo) throws Exception {
		super(parent, repo);
		super.setTitle("Pull");
	}

	@Override
	public void ok() {
		parent.runTask(new PullTask(repo, branchComboBox.getSelectedBranch(), remoteComboBox.getSelectedRemote()));
	}
}
