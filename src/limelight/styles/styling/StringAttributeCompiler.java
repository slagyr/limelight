//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StringAttribute;

public class StringAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
     return new StringAttribute(value);
  }
}
