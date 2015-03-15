package com.redpois0n.gitj.utils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.redpois0n.git.Tag;
import com.redpois0n.gitj.ui.components.DiffPanel;

public class IconGenerator {
	
	private static FontMetrics fontMetrics;
	
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
