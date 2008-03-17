package limelight.ui.painting;

import limelight.ui.*;
import limelight.ui.Panel;

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
    Border border = new Border(style, panel.getRectangleInsideMargins());

    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if(border.hasTopBorder())
      pen.withColor(Colors.resolve(style.getTopBorderColor())).withStroke(border.getTopWidth()).draw(border.getTopLine());
    if(border.hasTopRightCorner())
      pen.withColor(Colors.resolve(style.getTopRightBorderColor())).withStroke(border.getTopRightWidth()).draw(border.getTopRightArc());
    if(border.hasRightBorder())
      pen.withColor(Colors.resolve(style.getRightBorderColor())).withStroke(border.getRightWidth()).draw(border.getRightLine());
    if(border.hasBottomRightCorner())
      pen.withColor(Colors.resolve(style.getBottomRightBorderColor())).withStroke(border.getBottomRightWidth()).draw(border.getBottomRightArc());
    if(border.hasBottomBorder())
      pen.withColor(Colors.resolve(style.getBottomBorderColor())).withStroke(border.getBottomWidth()).draw(border.getBottomLine());
    if(border.hasBottomLeftCorner())
      pen.withColor(Colors.resolve(style.getBottomLeftBorderColor())).withStroke(border.getBottomLeftWidth()).draw(border.getBottomLeftArc());
    if(border.hasLeftBorder())
      pen.withColor(Colors.resolve(style.getLeftBorderColor())).withStroke(border.getLeftWidth()).draw(border.getLeftLine());
    if(border.hasTopLeftCorner())
      pen.withColor(Colors.resolve(style.getTopLeftBorderColor())).withStroke(border.getTopLeftWidth()).draw(border.getTopLeftArc());
    
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

//    limelight.ui.Rectangle r = panel.getRectangleInsideMargins();
//
//    int topWidth = resolveInt(style.getTopBorderWidth());
//    int rightWidth = resolveInt(style.getRightBorderWidth());
//    int bottomWidth = resolveInt(style.getBottomBorderWidth());
//    int leftWidth = resolveInt(style.getLeftBorderWidth());
//
//    shaveOffHalfBorderWidth(r, topWidth, rightWidth, bottomWidth, leftWidth);
//
//    int top = r.top();
//    int right = r.right() + 1;
//    int bottom = r.bottom() + 1;
//    int left = r.left();
//
//    if(topWidth > 0)
//			pen.withColor(Colors.resolve(style.getTopBorderColor())).withStroke(topWidth).drawLine(left, top, right, top);
//    if(rightWidth > 0)
//      pen.withColor(Colors.resolve(style.getRightBorderColor())).withStroke(rightWidth).drawLine(right, top, right, bottom);
//    if(bottomWidth > 0)
//      pen.withColor(Colors.resolve(style.getBottomBorderColor())).withStroke(bottomWidth).drawLine(right, bottom, left, bottom);
//    if(leftWidth > 0)
//			pen.withColor(Colors.resolve(style.getLeftBorderColor())).withStroke(leftWidth).drawLine(left, bottom, left, top);
  }

  private void shaveOffHalfBorderWidth(limelight.ui.Rectangle r, int topWidth, int rightWidth, int bottomWidth, int leftWidth)
  {
    int rightShave = rightWidth / 2 + rightWidth % 2;
    int bottomShave = bottomWidth / 2 + bottomWidth % 2;
    r.shave(topWidth / 2, rightShave, bottomShave, leftWidth / 2);
  }
}
