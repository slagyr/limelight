package limelight.styles.styling;

import limelight.styles.abstrstyling.PixelsAttribute;
import limelight.util.Box;

public class StaticPixelsAttribute extends SimpleIntegerAttribute implements PixelsAttribute
{
  public StaticPixelsAttribute(int value)
  {
    super(value);
  }

  public int getPixels()
  {
    return super.getValue();
  }

  public int pixelsFor(int max)
  {
    return getPixels();
  }

  public int pixelsFor(Box bounds)
  {
    return getPixels();
  }
}
