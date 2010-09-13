//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.*;
import limelight.styles.Style;
import limelight.util.Box;

import java.awt.*;

public class GradientPainter implements Painter
{
  public static GradientPainter instance = new GradientPainter();

  private GradientPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    Style style = panel.getStyle();
    Box r = panel.getBorderedBounds();
    Color color1 = style.getCompiledBackgroundColor().getColor();
    Color color2 = style.getCompiledSecondaryBackgroundColor().getColor();
    int angle = style.getCompiledGradientAngle().getDegrees();
    double penetration = style.getCompiledGradientPenetration().getPercentage();
    boolean cyclic = style.getCompiledCyclicGradient().isOn();
    int x1, y1, x2, y2;
    if(angle == 90)
    {
      x1 = x2 = r.width / 2;
      y1 = r.height;
      y2 = 0;
    }
    else if(angle == 270)
    {
      x1 = x2 = r.width / 2;
      y1 = 0;
      y2 = r.height;
    }
    else if(angle == 0)
    {
      y1 = y2 = r.height / 2;
      x1 = 0;
      x2 = r.width;
    }

    else if(angle == 180)
    {
      y1 = y2 = r.height / 2;
      x1 = r.width;
      x2 = 0;
    }
    else
    {
      double radians = Math.toRadians(angle);
      double x = Math.cos(radians);
      double y = Math.sin(radians);
      double a = y / x;
      double dx = Math.abs(r.height / a);
      double dy = Math.abs(r.width * a);

      if(x > 0)
      {
        x1 = (int)(r.width/2 - dx/2);
        x1 = Math.max(x1, 0);
        x2 = (int)(r.width/2 + dx/2);
        x2 = Math.min(x2, r.width);
      }
      else
      {
        x2 = (int)(r.width/2 - dx/2);
        x2 = Math.max(x2, 0);
        x1 = (int)(r.width/2 + dx/2);
        x1 = Math.min(x1, r.width);
      }

      if(y >= 0)
      {
        y1 = (int)(r.height/2 + dy/2);
        y1 = Math.min(r.height, y1);
        y2 = (int)(r.height/2 - dy/2);
        y2 = Math.max(0, y2);
      }
      else
      {
        y2 = (int)(r.height/2 + dy/2);
        y2 = Math.min(r.height, y2);
        y1 = (int)(r.height/2 - dy/2);
        y1 = Math.max(0, y1);
      }
    }

    int x3 = x1 + (int)((x2 - x1) * penetration * 0.01);
    int y3 = y1 + (int)((y2 - y1) * penetration * 0.01);

    graphics.setPaint(new GradientPaint(x1, y1, color1, x3, y3, color2, cyclic));

    Border border = panel.getBorderShaper();
    Shape insideBorder = border.getShapeInsideBorder();
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.fill(insideBorder);
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
  }
}
