//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.DimensionValue;
import limelight.styles.abstrstyling.NoneableValue;

public class AutoDimensionValue implements DimensionValue
{
  public String toString()
  {
    return "auto";
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof AutoDimensionValue)
      return true;
    else
      return false;
  }

  public boolean isAuto()
  {
    return true;
  }

  public boolean isDynamic()
  {
    return true;
  }

  public int calculateDimension(int consumableSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max, int greediness)
  {
    if(max.isNone())
      return consumableSize;
    else
      return Math.min(consumableSize, max.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0));
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max)
  {
    int size = consumedSize;
    if(!min.isNone())
      size = Math.max(size, min.getAttribute().calculateDimension(consumedSize, DIMENSION_NONE, DIMENSION_NONE, 0));
    if(!max.isNone())
      size = Math.min(size, max.getAttribute().calculateDimension(consumedSize, DIMENSION_NONE, DIMENSION_NONE, 0));

    return size;
  }
}
