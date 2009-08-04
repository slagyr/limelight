package limelight.styles.styling;

import limelight.styles.abstrstyling.YCoordinateAttribute;

import java.awt.*;

public class StaticYCoordinateAttribute extends SimpleIntegerAttribute implements YCoordinateAttribute
{
  public StaticYCoordinateAttribute(int value)
  {
    super(value);
  }

  public int getY(int consumed, Rectangle area)
  {
    return getValue() + area.y;
  }
}
