//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.DimensionValue;
import limelight.styles.abstrstyling.NoneableValue;

public class GreedyDimensionValue implements DimensionValue
{

  public boolean isAuto()
  {
    return false;
  }

  public boolean isDynamic()
  {
    return true;
  }

  public int calculateDimension(int consumableSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max, int greediness)
  {
    if(min.isNone())
      return greediness;
    else
      return min.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0) + greediness;
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max)
  {
    return currentSize;
  }
}