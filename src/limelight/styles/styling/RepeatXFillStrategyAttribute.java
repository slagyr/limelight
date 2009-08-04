package limelight.styles.styling;

import limelight.styles.abstrstyling.FillStrategyAttribute;
import limelight.styles.abstrstyling.XCoordinateAttribute;
import limelight.styles.abstrstyling.YCoordinateAttribute;

import java.awt.*;

public class RepeatXFillStrategyAttribute extends FillStrategyAttribute
{

  public String name()
  {
    return "repeat_x";
  }

  public void fill(Graphics2D graphics, Image image, XCoordinateAttribute xCoordinate, YCoordinateAttribute yCoordinate)
  {
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    Rectangle area = graphics.getClipBounds();

    int xPaints = numberOfPaints(imageWidth, area.width);

    int startX = xCoordinate.getX(xPaints * imageWidth, area);
    int y = yCoordinate.getY(imageHeight, area);

    for(int x = startX; x < area.width; x += imageWidth)
    {
      graphics.drawImage(image, x, y, null);
    }
  }
}
