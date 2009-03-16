//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.IntegerAttribute;
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

  public boolean isPercentage()
  {
    return false;
  }

  public int calculateDimension(int consumableSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max)
  {
    return getPixels();
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max)
  {
    return currentSize;
  }
}
