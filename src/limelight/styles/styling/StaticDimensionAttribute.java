package limelight.styles.styling;

import limelight.styles.DimensionAttribute;

public class StaticDimensionAttribute extends IntegerAttribute implements DimensionAttribute
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
