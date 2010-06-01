//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.PixelsValue;
import limelight.util.Box;

public class StaticPixelsValue extends SimpleIntegerValue implements PixelsValue
{
  public StaticPixelsValue(int value)
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
