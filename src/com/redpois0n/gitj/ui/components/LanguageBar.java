package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JComponent;

import com.redpois0n.gitj.Language;

@SuppressWarnings("serial")
public class LanguageBar extends JComponent {
	
	public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 12);
	public static final Font FONT_NORMAL = new Font("Arial", Font.PLAIN, 12);

	private List<Language> languages;
	
	public LanguageBar(List<Language> languages) {
		this.languages = languages;
		super.setPreferredSize(new Dimension(50, 40));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		super.paintComponent(g);
		
		g.setColor(Color.white);
		g.drawRect(0, 0, getWidth(), getHeight());
		
		int max = 0;
		
		for (Language l : languages) {
			max += l.getLineCount();
		}
		
		int startText = 10;
		int start = 0;
		
		for (Language l : languages) {
			String s = l.getLanguage();
			String percent = String.format("%.2g%n", (((float) l.getLineCount() / (float) max) * 100)) + "%";
			
			g.setColor(Color.black);
			g.setFont(FONT_NORMAL);
			g.drawString(s, startText + 15 + g.getFontMetrics().stringWidth(percent), 5 + g.getFontMetrics().getHeight());
			g.setFont(FONT_BOLD);
			g.drawString(percent, startText + 15, 5 + g.getFontMetrics().getHeight());
			
			if (l.isOther()) {
				g.setColor(Color.lightGray);
			} else {
				g.setColor(l.getColor());
			}
			
			g.fillRect(startText, 10, 10, 10);
			
			startText += g.getFontMetrics().stringWidth(s) + 25;
			startText += g.getFontMetrics().stringWidth(percent);
			
			int todraw = (int) (((float) l.getLineCount() / (float) max) * getWidth());
			
			g.fillRect(start, getHeight() - 10, todraw, 10);
			
			start += todraw;
		}
	}

}
