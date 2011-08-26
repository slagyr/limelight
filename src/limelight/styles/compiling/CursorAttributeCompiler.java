//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.SimpleCursorValue;

public class CursorAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object value)
  {
    String name = stringify(value).toLowerCase();
    if("default".equals(name))
      return SimpleCursorValue.DEFAULT;
    else if("hand".equals(name))
      return SimpleCursorValue.HAND;
    else if("text".equals(name))
      return SimpleCursorValue.TEXT;
    else if("crosshair".equals(name))
      return SimpleCursorValue.CROSSHAIR;
    else
      throw new InvalidStyleAttributeError("Invalid value '" + value + "' for Cursor style attribute.");
  }
}
