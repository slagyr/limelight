package limelight;

import java.awt.*;
import java.awt.geom.Line2D;

public class Pen
{
	private Graphics2D graphics;

	public Pen(Graphics graphics)
	{
		this.graphics = (Graphics2D)graphics;
	}

	public void drawLine(double x1, double y1, double x2, double y2)
	{
		graphics.draw(new Line2D.Double(x1, y1, x2, y2));
	}

	public Pen withColor(Color color)
	{
		graphics.setColor(color);
		return this;
	}

	public Pen withStroke(float width)
	{
		graphics.setStroke(new BasicStroke(width));
		return this;
	}

	public Color getColor()
	{
		return graphics.getColor();
	}

	public BasicStroke getStroke()
	{
		return (BasicStroke)graphics.getStroke();
	}
}
