//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.DimensionValue;
import limelight.styles.abstrstyling.NoneableValue;

public class PercentageDimensionValue extends SimplePercentageValue implements DimensionValue
{
  public PercentageDimensionValue(double percentValue)
  {
    super(percentValue);
  }

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
    int calculatedSize = (int) ((getPercentage() * 0.01) * (double) consumableSize);

    if(!max.isNone())
      calculatedSize = Math.min(calculatedSize, max.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0));
    if(!min.isNone())
      calculatedSize = Math.max(calculatedSize, min.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0));

    return calculatedSize;
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max)
  {
    return currentSize;
  }
}
