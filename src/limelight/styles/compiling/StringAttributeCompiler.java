//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.StringValue;

public class StringAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object value)
  {
     return new StringValue(value);
  }
}
