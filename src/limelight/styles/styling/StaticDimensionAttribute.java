package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;

public class StaticDimensionAttribute extends SimpleIntegerAttribute implements DimensionAttribute
{
  public StaticDimensionAttribute(int pixels)
  {
    super(pixels);
  }

  public int getPixels()
  {
    return getValue();
  }
}
