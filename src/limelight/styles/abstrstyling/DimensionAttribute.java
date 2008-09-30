package limelight.styles.abstrstyling;

public interface DimensionAttribute extends StyleAttribute
{
  boolean isAuto();
  boolean isPercentage();
  int calculateDimension(int consumableSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max);
  int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<IntegerAttribute> min, NoneableAttribute<IntegerAttribute> max);
}
