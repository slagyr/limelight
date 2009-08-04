package limelight.styles.styling;

import limelight.styles.abstrstyling.YCoordinateAttribute;
import limelight.util.Box;

public class StaticYCoordinateAttribute extends SimpleIntegerAttribute implements YCoordinateAttribute
{
  public StaticYCoordinateAttribute(int value)
  {
    super(value);
  }

  public int getY(int consumed, Box area)
  {
    return getValue() + area.y;
  }
}
