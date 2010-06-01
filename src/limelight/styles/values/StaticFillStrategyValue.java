//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.FillStrategyValue;
import limelight.styles.abstrstyling.XCoordinateValue;
import limelight.styles.abstrstyling.YCoordinateValue;

import java.awt.*;

public class StaticFillStrategyValue extends FillStrategyValue
{

  public String name()
  {
    return "static";
  }

  public void fill(Graphics2D graphics, Image image, XCoordinateValue xCoordinate, YCoordinateValue yCoordinate)
  {
    Rectangle bounds = graphics.getClipBounds();
    int x = xCoordinate.getX(image.getWidth(null), bounds);
    int y = yCoordinate.getY(image.getHeight(null), bounds);
    graphics.drawImage(image, x, y, null);
//		Graphics innerGraphics = graphics.create(area.x, area.y, area.width, area.height);
//		innerGraphics.drawImage(image, 0, 0, null);
  }
}
