//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.styling.SimpleIntegerAttribute;

public class IntegerAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    try
    {
      int intValue;
      if(value instanceof Number)
        intValue = ((Number)value).intValue();
      else
        intValue = convertToInt(value.toString());
      
      return new SimpleIntegerAttribute(intValue);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }

  public static int convertToInt(String value)
  {
    int intValue;
    if(value.indexOf(".") != -1)
      intValue = (int)(Double.parseDouble(value) + 0.5);
    else
      intValue = Integer.parseInt(value);

    return intValue;
  }
}
