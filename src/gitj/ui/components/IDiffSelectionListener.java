package gitj.ui.components;

import java.util.List;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;

public abstract interface IDiffSelectionListener {

	public abstract void onSelect(Commit c, List<Diff> d, List<Diff> all);
}
