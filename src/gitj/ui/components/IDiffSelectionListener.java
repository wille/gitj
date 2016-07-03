package gitj.ui.components;

import java.util.List;

import git.Commit;
import git.Diff;

public abstract interface IDiffSelectionListener {

	public abstract void onSelect(Commit c, List<Diff> d, List<Diff> all);
}
