//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.IntegerAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

public class AutoDimensionAttribute implements DimensionAttribute
{
  public String toString()
  {
    return "auto";
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof AutoDimensionAttribute)
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

  public int calculateDimension(int consumableSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    if(max.isNone())
      return consumableSize;
    else
      return Math.min(consumableSize, max.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE));
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    int size = consumedSize;
    if(!min.isNone())
      size = Math.max(size, min.getAttribute().calculateDimension(consumedSize, DIMENSION_NONE, DIMENSION_NONE));
    if(!max.isNone())
      size = Math.min(size, max.getAttribute().calculateDimension(consumedSize, DIMENSION_NONE, DIMENSION_NONE));

    return size;
  }
}
