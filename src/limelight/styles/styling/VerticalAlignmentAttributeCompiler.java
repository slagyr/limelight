//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.util.Aligner;

public class VerticalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    String lowerCase = value.toString().toLowerCase().trim();
    if("top".equals(lowerCase))
      return new SimpleVerticalAlignmentAttribute(Aligner.TOP);
    else if("center".equals(lowerCase) || "middle".equals(lowerCase))
      return new SimpleVerticalAlignmentAttribute(Aligner.VERTICAL_CENTER);
    else if("bottom".equals(lowerCase))
      return new SimpleVerticalAlignmentAttribute(Aligner.BOTTOM);
    else
      throw makeError(value);

  }
}
