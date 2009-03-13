//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

public interface DimensionAttribute extends StyleAttribute
{
  boolean isAuto();
  boolean isPercentage();
  int calculateDimension(int consumableSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max);
  int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max);
}
