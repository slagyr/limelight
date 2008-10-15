package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class IntegerAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    try
    {
      int intValue;
      if(value.indexOf(".") != -1)
        intValue = (int)(Double.parseDouble(value) + 0.5);
      else
        intValue = Integer.parseInt(value);
      return new SimpleIntegerAttribute(intValue);
    }
    catch(NumberFormatException e)
    {
      throw makeError(value);
    }
  }
}
