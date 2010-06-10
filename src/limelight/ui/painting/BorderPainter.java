//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.*;
import limelight.styles.Style;

import java.awt.*;

public class BorderPainter implements Painter
{
  public static Painter instance = new BorderPainter();

  private BorderPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    Style style = panel.getStyle();
    Pen pen = new Pen(graphics);
    Border border = panel.getBorderShaper();

    if(border.hasTopBorder())
      pen.withColor(style.getCompiledTopBorderColor().getColor()).withStroke(border.getTopWidth()).withAntialiasing(false).draw(border.getTopLine());
    if(border.hasTopRightCorner())
      pen.withColor(style.getCompiledTopRightBorderColor().getColor()).withStroke(border.getTopRightWidth()).withAntialiasing(true).draw(border.getTopRightArc());
    if(border.hasRightBorder())
      pen.withColor(style.getCompiledRightBorderColor().getColor()).withStroke(border.getRightWidth()).withAntialiasing(false).draw(border.getRightLine());
    if(border.hasBottomRightCorner())
      pen.withColor(style.getCompiledBottomRightBorderColor().getColor()).withStroke(border.getBottomRightWidth()).withAntialiasing(true).draw(border.getBottomRightArc());
    if(border.hasBottomBorder())
      pen.withColor(style.getCompiledBottomBorderColor().getColor()).withStroke(border.getBottomWidth()).withAntialiasing(false).draw(border.getBottomLine());
    if(border.hasBottomLeftCorner())
      pen.withColor(style.getCompiledBottomLeftBorderColor().getColor()).withStroke(border.getBottomLeftWidth()).withAntialiasing(true).draw(border.getBottomLeftArc());
    if(border.hasLeftBorder())
      pen.withColor(style.getCompiledLeftBorderColor().getColor()).withStroke(border.getLeftWidth()).withAntialiasing(false).draw(border.getLeftLine());
    if(border.hasTopLeftCorner())
      pen.withColor(style.getCompiledTopLeftBorderColor().getColor()).withStroke(border.getTopLeftWidth()).withAntialiasing(true).draw(border.getTopLeftArc());
  }
}
