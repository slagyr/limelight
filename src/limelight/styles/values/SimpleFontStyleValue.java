//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.FontStyleValue;

import java.awt.*;

public class SimpleFontStyleValue implements FontStyleValue
{
  private boolean bold;
  private boolean italic;

  public SimpleFontStyleValue(String styling)
  {
    String[] tokens = styling.split(" ");
    for(String token : tokens)
    {
      if("bold".equals(token))
        bold = true;
      else if("italic".equals(token))
        italic = true;
    }
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalic()
  {
    return italic;
  }

  public String toString()
  {
    if(!bold && !italic)
      return "plain";
    else
    {
      StringBuffer buffer = new StringBuffer();
      if(bold)
        buffer.append("bold ");
      if(italic)
        buffer.append("italic ");
      return buffer.toString().trim();
    }
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleFontStyleValue)
    {
      SimpleFontStyleValue other = (SimpleFontStyleValue) obj;
      if(bold != other.bold)
        return false;
      if(italic != other.italic)
        return false;

      return true;
    }
    return false;
  }

  public int toInt()
  {
    int value = 0;
    
    if(bold)
      value |= Font.BOLD;
    if(italic)
      value |= Font.ITALIC;

    return value;
  }
}
