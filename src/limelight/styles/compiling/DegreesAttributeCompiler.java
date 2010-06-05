//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.values.SimpleDegreesValue;

public class DegreesAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      int intValue = Integer.parseInt(value);
      if(0 <= intValue && intValue <= 360)
        return new SimpleDegreesValue(intValue);
      else
        throw makeError(value);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }
}
