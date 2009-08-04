package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.XCoordinateAttribute;
import limelight.styles.HorizontalAlignment;

public class XCoordinateAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      XCoordinateAttribute attribute;
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

  private XCoordinateAttribute attemptStaticAttribute(String value)
  {
    int intValue = IntegerAttributeCompiler.convertToInt(value);

    if(intValue >= 0)
      return new StaticXCoordinateAttribute(intValue);
    else
      return null;
  }

  private XCoordinateAttribute attemptAlignedAttribute(String value)
  {
    HorizontalAlignment alignment = HorizontalAlignmentAttributeCompiler.parse(value);
    if(alignment != null)
      return new AlignedXCoordinateAttribute(alignment);
    else
      return null;
  }

  private XCoordinateAttribute attemptPercentageAttribute(String value)
  {
    if(PercentageAttributeCompiler.isPercentage(value))
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentageXCoordinateAttribute(percentValue);
    }
    return null;
  }
}
