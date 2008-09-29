package limelight.styles.styling;

import limelight.styles.StyleAttribute;
import limelight.styles.StyleAttributeCompiler;

public class DegreesAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    try
    {
      int intValue = Integer.parseInt(value);
      if(0 <= intValue && intValue <= 360)
        return new DegreesAttribute(intValue);
      else
        throw makeError(value);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }
}
