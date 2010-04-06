//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

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

  public boolean isAuto()
  {
    return false;
  }

  public boolean isDynamic()
  {
    return false;
  }

  public int calculateDimension(int consumableSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max, int greediness)
  {
    return getPixels();
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    return currentSize;
  }
}
