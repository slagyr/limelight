//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StyleAttributeCompiler;

public class DegreesAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      int intValue = Integer.parseInt(value);
      if(0 <= intValue && intValue <= 360)
        return new SimpleDegreesAttribute(intValue);
      else
        throw makeError(value);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }
}
