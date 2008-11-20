package limelight.styles.styling;

import limelight.styles.abstrstyling.PixelsAttribute;
import limelight.util.Box;

public class PercentagePixelsAttribute extends SimplePercentageAttribute implements PixelsAttribute
{
  public PercentagePixelsAttribute(double percentage)
  {
    super(percentage);
  }

  public int pixelsFor(int max)
  {
    return (int)(getPercentage() / 100.0 * max + 0.5);
  }

  public int pixelsFor(Box bounds)
  {
    return pixelsFor(Math.min(bounds.width, bounds.height));
  }
}
