package com.redpois0n.gitj.utils;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

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
}
