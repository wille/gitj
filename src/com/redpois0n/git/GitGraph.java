package com.redpois0n.git;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import gitj.utils.RenderUtils;

public class GitGraph {

	public static final String DEFAULT_GIT_COLOR = "[33m";
	public static final Color DEFAULT_COLOR = Color.black;

	public static final Map<String, Color> COLORS = new HashMap<String, Color>();

	static {
		COLORS.put("30m", Color.black);
		COLORS.put("31m", Color.red.darker());
		COLORS.put("32m", Color.green.darker());
		COLORS.put("33m", Color.yellow.darker());
		COLORS.put("34m", Color.blue);
		COLORS.put("35m", Color.magenta.darker());
		COLORS.put("36m", Color.cyan.darker());
		COLORS.put("37m", Color.white);
	}

	public static final String ANSI_RESET = "\u001B[0m";

	public static final int BALL_DIAMETER = 12;
	public static final int SPACE = 6;

	private int depth;
	private int height;
	private List<String> list = new ArrayList<String>();

	private BufferedImage image;

	public GitGraph() {

	}

	public GitGraph(int depth, int height, List<String> list) {
		this.depth = depth;
		this.height = height;
		this.list = list;
	}

	public BufferedImage getImage(int y) {
		BufferedImage i = new BufferedImage(depth * SPACE + SPACE, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = i.createGraphics();
		g.drawImage(image, 0, 0, i.getWidth(), i.getHeight(), 0, y, depth * SPACE + SPACE, y + height, null);
		return i;
	}

	public ImageIcon getIcon(int y) {
		return new ImageIcon(getImage(y));
	}

	public BufferedImage render() {
		image = new BufferedImage(100, height * list.size(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int location = SPACE;

		int depth = 0;

		for (String s : list) {
			if (s.length() > depth) {
				depth = s.length();
			}
		}

		g.setStroke(new BasicStroke(2));

		int y = 0;
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);

			for (int s = 0; s < str.length(); s++) {
				char c = str.charAt(s);

				if (c == '[') {
					if (str.charAt(s + 1) != 'm' && s + 3 >= str.length()) {
						g.setColor(GitGraph.DEFAULT_COLOR);
					} else {
						StringBuilder sb = new StringBuilder();

						char first = str.charAt(++s);

						if (first != 'm') {
							sb.append(first);
							sb.append(str.charAt(++s));
							sb.append(str.charAt(++s));

							g.setColor(GitGraph.COLORS.get(sb.toString()));
						}
					}
				}

				boolean drawn = c == '*' || c == '|' || c == '/' || c == '\\' || c == ' ';

				if (drawn) {
					if (c == '*' || c == '|') {
						g.drawLine(location, y, location, y + height);
					} else if (c == '/') {
						g.drawLine(location + SPACE, y, location - SPACE, y + height);
					} else if (c == '\\') {
						g.drawLine(location - SPACE, y, location + SPACE, y + height);
					}
				}
				
				if (c == '*') {
					RenderUtils.drawCircle(g, g.getColor(), location - BALL_DIAMETER / 2 + 1, y + height / 2 - BALL_DIAMETER / 2 + 1, BALL_DIAMETER, BALL_DIAMETER);
				}

				if (drawn) {
					location += SPACE;
				}
			}

			y += height;
			location = SPACE;
		}

		return image;

	}
	
	public String fix(String s) {
		String s1 = "";
		
		for (char c : s.toCharArray()) {
			boolean drawn = c == '*' || c == '|' || c == '/' || c == '\\' || c == ' ';
		
			if (drawn) {
				s1 += c;
			}
		}
		
		return s1;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void add(String s) {
		this.list.add(s);
	}
}
