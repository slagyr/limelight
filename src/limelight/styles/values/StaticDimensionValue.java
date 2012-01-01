//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.values;

import limelight.styles.abstrstyling.DimensionValue;
import limelight.styles.abstrstyling.NoneableValue;

public class StaticDimensionValue extends SimpleIntegerValue implements DimensionValue
{
  public StaticDimensionValue(int pixels)
  {
    super(pixels);
  }

  public int getPixels()
  {
    return getValue();
  }

  public int calculateDimension(int consumableSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max, int greediness)
  {
    return getPixels();
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max)
  {
    return currentSize;
  }
}
