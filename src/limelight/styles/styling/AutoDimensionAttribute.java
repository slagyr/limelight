package limelight.styles.styling;

import limelight.styles.DimensionAttribute;

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
}
