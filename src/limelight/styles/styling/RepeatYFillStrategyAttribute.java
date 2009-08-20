//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.FillStrategyAttribute;
import limelight.styles.abstrstyling.XCoordinateAttribute;
import limelight.styles.abstrstyling.YCoordinateAttribute;

import java.awt.*;

public class RepeatYFillStrategyAttribute extends FillStrategyAttribute
{

  public String name()
  {
    return "repeat_y";
  }

  public void fill(Graphics2D graphics, Image image, XCoordinateAttribute xCoordinate, YCoordinateAttribute yCoordinate)
  {
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    Rectangle area = graphics.getClipBounds();

    int yPaints = numberOfPaints(imageHeight, area.height);

    int x = xCoordinate.getX(imageWidth, area);
    int startY = yCoordinate.getY(yPaints * imageHeight, area);

    for(int y = startY; y < area.height; y += imageHeight)
    {
      graphics.drawImage(image, x, y, null);
    }
  }
}
