//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.*;
import limelight.ui.Panel;
import limelight.ui.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BackgroundPainter extends Painter
{
  public BackgroundPainter(Panel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
  {
    Style style = getStyle();
    Border border = panel.getBorderShaper();
    Shape insideBorder = border.getShapeInsideBorder();

    if(style.isOn(style.getGradient()))
      new GradientPainter(panel).paint(graphics);
    else if(!"transparent".equals(style.getBackgroundColor()))
    {
      Color color = Colors.resolve(style.getBackgroundColor());
      graphics.setColor(color);

      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.fill(insideBorder);
      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    if (!"none".equals(style.getBackgroundImage()))
    {
      try
      {
        Rectangle borderFrame = panel.getRectangleInsideBorders();
        String imageFilename = panel.getProp().getScene().getLoader().pathTo(style.getBackgroundImage());
        Image image = ImageIO.read(new File(imageFilename));
        Graphics2D borderedGraphics = (Graphics2D) graphics.create(borderFrame.x, borderFrame.y, borderFrame.width, borderFrame.height);
        ImageFillStrategies.get(style.getBackgroundImageFillStrategy()).fill(borderedGraphics, image);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
