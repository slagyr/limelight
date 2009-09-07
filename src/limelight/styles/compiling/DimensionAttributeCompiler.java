//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.styling.StaticDimensionAttribute;
import limelight.styles.styling.AutoDimensionAttribute;
import limelight.styles.styling.PercentageDimensionAttribute;
import limelight.styles.styling.GreedyDimensionAttribute;

public class DimensionAttributeCompiler extends StyleAttributeCompiler
{ 
  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      DimensionAttribute attribute;
      attribute = attemptAutoAttribute(value);
      if(attribute == null)
        attribute = attemptGreedyAttribute(value);
      if(attribute == null)
        attribute = attemptPercentageAttribute(value);
      if(attribute == null)
        attribute = attemptStaticAttribute(value);

      if(attribute != null)
        return attribute;
      else
        throw makeError(value);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }

  private DimensionAttribute attemptGreedyAttribute(String value)
  {
    if("greedy".equals(value.toLowerCase()))
      return new GreedyDimensionAttribute();
    else
      return null;
  }

  protected DimensionAttribute attemptStaticAttribute(String value)
  {
    int intValue = IntegerAttributeCompiler.convertToInt(value);

    if(intValue >= 0)
      return new StaticDimensionAttribute(intValue);
    else
      return null;
  }

  private DimensionAttribute attemptAutoAttribute(String value)
  {
    if("auto".equals(value.toLowerCase()))
      return new AutoDimensionAttribute();
    else
      return null;
  }

  protected DimensionAttribute attemptPercentageAttribute(String value)
  {
    if(PercentageAttributeCompiler.isPercentage(value))
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentageDimensionAttribute(percentValue);
    }
    return null;
  }
}
