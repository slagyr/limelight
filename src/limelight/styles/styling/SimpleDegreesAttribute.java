package limelight.styles.styling;

import limelight.styles.abstrstyling.DegreesAttribute;

public class SimpleDegreesAttribute extends SimpleIntegerAttribute implements DegreesAttribute
{
  public SimpleDegreesAttribute(int degrees)
  {
    super(degrees);
  }

  public int getDegrees()
  {
    return getValue();
  }
}
