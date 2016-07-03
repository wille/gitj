package gitj.utils;

import iconlib.IconUtils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import com.redpois0n.git.Change;
import com.redpois0n.git.Repository;

public class StatusUtils {

	public static List<JLabel> getIcons(Repository repo) {
		List<JLabel> list = new ArrayList<JLabel>();

		try {
			List<Change> changes = repo.parseStatus();

			int added = 0;
			int removed = 0;
			int modified = 0;
			int renamed = 0;
			int unknown = 0;

			for (Change change : changes) {
				Change.Type type = change.getFirstType();

				if (type == Change.Type.ADDED) {
					added++;
				} else if (type == Change.Type.UNSTAGED_DELETE) {
					removed++;
				} else if (type == Change.Type.STAGED_DELETE) {
					removed++;
				} else if (type == Change.Type.UNSTAGED_RENAME || type == Change.Type.STAGED_RENAME) {
					renamed++;
				} else if (type == Change.Type.UNSTAGED_COPY) {
					modified++;
				} else if (type == Change.Type.STAGED_COPY) {
					modified++;
				} else if (type == Change.Type.UNSTAGED_MODIFIED || type == Change.Type.STAGED_MODIFIED) {
					modified++;
				} else if (type == Change.Type.UNSTAGED) {
					unknown++;
				} else {
					removed++;
				}
			}

			if (added > 0) {
				JLabel lbl = new JLabel(added + "");
				lbl.setIcon(IconUtils.getIcon("added"));
				list.add(lbl);
			}

			if (removed > 0) {
				JLabel lbl = new JLabel(removed + "");
				lbl.setIcon(IconUtils.getIcon("removed"));
				list.add(lbl);
			}

			if (modified > 0) {
				JLabel lbl = new JLabel(modified + "");
				lbl.setIcon(IconUtils.getIcon("modified"));
				list.add(lbl);
			}

			if (renamed > 0) {
				JLabel lbl = new JLabel(renamed + "");
				lbl.setIcon(IconUtils.getIcon("renamed"));
				list.add(lbl);
			}

			if (unknown > 0) {
				JLabel lbl = new JLabel(unknown + "");
				lbl.setIcon(IconUtils.getIcon("unknown"));
				list.add(lbl);
			}
			
			if (added == 0 && removed == 0 && modified == 0 && renamed == 0 && unknown == 0) {
				JLabel lbl = new JLabel("Clean");
				lbl.setIcon(IconUtils.getIcon("ok"));
				list.add(lbl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
