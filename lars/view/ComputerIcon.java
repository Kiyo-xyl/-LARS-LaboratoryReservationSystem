package lars.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/** A simple desktop-computer symbol drawn with Java 2D, so no image files are needed. */
public class ComputerIcon implements Icon {

	private final int size;
	private final Color color;

	public ComputerIcon(int size, Color color) {
		this.size = size;
		this.color = color;
	}

	@Override
	public int getIconWidth() {
		return size;
	}

	@Override
	public int getIconHeight() {
		return size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);

		float stroke = Math.max(2f, size / 16f);
		g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		// monitor screen
		int screenHeight = (int) (size * 0.65);
		int inset = (int) (stroke / 2) + 1;
		g2.drawRoundRect(x + inset, y + inset, size - 2 * inset, screenHeight - 2 * inset, size / 8, size / 8);

		// a little ">_" prompt on the screen
		g2.drawLine(x + size / 5, y + screenHeight / 3, x + size * 3 / 10, y + screenHeight / 2);
		g2.drawLine(x + size * 3 / 10, y + screenHeight / 2, x + size / 5, y + screenHeight * 2 / 3);
		g2.drawLine(x + size * 2 / 5, y + screenHeight * 2 / 3, x + size * 3 / 5, y + screenHeight * 2 / 3);

		// stand and base
		int standWidth = size / 8;
		g2.fillRect(x + (size - standWidth) / 2, y + screenHeight, standWidth, size / 10);
		int baseWidth = size / 2;
		g2.fillRoundRect(x + (size - baseWidth) / 2, y + screenHeight + size / 10, baseWidth, size / 12, 6, 6);

		g2.dispose();
	}
}