package limelight.styles.styling;

import limelight.styles.abstrstyling.XCoordinateAttribute;
import limelight.util.Box;

public class StaticXCoordinateAttribute extends SimpleIntegerAttribute implements XCoordinateAttribute
{
  public StaticXCoordinateAttribute(int value)
  {
    super(value);
  }

  public int getX(int consumed, Box area)
  {
    return getValue() + area.x;
  }
}
