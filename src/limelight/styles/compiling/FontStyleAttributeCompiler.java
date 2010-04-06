//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.styling.SimpleFontStyleAttribute;

public class FontStyleAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    String lowerCase = value.toString().toLowerCase().trim();

    if("plain".equals(lowerCase))
      return new SimpleFontStyleAttribute("plain");
    else
    {
      String[] tokens = lowerCase.split(" ");
      for(String token : tokens)
      {
        if(!"bold".equals(token) && !"italic".equals(token))
          throw makeError(value);
      }
      return new SimpleFontStyleAttribute(lowerCase);
    }

  }
}
