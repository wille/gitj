package com.redpois0n.gitj.ui;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class DialogPush extends DialogRemoteAction {

	public DialogPush(Repository repo) throws Exception {
		super(repo);
		super.setTitle("Push");
	}

	@Override
	public void ok() {
		try {
			repo.push(remoteComboBox.getSelectedRemote(), branchComboBox.getSelectedBranch());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
