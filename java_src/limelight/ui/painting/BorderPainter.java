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
    Border border = panel.getBorderShaper();

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
  }
}
