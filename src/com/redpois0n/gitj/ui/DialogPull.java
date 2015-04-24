package com.redpois0n.gitj.ui;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class DialogPull extends DialogRemoteAction {

	public DialogPull(Repository repo) throws Exception {
		super(repo);
		super.setTitle("Pull");
	}

	@Override
	public void ok() {
		try {
			repo.pull(remoteComboBox.getSelectedRemote(), branchComboBox.getSelectedBranch());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
