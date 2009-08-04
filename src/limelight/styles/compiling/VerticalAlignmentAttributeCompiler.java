//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;

public class VerticalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    VerticalAlignment alignment = parse(value);
    if(alignment != null)
      return new SimpleVerticalAlignmentAttribute(alignment);
    else
      throw makeError(value);
  }

  public static VerticalAlignment parse(Object value)
  {
    String lowerCase = value.toString().toLowerCase().trim();
    if("top".equals(lowerCase))
      return VerticalAlignment.TOP;
    else if("center".equals(lowerCase) || "middle".equals(lowerCase))
      return VerticalAlignment.CENTER;
    else if("bottom".equals(lowerCase))
      return VerticalAlignment.BOTTOM;
    else
      return null;
  }
}
