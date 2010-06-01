//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.FillStrategyValue;
import limelight.styles.abstrstyling.XCoordinateValue;
import limelight.styles.abstrstyling.YCoordinateValue;

import java.awt.*;

public class ScaleXFillStrategyValue extends FillStrategyValue
{

  public String name()
  {
    return "scale_x";
  }

  public void fill(Graphics2D graphics, Image image, XCoordinateValue xCoordinate, YCoordinateValue yCoordinate)
  {
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    Rectangle area = graphics.getClipBounds();
    int x = xCoordinate.getX(area.width, area);
    int y = yCoordinate.getY(imageHeight, area);

    graphics.drawImage(image, x, y, x + area.width, y + imageHeight,  0, 0, imageWidth, imageHeight, null);
  }
}
