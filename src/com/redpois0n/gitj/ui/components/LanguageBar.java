package com.redpois0n.gitj.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;

import com.redpois0n.gitj.Language;

@SuppressWarnings("serial")
public class LanguageBar extends JComponent {
	
	private List<Language> languages;
	
	public LanguageBar(List<Language> languages) {
		this.languages = languages;
		super.setPreferredSize(new Dimension(50, 20));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.white);
		g.drawRect(0, 0, getWidth(), getHeight());
		
		int max = 0;
		
		for (Language l : languages) {
			max += l.getLineCount();
		}
		
		int start = 0;
		
		for (Language l : languages) {
			g.setColor(l.getColor());
			
			int todraw = (int) (((float) l.getLineCount() / (float) max) * getWidth());
			
			g.fillRect(start, 0, todraw, getHeight());
			
			start += todraw;
		}
	}

}
