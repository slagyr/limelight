//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.abstrstyling;


public interface DimensionValue extends StyleValue
{
  NoneableValue<DimensionValue> DIMENSION_NONE = new NoneableValue<DimensionValue>(null);

  int calculateDimension(int consumableSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max, int greediness);
  int collapseExcess(int currentSize, int consumedSize, NoneableValue<DimensionValue> min, NoneableValue<DimensionValue> max);
}
