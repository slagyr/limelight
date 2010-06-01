//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.FillStrategyValue;
import limelight.styles.abstrstyling.XCoordinateValue;
import limelight.styles.abstrstyling.YCoordinateValue;

import java.awt.*;

public class RepeatYFillStrategyValue extends FillStrategyValue
{

  public String name()
  {
    return "repeat_y";
  }

  public void fill(Graphics2D graphics, Image image, XCoordinateValue xCoordinate, YCoordinateValue yCoordinate)
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
