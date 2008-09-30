package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.IntegerAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

public class PercentageDimensionAttribute extends SimplePercentageAttribute implements DimensionAttribute
{

  public PercentageDimensionAttribute(int percentValue)
  {
    super(percentValue);
  }

  public boolean isAuto()
  {
    return false;
  }

  public boolean isPercentage()
  {
    return true;
  }

  public int calculateDimension(int consumableSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max)
  {
    int calculatedSize = (int) ((getPercentage() * 0.01) * (double) consumableSize);

    if(!max.isNone())
      calculatedSize = Math.min(calculatedSize, max.getAttribute().getValue());
    if(!min.isNone())
      calculatedSize = Math.max(calculatedSize, min.getAttribute().getValue());

    return calculatedSize;
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max)
  {
    return currentSize;
  }
}
