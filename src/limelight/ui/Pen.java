//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.awt.*;
import java.awt.geom.Line2D;

public class Pen
{
	private final Graphics2D graphics;

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

  public void draw(Shape shape)
  {
    graphics.draw(shape);
  }

  public Pen withAntialiasing(boolean on)
  {
    Object value = on ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF;
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, value);
    return this;
  }
}
