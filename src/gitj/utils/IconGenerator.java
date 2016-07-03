package gitj.utils;

import iconlib.IconUtils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import git.Tag;
import gitj.ui.components.DiffPanel;

public class IconGenerator {
	
	private static FontMetrics fontMetrics;
	
	/**
	 * Creates one icon from multiple tag icons
	 * @param tags
	 * @return
	 */
	public static ImageIcon getTagIcons(List<Tag> tags) {
		List<ImageIcon> icons = new ArrayList<ImageIcon>();
		
		int width = 0;
		int height = 0;
		
		for (Tag tag : tags) {
			ImageIcon icon = getTagIcon(tag);
			width += icon.getIconWidth() + 2;
			
			if (height == 0) {
				height = icon.getIconHeight();
			}
			
			icons.add(icon);
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int pos = 0;
		
		for (int i = 0; i < icons.size(); i++) {
			ImageIcon icon = icons.get(i);
			
			image.getGraphics().drawImage(icon.getImage(), pos, 0, null);
			
			pos += icon.getIconWidth() + 2;
		}
		
		return new ImageIcon(image);
	}
	
	/**
	 * Gets commit table tag icon
	 * @param tag
	 * @return
	 */
	public static ImageIcon getTagIcon(Tag tag) {		
		FontMetrics f = getFontMetrics();
		
		int height = f.getHeight() + 4;
		int width = f.stringWidth(tag.getTag()) + 22;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.setColor(DiffPanel.COLOR_PANEL);
		g.fillRect(0, 0, width, height);
		
		g.setColor(DiffPanel.COLOR_PANEL_BORDER);
		g.drawRect(0, 0, width - 1, height - 1);
		
		g.setColor(Color.black);
		g.drawString(tag.getTag(), 20, height - 4);
		
		String icon = tag.getType() == Tag.Type.ANNOTATED ? "tag-annotated" : "tag-normal";
		
		g.drawImage(IconUtils.getIcon(icon).getImage(), 2, 2, null);
		
		g.dispose();
		
		return new ImageIcon(image);
	}
	
	public static FontMetrics getFontMetrics() {		
		if (fontMetrics == null) {
			fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics();
		}
		
		return fontMetrics;
	}

}
