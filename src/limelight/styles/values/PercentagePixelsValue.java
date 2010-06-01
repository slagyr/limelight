//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.PixelsValue;
import limelight.util.Box;

public class PercentagePixelsValue extends SimplePercentageValue implements PixelsValue
{
  public PercentagePixelsValue(double percentage)
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
