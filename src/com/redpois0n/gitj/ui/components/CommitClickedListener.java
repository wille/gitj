package com.redpois0n.gitj.ui.components;

import com.redpois0n.git.Commit;

public abstract interface CommitClickedListener {

	public abstract void onClick(Commit c);
}
