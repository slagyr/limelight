//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

public class GreedyDimensionAttribute implements DimensionAttribute
{

  public boolean isAuto()
  {
    return false;
  }

  public boolean isDynamic()
  {
    return true;
  }

  public int calculateDimension(int consumableSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max, int greediness)
  {
    if(min.isNone())
      return greediness;
    else
      return min.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0) + greediness;
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    return currentSize;
  }
}