//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.SimpleDegreesValue;

public class DegreesAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = stringify(objValue);
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
