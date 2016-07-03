package gitj.utils;

import iconlib.IconUtils;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import git.Change;
import git.Diff;

public class GitIconUtils {

	public static Image getIconFromDiffType(Diff.Type type) {
		String s;
		
		if (type == Diff.Type.DELETED) {
			s = "deleted";
		} else if (type == Diff.Type.NEW) {
			s = "added";
		} else {
			s = "modified";
		}
		
		return IconUtils.getIcon(s).getImage();
	}
	
	public static ImageIcon getIconFromChangeType(Change.Type type) {
		String s = null;
		
		if (type == Change.Type.ADDED) {
			s = "added";
		} else if (type == Change.Type.UNSTAGED_DELETE) {
			s = "missing";
		} else if (type == Change.Type.STAGED_DELETE) {
			s = "deleted";
		} else if (type == Change.Type.UNSTAGED_RENAME || type == Change.Type.STAGED_RENAME) {
			s = "renamed";
		} else if (type == Change.Type.UNSTAGED_COPY) {
			
		} else if (type == Change.Type.STAGED_COPY) {
			
		} else if (type == Change.Type.UNSTAGED_MODIFIED || type == Change.Type.STAGED_MODIFIED) {
			s = "modified";
		} else if (type == Change.Type.UNSTAGED) {
			s = "unknown";
		}
		
		if (s == null) {
			s = "missing";
		}
		
		return IconUtils.getIcon(s);
	}
	
	public static List<? extends Image> getIcons() {
		List<Image> list = new ArrayList<Image>();
		list.add(IconUtils.getIcon("icon").getImage());
		list.add(IconUtils.getIcon("icon-big").getImage());

		return list;
	}
}
