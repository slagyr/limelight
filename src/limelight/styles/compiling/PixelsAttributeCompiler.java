//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.PixelsAttribute;
import limelight.styles.compiling.IntegerAttributeCompiler;
import limelight.styles.compiling.PercentageAttributeCompiler;
import limelight.styles.styling.StaticPixelsAttribute;
import limelight.styles.styling.PercentagePixelsAttribute;

public class PixelsAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      PixelsAttribute attribute = attemptPercentageAttribute(value);
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

  private PixelsAttribute attemptStaticAttribute(String value)
  {
    int intValue = IntegerAttributeCompiler.convertToInt(value);

    if(intValue >= 0)
      return new StaticPixelsAttribute(intValue);
    else
      return null;
  }

  private PixelsAttribute attemptPercentageAttribute(String value)
  {
    if(value.indexOf('%') != -1)
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentagePixelsAttribute(percentValue);
    }
    return null;
  }
}
