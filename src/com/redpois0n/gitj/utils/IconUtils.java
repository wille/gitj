package com.redpois0n.gitj.utils;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import com.redpois0n.git.Change;
import com.redpois0n.git.Diff;

public class IconUtils {
	
	public static final Map<String, ImageIcon> ICONS = new HashMap<String, ImageIcon>();

	public static ImageIcon getIcon(String path) {
		return getIcon(path, true);
	}
	
	public static ImageIcon getIcon(String path, boolean normal) {
		if (normal) {
			path = "/icons/" + path + ".png";
		}
		
		ImageIcon icon;
		
		if (ICONS.containsKey(path)) {
			icon = ICONS.get(path);
		} else {
			icon = new ImageIcon(IconUtils.class.getResource(path));
			ICONS.put(path, icon);
		}
		
		return icon;
	}

	public static Image getIconFromDiffType(Diff.Type type) {
		String s;
		
		if (type == Diff.Type.DELETED) {
			s = "deleted";
		} else if (type == Diff.Type.NEW) {
			s = "added";
		} else {
			s = "modified";
		}
		
		return getIcon(s).getImage();
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
		
		return getIcon(s);
	}
	
	public static Icon getFolderIcon() {
		return getFileIconFromExtension("", true);
	}
	
	public static Icon getFileIcon(File file) {
		return getFileIconFromExtension(file.getAbsolutePath(), false);
	}

	public static Icon getFileIconFromExtension(String f, boolean dir) {
		try {
			Icon icon;
			
			if (dir) {
				File temp = new File(System.getProperty("java.io.tmpdir") + "icon");
				temp.mkdirs();
				icon = FileSystemView.getFileSystemView().getSystemIcon(temp);
				temp.delete();
			} else {
				File temp = File.createTempFile((new Random()).nextInt() + "", f.substring(f.lastIndexOf("."), f.length()));
				icon = FileSystemView.getFileSystemView().getSystemIcon(temp);
				temp.delete();
			}	
			
			return icon;
		} catch (Exception ex) {
			return null;
		}
	}
}
