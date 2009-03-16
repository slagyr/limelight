//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

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
