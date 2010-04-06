//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;


public interface DimensionAttribute extends StyleAttribute
{
  NoneableAttribute<DimensionAttribute> DIMENSION_NONE = new NoneableAttribute<DimensionAttribute>(null);

  boolean isAuto();
  boolean isDynamic();
  int calculateDimension(int consumableSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max, int greediness);
  int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max);
}
