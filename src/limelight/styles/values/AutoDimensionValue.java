//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
    return obj instanceof AutoDimensionValue;
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
