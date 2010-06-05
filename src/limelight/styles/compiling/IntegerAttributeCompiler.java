//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.SimpleIntegerValue;

public class IntegerAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object value)
  {
    try
    {
      int intValue;
      if(value instanceof Number)
        intValue = ((Number)value).intValue();
      else
        intValue = convertToInt(value.toString());
      
      return new SimpleIntegerValue(intValue);
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
