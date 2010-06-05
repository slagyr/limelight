//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.DimensionValue;
import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.GreedyDimensionValue;
import limelight.styles.values.StaticDimensionValue;
import limelight.styles.values.AutoDimensionValue;
import limelight.styles.values.PercentageDimensionValue;

public class DimensionAttributeCompiler extends StyleCompiler
{ 
  public StyleValue compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      DimensionValue attribute;
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

  private DimensionValue attemptGreedyAttribute(String value)
  {
    if("greedy".equals(value.toLowerCase()))
      return new GreedyDimensionValue();
    else
      return null;
  }

  protected DimensionValue attemptStaticAttribute(String value)
  {
    int intValue = IntegerAttributeCompiler.convertToInt(value);

    if(intValue >= 0)
      return new StaticDimensionValue(intValue);
    else
      return null;
  }

  private DimensionValue attemptAutoAttribute(String value)
  {
    if("auto".equals(value.toLowerCase()))
      return new AutoDimensionValue();
    else
      return null;
  }

  protected DimensionValue attemptPercentageAttribute(String value)
  {
    if(PercentageAttributeCompiler.isPercentage(value))
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentageDimensionValue(percentValue);
    }
    return null;
  }
}
