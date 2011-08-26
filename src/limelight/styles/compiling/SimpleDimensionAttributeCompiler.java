//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.DimensionValue;

public class SimpleDimensionAttributeCompiler extends DimensionAttributeCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = stringify(objValue);
    try
    {
      DimensionValue attribute;
      attribute = attemptPercentageAttribute(value);
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
}
