package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class IntegerAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    try
    {
      int intValue = Integer.parseInt(value);
      return new IntegerAttribute(intValue);
    }
    catch(NumberFormatException e)
    {
      throw makeError(value);
    }
  }
}
