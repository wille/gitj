package gitj.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class RenderUtils {

	/**
	 * Creates circle with transparent background from image (and border)
	 * https://stackoverflow.com/questions/10852959/java-how-to-draw-a-transparent-shape-using-a-graphics-object-g
	 * @param targetg
	 * @param fill
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public static void drawCircle(Graphics2D targetg, Color fill, int x, int y, int w, int h) {
		BufferedImage i1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Ellipse2D.Double ellipse1 = new Ellipse2D.Double(w / 16, h / 16, 7 * w / 8, 7 * h / 8);
		Area circle = new Area(ellipse1);

		Graphics2D g = i1.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setClip(circle);
		g.setColor(fill);
		g.fillRect(0, 0, w, h);
		g.setClip(null);
		Stroke s = new BasicStroke(1);
		g.setStroke(s);
		g.setColor(Color.BLACK);
		g.draw(circle);
		g.dispose();

		targetg.drawImage(i1, x, y, w, h, null);
	}
}
