//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.XCoordinateValue;
import limelight.styles.HorizontalAlignment;
import limelight.styles.values.PercentageXCoordinateValue;
import limelight.styles.values.StaticXCoordinateValue;
import limelight.styles.values.AlignedXCoordinateValue;
import limelight.styles.compiling.HorizontalAlignmentAttributeCompiler;
import limelight.styles.compiling.IntegerAttributeCompiler;
import limelight.styles.compiling.PercentageAttributeCompiler;

public class XCoordinateAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = objValue.toString();
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

//    if(intValue >= 0)
      return new StaticXCoordinateValue(intValue);
//    else
//      return null;
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
