package limelight;

import java.awt.*;

public class BorderPainter extends Painter
{
  public BorderPainter(Panel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
  {
    Style style = getStyle();
    Pen pen = new Pen(graphics);

		Rectangle r = panel.getRectangleInsideMargins();
		r.shave(resolveInt(style.getTopBorderWidth()) / 2, resolveInt(style.getRightBorderWidth()) / 2 + 1, resolveInt(style.getBottomBorderWidth()) / 2 + 1, resolveInt(style.getLeftBorderWidth()) / 2);

		if(resolveInt(style.getTopBorderWidth()) > 0)
			pen.withColor(Colors.resolve(style.getTopBorderColor())).withStroke(resolveInt(style.getTopBorderWidth())).drawLine(r.left(), r.top(),r. right(), r.top());
		if(resolveInt(style.getRightBorderWidth()) > 0)
			pen.withColor(Colors.resolve(style.getRightBorderColor())).withStroke(resolveInt(style.getRightBorderWidth())).drawLine(r.right(), r.top(), r.right(), r.bottom());
		if(resolveInt(style.getBottomBorderWidth()) > 0)
			pen.withColor(Colors.resolve(style.getBottomBorderColor())).withStroke(resolveInt(style.getBottomBorderWidth())).drawLine(r.right(), r.bottom(), r.left(), r.bottom());
		if(resolveInt(style.getLeftBorderWidth()) > 0)
			pen.withColor(Colors.resolve(style.getLeftBorderColor())).withStroke(resolveInt(style.getLeftBorderWidth())).drawLine(r.left(), r.bottom(), r.left(), r.top());
  }
}
