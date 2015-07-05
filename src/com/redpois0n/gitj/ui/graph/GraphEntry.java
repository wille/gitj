package com.redpois0n.gitj.ui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.redpois0n.git.Commit;
import com.redpois0n.gitj.utils.RenderUtils;

public class GraphEntry {

	public static final int BALL_DIAMETER = 12;
	public static final int SPACE = 12;

	private GitGraph parent;
	private List<String> list = new ArrayList<String>();
	private Commit commit;

	public GraphEntry(GitGraph parent, String graphData, Commit c) {
		this.parent = parent;
		list.add(graphData);
		this.commit = c;
	}

	public BufferedImage render(int colorIndex, int height) {
		BufferedImage image = new BufferedImage(100, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		int location = SPACE;

		int depth = 0;

		for (String s : list) {
			if (s.length() > depth) {
				depth = s.length();
			}
		}

		g.setStroke(new BasicStroke(2));

		String str = list.get(colorIndex);

		int realChar = 0; // counts chars draw
		String realString = "";
		for (int s = 0; s < str.length(); s++) {
			char c = str.charAt(s);

			if (c == '[') {
				if (str.charAt(s + 1) != 'm' && s + 3 >= str.length()) {
					g.setColor(GitGraph.DEFAULT_COLOR);
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append(c);
					char first = str.charAt(++s);

					if (first != 'm') {
						sb.append(first);
						sb.append(str.charAt(++s));
						sb.append(str.charAt(++s));

						g.setColor(GitGraph.COLORS.get(sb.toString()));
						if (realChar < parent.getLatestColors().length) {
							parent.getLatestColors()[realChar] = g.getColor();
						}
					}
				}		
			} else if (realChar < parent.getLatestColors().length) {
				g.setColor(parent.getLatestColors()[realChar]);
			}
			
			boolean drawn = c == '*' || c == '|' || c == '/' || c == '\\';

			if (drawn) {
				realChar++;
				realString += c;
			}
			
			g.setColor(Color.red); // debug

			if (c == '*') {
				RenderUtils.drawCircle(g, g.getColor(), location - BALL_DIAMETER / 2 + 1, height / 2 - BALL_DIAMETER / 2 + 1, BALL_DIAMETER, BALL_DIAMETER);
			}

			if (drawn) {
				if (c == '*' || c == '|') {
					g.drawLine(location, 0, location, height);
				} else if (c == '/') {
					g.drawLine(location, 0, location - SPACE, height);
					//g.drawLine(location - SPACE, height / 2, location - SPACE, height);
				} else if (c == '\\') {
					g.drawLine(location - SPACE, 0, location, height);
					//g.drawLine(location - SPACE, 0, location - SPACE, height / 2);
				}
			}
			
			if (drawn) {
				location += SPACE;
			}
		}

		return image;
	}

	public ImageIcon renderIcon(int colorIndex, int height) {
		return new ImageIcon(render(colorIndex, height));
	}

	public Commit getCommit() {
		return this.commit;
	}

	public void addData(String graphData) {
		this.list.add(graphData);
	}

	public List<String> getData() {
		return this.list;
	}

}
