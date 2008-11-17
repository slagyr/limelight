package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class IntegerAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    try
    {
      int intValue;
      if(value instanceof String)
        intValue = (int)(Double.parseDouble(value.toString()) + 0.5);
      else if(value instanceof Number)
        intValue = ((Number)value).intValue();
      else
        intValue = Integer.parseInt(value.toString());
      return new SimpleIntegerAttribute(intValue);
    }
    catch(NumberFormatException e)
    {
      throw makeError(value);
    }
  }
}
