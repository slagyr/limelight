package limelight.ui;

import java.awt.*;

public class BorderPainter extends Painter
{
  public BorderPainter(BlockPanel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
  {
    Style style = getStyle();
    Pen pen = new Pen(graphics);

    limelight.Rectangle r = panel.getRectangleInsideMargins();
    r.shave(resolveInt(style.getTopBorderWidth()) / 2, resolveInt(style.getRightBorderWidth()) / 2 + 1, resolveInt(style.getBottomBorderWidth()) / 2 + 1, resolveInt(style.getLeftBorderWidth()) / 2);

    int topWidth = resolveInt(style.getTopBorderWidth());
    int rightWidth = resolveInt(style.getRightBorderWidth());
    int bottomWidth = resolveInt(style.getBottomBorderWidth());
    int leftWidth = resolveInt(style.getLeftBorderWidth());

    int top = r.top();
    int right = r.right();
    int bottom = r.bottom();
    int left = r.left();

    if(rightWidth > 0 && rightWidth % 2 == 0)
      right += 1;
    if(bottomWidth > 0 && bottomWidth % 2 == 0)
      bottom += 1;

    if(topWidth > 0)
			pen.withColor(Colors.resolve(style.getTopBorderColor())).withStroke(topWidth).drawLine(left, top, right, top);
    if(rightWidth > 0)
      pen.withColor(Colors.resolve(style.getRightBorderColor())).withStroke(rightWidth).drawLine(right, top, right, bottom);
    if(bottomWidth > 0)
      pen.withColor(Colors.resolve(style.getBottomBorderColor())).withStroke(bottomWidth).drawLine(right, bottom, left, bottom);
    if(leftWidth > 0)
			pen.withColor(Colors.resolve(style.getLeftBorderColor())).withStroke(leftWidth).drawLine(left, bottom, left, top);
  }
}

