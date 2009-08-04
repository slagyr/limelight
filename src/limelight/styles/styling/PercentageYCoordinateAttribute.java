package limelight.styles.styling;

import limelight.styles.abstrstyling.YCoordinateAttribute;
import limelight.util.Box;

public class PercentageYCoordinateAttribute extends SimplePercentageAttribute implements YCoordinateAttribute
{
  public PercentageYCoordinateAttribute(double percentage)
  {
    super(percentage);
  }

  public int getY(int consumed, Box area)
  {

    return (int) ((getPercentage() * 0.01) * (double) area.height) + area.y;
  }
}
