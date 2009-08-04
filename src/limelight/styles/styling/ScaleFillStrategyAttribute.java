package limelight.styles.styling;

import limelight.styles.abstrstyling.FillStrategyAttribute;
import limelight.styles.abstrstyling.XCoordinateAttribute;
import limelight.styles.abstrstyling.YCoordinateAttribute;

import java.awt.*;

public class ScaleFillStrategyAttribute extends FillStrategyAttribute
{

  public String name()
  {
    return "scale";
  }

  public void fill(Graphics2D graphics, Image image, XCoordinateAttribute xCoordinate, YCoordinateAttribute yCoordinate)
  {
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    Rectangle area = graphics.getClipBounds();
    int x = xCoordinate.getX(area.width, area);
    int y = yCoordinate.getY(area.height, area);

    graphics.drawImage(image, x, y, x + area.width, y + area.height,  0, 0, imageWidth, imageHeight, null);
  }
}
