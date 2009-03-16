//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
