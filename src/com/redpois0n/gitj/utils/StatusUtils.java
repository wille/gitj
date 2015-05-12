package com.redpois0n.gitj.utils;

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
				list.add(new JLabel(added + "", IconUtils.getIcon("added"), 10));
			}

			if (removed > 0) {
				list.add(new JLabel(removed + "", IconUtils.getIcon("removed"), 10));
			}

			if (modified > 0) {
				list.add(new JLabel(modified + "", IconUtils.getIcon("modified"), 10));
			}

			if (renamed > 0) {
				list.add(new JLabel(renamed + "", IconUtils.getIcon("renamed"), 10));
			}

			if (unknown > 0) {
				list.add(new JLabel(unknown + "", IconUtils.getIcon("unknown"), 10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
