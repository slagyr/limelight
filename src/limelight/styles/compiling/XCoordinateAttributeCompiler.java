//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.HorizontalAlignment;
import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.XCoordinateValue;
import limelight.styles.values.AlignedXCoordinateValue;
import limelight.styles.values.PercentageXCoordinateValue;
import limelight.styles.values.StaticXCoordinateValue;

public class XCoordinateAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = stringify(objValue);
    try
    {
      XCoordinateValue attribute;
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

  private XCoordinateValue attemptStaticAttribute(String value)
  {
    int intValue = IntegerAttributeCompiler.convertToInt(value);
    return new StaticXCoordinateValue(intValue);
  }

  private XCoordinateValue attemptAlignedAttribute(String value)
  {
    HorizontalAlignment alignment = HorizontalAlignmentAttributeCompiler.parse(value);
    if(alignment != null)
      return new AlignedXCoordinateValue(alignment);
    else
      return null;
  }

  private XCoordinateValue attemptPercentageAttribute(String value)
  {
    if(PercentageAttributeCompiler.isPercentage(value))
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentageXCoordinateValue(percentValue);
    }
    return null;
  }
}
