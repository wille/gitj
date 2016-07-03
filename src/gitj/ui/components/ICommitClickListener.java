package gitj.ui.components;

import git.Commit;

public abstract interface ICommitClickListener {

	public abstract void onClick(Commit c);
}
