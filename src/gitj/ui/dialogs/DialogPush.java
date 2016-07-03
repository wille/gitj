package gitj.ui.dialogs;

import git.Repository;
import gitj.tasks.PushTask;
import gitj.ui.MainFrame;

@SuppressWarnings("serial")
public class DialogPush extends DialogRemoteAction {

	public DialogPush(MainFrame parent, Repository repo) throws Exception {
		super(parent, repo);
		super.setTitle("Push");
	}

	@Override
	public void ok() {
		parent.runTask(new PushTask(repo, branchComboBox.getSelectedBranch(), remoteComboBox.getSelectedRemote()));
	}
}
