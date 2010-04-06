//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.styling.SimpleOnOffAttribute;

public class OnOffAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    String lowerCaseValue = value.toString().toLowerCase();
    if("on".equals(lowerCaseValue) || "true".equals(lowerCaseValue))
      return new SimpleOnOffAttribute(true);
    else if("off".equals(lowerCaseValue) || "false".equals(lowerCaseValue))
      return new SimpleOnOffAttribute(false);
    else
      throw makeError(value);
  }
}
