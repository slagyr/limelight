//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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

    if(border.hasTopBorder())
      pen.withColor(Colors.resolve(style.getTopBorderColor())).withStroke(border.getTopWidth()).withAntialiasing(false).draw(border.getTopLine());
    if(border.hasTopRightCorner())
      pen.withColor(Colors.resolve(style.getTopRightBorderColor())).withStroke(border.getTopRightWidth()).withAntialiasing(true).draw(border.getTopRightArc());
    if(border.hasRightBorder())
      pen.withColor(Colors.resolve(style.getRightBorderColor())).withStroke(border.getRightWidth()).withAntialiasing(false).draw(border.getRightLine());
    if(border.hasBottomRightCorner())
      pen.withColor(Colors.resolve(style.getBottomRightBorderColor())).withStroke(border.getBottomRightWidth()).withAntialiasing(true).draw(border.getBottomRightArc());
    if(border.hasBottomBorder())
      pen.withColor(Colors.resolve(style.getBottomBorderColor())).withStroke(border.getBottomWidth()).withAntialiasing(false).draw(border.getBottomLine());
    if(border.hasBottomLeftCorner())
      pen.withColor(Colors.resolve(style.getBottomLeftBorderColor())).withStroke(border.getBottomLeftWidth()).withAntialiasing(true).draw(border.getBottomLeftArc());
    if(border.hasLeftBorder())
      pen.withColor(Colors.resolve(style.getLeftBorderColor())).withStroke(border.getLeftWidth()).withAntialiasing(false).draw(border.getLeftLine());
    if(border.hasTopLeftCorner())
      pen.withColor(Colors.resolve(style.getTopLeftBorderColor())).withStroke(border.getTopLeftWidth()).withAntialiasing(true).draw(border.getTopLeftArc());
  }
}
