//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.YCoordinateValue;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.AlignedYCoordinateValue;
import limelight.styles.values.PercentageYCoordinateValue;
import limelight.styles.values.StaticYCoordinateValue;

public class YCoordinateAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      YCoordinateValue attribute;
      attribute = attemptAlignedAttribute(value);
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

  private YCoordinateValue attemptStaticAttribute(String value)
  {
    return new StaticYCoordinateValue(IntegerAttributeCompiler.convertToInt(value));
  }

  private YCoordinateValue attemptAlignedAttribute(String value)
  {
    VerticalAlignment alignment = VerticalAlignmentAttributeCompiler.parse(value);
    if(alignment != null)
      return new AlignedYCoordinateValue(alignment);
    else
      return null;
  }

  private YCoordinateValue attemptPercentageAttribute(String value)
  {
    if(PercentageAttributeCompiler.isPercentage(value))
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentageYCoordinateValue(percentValue);
    }
    return null;
  }
}
