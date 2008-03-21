package limelight.ui.painting;

import limelight.ui.*;
import limelight.ui.Panel;

import java.awt.*;

public class GradientPainter extends Painter
{
  public GradientPainter(Panel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
  {
    Style style = getStyle();
    limelight.ui.Rectangle r = panel.getRectangleInsideBorders();
    Color color1 = Colors.resolve(style.getBackgroundColor());
    Color color2 = Colors.resolve(style.getSecondaryBackgroundColor());
    int angle = Integer.parseInt(style.getGradientAngle());
    int penetration = Integer.parseInt(style.getGradientPenetration());
    boolean cyclic = "true".equals(style.getCyclicGradient());
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
