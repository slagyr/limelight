//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.HorizontalAlignment;
import limelight.styles.values.SimpleHorizontalAlignmentValue;

public class HorizontalAlignmentAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object value)
  {
    HorizontalAlignment alignment = parse(value);
    if(alignment != null)
      return new SimpleHorizontalAlignmentValue(alignment);
    else
      throw makeError(value);
  }

  public static HorizontalAlignment parse(Object value)
  {
    String lowerCase = value.toString().toLowerCase().trim();
    if("left".equals(lowerCase))
      return HorizontalAlignment.LEFT;
    else if("center".equals(lowerCase) || "middle".equals(lowerCase))
      return HorizontalAlignment.CENTER;
    else if("right".equals(lowerCase))
      return HorizontalAlignment.RIGHT;
    else
      return null;
  }
}
